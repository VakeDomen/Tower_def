package maps;

public class Stage {
	public static Stage stage = new Stage();
	
	public static final int X_SIZE = 30;
	public static final int Y_SIZE = 30;
	
	private int[][] boardMatrix;
	
	public Stage(int[] start, int sLen, int [] end, int eLen) {
		this.boardMatrix = new int[X_SIZE][Y_SIZE];
		initializeBoard(start, sLen, end, sLen);
		
	}
	
	public Stage() {}



	public int[][] getBoard(){
		return boardMatrix;
	}
	
	
	
	
	
	
	
	private void initializeBoard(int[] start, int sLen, int [] end, int eLen) {
		
		/* START */
		if(start[0]==0 || start[0]==X_SIZE-1) {
			//vertical start
			for(int i = start[1] ; i<start[1]+sLen ; i++) {
				boardMatrix[start[0]][i] = 1;
			}
		}else if(start[1] == 0 || start[1] == Y_SIZE-1) {
			//horizontal start
			for(int i = start[0] ; i<start[0]+sLen ; i++) {
				boardMatrix[i][start[1]] = 1;
			}
		}
		
		
		/* END */
		if(end[0]==0 || end[0]==X_SIZE-1) {
			//vertical start
			for(int i = end[1] ; i<end[1]+eLen ; i++) {
				boardMatrix[end[0]][i] = -1;
			}
		}else if(end[1] == 0 || end[1] == Y_SIZE-1) {
			//horizontal start
			for(int i = end[0] ; i<end[0]+eLen ; i++) {
				boardMatrix[i][end[1]] = -1;
			}
		}	
	}
	
	
	
	
	
	
	
}
