package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class Block extends Stmt {
    public Block() {
        super(ASTNodeTypes.BLOCK, "block");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {

        var block = new Block();

        ASTNode stmt = null;

        while ( (stmt = Stmt.parseStmt(it)) != null) {
            block.addChild(stmt);
        }

        return block;
    }
}
