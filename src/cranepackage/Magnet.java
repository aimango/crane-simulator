package cranepackage;

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
			double x = blocks.get(i).x;
			double y = blocks.get(i).y;
			int width = blocks.get(i).getWidth();
			int height = blocks.get(i).getHeight();
			
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
			if (p1y >= 28 && p1y <= 40 && p4y >= 28 && p4y <= 40 && p1x >= height/2 && p1x <= 80+height/2){
				System.out.println("attach\np1 coord (" + p1x + ", " + p1y + 
						") p4 coord ("+ p4x + ", " + p4y + ")");
				hasBlock = true;
				blocks.add(new Block((int)p1x,30,height,width,Math.toRadians(90),this,Color.red)); 
				blocks.remove(i);

				break; 
			} // good 
			else if (p4y >= 28 && p4y <= 40 && p3y >= 28 && p3y <= 40 && p4x >= width/2 && p4x <= 80+width/2){
				System.out.println("attach\np4 coord (" + p4x + ", " + p4y + 
						") p3 coord ("+ p3x + ", " + p3y + ")");
				hasBlock = true;
				blocks.add(new Block(0,30,width,height,Math.toRadians(90),this,Color.red));
				blocks.remove(i);

				break; 
			} else if (p3y >= 28 && p3y <= 40 && p2y >= 28 && p2y <= 40 && p3x >= height/2 && p3x <= 80+height/2){
				System.out.println("attach\np3 coord (" + p3x + ", " + p3y + 
						") p2 coord ("+ p2x + ", " + p2y + ")");
				hasBlock = true;
				blocks.add(new Block(0,0,width,height,Math.toRadians(90),this,Color.red));
				blocks.remove(i);

				break; 
			} //good
			else if (p2y >= 28 && p2y <= 40 && p1y >= 28 && p1y <= 40 && p2x >= width/2 && p2x <= 80+width/2){
				System.out.println("attach\np2 coord (" + p2x + ", " + p2y + 
						") p1 coord ("+ p1x + ", " + p1y + ")");
				hasBlock = true;
				blocks.add(new Block((int)p1x,120,height,width,Math.toRadians(0),this,Color.red)); 
				
				blocks.remove(i); 
				System.out.println("blocks[lastindex] = " + blocks.get(blocks.size()-1).x + " "+ blocks.get(blocks.size()-1).y);
				break;
			} else {
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
	
	//	check if it's parallel to the ground +/-5 degrees
	private boolean checkAngleParallelish(double angle){
		angle = Math.toDegrees(angle);
		angle %= 90;
		if (Math.abs(angle) < 5 || Math.abs(angle) > 85)
			return true;
		else
			return false;
	}
	
	private double getNewBlockLoc(Block b){
		// so go through each point, find the one with the highest y value.
		// get new p1 y value based on this high y value.
		// go through block list for collision detection. -> look at their p1.y values

		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add( getPointInverse(new Point2D.Double(b.x, b.y-120+30+1), true));
		points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth(), b.y), true));
		points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth(), b.y+b.getHeight()-120+30+1), true));
		points.add( getPointInverse(new Point2D.Double(b.x, b.y+b.getHeight()-120+30+1), true));
		double maxY = 0;
		int index = -1;
		for (int i = 0; i < points.size(); i++){
			double currY = points.get(i).getY();
			if ( currY > maxY){
				maxY = currY;
				index = i;
			}
		}
		maxY = -1;
		for (int i = 0; i < blocks.size(); i++){
			if (blocks.get(i).isInside(points.get(index))){
				maxY = blocks.get(i).y;
				break;
			}
		}
		if (maxY != -1){
			return maxY;
		} else {
			return 530;
		}
	}
	
	protected void releaseBlock(){
		//Point2D ground = getPointInverse (new Point2D.Double(0, 50), false);
		//ground = getPointInverse(ground, false);
		//System.out.println("ground " + ground.getX() + " " + ground.getY());
		//blocks.get(attachedBlockIndex).moveItem(ground);
		
		Block b = blocks.get(blocks.size()-1);
		System.out.println(b.x + " "+ b.y);
		
		Point2D p1 = getPointInverse(new Point2D.Double(b.x, b.y-120+30+1), true);
		Point2D p4 = getPointInverse(new Point2D.Double(b.x, b.y+b.getHeight()-120+30+1), true);
		double angle = getAngle(p1, p4);
		
		System.out.println("Angle "+Math.toDegrees(angle));
		blocks.remove(blocks.size()-1); 
		
		double ry = getNewBlockLoc(b);
		boolean angleOkay = checkAngleParallelish(angle);
		Color c = angleOkay ? new Color (255, 97, 215) : Color.gray;
		blocks.add(new Block((int)p1.getX(), (int)ry, b.getHeight(), b.getWidth(), angle, null, c)); 

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
				this.fillColor = new Color(200, 36, 255);
			else
				this.fillColor =  new Color(172, 0, 230);
			String s = turnedOn ? "ON" : "OFF";
			System.out.println("Turning " + s);
			return true;
		} else {
			return false;
		}
	}
	
	protected void blockInteraction(){
		if (!hasBlock){
			//System.out.println("DOESNT HAVE BLOCK");
			checkAttach();
		} 
	}
}