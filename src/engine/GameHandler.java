package engine;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import ai.BattleAi;
import ai.FlyAi;
import ai.MovementAi;
import buildings.Balista;
import buildings.Building;
import buildings.Cannon;
import buildings.IceCannon;
import buildings.SimpleCannon;
import buildings.SteelCannon;
import buildings.Wall;
import gfx.FloatText;
import maps.Stage;
import maps.Stage1;
import projectiles.Projectile;
import units.Angel;
import units.Pesant;
import units.Unit;

public class GameHandler {
	private static final int STARTING_GOLD = 1000;
	private static final int MAX_UNITS = 40;
	private static final int STARTING_HEALTH = 100;
	
	
	/*FLAG FOR GUI TO UPDATE DATA*/
	public boolean guiBoardUpdateFlag = false;
	public boolean guiMenuUpdateFlag = false;
	private boolean pause = false;
	
	
	/* LOCKS */
	public Object guiLock = new Object();
	public Object unitsLock = new Object();
	public Object projectileLock = new Object();
	public Object buildingLock = new Object();
	public Object goldLock = new Object();
	public Object engineLock = new Object();
	public Object floatTextLock = new Object();
	public Object healthLock = new Object();
	
	private Vector<Building> buildingList = null;
	private Vector<Building> buildings = null;
	private Vector<Unit> units = null;
	private Vector<Projectile> projectiles = null;
	private ArrayList<FloatText> floatTexts = null;
	
	
	private int gold;
	private int kills;
	private int health;
	
	
	
	private Stage stage = null;
	private int[][] board = null;
	private int fragmentSize;
	private MovementAi movementAI;
	private BattleAi battleAI;
	private FlyAi flyAI;
	
	
	private Engine engine = null;
	
	
	
	public GameHandler() {
		this.buildingList = new Vector<>();
		this.buildings = new Vector<>();
		this.projectiles = new Vector<>();
		this.units = new Vector<>();
		this.floatTexts = new ArrayList<>();
		this.fragmentSize = -1;
		
		movementAI = new MovementAi();
		battleAI = new BattleAi();
		flyAI = new FlyAi();
		this.gold = STARTING_GOLD;
		this.kills = 0;
		this.health = STARTING_HEALTH;
		
		initializeCannons();
	}
	
	
	public void update() {
		updateUnits();
		updateProjectiles();
		
		
		
		
		updateBuildings();
		shootCannons();
		
	}
	
	public void tick() {
		moveUnits();
	}
	
	
	
	

	
	/* _____________________________________________________ units stuff _________________________________________________________- */
	
	
	
	
	public Vector<Unit> getUnits() {
		synchronized(unitsLock) {
			return units;
		}
	}

	public void updateUnits() {
		synchronized(unitsLock) {
			Vector<Unit> toRemove = new Vector<>();
			for(Unit u : units) {
				
				u.update();
				if (checkIfAtEnd(u) || u.getHP()<= 0 ) toRemove.add(u);
				
			}
			killUnits(toRemove);
		}
		
	}
	
	private void killUnits(Vector<Unit> toRemove) {
		synchronized(unitsLock) {
			if(!toRemove.isEmpty()) {
				for(Unit u : toRemove) {
					if(u.getHP()<= 0) {
						u.onDeathEffect(this);
						kills++;
						synchronized(floatTextLock) {
							floatTexts.add(new FloatText(u.getX(), u.getY(), "+"+u.getReward()+"g" , false));
						}
					}
					
					Vector<Projectile> toRem = new Vector<>();
					synchronized(projectileLock){
						for(Projectile p : projectiles) {
							if(p.getTarget() == u) toRem.add(p);
						}
					}
					removeProjectiles(toRem);
					units.remove(u);
				}
			}
		}
	}
	private boolean checkIfAtEnd(Unit u) {
		Vector<Vector<Integer>> ends = getAllEndCoords();
		for(Vector<Integer> end : ends) {
			if(end.get(0) == u.getX()/fragmentSize && end.get(1) == u.getY()/fragmentSize) {
				decrementHealth();
				return true;
			}
		}
		return false;
	}


