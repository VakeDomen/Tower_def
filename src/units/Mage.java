package units;

import java.awt.Graphics2D;
import java.util.ArrayList;

import engine.GameHandler;
import projectiles.FireBall;

public class Mage extends Unit {
	private static int HP = 50;
	private static int reward = 70;
	private static int dmg = 100;
	private static int range = 150;
	private static boolean agressive = true;
	private static int cooldown = 50;
	private static double spawnOdds = 0.6;
	private static double speed = 3;
	private static boolean flying = false;
	
	private static String iconString = "units/enemy3";
	
	
	
	private ArrayList<FireBall> shots = new ArrayList<>();
	
	
	
	

	public Mage(float x, float y, int lvl) {
		super(x, y, iconString, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	public Mage(int lvl) {
		super(iconString, HP, dmg, reward, range, agressive, cooldown, spawnOdds, speed, lvl, flying);
		
	}
	
	
	@Override
	public void onDeathEffect(GameHandler gameHandler) {
		gameHandler.addGold(reward);
		
	}
	
	
	
	/*___________ attack sequence override ____________*/
	
	@Override
	public void update() {
		updateShots();
		if(super.freeze > 0 ) {
			super.freeze--;
			stop();
		}
		x += speedX;
		y += speedY;		
	}
	
	@Override
	protected void attackTarget() {
	
		
		if(super.cooldown == 0) {
			super.cooldown = super.cooldownTimer;
			shots.add(new FireBall(x, y, dmg, target));
		}
		
	}
	

	@Override
	public void draw(Graphics2D g2d) {
		if(!(speedX == 0 && speedY == 0)) tilt(g2d);
		else g2d.drawImage(icon, (int)(x-0.5*PLAYER_WIDTH), (int)(y-PLAYER_HEIGHT), null);

		
		drawHealth(g2d);
		
		drawProjectiles(g2d);
		
		if(super.freeze > 0) {
			g2d.drawImage(freezeEffect, (int)(x-0.5*(freezeEffect.getWidth(null))), y-freezeEffect.getHeight(null), null);
		}
	}
	
	
	private void drawProjectiles(Graphics2D g2d) {
		for(FireBall f : shots) {
			
			f.draw(g2d);
		}
	}
	
	private void updateShots() {
		ArrayList<FireBall> toDel = new ArrayList<>();
		
		for(FireBall b : shots) {
			b.update();
			if(b.getHit()) {
				toDel.add(b);
			}
		}
		for(FireBall f : toDel) shots.remove(f);
	}
}
