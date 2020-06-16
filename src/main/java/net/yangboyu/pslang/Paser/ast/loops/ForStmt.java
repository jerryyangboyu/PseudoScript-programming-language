package net.yangboyu.pslang.Paser.ast.loops;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.expressions.AssignStmt;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class ForStmt extends Stmt {

    public ForStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.FOR_STMT, "for");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("FOR");
        var forStmt = new ForStmt(parent);
        forStmt.setLexeme(lexeme);

        Variable variable = (Variable) Factor.parse(it);
        forStmt.addChild(variable);

        it.nextMatch("<-");

        Scalar lowerBound = (Scalar) Factor.parse(it);
        forStmt.addChild(lowerBound);

        it.nextMatch("TO");

        Scalar upperBound = (Scalar) Factor.parse(it);
        assert upperBound != null;
        int upperBoundVal = Integer.parseInt(upperBound.getLexeme().getValue());
        assert lowerBound != null;
        int lowerBoundVal = Integer.parseInt(lowerBound.getLexeme().getValue());
        if (upperBoundVal < lowerBoundVal) {
            throw new ParseException("Upper bound must be bigger than lower bound!");
        }
        forStmt.addChild(upperBound);

        if (it.peek().getValue().equals("STEP")) {
            it.nextMatch("STEP");
            Scalar step = (Scalar) Factor.parse(it);
            forStmt.addChild(step);
        } else {
            forStmt.addChild(new Scalar(new Token(TokenType.INTEGER, "1")));
        }

        var block = Block.parse(parent, it);
        forStmt.addChild(block);

        it.nextMatch("ENDFOR");
        return forStmt;
    }
}