package buildings;

import projectiles.BalistaProjectile;
import projectiles.Projectile;

public class Balista extends Cannon{
	
	private static int cost = 600;
	private static int dmg = 40;
	private static int lvl = 1;
	private static int key = 4;
	private static int range = 400;
	private static int cooldown = 40;
	private static int HP = 150;
	private static String top = "cannons/cannon3top.png";
	private static String bottom = "cannons/cannon3bottom.png";
	private static String icon = "cannons/cannon3icon.png";
	private static String discription = "This is a balista. It's a long range tower, with an deadly area-of-effect projectiles. ";
	
	
	private static int spread = 70;
	
	
	public Balista(int x, int y, int fragmentSize) {
		super(cost, dmg, lvl, HP, top, bottom, icon, x, y, key, range, cooldown, discription);
	}
	public Balista() {
		super(cost, dmg, lvl, HP, top, bottom, icon, key, range, cooldown, discription);
	}
	
	
	@Override
	public Projectile shoot() {
		Projectile p = null;
		if(super.cooldown == 0) {
			p = new BalistaProjectile((int)(coordx),(int) (coordy), dmg, target, spread, super.units);
			super.cooldown = super.cooldownSpeed;
		}
		return p;
	}

}
