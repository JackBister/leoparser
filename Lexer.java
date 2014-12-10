import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * En klass för att göra lexikal analys, konvertera indataströmmen
 * till en sekvens av tokens.  Den här klassen läser in hela
 * indatasträngen och konverterar den på en gång i konstruktorn.  Man
 * kan tänka sig en variant som läser indataströmmen allt eftersom
 * tokens efterfrågas av parsern, men det blir lite mer komplicerat.
 */
public class Lexer {
	private String input;
	private List<Token> tokens;
	private int currentToken;
	// Hjälpmetod som läser in innehållet i en inputstream till en
	// sträng
	private static String readInput(InputStream f) throws java.io.IOException {
		Reader stdin = new InputStreamReader(f);
		StringBuilder buf = new StringBuilder();
		char input[] = new char[1024];
		int read = 0;
		while ((read = stdin.read(input)) != -1) {
			buf.append(input, 0, read);
		}
		return buf.toString();
	}


	public Lexer(InputStream in) throws java.io.IOException, SyntaxError {
		String input = Lexer.readInput(in);
		// Ett regex som beskriver hur ett token kan se ut, plus whitespace (som vi här vill ignorera helt)
		Pattern tokenPattern = Pattern.compile("FORW|BACK|LEFT|RIGHT|DOWN|UP|COLOR|REP|\\.|\\#|\\\"|\\%.*\n|[A-F0-9]+|\\s+", Pattern.CASE_INSENSITIVE);
		Matcher m = tokenPattern.matcher(input);
		int inputPos = 0;
		tokens = new ArrayList<Token>();
		currentToken = 0;
		int currentLine = 1;
		// Hitta förekomster av tokens/whitespace i indata
		while (m.find()) {
			// Om matchningen inte börjar där den borde har vi hoppat
			// över något skräp i indata, markera detta som ett
			// "Invalid"-token
			if (m.start() != inputPos) {
				tokens.add(new Token(currentLine, TokenType.Invalid));
			}
			// Kolla vad det var som matchade
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
			else if (m.group().equals("#")) {
				m.find();
				tokens.add(new Token(currentLine, TokenType.HASH, m.group()));
			} else if (m.group().equals("\n")) {
				currentLine++;
				tokens.add(new Token(currentLine, TokenType.LF));
				tokens.add(new Token(currentLine, TokenType.WHITESPACE));
			} else if (m.group().matches("\\%\\s*.*\n")) {
				//tokens.add(new Token(currentLine, TokenType.PERCENT));
				//tokens.add(new Token(currentLine, TokenType.LF));
				tokens.add(new Token(currentLine, TokenType.WHITESPACE));
				currentLine++;
			} else if (m.group().matches("\\s+"))
				tokens.add(new Token(currentLine, TokenType.WHITESPACE));
			else if (m.group().matches("[0-9]+")) {
				tokens.add(new Token(currentLine, TokenType.Number, m.group()));
			}
			inputPos = m.end();
		}
		// Kolla om det fanns något kvar av indata som inte var ett token
		if (inputPos != input.length()) {
			tokens.add(new Token(currentLine, TokenType.Invalid));
		}
		
		// Debug-kod för att skriva ut token-sekvensen
	/*	for (Token token: tokens)
		   System.out.println(token.getType()); 
	*/
		
		ArrayList<Token> tlist = new ArrayList<Token>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if(token.getType() == TokenType.PERCENT || token.getType() == TokenType.LF)
				tlist.add(token);
		}
		tokens.removeAll(tlist);
		for (int i = 1; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if(token.getType() == TokenType.WHITESPACE && tokens.get(i-1).getType() == TokenType.WHITESPACE)
				tlist.add(token);
		}
		tokens.removeAll(tlist);
	}

	// Kika på nästa token i indata, utan att gå vidare
	public Token peekToken() throws SilentError, SyntaxError {
		// Slut på indataströmmen
		if (!hasMoreTokens()) {
			if(tokens.size() == 0)
				throw new SilentError();
			if(tokens.get(currentToken-1).getType() != TokenType.WHITESPACE)
				throw new SyntaxError(tokens.get(currentToken-1).getLine());
			else if(tokens.size() >= 2)
				throw new SyntaxError(tokens.get(currentToken-2).getLine());
		}
		return tokens.get(currentToken);
	}

	// Hämta nästa token i indata och gå framåt i indata
	public Token nextToken() throws SilentError, SyntaxError {
		Token res = peekToken();
		++currentToken;
		return res;
	}

	public Token lastToken() {
		return tokens.get(currentToken-1);
	}
	public boolean hasMoreTokens() {
		return currentToken < tokens.size();
	}
}