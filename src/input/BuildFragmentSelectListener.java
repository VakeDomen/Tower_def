package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import buildings.Building;
import buildings.Wall;
import engine.GameHandler;
import gfx.GameMap;
import gfx.Gui;
import gfx.MapFragment;

public class BuildFragmentSelectListener implements MouseListener{
	private Building building;
	private GameMap gameMap;
	private GameHandler gh;
	private Gui gui;
	
	
	private int x1;
	private int y1;
	
	
	public BuildFragmentSelectListener(Building building, GameMap gameMap, GameHandler gh, Gui gui) {
		this.building = building;
		this.gameMap = gameMap;
		this.gh=gh;
		this.gui = gui;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		MapFragment fragment = gameMap.getFragment(arg0.getPoint());
		if (fragment != null && !(building instanceof Wall)) {
			String msg = gh.buildBuilding(fragment.getI(), fragment.getJ(), building);
			gameMap.deactivateHoverEffect();
			gui.unselectBuilding();
			gui.cleanML(this, gameMap);
			if(msg != "") gameMap.addFloatText(fragment.getI()*fragment.getSize(), fragment.getJ()*fragment.getSize(), msg, true);
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		MapFragment fragment = gameMap.getFragment(arg0.getPoint());
		if(fragment!=null && building instanceof Wall) {
			
			gameMap.setDrag(true);
			
			x1 = fragment.getI();
			y1 = fragment.getJ();
			gameMap.createHoverListener(x1, y1);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		MapFragment fragment = gameMap.getFragment(arg0.getPoint());
		
		
		if(fragment!=null && building instanceof Wall) {
			int x2 = fragment.getI();
			int y2 = fragment.getJ();
			
			int is, js, ie, je;
			if(x1 > x2) {
				is = x2; ie = x1; 
			} else { 
				is = x1; ie = x2; 
			}
			if(y1 > y2) {
				js = y2; je = y1;
			} else {
				js = y1; je = y2;
			}
			
			Vector<Building> buildStream = new Vector<>();
			
			
			
			for(int i = is ; i <= ie ; i++) {
				try {
					
					building = building.getClass().newInstance();
					building.setLocation(i, y1, fragment.getSize());
					buildStream.add(building);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
			}
			
			for(int j = js ; j <= je ; j++) {
				try {
					
					building = building.getClass().newInstance();
					building.setLocation(x2, j, fragment.getSize());
					buildStream.add(building);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			
			gh.buildBuildingStream(buildStream);
			
			
			gameMap.setDrag(false);
			gameMap.clearWallPath();
			gui.cleanML(this, gameMap);
			gui.unselectBuilding();
			gameMap.deactivateHoverEffect();
			
		}
		
	}

}
