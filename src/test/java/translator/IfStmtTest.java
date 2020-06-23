package translator;

import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.Parser;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.declaraction.DeclareStmt;
import net.yangboyu.pslang.Paser.ast.expressions.AssignStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Translator.Translator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IfStmtTest {

    @Test
    public void test() throws LexicalException, ParseException {
        var source = "IF a THEN a <- 10 ENDIF";
        ASTNode ast = Parser.parse(source);
        var translator = new Translator();
        var program = translator.translate(ast);
        System.out.println(program.toString());

        String result = "IF a ELSE L0\n" +
                "SP -1\n" +
                "a = 10\n" +
                "SP 1\n" +
                "L0:\n";
        assertEquals(result, program.toString());
    }

    @Test
    public void testCommonIf() throws LexicalException, ParseException {
        var source = "IF a THEN b <- 10 ELSE \n" +
                "c <- 11 \n" +
                "ENDIF";
        ASTNode ast = Parser.parse(source);
        var translator = new Translator();
        var program = translator.translate(ast);
        System.out.println(program.toString());

        String result = "IF a ELSE L0\n" +
                "SP -1\n" +
                "b = 10\n" +
                "SP 1\n" +
                "GOTO L1\n" +
                "L0:\n" +
                "SP -1\n" +
                "c = 11\n" +
                "SP 1\n" +
                "L1:\n";
        assertEquals(result, program.toString());
    }

    @Test
    public void testComplexIf() throws LexicalException, ParseException {
        var source = "IF a THEN b <- 10 ELSE \n" +
                "IF b > 30 THEN \n" +
                "d <- 50 ELSE IF f > 50" +
                "THEN x <- 60 \n" +
                "ENDIF\n" +
                "ENDIF\n" +
                "ENDIF\n";
        ASTNode ast = Parser.parse(source);
        ast.print(0);
        var translator = new Translator();
        var program = translator.translate(ast);
        System.out.println(program.toString());

        String result = "IF a ELSE L0\n" +
                "SP -1\n" +
                "b = 10\n" +
                "SP 1\n" +
                "GOTO L4\n" +
                "L0:\n" +
                "p0 = b > 30\n" +
                "IF p0 ELSE L1\n" +
                "SP -3\n" +
                "d = 50\n" +
                "SP 3\n" +
                "GOTO L3\n" +
                "L1:\n" +
                "p1 = f > 50\n" +
                "IF p1 ELSE L2\n" +
                "SP -5\n" +
                "x = 60\n" +
                "SP 5\n" +
                "L2:\n" +
                "L3:\n" +
                "L4:\n";
        assertEquals(result, program.toString());
    }


}
