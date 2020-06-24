package net.yangboyu.pslang.Paser;

import net.yangboyu.pslang.Lexer.Lexer;
import net.yangboyu.pslang.Lexer.LexicalException;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.Program;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Parser {
    public static ASTNode parse(String source) throws LexicalException, ParseException {
        var lexer = new Lexer();
        var tokens = lexer.analyse(source.chars().mapToObj(x -> (char)x));
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }

    public static ASTNode fromFile(String file) throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
        var tokens = Lexer.fromFile(file);
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }
}
