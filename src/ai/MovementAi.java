package ai;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.plaf.synth.SynthSpinnerUI;

import aStarAlhorithm.AStarPathFinder;
import aStarAlhorithm.DigraphCoordinates;
import aStarAlhorithm.DigraphNode;
import aStarAlhorithm.DigraphWeightFuntion;
import aStarAlhorithm.EuclidianHeuristicFunction;
import aStarAlhorithm.HeuristicFunction;
import aStarAlhorithm.PreCalculated;
import engine.GameHandler;
import engine.VectorHelper;


public class MovementAi {
	protected DigraphCoordinates coordinates;
	protected DigraphWeightFuntion weightFunction;
	protected List<DigraphNode> graph = null;
	protected PreCalculated paths = null;
	protected ArrayList<DigraphNode> deleted = null;
	
	protected int [][]board = null;
	
	
	protected Object graphLock = new Object();
	protected Object weightsLock = new Object();
	
	protected boolean noPaths = false;
	
	
	
	public Vector<Double> predictMove(int[][] board, int x, int y){
		return predictMoveAStar(board, x, y);
	}
	
	protected Vector<Double> predictMoveAStar(int[][] board, int x, int y){
		
		if(graph != null) {
			Vector<Double> endV = getClosestEndCoord(board, x, y);
			
			DigraphNode start = 	choose(graph, x, y);
			DigraphNode end = 		choose(graph, (int)Math.round(endV.get(0)), (int) Math.round(endV.get(1)));
			
			List<DigraphNode> path = paths.get(start);
			
			
			if(start == null || end == null) return null;
			
			
			if( path == null) {
				//System.out.println("start: " + start + "  emd: " + end);
				//System.out.println("deleted.size: " + deleted.size());
				HeuristicFunction hf = new EuclidianHeuristicFunction(coordinates, deleted);
				synchronized(graphLock) {path = AStarPathFinder.search(start, end, weightFunction, hf); }
				paths.set(start, path);
				//System.out.println("new path found!");
			}
			
			
			
			
			//no possible path
			if(path.isEmpty()) {
				noPaths = true;
				return null;
			}
			try{
				endV.set(0, (double)(path.get(1).getX() - path.get(0).getX()));
				endV.set(1, (double)(path.get(1).getY() - path.get(0).getY()));
			}catch (Exception e) {
				System.out.println("endV: " + endV);
				System.out.println("path: " + path);
				e.printStackTrace();
			}
			
			
			
			return endV;
		}else {
			return null;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	public Vector<Integer> predictMoveToEnd(int[][] board, int x, int y, int chunk) {
		Vector<Vector<Integer>> out = new Vector<>();
		int i = x/chunk;
		int j = y/chunk;
		for (int i2 = 0; i2 < board.length; i2++) {
			for (int j2 = 0; j2 < board.length; j2++) {
				if(board[i2][j2] == -1) {
					Vector<Integer> closestOut = new Vector<>();
					closestOut.add(i2-i);
					closestOut.add(j2-j);
					out.add(closestOut);
				}
			}
		}
		return minimalSum(out);
	}
	
	
	protected Vector<Integer> minimalSum(Vector<Vector<Integer>> vv){
		Vector<Integer> v = new Vector<>();
		v = vv.firstElement();
		for(Vector<Integer> v1 : vv) {
			if(Math.abs(v.get(0))+Math.abs(v.get(1)) >= Math.abs(v1.get(0)) + Math.abs(v1.get(1))) v = v1;
		}
		return v;
	}
	
	
	
	public void intializeAStarPathfinding(int[][] board) {
		/*
		 * create graph nodes
		 */
		
		this.board = board;
		graph = new ArrayList<>(board.length * board[0].length);
		int id = 0;
		for(int i = 0 ; i < board.length ; i++) {
			for (int j = 0; j < board[0].length; j++) {
				graph.add(new DigraphNode(id, i, j));
				id++;
			}
		}
		/*
		 * create node connections
		 */
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				for (int k = -1; k < 2; k++) {
					for (int l = -1; l < 2; l++) {
						if(!(k==0 && l==0)) {
							DigraphNode head = choose(graph, i,j);
							DigraphNode tail = choose(graph, i+k, j+l);
							if(tail != null) head.addNode(tail);
						}
					}
				}
			}
		}
		
		
		
		
		
		
		
		/*
		 * create 2d points corresponding to each node
		 */
		coordinates = new DigraphCoordinates();
		for(DigraphNode node : graph) {
			coordinates.put(node, new Point2D.Double(node.getX()*1.0,node.getY()*1.0));
		}
		
		/*
		 * create weight function
		 */
		weightFunction = new DigraphWeightFuntion();
		for(DigraphNode node : graph) {
			Point2D.Double p1 = coordinates.get(node);
			
			for(DigraphNode child : node.getChildren()) {
				Point2D.Double p2 = coordinates.get(child);
				weightFunction.set(node, child, weightDiagonal(node, child));
			}
		}
		
		/*
		 * creates map for saving paths and array for keepnig track of deleted nodes
		 */
		paths = new PreCalculated();
		deleted = new ArrayList<>();
		
	}
	
	
	protected static DigraphNode choose(List<DigraphNode> list, int i, int j) {
		for(DigraphNode el : list) {
			if(el.compare(i, j)) {
				return el;
			}
		}
		return null;
	}
	
