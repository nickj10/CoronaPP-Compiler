package syntax;

public enum TokenType {
  EPSILON(0),

  // operators
  POST_INC(1),
  POST_DEC(2),
  PRE_INC(3),
  PRE_DEC(4),
  ARITMETIC_MULT(5),
  ARITMETIC_MOD(6),
  ARITMETIC_DIV(7),
  ARITMETIC_SUM(8),
  ARITMETIC_RES(9),
  ARITMETIC_EQ(10),
  RELATIONAL_EQ(11),
  RELATIONAL_NOTEQ(12),
  LOGIC_AND(13),
  LOGIC_OR(14),
  ASSIGN_EQ(15),

  // reserved words
  IF(16),
  ELSE(17),
  WHILE(18),
  INT(19),
  BOOL(20),
  CONST(21),
  CHAR(22),
  NULL(23),
  RETURN(24),
  FLOAT(25),

  // special symbols
  DOT_COMA(26),
  DOT(27),
  PARENT_OPEN(28),
  PARENT_CLOSED(29),
  LONG_COMMENT_O(30),
  LONG_COMMENT_C(31),
  SHORT_COMMENT(32),
  OPEN_BRACKET(33),
  CLOSE_BRACKET(34),

  // simple types
  IDENTIFIER(35),
  NUMBER(36);

  private int code;

  TokenType(int code) {
    this.code = code;
  }
}
