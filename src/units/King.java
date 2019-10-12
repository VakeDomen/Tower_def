package units;

import engine.GameHandler;

public class King extends Unit{
	private static int HP = 20;
	private static int reward = 40;
	private static int dmg = 100;
	private static int range = 30;
	private static boolean agressive = false;
	private static int cooldown = 30;
	private static double spawnOdds = 0.1;
	private static double speed = 2;
	private static boolean flying = false;
	
	private static String icon = "units/enemy6";
	
	
	
	

	public King(float x, float y , int lvl) {
		super(x, y, icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	public King(int lvl) {
		super(icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	
	
	@Override
	public void onDeathEffect(GameHandler gameHandler) {
		gameHandler.addGold(reward);
		for(int i = 0 ; i<3 ; i++) {
			gameHandler.createUnit(new Warrior(this.x, this.y, super.lvl));
		}
		
	}

}
