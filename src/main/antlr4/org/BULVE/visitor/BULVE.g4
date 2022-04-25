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
 | assignment
 | functionCall
 | systemFunctionCall
 | ifElseStatement
 | kolStatement
 | visiemsStatement
 ;

functionDeclaration
 : 'func' IDENTIFIER '(' paramList? ')' functionBody
 ;

paramList : IDENTIFIER (',' IDENTIFIER)* ;

functionBody : '{' statement* 'return' expression '}' ; //TODO cannot return from the midle of function

variableDeclaration
 : 'var' IDENTIFIER '=' expression
 | 'sveikuolis' IDENTIFIER '=' INTEGER
 | 'siulas' IDENTIFIER '=' STRING
 | 'dvigubas' IDENTIFIER '=' DECIMAL
 | 'artikrai' IDENTIFIER '=' BOOLEAN
 ;

assignment
 : IDENTIFIER '=' expression
 ;

functionCall
 : IDENTIFIER '(' expressionList? ')'
 ;

systemFunctionCall
 : PRINT '(' expression ')'                             #printFunctionCall
 ;

ifElseStatement : 'if' '(' expression ')' block 'else' block ;

kolStatement : 'kol' '(' BOOLEAN ')' block;

visiemsStatement : 'visiems' '(' 'sveikuolis' IDENTIFIER '=' INTEGER ';' BOOLEAN ';'
                       IDENTIFIER '=' IDENTIFIER numericAddOp|numericMultiOp INTEGER|DECIMAL ')' block ;

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
 | functionCall                                         #functionCallExpression
 ;

booleanUnaryOp : '!' ;

booleanBinaryOp : '||' | '&&' ;

numericMultiOp : '*' | '/' | '%' ;

numericAddOp : '+' | '-' ;

stringBinaryOp : '+' ; //sujungti du strings

PRINT : 'print';

INTEGER : [-][0-9]+ | [0-9]+ ; //TODO - support negative numbers - done?
DECIMAL : [-][0-9]+ '.' [0-9]+ | [0-9]+ '.' [0-9]+ ;
BOOLEAN : 'true' | 'false' ;
STRING : ["] ( ~["\r\n\\] | '\\' ~[\r\n] )* ["] ;

IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]* ; //var name

COMMENT : ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip ;

WS : [ \t\f\r\n]+ -> skip ; //whitespace ir tt