package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class Program extends Block {
    public Program(ASTNode _parent) {
        super(_parent);
        this.label = "program";
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {

        var program = new Program(parent);

        ASTNode stmt = null;

        while ( (stmt = Stmt.parseStmt(parent, it)) != null) {
            program.addChild(stmt);
        }


        return program;
    }
}
