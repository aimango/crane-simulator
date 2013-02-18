package cranepackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;


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
		angle = angle > 0 ? (int)(angle+0.5) : (int)(angle-0.5);
		angle *= 10;
		angle = Math.toRadians(angle);
		return angle;
	}

	protected boolean isInside(Point2D p) {
		p = getPointInverse(p, false);
		if (p.getX() > -40 && p.getX() < 40 && p.getY() > -15 && p.getY() < 15){
			if (turnedOn && hasBlock){
				System.out.println("Releasing block.");
				releaseBlock();
			}
			turnedOn = !turnedOn;
			if (turnedOn){
				this.fillColor = new Color(200, 36, 255);
			} else {
				this.fillColor =  new Color(172, 0, 230);
			}
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

	protected void attach(){
		for (int i = 0; i < blocks.size(); i++){
			Block b = blocks.get(i);
			double x = b.x;
			double y = b.y;
			int width = b.getWidth();
			int height = b.getHeight();
			
			Point2D p1, p2, p3, p4;
			if (!b.onItsSide){
				p1 = getPointInverse(new Point2D.Double(x-width/2, y-height/2), false);
				p2 = getPointInverse(new Point2D.Double(x+width/2, y-height/2), false);
				p3 = getPointInverse(new Point2D.Double(x+width/2, y+height/2), false);
				p4 = getPointInverse(new Point2D.Double(x-width/2, y+height/2), false);
			} else {
				p1 = getPointInverse(new Point2D.Double(x-height/2, y-width/2), false);
				p2 = getPointInverse(new Point2D.Double(x+height/2, y-width/2), false);
				p3 = getPointInverse(new Point2D.Double(x+height/2, y+width/2), false);
				p4 = getPointInverse(new Point2D.Double(x-height/2, y+width/2), false);
			}
			double p1x = p1.getX();
			double p1y = p1.getY();
			double p2x = p2.getX();
			double p2y = p2.getY();
			double p3x = p3.getX();
			double p3y = p3.getY();
			double p4x = p4.getX();
			double p4y = p4.getY();
			
			if (!b.onItsSide && p2y >= 15 && p2y <= 25 && p1y >= 15 && p1y <= 25){
				if ((p1x < 0 && p2x >= -width/4) || (p1x >= -width/4 && p1x <= 0)){
					
					//System.out.println("p2 coord (" + p2x + ", " + p2y + ") p1 coord ("+ p1x + ", " + p1y + ")");
					hasBlock = true;
					blocks.get(i).x = 0;
					blocks.get(i).y = 105+height/2;
					blocks.get(i).parent = this;
					blocks.get(i).at = new AffineTransform();
					blocks.get(i).at.translate(blocks.get(i).x, blocks.get(i).y); // hackz
					currBlock = i;
					break;
				}
			}
			else if (b.onItsSide && p2y >= 15 && p2y <= 25 && p1y >= 15 && p1y <= 25){
				if ((p1x < 0 && p2x >= -height/4) || (p1x >= -height/4 && p1x <= 0)){
					
					//System.out.println("p2 coord (" + p2x + ", " + p2y + ") p1 coord ("+ p1x + ", " + p1y + ")");
					hasBlock = true;
					blocks.get(i).x = 0;
					blocks.get(i).y = 105+height/2; 
					blocks.get(i).parent = this;
					blocks.get(i).at = new AffineTransform();
					
					double xTranslate = Math.toDegrees(b.angle) == 90 ? -width/2-40 : width/2+40;
					blocks.get(i).at.translate(xTranslate, 105-height/2-15); // hackz
					blocks.get(i).at.rotate(b.angle);
					currBlock = i;
					break;
				}
			}
		}
	}

	private double getNewBlockLoc(int currBlock, boolean angleOkay, double anglez){
		Block b = blocks.get(currBlock);
		ArrayList<Point2D> points = new ArrayList<Point2D>();
	
		if (!b.onItsSide){
			points.add( getPointInverse(new Point2D.Double(b.x-b.getWidth()/2, b.y-b.getHeight()/2-105), true));
			points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth()/2, b.y-b.getHeight()/2-105), true));
			points.add( getPointInverse(new Point2D.Double(b.x+b.getWidth()/2, b.y+b.getHeight()/2-105), true));
			points.add( getPointInverse(new Point2D.Double(b.x-b.getWidth()/2, b.y+b.getHeight()/2-105), true));
		} else {
			points.add( getPointInverse(new Point2D.Double(b.x-b.getHeight()/2, b.y-b.getWidth()/2-105), true));
			points.add( getPointInverse(new Point2D.Double(b.x+b.getHeight()/2, b.y-b.getWidth()/2-105), true));
			points.add( getPointInverse(new Point2D.Double(b.x+b.getHeight()/2, b.y+b.getWidth()/2-105), true));
			points.add( getPointInverse(new Point2D.Double(b.x-b.getHeight()/2, b.y+b.getWidth()/2-105), true));
		}
		Point2D mid = getPointInverse(new Point2D.Double(b.x,b.y),true);
		double yLoc = 999;
		if (angleOkay) {
			anglez = Math.toDegrees(anglez);

			for (int i = 0; i < blocks.size(); i++){
				if (i == currBlock)
					continue;
				Block c = blocks.get(i);
//				if (b.onItsSide)
//					System.out.println("Yes on its side");
//				else
//					System.out.println("No not on its side");
				if (!b.onItsSide && c.isInside(points.get(0).getX(), points.get(1).getX())
						|| b.onItsSide && c.isInside(points.get(0).getX(), points.get(3).getX())){
					System.out.println("y is "+ points.get(0).getY());
					double bHeight = b.onItsSide ? b.getWidth() : b.getHeight();
					double cHeight = c.onItsSide ? c.getWidth() : c.getHeight();
					double temp = c.y - cHeight/2 - bHeight/2;
					if (temp < yLoc) // search for highest block to fall on.
						yLoc = temp;
				}
			}
		} else { 
			double maxY = 0;
			int index = -1;
			for (int i = 0; i < points.size(); i++){
				double currY = points.get(i).getY();
				if (currY > maxY){
					maxY = currY;
					index = i;
				}
			}
			int indexBefore = (index-1)%3;
			int indexAfter = (index+1)%3;
			for (int i = 0; i < blocks.size(); i++){
				if (i == currBlock)
					continue;
				Block c = blocks.get(i);
				if (c.isInside(points.get(index))){

	        		System.out.println("This index "+i);
					yLoc = blocks.get(i).y;
					break;
				}
			}
		}
		
		if (yLoc != 999){
			return yLoc;
		} else if (!angleOkay){
			//b.velocity = 10;
			return mid.getY()+105-30;
		} else {
			boolean norm = (anglez == 90 || anglez == -90) ? false : true;
			double height = norm ? b.getHeight() : b.getWidth();
			return 530 - height/2;
		}
	}
	
	protected void releaseBlock(){
		Block b = blocks.get(currBlock);
		
		double width = b.onItsSide ? b.getWidth() : b.getHeight();
		Point2D p1 = getPointInverse(new Point2D.Double(b.x, b.y-105+15), true);
		Point2D p2 = getPointInverse(new Point2D.Double(b.x+width, b.y-105+15), true);
		
		//real life angle.
		double angle = Math.atan2(p2.getY()-p1.getY(), p2.getX()-p1.getX());
		boolean angleOkay = checkAngleParallelish(angle);
		if (angleOkay){
			angle = roundAngle(angle);
			System.out.println("Angle rounded to "+Math.toDegrees(angle));
		}
		if (Math.toDegrees(angle) == 90 || Math.toDegrees(angle) == -90) {
			blocks.get(currBlock).onItsSide = !blocks.get(currBlock).onItsSide;
		} 
		//System.out.println("Angle "+Math.toDegrees(angle));
		
		double ry = getNewBlockLoc(currBlock, angleOkay, angle);
		Color c = angleOkay ? new Color (255, 97, 215) : Color.gray;
		
		//System.out.print("x, y "+p1.getX()+ ", "+ry);
		
		blocks.get(currBlock).parent = null;
		blocks.get(currBlock).at = new AffineTransform();
		blocks.get(currBlock).x = (int)p1.getX();
		blocks.get(currBlock).y = (int)ry;
		blocks.get(currBlock).at.translate(p1.getX(), ry);
		blocks.get(currBlock).angle += angle;
		blocks.get(currBlock).at.rotate(blocks.get(currBlock).angle);
		blocks.get(currBlock).fillColor = c;

		hasBlock = false; 
	}

	protected void blockInteraction(){
		if (!hasBlock){
			attach();
		} 
	}
}