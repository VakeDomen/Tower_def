package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import buildings.Wall;
import gfx.GameMap;
import gfx.MapFragment;
import gfx.SelectedMenu;

public class SelectFragment implements MouseListener {
	
	GameMap gameMap;
	SelectedMenu selected;
	
	
	public SelectFragment(GameMap gameMap, SelectedMenu selected) {
		this.gameMap = gameMap;
		this.selected = selected;
	}

	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		MapFragment fragment = gameMap.getFragment(arg0.getPoint());
		if (fragment != null) {
			gameMap.unselectAllFragments();
			fragment.setSelected(true);
			selected.setSelected(fragment.getBuilding());
			selected.setShopBuilding(false);
			
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
