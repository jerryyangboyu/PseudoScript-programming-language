package translator;

import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.Parser;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Translator.Translator;
import org.junit.jupiter.api.Test;

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
}
