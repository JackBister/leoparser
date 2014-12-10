// De olika token-typer vi har i grammatiken
enum TokenType {
	FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, REP, QUOTE, DOT, HASH, LF, PERCENT, WHITESPACE, Number, Invalid
}

// Klass för att representera en token
// I praktiken vill man nog även spara info om vilken rad/position i
// indata som varje token kommer ifrån, för att kunna ge bättre
// felmeddelanden
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
