package buildings;

public class SimpleCannon extends Cannon {
	private static int cost = 50;
	private static int dmg = 10;
	private static int lvl = 1;
	private static int key = 2;
	private static int range = 200;
	private static int cooldown = 20;
	private static int HP = 100;
	private static String top = "cannons/cannon1top.png";
	private static String bottom = "cannons/cannon1bottom.png";
	private static String icon = "cannons/cannon1icon.png";
	private static String discription = "This is a simple wooden cannon. It's a cheap cannon, with medium range and medium-weak single target attack damage. At least it doesn't shoot slowly.";
	
	
	public SimpleCannon(int x, int y, int fragmentSize) {
		super(cost, dmg, lvl, HP, top, bottom, icon, x, y, key, range, cooldown, discription);
	}
	public SimpleCannon() {
		super(cost, dmg, lvl, HP, top, bottom, icon, key, range, cooldown, discription);
	}

}
