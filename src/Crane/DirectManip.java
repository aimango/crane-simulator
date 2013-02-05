/**
 * Elisa Lou 20372456.
 * Assignment 2 - Direct Manipulation.
 * Took some example code from RotateTriangle
 */
package Crane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DirectManip extends JPanel {
	private static final long serialVersionUID = 1L; // get rid of warning
	
	private ArrayList<Drawable> craneParts = new ArrayList<Drawable>();
	private Timer t;
	private Magnet m;
	private int fps = 40;
	private int clickedIndex;
	private boolean dragging = false;

	public DirectManip() {
		super();
		ActionListener repainter = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};

		t = new Timer(1000/fps, repainter);
		t.start();

		Color c = new Color(153, 102, 204);
		Tractor tractor = new Tractor(50, 500, 0, null, c);
		CraneArm kevin = new CraneArm(95, -130, Math.toRadians(180), tractor, c);
		CraneArm bob = new CraneArm(0, 120, Math.toRadians(40), kevin, c);
		CraneArm jon = new CraneArm(0, 120, Math.toRadians(40), bob, c);
		CraneArm dan = new CraneArm(0, 120, Math.toRadians(40), jon, c);
		m = new Magnet(-40, 125, 0, dan, c);

		craneParts.add(tractor);
		craneParts.add(kevin);
		craneParts.add(bob);
		craneParts.add(jon);
		craneParts.add(dan);
		craneParts.add(m);		
		
		c = new Color (184, 61, 122);
		m.blocks.add(new Block(500, 300, 10, 20, 0, null, c));
//		m.blocks.add(new Block(500, 500, 10, 20, 0, null, c));
//		m.blocks.add(new Block(500, 400, 10, 20, 0, null, c));
//		m.blocks.add(new Block(600, 300, 10, 20, 0, null, c));
//		m.blocks.add(new Block(600, 400, 10, 20, 0, null, c));
		
//		Random r = new Random();
//		for (int i = 0; i < 6; i++) {
//			int x = (r.nextInt(10)+1)*30; // 1 to 10
//			int width = (r.nextInt(10)+1)*5;
//			int height = (r.nextInt(10)+1)*5;
//			Block b = new Block(500+x,0,width,height,0,null,c);
//		}
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {				
		        Point2D p = e.getPoint();

		        for (int i = craneParts.size() - 1; i >= 0; i--) {
		        	if (craneParts.get(i).isInside(p) ) {
		        		dragging = true;
		        		System.out.println(i);
		        		clickedIndex = i;
		        		break;
		        	}
		        }
		        System.out.println("Mouse pressed at " + p.getX() +", " + p.getY());
			}

		    public void mouseReleased(MouseEvent e) {
		        System.out.println("Mouse released at " + e.getPoint().x + ", " + e.getPoint().y);
		        dragging = false;
		    }
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (dragging) {
					Point2D current = e.getPoint();
					craneParts.get(clickedIndex).moveItem(current);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		DirectManip canvas = new DirectManip();
		JFrame f = new JFrame("A02 - Direct Manipulation");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setContentPane(canvas);
		f.setResizable(false);
		f.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		g.clearRect(0,0,800,600); // clear the window before redraws..
		g.setColor(new Color(46,138,92));
		g.fillRect(0, 530, 800, 50);
		g.setColor(new Color(102,153,204));
		g.fillRect(0, 0, 800, 530);
		for (int i = 0; i < craneParts.size(); i++){
			final Drawable d = craneParts.get(i);
			d.paintComponent(g);
		}
		for (int i = 0; i < m.blocks.size(); i++){ // : (
			final Block b = m.blocks.get(i);
			b.paintComponent(g);
		}
	}
}