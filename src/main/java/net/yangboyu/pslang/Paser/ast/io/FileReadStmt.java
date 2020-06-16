package net.yangboyu.pslang.Paser.ast.io;

import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class FileReadStmt extends Stmt {
    public FileReadStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.FILE_READ_STMT, "readfile");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("READFILE");
        var frs = new FileReadStmt(parent);
        frs.setLexeme(lexeme);

        String filename = it.nextMatch(TokenType.STRING).getValue();
        frs.setProp("filename", filename);

        it.nextMatch(",");

        Variable storage = null;
        if (it.peek().isVariable()) {
            storage = (Variable) Factor.parse(it);
        } else {
            throw new ParseException(it.next());
        }

        frs.addChild(storage);

        return frs;
    }
}
