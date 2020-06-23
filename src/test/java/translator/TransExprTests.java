package translator;

import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.Parser;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Translator.*;
import net.yangboyu.pslang.Translator.TAInstruction;
import net.yangboyu.pslang.Translator.Translator;
import net.yangboyu.pslang.Translator.symbol.SymbolTable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransExprTests {
    private void assertOpCodes(String[] lines, ArrayList<TAInstruction> opCodes) {
        for (int i = 0; i < opCodes.size(); i++) {
            assertEquals(lines[i], opCodes.get(i).toString());
        }
    }

    @Test
    public void transExpr() throws LexicalException, ParseException {
        var source = "a+(b-c)+d*(b-c)*2";
        var tree = Parser.parse(source);
//        tree.print(0);
        var translator = new Translator();
        var symbolTable = new SymbolTable();
        var program = new TAProgram();

        // 这里程序的顶层默认添加了program项，需要配合parseProgram() -> parseExpr(), 才能运行这里为了测试方便直接.getChild()了
        translator.translateExpr(program, tree.getChild(0), symbolTable);
        System.out.println(program.toString());

        var expectedResults = new String[] {
                "p0 = b - c",
                "p1 = b - c",
                "p2 = p1 * 2",
                "p3 = d * p2",
                "p4 = p0 + p3",
                "p5 = a + p4",
        };

        assertOpCodes(expectedResults, program.getInstructions());
    }

    @Test
    public void testAssignStmt() throws LexicalException, ParseException {
        var source = "a<-1.0*2.0*3.0";
        var tree = Parser.parse(source);
//        tree.print(0);
        var translator = new Translator();
        var symbolTable = new SymbolTable();
        var program = new TAProgram();

        // 这里程序的顶层默认添加了program项，需要配合parseProgram() -> parseExpr(), 才能运行这里为了测试方便直接.getChild()了
        translator.translateAssignStmt(program, tree.getChild(0), symbolTable);
        System.out.println(program.toString());

        var expectedResults = new String[] {
                "p0 = 2.0 * 3.0",
                "p1 = 1.0 * p0",
                "a = p1"
        };

        assertOpCodes(expectedResults, program.getInstructions());
    }

    @Test
    public void testDeclareStmt() throws LexicalException, ParseException {
        var source = "a=1";
        var tree = Parser.parse(source);
//        tree.print(0);
        var translator = new Translator();
        var symbolTable = new SymbolTable();
        var program = new TAProgram();

        // 这里程序的顶层默认添加了program项，需要配合parseProgram() -> parseExpr(), 才能运行这里为了测试方便直接.getChild()了
        translator.translateDeclareStmt(program, tree.getChild(0), symbolTable);
        System.out.println(program.toString());

        var expectedResults = new String[] {
                "a = 1",
        };
        assertOpCodes(expectedResults, program.getInstructions());
    }

}
