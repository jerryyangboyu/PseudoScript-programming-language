package net.yangboyu.pslang.Paser.ast.expressions;

import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class AssignStmt extends Stmt {

    public AssignStmt() {
        super(ASTNodeTypes.ASSIGN_STMT, "assign");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        var stmt = new AssignStmt();

        var factor = (Variable) Factor.parse(it, ASTNodeTypes.VARIABLE);

        stmt.addChild(factor);

        var lexeme = it.nextMatch("<-");

        var expr = Expr.parse(it);

        stmt.addChild(expr);

        stmt.setLexeme(lexeme);

        return stmt;
    }
}
