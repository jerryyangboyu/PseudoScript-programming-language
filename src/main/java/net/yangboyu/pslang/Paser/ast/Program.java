package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class Program extends Block {
    public Program() {
        super();
        this.label = "program";
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {

        var program = new Program();

        ASTNode stmt = null;

        while ( (stmt = Stmt.parseStmt(it)) != null) {
            program.addChild(stmt);
        }

        return program;
    }
}
