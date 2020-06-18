package parser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Block;
import net.yangboyu.pslang.Paser.ast.loops.ForStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockTest {
    @Test
    public void testSimpleBlock() throws LexicalException, ParseException {
        var it = createTokenIt("");
        var tree = Block.parse(null, it);
//        tree.print(0);
        assertEquals("block", tree.getLabel());
    }

    @Test
    public void testBasicBlock() throws LexicalException, ParseException {
        var it = createTokenIt("myVariable <- 3 + 5 / 2 \n DECLARE var2 : STRING \n CALL sub");
        var tree = Block.parse(null, it);
        tree.print(0);

        var stmt1 = tree.getChild(0);
        assertEquals("<-", stmt1.getLexeme().getValue());
        assertEquals("myVariable", stmt1.getChild(0).getLexeme().getValue());
        assertEquals(ASTNodeTypes.BINARY_EXPR, stmt1.getChild(1).getType());
    }

    private PeekTokenIterator createTokenIt(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        return new PeekTokenIterator(tokens.stream());
    }
}
