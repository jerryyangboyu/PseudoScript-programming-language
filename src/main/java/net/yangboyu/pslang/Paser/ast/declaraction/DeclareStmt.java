package net.yangboyu.pslang.Paser.ast.declaraction;

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
        var type = it.nextMatch(TokenType.KEYWORD);
        if (type.isPrimaryType()) {
            factor.setTypeLexeme(type);
        } else if (type.isType()) {
            // other types TODO
        } else {
            throw new ParseException(type);
        }

        return stmt;
    }
}
