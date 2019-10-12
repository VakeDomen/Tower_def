package gfx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

import javax.swing.JComponent;

import ResourceLoader.ResourceLoader;
import buildings.Building;
import buildings.Cannon;
import engine.VectorHelper;

public class MapFragment{
	private int HEALTH_BAR_HEIGHT = 3;
	
	private boolean selected;
	
	private int coordx;
	private int coordy;
	
	private Building building = null;
	private Image icon = null;
	private Image ground = null;
	private Image bottom = null;
	private Image top = null;
	private Vector<Double> watchPoint;
	private double lastAngle = 0.0;
	
	
	private int size;
	private int i;
	private int j;
	private GameMap gm;
	private boolean hover = false;
	private boolean drag = false;
	
	
	public MapFragment(int coordx, int coordy, Building building, int size , int i, int j, GameMap gm) {
		this.coordx = coordx;
		this.coordy = coordy;
		this.size = size;
		this.ground = getRandomGroundIcon();
		this.building = building;
		this.selected = false;
		if(building instanceof Cannon) {
			this.top = ((Cannon) building).getTop().getScaledInstance(size, size, Image.SCALE_DEFAULT);
			this.bottom = ((Cannon) building).getBottom().getScaledInstance(size, size, Image.SCALE_DEFAULT);
		}else {
			this.icon = building.getIcon().getScaledInstance(size, size, Image.SCALE_DEFAULT);
		}
		this.i = i;
		this.j = j;
		this.gm = gm;
		this.watchPoint = new Vector<>();
		this.watchPoint.add(-1.0);
		this.watchPoint.add(1.0);
		
	}
	
	/*
	 * function: 
	 * 		-1 = end
	 * 		1 = start
	 * 		0 = nouthing/ground
	 */
	public MapFragment(int coordx, int coordy, int size, int function, int i , int j, GameMap gm) {
		this.coordx = coordx;
		this.coordy = coordy;
		this.size = size;
		this.ground = getRandomGroundIcon();
		this.i = i;
		this.j = j;
		this.gm = gm;
		
		
		if(function == -1) this.icon = ResourceLoader.getImage("end_zone.png").getScaledInstance(size, size, Image.SCALE_DEFAULT);
		else if( function == 1) this.icon = ResourceLoader.getImage("start_zone.png").getScaledInstance(size, size, Image.SCALE_DEFAULT);
		
	}
	
	
	
	private Image getRandomGroundIcon() {
		Random r = new Random();
		int index = r.nextInt(13);
		String img = "ground/ground";
		if(index<10) img += "0"+index+".png";
		else img += index+".png";
		return ResourceLoader.getImage(img).getScaledInstance(size, size, Image.SCALE_DEFAULT);
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(ground , coordx, coordy, null);
		if(building != null ) {
			
			
			if(building instanceof Cannon) {
				g2d.drawImage(bottom , coordx, coordy, null);
				
				double newAngle = findAngle();
				
				if(newAngle == 0.0 ) newAngle = lastAngle;
				else lastAngle += newAngle;
				
				AffineTransform backup = g2d.getTransform();
			    AffineTransform a = AffineTransform.getRotateInstance(newAngle, (int)(coordx+size*0.5), (int)(coordy+size*0.5));
			    g2d.setTransform(a);
			    g2d.drawImage(top, coordx, coordy, null);
			    g2d.setTransform(backup);
			    
			}else {
				g2d.drawImage(icon, coordx, coordy, null);
			}
		    
		    drawHealth(g2d);
			

		    
		    
		    
		    
		    
		}else {
			g2d.drawImage(icon, coordx, coordy, null);
		}
		
		
		
		
		if(gm.getHover() && (hover || drag) && building==null) {
			float alpha = 0.5f; //draw half transparent
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
			g2d.setComposite(ac);
			
			g2d.drawImage(gm.getHoverBuildingImage(), coordx, coordy, null);
			
			if(gm.getGold() < gm.getHoverBuilding().getCost()) {
				g2d.setColor(Color.RED);
				g2d.fillRect(coordx, coordy, size, size);
				g2d.setColor(Color.BLACK);
			}
			
			
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
			g2d.setComposite(ac);
			
		}
	}

	public void drawSelected(Graphics2D g2d) {
		if(selected && building != null) {
	    	if(building instanceof Cannon) drawRange(g2d);
	    }
	}
	
	private void drawHealth(Graphics2D g2d) {
		if(building.getHP() < building.getFullHP()) {
			g2d.setColor(Color.RED);
			g2d.fillRect(coordx, coordy, size, HEALTH_BAR_HEIGHT);
			g2d.setColor(Color.GREEN);
			g2d.fillRect(coordx, coordy, (int)(size*(building.getHP()*1.0/building.getFullHP())), HEALTH_BAR_HEIGHT);
			g2d.setColor(Color.BLACK);
		}
	}
	private void drawRange(Graphics2D g2d) {
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
		g2d.setComposite(ac);
		
		g2d.fillOval(coordx+ size/2 - ((Cannon)building).getRange(), coordy + size/2 - ((Cannon)building).getRange(), ((Cannon)building).getRange()*2, ((Cannon)building).getRange()*2);
		
		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		g2d.setComposite(ac);
		
	}
	
	public boolean contains(Point p) {
		if(p != null && p.x > coordx && p.x < coordx+size && p.y > coordy && p.y < coordy+size) return true;
		else return false;
	}
	
	private double findAngle() {
		Vector<Double> a = watchPoint;
		Vector<Double> b = ((Cannon) building).getTargetDirectionVector();
		
		if(b!=null && !a.equals(b)) {
			double angle = VectorHelper.angleBetweenVectors(a, b);
			if((b.get(0) + b.get(1)) > 1) angle = 360-angle;
			return Math.toRadians(angle);
		}else {
			return 0.0;
		}
	}
	
	
	public int getI() { return i; }
	public int getJ() { return j; }
	public void setHover(boolean h) { this.hover = h; }
	public void setDrag(boolean h) { this.drag = h; }
	public int getSize() { return size; }
	public Building getBuilding() { return building; }
	public void removeBuilding() { 
		this.building = null;
		this.icon = ResourceLoader.getImage("rubble.png").getScaledInstance(size, size, Image.SCALE_DEFAULT);
	}

	public void setSelected(boolean b) { this.selected = b; }

	public boolean getHover() {
		return hover;
	}

	public void drawHoverRange(Graphics2D g2d) {
		if(gm.getHoverBuilding() instanceof Cannon) {
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
			g2d.setComposite(ac);
			
			g2d.fillOval(coordx+ size/2 - ((Cannon)gm.getHoverBuilding()).getRange(), coordy + size/2 - ((Cannon)gm.getHoverBuilding()).getRange(), ((Cannon)gm.getHoverBuilding()).getRange()*2, ((Cannon)gm.getHoverBuilding()).getRange()*2);
			
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
			g2d.setComposite(ac);	

		}
	}

	
	
	
}
