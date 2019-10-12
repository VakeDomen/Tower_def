package gfx;

import java.awt.Font;
import java.awt.Graphics2D;

import buildings.Building;

public class StatDisplay {
	private static final int MARGIN = 100;
	
	private int offsetV;
	
	
	
	
	
	public StatDisplay(int offsetV) {
		this.offsetV = offsetV;
	}
	
	
	
	public void draw(Graphics2D g2d, int gold, int health) {
		String string = "GOLD: " + gold;
		g2d.drawString(string, MARGIN, MARGIN+offsetV);
		string = "HEALTH: " + health;
		g2d.drawString(string, MARGIN, MARGIN+offsetV*2);
	}
	
	
	
	
	
}
