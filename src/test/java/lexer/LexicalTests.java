package lexer;

import net.yangboyu.pslang.Lexer.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexicalTests {
    void assertToken(Token token, String value, TokenType type){
        assertEquals(value, token.getValue());
        assertEquals(type, token.getType());
    }

    @Test
    public void test_expression() throws LexicalException {
        var lexer = new Lexer();
        var source = "(a+b)^100.02==+100-.20";
        var tokens = lexer.analyse(source.chars().mapToObj(c -> (char)c));

        assertEquals(11, tokens.size());
        assertToken(tokens.get(0), "(", TokenType.BRACKET);
        assertToken(tokens.get(1), "a", TokenType.VARIABLE);
        assertToken(tokens.get(2), "+", TokenType.OPERATOR);
        assertToken(tokens.get(3), "b", TokenType.VARIABLE);
        assertToken(tokens.get(4), ")", TokenType.BRACKET);
        assertToken(tokens.get(5), "^", TokenType.OPERATOR);
        assertToken(tokens.get(6), "100.02", TokenType.REAL);
        assertToken(tokens.get(7), "==", TokenType.OPERATOR);
        assertToken(tokens.get(8), "+100", TokenType.INTEGER);
        assertToken(tokens.get(9), "-", TokenType.OPERATOR);
        assertToken(tokens.get(10), ".20", TokenType.REAL);

        for(Token i:tokens){
            System.out.print(i.getValue());
            System.out.print(" ");
        }
    }

    @Test
    public void testComment() throws LexicalException{
            var source =
                    "FOR i <- 1 TO 10 \n" +
                            "   OUTPUT \"Hello World\" // loop and print\n" +
                            "ENDFOR" + "\n/* \nThis function is used for looping and printing \n*/ \n "
                            + "\n";
            var lexer = new Lexer();
            var tokens = lexer.analyse(source.chars().mapToObj(c -> (char)c));
            for(Token t:tokens){
                System.out.println(String.format("Type: %s Value: %s", t.getType(), t.getValue()));
            }
    }

    @Test
    public void testWhileExpr() throws LexicalException {
        String[] source = {
                "WHILE a > 0",
                "   b <- c + 1",
                "ENDWHILE"
        };

        testAndPrintFromArray(source);
    }

    @Test
    public void test_fix_errorComment() throws LexicalException{
        var lexer = new Lexer();
        var source = "/;";
        var tokens = lexer.analyse(source.chars().mapToObj(c -> (char) c));
        for(Token i:tokens){
            System.out.println(i.getValue());
        }
    }

    @Test
    public void test_loadFile() throws FileNotFoundException, UnsupportedEncodingException, LexicalException {
        var tokens = Lexer.fromFile("./example/function.ts");
        assertEquals(16, tokens.size());
    }

    private void testAndPrintFromArray(String[] arr) throws LexicalException {
        String source = mergeArrayToString(arr);
        var lexer = new Lexer();
        var tokens = lexer.analyse(source.chars().mapToObj(c -> (char) c));
        for(Token i:tokens){
            System.out.println(i.getValue());
        }
    }

    private String mergeArrayToString(String[] arr) {
        StringBuilder s = new StringBuilder();
        for (String value : arr) {
            s.append(value);
        }
        return s.toString();
    }
}
