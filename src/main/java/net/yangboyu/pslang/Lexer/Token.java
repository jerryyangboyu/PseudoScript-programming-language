package net.yangboyu.pslang.Lexer;

import net.yangboyu.pslang.Common.AlphabetHelper;
import net.yangboyu.pslang.Common.PeekIterator;

public class Token {
    TokenType _type;
    String _value;

    public Token(TokenType type, String value){
        this._type = type;
        this._value = value;
    }

    public TokenType getType(){
        return this._type;
    }

    public String getValue() { return this._value; }

    public boolean isVariable(){
        return this._type == TokenType.VARIABLE;
    }

    public boolean isScalar(){
        return this._type == TokenType.INTEGER ||
                this._type == TokenType.REAL ||
                this._type == TokenType.BOOLEAN ||
                this._type == TokenType.STRING;
    }

    public static Token makeVariableOrKeyword(PeekIterator<Character> it){
        StringBuilder s = new StringBuilder();

        // 最简单的有限状态机算法
        while(it.hasNext()){
            var lookahead = it.peek();
            if(AlphabetHelper.isLiteral(lookahead)){
                s.append(lookahead);
            }else{
                break;
            }
            it.next();
        }

        if(Keywords.isKeyword(s.toString())){
            return new Token(TokenType.KEYWORD, s.toString());
        }

        if(s.toString().equals("TRUE") || s.toString().equals("FALSE")){
            return new Token(TokenType.BOOLEAN, s.toString());
        }

        return new Token(TokenType.VARIABLE, s.toString());
    }

    public static Token makeString(PeekIterator<Character> it) throws LexicalException {
        StringBuilder s = new StringBuilder();
        int state = 0;
        while(it.hasNext()){
            char c = it.next();
            switch (state){
                case 0:
                {
                    if(c == '\'' || c == '"') {
                        if (c == '\'') {
                            state = 1;
                        } else {
                            state = 2;
                        }
                        s.append(c);
                    }
                    break;
                }
                case 1:
                {
                    if(c == '\''){
                        return new Token(TokenType.STRING, s.append(c).toString());
                    }else{
                        s.append(c);
                    }
                    break;
                }
                case 2:
                {
                    if(c == '\"'){
                        return new Token(TokenType.STRING, s.append(c).toString());
                    }else{
                        s.append(c);
                    }
                    break;
                }
            }
        } // end while
        throw new LexicalException("Unexpected String Parsing Error");
    }

