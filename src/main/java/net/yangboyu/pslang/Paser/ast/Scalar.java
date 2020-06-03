package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Lexer.Token;

public class Scalar extends Factor {

    public Scalar(Token token) {
        super(token);
        this.type = ASTNodeTypes.SCALAR;
    }
}
