/**
 * Elisa Lou 20372456.
 * Assignment 2 - Direct Manipulation.
 */

package Crane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

public class BaseCrane extends JPanel{
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
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

		//g2.translate(500, 500);
	
	}
}
