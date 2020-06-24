package net.yangboyu.pslang.Paser.ast.io;

import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Stmt;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import org.apache.commons.lang3.StringUtils;

public class FileCloseStmt extends Stmt {
    private String filename;

    public FileCloseStmt() {
        super(ASTNodeTypes.FILE_CLOSE_STMT, "closefile");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        var fcs = new FileCloseStmt();
        var lexeme = it.nextMatch("CLOSEFILE");
        fcs.setLexeme(lexeme);

        fcs.filename = it.nextMatch(TokenType.STRING).getValue();

        return fcs;
    }

    public String getFilename() {
        return this.filename;
    }

    @Override
    public void print(int indent) {
        System.out.println(StringUtils.leftPad(" ", indent) + this.label);
        System.out.println(StringUtils.leftPad(" ", indent + 2) + this.filename);
    }
}
