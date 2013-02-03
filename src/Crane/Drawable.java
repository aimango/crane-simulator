package Crane;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;




public class Drawable extends JComponent {
	int x, y;
	public Drawable(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void paintComponent(Graphics g) {
		g.drawRect(x,y,100,100);
	}
}