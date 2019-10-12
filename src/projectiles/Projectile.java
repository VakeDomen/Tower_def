package projectiles;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import ResourceLoader.ResourceLoader;
import buildings.Building;
import buildings.Cannon;
import engine.VectorHelper;
import units.Unit;

public class Projectile {
	protected static final int PROJECTILE_SPEED = 10;
	protected static final String PROJECTILE_ICON = "bullet.png";
	protected int PROJECTILE_SIZE = 10;
	protected static final int PROJECTILE_ACCURACY = 5;
	
	protected int x;
	protected int y;
	protected Image icon;
	protected Unit target;
	protected int dmg;
	protected boolean hit = false;
	
	public Projectile(int x, int y,int dmg, Unit target) {
		this.x = x;
		this.y = y;
		this.dmg = dmg;
		this.target = target;
		this.icon = ResourceLoader.getImage(PROJECTILE_ICON).getScaledInstance(PROJECTILE_SIZE, PROJECTILE_SIZE, Image.SCALE_DEFAULT);
		if(target!=null)target.addProjectile(this);
	}
	
	
	public void update() {
		Vector<Integer> speed = new Vector<>();
		speed.add(((Unit) target).getX() - x);
		speed.add(((Unit) target).getY() - y);
		
		speed = VectorHelper.resizeVector(speed, PROJECTILE_SPEED);
		
		x += speed.get(0);
		y += speed.get(1);
		
		if(Math.abs(((Unit) target).getX()-x) <= PROJECTILE_ACCURACY && (Math.abs(((Unit) target).getY() - y) < PROJECTILE_ACCURACY || (((Unit) target).getY()-y <= PROJECTILE_ACCURACY+2 && ((Unit) target).getY()-y > 0)) && !hit) {
			hitTarget();
		}
		
		
	}
	
	protected void hitTarget() {
		((Unit) target).hitTarget(dmg);
		hit = true;
	}
	/*  _______________________________________________________  graphics  _________________________________________________*/
	
	
	
	protected float lastAngle = 0.0f;
	
	
	public void draw(Graphics2D g2d) {
		
		float newAngle = findAngle();
		
		if(newAngle == 0.0 ) newAngle = lastAngle;
		else lastAngle += newAngle;
		
		
		AffineTransform backup = g2d.getTransform();
	    AffineTransform a = AffineTransform.getRotateInstance(newAngle, (int)(x), (int)(y));
	    g2d.setTransform(a);
	    g2d.drawImage(icon,(int)( x-0.5*PROJECTILE_SIZE),(int)( y-0.5*PROJECTILE_SIZE),  null);
		
	    g2d.setTransform(backup);
				

	}
	
	
	
	/*  _______________________________________________________getters and setters _________________________________________________*/
	public boolean getHit() { return hit; }
	public Unit getTarget() { return (Unit) target; }
	public int getDmg() { return dmg; }
	
	
	
	protected float findAngle() {
		Vector<Double> targetCoord = target.getLocation();
		Vector<Double> thisLocation = new Vector<>();
		thisLocation.add((double) x);
		thisLocation.add((double) y);
		
		targetCoord = VectorHelper.minusDouble(targetCoord, thisLocation);
		thisLocation.set(0, 0.0);
		thisLocation.set(1, 1.0);
			
		double angle = VectorHelper.angleBetweenVectors(targetCoord, thisLocation);
		
		if((targetCoord.get(0) + thisLocation.get(1)) > 1) angle = 360-angle;
		
		
		return (float)Math.toRadians(angle);
		
		
	}
	
	
}
