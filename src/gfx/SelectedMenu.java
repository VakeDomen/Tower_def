package gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import buildings.Building;
import buildings.Cannon;

@SuppressWarnings("serial")
public class SelectedMenu extends JComponent {
	private int x, y, width, height;
	private static final int MARGIN = 30;
	
	
	private Building selected = null;
	private boolean shopBuilding = false;
	
	public SelectedMenu(int x , int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	
	
	
	
	
	public void draw(Graphics2D g2d) {
		displayBuildingStats(g2d);
	}
	private void displayBuildingStats(Graphics2D g2d) {
		g2d.setColor(new Color(15, 150, 190));
		
		if(selected != null) {
			
			if(shopBuilding) displayShopBuilding(g2d);
			else displayBuilding(g2d);
			
			
		}
		g2d.setColor(Color.BLACK);
		
		
	}
	
	
	private void displayBuilding(Graphics2D g2d) {
		
		g2d.drawImage(selected.getIcon(), x, y, null);
		
	}



	private void displayShopBuilding(Graphics2D g2d) {
		int x1 = x+MARGIN*4;
		int y1 = y+MARGIN*2;
		g2d.drawImage(selected.getIcon(), x1, y1, null);
		x1 += 0;
		y1 += (selected.getIcon().getHeight(null) + MARGIN*2);
		g2d.drawString("COST:  " + selected.getCost(), x1, y1);
		y1 += MARGIN;
		g2d.drawString("HP:  " + selected.getHP(), x1, y1);
		y1 += MARGIN;	
		if(selected instanceof Cannon) {
			g2d.drawString("RANGE:  " + ((Cannon) selected).getRange(), x1, y1);
			y1 += MARGIN;	
			g2d.drawString("DMG:  " + ((Cannon) selected).getDmg(), x1, y1);
			y1 += MARGIN;
			g2d.drawString("COOLDOWN:  " + ((Cannon) selected).getCooldown(), x1, y1);
			y1 += MARGIN;
			
		}
		x1 = width/2 + MARGIN;
		y1 = y+MARGIN*2;
		int lineLength = 13;
		
		String[] disc = selected.getDiscription().split(" ");
		
		int count = 0;
		String line = "";
		for(String s : disc) {
			line += (s+" ");
			count += s.length();
			if(count > lineLength || s == disc[disc.length-1]) {
				g2d.drawString(line, x1, y1);
				y1+=MARGIN;
				count = 0;
				line = "";
			}
			
		}
		
	}







	public void setSelected(Building building) { this.selected = building; }
	public void setShopBuilding(boolean b) { shopBuilding = b; }
}
