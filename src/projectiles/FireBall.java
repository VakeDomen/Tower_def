package projectiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import ResourceLoader.ResourceLoader;
import buildings.Building;
import engine.VectorHelper;

public class FireBall {
	
	private int FIREBALL_SPEED = 7;
	private int FIREBALL_ACCURACY = 4;
	private int FIREBALL_SIZE = 20;
	private String iconPath = "fireball.png";
	private Image icon;
	
	
	
	private int x;
	private int y;
	private int dmg;
	private Building target;
	
	private boolean hit = false;
	
	public FireBall(int x, int y, int dmg, Building target) {
		this.x = x;
		this.y = y;
		this.dmg = dmg;
		this.target = target;
		
		this.icon = ResourceLoader.getImage(iconPath).getScaledInstance(FIREBALL_SIZE, FIREBALL_SIZE, Image.SCALE_DEFAULT);
	}
	
	
	
	public void update() {
		Vector<Integer> speed = new Vector<>();
		speed.add(target.getXcoord() - x);
		speed.add(target.getYcoord() - y);
		
		speed = VectorHelper.resizeVector(speed, FIREBALL_SPEED);
		
		x += speed.get(0);
		y += speed.get(1);
		
		if(target.getHP()<= 0) this.hit = true;
		
		
		if(Math.abs(target.getXcoord()-x) <= FIREBALL_ACCURACY && (Math.abs(target.getYcoord() - y) < FIREBALL_ACCURACY || (target.getYcoord()-y <= FIREBALL_ACCURACY && target.getYcoord()-y > 0)) && !hit) {
			target.hitTarget(dmg);
			hit = true;
		}
		
	}
	
	
	public boolean getHit() { return hit; }
	
	
	
	private float lastAngle = 0.0f;
	public void draw(Graphics2D g2d) {
		
		float newAngle = findAngle();
		
		if(newAngle == 0.0 ) newAngle = lastAngle;
		else lastAngle += newAngle;
		
		
		AffineTransform backup = g2d.getTransform();
	    AffineTransform a = AffineTransform.getRotateInstance(newAngle, (int)(x), (int)(y));
	    g2d.setTransform(a);
	    g2d.drawImage(icon, x-FIREBALL_SIZE/2, y - FIREBALL_SIZE/2, null);
		
	    g2d.setTransform(backup);
	
	}
	
	
	
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
