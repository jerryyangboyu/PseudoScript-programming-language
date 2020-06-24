package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Paser.ast.declaraction.DeclareStmt;
import net.yangboyu.pslang.Paser.ast.expressions.AssignStmt;
import net.yangboyu.pslang.Paser.ast.expressions.CallExpr;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.ast.io.InputStmt;
import net.yangboyu.pslang.Paser.ast.io.OutputStmt;
import net.yangboyu.pslang.Paser.ast.selection.IfStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.FunctionDeclareStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.ReturnStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public abstract class Stmt extends ASTNode {

    public Stmt(ASTNodeTypes _type, String _label) {
        super( _type, _label);
    }

    public static ASTNode parseStmt(PeekTokenIterator it) throws ParseException {
        if(!it.hasNext()) {
            return null;
        }
        var token = it.next();
        var lookahead = it.peek();
        if (lookahead == null) {
            it.putBack();
            return null;
        }
        it.putBack();

        // make sure the token and lookahead are both not null pointer and put back to the origin place

        if(token.isVariable() && lookahead.getValue().equals("<-")) {
            return AssignStmt.parse(it);
        }else if (token.getValue().equals("DECLARE") && lookahead.isVariable()){
            return DeclareStmt.parse(it);
        }else if (token.getValue().equals("CALL")) {
            return CallExpr.parse(it);
        }else if (token.getValue().equals("FUNCTION")) {
            return FunctionDeclareStmt.parse(it);
        }else if (token.getValue().equals("RETURN")) {
            return ReturnStmt.parse(it);
        }else if (token.getValue().equals("IF")) {
            return IfStmt.parse(it);
        }else if (token.getValue().equals("OUTPUT")) {
            return OutputStmt.parse(it);
        }else if (token.getValue().equals("INPUT")) {
            return InputStmt.parse(it);
        } else {
            ASTNode expr = Expr.parse(it);


                // TODO deal with the last test of TestStmt
//            if (expr == null) {
//                throw new NotImplementedException(String.format("[dev branch warning] Token %s not impl, " +
//                        "This is an warning for beta version compiler. Mostly this exception would be happen " +
//                        "as class Expr cannot handle the token.", token.getValue()));
//            }
            return expr;

        }

    }
}
