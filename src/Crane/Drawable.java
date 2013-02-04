package Crane;

import java.awt.Color;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

public class Drawable extends JComponent {

	private static final long serialVersionUID = 1L;  // get rid of warning
	protected int x, y;
	protected Drawable parent = null;
	protected AffineTransform at;
	protected Color fillColor;

	public Drawable(int x, int y, double angle, Drawable parent, Color fill){
		this.x = x;
		this.y = y;
		this.parent = parent;
		at = new AffineTransform();
		at.translate(x, y);
		at.rotate(angle);
		this.fillColor = fill;
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
}