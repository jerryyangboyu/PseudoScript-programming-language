package net.yangboyu.pslang.Translator;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
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
        }
        throw new NotImplementedException("net.yangboyu.pslang.Translator not implemented for " + node.getType());
    }

    private void translateBlock(TAProgram program, ASTNode node, SymbolTable parent) throws ParseException {
        var symbolTable = new SymbolTable();
        parent.addChild(symbolTable);

        var parentOffset = symbolTable.createVariable();
        parentOffset.setLexeme(new Token(TokenType.INTEGER, parent.localSize() + ""));

        var pushRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(pushRecord);

        for (var stmt: node.getChildren()) {
            translateStmt(program, stmt, symbolTable);
        }

        var popRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(popRecord);

        // 栈指针从高位望底位走，所以是负的
        pushRecord.setArg1(-parent.localSize());
        popRecord.setArg1(parent.localSize());
    }

    public void translateAssignStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        Symbol assignedSymbol = symbolTable.createSymbolByLexeme(node.getChild(0).getLexeme());
        ASTNode expr = node.getChild(1);
        Symbol addr = translateExpr(program, expr, symbolTable);
        program.add(new TAInstruction(TAInstructionType.ASSIGN, assignedSymbol, "=", addr, null));
    }


    // SDD
    //  E -> E1 op E2
    //  E -> F
    public Symbol translateExpr(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        if (node.isValueType()) {
            Symbol addr = symbolTable.createSymbolByLexeme(node.getLexeme());
            node.setProp("addr", addr);
            return addr;
        } else if(node.getType() == ASTNodeTypes.CALL_EXPR) {
            throw new NotImplementedException("No implementation for call expr");
        }

        // 遍历他所有的子树并且建立临时变量
        for (var child : node.getChildren()) {
            translateExpr(program, child, symbolTable);
        }

        // 如果中间节点没有加上标签，那么创建中间节点的变量
        if (node.getProp("addr") == null) {
            node.setProp("addr", symbolTable.createVariable());
        }


        var instruction = new TAInstruction(TAInstructionType.ASSIGN,
                (Symbol)node.getProp("addr"),
                node.getLexeme().getValue(),
                (Symbol)node.getChild(0).getProp("addr"),
                (Symbol)node.getChild(1).getProp("addr"));

        program.add(instruction);

        return instruction.getResult();
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
