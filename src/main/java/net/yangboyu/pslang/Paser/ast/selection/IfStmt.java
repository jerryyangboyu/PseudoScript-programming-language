package net.yangboyu.pslang.Paser.ast.selection;

import net.yangboyu.pslang.Paser.ast.*;
import net.yangboyu.pslang.Paser.ast.expressions.Expr;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;

public class IfStmt extends Stmt {
    public IfStmt() {
        super(ASTNodeTypes.IF_STMT, "if");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        return IfStmt.parseIf(it);
    }

    public static ASTNode parseIf(PeekTokenIterator it) throws ParseException {
        var lexeme = it.nextMatch("IF");

        var IfStmt = new IfStmt();

        IfStmt.setLexeme(lexeme);

        var expr = Expr.parse(it);
        if(expr == null) {
            throw new ParseException("Syntax Error: Expression should be included inside if condition");
        }
        IfStmt.addChild(expr);

        it.nextMatch("THEN");

        var block = parseIfBlock(it);
        IfStmt.addChild(block);

        var tail = parseTail(it);

        if(tail != null) {
            IfStmt.addChild(tail);
        }

        it.nextMatch("ENDIF");

        return IfStmt;
    }

    public static ASTNode parseTail(PeekTokenIterator it) throws ParseException {

        if(!it.hasNext() || !it.peek().getValue().equals("ELSE")) {
            return null;
        }

        it.nextMatch("ELSE");

        var lookahead = it.peek();

        if(lookahead == null) {
            return null;
        }

        if (lookahead.getValue().equals("IF")) {
            return IfStmt.parseIf(it);
        } else if (lookahead.getValue().equals("ENDIF")) {
            return null;
        } else {
            return parseIfBlock(it);
        }
    }

    private static ASTNode parseIfBlock(PeekTokenIterator it) throws ParseException {
        var block = new Block();
        ASTNode stmt = null;
        var go = it.hasNext() && !it.peek().getValue().equals("ENDIF") && !it.peek().getValue().equals("ELSE");
        while (go && (stmt = Stmt.parseStmt(it)) != null) {
            block.addChild(stmt);
        }
        return block;
    }

    public ASTNode getExpr() {
        return this.getChild(0);
    }

    public ASTNode getBlock() {
        return this.getChild(1);
    }

    public ASTNode getElseBlock() {
        var block = this.getChild(2);
        if (block instanceof Block) {
            // if block exist
            return block;
        }
        return null;
    }

    public ASTNode getElseIfStmt() {
        var stmt = this.getChild(2);
        if (stmt instanceof IfStmt) {
            return stmt;
        }
        return null;
    }


}
