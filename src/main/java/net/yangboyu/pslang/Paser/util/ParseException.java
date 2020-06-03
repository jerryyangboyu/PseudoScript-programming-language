package net.yangboyu.pslang.Paser.util;

import net.yangboyu.pslang.Lexer.Token;

public class ParseException extends Exception{
    private String msg;

    public ParseException(String _msg){
        this.msg = _msg;
    }

    public ParseException(Token _token){
        this.msg = String.format("Syntax Error: Unexpected token %s with type %s", _token.getValue(), _token.getType());
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
