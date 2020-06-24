package parser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Block;
import net.yangboyu.pslang.Paser.ast.Stmt;
import net.yangboyu.pslang.Paser.ast.selection.IfStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.FunctionDeclareStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.ReturnStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectionTest {
    @Test
    public void testComplexIfStmt() throws LexicalException, ParseException {
        var it = createTokenIt("IF condition THEN" +
                " \n\n p1 <- 1\n " +
                "\n ELSE IF (condition2 == 3) >= 1 \n" +
                "THEN\n" +
                " p2 <- 1 " +
                "\n p3 <- p3 * 3 ELSE" +
                " \n p4 <- p1 + 1\n" +
                "ENDIF\n" +
                "ENDIF");
        var tree = IfStmt.parse(it);
        tree.print(0);
    }

    @Test
    public void testSimpleStmt() throws LexicalException, ParseException {
        var it = createTokenIt(
                "IF count >= 100\n" +
                "THEN \n" +
                "   i <- i + 1\n" +
                        "ENDIF");
        var tree = IfStmt.parse(it);
        tree.print(0);
    }

    private PeekTokenIterator createTokenIt(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        return new PeekTokenIterator(tokens.stream());
    }
}
