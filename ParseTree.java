abstract class Cmd {
	abstract public void process();
}

class CmdList {
	CmdList cl;
	Cmd c;
	
	public CmdList(Cmd c, CmdList cl) {
		this.c = c;
		this.cl = cl;
	}
	
	public Cmd pop() {
		Cmd temp = c;
		try {
			c = cl.pop();
		} catch(NullPointerException e) {
			c = null;
		}
		return temp;
	}
	
	public void process() {
		if(c != null)
			c.process();
		if(cl != null) 
			cl.process();
	}
}

class Rep extends Cmd {
	Integer r;
	CmdList reps;
	
	public Rep(Integer r, CmdList reps) {
		this.r = r;
		this.reps = reps;
	}
	
	public void process() {
		for(int i = 0; i < r; i++) {
			reps.process();
		}
	}
}

class ParseColor extends Cmd {
	String rgb;
	
	public ParseColor(String rgb) {
		this.rgb = rgb;
	}
	
	public void process() {
		Main.leo.setColor(rgb);
	}
}

class PenMove extends Cmd {
	TokenType type;
	
	public PenMove(TokenType type) {
		this.type = type;
	}
	
	public void process() {
		if(type == TokenType.DOWN)
			Main.leo.down();
		else
			Main.leo.up();
	}
}

class Move extends Cmd {
	TokenType type;
	Integer num;
	
	public Move(TokenType type, Integer num) {
		this.type = type;
		this.num = num;
	}
		
	public void process() {
		if(type == TokenType.FORW)
			Main.leo.forw(num);
		else if(type == TokenType.BACK)
			Main.leo.back(num);
		else if(type == TokenType.LEFT)
			Main.leo.left(num);
		else
			Main.leo.right(num);
	}
}