    public static Token makeOperator(PeekIterator<Character> it) throws LexicalException {
        int state = 0;

        while(it.hasNext()) {
            var lookahead = it.next();

            switch (state) {
                case 0:
                    switch (lookahead) {
                        case '+':
                            state = 1;
                            break;
                        case '-':
                            state = 2;
                            break;
                        case '*':
                            state = 3;
                            break;
                        case '/':
                            state = 4;
                            break;
                        case '>':
                            state = 5;
                            break;
                        case '<':
                            state = 6;
                            break;
                        case '=':
                            state = 7;
                            break;
                        case '!':
                            state = 8;
                            break;
                        case '&':
                            state = 9;
                            break;
                        case '|':
                            state = 10;
                            break;
                        case '^':
                            state = 11;
                            break;
                        case '%':
                            state = 12;
                            break;
                        case ',':
                            return new Token(TokenType.OPERATOR, ",");
                        case ';':
                            return new Token(TokenType.OPERATOR,  ";");
                        case ':':
                            return new Token(TokenType.OPERATOR, ":");
                    }
                    break;
                case 1:
                    if(lookahead == '+') {
                        return new Token(TokenType.OPERATOR, "++");
                    } else if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "+=");
                    }else {
                        // 这里是其他字符了，记得返回
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "+");
                    }
                case 2:
                    if(lookahead == '-') {
                        return new Token(TokenType.OPERATOR, "--");
                    } else if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "-=");
                    }else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "-");
                    }
                case 3:
                    if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "*=");
                    }else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "*");
                    }
                case 4:
                    if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "/=");
                    }else if(lookahead == '/'){
                        return new Token(TokenType.OPERATOR, "//");
                    }else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "/");
                    }
                case 5:
                    if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, ">=");
                    }else if(lookahead== '>') {
                        return new Token(TokenType.OPERATOR, ">>");
                    } else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, ">");

                    }
                case 6:
                    if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "<=");
                    }else if(lookahead== '<') {
                        return new Token(TokenType.OPERATOR, "<<");
                    }else if(lookahead == '>') {
                        return new Token(TokenType.OPERATOR, "<>");
                    }else if(lookahead == '-') {
                        return new Token(TokenType.OPERATOR, "<-");
                    }else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "<");
                    }
                case 7:
                    if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "==");
                    } else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "=");
                    }
                case 8:
                    if(lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "!=");
                    } else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "!");
                    }
                case 9:
                    if(lookahead == '&') {
                        return new Token(TokenType.OPERATOR, "&&");
                    }  else if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "&=");
                    } else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "&");
                    }
                case 10:
                    if(lookahead == '|') {
                        return new Token(TokenType.OPERATOR, "||");
                    }  else if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "|=");
                    } else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "|");
                    }
                case 11:
                    if(lookahead == '^') {
                        return new Token(TokenType.OPERATOR, "^^");
                    }  else if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "^=");
                    } else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "^");
                    }
                case 12:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "%=");
                    } else {
                        it.putBack();
                        return new Token(TokenType.OPERATOR, "%");
                    }
            }

        }
        throw new LexicalException("Unexpected operator parsing error");

    }

    public static Token makeNumber(PeekIterator<Character> it) throws LexicalException {
        StringBuilder s = new StringBuilder();
        int state = 0;
        while(it.hasNext()){
            char lookahead = it.peek();

            switch (state){
                case 0:
                    if(lookahead == '0'){
                        state = 1;
                    }else if(AlphabetHelper.isNumber(lookahead)){
                        // 1-9
                        state = 2;
                    }else if(lookahead == '+' || lookahead == '-'){
                        state = 3;
                    }else if(lookahead == '.'){
                        state = 5;
                    }
                    break;
                case 1:
                    if(lookahead == '0'){
                        state = 1;
                    }else if(AlphabetHelper.isNumber(lookahead)){
                        state = 2;
                    }else if(lookahead == '.'){
                        state = 4;
                    }else {
                        return new Token(TokenType.INTEGER, s.toString());
                    }
                    break;
                    // 17:25 s没有进行递增，寻找下一种情况
                case 2:
                    if(AlphabetHelper.isNumber(lookahead)) {
                        state = 2;
                    }
                    else if(lookahead == '.') {
                        state = 4;
                    }
                    else {
                        return new Token(TokenType.INTEGER, s.toString());
                    }
                    break;
                case 3:
                    if(AlphabetHelper.isNumber(lookahead)) {
                        state = 2;
                    } else if(lookahead == '.') {
                        state = 5;
                    } else {
                        throw new LexicalException(lookahead);
                    }
                    break;
                case 4:
                    if(lookahead == '.') {
                        throw new LexicalException(lookahead);
                    }
                    else if(AlphabetHelper.isNumber(lookahead)) {
                        state = 20;
                    }
                    else {
                        return new Token(TokenType.REAL, s.toString());
                    }
                    break;
                case 5:
                    if(AlphabetHelper.isNumber(lookahead)) {
                        state = 20;
                    }
                    else {
                        throw new LexicalException(lookahead);
                    }
                    break;
                case 20:
                    if(AlphabetHelper.isNumber(lookahead)) {
                        state = 20;
                    }
                    else if(lookahead == '.') {
                        throw new LexicalException(lookahead);
                    }
                    else {
                        return new Token(TokenType.REAL, s.toString());
                    }
                    break;
            }

            s.append(lookahead);
            it.next();
        } // end while
        throw new LexicalException("Unexpected Number parsing Error");
    }

    @Override
    public String toString() {
        return String.format("type: %s value:%s", this._type, this._value);
    }

    public boolean isNumber() {
        return this._type == TokenType.REAL || this._type == TokenType.INTEGER;
    }

    public boolean isOperator() {
        return this._type == TokenType.OPERATOR;
    }

    public boolean isValue() {
        return isVariable() || isScalar();
    }

    public boolean isPrimaryType() {
        return this._value.equals("BOOLEAN") ||
                this._value.equals("INT") ||
                this._value.equals("INTEGER") ||
                this._value.equals("STRING") ||
                this._value.equals("REAL");
    }

    public boolean isType() {
        return this._value.equals("BOOLEAN") ||
                this._value.equals("INT") ||
                this._value.equals("INTEGER") ||
                this._value.equals("STRING") ||
                this._value.equals("REAL") ||
                this._value.equals("DATE") ||
                this._value.equals("CURRENCY");
    }
}
