package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Paser.util.ParseException;
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

    public static ASTNode parse(PeekTokenIterator it, ASTNodeTypes type) throws ParseException {
        var token = it.peek();
        Factor factor = null;

        if (token.isVariable()){
            it.next();
            factor = new Variable(token);
        } else if (token.isScalar()) {
            it.next();
            factor =  new Scalar(token);
        }

        if (factor != null && factor.getType() == type) {
            return factor;
        } else if (factor == null) {
            throw new ParseException(String.format("Unexpected token type %s", type));
        } else {
            throw new ParseException(String.format("Unexpected token type %s, it should be type %s.", factor.getType(), type));
        }
    }
}
