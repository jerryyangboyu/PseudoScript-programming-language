package net.yangboyu.pslang.Paser.util;

import net.yangboyu.pslang.Common.PeekIterator;
import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;

import java.util.stream.Stream;

public class PeekTokenIterator extends PeekIterator<Token> {


    public PeekTokenIterator(Stream<Token> stream) {
        super(stream);
    }

    public Token nextMatch(String value) throws ParseException {
        if(!this.hasNext()) {
            throw new ParseException("Lexical Error: Early EOF");
        }

        var token = this.next();

        if (!token.getValue().equals(value)) {
            throw new ParseException(token);
        }
        return token;
    }

    public Token nextMatch(TokenType _type) throws ParseException {
        if(!this.hasNext()) {
            throw new ParseException("Lexical Error: Early EOF");
        }

        var token = this.next();
        if (!token.getType().equals(_type)) {
            throw new ParseException(token);
        }
        return token;
    }
}
