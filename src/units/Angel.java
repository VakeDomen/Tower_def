package units;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Angel extends Unit {
	private static int HP = 20;
	private static int reward = 10;
	private static int dmg = 10;
	private static int range = 30;
	private static boolean agressive = false;
	private static int cooldown = 10;
	private static double spawnOdds = 0.2;
	private static double speed = 3;
	private static boolean flying = true;
	
	private static String icon = "units/enemy7";
	
	
	
	

	public Angel(float x, float y , int lvl) {
		super(x, y, icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	public Angel(int lvl) {
		super(icon, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	
	
	
	
	@Override 
	protected void tilt(Graphics2D g2d) {
		double newAngle = newAngle = Math.toRadians(20);;
		int dip;
		if(super.leftTilt) {
			dip = 2;
		}else {
			dip = -2;
		}
		
		super.tiltCounter--;
		if(super.tiltCounter <= 0 ) {
			super.tiltCounter = super.tiltRefresh;
			super.leftTilt = !super.leftTilt;
		}
		AffineTransform backup = g2d.getTransform();
	    AffineTransform a = AffineTransform.getRotateInstance(newAngle, (int)(x), (int)(y));
	    g2d.setTransform(a);
	    g2d.drawImage(super.icon, (int)(x-0.5*PLAYER_WIDTH), (int)(y-PLAYER_HEIGHT + dip), null);
	    g2d.setTransform(backup);
	}
	
}
