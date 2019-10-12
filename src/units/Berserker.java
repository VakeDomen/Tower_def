package units;

public class Berserker extends Unit{

	
	
	private static int HP = 350;
	private static int reward = 100;
	private static int dmg = 120;
	private static int range = 30;
	private static boolean agressive = false;
	private static int cooldown = 70;
	private static double spawnOdds = 0.9;
	private static double speed = 1;
	private static boolean flying = false;
	
	private static String icon = "units/enemy5";
	
	
	
	

	public Berserker(float x, float y , int lvl) {
		super(x, y, icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	public Berserker( int lvl) {
		super(icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	
	
	
	@Override
	public void hitTarget(int dmg) {
		super.agressive = true;
		super.HP -= dmg;
	}
}
