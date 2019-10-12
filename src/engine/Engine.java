package engine;

import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

import maps.Stage;
import units.Angel;
import units.Berserker;
import units.King;
import units.Mage;
import units.Pesant;
import units.Unit;
import units.Warrior;


public class Engine implements Runnable{
	
	//engine speed
	private int TICK_SPEED = 20;
	
	//pointer to gameplay core, cor communication
	private GameHandler gameHandler;
	
	
	public Engine(GameHandler gh) { 
		System.out.println("Creating engine...");
		gameHandler = gh;
		gameHandler.setEngine(this);
	}
	
	
	
	//main loop
	public void run() {
		
		
		if(gameHandler.getStage()==null) gameHandler.setStage(1);


		
		
		while(!gameHandler.initialized()) {System.out.print(". ");}
		System.out.println();
		
		
		System.out.println("Running engine...");
				
		
		
		while ( true ) {
			
			while(gameHandler.getPause()) {}
			
			double t0 = System.currentTimeMillis();
			
			
			
			
			if(gameHandler.getKills()/4 +1  > gameHandler.getUnitsSize() && gameHandler.getUnitsSize() <= gameHandler.getMaxUnits()) {
				Unit toCreate = getRandomNewUnit();
				if(toCreate != null) gameHandler.createUnit(toCreate);
				
			}
			
			
			gameHandler.tick();
			gameHandler.update();
			
			
			
			
			
			double t1 = System.currentTimeMillis();
			//System.out.println("engine tick time: " + (t1-t0) + " / " + (int)Math.round(1000/TICK_SPEED));
			
			int CYCLE_SLEEP = (int)Math.round(1000/TICK_SPEED-(t1-t0));
			if(CYCLE_SLEEP < 0) CYCLE_SLEEP = 0; 
			
			try {
				Thread.sleep(CYCLE_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
	
	
	
	
	
	public void draw(Graphics g) {
		gameHandler.draw(g);
	}
	
	
	private Unit getRandomNewUnit() {
		Vector<Integer> coord = gameHandler.getRandomSpawnCoord();
		Vector<Unit> units = createUnitVector();
		Unit u = null;
		for(Unit un : units)
			if(VectorHelper.randBool(un.getSpawnOdds())) {
				u = un;
				u.setLocation(coord.get(0)*gameHandler.getFragmentSize()+gameHandler.getFragmentSize()/2, coord.get(1)*gameHandler.getFragmentSize() + gameHandler.getFragmentSize()/2);
				break;
			}
		
		return u;
	}
	
	private Vector<Unit> createUnitVector(){
		int lvl;
		
		if(gameHandler.getKills()<100) lvl = 1;
		else if(gameHandler.getKills()<300) lvl = 2;
		else if(gameHandler.getKills()<600) lvl = 3;
		else lvl = 4;
		
		Vector<Unit> v = new Vector<>();
		
		v.add(new Angel(lvl));
		v.add(new Pesant(lvl));
		v.add(new Warrior(lvl));
		v.add(new King(lvl));
		v.add(new Mage(lvl));
		v.add(new Berserker(lvl));
		
		return v;
	}

	
}
