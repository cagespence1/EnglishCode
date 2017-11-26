grammar grammarLang;
program : stat (','?stat)*;

stat
    : definition
    | func
    | ifstat
    | whilestat
    | forstat
    | assign
    | block
    | print
    | return_statement
    | math_expression
    | increment
    ;

ifstat : 'if' ifbody ;

ifbody : condition block ('otherwise' stat)*;

condition
    : leftCondition=condition log_ex rightCondition=condition   #conditionLogExp
    | leftValue=value COMPARATOR rightValue=value               #conditionCompare
    | value                                                     #conditionValue
    ;

value
    : literal
    | variable
    | func
    ;

forstat : 'start from' startValue=value ',' 'repeat until iteration amount' COMPARATOR iterationsValue=value block;

whilestat : 'while' condition 'remains' block;

definition
    : variable_define
    | function_define
    ;

variable_define
    : 'define' type 'variable' ID 'as' value;

function_define
    : 'define' 'function' ID ',''returning' type (',' 'using' parameters)? block
    ;

parameters
    : parameter (','parameter)*
    ;

parameter: type 'variable' ID;

func
    : 'function' ID ('using' arguments)?
    ;

arguments
    : value (','value)*
    ;

assign
    : 'set' variable 'to' value
//    | increment
    ;

increment
    : 'preincrement' variable
    | 'postincrement' variable
    | 'predecrement' variable
    | 'postdecrement' variable
    ;

math_expression
    : '('math_expression')' #mathExpParenthesis
    | math_expression OP math_expression #mathExpRegular
    | value #mathExpSingleValue
    ;

variable
    : 'variable' ID
    ;

type
    : TYPE_STR
    | TYPE_INT
    | TYPE_BOOL
    ;

TYPE_STR
    : 'string'
    ;

TYPE_INT
    : 'integer'
    ;

TYPE_BOOL
    : 'boolean'
    ;

literal
    : bool
    | integer
    | string
    ;

return_statement
    : 'return' value
    ;

bool
    : TRUE
    | FALSE
    ;

TRUE: 'true';

FALSE: 'false';

integer
    : '-'?INT
    ;

ID : CHAR+;

string
    : STRING
    | EMPTY_STRING
    ;

STRING
    : '"'  ('""'|~'"')* '"'
    ;

EMPTY_STRING
    : '""'
    ;

block
    : ':' (stat (','stat)*)* '.'
    ;

print
    : 'print' value BLOCK_CLOSE
    ;

BOOL
    : 'true'
    | 'false'
    ;

OP
    : 'plus'
    | 'divided by'
    | 'minus'
    | 'modulo'
    | 'multiplied by'
    ;

log_ex
    : 'or'
    | 'and'
    | 'not'
    ;

COMPARATOR
    : 'is equal to'
    | 'is more than'
    | 'is less than'
    | 'is more than equal to'
    | 'is less than equal to'
    ;

CLOSE : ')';
OPEN : '-'?'(';
CHAR : [a-zA-Z];
BLOCK_OPEN : ':';
BLOCK_CLOSE : '.';
INT : [0-9]+;
WS : ' '+ -> skip;
R : '\r'+ -> skip;
NL: '\n'+ -> skip;
