grammar BBAsm;
// starting point for parsing a java file

prog
    : stat+
    ;

stat
    : expr NEWLINE
    | NEWLINE
    ;

expr
    : instruction
    | LABEL // �����ǩ�����
    ;

// ���п��ܵ�ָ�
instruction
    : NoOperandIns
    | OneOperandIns operand
    | TowOperandIns operand COMMA operand
	| INS_CAL DataType CalculateOperator operand COMMA operand // CAL int ADD r0,12
	| INS_LD DataType operand COMMA operand  // ld int r1, 1067320848
    ;

operand
    : Register						// ʹ�üĴ���
    | LBRACK Register RBRACK
    | ConstFormula					// ʹ�ó������ʽ
    | LBRACK ConstFormula RBRACK
    | Identifier					// ʹ�ñ�ʶ��
    | LBRACK Identifier RBRACK
    ;

// �������ʽ,�ڱ����ڼ���Խ�����ֵ
ConstFormula
	: IntegerLiteral
	;
	
// �޲�������ָ��
NoOperandIns
    : INS_NOP
    | INS_RET
    | INS_EXIT
    ;
// һ����������ָ��
OneOperandIns
    : INS_JMP
    | INS_CALL
    | INS_PUSH
    | INS_POP
    ;
// ������������ָ��
TowOperandIns
    : INS_IN
    | INS_OUT
    ;


literal
    :   IntegerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   StringLiteral
    |   BooleanLiteral
    |   'null'
    ;


Register
    : RP
    | RF
    | RS
    | RB
    | R0
    | R1
    | R2
    | R3
    ;

// �Ĵ�������
fragment RP : R P ;
fragment RF : R F ;
fragment RS : R S ;
fragment RB : R B ;
fragment R0 : R '0' ;
fragment R1 : R '1' ;
fragment R2 : R '2' ;
fragment R3 : R '3' ;

// �Ƚϲ���
CompareOperator
	: CMP_Z	
	| CMP_B 	
	| CMP_BE	
	| CMP_A
	| CMP_AE
	| CMP_NZ
	;
	
fragment CMP_Z	: Z	  ;
fragment CMP_B 	: B   ;
fragment CMP_BE	: B E ;
fragment CMP_A 	: A   ;
fragment CMP_AE	: A E ;
fragment CMP_NZ	: N Z ;

// �������
CalculateOperator
	: CAL_ADD
	| CAL_SUB
	| CAL_MUL
	| CAL_DIV
	| CAL_MOD
	;

fragment CAL_ADD : A D D ;
fragment CAL_SUB : S U B ;
fragment CAL_MUL : M U L ;
fragment CAL_DIV : D I V ;
fragment CAL_MOD : M O D ;

// ��������
DataType
	: DataType_DWORD
	| DataType_WORD
	| DataType_BYTE
	| DataType_FLOAT
	| DataType_INT
	;

fragment DataType_DWORD :D W O R D ;
fragment DataType_WORD  :W O R D   ;
fragment DataType_BYTE  :B Y T E   ;
fragment DataType_FLOAT :F L O A T ;
fragment DataType_INT   :I N T     ;

// ���е�ָ��
INS_NOP    : N O P;
INS_LD     : L D;
INS_PUSH   : P U S H;
INS_POP    : P O P;
INS_IN     : I N;
INS_OUT    : O U T;
INS_JMP    : J M P;
INS_JPC    : J P C;
INS_CALL   : C A L L;
INS_RET    : R E T;
INS_CMP    : C M P;
INS_CAL    : C A L;
INS_EXIT   : E X I T;
INS_DATA   : D A T A;
INS_BLOCK  : DOT? B L O C K;
// ��ǩ��ʽ
LABEL : Identifier ':' ;

// ������ʽ,�ڱ������ܹ��õ������

// ��������ֵ
IntegerLiteral
    :   DecimalIntegerLiteral
    |   HexIntegerLiteral
    |   OctalIntegerLiteral
    |   BinaryIntegerLiteral
    ;

fragment
DecimalIntegerLiteral
    :   DecimalNumeral IntegerTypeSuffix?
    ;

fragment
HexIntegerLiteral
    :   HexNumeral IntegerTypeSuffix?
    ;

fragment
OctalIntegerLiteral
    :   OctalNumeral IntegerTypeSuffix?
    ;

fragment
BinaryIntegerLiteral
    :   BinaryNumeral IntegerTypeSuffix?
    ;

fragment
IntegerTypeSuffix
    :   [lL]
    ;

fragment
DecimalNumeral
    :   '0'
    |   NonZeroDigit (Digits? | Underscores Digits)
    ;

fragment
Digits
    :   Digit (DigitOrUnderscore* Digit)?
    ;

fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

fragment
DigitOrUnderscore
    :   Digit
    |   '_'
    ;

fragment
Underscores
    :   '_'+
    ;

