package net.yangboyu.pslang.Paser.ast.expressions;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Factor;
import net.yangboyu.pslang.Paser.util.ExprHOF;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Paser.util.PeekTokenIterator;
import net.yangboyu.pslang.Paser.util.PriorityTable;

public class Expr extends ASTNode {

    private static PriorityTable table = new PriorityTable();

    public Expr(ASTNode _parent){
        super(_parent);
    }

    public Expr(ASTNode parent, ASTNodeTypes types, Token lexeme){
        super(parent);
        this.type = types;
        this.lexeme = lexeme;
        this.label = lexeme.getValue();
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParseException {
        return E(parent, 0, it);
    }

    /*
    * left: E(k) -> E(k) op(k) E(k+1) | E(k+1)
    * right: E(k) -> E(K+1) E_(k) || E(t) -> F E_(t) | U E_(t)
    *           E_(K) -> op(k) E(k+1) _E(k) | null || E_(t) -> op(t) E(t) _E(t) | null
    *               F -> [0-9] | (E)
    * */
    private static ASTNode E(ASTNode parent, int k, PeekTokenIterator it) throws ParseException {
        if (k < table.size() - 1) {
            return combine(parent, () -> E(parent, k+1, it), () -> E_(parent, k, it), it);
        } else {
            return race(
                    () -> combine(parent, () -> F(parent, it), () -> E_(parent, k, it), it),
                    () -> combine(parent, () -> U(parent, it), () -> E_(parent, k, it), it),
                    it);
        }
    }

    private static ASTNode E_(ASTNode parent, int k, PeekTokenIterator it) throws ParseException {
        var token = it.peek();
        var value = token.getValue();

        if (table.get(k).indexOf(value) != -1){
            Expr expr = new Expr(parent, ASTNodeTypes.BINARY_EXPR, it.nextMatch(value));
            expr.addChild(combine(parent, () -> E(parent, k+1, it), () -> E_(parent, k, it), it));
            return expr;
        }

        return null;
    }

    private static ASTNode F(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var factor = Factor.parse(it);

        // 解析失败
        if (factor == null) {
            return null;
        }

        //函数调用
        if (it.hasNext() && it.peek().getValue().equals("(")) {
            return CallExpr.parse(parent, it, factor);
        }

        //正常的表达式因子
        return factor;
    }

    private static ASTNode U(ASTNode parent, PeekTokenIterator it) throws ParseException {
        var token = it.peek();
        var value = token.getValue();

        if (value.equals("(")) {
            it.nextMatch("(");
            var expr = E(parent, 0, it);
            it.nextMatch(")");
            return expr;
        } else if (value.equals("++") || value.equals("--") || value.equals("!")) {
            var t = it.peek();
            it.nextMatch(value);
            Expr unaryExpr = new Expr(parent, ASTNodeTypes.UNARY_EXPR, t);
            unaryExpr.addChild(E(parent, 0, it));
            return unaryExpr;
        }
        return null;
    }

    private static ASTNode race(ExprHOF aFunc, ExprHOF bFunc, PeekTokenIterator it) throws ParseException {
        if (!it.hasNext()){
            return null;
        }
        var a = aFunc.hoc();
        if (a != null) {
            return a;
        }else{
            return bFunc.hoc();
        }
    }

    private static ASTNode combine(ASTNode parent, ExprHOF aFunc, ExprHOF bFunc, PeekTokenIterator it) throws ParseException {
        var a = aFunc.hoc();
        if(a == null) {
            return it.hasNext() ? bFunc.hoc() : null;
        }

        var b = it.hasNext() ? bFunc.hoc() : null;
        if (b == null) {
            return a;
        }

//        Expr expr = new Expr(parent); // need pass parent
//        expr.lexeme = b.lexeme; // E(k) -> E(K+1) E_(k); b -> E_(k)
//        expr.label = b.label;
        Expr expr = new Expr(parent, ASTNodeTypes.BINARY_EXPR, b.getLexeme());
        expr.addChild(a);
        expr.addChild(b.getChild(0)); // b -> E_(k); E_(K) -> op(k) E(k+1) _E(k) | null

        return expr;
    }


}
