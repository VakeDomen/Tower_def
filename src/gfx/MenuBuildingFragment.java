package gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import buildings.Building;
import buildings.Cannon;

@SuppressWarnings("serial")
public class MenuBuildingFragment extends JComponent {
	private int iconSize;
	private boolean selected;
	
	Image icon;
	Building building;
	
	public MenuBuildingFragment(int x, int y,Building c, int iconSize) {
		this.iconSize = iconSize;
		this.building = c;
		this.icon = c.getIcon().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT);
		this.selected = false;
		this.setBounds(new Rectangle(x, y, iconSize, iconSize ));
		this.setPreferredSize(new Dimension( iconSize, iconSize ));
	}
	
	
	
	
	
	
	public void draw( Graphics2D g2d) {
		
		if(selected) {
			g2d.setColor(Color.YELLOW);
			g2d.drawRect(this.getX(), this.getY(), iconSize, iconSize);
			g2d.setColor(Color.BLACK);
		}
		g2d.drawImage(icon , this.getX(), this.getY(), null);
		
	}
	@Override
	public boolean contains(Point p) {
		if(p.x > this.getX() && p.x <= this.getX()+ iconSize && p.y >= this.getY() && p.y < this.getY()+ iconSize ) return true;
		else return false;
	}
	
	public void setClicked(boolean clicked) { this.selected=clicked; System.out.println("selected!"); }
	public Building getBuilding() { return this.building; }






	public void setSelected(boolean b) { this.selected = b; }

}
