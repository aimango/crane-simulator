package cranepackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public class Tractor extends Drawable {
	
	private static final long serialVersionUID = 1L;  // get rid of warning
	double xBegin = -1;
	
	public Tractor(int x, int y, double angle, Drawable parent, Color fill){
		super(x,y,angle,parent,fill);
	}
	
	public void paintComponent(Graphics g) {
		
		// http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
		final float dash1[] = {12.0f};
	    final BasicStroke dashed = new BasicStroke(4.0f, BasicStroke.CAP_BUTT,
	                        BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	    
	    Graphics2D g2 = (Graphics2D) g;
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		g2.transform(getTransform()); // offset
		
		g2.setStroke(new BasicStroke(2));

		g2.setColor(Color.gray);
		g2.fillRect(-10, 0, 220, 40);
		
	    int[] x = new int[]{0, 0, 20, 180, 200, 200};
	    int[] y = new int[]{0,-80,-100, -100, -80, 0};
		g2.setColor(fillColor);
	    g2.fillPolygon (x, y, x.length);
	    g2.setColor(Color.black);
		g2.drawPolygon(x, y, x.length);
		
	    x = new int[]{60, 70, 130, 140};
	    y = new int[]{-100,-130,-130, -100};
	    g2.setColor(fillColor);
		g2.fillPolygon(x, y, x.length);
		g2.setColor(Color.black);
		g2.drawPolygon(x, y, x.length);
		
		g2.setStroke(dashed);
		g2.drawRect(-10, 0, 220, 40);

		g2.setTransform(aiCurr); // set to
	}

	//TODO: dont allow moving in the triangular parts..
	protected boolean isInside(Point2D p) {
		if (p.getX() > x - 10 && p.getX() < x+210 && p.getY() > y && p.getY() < y+40 ||
				p.getX() >= x && p.getX() < x+200 && p.getY() >= y-100 && p.getY() < y){ // bounds check
			xBegin = p.getX();
			return true;
		} else {
			return false;
		}
	}

	protected void moveItem(Point2D p) {
		int delta = (int) (p.getX()-xBegin);
		at.translate(delta, 0);
		xBegin = p.getX();
		this.x = x+delta;
	}
	
}