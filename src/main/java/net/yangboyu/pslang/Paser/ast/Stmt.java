package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Paser.ast.declaraction.DeclareStmt;
import net.yangboyu.pslang.Paser.ast.expressions.AssignStmt;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.ast.selection.IfStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.FunctionDeclareStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.ReturnStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public abstract class Stmt extends ASTNode {

    public Stmt(ASTNode _parent, ASTNodeTypes _type, String _label) {
        super(_parent, _type, _label);
    }

    public static ASTNode parseStmt(ASTNode parent, PeekTokenIterator it) throws ParseException {
        if(!it.hasNext()) {
            return null;
        }

        var token = it.next();
        var lookahead = it.peek();
        it.putBack();

        if(token.isVariable() && lookahead.getValue().equals("=")) {
            return AssignStmt.parse(parent, it);
        }else if(token.getValue().equals("auto") && lookahead.isVariable()){
            return DeclareStmt.parse(parent, it);
        }else if(token.getValue().equals("func")) {
            return FunctionDeclareStmt.parse(parent, it);
        }else if(token.getValue().equals("return")) {
            return ReturnStmt.parse(parent, it);
        }else if(token.getValue().equals("if")) {
            return IfStmt.parse(parent, it);
        }else if(token.getValue().equals("{")) {
            return Block.parse(parent, it);
        }else{
            ASTNode expr = Expr.parse(parent, it);


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
