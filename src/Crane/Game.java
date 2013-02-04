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
	private ArrayList<CraneArm> craneArms = new ArrayList<CraneArm>();
	private Tractor tractor;
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

		CraneArm kevin = new CraneArm(300, 300, Math.toRadians(180), null, Color.cyan);
		CraneArm bob = new CraneArm(0,60, Math.toRadians(40), kevin, Color.blue);
		CraneArm jon = new CraneArm (0,60, Math.toRadians(40), bob, Color.gray);
		CraneArm dan = new CraneArm (0, 60, Math.toRadians(40), jon, Color.pink);
		craneArms.add(kevin);
		craneArms.add(bob);
		craneArms.add(jon);
		craneArms.add(dan);
		
		tractor = new Tractor(400,400, 0, null, Color.BLACK);
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
					craneArms.get(clickedIndex).moveArm(current);
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
			final CraneArm d = craneArms.get(i);
			d.paintComponent(g);
		}
		tractor.paintComponent(g);
	}
}