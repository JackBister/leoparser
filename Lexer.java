import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
	private List<Token> tokens;
	private int currentToken;

	private static String readInput(InputStream f) throws java.io.IOException {
		Reader stdin = new InputStreamReader(f);
		StringBuilder buf = new StringBuilder();
		char input[] = new char[1024];
		int read = 0;
		BufferedReader std = new BufferedReader(stdin);
		while(std.ready()) {
			read = std.read(input);
			buf.append(input, 0, read);
		}
		return buf.toString();
	}


	public Lexer(InputStream in) throws java.io.IOException, SyntaxError {
		String input = Lexer.readInput(in);
		Pattern tokenPattern = Pattern.compile("FORW|BACK|LEFT|RIGHT|DOWN|UP|COLOR|REP|\\.|\\#[A-F0-9]{6}|\\\"|\\%.*\n|[0-9]+|\\s", Pattern.CASE_INSENSITIVE);
		Matcher m = tokenPattern.matcher(input);
		int inputPos = 0;
		tokens = new ArrayList<Token>();
		currentToken = 0;
		int currentLine = 1;
		
		while (m.find()) {
			if (m.start() != inputPos) {
				tokens.add(new Token(currentLine, TokenType.Invalid));
			}
			if (m.group().equalsIgnoreCase("FORW")) 
				tokens.add(new Token(currentLine, TokenType.FORW));
			else if (m.group().equalsIgnoreCase("BACK"))
				tokens.add(new Token(currentLine, TokenType.BACK));
			else if (m.group().equalsIgnoreCase("LEFT"))
				tokens.add(new Token(currentLine, TokenType.LEFT));
			else if (m.group().equalsIgnoreCase("RIGHT"))
				tokens.add(new Token(currentLine, TokenType.RIGHT));
			else if (m.group().equalsIgnoreCase("DOWN"))
				tokens.add(new Token(currentLine, TokenType.DOWN));		
			else if (m.group().equalsIgnoreCase("UP"))
				tokens.add(new Token(currentLine, TokenType.UP));
			else if (m.group().equalsIgnoreCase("COLOR"))
				tokens.add(new Token(currentLine, TokenType.COLOR));	
			else if (m.group().equalsIgnoreCase("REP"))
				tokens.add(new Token(currentLine, TokenType.REP));	
			else if (m.group().equals("\""))
				tokens.add(new Token(currentLine, TokenType.QUOTE));
			else if (m.group().equals("."))
				tokens.add(new Token(currentLine, TokenType.DOT));
			else if (m.group().matches("#[a-fA-F0-9]{6}")) 
				tokens.add(new Token(currentLine, TokenType.HASH, m.group().substring(1)));
			else if (m.group().equals("\n")) {
				tokens.add(new Token(currentLine, TokenType.WHITESPACE));
				currentLine++;
			} else if (m.group().matches("\\%\\s*.*\n")) {
				tokens.add(new Token(currentLine, TokenType.WHITESPACE));
				currentLine++;
			} else if (m.group().matches("\\s"))
				tokens.add(new Token(currentLine, TokenType.WHITESPACE));
			else if (m.group().matches("[0-9]+")) {
				tokens.add(new Token(currentLine, TokenType.Number, m.group()));
			}
			inputPos = m.end();
		}
		// Checks for garbage data at the end of the string
		if (inputPos != input.length()) {
			tokens.add(new Token(currentLine, TokenType.Invalid));
		}
		
		// Writes out each token and line number for debugging purposes.
	/*	for (Token token: tokens)
		   System.out.println(token.getType().toString() + token.getLine()); 
	
	*/
		ArrayList<Token> tlist = new ArrayList<Token>();
		//Removes duplicate whitespaces
		for (int i = 1; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if(token.getType() == TokenType.WHITESPACE && tokens.get(i-1).getType() == TokenType.WHITESPACE)
				tlist.add(token);
		}
		tokens.removeAll(tlist); 
	}

	public Token peekToken() throws SyntaxError {
		if (!hasMoreTokens()) {
			//Errors on last non-whitespace token.
			if(tokens.get(currentToken-1).getType() != TokenType.WHITESPACE)
				throw new SyntaxError(tokens.get(currentToken-1).getLine());
			else if(tokens.size() >= 2)
				throw new SyntaxError(tokens.get(currentToken-2).getLine());
		}
		return tokens.get(currentToken);
	}

	public Token nextToken() throws SyntaxError {
		Token res = peekToken();
		++currentToken;
		return res;
	}

	public boolean hasMoreTokens() {
		return currentToken < tokens.size();
	}
}