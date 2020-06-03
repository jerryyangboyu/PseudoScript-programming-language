package common;

import net.yangboyu.pslang.Lexer.*;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.junit.jupiter.api.Test;

public class PeekTokenIteratorTest {
    @Test
    void simple() throws LexicalException {
        String src = "10 * (7+4)";
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        var it = new PeekTokenIterator(tokens.stream());

        while (it.hasNext()) {
            System.out.println(it.next().getValue());
        }
    }
}
