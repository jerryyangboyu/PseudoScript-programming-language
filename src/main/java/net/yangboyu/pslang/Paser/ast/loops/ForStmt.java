package net.yangboyu.pslang.Paser.ast.loops;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Stmt;

public class ForStmt extends Stmt {

    public ForStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.FOR_STMT, "for");
    }
}
