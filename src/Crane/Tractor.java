package Crane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class Tractor extends Drawable {

	private static final long serialVersionUID = 1L;  // get rid of warning

	public Tractor(int x, int y, double angle, Drawable parent, Color fill){
		super(x,y,angle,parent,fill);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		//g2.setColor(fillColor);
		
		//AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		//g2.transform(getTransform()); // offset
		//g2.fill(rect);
		
		
		// http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
		final float dash1[] = {10.0f};
	    final BasicStroke dashed = new BasicStroke(3.0f,
	                        BasicStroke.CAP_BUTT,
	                        BasicStroke.JOIN_MITER,
	                        10.0f, dash1, 0.0f);
	    
		// Do all drawing with respect to the origin (0,0)
		// Translate to where we want it displayed.
		g2.translate(50, 500);
		
		g2.setColor(Color.CYAN);
	    int[] x = new int[]{0, 0, 20, 180, 200, 200};
	    int[] y = new int[]{0,-100,-150, -150, -100, 0};
	    g2.fillPolygon (x, y, x.length);

	    g2.setColor(Color.BLUE);
		g2.drawPolygon(x, y, x.length);
		
	    x = new int[]{80, 90, 110, 120};
	    y = new int[]{-150,-180,-180, -150};
		g2.fillPolygon(x, y, x.length);

		g2.setColor(Color.GRAY);
		g2.fillRect(-10, 0, 220, 40);
		
		g2.setColor(Color.BLACK);
		g2.setStroke(dashed);
		g2.drawRect(-10, 0, 220, 40);
		
		
		//g2.setTransform(aiCurr); // set to
	}
	
	public AffineTransform getTransform(){
		//AffineTransform aiChild = ai;
		AffineTransform atParent;
		if (parent != null){
			atParent = new AffineTransform(parent.getTransform()); // recursive
			atParent.concatenate(at);
		} else {
			atParent = at;
		}
		return atParent;
	}
	
}