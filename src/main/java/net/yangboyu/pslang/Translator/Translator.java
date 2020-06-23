package net.yangboyu.pslang.Translator;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.selection.IfStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.FunctionDeclareStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Translator.symbol.Symbol;
import net.yangboyu.pslang.Translator.symbol.SymbolTable;
import org.apache.commons.lang3.NotImplementedException;

public class Translator {
    public TAProgram translate(ASTNode astNode) throws ParseException {
        var program = new TAProgram();
        var symbolTable = new SymbolTable();

        for(var child : astNode.getChildren()) {
            translateStmt(program, child, symbolTable);
        }

        return program;
    }

    public void translateStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {

        switch (node.getType()) {
            case ASSIGN_STMT:
                translateAssignStmt(program, node, symbolTable);
                return;
            case DECLARE_STMT:
                translateDeclareStmt(program, node, symbolTable);
                return;
            case BLOCK:
                translateBlock(program, node, symbolTable);
                return;
            case IF_STMT:
                translateIfStmt(program, node, symbolTable);
                return;
            case FUNCTION_DECLARE_STMT:
                translateFunctionDeclareStmt(program, node, symbolTable);
            case CALL_EXPR:
                translateCallExpr(program, node, symbolTable);
        }
        throw new NotImplementedException("net.yangboyu.pslang.Translator not implemented for " + node.getType());
    }

    private void translateCallExpr(TAProgram program, ASTNode node, SymbolTable symbolTable) {
        // ��ȡ��������
        var factor = node.getChild(0);

        // ��������ֵ��ʱ����
        var returnValue = symbolTable.createVariable();
        // ���ص�ַ
        symbolTable.createVariable();

        // arg�ӵڶ���Ԫ�ؿ�ʼ
        for (int i = 1; i < node.getChildren().size(); i++) {
            var expr = node.getChild(i);
            var addr = translateExpr(program, expr, symbolTable);
            program.add(new TAInstruction(TAInstructionType.PARAM, null, null, addr,i - 1));
        }

        // ͨ�� lexeme �ҵ� func �� label
        var funcAddr = symbolTable.cloneFromSymbolTree(factor.getLexeme(), 0);

        program.add(new TAInstruction(TAInstructionType.SP, null, null, -symbolTable.localSize(), null));
        program.add(new TAInstruction(TAInstructionType.CALL, null, null, funcAddr, null));
        program.add(new TAInstruction(TAInstructionType.SP, null, null, symbolTable.localSize(), null));

    }

    private void translateFunctionDeclareStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        var label = program.addLabel();
        var func = (FunctionDeclareStmt)node;

        var funcSymbolTable = new SymbolTable();
        // �������õ�ʱ�����
        funcSymbolTable.createLabel((String)label.getArg1(), node.getLexeme());
        // ����������
        label.setArg2(node.getLexeme().getValue());

        for (var arg: func.getArgs().getChildren()) {
            funcSymbolTable.createSymbolByLexeme(arg.getLexeme());
        }

