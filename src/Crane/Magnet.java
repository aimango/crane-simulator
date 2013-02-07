package Crane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;


// right now ONLY allow blocks to be picked up on the long end.. assuming right now that they all sit on their short end..
// also only allows blocks to be picked up on the LHS rite now..
public class Magnet extends Drawable {

	private static final long serialVersionUID = 1L;  // get rid of warning
	private Rectangle rect;
	public ArrayList<Block> blocks = new ArrayList<Block>();
	private boolean hasBlock = false;
	private int attachedBlockIndex = -1;
	
	public Magnet(int x, int y, double angle, Drawable parent, Color fill){
		super(x,y,angle,parent,fill);
		rect = new Rectangle(0,0,80,30);
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
			Point2D p1 = getPointInverse(new Point2D.Double(x, y));
			Point2D p2 = getPointInverse(new Point2D.Double(x+width, y));
			Point2D p3 = getPointInverse(new Point2D.Double(x+width, y+height));
			Point2D p4 = getPointInverse(new Point2D.Double(x, y+height));
			
			double p1x = p1.getX();
			double p1y = p1.getY();
			double p2x = p2.getX();
			double p2y = p2.getY();
			double p3x = p3.getX();
			double p3y = p3.getY();
			double p4x = p4.getX();
			double p4y = p4.getY();

			//cray coord checking
			if (p1y >= 30 && p1y <= 40 && p4y >= 30 && p4y <= 40 && p1x >= height/2 && p1x <= 80+height/2 ||
				p4y >= 30 && p4y <= 40 && p3y >= 30 && p3y <= 40 && p4x >= width/2 && p4x <= 80+width/2 ||
				p3y >= 30 && p3y <= 40 && p2y >= 30 && p2y <= 40 && p3x >= height/2 && p3x <= 80+height/2 ||
				p2y >= 30 && p2y <= 40 && p1y >= 30 && p1y <= 40 && p2x >= width/2 && p1x <= 80+width/2){ 
				
				System.out.println("attach\np1 coord (" + p1x + ", " + p1y + 
						") p4 coord ("+ p4x + ", " + p4y + ")");
				hasBlock = true;
				attachedBlockIndex = i;
				blocks.add(new Block(0,30,width,height,Math.toRadians(90),this,Color.red));
				blocks.remove(i); // IT WORKS !!11312121ONE1

				break; // break so we only pick up 1 block. if 2 are closeby then only pick the first one ;D
			} else {
				System.out.println("clicked but nothing to pick up");
				System.out.println("p1 coord (" + p1x + ", " + p1y + ") p2 coord (" + p2x + ", " + p2y + 
						") p3 coord ("+ p3x + ", " + p3y + ") p4 coord ("+ p4x + ", " + p4y + ")");
			}
		}
	}
	
	protected void releaseBlock(){
		Point2D ground = getPointInverse (new Point2D.Double(0, 550));
		ground = getPointInverse(ground);
		System.out.println(ground.getX() + " " + ground.getY());
		//blocks.get(attachedBlockIndex).moveItem(ground);
//		Block b = blocks.get(attachedBlockIndex);
//		Point2D p = getPointInverse(new Point2D.Double(b.getX(), b.getY()));
//		System.out.print(p.getX() + " " + p.getY());
//		blocks.add(new Block((int)p.getX(),(int)p.getY(),1,1,0,null,Color.orange)); 
//		// ^ fix coords later XD based on where we wanna drop it..
//		// would probably need to transform inverse it again... or something.... and also check if it's parallel to
//		// the ground.
		blocks.remove(blocks.size()-1);
		//attachedBlockIndex = -1;
		hasBlock = false; 
	}

	protected boolean isInside(Point2D p) {
		p = getPointInverse(p);
		if (p.getX() > 0 && p.getX() < 80 && p.getY() > 0 && p.getY() < 30){ // bounds check
			//check if magnet is nearby... and attach it if it is. o/w continue with life
			//would need to know about the list of blocks
			if (hasBlock) {
				System.out.println("HAS BLOCK. RELEASE");
				releaseBlock();
			} else {
				System.out.println("DOESNT HAVE BLOCK");
				checkAttach();
			} 
			return true;
		} else {
			return false;
		}
	}	

}