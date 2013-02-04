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

public class Game extends JPanel {
	private static final long serialVersionUID = 1L; // get rid of warning
	private ArrayList<Drawable> craneArms = new ArrayList<Drawable>();
	private Timer t;
	private int fps = 40;
	private int clickedIndex;
	private boolean dragging = false;
	
	public Game(){
		super();
		ActionListener repainter = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};

		t = new Timer(1000/fps, repainter);
		t.start();

		Tractor tractor = new Tractor(50,500, 0, null, Color.BLACK);
		CraneArm kevin = new CraneArm(100, -200, Math.toRadians(180), tractor, Color.orange);
		CraneArm bob = new CraneArm(0, 120, Math.toRadians(40), kevin, Color.red);
		CraneArm jon = new CraneArm(0, 120, Math.toRadians(40), bob, Color.yellow);
		CraneArm dan = new CraneArm(0, 120, Math.toRadians(40), jon, Color.pink);
		craneArms.add(tractor);
		craneArms.add(kevin);
		craneArms.add(bob);
		craneArms.add(jon);
		craneArms.add(dan);
		
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {				
		        Point2D p = e.getPoint();

		        for (int i = craneArms.size() - 1; i >= 0; i--) {
		        	if (craneArms.get(i).isInside(p) ) {
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
					//System.out.println("Drag");
					Point2D current = e.getPoint();
					craneArms.get(clickedIndex).moveItem(current);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		Game canvas = new Game();
		JFrame f = new JFrame("Crane");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setContentPane(canvas);
		f.setResizable(false);
		f.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		g.clearRect(0,0,800,600); // lol
		for (int i = 0; i < craneArms.size(); i++){
			final Drawable d = craneArms.get(i);
			d.paintComponent(g);
		}
	}
}