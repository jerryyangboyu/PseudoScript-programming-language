package net.yangboyu.pslang.Paser.ast;

import net.yangboyu.pslang.Lexer.Token;
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

    public ASTNode(ASTNode _parent){
        this.parent = _parent;
    }

    public ASTNode(ASTNode _parent, ASTNodeTypes _type, String _label){
        this.parent = _parent;
        this.type = _type;
        this.label = _label;
    }

    public ASTNode() {

    }

    public void setLabel(String _label){
        this.label = _label;
    }

    public ASTNode getChild(int index){
        return this.children.get(index);
    }

    public void addChild(ASTNode node){
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
