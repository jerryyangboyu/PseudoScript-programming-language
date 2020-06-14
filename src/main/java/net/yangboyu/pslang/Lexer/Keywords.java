package net.yangboyu.pslang.Lexer;

import java.util.Arrays;
import java.util.HashSet;

public class Keywords {
    static String[] _keyword = {
            // LOOP
            "FOR",
            "ENDFOR",
            "TO",
            "STEP",
            "WHILE",
            "ENDWHILE",
            "REPEAT",
            "UNTIL",

            // SELECTION
            "IF",
            "THEN",
            "ELSE",
            "ENDIF",
            "CASE",
            "OF",
            "ENDCASE",

            // MODULE
            "FUNCTION",
            "RETURNS",
            "RETURN",
            "BYREF",
            "BYVAL",
            "ENDFUNCTION",
            "PROCEDURE",
            "ENDPROCEDURE",

            // IO
            "INPUT",
            "OUTPUT",
            "READ",
            "WRITE",

            // PRIMARY DATA TYPE
            "INT",
            "REAL",
            "STRING",
            "CHAR",
            "BOOLEAN",

            // COMPOSITE DATA TYPE
            "DATE",
            "CURRENCY",

            // USER DEFINED DATA TYPE
            "ENUM",
            "ENDENUM",
            "TYPE",
            "ENDTYPE",

            // DECLARE
            "DECLARE",
    };

    static HashSet<String> set = new HashSet<>(Arrays.asList(_keyword));

    public static boolean isKeyword(String keyword){
        return set.contains(keyword);
    }
}
