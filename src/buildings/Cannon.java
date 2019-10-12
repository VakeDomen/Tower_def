package buildings;

import java.awt.Image;
import java.util.Vector;

import ResourceLoader.ResourceLoader;
import engine.VectorHelper;
import projectiles.Projectile;
import units.Unit;

public class Cannon extends Building{
	protected int dmg;
	protected int range;
	protected int cooldown;
	protected int cooldownSpeed;
	
	protected double bonusDmgMultiplier = 1;
	
	
	
	protected Unit target = null;
	protected Vector<Unit> units = null;
	
	
	
	protected Image top;
	protected Image bottom;
	protected Image icon;
	
	
	public Cannon(int cost, int dmg, int lvl, int HP, String top, String bottom, String icon, int x, int y, int key, int range, int cooldown, String discription) {
		super(cost, lvl, HP, x, y, key, discription);
		this.dmg=dmg;
		this.top = ResourceLoader.getImage(top);
		this.bottom = ResourceLoader.getImage(bottom);
		this.icon = ResourceLoader.getImage(icon);
		this.range = range;
		this.cooldownSpeed = cooldown;
		this.cooldown = 0;
		
	}
	
	public Cannon(int cost, int dmg, int lvl,int HP,  String top, String bottom, String icon, int key, int range, int cooldown, String discription) {
		super(cost, lvl, HP, key, discription);
		this.dmg=dmg;
		this.top = ResourceLoader.getImage(top);
		this.bottom = ResourceLoader.getImage(bottom);
		this.icon = ResourceLoader.getImage(icon);
		this.range = range;
		this.cooldownSpeed = cooldown;
		this.cooldown = 0;
	}
	
	
	
	
	
	
	

	/*  _______________________________________________________ combat ______________________________________________________*/
	
	
	public Projectile shoot() {
		Projectile p = null;
		if(cooldown == 0) {
			p = new Projectile((int)(coordx),(int) (coordy),(int) Math.round(dmg*bonusDmgMultiplier), target);
			cooldown = cooldownSpeed;
		}
		return p;
	}
	
	

	
	public boolean findTarget(Vector<Unit> units, Vector<Projectile> projectiles) {
		
		this.units = units;
		
		
		if(cooldown > 0) cooldown--;		
		
		
		
		if(units.isEmpty()) return false;
		units = sortByDist(units);
		
		for(Unit u : units) {
			if( inRange(u) && !u.getOverkill() ) {
				target = u;
				return true;
			}
		}
		
		return false;
	}
	
	/*  _______________________________________________________ getters and setters _________________________________________________*/
	
	public Vector<Double> getTargetDirectionVector() {
		if(target != null) {
			Vector<Double> targetLocation = target.getLocation();
			Vector<Double> can = new Vector<>();
			can.add((double)(coordx)); can.add((double) (coordy));
			return VectorHelper.minusDouble(targetLocation, can);

		} else return null;
	}
	
	
	
	
	@Override
	public Image getIcon() { return this.icon; }
	public Image getBottom() { return this.bottom; }
	public Image getTop() { return this.top; }
	public int getDmg() { return this.dmg; }
	public Unit getTarget() { return target; }
	public void setTarget(Unit target) { this.target = target; }
	public int getRange() { return range; }
	public int getCooldown() { return cooldownSpeed; }
	
	public void addDmgMultiplier(double mtp) { this.bonusDmgMultiplier+=mtp; }
	public double getDmgMultiplier(double mtp) { return bonusDmgMultiplier; }
	public void removeDmgMultiplier(double mtp) { this.bonusDmgMultiplier-=mtp; }
	
	
	
	

	/*  _______________________________________________________ helper functions _________________________________________________*/
	
	protected boolean inRange(Unit unit) {
		int dist = distTo(unit);
		if(dist < range) return true;
		else return false;
	}
	
	
	
	
	


		





	
	
	
}
