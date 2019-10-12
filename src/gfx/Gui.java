package gfx;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import buildings.Building;
import buildings.Cannon;
import engine.GameHandler;
import input.BuildFragmentSelectListener;
import input.FragmentHoverListener;
import input.InputHandler;
import input.SelectFragment;

public class Gui {
	private int menuW;
	private int menuH;
	private String menuLocation = BorderLayout.EAST;
	private String gameMapLocation = BorderLayout.CENTER;
	
	
	private Menu menu;
	private GameMap gameMap;
	
	JFrame f = null;
	FPS fps = null;
	GameHandler gameHandler;
	
	
	MouseListener buildListener;
	
	
	private final static int FRAME_WIDTH = 1650;
	private final static int FRAME_HEIGHT = 900;
	private final static int FPS_CAP = 300;
	
	
	public Gui( GameHandler gh) {
		this.gameHandler=gh;
		this.menuH = FRAME_HEIGHT;
		this.menuW = (FRAME_WIDTH-FRAME_HEIGHT);
		this.buildListener = null;
	}
	



	
	/*_______________________________________________________ initialization _______________________________________________________________*/
	
	

	public void setup() {
		createComponents();
		editComponents();
		attachComponents();
		f.setVisible(true);	
	}	
	public void initializeFrame() {
		f = new JFrame("TowerDef");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLayout(new BorderLayout());
		f.getContentPane().setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		f.pack();
		fps = new FPS(FPS_CAP);
			
	}
	private void createComponents() {
		menu = new Menu(gameHandler, this, menuW, menuH);
		gameMap = new GameMap(gameHandler);
		
	}
	private void attachComponents() {
		f.add(menuLocation, menu);
		f.add(gameMapLocation, gameMap);
		f.addKeyListener(new InputHandler(gameHandler, this));
		
	}
	private void editComponents() {
		menu.setPreferredSize(new Dimension(menuW, menuH));
		f.getLayout().preferredLayoutSize(f);
		gameMap.addMouseListener(new SelectFragment(gameMap, menu.getSelectedMenu()));
	}
	

	/*__________________________________________ interaction with components ______________________________________________________________*/


	public void selectBuilding(Building building) {
		if(buildListener != null) {
			cleanML(buildListener, gameMap);
			buildListener = null;
		}
		buildListener = new BuildFragmentSelectListener(building, gameMap, gameHandler, this);
		gameMap.addMouseListener(buildListener);
		gameMap.activateHoverEffect(building);
	}
	public void unselectBuilding() {
		menu.unselect();
	}
	
	
	/*__________________________________________ cleaning _________________________________________________________________________________*/


	public void cleanML(MouseListener mouseListener, JPanel target) {
		MouseListener[] listeners = target.getMouseListeners();
		for(MouseListener ml : listeners) {
			if(ml == mouseListener) {
				target.removeMouseListener(ml);
				System.out.println("removed ml");
				break;
			}
		}
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





	public void removeBuildListener() {
		cleanML(buildListener, gameMap);
		gameMap.deactivateHoverEffect();
		unselectBuilding();
	}
	

}
