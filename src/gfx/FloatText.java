package gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class FloatText {

	private int x, y;
	private String text;
	private boolean bad;
	
	private int counter = 0;
	private int timer = 60;
	
	public FloatText(int x, int y, String text, boolean bad) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.bad = bad;
	}
	
	
	public boolean draw(Graphics2D g2d) {
		counter++;
		Font backup = g2d.getFont();
		Font font = new Font("TimesRoman", Font.BOLD, 22);
		g2d.setFont(font);
		if(bad)	g2d.setColor(Color.RED);
		else g2d.setColor(Color.GREEN);
		if(counter>=timer) {
			g2d.drawString(text, x, (int)Math.round(y-counter/2));
			g2d.setColor(Color.BLACK);
			g2d.setFont(backup);
			return true;
		}else {
			g2d.drawString(text, x, (int)Math.round(y-counter/2));
			g2d.setColor(Color.BLACK);
			g2d.setFont(backup);
			return false;
		}
	}
	
	
}
