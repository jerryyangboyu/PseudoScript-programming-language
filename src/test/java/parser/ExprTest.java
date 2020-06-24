package parser;

import jdk.jshell.spi.ExecutionControl;
import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Variable;
import net.yangboyu.pslang.Paser.ast.declaraction.DeclareStmt;
import net.yangboyu.pslang.Paser.ast.expressions.AssignStmt;
import net.yangboyu.pslang.Paser.ast.expressions.CallExpr;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.ast.loops.ForStmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.ParserUtils;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExprTest {
    @Test
    public void testAssignStmt() throws LexicalException, ParseException {
        var it = createTokenIt("myVariable <- \"Hello World\"");
        var tree = AssignStmt.parse(null, it);
        tree.print(0);

        assertEquals("<-", tree.getLexeme().getValue());
        assertEquals("myVariable", tree.getChild(0).getLexeme().getValue());
        assertEquals("\"Hello World\"", tree.getChild(1).getLexeme().getValue());
    }

    @Test
    public void testSimpleExpr() throws LexicalException, ParseException, ExecutionControl.NotImplementedException {
        var it = createTokenIt("a + b / (c * (a - b))");
        var tree = Expr.parse(null, it);
//        tree.print(0);

        assertEquals("+", tree.getLexeme().getValue());
        assertEquals("a", tree.getChild(0).getLexeme().getValue());

        assertEquals("a b c a b - * / +", ParserUtils.toPostfixExpression(tree));
    }

    @Test
    public void testComplexAssignStmt() throws LexicalException, ParseException, ExecutionControl.NotImplementedException {
        var it = createTokenIt("myVariable <- a + b / (c * (a - b))");
        var tree = AssignStmt.parse(null, it);
//        tree.print(0);

        assertEquals("<-", tree.getLexeme().getValue());
        assertEquals("myVariable", tree.getChild(0).getLexeme().getValue());
        assertEquals(ASTNodeTypes.BINARY_EXPR, tree.getChild(1).getType());
    }

    @Test
    public void testBasicCallStmt() throws ParseException, LexicalException {
        var it = createTokenIt("CALL myFunc()");
        var it2 = createTokenIt("CALL myRoutine");

        var tree = CallExpr.parse(null, it);
        var tree2 = CallExpr.parse(null, it2);

        assertEquals("CALL", tree.getLexeme().getValue());
        assertEquals("myFunc", tree.getChild(0).getLexeme().getValue());
        assertEquals("myRoutine", tree2.getChild(0).getLexeme().getValue());
    }

    @Test
    public void testComplexCallStmt() throws LexicalException, ParseException {
        var it = createTokenIt("CALL myFunc(1+1, arg2, arg3)");
        var tree = CallExpr.parse(null, it);
        tree.print(0);

        assertEquals("myFunc", tree.getChild(0).getLexeme().getValue());
        assertEquals(ASTNodeTypes.BINARY_EXPR, tree.getChild(1).getType());
        assertEquals("arg2", tree.getChild(2).getLexeme().getValue());
        assertEquals("arg3", tree.getChild(3).getLexeme().getValue());
    }

    @Test
    public void testFunctionCallStmt() throws LexicalException, ParseException {
        var it = createTokenIt("myFunc(n, n - 1)");
        var tree = Expr.parse(null, it);
        tree.print(0);
    }

    @Test
    public void testDeclareStmt() throws LexicalException, ParseException {
        var it = createTokenIt("DECLARE myVariable : INT");
        var tree = DeclareStmt.parse(null, it);
        tree.print(0);

        assertEquals("myVariable", tree.getChild(0).getLexeme().getValue());

        var factor = (Variable) tree.getChild(0);
        assertEquals("INT", factor.getTypeLexeme().getValue());
        assertEquals(TokenType.INTEGER, factor.getTypeLexeme().getType());
    }

    @Test
    public void testComplexDeclareStmt() throws LexicalException, ParseException {
        var it = createTokenIt("DECLARE myVariable : DATE");
        var tree = DeclareStmt.parse(null, it);
        tree.print(0);

        assertEquals("myVariable", tree.getChild(0).getLexeme().getValue());

        var factor = (Variable) tree.getChild(0);
        assertEquals("DATE", factor.getTypeLexeme().getValue());
        assertEquals(TokenType.TYPE, factor.getTypeLexeme().getType());
    }

    private PeekTokenIterator createTokenIt(String src) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(src.chars().mapToObj(x -> (char)x));
        return new PeekTokenIterator(tokens.stream());
    }
}
