import java.lang.reflect.Array;
import java.util.*;

public class PlayerImpl implements Player {
	// Identifies the player
	private int name = 0;
	int n = 0;

	// Constructor
	public PlayerImpl(int name, int n) {
		this.name = 0;
		this.n = n;
	}

	// Function to find possible successors
	@Override
	public ArrayList<Integer> generateSuccessors(int lastMove, int[] crossedList) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		
		if(lastMove == -1){
			for(int i=(n-1)/2; i >= 1; i--){
				if(i%2 == 0){
					ret.add(i);
				}
			}
		}
		
		else{
			for(int i=n; i >= 1; i--){
				if(crossedList[i] == 0){
					if(i%lastMove == 0 || lastMove %i == 0){
						ret.add(i);
					}				
				}
			}	
		}
		return ret;
	}

	
	// The max value function
	@Override
	public int max_value(GameState s) {
		ArrayList<Integer> succ = generateSuccessors(s.lastMove, s.crossedList);
		if(succ.size() == 0){
			s.leaf = true;
			return -1;
		}
		int ret  = -100;
		int bestMove =- 100;
		for(Integer i : succ){
			int new_crossedList[] = s.crossedList.clone();
			new_crossedList[i] = 1;
			GameState newState = new GameState(new_crossedList, i);
			int theMin = min_value(newState);
			if(ret<theMin){
				ret = theMin;
				bestMove = i;
			}
			if(ret==1) 
				break;
		}
		s.bestMove=bestMove;
		return ret;
	}

	
	// The min value function
	@Override
	public int min_value(GameState s) {
		ArrayList<Integer> succ = generateSuccessors(s.lastMove, s.crossedList);
		if(succ.size() == 0){
			s.leaf = true;
			return 1;
		}
		int ret  = 100;
		int bestMove = 100;
		for(Integer i : succ){
			int new_crossedList[] = s.crossedList.clone();
			new_crossedList[i] = 1;
			GameState newState = new GameState(new_crossedList, i);
			int theMax = max_value(newState);
			if(ret>theMax){
				ret = theMax;
				bestMove = i;
			}
			if(ret==-1)
				break;
		}
		s.bestMove = bestMove;
		return ret;
	}


	// Function to find the next best move
	@Override
	public int move(int lastMove, int[] crossedList) {
		GameState s = new GameState(crossedList, lastMove);
		max_value(s);
		if(s.leaf){
			return -1;
		}
		else{
			return s.bestMove;
		}
	}
}