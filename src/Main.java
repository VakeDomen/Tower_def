import engine.Engine;
import engine.GameHandler;
import gfx.Gui;

public class Main {
	public static Gui gui;
	public static Engine engine;
	public static Thread game;
	public static GameHandler gameHandler;
	
	public static void main(String[] args) {
		
		//kreira objekt, ki nadzira gameplay igre
		gameHandler = new GameHandler();
		//kreira engine, ki kalkulira vsa dogajanja
		engine = new Engine(gameHandler);
		//gui izrisuje...
		gui = new Gui(gameHandler);
		gui.initializeFrame();
		gui.setup();
		
		
		
		//zacne igro
		game = new Thread( engine );
		game.start();
		
		
		
		
	}
}
