package net.yangboyu.pslang.Translator.symbol;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;

import java.util.ArrayList;

public class SymbolTable {

    public SymbolTable parent = null;

    private ArrayList<SymbolTable> children;
    private ArrayList<Symbol> symbols;

    private int tempIndex = 0;
    private int offsetIndex = 0;
    public int level = 0;

    public SymbolTable() {
        this.children = new ArrayList<>();
        this.symbols = new ArrayList<>();
    }

    public void addSymbol(Symbol symbol) {
        this.symbols.add(symbol);
        symbol.parent = this;
    }

    public boolean exists(Token lexeme) {
        var _symbol = this.symbols.stream().filter(x -> x.lexeme.getValue().equals(lexeme.getValue())).findFirst();

        if(_symbol.isPresent()) {
            return true;
        }

        if(this.parent != null) {
            return this.parent.exists(lexeme);
        }

        return false;
    }

    public Symbol cloneFromSymbolTree(Token lexeme, int layoutOffset) {
        var _symbol = this.symbols.stream()
                .filter(x -> x.lexeme.getValue().equals(lexeme.getValue()))
                .findFirst();

        if(_symbol.isPresent()) {
            var symbol = _symbol.get().copy();
            symbol.setLayerOffset(layoutOffset);
            return symbol;
        }

        if(this.parent != null) {
            return this.parent.cloneFromSymbolTree(lexeme, layoutOffset + 1);
        }

        return null;
    }

    public Symbol createSymbolByLexeme(Token lexeme) {
        Symbol symbol = null;

        if(lexeme.isScalar()) {
            symbol =  Symbol.createImmediateSymbol(lexeme);
        } else {
            symbol = this.cloneFromSymbolTree(lexeme, 0);
            if (symbol == null) {
                symbol =  Symbol.createAddressSymbol(lexeme, this.offsetIndex++);
            }
        }

        this.symbols.add(symbol);

        return symbol;
    }

    // create temp variable such as p0 p1
    public Symbol createVariable() {
        var lexeme = new Token(TokenType.VARIABLE, "p" + this.tempIndex++);
        var symbol = Symbol.createAddressSymbol(lexeme, this.offsetIndex++);
        this.addSymbol(symbol);
        return symbol;
    }

    public void addChild(SymbolTable child) {
        child.parent = this;
        child.level = this.level + 1;
        this.children.add(child);
    }

    public int localSize() {
        return this.offsetIndex;
    }

    public ArrayList<Symbol> getSymbols() {
        return this.symbols;
    }

    public ArrayList<SymbolTable> getChildren() {
        return this.children;
    }

    public void createLabel(String label, Token lexeme) {
        var labelSymbol = Symbol.createLabelSymbol(label, lexeme);
        this.addSymbol(labelSymbol);
    }


}
