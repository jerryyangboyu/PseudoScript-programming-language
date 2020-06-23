package net.yangboyu.pslang.Paser.ast.subroutines;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
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

        while(it.hasNext() && it.peek().isVariable()) {
            var variable = (Variable) Factor.parse(it, ASTNodeTypes.VARIABLE);
            it.nextMatch(":");
            var type = it.next();
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
