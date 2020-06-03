package net.yangboyu.pslang.Paser.ast;

public enum  ASTNodeTypes {
    BLOCK,
    BINARY_EXPR, // 1 + 1
    UNARY_EXPR, // ++1
    VARIABLE,
    SCALAR, // 1.0, true
    IF_STMT,
    WHILE_STMT,
    FOR_STMT,
    ASSIGN_STMT,
    FUNCTION_DECLARE_STMT,
    DECLARE_STMT,
    RETURN_STMT,
    CALL_EXPR,
    INPUT_STMT,
    OUTPUT_STMT
}
