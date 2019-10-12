package gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import buildings.Building;
import buildings.Cannon;
import engine.GameHandler;
import input.FragmentHoverListener;
import input.SelectFragment;
import maps.Stage;
import projectiles.Projectile;
import units.Unit;

@SuppressWarnings("serial")
public class GameMap extends JPanel {
	
	private MapFragment[][] mapMatrix;
	private int fragmentSize;
	
	
	private Building hoverBuilding;
	private Image hoverBuildingImage;
	private boolean hover;
	private boolean drag;
	private MouseMotionListener hoverListener = null;
	private ArrayList<FloatText> floatTexts = new ArrayList<>();
	
	private GameHandler gameHandler;
	
	//enemys and cannons on the board
	
	private Vector<Unit> units = null;
	private Vector<Projectile> projectiles = null;
	
	
	Object projectileLock = new Object();
	Stage stage = null;
	
	public GameMap(GameHandler gameHandler) {
		System.out.println("Creating GameMap...");
		this.gameHandler = gameHandler;
		this.hover=false;
		this.drag = false;
		
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.BLACK);
		g2d.setColor(Color.BLACK);
		/*
		 * INITAL LOOP, TO WAIT FOR STAGE TO LOAD ________________________________________________________________________________________________
		 */

		while(stage==null) {
			if(gameHandler.getStage() != null ) {
				initializeStage();
				break;
			}
		}
	
		
		
		
		/*
		 * UPDATE DATA IF NEEDED ________________________________________________________________________________________________
		 */
		if(gameHandler.guiBoardUpdateFlag()) updateBoard();
		units = gameHandler.getUnits();
		synchronized(projectileLock) {projectiles = gameHandler.getProjectiles(); }
		
		
		
		
		/*
		 * DISPLAY ALL COMPONENTS OF GAMEMAP ______________________________________________________________________________________
		 */
		displayBoard(g2d);
		displayUnits(g2d);
		displayProjectiles(g2d);
		displaySelected(g2d);
		
		if(hover) displayHoverRange(g2d);
		
		displayFloatText(g2d);
		
