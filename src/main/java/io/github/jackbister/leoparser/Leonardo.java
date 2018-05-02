package io.github.jackbister.leoparser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Leonardo {
	boolean down;
	int facing;
	Point2D.Double p;
	Color c;

	Graphics graphics;
	
	public Leonardo(Graphics graphics, int width, int height) {
		down = false;
		facing = 0;
		p = new Point2D.Double(width/2, height/2);
		c = new Color(0, 0, 255);

		this.graphics = graphics;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
	}

	public Color getColor() { return c; }
	public int getFacing() { return facing; }
	public Point2D getPosition() { return (Point2D)p.clone(); }
	public boolean isDown() { return down; }
	
	public void setColor(String s) {
		c = Color.decode(s);
		graphics.setColor(c);
	}
	
	public void up() {
		down = false;
	}
	
	public void down() {
		down = true;
	}
	
	public void left(int theta) {
		facing += theta;
	}
	
	public void right(int theta) {
		facing -= theta;
	}
	
	public void forw(int d) {
		Point2D.Double temp = (Point2D.Double) p.clone();
		p.x = p.x + d * Math.cos(Math.PI*facing/180);
		p.y = p.y + d * Math.sin(Math.PI*facing/180);
		if(down) {
			graphics.setColor(c);
			graphics.drawLine((int)temp.x, (int)temp.y, (int)p.x, (int)p.y);
		}
	} 
	
	public void back(int d) {
		//Walking backwards is the same as turning around 180 degrees and walking forward.
		int tf = facing;
		facing += 180;
		forw(d);
		facing = tf;
	}
}
