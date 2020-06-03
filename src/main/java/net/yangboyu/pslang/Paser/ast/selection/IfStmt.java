package net.yangboyu.pslang.Paser.ast.selection;

import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class IfStmt extends Stmt {
    public IfStmt(ASTNode _parent) {
        super(_parent, ASTNodeTypes.IF_STMT, "if");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        return IfStmt.parseIf(parent, it);
    }

    public static ASTNode parseIf(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("if");

        var IfStmt = new IfStmt(parent);

        IfStmt.setLexeme(lexeme);

        it.nextMatch("(");

        var expr = Expr.parse(parent, it);
        if(expr == null) {
            throw new ParseException("Syntax Error: Expression should be included inside if condition");
        }

        IfStmt.addChild(expr);

        it.nextMatch(")");

        var block = Block.parse(parent, it);

        IfStmt.addChild(block);

        var tail = parseTail(parent, it);

        if(tail != null) {
            IfStmt.addChild(tail);
        }

        return IfStmt;
    }

    public static ASTNode parseTail(ASTNode parent, PeekTokenIterator it) throws ParseException {

        if(!it.hasNext() || !it.peek().getValue().equals("else")) {
            return null;
        }

        it.nextMatch("else");

        var lookahead = it.peek();

        if(lookahead == null) {
            return null;
        }

        if (lookahead.getValue().equals("if")) {
            return IfStmt.parseIf(parent, it);
        } else if(lookahead.getValue().equals("{")) {
            return Block.parse(parent, it);
        } else {
            return null;
        }

    }
}
