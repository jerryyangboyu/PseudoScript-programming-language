package parser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.Parser;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.declaraction.DeclareStmt;
import net.yangboyu.pslang.Paser.ast.expressions.AssignStmt;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.ast.selection.IfStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.FunctionDeclareStmt;
import net.yangboyu.pslang.Paser.ast.subroutines.ReturnStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StmtTest {
    @Test
    public void declare() throws LexicalException, ParseException, ExecutionControl.NotImplementedException {
        var it = createTokenIt("auto i = 100 * 2");

        var stmt = DeclareStmt.parse(null, it);

//        assertEquals(ParserUtils.toPostfixExpression(stmt), "i 100 2 * =");

        stmt.print(0);
    }

    @Test
    public void assign() throws LexicalException, ParseException {
        var it = createTokenIt("i = (100 * 2) + 3");

        var stmt = AssignStmt.parse(null, it);

        stmt.print(0);
    }

    @Test
    public void Stmt() throws LexicalException, ParseException {
        var it = createTokenIt("var myVariable = 300 + 1 / 0");
        var it2 = createTokenIt("myVariable = 0");

        var stmt = Stmt.parseStmt(null, it);
        var stmt2 = Stmt.parseStmt(null, it2);

        assert stmt != null;
        stmt.print(0);
        assert stmt2 != null;
        stmt2.print(0);
    }

    @Test
    public void Block() throws LexicalException, ParseException {
        var it = createTokenIt("{" +
                "auto i = 0" +
                "i++" +
                "i > 0" +
                "i  = 100" +
                "}");

        var block = Block.parse(null, it);

        block.print(0);
    }

    @Test
    public void program() throws LexicalException, ParseException, FileNotFoundException, UnsupportedEncodingException {
        var tokens = Lexer.fromFile("./example/program.ps");
        var func = (FunctionDeclareStmt)Stmt.parseStmt(null, new PeekTokenIterator(tokens.stream()));

        assert func != null;
        func.print(0);
    }

    @Test
    public void IfStmt() throws LexicalException, ParseException {
        var it = createTokenIt("if(a) \n{\n a = 1\n }\n");
        var it2 = createTokenIt("if(a > (b+1)) \n{\n a = 1\n }\n");

        var stmt = IfStmt.parse(null, it);
        var stmt2 = IfStmt.parse(null, it2);

        stmt.print(0);
        stmt2.print(0);
    }

    @Test
    public void IfElseStmt() throws LexicalException, ParseException {
        var it = createTokenIt("if(a) \n{\n a = 1\n }\n else { b = 1 \n a = a * 3}");
        var it2 = createTokenIt("if(a) \n{\n a = 1\n }\n else if((c == 3) >= 1) { b = 1 \n a = a * 3} else \n {c = a + 1}");

        var stmt = IfStmt.parse(null, it);
        var stmt2 = IfStmt.parse(null, it2);

        stmt2.print(0);
        stmt.print(0);
    }

    @Test
    public void function() throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
        var tokens = Lexer.fromFile("./example/function.ps");
        var func = (FunctionDeclareStmt)Stmt.parseStmt(null, new PeekTokenIterator(tokens.stream()));

        assert func != null;
        func.print(0);

        var args = func.getArgs();
        assertEquals("a", args.getChild(0).getLexeme().getValue());
        assertEquals("b", args.getChild(1).getLexeme().getValue());

        var type = func.getFunctionType();
        assertEquals("int", type);

        var functionVariable = func.getFunctionVariable();
        assertEquals("add", functionVariable.getLexeme().getValue());

        var block = func.getBlock();
        assertTrue(block.getChild(1) instanceof ReturnStmt);

    }

    @Test
    public void function1() throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
        var tokens = Lexer.fromFile("./example/recursion.ps");
        var func = (FunctionDeclareStmt)Stmt.parseStmt(null, new PeekTokenIterator(tokens.stream()));

        assert func != null;
        func.print(0);

    }

    @Test
    public void callStmt() throws LexicalException, ParseException {
        var it = createTokenIt("myFunction(10,11,12)");
        var it2 = createTokenIt("myFunction2(a + b, 3 + 5 /2)");

        var stmt = Stmt.parseStmt(null, it);
        var stmt2 = Stmt.parseStmt(null, it2);

        assert stmt != null;
        stmt.print(0);
        assert stmt2 != null;
        stmt2.print(0);
    }

    @Test
    public void file() throws FileNotFoundException, ParseException, LexicalException, UnsupportedEncodingException {
        var tree = Parser.fromFile("./example/program.ps");
        tree.print(0);
    }

    @Test
    public void parser() throws LexicalException, ParseException {
        var tree = Parser.parse("auto a = 1\n" +
                "auto n = 1\n" +
                " a = ( a* (n / 4)) + 1\n" +
                "\n" +
                " {\n" +
                "    return a + n\n" +
                " }\n" +
                "\n" +
                " func fuck() void {\n" +
                " }\n" +
                "\n" +
                " if(a == (b + c / 4 + 5)){\n" +
                " fuck()\n" +
                " }else if (true){}");

        tree.print(0);
    }

    @Test
    public void programComplex() throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
        var tokens = Lexer.fromFile("./example/program.ps");
        var it = new PeekTokenIterator(tokens.stream());

        var program = Program.parse(null, new PeekTokenIterator(tokens.stream()));
        program.print(0);
    }

    @Test
    public void program1() throws LexicalException, ParseException {
        var source = "var a = 1\n" +
                "{var b = a * 100\n" +
                "}\n" +
                "{\n" +
                "var c = b * 100\n" +
                "}\n";

        var ast = Parser.parse(source);
        ast.print(0);
    }

    private PeekTokenIterator createTokenIt(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        return new PeekTokenIterator(tokens.stream());
    }

    private ASTNode createExpr(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        var it = new PeekTokenIterator(tokens.stream());
        return Expr.parse(null, it);
    }
}
