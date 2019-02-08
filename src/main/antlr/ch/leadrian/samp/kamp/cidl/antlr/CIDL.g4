grammar CIDL;

@header {
package ch.leadrian.samp.kamp.cidl.antlr;
}

declarations
    : (declaration SEMICOLON)*?
    ;

declaration
    : constantDeclaration
    | functionDeclaration
    ;

constantDeclaration
    : CONST typeName constantName EQUALS constantExpression
    ;

constantName
    : IDENT
    ;

constantExpression
    : number
    | bool
    | character
    | string
    | constantReference
    ;

number
    : integer
    | octal
    | hexadecimal
    | decimal
    ;

integer
    : INT
    ;

octal
    : OCT
    ;

hexadecimal
    : HEX
    ;

decimal
    : DECIMAL
    ;

bool
    : BOOL
    ;

character
    : pchar
    | echar
    | achar
    | uchar
    ;

pchar
    : PCHAR
    ;

echar
    : ECHAR
    ;

achar
    : ACHAR
    ;

uchar
    : UCHAR
    ;

string
    : STRING
    ;

constantReference
    : IDENT
    ;

functionDeclaration
    : attributes? typeName functionName parameters
    ;

functionName
    : IDENT
    ;

attributes
    : LBRACK attributeList RBRACK
    ;

attributeList
    : attribute (COMMA attribute)*
    ;

attribute
    : attributeName (LPAREN constantExpression RPAREN)?
    ;

attributeName
    : IDENT
    ;

parameters
    : LPAREN parameterList? RPAREN
    ;

parameterList
    : parameter (COMMA parameter)*
    ;

parameter
    : attributes? typeName parameterName (EQUALS constantExpression)?
    ;

parameterName
    : IDENT
    ;

typeName
    : IDENT
    ;

CONST
    : 'const'
    ;

BOOL
    : 'true'
    | 'false'
    ;

DECIMAL
    : [+-]?(([0-9]* '.' [0-9]*)([eE][+-]?[0-9]+)?|[0-9]+[eE][+-][0-9]+)
    ;

HEX
    : '0' [xX][0-9a-fA-F]+
    ;

OCT
    : '0' [0-7]+
    ;

INT
    : [+-]?([1-9][0-9]+|[0-9])
    ;

PCHAR
    : '\''[^\b\f\n\r\t]'\''
    ;

ECHAR
    : '\\'[abfnrtv'"\\?]
    ;

ACHAR
    : '\'' '\\x' [0-9a-fA-F]+ '\''
    ;

UCHAR
    : '\'' '\\x' [0-9a-fA-F]+ '\''
    ;

STRING
    : '"' (.|'\n')*? '"'
    ;

COMMENT
    : '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

WS
    : [ \t\r\n\u000C]+ -> skip
    ;

IDENT
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

LPAREN
    : '('
    ;

RPAREN
    : ')'
    ;

LBRACK
    : '['
    ;

RBRACK
    : ']'
    ;

EQUALS
    : '='
    ;

COMMA
    : ','
    ;

SEMICOLON
    : ';'
    ;