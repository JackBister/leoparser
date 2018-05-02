package io.github.jackbister.leoparser;

abstract class Cmd {
	abstract public void process(Leonardo leo);
}

public class CmdList {
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

	public void process(Leonardo leo) {
		if(c != null)
			c.process(leo);
		if(cl != null)
			cl.process(leo);
	}
}

class Rep extends Cmd {
	Integer r;
	CmdList reps;
	
	public Rep(Integer r, CmdList reps) {
		this.r = r;
		this.reps = reps;
	}
	
	public void process(Leonardo leo) {
		for(int i = 0; i < r; i++) {
			reps.process(leo);
		}
	}
}

class ParseColor extends Cmd {
	String rgb;
	
	public ParseColor(String rgb) {
		this.rgb = rgb;
	}
	
	public void process(Leonardo leo) {
		leo.setColor(rgb);
	}
}

class PenMove extends Cmd {
	TokenType type;
	
	public PenMove(TokenType type) {
		this.type = type;
	}
	
	public void process(Leonardo leo) {
		if(type == TokenType.DOWN)
			leo.down();
		else
			leo.up();
	}
}

class Move extends Cmd {
	TokenType type;
	Integer num;
	
	public Move(TokenType type, Integer num) {
		this.type = type;
		this.num = num;
	}
		
	public void process(Leonardo leo) {
		if(type == TokenType.FORW)
			leo.forw(num);
		else if(type == TokenType.BACK)
			leo.back(num);
		else if(type == TokenType.LEFT)
			leo.left(num);
		else
			leo.right(num);
	}
}