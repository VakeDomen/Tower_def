package gfx;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class FPS {
	
	private int FPS_CAP;

	public FPS(int cap) {
		this.FPS_CAP = cap;
	}
	
	public void draw(Graphics g, int fps){
		Graphics2D g2d=(Graphics2D)g;
		g2d.setColor(Color.GREEN);
		g2d.drawString("FPS: "+fps, 1100, 20);
		
		return;
	}
}