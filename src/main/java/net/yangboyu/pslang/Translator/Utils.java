package net.yangboyu.pslang.Translator;

import net.yangboyu.pslang.Lexer.Token;
import net.yangboyu.pslang.Lexer.TokenType;
import net.yangboyu.pslang.Paser.ast.ASTNode;
import net.yangboyu.pslang.Paser.ast.ASTNodeTypes;
import net.yangboyu.pslang.Paser.ast.Variable;
import net.yangboyu.pslang.Paser.util.ParseException;
import net.yangboyu.pslang.Translator.symbol.Symbol;

public class Utils {
    public static void checkNodeDataTypeConsistency(ASTNode node) {
//        Token operator = node.getLexeme();
//        ASTNode leftNode = node.getChild(0);
//        ASTNode rightNode = node.getChild(1);
//        TokenType leftType = null;
//        TokenType rightType = null;
//        if (leftNode.getType() == ASTNodeTypes.VARIABLE) {
//            leftType = ((Variable)leftNode).getTypeLexeme().getType();
//        } else if (leftNode.get)
//
//        if (rightNode.getType() == ASTNodeTypes.VARIABLE) {
//            rightType = ((Variable)rightNode).getTypeLexeme().getType()
//        } else {
//            leftType =
//        }
    }

    public static Token assertEqualType(Symbol s1, Symbol s2) throws ParseException {

//        System.out.println(s1.getLexeme().getType());
//        System.out.println(s1.getLexeme().getValue() + "," + s2.getLexeme().getValue());
//        System.out.println(s2.getLexeme().getType());

        Token typeLexeme1 = null;
        Token typeLexeme2 = null;

        String val1 = s1.getLexeme().getValue();
        String val2 = s2.getLexeme().getValue();

        if (s1.getLexeme().getType() == TokenType.VARIABLE) {
            typeLexeme1 = s1.getTypeLexeme();
        } else {
            typeLexeme1 = s1.getLexeme();
        }

        if (s2.getLexeme().getType() == TokenType.VARIABLE) {
            typeLexeme2 = s2.getTypeLexeme();
        } else {
            typeLexeme2 = s2.getLexeme();
        }

//        System.out.println(typeLexeme1);
//        System.out.println(typeLexeme2);

        if (typeLexeme1.getType() == typeLexeme2.getType()) {
            if (typeLexeme1.getType() == TokenType.TYPE && !typeLexeme1.getValue().equals(typeLexeme2.getValue())) {
                throw new ParseException(String.format("Syntax Error: incompatible type with token %s and %s", val1, val2));
            }
            return typeLexeme1;
        }

        throw new ParseException(String.format("Syntax Error: incompatible type with token %s and %s", val1, val2));
    }

    public static Token getTypeDerivation(Symbol s1, Symbol s2) throws ParseException {
        // TODO
        return assertEqualType(s1, s2);
    }
}
