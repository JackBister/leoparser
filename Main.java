public class Main {
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
		} catch(NullPointerException e) {
			//wtf
			e.printStackTrace();
		}
	}
}
