package net.yangboyu.pslang.Translator.symbol;

import net.yangboyu.pslang.Lexer.Token;

public class Symbol {
    // union
    SymbolTable parent;
    Token lexeme;
    Token typeLexeme;
    String label;

    int offset;
    int layerOffset = 0;

    SymbolType type;

    public Symbol(SymbolType type) {
        this.type = type;
    }

    public static Symbol createAddressSymbol(Token lexeme, int offset) {
        var symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.lexeme = lexeme;
        symbol.offset = offset;
        return symbol;
    }

    public static Symbol createImmediateSymbol(Token lexeme) {
        var symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.lexeme = lexeme;
        return symbol;
    }

    public static Symbol createLabelSymbol(String label, Token lexeme) {
        var symbol = new Symbol(SymbolType.LABEL_SYMBOL);
        symbol.label = label;
        symbol.lexeme = lexeme;
        return symbol;
    }

    public Symbol copy() {
        var symbol = new Symbol(this.type);
        symbol.typeLexeme = this.typeLexeme;
        symbol.lexeme = this.lexeme;
        symbol.label = this.label;
        symbol.offset = this.offset;
        symbol.layerOffset = this.layerOffset;
        symbol.type = this.type;
        return symbol;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }

    public SymbolType getType(){
        return this.type;
    }

    @Override
    public String toString() {
        switch (this.type) {
            case ADDRESS_SYMBOL:
            case IMMEDIATE_SYMBOL:
                return lexeme.getValue();
            case LABEL_SYMBOL:
                return label;
        }
        return "";
    }

    public void setLexeme(Token lexeme) {
        this.lexeme = lexeme;
    }

    public int getOffset() {
        return this.offset;
    }

    public Token getLexeme() {
        return this.lexeme;
    }

    public void setLayerOffset(int offset) {
        this.layerOffset = offset;
    }

    public int getLayerOffset(){
        return this.layerOffset;
    }

    public String getLabel() {
        return this.label;
    }

    public void setTypeLexeme(Token type) {
        this.typeLexeme = type;
    }

    public Token getTypeLexeme() {
        return this.typeLexeme;
    }

}
