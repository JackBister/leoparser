package io.github.jackbister.leoparser;

enum TokenType {
	FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, REP, QUOTE, DOT, HASH, WHITESPACE, Number, Invalid
}

class Token {
	private int line;
	private TokenType type;
	private Object data;
	
	public Token(int line, TokenType type) {
		this.line = line;
		this.type = type;
		this.data = null;
	}

	public Token(int line, TokenType type, Object data) {
		this.line = line;
		this.type = type;
		this.data = data;
	}

	public TokenType getType() { return type; }
	public Object getData() { return data; }
	public int getLine() { return line; }

}
