package buildings;

import java.awt.Image;

import ResourceLoader.ResourceLoader;

public class Wall extends Building{
	private static int cost = 5;
	private static int lvl = 1;
	private static int HP = 500;
	private static int key = 11;
	
	private static String iconPath = "wall.png";
	private static String discription = "This is a wall. You can build this bulding by draging it on the board. It provides a cheap defense tool, to protect your cannons or carve paths for the units to walk between.";
	
	private Image icon;
	
	
	
	
	
	public Wall(int x, int y) {
		super(cost, lvl, HP, x, y, key, discription);
		this.icon = ResourceLoader.getImage(iconPath).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
	}
	public Wall() {
		super(cost, lvl, HP, key, discription);
		this.icon = ResourceLoader.getImage(iconPath).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
	}



	@Override
	public Image getIcon() { return icon; }
	
	
}


