package translator;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Translator.symbol.StaticSymbolTable;
import net.yangboyu.pslang.Translator.symbol.Symbol;
import net.yangboyu.pslang.Translator.symbol.SymbolTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SymbolTests {
    @Test
    public void symbolTable() {
        var symbolTable = new SymbolTable();

        // ������ת��ǩ
        symbolTable.createLabel("L0", new Token(TokenType.VARIABLE, "foo"));

        // ��ʱ����
        symbolTable.createVariable();

        // ����һ�������ı���
        symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));

        assertEquals(2, symbolTable.localSize());

    }

    @Test
    public void chain() {
        var symbolTable = new SymbolTable();
        symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));

        var childTable = new SymbolTable();
        symbolTable.addChild(childTable);

        var childChildTable = new SymbolTable();
        childTable.addChild(childChildTable);

        // �������������Ƿ����ҵ�����������
        assertTrue(childTable.exists(new Token(TokenType.VARIABLE, "a")));

        // �������Ӽ��������Ƿ����ҵ�үү��������ı���
        assertTrue(childChildTable.exists(new Token(TokenType.VARIABLE, "a")));
    }

    @Test
    public void symbolOffset() {
        var symbolTable = new SymbolTable();

        symbolTable.createSymbolByLexeme(new Token(TokenType.INTEGER, "100"));
        var symbolA = symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));
        var symbolB = symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "b"));

        var childTable = new SymbolTable();
        symbolTable.addChild(childTable);

        // ������ͬ�ı����ܲ��ܱ���¡����
        var anotherSymbolB = childTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "b"));
        var symbolC = childTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "c"));

        assertEquals(0, symbolA.getOffset());
        assertEquals(1, symbolB.getOffset());

        // another symbol B ��Ϊ�ǴӸ��������򿽱�����������OffsetӦ��Ҳ��1
        assertEquals(1, anotherSymbolB.getOffset());
        assertEquals(1, anotherSymbolB.getLayerOffset());

        assertEquals(0, symbolC.getOffset());
        assertEquals(0, symbolC.getLayerOffset()); // ֻҪ���ڵ�ǰ������ı�������0
    }

    @Test
    public void staticSymbolTable() {
        var ss = new StaticSymbolTable();
        ss.add(Symbol.createImmediateSymbol(new Token(TokenType.REAL, "3.1415926")));

        var duplicateSymbol1 = Symbol.createImmediateSymbol(new Token(TokenType.INTEGER, "2"));
        var duplicateSymbol2 = Symbol.createImmediateSymbol(new Token(TokenType.INTEGER, "2"));

        ss.add(duplicateSymbol1);
        ss.add(duplicateSymbol2);

        assertEquals(2, ss.size());
        assertEquals(duplicateSymbol1.getOffset(), duplicateSymbol2.getOffset());
    }
}
