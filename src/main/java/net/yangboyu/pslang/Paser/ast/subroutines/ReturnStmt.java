package net.yangboyu.pslang.Paser.ast.subroutines;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.ast.Stmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class ReturnStmt extends Stmt {

    public ReturnStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.RETURN_STMT, "return");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("RETURN");

        var returnStmt = new ReturnStmt(parent);

        returnStmt.setLexeme(lexeme);

        var expr = Expr.parse(parent, it);

        if(expr != null) {
            returnStmt.addChild(expr);
        }

        return returnStmt;
    }
}
