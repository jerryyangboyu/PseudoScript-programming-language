package parser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.*;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.ParserUtils;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseExprTest {
    @Test
    public void simple() throws LexicalException, ParseException, ExecutionControl.NotImplementedException {
        var expr = createExpr("1+1+1");
        assertEquals("1 1 1 + +", ParserUtils.toPostfixExpression(expr));
    }

    @Test
    public void simple1() throws LexicalException, ParseException, ExecutionControl.NotImplementedException {
        var expr = createExpr("\"1\" == \"\"");
        assertEquals("\"1\" \"\" ==", ParserUtils.toPostfixExpression(expr));
    }

    @Test
    public void complex() throws LexicalException, ParseException, ExecutionControl.NotImplementedException {
        var expr = createExpr("1+2*3");
        var expr2 = createExpr("10 * (7+4)");
        var expr3 = createExpr("(1*2!=7)==3!=4*5+6");
        assertEquals("1 2 3 * +", ParserUtils.toPostfixExpression(expr));
        assertEquals("10 7 4 + *", ParserUtils.toPostfixExpression(expr2));
        assertEquals("1 2 * 7 != 3 4 5 * 6 + != ==", ParserUtils.toPostfixExpression(expr3));
    }

    @Test
    public void error1() throws LexicalException, ParseException {
        var simple1 = createExpr("c << 3");
//        var expr = createExpr("(c << 3) >= 1");

        simple1.print(0);
//        expr.print(0);
    }

    private ASTNode createExpr(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        var it = new PeekTokenIterator(tokens.stream());
        return Expr.parse(it);
    }
}
