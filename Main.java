/**
 * Exempel på rekursiv medåkning: en parser för binära träd enligt grammatiken
 *
 * BinTree --> leaf ( Number ) | branch ( BinTree , BinTree )
 *
 * Parsar trädet och skriver sedan ut det i ett lite annorlunda format
 * samt byter plats på vänster och höger i alla noder (i brist på
 * något roligare att göra)
 *
 * Provkörning från terminal på fil "tree.in"
 *
 * javac *.java
 * java Main < test.in
 *
 *
 * (Det här exempelprogrammet skrevs av en person som normalt inte
 * använder Java, så ha överseende om delar av koden inte är så
 * vacker.)
 */
public abstract class Main {
	public static Leonardo leo = new Leonardo();
	public static void main(String args[]) throws java.io.IOException, SyntaxError {
		try {
			Lexer lexer = new Lexer(System.in);
			if(lexer.hasMoreTokens()) {
				Parser parser = new Parser(lexer);
				CmdList result;
				result = parser.parse();
				result.process();
			}
		} catch(SilentError e) {
			System.exit(0);
		} catch(SyntaxError e) {
			System.out.println("Syntaxfel på rad " + e.getLine());
			// DEBUG
			//e.printStackTrace();
		}
	}
}
