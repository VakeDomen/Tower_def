package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import buildings.Cannon;
import buildings.SimpleCannon;
import engine.GameHandler;
import gfx.MenuBuildingFragment;
import gfx.Gui;
import gfx.Menu;

public class BuildMouseListener implements MouseListener{
	
	Vector<MenuBuildingFragment> cannons;
	Gui gui;
	Menu menu;
	
	MenuBuildingFragment selected;
	
	
	public BuildMouseListener(Vector<MenuBuildingFragment> cannons, Gui gui, Menu menu) {
		this.cannons = cannons;
		this.gui = gui;
		this.menu = menu;
		selected = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(MenuBuildingFragment c : cannons) {
			if(c.contains(e.getPoint())) {
				try {
					gui.selectBuilding(c.getBuilding().getClass().newInstance());
					if(selected!=null) selected.setSelected(false);
					selected = menu.iconClick(c.getBuilding());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
			
	}

}