fragment
HexNumeral
    :   '0' [xX] HexDigits
    ;

fragment
HexDigits
    :   HexDigit (HexDigitOrUnderscore* HexDigit)?
    ;

fragment
HexDigit
    :   [0-9a-fA-F]
    ;

fragment
HexDigitOrUnderscore
    :   HexDigit
    |   '_'
    ;

fragment
OctalNumeral
    :   '0' Underscores? OctalDigits
    ;

fragment
OctalDigits
    :   OctalDigit (OctalDigitOrUnderscore* OctalDigit)?
    ;

fragment
OctalDigit
    :   [0-7]
    ;

fragment
OctalDigitOrUnderscore
    :   OctalDigit
    |   '_'
    ;

fragment
BinaryNumeral
    :   '0' [bB] BinaryDigits
    ;

fragment
BinaryDigits
    :   BinaryDigit (BinaryDigitOrUnderscore* BinaryDigit)?
    ;

fragment
BinaryDigit
    :   [01]
    ;

fragment
BinaryDigitOrUnderscore
    :   BinaryDigit
    |   '_'
    ;

// ����������ֵ
FloatingPointLiteral
    :   DecimalFloatingPointLiteral
    |   HexadecimalFloatingPointLiteral
    ;

fragment
DecimalFloatingPointLiteral
    :   Digits '.' Digits? ExponentPart? FloatTypeSuffix?
    |   '.' Digits ExponentPart? FloatTypeSuffix?
    |   Digits ExponentPart FloatTypeSuffix?
    |   Digits FloatTypeSuffix
    ;

fragment
ExponentPart
    :   ExponentIndicator SignedInteger
    ;

fragment
ExponentIndicator
    :   [eE]
    ;

fragment
SignedInteger
    :   Sign? Digits
    ;

fragment
Sign
    :   [+-]
    ;

fragment
FloatTypeSuffix
    :   [fFdD]
    ;

fragment
HexadecimalFloatingPointLiteral
    :   HexSignificand BinaryExponent FloatTypeSuffix?
    ;

fragment
HexSignificand
    :   HexNumeral '.'?
    |   '0' [xX] HexDigits? '.' HexDigits
    ;

fragment
BinaryExponent
    :   BinaryExponentIndicator SignedInteger
    ;

fragment
BinaryExponentIndicator
    :   [pP]
    ;

// ��������ֵ
BooleanLiteral
    :   'true'
    |   'false'
    ;

// �ַ�����ֵ
CharacterLiteral
    :   '\'' SingleCharacter '\''
    |   '\'' EscapeSequence '\''
    ;

fragment
SingleCharacter
    :   ~['\\]
    ;

// �ַ�������ֵ
StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;

fragment
StringCharacter
    :   ~["\\]
    |   EscapeSequence
    ;

// �ַ����ַ�����ת������
fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
    |   OctalEscape
    |   UnicodeEscape
    ;

fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

// The Null Literal

NullLiteral
    :   'null'
    ;

// Separators

LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LBRACK          : '[';
RBRACK          : ']';
SEMI            : ';';
COMMA           : ',';
DOT             : '.';

// Operators

ASSIGN          : '=';
GT              : '>';
LT              : '<';
BANG            : '!';
TILDE           : '~';
QUESTION        : '?';
COLON           : ':';
EQUAL           : '==';
LE              : '<=';
GE              : '>=';
NOTEQUAL        : '!=';
AND             : '&&';
OR              : '||';
INC             : '++';
DEC             : '--';
ADD             : '+';
SUB             : '-';
MUL             : '*';
DIV             : '/';
BITAND          : '&';
BITOR           : '|';
CARET           : '^';
MOD             : '%';

ADD_ASSIGN      : '+=';
SUB_ASSIGN      : '-=';
MUL_ASSIGN      : '*=';
DIV_ASSIGN      : '/=';
AND_ASSIGN      : '&=';
OR_ASSIGN       : '|=';
XOR_ASSIGN      : '^=';
MOD_ASSIGN      : '%=';
LSHIFT_ASSIGN   : '<<=';
RSHIFT_ASSIGN   : '>>=';
URSHIFT_ASSIGN  : '>>>=';

// ��ʶ�� ���������йؼ���֮��
Identifier
    :   JavaLetter JavaLetterOrDigit*
    ;

fragment
JavaLetter
    :   [a-zA-Z$_] // these are the "java letters" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
JavaLetterOrDigit
    :   [a-zA-Z0-9$_] // these are the "java letters or digits" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

//
// Additional symbols not defined in the lexical specification
//

AT : '@';
ELLIPSIS : '...';

//
// Whitespace and comments
//

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   ('//' | ';') ~[\r\n]* -> skip
    ;

NEWLINE:'\r'? '\n' ; // return newlines to parser (is end-statement signal)

// ��������Сд�޹ص��﷨
fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');