	public void moveUnits() {
		synchronized(unitsLock) {
			for(Unit u : units) {
				
				if(u.getMovementAI() == null && u.getFlying()) u.setMovementAI(flyAI);
				else if(u.getMovementAI() == null) u.setMovementAI(movementAI);
				if(u.getBattleAi() == null) u.setBattleAI(battleAI);
				
				
				if(fragmentSize > 0) {
					synchronized(buildingLock) {
						if(u.findTarget(buildings) && u.getAgressive()) u.attack();
						else u.moveToEndAi(board, fragmentSize);
					}
				}
			}
	
		}
	}
	public int getMaxUnits() { synchronized(unitsLock) { return MAX_UNITS; } }
	public int getUnitsSize() { synchronized(unitsLock) { return units.size(); } }
	
	
	public void createUnit(Unit unit) {
		synchronized(unitsLock) {
			Random r = new Random();
			unit.setX(unit.getX()+(r.nextInt(fragmentSize)-fragmentSize/2));
			unit.setY(unit.getY()+(r.nextInt(fragmentSize)-fragmentSize/2));
			units.add(unit);
		}
	}
	
	
	
	
	

	
	/* _____________________________________________________ building stuff _________________________________________________________- */
	
	private void shootCannons() {
		synchronized(buildingLock){
			for(Building c : buildings) {
				synchronized(unitsLock) {
					synchronized(unitsLock) {
						if(c instanceof Cannon && ((Cannon) c).findTarget(units, projectiles)) {
							Projectile p = ((Cannon) c).shoot();
							if(p != null) {
								projectiles.add(p);
							}
						}
					}
				}
			}
		}
	}
	
	private void updateBuildings() {
		cleanDestroyedBuildings();
	}
	private void cleanDestroyedBuildings() {
		Vector<Building> toDel = new Vector<>();
		synchronized(buildingLock) {
			for(Building c : buildings) {
				if(c.getHP() <= 0) {
					toDel.add(c);
				}
			}
			for(Building c : toDel) {
				System.out.println("destroyed building: " + c.getX() + " " + c.getY());
				buildings.remove(c);
				board[c.getX()][c.getY()] = 0;
				movementAI.addNode(c.getX(), c.getY());
			}
			
			if(!toDel.isEmpty()) guiBoardUpdateFlag = true;
		}
	}
	
	public String buildBuilding(int x, int y, Building building) {
		String ret = "";
		synchronized(goldLock) {
			synchronized(buildingLock){ 
				synchronized(guiLock) {		
					if(gold >= building.getCost() && board[x][y] == 0) {
				
							movementAI.deleteNode(x, y);
					
							board[x][y] = building.getKey();
							gold -= building.getCost(); 
				
							building.setLocation(x, y, fragmentSize);
							
							buildings.add(building); 
							guiBoardUpdateFlag = true; 
					
							return ret;
					} else {
						if(gold < building.getCost()) ret = "Not enough gold!";
						else if(board[x][y] != 0) ret = "You can not build here!";
								
								
						System.out.println("unable to build "+building.getClass());
						System.out.println("\tboard: " + board[x][y] + " on: " + x +" | " + y);
						System.out.println("\tgold: " + gold + " cost: " + building.getCost());
						return ret;
					}
				}
				
			}
		}
	}
	
	
	public void buildBuildingStream(Vector<Building> buildStream) {
		synchronized(goldLock) {
			synchronized(buildingLock){ 
				synchronized(guiLock) {		
					for(Building b : buildStream) {
						int x = b.getX();
						int y = b.getY();
						if(gold >= b.getCost() && board[x][y] == 0) {
							
							movementAI.deleteNode(x, y);
					
							board[x][y] = b.getKey();
							gold -= b.getCost(); 
				
							b.setLocation(x, y, fragmentSize);
							
							buildings.add(b); 
							guiBoardUpdateFlag = true; 
					
						} else {
							System.out.println("unable to build "+b.getClass());
							System.out.println("\tboard: " + board[x][y] + " on: " + x +" | " + y);
							System.out.println("\tgold: " + gold + " cost: " + b.getCost());
						}						
					}				
				}
			}
		}
	}
	
	
	
