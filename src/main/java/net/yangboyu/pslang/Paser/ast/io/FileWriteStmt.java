package net.yangboyu.pslang.Paser.ast.io;

import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class FileWriteStmt extends Stmt {

    public FileWriteStmt() {
        super(ASTNodeTypes.FILE_WRITE_STMT, "writefile");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("WRITEFILE");
        var fws = new FileWriteStmt();
        fws.setLexeme(lexeme);

        String filename = it.nextMatch(TokenType.STRING).getValue();
        fws.setProp("filename", filename);

        it.nextMatch(",");

        Variable storage = null;
        if (it.peek().isVariable()) {
            storage = (Variable) Factor.parse(it);
        } else {
            throw new ParseException(it.next());
        }

        fws.addChild(storage);

        return fws;
    }
}
