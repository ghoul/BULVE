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
 | functionCall
 | systemFunctionCall
 | ifElseStatement
 | visiemsStatement
 | kolStatement
 ;

functionDeclaration //TODO:vėliau ir tipą turės nurodyt gal
 : 'func' IDENTIFIER '(' paramList? ')' functionBody
 ;

paramList : IDENTIFIER (',' IDENTIFIER)* ;

functionBody : '{' statement* 'return' expression '}' ; //TODO cannot return from the midle of function

variableDeclaration
 : 'var' IDENTIFIER '=' expression
 ;

integerDeclaration
: 'sveikuolis' IDENTIFIER '=' INTEGER
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

functionCall
 : IDENTIFIER '(' expressionList? ')'
 ;

systemFunctionCall
 : PRINT '(' expression ')'                             #printFunctionCall
 ;

ifElseStatement : 'jeigu' '(' expression ')' block 'kitaip' block ;

kolStatement : 'kol' '(' expression ')' block;

visiemsStatement : 'visiems' '(' integerDeclaration';' IDENTIFIER numericCompareOp expression ';'
                       assignment ')' block ;

block : '{' statement* '}' ;

constant: INTEGER | DECIMAL | BOOLEAN |STRING ;

expressionList
 : expression (',' expression)*
 ;

expression

 : constant                                             #constantExpression
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