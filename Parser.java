/**
 * En rekursiv medåknings-parser för binära träd
 */
public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public CmdList parse() throws SilentError, SyntaxError {
		// Startsymbol är BinTree
		CmdList result = BinTree();
		// Borde inte finnas något kvar av indata när vi parsat ett bintree
		if (lexer.hasMoreTokens())
			throw new SyntaxError(lexer.peekToken().getLine());
		return result;
	}

	private CmdList BinTree() throws SilentError, SyntaxError {
		// Kika på nästa indata-token för att välja produktionsregel
		Token t = lexer.peekToken();
		if (t.getType() == TokenType.FORW || t.getType() == TokenType.BACK
		 || t.getType() == TokenType.LEFT || t.getType() == TokenType.RIGHT) {
			lexer.nextToken();
			Token num = lexer.nextToken(); 
			if(num.getType() != TokenType.WHITESPACE) {
				throw new SyntaxError(num.getLine());
			}
			num = lexer.nextToken(); 
			if(num.getType() != TokenType.Number) {
				throw new SyntaxError(num.getLine());
			}
			int d = 0;
			try {
				d = Integer.parseInt((String)num.getData());
			} catch (NumberFormatException e) {
				throw new SyntaxError(num.getLine());
			}
			if(d == 0) throw new SyntaxError(num.getLine());
			if(lexer.peekToken().getType() == TokenType.WHITESPACE)
				lexer.nextToken();
			if(lexer.peekToken().getType() != TokenType.DOT) {
				throw new SyntaxError(lexer.peekToken().getLine());
			} else {
				lexer.nextToken();
				if(lexer.hasMoreTokens()) {
					if(lexer.peekToken().getType() == TokenType.WHITESPACE)
						lexer.nextToken();
					if(lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.QUOTE)
						return new CmdList(new Move(t.getType(), d), BinTree());
					else
						return new CmdList(new Move(t.getType(), d), null);
				} else
					return new CmdList(new Move(t.getType(), d), null);
			}
		} else if(t.getType() == TokenType.UP || t.getType() == TokenType.DOWN) {
			lexer.nextToken();
			if(lexer.peekToken().getType() == TokenType.WHITESPACE)
				lexer.nextToken();
			if(lexer.peekToken().getType() != TokenType.DOT) {
				throw new SyntaxError(lexer.peekToken().getLine());
			} else {
				lexer.nextToken();
				if(lexer.hasMoreTokens()) {
					if(lexer.peekToken().getType() == TokenType.WHITESPACE)
						lexer.nextToken();
					if(lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.QUOTE) 
						return new CmdList(new PenMove(t.getType()), BinTree());
					else 
						return new CmdList(new PenMove(t.getType()), null);
				} else
					return new CmdList(new PenMove(t.getType()), null);
			}
		} else if(t.getType() == TokenType.COLOR) {
			lexer.nextToken();
			if(lexer.peekToken().getType() != TokenType.WHITESPACE) {
				throw new SyntaxError(lexer.peekToken().getLine());
			}
			lexer.nextToken();
			Token c = lexer.nextToken();
			if(c.getType() != TokenType.HASH) {
				throw new SyntaxError(c.getLine());
			}
	
			if(lexer.peekToken().getType() == TokenType.WHITESPACE)
				lexer.nextToken();
			if(lexer.peekToken().getType() != TokenType.DOT) {
				throw new SyntaxError(lexer.peekToken().getLine());
			} else {
				lexer.nextToken();
				if(lexer.hasMoreTokens()) {
					if(lexer.peekToken().getType() == TokenType.WHITESPACE)
						lexer.nextToken();
					if(lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.QUOTE) 
						return new CmdList(new ParseColor("0x" + (String)c.getData()), BinTree());
					else
						return new CmdList(new ParseColor("0x" + (String)c.getData()), null);
				} else
					return new CmdList(new ParseColor("0x" + (String)c.getData()), null);
			}
		} else if(t.getType() == TokenType.REP) {
			lexer.nextToken();
			if(lexer.peekToken().getType() != TokenType.WHITESPACE) throw new SyntaxError(lexer.peekToken().getLine());
			lexer.nextToken();
			Token r = lexer.nextToken();
			if(r.getType() != TokenType.Number) throw new SyntaxError(r.getLine());
			int rint = 0;
			try {
				rint = Integer.parseInt((String)r.getData());
			} catch (NumberFormatException e) {
				throw new SyntaxError(r.getLine());
			}
			if(rint == 0) throw new SyntaxError(r.getLine());
			if(lexer.peekToken().getType() != TokenType.WHITESPACE) 
				throw new SyntaxError(lexer.peekToken().getLine());
			lexer.nextToken();
			if(!lexer.hasMoreTokens())
				throw new SyntaxError(r.getLine());
			Token q = lexer.peekToken();
			if(q.getType() == TokenType.QUOTE) {
				lexer.nextToken();
				CmdList cl = BinTree();
				q = lexer.nextToken();
				if(q.getType() != TokenType.QUOTE) {
					throw new SyntaxError(q.getLine());
				}
				if(lexer.hasMoreTokens()) {
					if(lexer.peekToken().getType() == TokenType.WHITESPACE)
						lexer.nextToken();
					if(lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.QUOTE) 
						return new CmdList(new Rep(rint, cl), BinTree());
					else 
						return new CmdList(new Rep(rint, cl), null);
				} else
					return new CmdList(new Rep(rint, cl), null);
			} else {
				CmdList cl = BinTree();
				CmdList rcl = new CmdList(cl.pop(), null);
				return new CmdList(new Rep(rint, rcl), cl);
			}
		} else if (t.getType() == TokenType.PERCENT) {
			//Stegar fram tills vi kommer till en ny rad
			while(lexer.peekToken().getType() != TokenType.LF) 
				lexer.nextToken();
			//"Äter upp" LF
			lexer.nextToken();
			if (lexer.hasMoreTokens())
				return BinTree();
			else
				return null;
		} else if (t.getType() == TokenType.LF || t.getType() == TokenType.WHITESPACE) {
			lexer.nextToken();
			if (lexer.hasMoreTokens())
				return BinTree();
			else
				return null;
		} else
			throw new SyntaxError(t.getLine());
	}
}