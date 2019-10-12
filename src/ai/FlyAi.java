package ai;

import java.util.Vector;

public class FlyAi extends MovementAi{

	
	
	@Override
	public Vector<Double> predictMove(int[][] board, int x, int y){
		Vector<Double> ret = new Vector<>();
		Vector<Double> end = getClosestEndCoord(board, x, y);
		ret.add((double)(end.get(0)-x));
		ret.add((double)(end.get(1)-y));
		return ret;
	}
}
