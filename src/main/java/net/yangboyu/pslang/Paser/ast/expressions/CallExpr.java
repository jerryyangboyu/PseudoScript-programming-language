package net.yangboyu.pslang.Paser.ast.expressions;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class CallExpr extends Expr {
    public CallExpr(ASTNode _parent) {
        super(_parent);
        this.label = "call";
        this.type = ASTNodeTypes.CALL_EXPR;
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it, ASTNode factor) throws ParseException {
        var expr = new CallExpr(parent);
        expr.addChild(factor);

        it.nextMatch("(");

        ASTNode arg = null;
        while((arg = Expr.parse(parent, it)) != null) {
            expr.addChild(arg);

            if (!it.peek().getValue().equals(")")) {
                it.nextMatch(",");
                if(it.peek().getValue().equals(")")) {
                    throw new ParseException("Syntax Error: parameter expected, get none");
                }
            }
        }

        it.nextMatch(")");

        return expr;
    }
}
