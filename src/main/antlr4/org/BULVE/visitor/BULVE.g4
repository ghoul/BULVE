grammar BULVE;

program
 : line+ EOF
 ;

line
 : functionDeclaration
 | statement
 ;

statement
 : variableDeclaration
 | integerDeclaration
 | stringDeclaration
 | decimalDeclaration
 | boolDeclaration
 | intArrayDeclaration
 | assignment
 | functionDeclaration
 | functionCall
 | systemFunctionCall
 | ifElseStatement
 | visiemsStatement
 | kolStatement
 | intArrayAdd
 | returnStatment

 ;

functionDeclaration
 : 'func ' IDENTIFIER '(' paramList? ')' functionBody
 ;

paramList : type IDENTIFIER (','  type IDENTIFIER)*
;

type
: 'var'
| 'siulas'
| 'sveikuolis'
| 'dvigubas'
| 'artikrai'
;

functionBody : '{'  statement* '}' ;

variableDeclaration
 : 'var' IDENTIFIER '=' expression
 ;

integerDeclaration
: 'sveikuolis' IDENTIFIER '=' expression
;

stringDeclaration
: 'siulas' IDENTIFIER '=' expression
;

decimalDeclaration
: 'dvigubas' IDENTIFIER '=' expression
;

boolDeclaration
: 'artikrai' IDENTIFIER '=' expression
;

intArrayDeclaration
: 'sveikuolis[]' IDENTIFIER '=' 'new sveikuolis['expression']'
;

intArrayAdd
: IDENTIFIER'['INTEGER']''=' expression
;

intArrayGet
: IDENTIFIER'['INTEGER']'
;

assignment
 : IDENTIFIER '=' expression
 ;
returnStatment
: 'grazink' expression?
;
functionCall
 : IDENTIFIER '(' expressionList? ')'
 ;

systemFunctionCall
 : PRINT '(' expression ')'                             #printFunctionCall
 ;

ifElseStatement : 'jeigu' '(' expression ')' block ('kitaip' block)? ; //('kitaip jeigu' block)*

kolStatement : 'kol' '(' expression ')' block;

visiemsStatement : 'visiems' '(' integerDeclaration';' IDENTIFIER numericCompareOp expression ';'
                       assignment ')' block ;

block : '{' statement* '}' ;

constant
: DECIMAL
| INTEGER
| BOOLEAN
| STRING
;

expressionList
 : expression (',' expression)*
 ;

expression //a=a-1
 : constant                                             #constantExpression
 |type                                                  #typeExpression
 | IDENTIFIER                                           #identifierExpression
 | '(' expression ')'                                   #parenthesesExpression
 | intArrayGet                                          #intArrayGetExpression
 | booleanUnaryOp expression                            #booleanUnaryOpExpression
 | expression booleanBinaryOp expression                #booleanBinaryOpExpression
 | expression numericMultiOp expression                 #numericMultiOpExpression
 | expression numericAddOp expression                   #numericAddOpExpression
 | expression stringBinaryOp expression                 #stringBinaryOpExpression
 | expression numericCompareOp expression               #numericCompareOpExpresion
 | functionCall                                         #functionCallExpression
 ;

booleanUnaryOp : '!' ;

booleanBinaryOp : '||' | '&&' ;

numericMultiOp : '*' | '/' | '%' ;

numericAddOp : '+' | '--' ;

numericCompareOp : '>' | '<' | '<=' | '>=' | '==' | '!=' ;

stringBinaryOp : '++' ; //sujungti du strings

PRINT : 'spausdink'; //spausdink

INTEGER : [-][0-9]+ | [0-9]+ ;
DECIMAL : [-][0-9]+ '.' [0-9]+ | [0-9]+ '.' [0-9]+ ;
BOOLEAN : 'true' | 'false' ; //tiesa|melas
STRING : ["] ( ~["\r\n\\] | '\\' ~[\r\n] )* ["] ;

IDENTIFIER : [&]?[a-zA-Z_][a-zA-Z_0-9]* ; //var name

COMMENT : ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip ;

WS : [ \t\f\r\n]+ -> skip ; //whitespace ir tt