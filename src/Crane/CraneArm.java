package Crane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class CraneArm extends Drawable {

	private static final long serialVersionUID = 1L;  // get rid of warning
	private Rectangle rect;

	public CraneArm(int x, int y, double angle, Drawable parent, Color fill){
		super(x,y,angle,parent,fill);
		rect = new Rectangle(-25,-25,50,100);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(fillColor);
		
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		g2.transform(getTransform()); // offset
		g2.fill(rect);
		g2.setTransform(aiCurr); // set to
	}
	
	protected AffineTransform getTransform(){
		AffineTransform atParent;
		if (parent != null){
			atParent = new AffineTransform(parent.getTransform()); // recursive
			atParent.concatenate(at);
		} else {
			atParent = at;
		}
		return atParent;
	}
	
	protected Point2D getPointInverse(Point2D p){
		AffineTransform temp = new AffineTransform(getTransform()); // copy cxr
		try {
			temp = temp.createInverse();
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Point2D d = new Point2D.Double();
		temp.transform(p, d);
		return d;
	}
	
	protected boolean isInside(Point2D p){
		Point2D d = getPointInverse(p);
		if (d.getX() > -25 && d.getX() < 25 && d.getY() > -25 && d.getY() < 75 ){ // bounds check
			System.out.print("yay");
			return true;
		}
		else{
			return false;
		}
	}

	protected void moveItem(Point2D p) {
		Point2D d = getPointInverse(p);
		double angle = Math.atan2(d.getY(), d.getX());
		at.rotate(angle-Math.toRadians(90)); // funny hack... not sure why jittering off by 90 deg
	}
}