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
 | systemFunctionCall
 | ifElseStatement
 | visiemsStatement
 | kolStatement
 | returnStatment

 ;

functionDeclaration
 : 'func ' IDENTIFIER '(' paramList? ')' functionBody // type IDENTIFIER
 ;

paramList :  IDENTIFIER (','  IDENTIFIER)* //type
;

type
: 'var'
| 'siulas'
| 'sveikuolis'
| 'dvigubas'
| 'artikrai'
;

functionBody : '{' statement*   '}' ; //expression*

variableDeclaration
 : 'var' IDENTIFIER '=' expression
 ;

integerDeclaration
: 'sveikuolis' IDENTIFIER '=' INTEGER | expression //kaip uztikrint kad int expression?
;

stringDeclaration
: 'siulas' IDENTIFIER '=' STRING
;

decimalDeclaration
: 'dvigubas' IDENTIFIER '=' DECIMAL
;

boolDeclaration
: 'artikrai' IDENTIFIER '=' BOOLEAN
;

assignment
 : IDENTIFIER '=' expression    //#assignmentExpression //+ neina mazint variables, nes minusa skaito kaip neigiama skaiciu
 //TODO:padaryt, kad jei randa minusini variable, darytu sudeti su minusiniu skaicium? ir jeigu -5+2 nesupranta
 ;
returnStatment
: 'return' expression?
;
functionCall
 : IDENTIFIER '(' expressionList? ')' //a=a--1
 ;

systemFunctionCall
 : PRINT '(' expression ')'                             #printFunctionCall
 ;

ifElseStatement : 'jeigu' '(' expression ')' block ('kitaip' block)? ;

kolStatement : 'kol' '(' expression ')' block;

visiemsStatement : 'visiems' '(' integerDeclaration';' IDENTIFIER numericCompareOp expression ';'
                       assignment ')' block ;

block : '{' statement* '}' ;

constant: INTEGER | DECIMAL | BOOLEAN |STRING ;

expressionList
 : expression (',' expression)*
 ;

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

IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]* ; //var name

COMMENT : ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip ;

WS : [ \t\f\r\n]+ -> skip ; //whitespace ir tt