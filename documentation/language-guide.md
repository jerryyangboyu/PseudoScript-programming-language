# Language Guide For PseudoScript Programming language

## Part I Basic Syntax
### Chapter I Basic IO
#### Stdio

##### Input Statement
Get the user input from interactive console, and assign the value to variable.

Syntax:
```
INPUT <variable>, <variable> , ...
```
Derived formula:
```
Token<Keyword> INPUT
loop 
match Token<Variable>
until Token<null>
throws Exception<ParserException>
```

##### Output Statement
Output the value stored in the variable to interactive console.

Syntax:
```
OUTPUT <variable>, <variable> , ...
```
Derived formula:
```
Token<Keyword> OUTPUT
loop 
match Token<Variable>
until Token<null>
throws Exception<ParserException>
```

#### File IO
##### File-handler
File handler are responsible for open and close of file stored on permanent storage devices.

Syntax:
```
OPENFILE <filename>, <mode>
```
Parameters:
- **filename**: *string*, the absolute address of file stored on the devices.
- **mode**: *string*, 'r' for read and 'w' for write

Derived formula:
```
Token<Keyword> OPENFILE
match Token<string>
match Token<string>
throws Exception<ParserException>
```

##### File-in-stream
Read one line of the file stored in permanent storage devices and stored the value read from the file in variable.

Syntax:
```
READFILE <filename>, <variable>
```
Parameters:
- **filename**: *string*, the absolute address of file stored on the devices.

Derived formula:
```
Token<Keyword> READFILE
match Token<string>
match Token<variable>
throws Exception<ParserException>
```

##### File-out-stream
Write the value stored in the variable to the file.

Syntax:
```
WRITEFILE <filename>, <variable>
```
Parameters:
- **filename**: *string*, the absolute address of file stored on the devices.

Derived formula:
```
Token<Keyword> WRITEFILE
match Token<string>
match Token<variable>
throws Exception<ParserException>
```

##### File EOF marker
Build-in function, return FALSE when it comes to the end of file

Syntax:
```
EOF(<filename>)
```
Parameters:
- **filename**: *string*, the absolute address of file stored on the devices.

Derived formula:
```
Token<Keyword> EOF
match (
match Token<string>
match )
throws Exception<ParserException>
```

### Chapter II Basic Data Types and Expressions
#### Primary Data Types

| Description of data | Syntax | Comments |
| :-------------: |:-------------:| :-----:|
| Whole signed Numbers | ```INT``` |  |
| Signed decimals | ```REAL```      |  |
| Single alphabet | ```CHAR``` |  |
| A sequence of alphabet characters | ```STRING``` | "I am a string" |
| Logical values | ```BOOLEAN``` | TRUE or FALSE |

#### In-built DataTypes
> Warning: This feature is in plan

| Description of data | Syntax | Comments |
| :-----------------: | :----: | :------: |
| Date and time       | ```DATE``` | #16/05/2017# |
| Currency            | ```CURRENCY``` | In plan |

#### User-defined Data Types
##### Record
User defined types, like struct in C++/C

Syntax:
```
TYPE <typeName>
    DECLARE <variableName> : <dataType>
    DECLARE <variableName> : <dataType>
    ...
ENDTYPE
```
Derived formula:
```
Token<Keyword> TYPE
match Token<variable>
loop
parse Declare<ASTNode>
until Token<Keyword> ENDTYPE
match Token<Keyword> ENDTYPE
throws Exception<ParserException>
```

##### Enumerations
An enumerated data type defines a list of possible values

Syntax:
```
TYPE 
<enumName> = (<val1>, <val2>, <val3>, ...)
```
Derived formula:
```
Token<Keyword> TYPE
match Token<variable>
match (
loop
match Token<variable>
match ,
until Token<Keyword> )
match Token<Keyword> )
throws Exception<ParserException>
```

##### Pointer
A variable stores the memory address. Used to reference a memory location
> Warning: This feature is in plan

Syntax:
```
TYPE
<pointerTypeName> = ^<typeName>
```
Derived formula:
```
Token<Keyword> TYPE
match Token<variable>
match = 
match ^
match Token<dataType>
throws Exception<ParserException>
```

##### Declare Statement
Declare a variable with certain data type without assign value to the variable.

Syntax:
```
DECLARE <variableName> : <typeDeclatationExpr>
DECLARE <variableName> : <primaryDataType | inbuiltDataType | userDefinedDataType>
DECLARE <variableName> : (<primaryDataType | inbuiltDataType | userDefinedDataType>, ...) // for composite datatype declaration
DECLARE <variableName> : <int> ... <int> // for declaration of range in int value
DECLARE <variableName> : ARRAY[<lowerBound>:<upperBound>] OF <primaryDataType | inbuiltDataType | userDefinedDataType>
```
Derived formula:
```

```

