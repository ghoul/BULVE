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
 | assignment
 | functionDeclaration
 | functionCall
 //| nestedFunctionCall
 | systemFunctionCall
 | ifElseStatement
 | visiemsStatement
 | kolStatement
 | returnStatment

 ;

functionDeclaration
 : 'func ' IDENTIFIER '(' paramList? ')' functionBody // type IDENTIFIER ('(' paramList? ')')+
 ;

paramList : type? IDENTIFIER (','  type? IDENTIFIER)* //type?
;

type
: 'var'
| 'siulas'
| 'sveikuolis'
| 'dvigubas'
| 'artikrai'
;

functionBody : '{'  statement* '}' ; //(expression* statement* expression*)*

variableDeclaration
 : 'var' IDENTIFIER '=' expression
 ;

integerDeclaration
: 'sveikuolis' IDENTIFIER '=' expression //| INTEGER //INTEGER | expression //kaip uztikrint kad int expression?
;

stringDeclaration
: 'siulas' IDENTIFIER '=' expression //| STRING //expression |
;

decimalDeclaration
: 'dvigubas' IDENTIFIER '=' expression //|DECIMAL
;

boolDeclaration
: 'artikrai' IDENTIFIER '=' expression //|BOOLEAN
;

assignment
 : IDENTIFIER '=' expression    //#assignmentExpression
 ;
returnStatment
: 'return' expression?
;
functionCall
 : IDENTIFIER '(' expressionList? ')' //nestedExpressionList?  //a=a--1
 ;
/*nestedFunctionCall
: IDENTIFIER('.'IDENTIFIER)+'('expressionList?')' //iskvietimas nested funkciju su parametrais jom duotais
;*/
systemFunctionCall
 : PRINT '(' expression ')'                             #printFunctionCall
 ;

ifElseStatement : 'jeigu' '(' expression ')' block ('kitaip' block)? ;

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
/*nestedExpressionList
: ('(' expressionList?')')* //is sito zinosim, ar yra nested funkciju - TODO nereikalingas isvis, nebent reiks, kad viska vykdyt reikes
;*/
expression //a=a-1
 : constant                                             #constantExpression
 |type                                                  #typeExpression
 | IDENTIFIER                                           #identifierExpression
 | '(' expression ')'                                   #parenthesesExpression
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

stringBinaryOp : '+' ; //sujungti du strings

PRINT : 'print'; //spausdink

INTEGER : [-][0-9]+ | [0-9]+ ; //TODO - support negative numbers - done?
DECIMAL : [-][0-9]+ '.' [0-9]+ | [0-9]+ '.' [0-9]+ ;
BOOLEAN : 'true' | 'false' ; //tiesa|melas
STRING : ["] ( ~["\r\n\\] | '\\' ~[\r\n] )* ["] ;

IDENTIFIER : [&]?[a-zA-Z_][a-zA-Z_0-9]* ; //var name

COMMENT : ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip ;

WS : [ \t\f\r\n]+ -> skip ; //whitespace ir tt