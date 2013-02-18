package cranepackage;

import java.awt.BasicStroke;
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
	protected boolean onItsSide;
	protected int origX, origY;
	//protected int velocity;
	//protected int offset = 0;
	public Block(int x, int y, int height, int width, double angle, Drawable parent, Color fill) {
		super(x, y, angle, parent, fill);
		origX = x;
		origY = y;
		rect = new Rectangle(-width/2, -height/2, width, height);
		this.height = height;
		this.width = width;
		this.angle = angle;
		this.onItsSide = false;
		//this.velocity = 0;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		
		g2.transform(getTransform()); // offset
		g2.setStroke(new BasicStroke(2));
		g2.setColor(fillColor);
		if (parent != null) { // means it's attached to the magnet!
			g2.translate(0, -90);
		} 
//		else if (parent == null && angle != 0){
//			g2.rotate(Math.toRadians(-90));
//		}
//		offset +=velocity;
//		y += velocity;
//		g2.translate(0, offset);
		g2.fill(rect);
		g2.setColor(Color.black);
		g2.draw(rect);
		g2.fillOval(0, 0, 10, 10);
		g2.setTransform(aiCurr); // set to
	}

	protected boolean isInside(Point2D p1) {
		if (p1.getX() > x-width/2 && p1.getX() < x+width/2){ // bounds check
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean isInside(double x1, double x2) {
		System.out.println("x1, x2 " + x1 + " " + x2);
		if ( (x1 < x-width/2 && x2 > x-width/2) || (x1 > x-width/2 && x1 < x+width/2)){ 
			return true;
		} else {
			return false;
		}
	}
	


	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
}