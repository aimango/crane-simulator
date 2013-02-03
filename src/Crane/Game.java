package Crane;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Game extends JPanel {
	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	private Timer t;
	private int fps = 40;

	public Game(){
		super();
		ActionListener repainter = new ActionListener(){
			public void actionPerformed(ActionEvent e){
//				cur_angle += (radians_per_second/((double)fps));
//				x = (int)(radius * Math.cos(cur_angle));
//				y = (int)(radius * Math.sin(cur_angle));
				repaint();
			}
		};
		t = new Timer(1000/fps, repainter);
		t.start();
		
		//for (int i = 0; i < 5; i++)
			drawables.add(new Drawable(100,100));
			drawables.add(new Drawable(300,100));
	}
	
	public static void main(String[] args) {
		Game canvas = new Game();
		JFrame f = new JFrame("Crane");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setContentPane(canvas);
		f.setVisible(true);
		
		
	}
	
	public void paintComponent(Graphics g) {
		for (int i = 0; i < drawables.size(); i++){
			drawables.get(i).paintComponent(g);
		}
	}
	
}