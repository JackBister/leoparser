import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class Leonardo {
	boolean down;
	int facing;
	Point2D.Double p;
	Color c;
	
	public Leonardo() {
		down = false;
		facing = 0;
		p = new Point2D.Double(0, 0);
		c = new Color(0, 0, 255);
	}
	
	public void setColor(String s) {
		c = Color.decode(s);
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
		if(down)
			System.out.printf("#%02x%02x%02x %.4f %.4f %.4f %.4f\n", c.getRed(), c.getGreen(), c.getBlue(), temp.x, temp.y, p.x, p.y);
	} 
	
	public void back(int d) {
		Point2D.Double temp = (Point2D.Double) p.clone();
		//Vänder tillfälligt leonardo baklänges och går sedan framåt i den riktningen
		int tf = facing + 180;
		p.x = p.x + d * Math.cos(Math.PI*tf/180);
		p.y = p.y + d * Math.sin(Math.PI*tf/180);
		if(down)
			System.out.printf("#%02x%02x%02x %.4f %.4f %.4f %.4f\n", c.getRed(), c.getGreen(), c.getBlue(), temp.x, temp.y, p.x, p.y);
	}
}