	protected int weightDiagonal(DigraphNode head, DigraphNode tail) {
		int x = Math.abs(head.getX()-tail.getX());
		int y = Math.abs(head.getY()-tail.getY());
		if(x+y == 2) return 14;
		else return 10;
	}
	
	
	
	public Vector<Double> getClosestEndCoord(int[][] board, int x, int y) {
		Vector<Vector<Integer>> out = getAllEndCoords(board);
		for (Vector<Integer> o : out) {
			o.set(0, o.get(0) - x);
			o.set(1, o.get(1) - y);
		}
		Vector<Integer> ret =  minimalSum(out);
		ret.set(0, ret.get(0) + x);
		ret.set(1, ret.get(1) + y);
		
		return VectorHelper.intToDouble(ret);

	}



	protected Vector<Vector<Integer>> getAllEndCoords(int[][] board) {
		Vector<Vector<Integer>> ends = new Vector<>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if(board[i][j] == -1) {
					Vector<Integer> end = new Vector<>();
					end.add(i);
					end.add(j);
					ends.add(end);
				}
			}
		}
		return ends;
	}
	
	
	public void deleteNode(int x, int y) {
		ArrayList<DigraphNode> toDelFromPath = new ArrayList<>();
		toDelFromPath.add(choose(graph, x, y));
		
		ArrayList<DigraphNode> nullNeighbours = new ArrayList<>();
		
		synchronized(graphLock) {
			for(int i = -1 ; i<2 ; i++) {
				for(int j = -1 ; j<2 ; j++) {
					DigraphNode neighbour = choose(graph, x+i, y+j);
					if(!(i==0 && j==0) && inBoard(x, y, i, j)) {
						neighbour.removeChild(toDelFromPath.get(0));
					
					
					
						//brisem povezave da ne morjo hodt po diagonali med dvema towroma
						//brise diagonale okol okol blocka
						if((i+j)%2==1) {
							if(i == 0) {
								if(inBoard(x, y, j, 0)) 		choose(graph, x+i, y+j).removeChild(choose(graph, x+j , y));
								if(inBoard(x, y, -j, 0))		choose(graph, x+i, y+j).removeChild(choose(graph, x-j , y));
							} else {
								if(inBoard(x, y, 0, i)) 		choose(graph, x+i, y+j).removeChild(choose(graph, x+j , y));
								if(inBoard(x, y, 0, -i)) 		choose(graph, x+i, y+j).removeChild(choose(graph, x+j , y));
							}
							toDelFromPath.add(choose(graph, x+i, y+j));
						}
						
					
					}
					
					
					
					
					
				}
			}
			
		}
		paths.removeContainig(toDelFromPath);
		deleted.add(toDelFromPath.get(0));
	}
	
	
	protected boolean inBoard(int x, int y, int i, int j) {
		return x+i >= 0 && y+j >= 0 && y+j<board.length && x+i<board[0].length;
	}
	
	public void addNode(int x, int y) {
		synchronized(graphLock) {
			synchronized(weightsLock) {
				DigraphNode install = choose(graph, x, y);
				if(install != null) {
		
					for(int i = -1 ; i<2 ; i++) {
						for(int j = -1 ; j<2 ; j++) {
							DigraphNode neighbour = choose(graph, x+i, y+j);
							if(neighbour != null && !neighbour.getChildren().contains(install) && !(i==0 && j==0) && inBoard(x, y, i, j)) {
								neighbour.addNode(install);
								
	
								deleted.remove(install);
								
							}
						}
					}
					
					noPaths = false;
					paths = new PreCalculated();
					
				}else {
					System.out.println("node can't be installed");
				}
			}
		}
	}
	

}