        for (var expr: func.getBlock().getChildren()) {
            translateStmt(program, expr, funcSymbolTable);
        }
    }

    private void translateIfStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        var ifStmt = (IfStmt)node;
        var exprAddr = translateExpr(program, ifStmt.getExpr(), symbolTable);
        var ifInstruction = new TAInstruction(TAInstructionType.IF, null, null, exprAddr, null);
        program.add(ifInstruction);

        // ִ��if���巭��
        translateBlock(program, ifStmt.getBlock(), symbolTable);

        // if(expr) {...} else if {...}
        TAInstruction gotoInstruction = null;
        if (node.getChild(2) != null) {
            // if��֧������ֱ��������β��ע���������else��֧ǰ�Ѿ�ִ����if block�ķ���
            gotoInstruction = new TAInstruction(TAInstructionType.GOTO, null, null, null, null);
            program.add(gotoInstruction);
            // ����һ��label��ת��if����֧���������������
            var labelEndIf = program.addLabel();
            ifInstruction.setArg2(labelEndIf.getArg1());

            // ��Ϊ������������滹��if�����ߺ���ֻ��else
            if (ifStmt.getElseBlock() != null) {
                // ���������else-block�Ļ�
                translateBlock(program, ifStmt.getElseBlock(), symbolTable);
            } else if (ifStmt.getElseIfStmt() != null) {
                // ���������else-if-block�Ļ�
                translateIfStmt(program, ifStmt.getElseIfStmt(), symbolTable);
            }
        }

        var labelEnd = program.addLabel();
        // �������λ�ã���if������������ת����û��else�������
        if (node.getChild(2) == null) {
            ifInstruction.setArg2(labelEnd.getArg1());
        } else {
            assert gotoInstruction != null;
            gotoInstruction.setArg1(labelEnd.getArg1());
        }
        //end
    }

    private void translateBlock(TAProgram program, ASTNode node, SymbolTable parent) throws ParseException {
        var symbolTable = new SymbolTable();
        parent.addChild(symbolTable);

        // ����ڸ����������ƫ����
        // ���������������Ϊ��ռλ����ʾ��һ����block
        var parentOffset = symbolTable.createVariable();
        parentOffset.setLexeme(new Token(TokenType.INTEGER, parent.localSize() + ""));

        // ѹջ���¼
        var pushRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(pushRecord);

        for (var stmt: node.getChildren()) {
            translateStmt(program, stmt, symbolTable);
        }

        // ��ջ
        var popRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(popRecord);

        // ջָ��Ӹ�λ����λ�ߣ������Ǹ���
        pushRecord.setArg1(-parent.localSize());
        popRecord.setArg1(parent.localSize());
    }

    public void translateAssignStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) {
        Symbol assignedSymbol = symbolTable.createSymbolByLexeme(node.getChild(0).getLexeme());
        ASTNode expr = node.getChild(1);
        Symbol addr = translateExpr(program, expr, symbolTable);
        program.add(new TAInstruction(TAInstructionType.ASSIGN, assignedSymbol, "=", addr, null));
    }

    // SDD
    //  E -> E1 op E2
    //  E -> F
    public Symbol translateExpr(TAProgram program, ASTNode node, SymbolTable symbolTable) {
        if (node.isValueType()) {
            Symbol addr = symbolTable.createSymbolByLexeme(node.getLexeme());
            node.setProp("addr", addr);
            return addr;
        } else if(node.getType() == ASTNodeTypes.CALL_EXPR) {
            throw new NotImplementedException("No implementation for call expr");
        }

        // ���������е��������ҽ�����ʱ����
        for (var child : node.getChildren()) {
            translateExpr(program, child, symbolTable);
        }

        // ����м�ڵ�û�м��ϱ�ǩ����ô�����м�ڵ�ı���
        if (node.getProp("addr") == null) {
            node.setProp("addr", symbolTable.createVariable());
        }

        checkDataType(node);

        var instruction = new TAInstruction(TAInstructionType.ASSIGN,
                (Symbol)node.getProp("addr"),
                node.getLexeme().getValue(),
                (Symbol)node.getChild(0).getProp("addr"),
                (Symbol)node.getChild(1).getProp("addr"));

        program.add(instruction);

        return instruction.getResult();
    }

    public static void checkDataType(ASTNode node) {
        // TODO
    }

    public void translateDeclareStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        Token lexeme = node.getChild(0).getLexeme();
        if (symbolTable.exists(lexeme)) {
            throw new ParseException("Syntax Error, Identifier " + lexeme.getValue() + "is already defined");
        }

        var assignedSymbol = symbolTable.createSymbolByLexeme(lexeme);
        var expr = node.getChild(1);
        var addr = translateExpr(program, expr, symbolTable);
        program.add(new TAInstruction(TAInstructionType.ASSIGN, assignedSymbol, "=", addr, null));
    }
}
