package net.yangboyu.pslang.Paser.ast.expressions;

import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class AssignStmt extends Stmt {

    public AssignStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.ASSIGN_STMT, "assign");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var stmt = new AssignStmt(parent);

        var tkn = it.peek();
        var factor = Factor.parse(it);

        if(factor == null) {
            throw new ParseException(tkn);
        }

        stmt.addChild(factor);

        var lexeme = it.nextMatch("<-");

        var expr = Expr.parse(stmt, it);

        stmt.addChild(expr);

        stmt.setLexeme(lexeme);

        return stmt;
    }
}
