package net.yangboyu.pslang.Paser.ast.subroutines;

import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class FunctionDeclareStmt extends Stmt {

    public FunctionDeclareStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.FUNCTION_DECLARE_STMT, "func");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        it.nextMatch("func");

        var func = new FunctionDeclareStmt(parent);

        var lexeme  = it.peek(); // func fibonacci

        var functionVariable = (Variable) Factor.parse(it);

        func.setLexeme(lexeme);
        func.addChild(functionVariable);

        it.nextMatch("(");
        var args = FunctionArgs.parse(parent, it);
        it.nextMatch(")");
        func.addChild(args);

        var keyword = it.nextMatch(TokenType.KEYWORD);
        if(!keyword.isType()) {
            throw new ParseException(keyword);
        }
        assert functionVariable != null;
        functionVariable.setTypeLexeme(keyword);

        var block = Block.parse(parent, it);
        func.addChild(block);

        return func;
    }

    public ASTNode getArgs() throws ParseException {
        return this.getChild(1);
    }

    public Variable getFunctionVariable() throws ParseException {
        return (Variable)this.getChild(0);
    }

    public String getFunctionType() throws ParseException {
        return this.getFunctionVariable().getTypeLexeme().getValue();
    }

    public Block getBlock() throws ParseException {
        return (Block)this.getChild(2);
    }
}
