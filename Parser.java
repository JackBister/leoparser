/**
 * En rekursiv medåknings-parser för binära träd
 */
public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public CmdList parse() throws Exception {
		CmdList result = ParseCmds();
		//There shouldn't be any tokens left after the parsing is finished:
		if (lexer.hasMoreTokens())
			throw new SyntaxError(lexer.peekToken().getLine());
		//Result can be null if input consists only of whitespace.
		if(result == null)
			throw new Exception("Empty input file.");
		return result;
	}
	
	/*
	 * If the next token is whitespace, this method steps past it.
	 * Otherwise, it does nothing.
	 */
	private void chewWhitespace() throws SyntaxError {
		if(lexer.peekToken().getType() == TokenType.WHITESPACE)
			lexer.nextToken();
	}
	
	/*
	 * Checks that a token has the type specified by the parameter t,
	 * and returns the token.
	 */
	private Token mustBe(TokenType t) throws SyntaxError {
		Token token = lexer.nextToken();
		if(token.getType() != t)
			throw new SyntaxError(token.getLine());
		return token;
	}
	
	/*
	 * Finalizes the parsing of a token.
	 * At this point it is known what command and what parameters a token is called with,
	 * but it is not known whether there is more tokens to parse.
	 * This code was reused in a lot of places which is why it was broken out here.
	 */
	private CmdList finalizeParse(Cmd c) throws SyntaxError {
		if(lexer.hasMoreTokens()) {
			chewWhitespace();
			if(lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.QUOTE)
				return new CmdList(c, ParseCmds());
			else
				return new CmdList(c, null);
		} else
			return new CmdList(c, null);
	}
	
	/*
	 * Parses commands of the 'move' class - FORW, BACKW, LEFT and RIGHT.
	 */
	private CmdList parseMove() throws SyntaxError {
		Token t = lexer.nextToken();
		mustBe(TokenType.WHITESPACE);
		Token num = mustBe(TokenType.Number);
		int d = 0;
		try {
			d = Integer.parseInt((String)num.getData());
		} catch (NumberFormatException e) {
			throw new SyntaxError(num.getLine());
		}
		if(d == 0) 
			throw new SyntaxError(num.getLine());
		chewWhitespace();
		Token tmp = lexer.nextToken();
		if(tmp.getType() != TokenType.DOT) {
			throw new SyntaxError(tmp.getLine());
		} else
			return finalizeParse(new Move(t.getType(), d));
	}
	
	/*
	 * Parses commands of the "penmove' class - UP and DOWN.
	 */
	private CmdList parsePenMove() throws SyntaxError {
		Token t = lexer.nextToken();
		chewWhitespace();
		Token tmp = lexer.nextToken();
		if(tmp.getType() != TokenType.DOT) {
			throw new SyntaxError(tmp.getLine());
		} else
			return finalizeParse(new PenMove(t.getType()));
	}
	
	/*
	 * Parses COLOR commands.
	 */
	private CmdList parseColor() throws SyntaxError {
		lexer.nextToken();
		mustBe(TokenType.WHITESPACE);
		Token c = mustBe(TokenType.HASH);
		chewWhitespace();
		Token tmp = lexer.nextToken();
		if(tmp.getType() != TokenType.DOT) {
			throw new SyntaxError(tmp.getLine());
		} else
			return finalizeParse(new ParseColor("0x" + (String)c.getData()));
	}
	
	/*
	 * Parses REP commands.
	 */
	private CmdList parseReps() throws SyntaxError {
		lexer.nextToken();
		mustBe(TokenType.WHITESPACE);
		Token r = mustBe(TokenType.Number);
		int rint = 0;
		try {
			rint = Integer.parseInt((String)r.getData());
		} catch (NumberFormatException e) {
			throw new SyntaxError(r.getLine());
		}
		if(rint == 0) 
			throw new SyntaxError(r.getLine());
		mustBe(TokenType.WHITESPACE);
		if(!lexer.hasMoreTokens())
			throw new SyntaxError(r.getLine());
		Token q = lexer.peekToken();
		if(q.getType() == TokenType.QUOTE) {
			lexer.nextToken();
			CmdList cl = ParseCmds();
			q = lexer.nextToken();
			if(q.getType() != TokenType.QUOTE) {
				throw new SyntaxError(q.getLine());
			}
			return finalizeParse(new Rep(rint, cl));
		} else {
			CmdList cl = ParseCmds();
			CmdList rcl = new CmdList(cl.pop(), null);
			return new CmdList(new Rep(rint, rcl), cl);
		}
	}
	
	/*
	 * Chooses how to parse the upcoming tokens based on the type of the next token.
	 */
	private CmdList ParseCmds() throws SyntaxError {
		Token t = lexer.peekToken();
		if (t.getType() == TokenType.FORW || t.getType() == TokenType.BACK
		 || t.getType() == TokenType.LEFT || t.getType() == TokenType.RIGHT)
			return parseMove();
		else if(t.getType() == TokenType.UP || t.getType() == TokenType.DOWN)
			return parsePenMove();
		else if(t.getType() == TokenType.COLOR)
			return parseColor();
		else if(t.getType() == TokenType.REP)
			return parseReps();
		else if (t.getType() == TokenType.WHITESPACE) {
			lexer.nextToken();
			if (lexer.hasMoreTokens())
				return ParseCmds();
			else
				return null;
		} else
			throw new SyntaxError(t.getLine());
	}
}