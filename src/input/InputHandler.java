package input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import engine.Engine;
import engine.GameHandler;
import gfx.Gui;


public class InputHandler extends KeyAdapter {
	
	private GameHandler gameHandler;
	private Gui gui;
	
	
	public InputHandler(GameHandler gameHandler, Gui gui) {
		this.gameHandler = gameHandler;
		this.gui = gui;
	}
	

	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyCode();
				
		if(key == KeyEvent.VK_SPACE) gameHandler.pauseGame();
		
		if(key == KeyEvent.VK_ESCAPE) {
			System.out.println("esc pressed");
			gui.removeBuildListener();
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int key = arg0.getKeyCode();
		
 	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	
	
}
