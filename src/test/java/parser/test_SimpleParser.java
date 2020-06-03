package parser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.SimpleParser;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.ast.Scalar;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class test_SimpleParser {
    @Test
    public void test_add() throws ParseException, LexicalException {
        var source = "1+2+3+4".chars().mapToObj(c -> (char)c);
        var res = new Lexer().analyse(source);
//        for (Token i:res) {
//            System.out.println(i.getValue());
//        }
        var it = new PeekTokenIterator(res.stream());
        var expr = SimpleParser.parse(it);

        assertEquals(2, expr.getChildren().size());

        var v1 = (Scalar)expr.getChild(0);
        assertEquals("1", v1.getLexeme().getValue());
        assertEquals("+", expr.getLexeme().getValue());

        var e2 = (Expr)expr.getChild(1);
        var v2 = (Scalar)e2.getChild(0);
        assertEquals("2", v2.getLexeme().getValue());
        assertEquals("+", e2.getLexeme().getValue());

        expr.print(0);
    }
}
