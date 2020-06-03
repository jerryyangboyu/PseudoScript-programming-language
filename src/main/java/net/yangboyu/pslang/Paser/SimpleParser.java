package net.yangboyu.pslang.Paser;

import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class SimpleParser {
    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        var expr = new Expr(null);
        var scalar = Factor.parse(it);
//        System.out.println(it.peek());
        if (!it.hasNext() || it.peek() == null) {
            return scalar;
        }

        expr.setLexeme(it.peek());
        it.nextMatch("+");
        expr.setLabel("+");
        expr.addChild(scalar);
        expr.setType(ASTNodeTypes.BINARY_EXPR);
        var rightNode = parse(it);
        expr.addChild(rightNode);
        return expr;
    }
}
