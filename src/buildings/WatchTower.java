package buildings;

import java.awt.Image;

import ResourceLoader.ResourceLoader;
import gfx.GameMap;

public class WatchTower extends SupportBuilding{
	private static int cost = 1000;
	private static int lvl = 1;
	private static int HP = 300;
	private static int key = 5;
	
	private static String iconPath = "watchTower.png";
	private static String discription = "This is a watch tower.";
	
	private Image icon;
	
	
	
	
	
	public WatchTower(int x, int y) {
		super(cost, lvl, HP, x, y, key, discription);
		this.icon = ResourceLoader.getImage(iconPath).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
	}
	public WatchTower() {
		super(cost, lvl, HP, key, discription);
		this.icon = ResourceLoader.getImage(iconPath).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
	}



	@Override
	public Image getIcon() { return icon; }
	@Override
	public void onCreate(GameMap gameMap) {
		
	}
}
