package net.yangboyu.pslang.Translator.symbol;

import java.util.ArrayList;
import java.util.Hashtable;

public class StaticSymbolTable {

    private Hashtable<String, Symbol> offsetMap; // �ô��ط���ÿһ��Symbol��Ԫ,����õ�
    private ArrayList<Symbol> symbols;
    private int offsetCounter = 0;

    public StaticSymbolTable() {
        symbols = new ArrayList<>();
        offsetMap = new Hashtable<>();
    }

    public void add(Symbol symbol) {
        var lexemeVal = symbol.getLexeme().getValue();

        if(!offsetMap.containsKey(lexemeVal)) {
            // ������������ͷ���
            offsetMap.put(lexemeVal, symbol);
            symbol.setOffset(offsetCounter++);
            symbols.add(symbol);
        } else {
            var sameSymbol = offsetMap.get(lexemeVal);
            symbol.setOffset(sameSymbol.offset); // �ظ��ĳ�������ָ��ͬһ���ڴ��ַ
        }
    }

    // ���س��������ڴ�ռ�ô�С
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
