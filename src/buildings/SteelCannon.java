package buildings;

public class SteelCannon extends Cannon{
	private static int cost = 200;
	private static int dmg = 50;
	private static int lvl = 1;
	private static int key = 3;
	private static int range = 250;
	private static int cooldown = 30;
	private static int HP = 300;
	private static String top = "cannons/cannon2top.png";
	private static String bottom = "cannons/cannon2bottom.png";
	private static String icon = "cannons/cannon2icon.png";
	private static String discription = "This is a steel cannon. It's a strong single target tower, with medium range and medium cooldown.";
	
	
	public SteelCannon(int x, int y, int fragmentSize) {
		super(cost, dmg, lvl, HP, top, bottom, icon, x, y, key, range, cooldown, discription);
	}
	public SteelCannon() {
		super(cost, dmg, lvl, HP, top, bottom, icon, key, range, cooldown, discription);
	}
}
