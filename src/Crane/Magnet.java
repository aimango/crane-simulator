package Crane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

//TODO:
//Figure out block angle & falling & placing.
//Do automatic EM detection - while(true) loop?
//Then, do crayyyyy stuff with collision detection.

public class Magnet extends Drawable {

	private static final long serialVersionUID = 1L;  // get rid of warning
	private Rectangle rect;
	public ArrayList<Block> blocks = new ArrayList<Block>();
	private boolean hasBlock = false;
	public boolean turnedOn = false;
	
	public Magnet(int x, int y, double angle, Drawable parent, Color fill){
		super(x,y,angle,parent,fill);
		rect = new Rectangle(0,0,80,30);
	}
	
	public boolean getOn(){
		return turnedOn;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform aiCurr = g2.getTransform(); // to recover at the end
		
		g2.transform(getTransform()); // offset
		g2.setColor(fillColor);
		g2.fill(rect);
		g2.setColor(Color.black);
		g2.draw(rect);
		g2.fillOval(0, 0, 10, 10);
		g2.setTransform(aiCurr); // set to
		
	}

	protected void checkAttach(){
		for (int i = 0; i < blocks.size(); i++){
			//have rectangular regions on each side of a block to check if there are some intersections
			double x = blocks.get(i).x;
			double y = blocks.get(i).y;
			int width = blocks.get(i).getWidth();
			int height = blocks.get(i).getHeight();
			
			System.out.println("Width "+width+" Height "+ height);
			Point2D p1 = getPointInverse(new Point2D.Double(x, y), false);
			Point2D p2 = getPointInverse(new Point2D.Double(x+width, y), false);
			Point2D p3 = getPointInverse(new Point2D.Double(x+width, y+height), false);
			Point2D p4 = getPointInverse(new Point2D.Double(x, y+height), false);
			
			double p1x = p1.getX();
			double p1y = p1.getY();
			double p2x = p2.getX();
			double p2y = p2.getY();
			double p3x = p3.getX();
			double p3y = p3.getY();
			double p4x = p4.getX();
			double p4y = p4.getY();

			//cray coord checking -- good
			if (p1y >= 30 && p1y <= 40 && p4y >= 30 && p4y <= 40 && p1x >= height/2 && p1x <= 80+height/2){
				System.out.println("attach\np1 coord (" + p1x + ", " + p1y + 
						") p4 coord ("+ p4x + ", " + p4y + ")");
				hasBlock = true;
				blocks.add(new Block((int)p4x,(int)p4y,height,width,Math.toRadians(90),this,Color.red)); // somehow centre it....
				blocks.remove(i);

				break; // break so we only pick up 1 block. if 2 are closeby then only pick the first one ;D
			} // good 
			else if (p4y >= 30 && p4y <= 40 && p3y >= 30 && p3y <= 40 && p4x >= width/2 && p4x <= 80+width/2){
				System.out.println("attach\np4 coord (" + p4x + ", " + p4y + 
						") p3 coord ("+ p3x + ", " + p3y + ")");
				hasBlock = true;
				blocks.add(new Block(0,30,width,height,Math.toRadians(90),this,Color.red)); // somehow centre it....
				blocks.remove(i);

				break; // break so we only pick up 1 block. if 2 are closeby then only pick the first one ;D
			} else if (p3y >= 30 && p3y <= 40 && p2y >= 30 && p2y <= 40 && p3x >= height/2 && p3x <= 80+height/2){
				System.out.println("attach\np3 coord (" + p3x + ", " + p3y + 
						") p2 coord ("+ p2x + ", " + p2y + ")");
				hasBlock = true;
				blocks.add(new Block(0,30,width,height,Math.toRadians(90),this,Color.red)); // somehow centre it....
				blocks.remove(i);

				break; // break so we only pick up 1 block. if 2 are closeby then only pick the first one ;D
			} //good
			else if (p2y >= 30 && p2y <= 40 && p1y >= 30 && p1y <= 40 && p2x >= width/2 && p1x <= 80+width/2){
				System.out.println("attach\np2 coord (" + p2x + ", " + p2y + 
						") p1 coord ("+ p1x + ", " + p1y + ")");
				hasBlock = true;
				blocks.add(new Block(0,30,width,height,Math.toRadians(90),this,Color.red)); // somehow centre it....
				blocks.remove(i); 

				break; // break so we only pick up 1 block. if 2 are closeby then only pick the first one ;D
			} else {
				System.out.println("clicked but nothing to pick up");
				System.out.println("p1 coord (" + p1x + ", " + p1y + ") p2 coord (" + p2x + ", " + p2y + 
						") p3 coord ("+ p3x + ", " + p3y + ") p4 coord ("+ p4x + ", " + p4y + ")");
			}
		}
	}
	
	private double getAngle(Point2D origin, Point2D other) {
	    double dy = other.getY() - origin.getY();
	    double dx = other.getX()- origin.getX();
	    double angle;
	    double PI = Math.PI;
	    if (dx == 0) // special case
	        angle = dy >= 0? PI/2: -PI/2;
	    else
	    {
	        angle = Math.atan(dy/dx);
	        if (dx < 0) // hemisphere correction
	            angle += PI;
	    }
	    // all between 0 and 2PI
	    if (angle < 0) // between -PI/2 and 0
	        angle += 2*PI;
	    return angle;
	}
	
	protected void releaseBlock(){
		Point2D ground = getPointInverse (new Point2D.Double(0, 50), false);
		ground = getPointInverse(ground, false);
		System.out.println("ground " + ground.getX() + " " + ground.getY());
		//blocks.get(attachedBlockIndex).moveItem(ground);
		
		Block b = blocks.get(blocks.size()-1);
		//System.out.println(b.getX() + " "+ b.getY());
		Point2D p1 = getPointInverse(new Point2D.Double(b.getX(), b.getY()), true);
		Point2D p4  = getPointInverse(new Point2D.Double(b.getX(), b.getY()+b.getHeight()), true);
		double angle = getAngle(p1,p4);
		blocks.remove(blocks.size()-1);
		blocks.add(new Block((int)p1.getX(),(int)p1.getY(),b.getWidth(),b.getHeight(), angle, null, Color.orange)); 
//		check if it's parallel to the ground.
		hasBlock = false; 
	}

	protected boolean isInside(Point2D p) {
		p = getPointInverse(p, false);
		if (p.getX() > 0 && p.getX() < 80 && p.getY() > 0 && p.getY() < 30){
			if (turnedOn && hasBlock){
				System.out.println("HAS BLOCK. RELEASE");
				releaseBlock();
			}
			turnedOn = !turnedOn;
			if (turnedOn)
				this.fillColor = Color.white;
			else
				this.fillColor =  new Color(153, 102, 204);
			String s = turnedOn ? "ON" : "OFF";
			System.out.println("Turning " + s);
			return true;
		} else {
			return false;
		}
	}
	
	protected void blockInteraction(){
		if (!hasBlock){
			System.out.println("DOESNT HAVE BLOCK");
			checkAttach();
		} 
	}
}