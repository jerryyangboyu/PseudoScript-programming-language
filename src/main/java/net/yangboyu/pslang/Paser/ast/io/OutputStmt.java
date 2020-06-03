package net.yangboyu.pslang.Paser.ast.io;

import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class OutputStmt extends Stmt {
    public OutputStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.OUTPUT_STMT, "output");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var output_stmt = new OutputStmt(parent);
        var lexeme = it.nextMatch("OUTPUT");
        output_stmt.setLexeme(lexeme);

        // match for <var1>
        ASTNode factor = Factor.parse(it);
        if (factor != null) {
            output_stmt.addChild(factor);
        } else {
            throw new ParseException("Syntax Error: factors expected after OUTPUT keyword");
        }

        // match pattern for , <var2>, <var3> ...
        while(it.hasNext() && it.peek().getValue().equals(",")) {
            it.nextMatch(",");
            factor = Factor.parse(it);
            if (factor == null) {
                throw new ParseException("Syntax Error: factors expected after output keyword");
            }
            output_stmt.addChild(factor);
        }

        return output_stmt;
    }
}
