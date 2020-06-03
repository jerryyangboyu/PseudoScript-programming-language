package net.yangboyu.pslang.Translator;

import net.yangboyu.pslang.Translator.symbol.StaticSymbolTable;
import net.yangboyu.pslang.Translator.symbol.SymbolTable;
import net.yangboyu.pslang.Translator.symbol.SymbolType;

import java.util.ArrayList;

public class TAProgram {
    private ArrayList<TAInstruction> instructions = new ArrayList<>();
    private int labelCounter = 0;
    private StaticSymbolTable staticSymbolTable = new StaticSymbolTable();

    public void add(TAInstruction code) {
        instructions.add(code);
    }

    public ArrayList<TAInstruction> getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (var instruction : instructions) {
            s.append(instruction.toString()).append('\n');
        }
        return s.toString();
    }

    public void setStaticSymbolTables(SymbolTable symbolTable) {
        for (var symbol : symbolTable.getSymbols()) {
            if (symbol.getType() == SymbolType.IMMEDIATE_SYMBOL) {
                this.staticSymbolTable.add(symbol);
            }
        }

        for (var child : symbolTable.getChildren()) {
            this.setStaticSymbolTables(child);
        }
        // end
    }
}
