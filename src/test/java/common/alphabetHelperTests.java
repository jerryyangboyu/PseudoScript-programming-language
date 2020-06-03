package common;

import net.yangboyu.pslang.Common.AlphabetHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class alphabetHelperTests {
    @Test
    public void test(){
        assertEquals(true, AlphabetHelper.isLetter('Z'));
        assertEquals(false, AlphabetHelper.isLetter('&'));
        assertEquals(false, AlphabetHelper.isLetter('•m'));
        assertEquals(true, AlphabetHelper.isNumber('9'));
        assertEquals(false, AlphabetHelper.isNumber('*'));
        assertEquals(true, AlphabetHelper.isLiteral('_'));
        assertEquals(false, AlphabetHelper.isLiteral(' '));
        assertEquals(true, AlphabetHelper.isLiteral('G'));
        assertEquals(true, AlphabetHelper.isOperator('+'));
        assertEquals(true, AlphabetHelper.isOperator('-'));
        assertEquals(true, AlphabetHelper.isOperator('*'));
        assertEquals(true, AlphabetHelper.isOperator('/'));
        assertEquals(true, AlphabetHelper.isOperator('&'));
        assertEquals(true, AlphabetHelper.isOperator('|'));
        assertEquals(true, AlphabetHelper.isOperator('^'));
        assertEquals(true, AlphabetHelper.isOperator('%'));
        assertEquals(true, AlphabetHelper.isOperator('<'));
        assertEquals(true, AlphabetHelper.isOperator('>'));
        assertEquals(false, AlphabetHelper.isOperator('a'));
        assertEquals(false, AlphabetHelper.isOperator('·}'));
        assertEquals(false, AlphabetHelper.isOperator('#'));
        assertEquals(false, AlphabetHelper.isOperator('.'));
        assertEquals(false, AlphabetHelper.isOperator(','));
    }
}
