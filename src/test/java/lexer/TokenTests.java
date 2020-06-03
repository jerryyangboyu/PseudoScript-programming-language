package lexer;

import net.yangboyu.pslang.Common.PeekIterator;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTests {
    @Test
    public void test_varOrKeyword(){
        var it1 = new PeekIterator<Character>("IF abc".chars().mapToObj(c -> (char)c));
        var it2 = new PeekIterator<Character>("TRUE is &*(determined*)".chars().mapToObj(c -> (char)c));

        var token1 = Token.makeVariableOrKeyword(it1);
        var token2 = Token.makeVariableOrKeyword(it2);

        assertEquals(TokenType.KEYWORD, token1.getType());
        assertEquals("IF", token1.getValue());
        it1.next();
        var token3 = Token.makeVariableOrKeyword(it1);
        assertEquals(TokenType.VARIABLE, token3.getType());
        assertEquals("abc", token3.getValue());

        assertEquals(TokenType.BOOLEAN, token2.getType());
        assertEquals("TRUE", token2.getValue());
        it2.next();
        var token4 = Token.makeVariableOrKeyword(it2);
        assertEquals(TokenType.VARIABLE, token4.getType());
        assertEquals("is", token4.getValue());
        it2.next();

//        var token5 = Token.makeVariableOrKeyword(it2);
//        assertEquals("determined", token5.getValue());
//        assertEquals(TokenType.VARIABLE, token5.getType());
    }

    @Test
    public void test_makeString() throws LexicalException {
        String[] tests = {
                "apple\"abc\"company"
        };
        for(String test:tests){
            var it = new PeekIterator<Character>(test.chars().mapToObj(c -> (char)c));
            var token = Token.makeString(it);
            assertEquals(TokenType.STRING, token.getType());
            assertEquals("\"abc\"", token.getValue());
        }
    }

    @Test
    public void test_makeOperator() throws LexicalException {
        String[] tests = {
                "fuck *& jelly",
                "you -- 13",
                "he (^6",
                "&& false",
                "|? true",
                "[//]",
                "JELLY <- 123",
                "Tom <> TRUE"
        };
        String[] result = {
                "*",
                "--",
                "^",
                "&&",
                "|",
                "//",
                "<-",
                "<>"
        };
        int i = 0;
        for(String test:tests){
            var it = new PeekIterator<Character>(test.chars().mapToObj(c -> (char)c));
            var token = Token.makeOperator(it);
            assertEquals(result[i++], token.getValue());
        }
    }

    @Test
    public void test_makeNumber() throws LexicalException {
        String[] tests = {
                "0.11 ",
                "+0.11 ",
                "-0.11 ",
                "+111 ",
                "-111 ",
                "101010 ",
                "1234567890 ",
                "10000000000 ",
                ".1010101010 ",
                ".12345678987654321 ",
                "100.02 ",
                "-20",
                "1",
        };

        String[] results = {
                "0.11",
                "+0.11",
                "-0.11",
                "+111",
                "-111",
                "101010",
                "1234567890",
                "10000000000",
                ".1010101010",
                ".12345678987654321",
                "100.02",
                "-20",
                "1"
        };

        int i = 0;

        for(String test:tests){
            var it = new PeekIterator<Character>(test.chars().mapToObj(c -> (char)c), (char)0);
            var token  = Token.makeNumber(it);
            assertEquals(results[i++], token.getValue());
        }
    }

    @Test
    public void test_end_number() throws LexicalException {
        var test = "1";
        var it = new PeekIterator<Character>(test.chars().mapToObj(c -> (char)c), (char)0);
        var token  = Token.makeNumber(it);
        assertEquals("1", token.getValue());
    }

    @Test
    public void test_commentLikeOperator() throws LexicalException {
        var test = "*=";
        var it = new PeekIterator<Character>(test.chars().mapToObj(c -> (char)c), (char)0);
        var token  = Token.makeOperator(it);
        assertEquals("*=", token.getValue());
    }
}
