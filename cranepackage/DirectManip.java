/**
 * Elisa Lou 20372456.
 * Assignment 2 - Direct Manipulation.
 */
package cranepackage;

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
	private Timer t, tClear;
	private int fps = 40;
	
	private Magnet m;
	private ArrayList<Drawable> craneParts = new ArrayList<Drawable>();
	private int clickedIndex;
	private boolean dragging = false;
	private Color pink = new Color (255, 97, 215);
	
	public DirectManip(){
		super();
		ActionListener repainter = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
		t = new Timer(1000/fps, repainter);
		t.start();

		ActionListener clear = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// my way of "destroying" my blocks
				for (int i = 0; i < m.blocks.size(); i++){
					final Block b = m.blocks.get(i);
					if (b.fillColor == Color.gray){
						m.blocks.add(new Block(b.origX, b.origY, b.height, b.width, 0, null, pink));
						m.blocks.remove(i);
					}
				}
			}
		};
		tClear = new Timer(20000/fps, clear);
		tClear.start();
		
		Color purple = new Color(172, 0, 230);
		Tractor tractor = new Tractor(50, 500, 0, null, purple);
		CraneArm kevin = new CraneArm(100, -130, Math.toRadians(180), tractor, purple);
		CraneArm bob = new CraneArm(0, 120, Math.toRadians(40), kevin, purple);
		CraneArm jon = new CraneArm(0, 120, Math.toRadians(40), bob, purple);
		CraneArm dan = new CraneArm(0, 120, Math.toRadians(40), jon, purple);
		m = new Magnet(0, 125, 0, dan, purple);

		craneParts.add(tractor);
		craneParts.add(kevin);
		craneParts.add(bob);
		craneParts.add(jon);
		craneParts.add(dan);
		craneParts.add(m);		
	
		m.blocks.add(new Block(500, 520, 50, 100, 0, null, pink));
		m.blocks.add(new Block(620, 500, 100, 120, 0, null, pink));
		m.blocks.add(new Block(500, 450, 90, 60, 0, null, pink));
		m.blocks.add(new Block(600, 410, 80, 40, 0, null, pink));
		m.blocks.add(new Block(660, 390, 120, 40, 0, null, pink));
		
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){				
		        Point2D p = e.getPoint();

		        for (int i = craneParts.size() - 1; i >= 0; i--){
		        	if (craneParts.get(i).isInside(p) ){
		        		dragging = true;
		        		clickedIndex = i;
		        		break;
		        	}
		        }
			}
		    public void mouseReleased(MouseEvent e){
		        dragging = false;
		    }
		});

		this.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				if (dragging){
					Point2D current = e.getPoint();
					craneParts.get(clickedIndex).moveItem(current);
				}
			}
		});
	}
	
	public static void main(String[] args){
		DirectManip canvas = new DirectManip();
		JFrame f = new JFrame("A02 - Direct Manipulation");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setContentPane(canvas);
		f.setResizable(false);
		f.setVisible(true);
	}
	
	public void paintComponent(Graphics g){
		g.clearRect(0,0,800,600); // clear the window before redraws..
		g.setColor(new Color(46,138,92));
		g.fillRect(0, 530, 800, 50); // grass 
		g.setColor(new Color(128,159,255));
		g.fillRect(0, 0, 800, 530); // sky

		for (int i = 0; i < m.blocks.size(); i++){
			final Block b = m.blocks.get(i);
			b.paintComponent(g);
		}
		for (int i = 0; i < craneParts.size(); i++){
			final Drawable d = craneParts.get(i);
			d.paintComponent(g);
		}
		for (int i = 0; i < m.blocks.size(); i++){
			final Block b = m.blocks.get(i);
			if (b.parent != null){// paint held blocks in front of non held blocks.
				b.paintComponent(g);
				break;
			}
		}
		
		if (m.getOn()){
			m.attach();
		}
	}
}