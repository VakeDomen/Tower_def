package buildings;

import projectiles.IceProjectile;
import projectiles.Projectile;

public class IceCannon extends Cannon {
	private static int cost = 400;
	private static int dmg = 5;
	private static int lvl = 1;
	private static int key = 5;
	private static int range = 250;
	private static int cooldown = 20;
	private static int HP = 100;
	private static String top = "cannons/cannon4top.png";
	private static String bottom = "cannons/cannon4bottom.png";
	private static String icon = "cannons/cannon4icon.png";
	private static String dicription = "This is an ice cannon. Ice cannon has a medium range and will do a very small ammount of damage. It has an extra effect, such that it freezes it's targets on impact.";
	
	private static int freeze = 30;
	private static int freezeSpread = 60;
	
	public IceCannon(int x, int y, int fragmentSize) {
		super(cost, dmg, lvl, HP, top, bottom, icon, x, y, key, range, cooldown, dicription);
	}
	public IceCannon() {
		super(cost, dmg, lvl, HP, top, bottom, icon, key, range, cooldown, dicription);
	}

	
	
	
	
	@Override
	public Projectile shoot() {
		Projectile p = null;
		if(super.cooldown == 0) {
			p = new IceProjectile((int)(coordx),(int) (coordy), dmg, target, freeze, freezeSpread, super.units);
			super.cooldown = super.cooldownSpeed;
		}
		return p;
	}

}
