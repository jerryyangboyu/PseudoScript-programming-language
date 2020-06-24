package translator;

import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.Parser;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.declaraction.DeclareStmt;
import net.yangboyu.pslang.Paser.ast.expressions.AssignStmt;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Translator.Translator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockTest {

    @Test
    public void test() throws LexicalException, ParseException {
        var source = "auto a = 1\n" +
                "{auto b = a * 100\n" +
                "}\n" +
                "{\n" +
                "auto c = b * 100\n" +
                "}\n";

        ASTNode ast = Parser.parse(source);
//        ast.print(0);

        var translator = new Translator();

        var program = translator.translate(ast);

        System.out.println(program.toString());
    }

    @Test
    public void testBlock() throws ParseException {
        var node = generateDumpBlock();
        var translator = new Translator();
        var program = translator.translate(node);
        node.print(0);
        System.out.println(program.toString());

    }

    private static ASTNode generateDumpBlock() {
        var node = new Program();
        var parent = new Block();
        var child = new Block();
        var decls1 = generateDumpDecls();
        var decls2 = generateDumpDecls();
        var decls3 = generateDumpDecls();
        var assign = new AssignStmt();
        assign.addChild(new Variable(new Token(TokenType.VARIABLE, "myVariable")));
        assign.addChild(new Scalar(new Token(TokenType.INTEGER, "250")));
        child.addChild(new Block());
        parent.addChild(assign);
        parent.addChild(decls3);
        parent.addChild(child);
        node.addChild(decls1);
        node.addChild(decls2);
        node.addChild(parent);
        return node;
    }

    private static ASTNode generateDumpDecls() {
        var decls = new DeclareStmt();
        int hashCode = decls.hashCode();
        decls.addChild(new Variable(new Token(TokenType.VARIABLE, "p" + hashCode)));
        decls.addChild(new Scalar(new Token(TokenType.INTEGER, "250")));
        return decls;
    }
}
