package projectiles;

import java.util.Vector;

import engine.VectorHelper;
import units.Unit;

public class BalistaProjectile extends Projectile {
	
	private Vector<Unit> units;
	
	private int projectileRange;
	

	public BalistaProjectile(int x, int y, int dmg, Unit target, int projectileRange, Vector<Unit> units) {
		super(x, y, dmg, target);
		this.units = units;
		this.projectileRange = projectileRange;
		
	}
	
	
	
	@Override
	protected void hitTarget() {
		Vector<Unit> hits = getHitTargets();
		for(Unit u : hits) {
			u.hitTarget(super.dmg);
		}
		
		hit = true;
	}
	
	
	protected Vector<Unit> getHitTargets(){
		Vector<Unit> ret = new Vector<>();
		for(Unit u : units) {
			if(inExplosionRange(super.target, u)) ret.add(u);
		}
		return ret;
	}
	
	private boolean inExplosionRange(Unit u , Unit v) {
		return projectileRange >= VectorHelper.vectorSize(VectorHelper.doubleToInt(VectorHelper.minusDouble( u.getLocation(), v.getLocation())));
	}
	

}
