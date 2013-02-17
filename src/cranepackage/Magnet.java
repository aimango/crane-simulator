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
	private int currBlock;
	
	public Magnet(int x, int y, double angle, Drawable parent, Color fill){
		super(x,y,angle,parent,fill);
		rect = new Rectangle(-40,-15,80,30);
	}
	
	private double getAngle(Point2D origin, Point2D other) {
	    double dy = other.getY() - origin.getY();
	    double dx = other.getX() - origin.getX();
	    double angle;
	    double PI = Math.PI;
	    if (dx == 0) // special case
	        angle = dy >= 0? PI/2: -PI/2;
	    else {
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
	
	// up to the nearest 10s
	private double roundAngle(double angle){
		angle = Math.toDegrees(angle);
		angle /= 10;
		angle = (int)(angle+0.5);
		angle *= 10;
		angle = Math.toRadians(angle);
		return angle;
	}

	protected boolean isInside(Point2D p) {
		p = getPointInverse(p, false);
		if (p.getX() > -40 && p.getX() < 40 && p.getY() > -15 && p.getY() < 15){
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
			
			Point2D p1 = getPointInverse(new Point2D.Double(x-width/2, y-height/2), false);
			Point2D p2 = getPointInverse(new Point2D.Double(x+width/2, y-height/2), false);
			Point2D p3 = getPointInverse(new Point2D.Double(x+width/2, y+height/2), false);
			Point2D p4 = getPointInverse(new Point2D.Double(x-width/2, y+height/2), false);
			
			double p1x = p1.getX();
			double p1y = p1.getY();
			double p2x = p2.getX();
			double p2y = p2.getY();
			double p3x = p3.getX();
			double p3y = p3.getY();
			double p4x = p4.getX();
			double p4y = p4.getY();

			//cray coord checking
			//System.out.println("height/2 is " + height/2+ " width/2 is "+width/2);
			if (p1x >= 40 && p1x <= 50 && p4x >= 40 && p4x <= 50 
					&& ((p4y < 0 && p1y >= -width/4) || (p4y >= -width/4 && p4y <= 0))){
				System.out.println("attach\np1 coord (" + p1x + ", " + p1y + 
						") p4 coord ("+ p4x + ", " + p4y + ")");
				hasBlock = true;
				blocks.get(i).x = 0;
				blocks.get(i).y = 0;
				blocks.get(i).parent = this;
				blocks.get(i).at = new AffineTransform();
				blocks.get(i).at.translate(0,105+height/2); // hackz
				currBlock = i;
				break;
			} else if (p3y >= 15 && p3y <= 25 && p4y >= 15 && p4y <= 25 
					&& ((p3x < 0 && p4x >= -width/4) || (p3x >= -width/4 && p3x <= 0))){
				System.out.println("attach\np4 coord (" + p4x + ", " + p4y + 
						") p3 coord ("+ p3x + ", " + p3y + ")");
				blocks.get(i).x = 0;
				blocks.get(i).y = 0;
				hasBlock=true;
				blocks.get(i).parent = this;
				blocks.get(i).at = new AffineTransform();
				blocks.get(i).at.translate(0,105+height/2); // hackz
				currBlock = i;
				break; 
			} else if (p3x >= 40 && p3x <= 50 && p2x >= 40 && p2x <= 50 
					&& ((p2y < 0 && p3y >= -height/4) || (p2y >= -height/4 && p2y <= 0))){
				System.out.println("attach\np3 coord (" + p3x + ", " + p3y + 
						") p2 coord ("+ p2x + ", " + p2y + ")");
				blocks.get(i).x = 0;
				blocks.get(i).y = 0;
				hasBlock = true;
				blocks.get(i).parent = this;
				blocks.get(i).at = new AffineTransform();
				blocks.get(i).at.translate(0,105+height/2); // hackz
				currBlock = i;
				break; 
			} else if (p2y >= 15 && p2y <= 25 && p1y >= 15 && p1y <= 25 
					&& ((p1x < 0 && p2x >= -width/4) || (p1x >= -width/4 && p1x <= 0))){
				System.out.println("attach\np2 coord (" + p2x + ", " + p2y + 
						") p1 coord ("+ p1x + ", " + p1y + ")");
				hasBlock = true;
				blocks.get(i).x = 0;
				blocks.get(i).y = 105+height/2;

				blocks.get(i).parent = this;
				blocks.get(i).at = new AffineTransform();
				blocks.get(i).at.translate(blocks.get(i).x, blocks.get(i).y); // hackz
				currBlock = i;
				break;
			} else {
				System.out.println("p1 coord (" + p1x + ", " + p1y + ") p2 coord (" + p2x + ", " + p2y + 
						") p3 coord ("+ p3x + ", " + p3y + ") p4 coord ("+ p4x + ", " + p4y + ")");
			}
		}
	}

	private double getNewBlockLoc(int currBlock, boolean angleOkay, double anglez){

		Block b = blocks.get(currBlock);
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		ArrayList<Point2D> points2 = new ArrayList<Point2D>();
//		points.add( getPointInverse(new Point2D.Double(b.x, b.y-120+30+1), true));
//		points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth(), b.y), true));
//		points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth(), b.y+b.getHeight()-120+30+1), true));
//		points.add( getPointInverse(new Point2D.Double(b.x, b.y+b.getHeight()-120+30+1), true));	

		points.add( getPointInverse(new Point2D.Double(b.x-b.getWidth()/2, b.y-b.getHeight()/2-120+30+1), true));
		points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth()/2, b.y-b.getHeight()/2), true));
		points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth()/2, b.y+b.getHeight()/2-120+30+1), true));
		points.add( getPointInverse(new Point2D.Double(b.x-b.getWidth()/2, b.y+b.getHeight()/2-120+30+1), true));

		double maxY = 0, maxY2 = 0;
		int index = -1, index2 = -1;

		for (int i = 0; i < points.size(); i++){
			double currY = points.get(i).getY();
			if (currY >= maxY){
				maxY = currY;
				index = i;
			}
		}
		points.remove(index);
		for (int i = 0; i < points.size(); i++){
			double currY = points.get(i).getY();
			if (currY >= maxY2){
				maxY2 = currY;
				index2 = i;
			}
		}
		//System.out.println("maxY, maxY2 "+ maxY+ " " + maxY2);
		maxY = -1;

		if (angleOkay) {
			double lowestX, lowestX2, lowestY;
			anglez = Math.toDegrees(anglez);
			if (anglez % 180 == 0) {
				lowestX = b.x-b.getWidth()/2;
				lowestX2 = b.x+b.getWidth()/2;
				lowestY = b.y-b.getHeight()/2;
			} else{
				lowestX = b.x-b.getHeight()/2;
				lowestX2 = b.x+b.getHeight()/2;
				lowestY = b.y-b.getWidth()/2;
			}

			points2.add(getPointInverse(new Point2D.Double(lowestX, lowestY), true));
			points2.add(getPointInverse(new Point2D.Double(lowestX2, lowestY), true));
			System.out.println("x1, x2 "+ points2.get(0).getX() + " " + points2.get(1).getX());
			for (int i = 0; i < blocks.size(); i++){
				if (i == currBlock)
					continue;
				Block c = blocks.get(i);
				if (c.isInside(points2.get(0).getX(), points2.get(1).getX())){
					System.out.println("why is "+ points2.get(0).getY());
					double height = anglez % 180 == 0 ? b.getWidth() : b.getHeight();
					maxY = c.y - c.getHeight()/2 - height/2;//- c.getHeight(); // cant subtract by height if falls at rotated loc..
					break;
				}
			}
		} else { 
			for (int i = 0; i < blocks.size(); i++){
				if (blocks.get(i).isInside(points.get(index))){
					maxY = blocks.get(i).y;
					break;
				}
			}
		}
		if (maxY != -1){
			System.out.println(maxY);
			return maxY;
		} else {
			double height = anglez % 180 != 0 ? b.getHeight() : b.getWidth();
			return 530 - height/2;
		}
	}
	
	protected void releaseBlock(){
		Block b = blocks.get(currBlock);
		System.out.println("B coords " + b.x + " "+ b.y);
		
		Point2D old1, old2;
		old1 = new Point2D.Double(b.x, b.y-120+30+1);
		old2 = new Point2D.Double(b.x, b.y+b.getHeight()-120+30+1);
		Point2D p1 = getPointInverse(new Point2D.Double(b.x, b.y-120+30+1), true);
		Point2D p4 = getPointInverse(new Point2D.Double(b.x, b.y+b.getHeight()-120+30+1), true);
		//double angle = getAngle(p1, p4);
		double angle = Math.atan2(p4.getY()-p1.getY(), p4.getX()-p1.getX());
		
		System.out.println("Angle "+Math.toDegrees(angle));
		
		boolean angleOkay = checkAngleParallelish(angle);
		if (angleOkay){
			angle = roundAngle(angle);
			System.out.println("Angle rounded "+Math.toDegrees(angle));
		}
		double ry = getNewBlockLoc(currBlock, angleOkay, angle);
		Color c = angleOkay ? new Color (255, 97, 215) : Color.gray;

		System.out.print("x, y "+p1.getX()+ ", "+ry);
		
		blocks.get(currBlock).parent = null;
		blocks.get(currBlock).at = new AffineTransform();
		blocks.get(currBlock).x = (int)p1.getX();
		blocks.get(currBlock).y = (int)ry;
		blocks.get(currBlock).at.translate(p1.getX(), ry);
		blocks.get(currBlock).angle = angle; 
		blocks.get(currBlock).at.rotate(angle);
		blocks.get(currBlock).fillColor = c;

		hasBlock = false; 
	}

	protected void blockInteraction(){
		if (!hasBlock){
			checkAttach();
		} 
	}
}