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
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

//http://stackoverflow.com/questions/4145609/rotate-rectangle-in-java
//http://www.java-forums.org/awt-swing/19817-java-2d-graphics-drag-drop.html
public class CraneTool extends JPanel {
	// get rid of warning
	private static final long serialVersionUID = 1L;
	
	//Rectangle2D rect = new Rectangle(100, 100, 150, 75);
    static Point currentPoints[] = new Point[4];
    static Point updated[] = new Point[4];
    static Polygon poly;
    private double angle = 30;
    boolean flag = false; 
	static int startX ;
	static int startY ;

	public CraneTool(int startX, int startY) {
		this.startX = startX;
		this.startY = startY;
	}
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        //g2.drawRect(100,100,50,50);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.blue);
        
        
        rotatePointMatrix(currentPoints, angle);
    	//System.out.print("\nTRALALA" + Arrays.toString(currentPoints));

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
    private int round(double d){
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result<0.5){
            return d<0 ? -i : i;            
        }else{
            return d<0 ? -(i+1) : i+1;          
        }
    }
    
    protected void rotatePointMatrix(Point[] origPoints, double angle){

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
			
			PathIterator i = poly.getPathIterator(at); // I actually need to declare a new iterator.
			Polygon polyNew = new Polygon(); 

			int k = 0;
			while (!i.isDone()) {
			    double[] xy = new double[2];
			    i.currentSegment(xy);
			    if (xy[0] != 0 && xy[1] != 0) {
			    	polyNew.addPoint( round(xy[0]), round(xy[1]));
			    	updated[k] = new Point(round(xy[0]), round(xy[1]));
			    	k++;
			    }
			    //System.out.println(Arrays.toString(xy));
			    //System.out.println("SHOULDBESAME"+Arrays.toString(updated));
			    i.next();
			}
//			if (polyNew.intersects(50, 350, 200, 50) || polyNew.intersects(50,150,80,300)){
//				
//			}
//			else 
				poly = polyNew;
			//currentPoints=updated;
			//System.out.print(poly.xpoints[0] + " " + poly.ypoints[0]);
    	}
    }

    private static Polygon polygonize(Point[] polyPoints){
        Polygon tempPoly = new Polygon();
        for (int i = 0; i < polyPoints.length; i++){
             tempPoly.addPoint(polyPoints[i].x, polyPoints[i].y);
        }
        return tempPoly;
    }
    
    public static void main(String[] args) {
        CraneTool test1 = new CraneTool(50,210);
        new CraneController(test1);
        
        CraneTool test2 = new CraneTool(100,210);
        new CraneController(test2);
        JFrame f = new JFrame("A02 - Direct Manipulation");

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test1);
        f.add(test2);
        f.setSize(800,600);
        f.setLocation(100,100);
        f.setVisible(true);
        initPoints();
        poly = polygonize(currentPoints);
	}
}
 
class CraneController extends MouseInputAdapter {
    CraneTool component;
    Point currLoc = new Point();
    Point startLoc = new Point();
    boolean dragging = false;
    
    double startAngle = 0, currentAngle = 0;

    public CraneController(CraneTool tool) {
        component = tool;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }
 
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        Polygon poly = component.poly;
        if(poly.contains(p)) {
        	startLoc = e.getPoint();
            dragging = true;
        }
        System.out.println("Mouse pressed at "+p.x+", "+p.y);
    }
 
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released at " + e.getPoint().x + ", " + e.getPoint().y);
        //component.currentPoints = component.updated; // only updated when not dragging.
        dragging = false;
    }
 
    //http://stackoverflow.com/questions/2198303/java-2d-drag-mouse-to-rotate-image-smoothly/2198368#2198368
    public double getAngle(Point origin, Point other){ 
    	double dy = other.y - origin.y;
    	double dx = other.x - origin.x;
    	double angle = 0;
    	double PI = Math.PI;
    	if (dx == 0) {// edge case
    		angle = dy>=0 ? PI/2 : -PI/2;
    	} else {
    		angle = Math.atan(dy/dx);
    		if (dx < 0) { // put it in the right place
    			angle += PI;
    		}
    	}
    	if (angle < 0)
    		angle += 2*PI;
    	return angle;
    }

//    	double vectorA[] = {e.getX()-originX, e.getY()-originY};
//    	double vectorB[] = {mouseLoc.x-originX, mouseLoc.y-originY};
//    	
//    	System.out.println(e.getX()+ ", " + e.getY() + ") (" + mouseLoc.x + ", " + mouseLoc.y);
//    	double numerator = vectorA[0]*vectorB[0] + vectorA[1]*vectorB[1];
//    	double temp = Math.sqrt(Math.abs(vectorA[0]*vectorA[0] + vectorA[1]*vectorA[1]));
//    	double temp2 = Math.sqrt(Math.abs(vectorB[0]*vectorB[0] + vectorB[1]*vectorB[1]));
//    	double denominator = temp * temp2;
//        double angle = Math.acos(numerator/denominator); // this is in ?
//        boolean negative = mouseLoc.y-e.getY() < 0;
//        
//        System.out.println(Arrays.toString(vectorA) + " " + Arrays.toString(vectorB));
//        System.out.println("Temps "+temp + " " + temp2);
//        System.out.println(numerator+" "+denominator+ " ANGLE " + Math.toDegrees(angle));
//        
        //cos-1((P122 + P132 - P232)/(2 * P12 * P13))
    	//sqrt((P1x - P2x)2 + (P1y - P2y)2)
    	
//    	double p12 = Math.sqrt((originX-mouseLoc.x)*(originX-mouseLoc.x) + (originY-mouseLoc.y)*(originY-mouseLoc.y));
//    	double p13 = Math.sqrt((originX-e.getX())*(originX-e.getX()) + (originY-e.getY())*(originY-e.getY()));
//    	double p23 = Math.sqrt((e.getX()-mouseLoc.x)*(e.getX()-mouseLoc.x) + (e.getY()-mouseLoc.y)*(e.getY()-mouseLoc.y));
//        double angle = Math.acos((p12*p12 + p13*p13 - p23*p23)/(2*p12*p13));
//        System.out.println(p12 + " "+ p13 + " " + p23 + " " + Math.toDegrees(angle));
        
//    	double slope1 = (double)(mouseLoc.y-component.startY-originY)/(mouseLoc.x-component.startX-originX);
//        double slope2 = (double)(e.getY()-component.startY-originY)/(e.getX()-component.startX-originX);
//        double angle = Math.atan((slope1 - slope2) / (1 + (slope1 * slope2)));
//        System.out.println("Angle " + Math.toDegrees(angle));

    
    public void mouseDragged(MouseEvent e) {
    	currLoc = e.getPoint();
        if(dragging && currLoc != startLoc) {
        	int originX = component.startX+100;
        	int originY = component.startY+115;
        	Point origin = new Point(originX, originY);
        	
        	
        	double startAngle = getAngle(origin, startLoc);
        	double currAngle = getAngle(origin, currLoc);
        	System.out.println("THE START ANGLE IS " + Math.toDegrees(startAngle));
        	System.out.print("THE CURR ANGLE IS " + Math.toDegrees(currAngle));
        	component.setAngle(currAngle - startAngle);
        }
    }
}