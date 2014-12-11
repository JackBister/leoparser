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
			} else {
				System.out.println("Empty input file.");
			}
		} catch(SyntaxError e) {
			System.out.println("Syntax error on line: " + e.getLine());
			//Stack trace which can help figure out where the syntax error was.
			//e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
