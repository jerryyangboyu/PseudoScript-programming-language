package translator;

import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.Parser;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Translator.TAProgram;
import net.yangboyu.pslang.Translator.Translator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuncStmtTest {


    @Test
    public void testFunctionDeclsStmt() throws LexicalException, ParseException {
        // TODO
        // bug1: ��Utils�������ݺ���
        // TODO
        // bug2: ���ֻ�Ǻ�������ͬ���ǲ�����ͬ�����ᱨδ�ҵ�����

        var source = "FUNCTION add(a: INTEGER, b: INTEGER) RETURNS INTEGER\n " +
                "   RETURN a + b\n" +
                "ENDFUNCTION ";
        var tree = Parser.parse(source);
        tree.print(0);
        var translator = new Translator();
        var program = translator.translate(tree);
        System.out.println(program.toString());
    }


}
