package Crane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public class Block extends Drawable {

	private static final long serialVersionUID = 1L;  // get rid of warning
	private Rectangle rect;
	protected int height, width;
	protected double angle;
	
	public Block(int x, int y, int height, int width, double angle, Drawable parent, Color fill) {
		super(x, y, angle, parent, fill);
		//i should randomly generate a height and width : O and x,y..... meh.
		rect = new Rectangle(0, 0, width, height);
		this.height = height;
		this.width = width;
		this.angle = angle;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		
		g2.transform(getTransform()); // offset
		g2.setColor(fillColor);
		
		if (parent != null) { // means it's attached to the magnet!
			g2.translate(0, -90);
		}
		g2.fill(rect);
		g2.setColor(Color.black);
		g2.draw(rect);
		g2.fillOval(0, 0, 10, 10);
		g2.setTransform(aiCurr); // set to
	}

	protected boolean isInside(Point2D p) {
		Point2D d = p;
		if (d.getX() > x && d.getX() < x+230 && d.getY() > y && d.getY() < y+40){ // bounds check
			return true;
		} else {
			return false;
		}
	}
	
	//when blocks are falling down.
	//dont remove as child until it hits the ground.
	//go through all the other blocks and check if position is below the placed block.
	protected void moveItem(Point2D p){
		System.out.println(y + " " + p.getY());
		while(y > p.getY()){
			y-=10;
		}
		System.out.println(y + " " + p.getY());
		this.repaint();
	}

	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
}