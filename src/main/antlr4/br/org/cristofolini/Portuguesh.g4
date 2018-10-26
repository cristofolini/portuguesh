grammar Portuguesh ;

@header {
import org.antlr.symtab.*;
}

file returns [Scope scope]
    : (function | declaration | attribution | conditional | loop | comment)+ EOF
    ;
declaration
    : 'seja' ID '=' value NEW_LINE
    ;
attribution
    : ID '=' value NEW_LINE
    ;
value
    : ('"' (LETTER | DIGIT) '"' | DIGIT)
    ;
conditional
    : ('se' '[' condition ']' ':' NEW_LINE (declaration | attribution)+ ('senao' (declaration | attribution)+)? 'es') NEW_LINE
    ;
loop
    : ('enquanto' '[' condition ']' ':' NEW_LINE (declaration | attribution)+ 'otnauqne') NEW_LINE
    ;
condition
    : operand comparator? operand
    ;
operand
    : (ID | value | LOGICAL)
    ;
comparator
    : '<' | '<=' | '>' | '>=' | '=' | '==' | '!=' | '&' | '|' | '!'
    ;
function returns [Scope scope]
    : ('função' TYPE ID ('(' (parameter ','?)* ')')? ':') NEW_LINE block 'oãçnuf' NEW_LINE
    ;
block returns [Scope scope]
    : (declaration | attribution | conditional | loop)* ('retorne' ID NEW_LINE)?
    ;
parameter
    : ID
    ;
comment
    : '#' ~( NEW_LINE )*
    ;

ID        : [a-z] ;
TYPE      : 'texto' | 'número' ;
NEW_LINE    : ('\n'|'\r') ;
WS          : (' '|'\t') -> skip ;
LOGICAL     : 'verdadeiro' | 'falso' ;
LETTER : 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G'
       | 'H' | 'I' | 'J' | 'K' | 'L' | 'M' | 'N'
       | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U'
       | 'V' | 'W' | 'X' | 'Y' | 'Z' | 'a' | 'b'
       | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i'
       | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | 'p'
       | 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w'
       | 'x' | 'y' | 'z' ;
DIGIT : '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ;