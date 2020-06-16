package net.yangboyu.pslang.Paser.ast.io;

import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.apache.commons.lang3.StringUtils;

public class FileOpenStmt extends Stmt {

    private String filename;
    private String openMode;

    public String getFilename() {
        return filename;
    }

    public String getOpenMode() {
        return openMode;
    }

    public FileOpenStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.FILE_OPEN_STMT, "openfile");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var ofs = new FileOpenStmt(parent);
        var lexeme = it.nextMatch("OPENFILE");
        ofs.setLexeme(lexeme);

        ofs.filename = it.nextMatch(TokenType.STRING).getValue();

        it.nextMatch("FOR");

        String openMode = it.nextMatch(TokenType.KEYWORD).getValue();
        if (openMode == null || (!openMode.equals("READ") && !openMode.equals("WRITE"))) {
            throw new ParseException(openMode);
        }
        ofs.openMode = openMode;

        return ofs;
    }

    @Override
    public void print(int indent) {
        System.out.println(StringUtils.leftPad(" ", indent) + this.label);
        System.out.println(StringUtils.leftPad(" ", indent + 2) + this.filename);
        System.out.println(StringUtils.leftPad(" ", indent + 2) + this.openMode);
    }
}
