package units;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import ResourceLoader.ResourceLoader;
import ai.BattleAi;
import ai.MovementAi;
import buildings.Building;
import engine.GameHandler;
import engine.VectorHelper;
import projectiles.Projectile;

public class Unit {
	protected int x;
	protected int y;
	protected float speedX = 0;
	protected float speedY = 0;
	protected int freeze = 0;

	protected Vector<Projectile> incoming = new Vector<>();
	
	
	protected static final int PLAYER_HEIGHT = 35;
	protected static final int PLAYER_WIDTH = 25;
	protected static final int HEALTH_BAR_WIDTH = 30;
	protected static final int HEALTH_BAR_HEIGHT = 5;
	
	protected static final float LVL_MULTIPLIER = 1.7f;
	
	protected Image icon;
	protected Image freezeEffect;
	
	
	protected int HP;
	protected int fullHP;
	protected int dmg;
	protected int reward;
	protected int range;
	protected double speed;
	protected boolean agressive;
	protected double spawnOdds;
	protected int lvl;
	protected boolean flying;
	
	protected int cooldown;
	protected int cooldownTimer;
	protected Building target;
	
	protected MovementAi movementAI;
	protected BattleAi battleAi;
	
	
	
	protected boolean leftTilt = false;
	protected int tiltCounter = 10;
	protected int tiltRefresh = 20;
	
	
	
	
	
	public Unit(float x, float y, String icon, int HP, int dmg, int reward, int range, boolean agressive, int cooldown, double spawnOdds, double speed, int lvl, boolean flying) {
		this.x=(int)x;
		this.y=(int)y;
		this.icon = ResourceLoader.getImage(icon+lvl+".png").getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_DEFAULT);
		this.freezeEffect = ResourceLoader.getImage("freezeEffect.png").getScaledInstance((int)Math.round(PLAYER_WIDTH*1.2),(int)Math.round(PLAYER_HEIGHT*0.2), Image.SCALE_DEFAULT);
		this.HP = HP;
		this.fullHP = HP;
		this.dmg = dmg;
		this.reward = reward;
		this.range = range;
		this.agressive = agressive;
		this.flying = flying;
		this.spawnOdds = spawnOdds;
		this.speed=speed;
		this.lvl = lvl;
		this.cooldownTimer = cooldown;
		this.cooldown = 0;
		this.target = null;
		
