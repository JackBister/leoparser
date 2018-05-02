package io.github.jackbister.leoparser;

// Klass för att representera syntaxfel.  I praktiken vill man nog
// även ha med ett litet felmeddelande om *vad* som var fel, samt på
// vilken rad/position felet uppstod
public class SyntaxError extends Exception {
	private int line;
	
	public SyntaxError(int line) {
		this.line = line;
	}
	
	public int getLine() { return line; }
}
