package gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import javax.swing.JPanel;

import ResourceLoader.ResourceLoader;
import buildings.Building;
import buildings.Cannon;
import engine.GameHandler;
import input.BuildMouseListener;

public class Menu extends JPanel{
	private static final int CANNON_ICON_OFFSET = 60;
	private static final int CANNON_ICON_SIZE = 60;
	private String img = "menu.png";
	private Image background;
	private int xsize;
	private int ysize;
	
	
	
	
	
	
	
	
	private Gui parent;
	private StatDisplay statDisplay;
	private SelectedMenu selectedMenu;
	private GameHandler gameHandler;
	private Vector<Building> unlockedCannons;
	private Vector<MenuBuildingFragment> buildingIcons;
	
	public Menu(GameHandler gameHandler, Gui parent, int xsize, int ysize) {
		System.out.println("Creating menu...");
		this.gameHandler = gameHandler;
		this.unlockedCannons = new Vector<>();
		this.buildingIcons = new Vector<>();
		this.parent = parent;
		this.xsize = xsize;
		this.ysize = ysize;
		this.background = ResourceLoader.getImage(img).getScaledInstance(xsize, ysize, Image.SCALE_DEFAULT);
		Font font = new Font("SansSerif", Font.BOLD, 25);
		this.setFont(font);
		unlockedCannons = gameHandler.getBuildingList();
		createCannons();
		this.addMouseListener(new BuildMouseListener(buildingIcons, parent, this));
		this.statDisplay = new StatDisplay((int)(ysize*0.66));
		this.selectedMenu =  new SelectedMenu(0, (int)Math.round(ysize*0.35) , xsize, (int)Math.round(ysize*0.3));
	}
	
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setBackground(Color.BLACK);
		g2d.drawImage(background, 0, 0, null);
		
		displayCannons(g2d);
		
		
		displaySelectedMenu(g2d);
		displayStatInfo(g2d);
		

		super.repaint();
	}
	
	
	
	
	
	private void displayCannons(Graphics2D g2d) {
		for(int i = 0 ; i < buildingIcons.size() ; i++) {
			buildingIcons.get(i).draw(g2d);
		}
	}
	
	private void displayStatInfo(Graphics2D g2d) {
		int gold = gameHandler.getGold();
		int health = gameHandler.getHealth();
		statDisplay.draw(g2d, gold, health);
	}
	private void displaySelectedMenu(Graphics2D g2d) {
		selectedMenu.draw(g2d);
	}
	
	private void createCannons() {
		buildingIcons.clear();
		int i = 0;
		for(Building c : unlockedCannons) {
			buildingIcons.add(new MenuBuildingFragment((CANNON_ICON_OFFSET+CANNON_ICON_SIZE)*(i)+CANNON_ICON_OFFSET, CANNON_ICON_OFFSET, c, CANNON_ICON_SIZE));
			i++;
		}		
	}
	
	
	public MenuBuildingFragment iconClick(Building building) {
		for(MenuBuildingFragment fr : buildingIcons) {
			if(fr.getBuilding() == building) {
				fr.setSelected(true);
				selectedMenu.setSelected(fr.getBuilding());
				selectedMenu.setShopBuilding(true);
				return fr;
			}
		}
		return null;
	}
	public void unselect() {
		for(MenuBuildingFragment fr : buildingIcons) {
			fr.setSelected(false);
		}
		selectedMenu.setSelected(null);
	}
	
	
	
	
	
	public SelectedMenu getSelectedMenu() { return selectedMenu; }
	
	
}
