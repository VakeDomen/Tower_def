package units;



public class Pesant extends Unit {
	private static int HP = 20;
	private static int reward = 10;
	private static int dmg = 10;
	private static int range = 30;
	private static boolean agressive = false;
	private static int cooldown = 10;
	private static double spawnOdds = 0.7;
	private static double speed = 3;
	private static boolean flying = false;
	
	private static String icon = "units/enemy1";
	
	
	
	

	public Pesant(float x, float y , int lvl) {
		super(x, y, icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	public Pesant(int lvl) {
		super(icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	
	
	
	
	

}
