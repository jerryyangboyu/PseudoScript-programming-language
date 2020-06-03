package net.yangboyu.pslang.Paser.ast.declaraction;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Factor;
import net.yangboyu.pslang.Paser.ast.Stmt;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class DeclareStmt extends Stmt {

    public DeclareStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.DECLARE_STMT, "declare");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var stmt = new DeclareStmt(parent);
        it.nextMatch("auto");
        var tkn = it.peek();
        var factor = Factor.parse(it);

        if(factor == null) {
            throw new ParseException(tkn);
        }

        stmt.addChild(factor);

        var lexeme = it.nextMatch("=");

        var expr = Expr.parse(stmt, it);

        stmt.addChild(expr);

        stmt.setLexeme(lexeme);

        return stmt;
    }
}
