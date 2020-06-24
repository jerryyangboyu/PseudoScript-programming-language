package net.yangboyu.pslang.Paser.ast.subroutines;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.ast.Stmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class ReturnStmt extends Stmt {

    public ReturnStmt() {
        super(ASTNodeTypes.RETURN_STMT, "return");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("RETURN");

        var returnStmt = new ReturnStmt();

        returnStmt.setLexeme(lexeme);

        var expr = Expr.parse(it);

        if(expr != null) {
            returnStmt.addChild(expr);
        }

        return returnStmt;
    }
}
