package net.yangboyu.pslang.Paser.util;

import net.yangboyu.pslang.Paser.ast.ASTNode;

@FunctionalInterface
public interface ExprHOF {
    ASTNode hoc() throws ParseException;
}
