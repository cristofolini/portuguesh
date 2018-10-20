grammar Portuguesh ;
prog        : program+ ;
program     : (function | declaration | conditional | loop | COMMENT)+ ;
declaration : ((DEF NAME EQUAL value) | (NAME EQUAL value)) NEW_LINE ;
value       : (QUOTATION (LETTER | DIGIT)+ QUOTATION | NUMBER) ;
conditional : (IF L_SQBRACKET condition R_SQBRACKET COLON NEW_LINE declaration+ (ELSE declaration+)? END_IF) NEW_LINE ;
loop        : (WHILE L_SQBRACKET condition R_SQBRACKET COLON NEW_LINE declaration+ END_WHILE) NEW_LINE ;
condition   : (NAME | value | LOGICAL) comparator? (NAME | value | LOGICAL) ;
comparator  : (LESSER | LESSEREQUAL | GREATER | GREATEREQUAL | EQUAL | EQUALEQUAL | DIFFERENT | AND | OR | NOT) ;
function    : (FUNC (STRING | NUMERICAL) NAME (L_PARENT (parameter COMMA?)* R_PARENT)? COLON) NEW_LINE (declaration | conditional | loop)* (RETURN NAME)? END_FUNC NEW_LINE ;
parameter   : NAME ;

PLUS 	  : '+' ;
MINUS	  : '-' ;
MULT	  : '*' ;
DIV		  : '/' ;
DEF		  : 'seja';
IF		  : 'se';
ELSE      : 'senao';
END_IF    : 'es';
WHILE	  : 'enquanto';
END_WHILE : 'otnauqne';
FUNC      : 'função';
END_FUNC  : 'oãçnuf';
RETURN    : 'retorne';
STRING    : 'texto';
NUMERICAL : 'número';
HASH      : '#';
AND		  : '&';
NOT		  : '!';
OR		  : '|';
TRUE	  : 'verdadeiro';
FALSE     : 'falso';
PRINT     : 'diga';
PLUSPLUS	: '++' ;
MINUSMINUS	: '--' ;
MODULO		: '%';
L_SQBRACKET	: '[';
R_SQBRACKET	: ']';
L_PARENT	: '(';
R_PARENT    : ')';
COLON		: ':';
COMMA		: ',';
DOT		    : '.';
SEMICOLON	: ';';
QUOTATION   : '"';
GREATER		: '>';
LESSER		: '<';
EQUALEQUAL	: '==';
DIFFERENT	: '!=';
GREATEREQUAL	: '>=';
LESSEREQUAL	: '<=';
EQUAL		: '=';
NEW_LINE    : ('\n'|'\r');
WS          : (' '|'\t') -> channel(HIDDEN);

TYPE        : STRING | NUMERICAL ;
NAME        : LETTER+ ;
NUMBER      : DIGIT+ ;
LOGICAL     : TRUE | FALSE ;
OPERATOR    : PLUS | MINUS | MULT | DIV | MODULO ;
COMPARATOR  : LESSER | LESSEREQUAL | GREATER | GREATEREQUAL | EQUAL | EQUALEQUAL | DIFFERENT | AND | OR | NOT ;
COMMENT     : HASH ~( '\r' | '\n' )* ;

LETTER : 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G'
       | 'H' | 'I' | 'J' | 'K' | 'L' | 'M' | 'N'
       | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U'
       | 'V' | 'W' | 'X' | 'Y' | 'Z' | 'a' | 'b'
       | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i'
       | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | 'p'
       | 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w'
       | 'x' | 'y' | 'z' ;
DIGIT : '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ;