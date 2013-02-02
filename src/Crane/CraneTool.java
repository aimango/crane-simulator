package Crane;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;
import java.util.Timer;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
 
//http://www.java-forums.org/awt-swing/19817-java-2d-graphics-drag-drop.html
public class CraneTool extends JPanel {
    Rectangle rect = new Rectangle(100, 100, 150, 75);
    static Point currentPoints[] = new Point[4];

//    int xpoints[] = {100, 250, 250, 100, 100, 0 };
//	int ypoints[] = {100, 100, 175, 175, 100, 0 };
    Polygon poly;
    private double angle = 0;

	
	 Boolean flag = false; 
    // time loop is set to run at fixed rate of 50 ms

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.blue);
        
        
        Point newPoints[] = getOriginalPoints();
        poly = polygonize(currentPoints);
        rotatePointMatrix(getOriginalPoints(), angle, currentPoints);
        
        g2.draw(poly);
        		
        //g2.translate(x, y);
        //g2.draw(path);
        g2.dispose();
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
        repaint();

    }

	protected static void initPoints(){
	    currentPoints[0]= new Point(100, 100);
	    currentPoints[1]= new Point(250, 100);
	    currentPoints[2]= new Point(250, 175);
	    currentPoints[3]= new Point(100, 175);
		
	}
    protected Point[] getOriginalPoints(){
        return currentPoints;
    }

    protected void setOriginalPoints(Point p[]) {
    	for (int i = 0; i < 4; i++) {
    		currentPoints[i] = p[i];
    	}
    }
    
    public void rotatePointMatrix(Point[] origPoints, double angle, Point[] storeTo){

        /* We ge the original points of the polygon we wish to rotate
         *  and rotate them with affine transform to the given angle. 
         *  After the opeariont is complete the points are stored to the 
         *  array given to the method.
        */
    	if (!flag) {
	        AffineTransform.getRotateInstance(angle, 100, 137)
	                .transform(origPoints,0,storeTo,0,4);
	        flag = true;
    	}
    	else {
			AffineTransform at = new AffineTransform();
			at.rotate(angle, 100, 137); // doesnt get painted... need to use poly completely .
			PathIterator pi = poly.getPathIterator(at);
			
			Path2D path = new Path2D.Float();
			path.append(pi, true);
			
			
			PathIterator i = poly.getPathIterator(at);
			poly = new Polygon(); 
			Point updated[] = new Point[4];
			int k = 0;
			while (!i.isDone()) {
			    double[] xy = new double[2];
			    i.currentSegment(xy);
			    if (xy[0] != 0 && xy[1] != 0) {
			    	poly.addPoint((int) xy[0], (int) xy[1]);
			    	updated[k] = new Point((int)xy[0], (int)xy[1]);
			    }
			    System.out.print(Arrays.toString(xy));
			    i.next();
			}
			//System.out.print(poly.xpoints[0] + " " + poly.ypoints[0]);
			//rect.x = poly.xpoints[0];
			//rect.y = poly.ypoints[0];
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
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);
        f.setSize(400,400);
        f.setLocation(100,100);
        f.setVisible(true);
        initPoints();


    }
}
 
class CraneController extends MouseInputAdapter {
    CraneTool component;
    Point offset = new Point();
    boolean dragging = false;
 
    public CraneController(CraneTool gdad) {
        component = gdad;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }
 
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        Polygon poly = component.poly;
       // Rectangle r = component.rect;
        if(poly.contains(p)) {
        	offset = e.getPoint();
            //offset.x = p.x;
            //offset.y = p.y;
            dragging = true;
        }
        System.out.println("Mouse pressed at "+p.x+", "+p.y);
    }
 
    public void mouseReleased(MouseEvent e) {
    	
        System.out.println("Mouse released at "+offset.x+", "+offset.y);
        dragging = false;
    }
 
    public void mouseDragged(MouseEvent e) {
        if(dragging) {
//            int x = e.getX() - offset.x;
//            int y = e.getY() - offset.y;
            
        	System.out.println(e.getX()+ ", " + e.getY() + ") (" + offset.x + ", " + offset.y);
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
        	double slope1 = (double)(offset.y-100)/(offset.x-100);
            double slope2 = (double)(e.getY()-100)/(e.getX()-100);
            double angle = Math.atan((slope1 - slope2) / (1 + (slope1 * slope2)));
            System.out.println(slope1 + " " + slope2 + " " + Math.toDegrees(angle));

            component.setAngle(-1*angle);

        }
    }
}