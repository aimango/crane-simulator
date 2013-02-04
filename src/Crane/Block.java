package Crane;

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
	public Block(int x, int y, double angle, Drawable parent, Color fill){
		super(x,y,angle,parent,fill);
		//randomly generate a height and width : O and x,y.....
		rect = new Rectangle(-25, -25, 30, 80);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		g2.transform(getTransform()); // offset
		g2.fill(rect);
		g2.setTransform(aiCurr); // set to
		
	}

	protected boolean isInside(Point2D p){
		Point2D d = p;
		if (d.getX() > x && d.getX() < x+230 && d.getY() > y && d.getY() < y+40){ // bounds check
			//xBegin = d.getX();
			return true;
		}
		else{
			return false;
		}
	}

	protected void moveItem(Point2D p) {
		//int delta = (int) (p.getX()-xBegin);
		//at.translate(delta, 0);
		//xBegin = p.getX();
		//this.x = x+delta;
	}
	
}