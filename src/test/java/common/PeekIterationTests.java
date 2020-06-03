package common;

import net.yangboyu.pslang.Common.PeekIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeekIterationTests {
    @Test
    public void test_peek(){
        var source = "abcdefg";
        var it = new PeekIterator<Character>(source.chars().mapToObj(c -> (char) c));
        assertEquals('a', it.next());
        assertEquals('b', it.next());
        it.next();
        it.next();
        assertEquals('e', it.next());
        assertEquals('f', it.peek());
        assertEquals('f', it.peek());
        assertEquals('f', it.next());
        assertEquals('g', it.next());
    }

    @Test
    public void test_lookahead(){
        var source = "abcdefg";
        var it = new PeekIterator<Character>(source.chars().mapToObj(c -> (char) c));
        assertEquals('a', it.next());
        assertEquals('b', it.next());
        it.putBack();
        it.putBack();
        assertEquals('a', it.next());
    }

    @Test
    public void test_endToken(){
        var source = "abc";
        var it = new PeekIterator<Character>(source.chars().mapToObj(c -> (char) c), (char)0);
        var i = 0;
        while(it.hasNext()){
            if(i == 2){
                assertEquals('c', it.next());
                assertEquals('\0', it.peek());
            }else{
                assertEquals(source.charAt(i++), it.next());
            }
        }
    }

    @Test
    public void test_end_peek(){
        var source = "abc";
        var it = new PeekIterator<Character>(source.chars().mapToObj(c -> (char) c), (char)0);
        var i = 0;
        while(it.hasNext()){
            if(i == 2){
                assertEquals('c', it.next());
                assertEquals('\0', it.peek());
            }
            it.next();
            i++;
        }
    }

    @Test
    public void test_commentError(){
        var source = "1";
        var it = new PeekIterator<Character>(source.chars().mapToObj(c -> (char)c), (char)0);
        // main while
        it.next();
        it.putBack();

        // Op while
        System.out.println(it.next());
        System.out.println(it.next());
        it.putBack();

        System.out.println(it.next());
    }
}