	public Vector<Building> getUpdatedBuildings(){
		synchronized(buildingLock){ 
			synchronized(guiLock) {
				guiBoardUpdateFlag = false;
				System.out.println("gui fetched buildings");
				return buildings;
			}
		}
	}
	
	
	public Vector<Building> getBuildings() { synchronized(buildingLock){ return buildings; } }
	public Vector<Building> getBuildingList(){ return this.buildingList; }
	
	
	
	private void initializeCannons() {
		this.buildingList.add(new Wall());
		this.buildingList.add(new SimpleCannon());
		this.buildingList.add(new SteelCannon());
		this.buildingList.add(new Balista());
		this.buildingList.add(new IceCannon());
	}
	
	
	/* _____________________________________________________ projectile stuff _________________________________________________________- */
	
	
	public void updateProjectiles() {
		Vector<Projectile> toRemove = new Vector<>();
		for(Projectile p : projectiles) {
			synchronized(projectileLock) { 
				p.update(); 
				if(p.getHit() || p.getTarget().getHP() <= 0) toRemove.add(p);
				
			}
		}
		if(!toRemove.isEmpty()) for(Projectile u : toRemove) projectiles.remove(u);
	}
	public Vector<Projectile> getProjectiles() {
		synchronized(projectileLock) {
			return projectiles;
		}
	}
	
	
	private void removeProjectiles(Vector<Projectile> toRemove) {
		for(Projectile p : toRemove) {
			synchronized(projectileLock) { projectiles.remove(p); }
		}
	}
	
	
	/* _____________________________________________________ board stuff _________________________________________________________- */
	
	public void setStage(int stagelvl) {
		System.out.println("new stage: Stage" + stagelvl);
		switch(stagelvl) {
			case 1: 
				stage = new Stage1();
				board = stage.getBoard();
				movementAI.intializeAStarPathfinding(board);
			
		}
	}
	public Vector<Integer> getRandomSpawnCoord() {
		Vector<Vector<Integer>> spawns = getAllSpawnCoords();
		Random r = new Random();
		return spawns.get(r.nextInt(spawns.size()));
	}



	private Vector<Vector<Integer>> getAllSpawnCoords() {
		Vector<Vector<Integer>> spawns = new Vector<>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if(board[i][j] == 1) {
					Vector<Integer> spawn = new Vector<>();
					spawn.add(i);
					spawn.add(j);
					spawns.add(spawn);
				}
			}
		}
		return spawns;
	}
	private Vector<Vector<Integer>> getAllEndCoords() {
		Vector<Vector<Integer>> ends = new Vector<>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if(board[i][j] == -1) {
					Vector<Integer> end = new Vector<>();
					end.add(i);
					end.add(j);
					ends.add(end);
				}
			}
		}
		return ends;
	}

	
	
	public Stage getStage() { return stage; }
	public int[][] getBoard(){ return board; }
	
	
	
	/* _____________________________________________________ graphics and game info _____________________________________________________________- */
	
	
	public void draw(Graphics g) {
		
	}
	
	
	public int getGold() { synchronized(goldLock) { return gold; } }
	public int getKills() { return kills; }
	public int getHealth() { synchronized(healthLock) {return health;}}
	public void addGold(int reward) { synchronized(goldLock) { this.gold+=reward; } }
	public int getFragmentSize() { return fragmentSize; }

	private void decrementHealth() { synchronized(healthLock) { this.health--; } }
	public boolean guiBoardUpdateFlag() { synchronized(guiLock) { return guiBoardUpdateFlag; }}




	public ArrayList<FloatText> fetchFloatText(){
		synchronized(floatTextLock) {
			ArrayList<FloatText> tmp = (ArrayList<FloatText>) floatTexts.clone();
			floatTexts.clear();
			return tmp;
		}
	}
	public void setFragmentSize(int fragmentSize) {
		this.fragmentSize = fragmentSize;
	}


	
	
	/* _____________________________________________________ game controls ____________________________________________________________________- */
	
	public void pauseGame() {
		synchronized(engineLock) {
			if(!pause) {
				pause = true;
			}else {
				pause = false;
			}
			System.out.println("pause: " + pause);

		}
	}
	public boolean getPause() {
		synchronized(engineLock) {
			return pause;
		}
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public boolean initialized() {
		 return (engine != null && fragmentSize != -1);
	}


	
	
	
	
}
