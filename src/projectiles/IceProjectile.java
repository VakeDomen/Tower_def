package projectiles;

import java.awt.Image;
import java.util.Vector;

import ResourceLoader.ResourceLoader;
import engine.VectorHelper;
import units.Unit;

public class IceProjectile extends Projectile{
	
	private static String iconPath = "iceProjectile.png";
	private static int PROJECTILE_SIZE = 30;
	
	
	private Vector<Unit> units;
	private int freezeDuration;
	private int projectileRange;
	
	
	public IceProjectile(int x, int y, int dmg, Unit target, int freezeDuration, int projectileRange, Vector<Unit> units) {
		super(x, y, dmg, target);
		super.icon = ResourceLoader.getImage(iconPath).getScaledInstance(PROJECTILE_SIZE, PROJECTILE_SIZE, Image.SCALE_DEFAULT);
		super.PROJECTILE_SIZE = PROJECTILE_SIZE;
		this.freezeDuration = freezeDuration;
		this.projectileRange = projectileRange;
		this.units = units;
	}
	
	
	
	@Override
	protected void hitTarget() {
		Vector<Unit> hits = getHitTargets();
		for(Unit u : hits) {
			u.hitTarget(dmg);
			u.freeze(freezeDuration);
		}
		
		hit = true;
	}
	
	
	protected Vector<Unit> getHitTargets(){
		Vector<Unit> ret = new Vector<>();
		for(Unit u : units) {
			if(inExplosionRange(target, u)) ret.add(u);
		}
		return ret;
	}
	
	private boolean inExplosionRange(Unit u , Unit v) {
		return projectileRange >= VectorHelper.vectorSize(VectorHelper.doubleToInt(VectorHelper.minusDouble( u.getLocation(), v.getLocation())));
	}
	
	

}
