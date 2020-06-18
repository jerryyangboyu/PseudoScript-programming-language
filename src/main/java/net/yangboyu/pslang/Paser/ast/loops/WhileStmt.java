package net.yangboyu.pslang.Paser.ast.loops;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class WhileStmt extends Stmt {

    public WhileStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.WHILE_STMT, "while");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("WHILE");
        var whileStmt = new WhileStmt(parent);
        whileStmt.setLexeme(lexeme);

        var expr = Expr.parse(parent, it);
        whileStmt.addChild(expr);

        var block = Block.parse(parent, it);
        whileStmt.addChild(block);

        it.nextMatch("ENDWHILE");
        return whileStmt;
    }
}