package aStarAlhorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreCalculated {
	private final Map<DigraphNode, List<DigraphNode>> paths = new HashMap<>();
	private Object lock = new Object();
	
	public void set(DigraphNode start, List<DigraphNode> path) {
		synchronized(lock) {
			paths.putIfAbsent(start, path);
		}
			
	}
	
	
	public List<DigraphNode> get(DigraphNode start){
		synchronized(lock) {
			return paths.get(start);
		}
		
	}


	public void removeContainig(ArrayList<DigraphNode> toDel) {
		synchronized(lock) {
			for(DigraphNode d : toDel) {
				List<DigraphNode> del = new ArrayList<>();
				for(DigraphNode key :  paths.keySet()) {
					if(paths.get(key).contains(d)) {
						del.add(key);
					}
				}
				for(DigraphNode dn : del) paths.remove(dn);
			}
		}
		
	}


	public List<DigraphNode> getContainig(DigraphNode start) {
		synchronized(lock) {
			if(paths.get(start) != null) {
				return paths.get(start);
			} else {
				for(DigraphNode key :  paths.keySet()) {
					if(paths.get(key).contains(start)) {
						return paths.get(key);
					}
				}	
			}
		}
		return null;
	}

}
