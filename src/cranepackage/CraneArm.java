package cranepackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class CraneArm extends Drawable {

	private static final long serialVersionUID = 1L;  // get rid of warning
	private Rectangle rect;
	
	public CraneArm(int x, int y, double angle, Drawable parent, Color fill) {
		super(x,y,angle,parent,fill);
		rect = new Rectangle(-25,-25,40,150);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(fillColor);
		
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		g2.transform(getTransform()); // offset
		g2.fill(rect);
		
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.black);
		g2.draw(rect);
		g2.setColor(Color.darkGray);
		g2.fillOval(-12, -20, 16, 16);
		g2.setTransform(aiCurr); // set to
	}
	
	protected boolean isInside(Point2D p) {
		p = getPointInverse(p, false);
		if (p.getX() > -25 && p.getX() < 15 && p.getY() > -25 && p.getY() < 125){ // bounds check
			System.out.print("yay");
			return true;
		} else {
			return false;
		}
	}

	protected void moveItem(Point2D p) {
		Point2D d = getPointInverse(p, false);
		double angle = Math.atan2(d.getY(), d.getX());
		at.rotate(angle-Math.toRadians(90)); // funny hack... not sure why jittering off by 90 deg
		//TODO: still jitters the first time a single joint is manipulated.
	}
}