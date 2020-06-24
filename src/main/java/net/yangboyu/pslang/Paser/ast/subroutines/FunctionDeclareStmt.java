package net.yangboyu.pslang.Paser.ast.subroutines;

import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class FunctionDeclareStmt extends Stmt {

    public FunctionDeclareStmt() {
        super(ASTNodeTypes.FUNCTION_DECLARE_STMT, "func");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        it.nextMatch("FUNCTION");

        var func = new FunctionDeclareStmt();

        // lexeme is function variable
        var lexeme  = it.peek(); // func fibonacci
        var functionVariable = (Variable) Factor.parse(it, ASTNodeTypes.VARIABLE);
        func.setLexeme(lexeme);
        func.addChild(functionVariable);

        // parse arguments
        it.nextMatch("(");
        var args = FunctionArgs.parse(it);
        it.nextMatch(")");
        func.addChild(args);

        // parse return data type
        it.nextMatch("RETURNS");
        var keyword = it.next();
        // It can return any data type include user defined data type and inbuilt data type
        if(!keyword.isType()) {
            throw new ParseException(keyword);
        }
        functionVariable.setTypeLexeme(keyword);

        // parse function body
        var block = Block.parse(it);
        func.addChild(block);

        it.nextMatch("ENDFUNCTION");

        return func;
    }

    public ASTNode getArgs() {
        return this.getChild(1);
    }

    public Variable getFunctionVariable() {
        return (Variable)this.getChild(0);
    }

    public String getFunctionType() {
        return this.getFunctionVariable().getTypeLexeme().getValue();
    }

    public Block getBlock() {
        return (Block)this.getChild(2);
    }
}
