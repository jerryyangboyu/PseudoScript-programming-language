package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Lexer.Token;

public class Variable extends Factor {

    private Token typeLexeme = null;
    public Variable(Token token) {
        super(token);
        this.type = ASTNodeTypes.VARIABLE;
    }

    public void setTypeLexeme(Token type) {
        this.typeLexeme = type;
    }

    public Token getTypeLexeme() {
        return this.typeLexeme;
    }
}
