package net.yangboyu.pslang.Lexer;

public class LexicalException extends Exception {
    private String _msg;

    public LexicalException(String msg){
        this._msg = msg;
    }

    public LexicalException(char msg){
        this._msg = String.format("Unexpected character %s", msg);
    }

    @Override
    public String getMessage() {
        return this._msg;
    }
}
