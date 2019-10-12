package units;

public class Warrior extends Unit{

	private static int HP = 60;
	private static int reward = 20;
	private static int dmg = 10;
	private static int range = 30;
	private static boolean agressive = true;
	private static int cooldown = 15;
	private static double spawnOdds = 0.1;
	private static double speed = 4;
	private static boolean flying = false;
	
	private static String icon = "units/enemy4";
	
	
	
	

	public Warrior(float x, float y, int lvl ) {
		super(x, y, icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	public Warrior(int lvl) {
		super(icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	
	
}