#### Expressions
##### SymbolTable
| Description of data | Syntax | Comments |
| :-------------: |:-------------:| :-----:|
| plus | ```+``` |  |
| minus | ```-```      |  |
| multiply | ```*``` |  |
| division | ```/``` | |
| power | ```^``` |  |
| left shift | ```<<``` | |
| right shift | ```>>``` | |
| and gate | ```&``` | |
| or gate | 1 | |
| and logic | ```AND``` | |
| or logic | ```OR``` | |
| bigger | ```>``` | |
| bigger or equal than | ```>=``` | |
| smaller | ```<``` | |
| smaller or equal than | ```<=``` | |
| equal | ```=``` | reloaded |
| not equal | ```<>``` or ```!=``` | |
| access operator | ```.``` | |
| expand operator | ```..``` | |
| index access operator | ```[<facotor>]``` | |

##### Boolean Expressions
Boolean expressions involves calculation of boolean variable, or boolean scalars

##### Arithmetic Expressions
Arithmetic Expressions involves calculation of arithmetic variable and scalar

##### Derived Formula
Left recursion:
```
left: E(k) -> E(k) op(k) E(k+1) | E(k+1)
right: E(k) -> E(K+1) E_(k) || E(t) -> F E_(t) | U E_(t)
       E_(K) -> op(k) E(k+1) _E(k) | null || E_(t) -> op(t) E(t) _E(t) | null
       F -> [0-9] | (E)
``` 

### Chapter III Loops and Iteration
#### For-loop
For-loop is a count control loop, usually temp variable i is used for counting

Syntax:
```
FOR i <- <int> TO <int> [STEP <int>]
    <Block>
ENDFOR
```
Derived formula:
```
Token<Keyword> FOR
match Token<variable>
match Token<Operator.ASSIGN>
match Token<int>
match Token<Keyword> TO
match Token<int>
if typeof(nextToken) = Token<Keyword> then
    match Token<Keyword> STEP
    match Token<int>
endif
parse Block<ASTNode>
match Token<Keyword> ENDFOR
throws Exception<ParserException>
```

#### While-loop
While-loop is a pre-condition loop // further explanation is required 

Syntax:
```
WHILE <Expr>
    <Block>
ENDWHILE
```
Derived formula:
```
Token<Keyword> WHILE
parse Expr<ASTNode>
parse Block<ASTNode>
match Token<Keyword> ENDWHILE
throws Exception<ParserException>
```

#### Repeat-Until
Repeat-Until is a post-condition loop // further explanation is required 

Syntax:
```
REPEAT
    <Block>
UNTIL <Expr>
```
Derived formula:
```
Token<Keyword> REPEAT
parse Block<ASTNode>
match Token<Keyword> UNTIL
parse Expr<ASTNode>
throws Exception<ParserException>
```

### Chapter IV Functions and Procedures
#### Functions
A sub-routine that returns values

Syntax:
```
FUNCTION <functionName> ([<passMode>] <parameterName> : <dataType>, [<passMode>] <parameterName> : <dataType>, ...) RETURNS <dataType>
    <Block>
        <Return>
ENDFUNCTION
```

Derived Formula:

Common args part:
```
loop
    if typeof(nextToken) = Token<Keyword>
        assert nextToken.value = "BYVAL" | "BYREF"
    endif
    
    match Token<variable>
    match :
    match Token<dataType>
    match ,
until )
```
Function declaration part:
```
Token<Keyword> FUNCTION
match Token<variable>
match (
parse Args<ASTNode>
match )
match Token<Keyword> RETURNS
match Token<dataType>
parse Block
match Token<Keyword> ENDFUNCTION
```

#### Return Statement
Return a value and terminate the execution until that line in a scope.

Syntax:
```
RETURN <variable> | <scalar>
```

Derived Formula:
```
match Token<Keyword> RETURN
match Token<Factor> -> Token<variable> | Token<scalar>
```

#### Procedures
A standard sub-routine

Syntax:
```
PROCEDURE <procedureName> ([<passMode>] <parameterName> : <dataType>, [<passMode>] <parameterName> : <dataType>, ...)
    <Block>
ENDPROCEDURE
```
Derived Formula:
```
Token<Keyword> PROCEDURE
match Token<variable>
match (
parse Args<ASTNode>
match )
parse Block
match Token<Keyword> ENDPROCEDURE
```
