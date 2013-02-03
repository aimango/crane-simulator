package Crane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class Drawable extends JComponent {
	int x, y;
	double angle;
	Drawable parent = null;
	AffineTransform ai;
	Color fillColor;
	public Drawable(int x, int y, double angle, Drawable parent, Color fill){
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.parent = parent;
		ai = new AffineTransform();
		ai.translate(x, y);
		ai.rotate(angle);
		//.getRotateInstance(angle, x, y);
		this.fillColor = fill;
	}
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(fillColor);
		
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		g2.transform(getTransform()); // offset
		//g.translate(100, 100);
		g2.fillRect(0,0,50,100);
		g2.setTransform(aiCurr); // set to
	}
	
	public AffineTransform getTransform(){
		//AffineTransform aiChild = ai;
		AffineTransform aiParent;
		if (parent != null){
			//System.out.print("trolol");
			aiParent = new AffineTransform(parent.getTransform());
			//aiParent = parent.getTransform();
			aiParent.concatenate(ai);
		} else {
			aiParent = ai;
		}
		return aiParent;
	}
}