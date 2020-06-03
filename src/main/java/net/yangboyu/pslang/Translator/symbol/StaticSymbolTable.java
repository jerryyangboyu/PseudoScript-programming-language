package net.yangboyu.pslang.Translator.symbol;

import java.util.ArrayList;
import java.util.Hashtable;

public class StaticSymbolTable {

    private Hashtable<String, Symbol> offsetMap; // 用词素访问每一个Symbol单元,查表用的
    private ArrayList<Symbol> symbols;
    private int offsetCounter = 0;

    public StaticSymbolTable() {
        symbols = new ArrayList<>();
        offsetMap = new Hashtable<>();
    }

    public void add(Symbol symbol) {
        var lexemeVal = symbol.getLexeme().getValue();

        if(!offsetMap.containsKey(lexemeVal)) {
            // 如果不包含，就放入
            offsetMap.put(lexemeVal, symbol);
            symbol.setOffset(offsetCounter++);
            symbols.add(symbol);
        } else {
            var sameSymbol = offsetMap.get(lexemeVal);
            symbol.setOffset(sameSymbol.offset); // 重复的常量引用指向同一个内存地址
        }
    }

    // 返回常量区的内存占用大小
    public int size() {
        return this.symbols.size();
    }

    public ArrayList<Symbol> getSymbols() {
        return this.symbols;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < symbols.size(); i++){
            s.append(i).append(":").append(this.symbols.get(i).toString()).append('\n');
        }
        return s.toString();
    }
}
