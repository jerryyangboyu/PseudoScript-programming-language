package net.yangboyu.pslang.Translator;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Variable;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
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
                return;
            case CALL_EXPR:
                translateCallExpr(program, node, symbolTable);
                return;
            case RETURN_STMT:
                translateReturnStmt(program, node, symbolTable);
                return;
        }
        throw new NotImplementedException("net.yangboyu.pslang.Translator not implemented for " + node.getType());
    }

    private void translateReturnStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        Symbol resultVal = null;
        // return 返回值可能为空
        if (node.getChild(0) != null) {
            resultVal = translateExpr(program, node.getChild(0), symbolTable);
        }
        program.add(new TAInstruction(TAInstructionType.RETURN, null, null, resultVal, null));
    }

    private Symbol translateCallExpr(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        // 提取函数名称
        var factor = node.getChild(0);

        // 创建返回值临时变量
        var returnValue = symbolTable.createVariable();
        // 返回地址
        symbolTable.createVariable();

        // arg从第二个元素开始
        for (int i = 1; i < node.getChildren().size(); i++) {
            var expr = node.getChild(i);
            var addr = translateExpr(program, expr, symbolTable);
            program.add(new TAInstruction(TAInstructionType.PARAM, null, null, addr,i - 1));
        }

        // 通过 lexeme 找到 func 的 label
        var funcAddr = symbolTable.cloneFromSymbolTree(factor.getLexeme(), 0);

        if (funcAddr == null) {
            throw new ParseException("Syntax Error, procedure or function name \"" + factor.getLexeme().getValue() + "\" is not defined");
        }

        program.add(new TAInstruction(TAInstructionType.SP, null, null, -symbolTable.localSize(), null));
        program.add(new TAInstruction(TAInstructionType.CALL, null, null, funcAddr, null));
        program.add(new TAInstruction(TAInstructionType.SP, null, null, symbolTable.localSize(), null));

        return returnValue;
    }

    private void translateFunctionDeclareStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        var label = program.addLabel();
        var func = (FunctionDeclareStmt)node;

        var funcSymbolTable = new SymbolTable();

        program.add(new TAInstruction(TAInstructionType.FUNC_BEGIN, null, null, null, null));
        // 函数调用的时候关联
        symbolTable.createLabel((String)label.getArg1(), node.getLexeme());
        // 关联函数名
        label.setArg2(node.getLexeme().getValue());
        symbolTable.addChild(funcSymbolTable);

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

        // 执行if主体翻译
        translateBlock(program, ifStmt.getBlock(), symbolTable);

        // if(expr) {...} else if {...}
        TAInstruction gotoInstruction = null;
        if (node.getChild(2) != null) {
            // if分支结束，直接跳到结尾，注意在这个大else分支前已经执行了if block的翻译
            gotoInstruction = new TAInstruction(TAInstructionType.GOTO, null, null, null, null);
            program.add(gotoInstruction);
            // 创造一个label调转到if主分支结束，而不是最后
            var labelEndIf = program.addLabel();
            ifInstruction.setArg2(labelEndIf.getArg1());

            // 分为两种情况，后面还有if，或者后面只有else
            if (ifStmt.getElseBlock() != null) {
                // 如果后面是else-block的话
                translateBlock(program, ifStmt.getElseBlock(), symbolTable);
            } else if (ifStmt.getElseIfStmt() != null) {
                // 如果后面是else-if-block的话
                translateIfStmt(program, ifStmt.getElseIfStmt(), symbolTable);
            }
        }

        var labelEnd = program.addLabel();
        // 标记以下位置，等if条件不成立跳转，再没有else的情况下
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

        // 相对于父级作用域的偏移量
        // 这个变量的作用是为了占位，表示上一级的block
        var parentOffset = symbolTable.createVariable();
        parentOffset.setLexeme(new Token(TokenType.INTEGER, parent.localSize() + ""));

        // 压栈活动记录
        var pushRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(pushRecord);

        for (var stmt: node.getChildren()) {
            translateStmt(program, stmt, symbolTable);
        }

        // 出栈
        var popRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(popRecord);

        // 栈指针从高位望底位走，所以是负的
        pushRecord.setArg1(-parent.localSize());
        popRecord.setArg1(parent.localSize());
    }

    public void translateAssignStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        Symbol assignedSymbol = symbolTable.cloneFromSymbolTree(node.getChild(0).getLexeme(), 0);
        if (assignedSymbol == null) {
            throw new ParseException("Syntax Error, Identifier \"" + node.getChild(0).getLexeme().getValue() + "\" is not defined");
        }
        ASTNode expr = node.getChild(1);
        Symbol addr = translateExpr(program, expr, symbolTable);

        // TODO
        // still get some bugs
        // 对于自动生成的token有些没有typeLexeme
//        System.out.println(addr.getTypeLexeme());
//        System.out.println(addr.getLexeme().getType());
//        System.out.println(assignedSymbol.getTypeLexeme());
//        Utils.assertEqualType(assignedSymbol, addr);

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
            var addr = translateCallExpr(program, node, symbolTable);
            node.setProp("addr", addr);
            return addr;
        } else if(node instanceof Expr) {

            // 遍历他所有的子树并且建立临时变量
            for (var child : node.getChildren()) {
                translateExpr(program, child, symbolTable);
            }

            // 没得可遍历了，到这里时single节点
            // 如果中间节点没有加上标签，那么创建中间节点的变量
            if (node.getProp("addr") == null) {
                node.setProp("addr", symbolTable.createVariable());
            }

            Symbol parent = (Symbol)node.getProp("addr");
            Symbol child1 = (Symbol)node.getChild(0).getProp("addr");
            Symbol child2 = (Symbol)node.getChild(1).getProp("addr");

            var instruction = new TAInstruction(TAInstructionType.ASSIGN, parent, node.getLexeme().getValue(), child1, child2);

            // TODO
            // still get some bugs
            // 对于自动生成的token有些没有typeLexeme
//            parent.setTypeLexeme(Utils.getTypeDerivation(child1, child2));
//            program.add(instruction);

            return instruction.getResult();
        }

        String debugInfo = " [DEBUG INFO] TokenName: " + node.getLexeme().getValue() + "Node: " + node;
        throw new NotImplementedException("Unexpected token type: " + node.getType() + debugInfo);
    }

    public void translateDeclareStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParseException {
        Token lexeme = node.getChild(0).getLexeme();
        Token typeLexeme = ((Variable)node.getChild(0)).getTypeLexeme();
        if (symbolTable.exists(lexeme)) {
            throw new ParseException("Syntax Error, Identifier \"" + lexeme.getValue() + "\" is already defined");
        }
        var assignedSymbol = symbolTable.createSymbolByLexeme(lexeme);
        assignedSymbol.setTypeLexeme(typeLexeme);
    }
}
