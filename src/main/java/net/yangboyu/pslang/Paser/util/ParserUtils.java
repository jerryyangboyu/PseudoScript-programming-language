package net.yangboyu.pslang.Paser.util;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import jdk.jshell.spi.ExecutionControl;

public class ParserUtils {
    public static String toPostfixExpression(ASTNode node) throws ExecutionControl.NotImplementedException {
        String leftStr = "";
        String rightStr = "";
        switch (node.getType()) {
            case BINARY_EXPR:
                leftStr = toPostfixExpression(node.getChild(0));
                rightStr = toPostfixExpression(node.getChild(1));
                return leftStr + " " + rightStr + " " + node.getLexeme().getValue();
            case VARIABLE:
            case SCALAR:
                return node.getLexeme().getValue();
        }

        throw new ExecutionControl.NotImplementedException(".not implemented");
    }
}
