package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;


public class Factor extends ASTNode {

    public Factor(Token token) {
        super();
        this.lexeme = token;
        this.label = token.getValue();
    }

    public static ASTNode parse(PeekTokenIterator it) {
        var token = it.peek();

        if (token.isVariable()){
            it.next();
            return new Variable(token);
        } else if (token.isScalar()) {
            it.next();
            return new Scalar(token);
        }

        return null;
    }
}
