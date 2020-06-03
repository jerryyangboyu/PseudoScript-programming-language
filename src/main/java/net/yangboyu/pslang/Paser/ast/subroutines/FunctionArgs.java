package net.yangboyu.pslang.Paser.ast.subroutines;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.Factor;
import net.yangboyu.pslang.Paser.ast.Variable;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class FunctionArgs extends ASTNode {
    public FunctionArgs(ASTNode _parent) {
        super(_parent);
        this.label = "args";
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {

        var args = new FunctionArgs(parent);

        while(it.peek().isType()) {
            var type = it.next();
            var variable = (Variable) Factor.parse(it);

            assert variable != null;
            variable.setTypeLexeme(type);

            args.addChild(variable);

            if(!it.peek().getValue().equals(")")) {
                it.nextMatch(",");
                if(it.peek().getValue().equals(")")) {
                    throw new ParseException("Syntax Error: argument expected, get none");
                }
            }
        }

        return args;
    }
}
