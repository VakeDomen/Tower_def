package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import aStarAlhorithm.DigraphCoordinates;
import aStarAlhorithm.DigraphNode;
import aStarAlhorithm.DigraphWeightFuntion;
import aStarAlhorithm.PreCalculated;
import buildings.Building;
import buildings.Cannon;
import engine.VectorHelper;
import units.Unit;

public class BattleAi {
	private DigraphCoordinates coordinates;
	private DigraphWeightFuntion weightFunction;
	private List<DigraphNode> graph = null;
	private PreCalculated paths = null;
	private ArrayList<DigraphNode> deleted = null;
	
	private int [][]board = null;
	
	
	public BattleAi() {
		
	}
	
	
	
	
	public Vector<Double> findDirection(Unit unit, Building target){
		Vector<Integer> dir = new Vector<>();
		dir.add(target.getXcoord()- unit.getX());
		dir.add(target.getYcoord()- unit.getY());
		return VectorHelper.intToDouble(dir);
	}

}
