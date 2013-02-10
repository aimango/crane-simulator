package cranepackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

public class Drawable extends JComponent {

	private static final long serialVersionUID = 1L;  // get rid of warning
	protected int x, y;
	protected Drawable parent = null;
	protected AffineTransform at = new AffineTransform();
	protected Color fillColor;

	public Drawable(int x, int y, double angle, Drawable parent, Color fill){
		this.x = x;
		this.y = y;
		this.parent = parent;
		at.translate(x, y);
		at.rotate(angle);
		this.fillColor = fill;
	}

	public void paintComponent(Graphics g) {}
	
	protected AffineTransform getTransform() {
		AffineTransform atParent;
		if (parent != null){
			atParent = new AffineTransform(parent.getTransform()); // recursive
			atParent.concatenate(at);
		} else {
			atParent = at;
		}
		return atParent;
	}
	
	protected Point2D getPointInverse(Point2D p, boolean realWorld) {
		AffineTransform temp = new AffineTransform(this.getTransform()); // copy cxr
		if (!realWorld){
			try {
				temp = temp.createInverse();
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Point2D d = new Point2D.Double();
		temp.transform(p, d);
		return d;
	}

	protected void getAngleUpdated(double angle, boolean realWorld){
		AffineTransform temp = new AffineTransform(this.getTransform());
		//double newAngle = 0;
		temp.rotate(angle);
		at=temp;
		//return newAngle; //!?!?!?
	}
	
	protected boolean isInside(Point2D p) {
		return false;
	}

	protected void moveItem(Point2D p) {}
	

}