		super.repaint();
	}
	
	
	
	private void displayProjectiles(Graphics2D g2d) {
		synchronized(projectileLock) {
			for(Projectile p : projectiles) {
				p.draw(g2d);
			}
		}
		

	
	}


	private void updateBoard() {
		Vector<Building> cannons = gameHandler.getUpdatedBuildings();
		for(Building c : cannons) {
			mapMatrix[c.getX()][c.getY()] = new MapFragment(c.getX()*fragmentSize, c.getY()*fragmentSize, c, fragmentSize, c.getX(), c.getY(), this);
		}
		for (int i = 0; i < mapMatrix.length; i++) {
			for (int j = 0; j < mapMatrix[0].length; j++) {
				if(mapMatrix[i][j].getBuilding()!=null && !cannons.contains(mapMatrix[i][j].getBuilding())) mapMatrix[i][j].removeBuilding(); 
			}
		}		
	}
	
	
	
	
	
	
	
	
	/*________________________________________________ initializations _______________________________________________________  */
	
	/*
	 * gets the stage's board setting (matrix) 
	 * splits the map into MapFragments AxB according to the matrix size
	 * Separates the start and end of stage
	 */
	private void initializeStage() {
		System.out.println("initializing game fragments");
		stage = gameHandler.getStage();
		int[][] matrix = stage.getBoard();
		mapMatrix = new MapFragment[matrix.length][matrix[0].length];
		
		fragmentSize  = this.getHeight()/matrix.length;
		
		for (int i = 0; i < mapMatrix.length; i++) {
			for (int j = 0; j < mapMatrix[i].length; j++) {
				mapMatrix[i][j] = new MapFragment(i*fragmentSize, j*fragmentSize, fragmentSize, matrix[i][j], i , j, this);
			}
		}
		
		
		gameHandler.setFragmentSize(fragmentSize);
		
		
	}

	/*________________________________________________ display components of the GameMap _______________________________________________________  */
	
	
	private void displayBoard(Graphics2D g2d ) {
		if(mapMatrix!=null) {
			for (int i = 0; i < mapMatrix.length; i++) {
				for (int j = 0; j < mapMatrix.length; j++) {
					mapMatrix[i][j].draw(g2d);
				}
			}	
		}
		
	}
	
	private void displayUnits(Graphics2D g2d) {
		synchronized(gameHandler.unitsLock) {	
			for(Unit unit : units) {
				unit.draw(g2d);
			}
		}
	}
	private void displayHoverRange(Graphics2D g2d) {
		for (int i = 0; i < mapMatrix.length; i++) {
			for (int j = 0; j < mapMatrix[i].length; j++) {
				if(mapMatrix[i][j].getHover()) mapMatrix[i][j].drawHoverRange(g2d);
			}
		}
	}
	public void displayFloatText(Graphics2D g2d) {
		fetchFloatText();
		ArrayList<FloatText> toDel = new ArrayList<>();
		for(FloatText f : floatTexts) {
			if(f.draw(g2d)) toDel.add(f); 
		}
		for(FloatText f : toDel) floatTexts.remove(f);
	}
	

	private void displaySelected(Graphics2D g2d) {
		for (int i = 0; i < mapMatrix.length; i++) {
			for (int j = 0; j < mapMatrix[i].length; j++) {
				mapMatrix[i][j].drawSelected(g2d);
			}
		}
	}

	public MapFragment getFragment(Point point) {
		MapFragment fragment = null;
		for (int i = 0; i < mapMatrix.length; i++) {
			for (int j = 0; j < mapMatrix[i].length; j++) {
				if(mapMatrix[i][j].contains(point)) {
					fragment = mapMatrix[i][j];
					break;
				}
			}
		}
		return fragment;
	}
	
	public void unselectAllFragments() {
		for (int i = 0; i < mapMatrix.length; i++) {
			for (int j = 0; j < mapMatrix[i].length; j++) {
				mapMatrix[i][j].setSelected(false);
			}
		}
	}
	
	
	/*____________________________________________hover stuff _______________________________________________________________*/
	
	public void activateHoverEffect(Building building) {
		hoverListener = new FragmentHoverListener(fragmentSize, mapMatrix.length, mapMatrix[0].length, this);
		this.addMouseMotionListener(hoverListener);
		hoverBuilding = building;
		hoverBuildingImage = building.getIcon().getScaledInstance(fragmentSize, fragmentSize, Image.SCALE_FAST);
		hover = true;

	}
	public void deactivateHoverEffect() {
		drag = false;
		hover = false;
	}
	
	
	
	public void setFragmentHover(int i, int j, boolean b) { mapMatrix[i][j].setHover(b);}
	public void setFragmentHoverDragPath(int i, int j) { mapMatrix[i][j].setDrag(drag); }
	public Building getHoverBuilding() { return hoverBuilding; }
	public Image getHoverBuildingImage() { return hoverBuildingImage; }
	public boolean getHover() { return hover; }
	public void setDrag(boolean b) { drag = b; }
	public int getGold() { return gameHandler.getGold(); }
	
	public void createHoverListener(int x, int y) {
		System.out.println("new hover crated---x: " + x + " y: " + y);
		cleanHover(hoverListener, this);
		if(!drag) hoverListener = new FragmentHoverListener(fragmentSize, mapMatrix.length, mapMatrix[0].length, this);
		else hoverListener = new FragmentHoverListener(fragmentSize, mapMatrix.length, mapMatrix[0].length, this, x, y);
		this.addMouseMotionListener(hoverListener);
	}
	
	
	public void cleanHover(MouseMotionListener mml,	JPanel target) {
		MouseMotionListener[] mmls = target.getMouseMotionListeners();
		for(MouseMotionListener mm : mmls) {
			if(mm.getClass() == mml.getClass()) {
				target.removeMouseMotionListener(mm);
				System.out.println("removed mml");
			}
		}
	}


	public void clearWallPath() {
		for (int i = 0; i < mapMatrix.length; i++) {
			for (int j = 0; j < mapMatrix[i].length; j++) {
				mapMatrix[i][j].setDrag(false);
			}
		}
	}
	
	
	
	public void addFloatText(int x, int y, String text, boolean bad) {
		floatTexts.add(new FloatText(x, y, text, bad));
	}
	
	private void fetchFloatText() {
		ArrayList<FloatText> toadd = gameHandler.fetchFloatText();
		floatTexts.addAll(toadd);
	}
	
	
	
	
	
	
}
