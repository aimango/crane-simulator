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
			Point2D p1 = getPointInverse(new Point2D.Double(x, y));
			Point2D p2 = getPointInverse(new Point2D.Double(x+30, y));
			Point2D p3 = getPointInverse(new Point2D.Double(x+30, y+80));
			Point2D p4 = getPointInverse(new Point2D.Double(x, y+80));
			
			double p1x = p1.getX();
			double p1y = p1.getY();
			double p4x = p4.getX();
			double p4y = p4.getY();

			if ( (p1y >= 30 && p1y <= 40 && p4y >= 30 && p4y <= 40 && p1x >= 50 && p1x <= 150) || //CRAAAY coords checking MUAHAHAHA
					(p1x >= -10 && p1x <= 0 && p4x >= -10 && p4x <= 0 && p4y >= -70 && p4y <= 0) || // 100 = block height, 30 = magnet height.
					(p1x >= 80 && p1x <= 90 && p4x >= 80 && p4x <= 90 && p1y >= -70 && p1y <= 0)){ // same thing here.... 80 = magnet width :3
				System.out.println("ATTACH!");
				System.out.println("p1 coord (" + p1x + ", " + p1y + 
						") p4 coord ("+ p4x + ", " + p4y + ")");
				hasBlock = true;
				attachedBlockIndex = i;
//				blocks.add(new Block(30,30,10,20,Math.toDegrees(0),this,Color.red));
//				blocks.remove(i); // IT WORKS !!11312121ONE1 move it below if/else for now for easier testing..
//				break; // break so we only pick up 1 block. if 2 are closeby then only pick the first one ;D
			} else {
				System.out.println("p1 coord (" + p1x + ", " + p1y + 
						") p4 coord ("+ p4x + ", " + p4y + ")");
			}
			blocks.add(new Block(0,30,10,20,Math.toRadians(90),this,Color.red));
			blocks.remove(i); // IT WORKS !!11312121ONE1

		}
		System.out.println("YUP");
	}
	
	protected void releaseBlock(){
		blocks.add(new Block(1,1,1,1,0,null,Color.orange)); 
		// ^ fix coords later XD based on where we wanna drop it..
		// would probably need to transform inverse it again... or something.... and also check if it's parallel to
		// the ground.
		blocks.remove(attachedBlockIndex);
	}

	protected boolean isInside(Point2D p) {
		p = getPointInverse(p);
		if (p.getX() > 0 && p.getX() < 80 && p.getY() > 0 && p.getY() < 30){ // bounds check
			//check if magnet is nearby... and attach it if it is. o/w continue with life
			//would need to know about the list of blocks
			if (!hasBlock) {
				checkAttach();
			} else {
				releaseBlock();
			}
			return true;
		} else {
			return false;
		}
	}	

}