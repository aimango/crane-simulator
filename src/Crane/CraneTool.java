/**
 * Elisa Lou 20372456.
 * Assignment 2 - Direct Manipulation.
 */
package Crane;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

//http://stackoverflow.com/questions/4145609/rotate-rectangle-in-java
//http://www.java-forums.org/awt-swing/19817-java-2d-graphics-drag-drop.html
public class CraneTool extends JPanel {
     //Rectangle2D rect = new Rectangle(100, 100, 150, 75);
    static Point currentPoints[] = new Point[4];
    static Point updated[] = new Point[4];
    Polygon poly;
    private double angle = 0;
    boolean flag = false; 
	static int startX = 50;
	static int startY = 210;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        //g2.drawRect(100,100,50,50);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.blue);
        
        poly = polygonize(currentPoints);
        rotatePointMatrix(currentPoints, angle);
    	System.out.print("\nTRALALA" + Arrays.toString(currentPoints));

		g2.setStroke(new BasicStroke(5));
        g2.draw(poly);
        g2.setPaint(Color.CYAN);
        g2.fillPolygon(poly);
        
        BaseCrane canvas = new BaseCrane();
        canvas.paintComponent(g2);
        //g2.translate(x, y);
        //g2.draw(path);
        g2.dispose();
        
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
        repaint();
    }

	protected static void initPoints(){

	    currentPoints[0]= new Point(startX+100, startY+100);
	    currentPoints[1]= new Point(startX+250, startY+100);
	    currentPoints[2]= new Point(startX+250, startY+130);
	    currentPoints[3]= new Point(startX+100, startY+130);
	}

    protected Point[] getOriginalPoints(){
        return currentPoints;
    }
//
//    protected void setOriginalPoints(Point p[]) {
//    	for (int i = 0; i < 4; i++) {
//    		currentPoints[i] = p[i];
//    	}
//    }
    
    public void rotatePointMatrix(Point[] origPoints, double angle){

    	if (false) {
	        AffineTransform.getRotateInstance(angle, startX+100, startY+115)
	                .transform(origPoints,0,currentPoints,0,4);
	        flag = true;
    	}
    	else {
			AffineTransform at = new AffineTransform();
			at.rotate(angle, startX+100, startY+115);
			PathIterator pi = poly.getPathIterator(at);
			
			Path2D path = new Path2D.Float();
			path.append(pi, true);
			
			PathIterator i = poly.getPathIterator(at); // i actually need to declare a new 1.
			poly = new Polygon(); 

			int k = 0;
			while (!i.isDone()) {
			    double[] xy = new double[2];
			    i.currentSegment(xy);
			    if (xy[0] != 0 && xy[1] != 0) {
			    	poly.addPoint((int) xy[0], (int) xy[1]);
			    	updated[k] = new Point((int)xy[0], (int)xy[1]);
			    	k++;
			    }
			    System.out.println(Arrays.toString(xy));
			    System.out.println("SHOULDBESAME"+Arrays.toString(updated));
			    i.next();
			}
			
			//System.out.print(poly.xpoints[0] + " " + poly.ypoints[0]);
    	}
    }

    public Polygon polygonize(Point[] polyPoints){
        //a simple method that makes a new polygon out of the rotated points
        Polygon tempPoly = new Polygon();

         for(int  i=0; i < polyPoints.length; i++){
             tempPoly.addPoint(polyPoints[i].x, polyPoints[i].y);
        }

        return tempPoly;

    }
    
    public static void main(String[] args) {
        CraneTool test = new CraneTool();
        new CraneController(test);
        JFrame f = new JFrame("A02 - Direct Manipulation");

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);
        f.setSize(800,600);
        f.setLocation(100,100);
        f.setVisible(true);
        initPoints();
	}
}
 
class CraneController extends MouseInputAdapter {
    CraneTool component;
    Point mouseLoc = new Point();
    boolean dragging = false;
 
    public CraneController(CraneTool tool) {
        component = tool;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }
 
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        Polygon poly = component.poly;
        if(poly.contains(p)) {
        	mouseLoc = e.getPoint();
            dragging = true;
        }
        System.out.println("Mouse pressed at "+p.x+", "+p.y);
    }
 
    public void mouseReleased(MouseEvent e) {
    	
        System.out.println("Mouse released at " + mouseLoc.x + ", " + mouseLoc.y);
        component.currentPoints = component.updated; // only updated when not dragging.
        dragging = false;
    }
 
    public void mouseDragged(MouseEvent e) {
        if(dragging) {
        	System.out.println(e.getX()+ ", " + e.getY() + ") (" + mouseLoc.x + ", " + mouseLoc.y);
//        	double numerator = e.getX() * offset.x + e.getY() * offset.y;
//        	double temp = Math.sqrt(Math.abs(e.getX()*e.getX() + offset.x*offset.x));
//        	double temp2 = Math.sqrt(Math.abs(e.getY()*e.getY() + offset.y*offset.y));
//        	double denominator = temp * temp2;
//            double angle = Math.acos(numerator/denominator); // this is in radians
//            System.out.println(temp + " " + temp2);
//            System.out.println(numerator+" "+denominator+ " " + Math.toDegrees(angle));
//            cos-1((P122 + P132 - P232)/(2 * P12 * P13))
        	//sqrt((P1x - P2x)2 + (P1y - P2y)2)
        	
//        	double p12 = Math.sqrt((100-offset.x)*(100-offset.x) + (100-offset.y)*(100-offset.y));
//        	double p13 = Math.sqrt((100-e.getX())*(100-e.getX()) + (100-e.getY())*(100-e.getY()));
//        	double p23 = Math.sqrt((e.getX()-offset.x)*(e.getX()-offset.x) + (e.getY()-offset.y)*(e.getY()-offset.y));
//            double angle = Math.acos((p12*p12 + p13*p13 - p23*p23)/(2*p12*p13));
//            System.out.println(p12 + " "+ p13 + " " + p23 + " " + Math.toDegrees(angle));
//            
        	double slope1 = (double)(mouseLoc.y-component.startY-115)/(mouseLoc.x-component.startX-100);
            double slope2 = (double)(e.getY()-component.startY-115)/(e.getX()-component.startX-100);
            double angle = Math.atan((slope1 - slope2) / (1 + (slope1 * slope2)));
            System.out.println("Angle " + Math.toDegrees(angle));
            
            component.setAngle(-1*angle); // fix this..

        }
    }
}