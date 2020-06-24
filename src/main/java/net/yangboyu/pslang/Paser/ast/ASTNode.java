package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Paser.util.ParseException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ASTNode {
    /*Tree*/
    protected ArrayList<ASTNode> children = new ArrayList<>();
    protected ASTNode parent;

    /*Key information*/
    protected Token lexeme; // 词法单元
    protected String label; // 备注标签
    protected ASTNodeTypes type;

    public Object getProp(String key) {
        if (!this._props.containsKey(key)) {
            return null;
        }
        return this._props.get(key);
    }

    public void setProp(String key, Object val) {
        this._props.put(key, val);
    }

    protected HashMap<String, Object> _props = new HashMap<>();

    public ASTNode(ASTNodeTypes _type, String _label){
        this.type = _type;
        this.label = _label;
    }

    public ASTNode() {

    }

    public void setLabel(String _label){
        this.label = _label;
    }

    public String getLabel() {return this.label;}

    public ASTNode getChild(int index) {
        if (index >= this.children.size() || index < 0) {
            return null;
            // System.out.println(String.format("Oops, index out of range for getChild, index: %s, max index: %s", index, this.children.size()));
        }
        return this.children.get(index);
    }

    public void addChild(ASTNode node){
        node.parent = this;
        this.children.add(node);
    }

    public void setType(ASTNodeTypes _type) {
        this.type = _type;
    }

    public ASTNodeTypes getType() {
        return this.type;
    }

    public Token getLexeme(){
        return this.lexeme;
    }

    public void setLexeme(Token _lexeme){
        this.lexeme = _lexeme;
    }

    public ArrayList<ASTNode> getChildren(){
        return this.children;
    }

    public boolean isValueType() {
        return this.type == ASTNodeTypes.SCALAR || this.type == ASTNodeTypes.VARIABLE;
    }

    public void print(int indent) {
        System.out.println(StringUtils.leftPad(" ", indent) + this.label);
        for (var child:children) {
            child.print(indent + 1);
        }
    }

}
