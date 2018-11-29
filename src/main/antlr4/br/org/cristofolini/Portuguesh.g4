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
    : ('"'(LETTER | DIGIT)'"' | DIGIT)
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
lesser_block returns [Scope scope]
    : (declaration | attribution)*
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
DIGIT : [0-9] ;
LETTER : [a-zA-Z\u0080-\u00FF_] ;
