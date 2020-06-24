package parser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.io.*;
import net.yangboyu.pslang.Paser.ast.loops.ForStmt;
import net.yangboyu.pslang.Paser.ast.loops.WhileStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoopTest {
    @Test
    public void testForLoopSimple() throws LexicalException, ParseException {
        var it = createTokenIt("FOR i <- 1 TO 10\n" +
                "OUTPUT \"PseudoScript count\", i\n " +
                "ENDFOR");
        var tree = ForStmt.parse(it);
        tree.print(0);
    }

    @Test
    public void testWhileLoopSimple() throws LexicalException, ParseException {
        var it = createTokenIt("WHILE a > b \n" +
                "counter <- counter + 1\n" +
                "ENDWHILE");
        var tree = WhileStmt.parse(null, it);
        tree.print(0);
    }

    private PeekTokenIterator createTokenIt(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        return new PeekTokenIterator(tokens.stream());
    }
}
