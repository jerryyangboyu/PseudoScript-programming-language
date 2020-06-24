package net.yangboyu.pslang.Paser.ast.expressions;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Factor;
import net.yangboyu.pslang.Paser.ast.Variable;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class CallExpr extends Expr {
    public CallExpr() {
        super();
        this.label = "call";
        this.type = ASTNodeTypes.CALL_EXPR;
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        var expr = new CallExpr();
        it.nextMatch("CALL");

        var factor = (Variable) Factor.parse(it, ASTNodeTypes.VARIABLE);
        expr.addChild(factor);

        if (it.hasNext() && it.peek().getValue().equals("(")) {
            it.nextMatch("(");
            ASTNode arg = null;
            while((arg = Expr.parse(it)) != null) {
                expr.addChild(arg);
                if (!it.peek().getValue().equals(")")) {
                    it.nextMatch(",");
                    if(it.peek().getValue().equals(")")) {
                        throw new ParseException("Syntax Error: parameter expected, get none");
                    }
                }
            }
            it.nextMatch(")");
        }

        return expr;
    }

    public static ASTNode parse(ASTNode factor, PeekTokenIterator it) throws ParseException {
        var expr = new CallExpr();
        expr.addChild(factor);

        if (it.hasNext() && it.peek().getValue().equals("(")) {
            it.nextMatch("(");
            ASTNode arg = null;
            while((arg = Expr.parse(it)) != null) {
                expr.addChild(arg);
                if (!it.peek().getValue().equals(")")) {
                    it.nextMatch(",");
                    if(it.peek().getValue().equals(")")) {
                        throw new ParseException("Syntax Error: parameter expected, get none");
                    }
                }
            }
            it.nextMatch(")");
        }

        return expr;
    }
}
