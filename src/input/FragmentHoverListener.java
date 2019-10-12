package input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import gfx.GameMap;

public class FragmentHoverListener implements MouseMotionListener{
	private GameMap gameMap;
	
	private int size;
	
	//dimensions of gameMap...MapFragment-wise
	private int xcount;
	private int ycount;
	
	
	
	private int firstx = -1;
	private int firsty = -1;
	private int lastx = 0;
	private int lasty = 0;
	
	
	public FragmentHoverListener(int size, int xcount, int ycount, GameMap gameMap) {
		this.size = size;
		this.xcount = xcount;
		this.ycount = ycount;
		this.gameMap = gameMap;
		
	}
	public FragmentHoverListener(int size, int xcount, int ycount, GameMap gameMap, int firstx, int firsty) {
		this.size = size;
		this.xcount = xcount;
		this.ycount = ycount;
		this.gameMap = gameMap;
		this.firstx = firstx;
		this.firsty = firsty;
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		int x = p.x/size;
		int y = p.y/size;
		if (x < xcount && y < ycount) {
			if(!(x==lastx && y == lasty)) {
				if(firstx != -1 && firsty != -1) {
					clearWallPath();
					dragWallPath(x, y);
				}
				
			}
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		int x = p.x/size;
		int y = p.y/size;
		if (x < xcount && y < ycount) {
			if(!(x==lastx && y == lasty)) {
				
				gameMap.setFragmentHover(lastx, lasty, false);
				gameMap.setFragmentHover(x, y, true);
				this.lastx = x;
				this.lasty = y;
			}
			
		} else {
			gameMap.setFragmentHover(lastx, lasty, false);
		}
	}
	
	
	
	
	
	
	private void dragWallPath(int x, int y) {
		int is, js, ie, je;
		if(firstx > x) {
			is = x; ie = firstx; 
		} else { 
			is = firstx ; ie = x;
		}
		if(firsty > y) {
			js = y; je = firsty;
		} else {
			js = firsty; je = y;
		}
		
		for(int i = is ; i <= ie  ; i++) {
			gameMap.setFragmentHoverDragPath(i, firsty);
		}
		for(int j = js ; j <= je ; j++) {
			gameMap.setFragmentHoverDragPath(x, j);
		}
		
		
	}
	 private void clearWallPath() {
		 gameMap.clearWallPath();
	 }

	
	
	
}
