package parser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.io.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IOTest {
    @Test
    public void testBasicInput() throws LexicalException, ParseException {
        var it = createTokenIt("INPUT myVariable\n");
        var it2 = createTokenIt("INPUT var2 IF a == 0");
        var tree = InputStmt.parse(it);
        var tree2 = InputStmt.parse(it2);

        assertEquals("myVariable", tree.getChild(0).getLexeme().getValue());
        assertEquals(TokenType.KEYWORD, tree.getLexeme().getType());

        assertEquals("var2", tree2.getChild(0).getLexeme().getValue());
        assertEquals(TokenType.KEYWORD, tree2.getLexeme().getType());
        assertEquals("IF", it2.peek().getValue());
    }

    @Test
    public void complexInput() throws LexicalException, ParseException {
        var it = createTokenIt("INPUT var, var1, var2, var3 IF a == 0");
        var tree = InputStmt.parse(it);
        assertEquals(TokenType.KEYWORD, tree.getLexeme().getType());
        assertEquals("var", tree.getChild(0).getLexeme().getValue());
        assertEquals("var1", tree.getChild(1).getLexeme().getValue());
        assertEquals("var2", tree.getChild(2).getLexeme().getValue());
        assertEquals("var3", tree.getChild(3).getLexeme().getValue());
        assertEquals("IF", it.peek().getValue());

        var it2 = createTokenIt("INPUT var, var1, var2, var3\n");
        var tree2 = InputStmt.parse(it2);
        assertEquals(TokenType.KEYWORD, tree.getLexeme().getType());
        assertEquals("var", tree.getChild(0).getLexeme().getValue());
        assertEquals("var1", tree.getChild(1).getLexeme().getValue());
        assertEquals("var2", tree.getChild(2).getLexeme().getValue());
        assertEquals("var3", tree.getChild(3).getLexeme().getValue());
    }

    @Test
    public void testOpenFileStmt() throws LexicalException, ParseException {
        var it = createTokenIt("OPENFILE \"test.txt\" FOR READ");
        var tree = FileOpenStmt.parse(it);
        tree.print(0);
    }

    @Test
    public void testCloseFileStmt() throws ParseException, LexicalException {
        var it = createTokenIt("CLOSEFILE \"test.txt\"");
        var tree = FileCloseStmt.parse(it);
        tree.print(0);

    }

    @Test
    public void testFileReadStmt() throws LexicalException, ParseException {
        var it = createTokenIt("READFILE \"test.txt\", myVariable");
        ASTNode tree = FileReadStmt.parse(it);

        assertEquals("myVariable", tree.getChild(0).getLexeme().getValue());
        assertEquals("\"test.txt\"", tree.getProp("filename"));
    }

    @Test
    public void testFileWriteStmt() throws LexicalException, ParseException {
        var it = createTokenIt("WRITEFILE \"test.txt\", myVariable");
        ASTNode tree = FileWriteStmt.parse(it);

        assertEquals("myVariable", tree.getChild(0).getLexeme().getValue());
        assertEquals("\"test.txt\"", tree.getProp("filename"));
    }

    @Test
    public void testOutput() throws LexicalException, ParseException {
        var it = createTokenIt("OUTPUT \"PseudoScript is fun!\"");
        var tree = OutputStmt.parse(it);
        tree.print(0);
    }

    private PeekTokenIterator createTokenIt(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        return new PeekTokenIterator(tokens.stream());
    }
}
