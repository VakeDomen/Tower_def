package buildings;

import java.awt.Image;
import java.util.Vector;

import engine.VectorHelper;
import units.Unit;

public class Building {


	protected int lvl;
	protected int cost;
	protected int HP;
	protected int fullHP;
	protected int x;
	protected int y;
	protected int coordx;
	protected int coordy;
	protected int key;
	protected String discription;
	
	
	public Building(int cost, int lvl, int HP, int x, int y, int key, String discription) {
		this.cost = cost;
		this.lvl = lvl;
		this.HP = HP;
		this.fullHP = HP;
		this.x = x;
		this.y = y;
		this.key = key;
		this.discription = discription;
	}
	public Building(int cost, int lvl, int HP, int key, String discription) {
		this.cost = cost;
		this.lvl = lvl;
		this.HP = HP;
		this.fullHP = HP;
		this.key = key;
		this.discription = discription;
	}
	
	
	public void hitTarget(int dmg) {
		this.HP-=dmg;
	}
	
	
	
	
	
	
	
	public void setLocation(int x, int y, int fragmentSize) {
		this.x = x;
		this.y = y;
		this.coordx =(int)( x*fragmentSize+fragmentSize*0.5);
		this.coordy =(int)( y*fragmentSize + fragmentSize*0.5);
	}
	
	public Vector<Double> getLocation(){
		Vector<Double> ret = new Vector<>();
		ret.add((double) coordx);
		ret.add((double) coordy);
		return ret;
	}
	
	public int getKey() { return key; }
	public int getX() { return x; }
	public int getY() { return y; }
	public int getCost() { return this.cost; }
	public void setX(int x) { this.x=x; }
	public void setY(int y) { this.y=y; }
	public int getXcoord() { return coordx; }
	public int getYcoord() { return coordy; }
	public int getHP() { return HP; }
	public int getFullHP() { return fullHP; }
	public Image getIcon() { return null; }
	public String getDiscription() { return discription; }

	/*
	 * bubble sort units by distance to this object
	 */
	
	
	protected Vector<Unit> sortByDist(Vector<Unit> v){
		boolean swaped = true;
		while(swaped) {
			swaped = false;
			for (int i = 1; i < v.size(); i++) {
				if(distTo(v.get(i-1)) > distTo(v.get(i))) {
					Unit tmp = v.get(i);
					v.set(i, v.get(i-1));
					v.set(i-1, tmp);
					swaped = true;
					
				}
			}
		}		
		return v;
	}
	
	
	protected int distTo(Unit unit) {
		Vector<Integer> can = new Vector<>();
		Vector<Integer> un = new Vector<>();
		can.add(coordx); can.add(coordy);
		un.add(unit.getX()); un.add(unit.getY());
		return VectorHelper.vectorSize(VectorHelper.minus(can, un));
	}


	
	
	
	
}
