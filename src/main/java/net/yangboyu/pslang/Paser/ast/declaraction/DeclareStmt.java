package net.yangboyu.pslang.Paser.ast.declaraction;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class DeclareStmt extends Stmt {

    public DeclareStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.DECLARE_STMT, "declare");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var stmt = new DeclareStmt(parent);
        var lexeme = it.nextMatch("DECLARE");
        stmt.setLexeme(lexeme);

        var factor = (Variable) Factor.parse(it, ASTNodeTypes.VARIABLE);
        stmt.addChild(factor);

        it.nextMatch(":");

        // primary data type support
        Token typeKeyword = it.next();
        if (typeKeyword.isPrimaryType()) {
            factor.setTypeLexeme(typeKeyword);
        } else if (typeKeyword.isVariable() || typeKeyword.isType()) {
            // TODO
            // 兼容非系统类型，包括预定义类型比如DATE
            factor.setTypeLexeme(new Token(TokenType.TYPE, typeKeyword.getValue()));
        } else {
            throw new ParseException(typeKeyword);
        }

        return stmt;
    }
}
