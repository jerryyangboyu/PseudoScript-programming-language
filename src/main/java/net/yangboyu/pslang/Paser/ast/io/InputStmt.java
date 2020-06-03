package net.yangboyu.pslang.Paser.ast.io;

import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Stmt;
import net.yangboyu.pslang.Paser.ast.Variable;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class InputStmt extends Stmt {
    public InputStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.INPUT_STMT, "input");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var input_stmt = new InputStmt(parent);
        var lexeme = it.nextMatch("INPUT");
        input_stmt.setLexeme(lexeme);

        // match for <var1>
        ASTNode variable = Variable.parse(it);
        if (variable != null) {
            input_stmt.addChild(variable);
        } else {
            throw new ParseException("Syntax Error: variables expected after INPUT keyword");
        }

        // match pattern for , <var2>, <var3> ...
        while(it.hasNext() && it.peek().getValue().equals(",")) {
            it.nextMatch(",");
            variable = Variable.parse(it);
            if (variable == null) {
                throw new ParseException("Syntax Error: variables expected after INPUT keyword");
            }
            input_stmt.addChild(variable);
        }

        return input_stmt;
    }
}