		scaleByLvl();
		
	}
	public Unit(String icon, int HP, int dmg, int reward, int range, boolean agressive, int cooldown, double spawnOdds, double speed, int lvl, boolean flying) {
		this.icon = ResourceLoader.getImage(icon+lvl+".png").getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_DEFAULT);
		this.freezeEffect = ResourceLoader.getImage("freezeEffect.png").getScaledInstance((int)Math.round(PLAYER_WIDTH*1.2),(int)Math.round(PLAYER_HEIGHT*0.2), Image.SCALE_DEFAULT);
		this.HP = HP;
		this.fullHP = HP;
		this.dmg = dmg;
		this.reward = reward;
		this.range = range;
		this.agressive = agressive;
		this.flying = flying;
		this.spawnOdds = spawnOdds;
		this.speed = speed;
		this.lvl = lvl;
		this.cooldownTimer = cooldown;
		this.cooldown = 0;
		this.target = null;
		
		scaleByLvl();
		
	}
	
	
	
	
	/*  _______________________________________________________combat and ai _________________________________________________*/
	
	
	
	public void hitTarget(int dmg) {
		this.HP -= dmg;
	}
	
	public void attack() {
		
		if(target!=null) {
			
			if(inRange(target)) {
				attackTarget();
				stop();
			}else {
				move(battleAi.findDirection(this, target));
			}
			
		}
	}
	protected void attackTarget() {
		if(cooldown == 0) {
			System.out.println("unit attacked");
			target.hitTarget(dmg);
			cooldown = cooldownTimer;
		}
		
	}
	public boolean findTarget(Vector<Building> buildings) {
		if(cooldown != 0) cooldown--;
				
		if(buildings.isEmpty()) return false;
		else {
			buildings = sortByDist(buildings);
			target = buildings.firstElement();
			return true;
		}
	}







	/*  _______________________________________________________movement and ai _________________________________________________*/
	
	
	public void freeze(int duration) {
		this.freeze = duration;
	}
	
	public void update() {
		if(freeze > 0 ) {
			freeze--;
			stop();
		}
		x += speedX;
		y += speedY;		
	}
	
	public void stop() {
		speedX = 0;
		speedY = 0;
	}
	public void move(Vector<Double> direction) {
		if(direction != null) {
			
			direction = VectorHelper.resizeVector(direction, speed);
			
			speedX = (int)Math.round(direction.get(0));
			speedY = (int)Math.round(direction.get(1));
			
		}else{
			System.out.println("unit has no path to the end! - switched to attack mode");
			attack();
		}
		
	}
	
	public void moveToEndAi(int[][] board, int chunk) {
		Vector<Double> move = movementAI.predictMove(board, (x)/chunk, (y)/chunk);
		
		if(move!=null && !flying) {
			int oldX = x/chunk;
			int oldY = y/chunk;
			int newX = (int)(x+move.get(0))/chunk;
			int newY =(int)(y+move.get(1))/chunk;
			
			if(board[newX][newY] >= 2 ) {
				int xdif = newX - oldX;

				if(xdif == 0) move.set(1, 0.0);
				else move.set(0, 0.0);
			}
			
		}
		
		move(move);
	}
	
	
	

	/*  _______________________________________________________ graphics _________________________________________________________*/
	
	
	public void draw(Graphics2D g2d) {
		if(!(speedX == 0 && speedY == 0)) tilt(g2d);
		else g2d.drawImage(icon, (int)(x-0.5*PLAYER_WIDTH), (int)(y-PLAYER_HEIGHT), null);
		//g2d.drawImage(icon, (int)(x), (int)(y), null);
		drawHealth(g2d);
		
		if(freeze > 0) {
			g2d.drawImage(freezeEffect, (int)(x-0.5*(freezeEffect.getWidth(null))), y-freezeEffect.getHeight(null), null);
		}
		
		
	}
	protected void drawHealth(Graphics2D g2d) {
		if(HP < fullHP) {
			g2d.setColor(Color.RED);
			g2d.fillRect((int)(x-HEALTH_BAR_WIDTH/2), (int)(y-PLAYER_HEIGHT), HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
			g2d.setColor(Color.GREEN);
			g2d.fillRect((int)(x-HEALTH_BAR_WIDTH/2), (int)(y-PLAYER_HEIGHT), (int)(HEALTH_BAR_WIDTH * (HP*1.0/fullHP)), HEALTH_BAR_HEIGHT);
			g2d.setColor(Color.BLACK);
		}
	}
	protected void tilt(Graphics2D g2d) {
		double newAngle;
		if(leftTilt) {
			newAngle = Math.toRadians(3);
		}else {
			newAngle = Math.toRadians(-3);
		}
		
		tiltCounter--;
		if(tiltCounter <= 0 ) {
			tiltCounter = tiltRefresh;
			leftTilt = !leftTilt;
		}
		AffineTransform backup = g2d.getTransform();
	    AffineTransform a = AffineTransform.getRotateInstance(newAngle, (int)(x), (int)(y));
	    g2d.setTransform(a);
	    g2d.drawImage(icon, (int)(x-0.5*PLAYER_WIDTH), (int)(y-PLAYER_HEIGHT), null);
	    g2d.setTransform(backup);
	}
	
	
	/*  _______________________________________________________getters and setters _________________________________________________*/
	
	
	public Vector<Double> getLocation(){
		Vector<Double> coords = new Vector<>();
		coords.add((double) x);
		coords.add((double) y);
		return coords;
	}
	public boolean getOverkill() { 
		int dmgSum = 0;
		Vector<Projectile> toDel = new Vector<>();
		for(Projectile p : incoming) {
			if(!p.getHit()) dmgSum += p.getDmg();
			else toDel.add(p);
		}
		for(Projectile p : toDel) incoming.remove(p);
		return dmgSum > HP; 
	}
	public void setLocation(int x, int y) {
		this.x = x; 
		this.y = y;
	}
	
	
	public int getX() { return x; }
	public int getY() { return y; }
	public void setX(int x) { this.x=x; }
	public void setY(int y) { this.y=y; }
	public int getHP() { return this.HP; }
	public int getReward() { return this.reward; }
	public void setMovementAI(MovementAi ai) { this.movementAI = ai; }
	public void setBattleAI(BattleAi ai) { this.battleAi = ai; }
	public BattleAi getBattleAi() { return battleAi; }
	public MovementAi getMovementAI() { return movementAI; }
	public void addProjectile(Projectile p) { incoming.add(p); }
	public boolean getAgressive() { return agressive; }
	public double getSpawnOdds() { return spawnOdds; }
	public boolean getFlying() { return flying; }
	


	/*  _______________________________________________________ helper functions ___________________________________________________*/
	
	
	
	protected Vector<Building> sortByDist(Vector<Building> buildings) {
		boolean swaped = true;
		while(swaped) {
			swaped = false;
			for (int i = 1; i < buildings.size(); i++) {
				if(distTo(buildings.get(i-1)) > distTo(buildings.get(i))) {
					Building tmp = buildings.get(i);
					buildings.set(i, buildings.get(i-1));
					buildings.set(i-1, tmp);
					swaped = true;
					
				}
			}
		}		
		return buildings;
	}
	
	protected boolean inRange(Building target2) {
		if(distTo(target2) <= range ) return true;
		else return false;
	}
	
	
	protected int distTo(Building building) {
		Vector<Integer> can = new Vector<>();
		Vector<Integer> un = new Vector<>();
		can.add(building.getXcoord()); can.add(building.getYcoord());
		un.add(x); un.add(y);
		return VectorHelper.vectorSize(VectorHelper.minus(can, un));
	}
	public void onDeathEffect(GameHandler gameHandler) {
		gameHandler.addGold(this.reward);
	}

	
	
	
	protected void scaleByLvl() {
		this.dmg = (int) Math.round( dmg * Math.pow(LVL_MULTIPLIER, lvl));
		this.fullHP = (int) Math.round( fullHP * Math.pow(LVL_MULTIPLIER, lvl));
		this.HP = (int) Math.round( HP * Math.pow(LVL_MULTIPLIER, lvl));
	}
	
	
}
