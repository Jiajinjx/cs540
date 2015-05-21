import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class AStarSearchImpl implements AStarSearch {
	
	@Override	
	public SearchResult search(String initConfig, int modeFlag) {
		// TODO Add your code here
			   
		// build the priority queue (sorting with Comparator)
		PriorityQueue<State> pqState = new PriorityQueue<State>(11, State.comparator);
		HashMap<String, Integer> stateIndex = new HashMap<String, Integer> ();
		State state = new State(initConfig, 0, getHeuristicCost(initConfig, modeFlag), "");
		pqState.add(state);
		stateIndex.put(initConfig, state.realCost + state.heuristicCost);
		int numCount = 0;
		char[] moveSet = {'A','B','C','D','E','F','G','H'};
		while (pqState.size() > 0)
		{
			state = pqState.poll();
			numCount++;
			if (checkGoal(state.config))
			{
				SearchResult result = new SearchResult(state.config, state.opSequence, numCount);
				return result;
			}
			for (char op: moveSet)
			{
				String nextConfig = move(state.config, op);
				State newState = new State(nextConfig, state.realCost + 1, getHeuristicCost(nextConfig, modeFlag), state.opSequence + op);
				if (!stateIndex.containsKey(nextConfig))
				{
					pqState.add(newState);
					stateIndex.put(nextConfig, newState.realCost + newState.heuristicCost);
				}
				else
				{
					int oldValue = stateIndex.get(nextConfig);
					if (newState.realCost + newState.heuristicCost < oldValue)
					{
						pqState.remove(newState);
						pqState.add(newState);
						stateIndex.remove(nextConfig);
						stateIndex.put(nextConfig, newState.realCost  + newState.heuristicCost);
					}
				}
			}
		}
		return null;
	}

	
	
	
	@Override
	public boolean checkGoal(String config) {
		// TODO Add your code here
	
		if(	
				config.charAt(6)==config.charAt(7) &&
				config.charAt(6)==config.charAt(8) &&
				config.charAt(6)==config.charAt(11) &&
				config.charAt(6)==config.charAt(12) &&
				config.charAt(6)==config.charAt(15) &&
				config.charAt(6)==config.charAt(16) &&
				config.charAt(6)==config.charAt(17))
		{
			
		    return true;
		}
		else{
			return false;
		}
	}

	@Override
	public String move(String config, char op) {
		// TODO Add your code here
		char[] configArray = config.toCharArray();
		
		if (op == 'A'){
			char temp = configArray[0];
			configArray[0] = configArray[2];
			configArray[2] = configArray[6];
			configArray[6] = configArray[11];
			configArray[11] = configArray[15];
			configArray[15] = configArray[20];
			configArray[20] = configArray[22];
			configArray[22] = temp;
		}	
		
		else if (op == 'F'){
			char temp = configArray[22];
			configArray[22] = configArray[20];
			configArray[20] = configArray[15];
			configArray[15] = configArray[11];
			configArray[11] = configArray[6];
			configArray[6] = configArray[2];
			configArray[2] = configArray[0];
			configArray[0] = temp;
		}
		
		else if (op == 'B'){
			char temp = configArray[1];
			configArray[1] = configArray[3];
			configArray[3] = configArray[8];
			configArray[8] = configArray[12];
			configArray[12] = configArray[17];
			configArray[17] = configArray[21];
			configArray[21] = configArray[23];
			configArray[23] = temp;
		}
		
		else if (op == 'E'){
			char temp = configArray[23];
			configArray[23] = configArray[21];
			configArray[21] = configArray[17];
			configArray[17] = configArray[12];
			configArray[12] = configArray[8];
			configArray[8] = configArray[3];
			configArray[3] = configArray[1];
			configArray[1] = temp;
		}
		
		else if (op == 'H'){
			char temp = configArray[4];
			configArray[4] = configArray[5];
			configArray[5] = configArray[6];
			configArray[6] = configArray[7];
			configArray[7] = configArray[8];
			configArray[8] = configArray[9];
			configArray[9] = configArray[10];
			configArray[10] = temp;
		}
		
		else if (op == 'C'){
			char temp = configArray[10];
			configArray[10] = configArray[9];
			configArray[9] = configArray[8];
			configArray[8] = configArray[7];
			configArray[7] = configArray[6];
			configArray[6] = configArray[5];
			configArray[5] = configArray[4];
			configArray[4] = temp;
		}	
		
		else if (op == 'G'){
			char temp = configArray[13];
			configArray[13] = configArray[14];
			configArray[14] = configArray[15];
			configArray[15] = configArray[16];
			configArray[16] = configArray[17];
			configArray[17] = configArray[18];
			configArray[18] = configArray[19];
			configArray[19] = temp;
		}
		
		else if (op == 'D'){
			char temp = configArray[19];
			configArray[19] = configArray[18];
			configArray[18] = configArray[17];
			configArray[17] = configArray[16];
			configArray[16] = configArray[15];
			configArray[15] = configArray[14];
			configArray[14] = configArray[13];
			configArray[13] = temp;
		}
			
		config = String.valueOf(configArray);
		return config;
	}

	@Override
	public int getHeuristicCost(String config, int modeFlag) {		
		// TODO Add your code here
		int HeuristicCost = 0;
		int n1 = 0;
		int n2 = 0;
		int n3 = 0;
		char[] configArray = config.toCharArray();
		if(modeFlag == 1){
			
			//the index for the center square is 6,7,8,11,12,15,16,17
			for(int i = 6; i < 9; i++){
				if(configArray[i] == '1') { n1 ++; }
				else if(configArray[i] == '2') { n2 ++; }
				else{ n3 ++; }	
			}
			for(int i = 11; i < 13; i++){
				if(configArray[i] == '1') { n1 ++; }
				else if(configArray[i] == '2') { n2 ++; }
				else{ n3 ++; }		
			}
			for(int i = 15; i < 18; i++){
				if(configArray[i] == '1') { n1 ++; }
				else if(configArray[i] == '2') { n2 ++; }
				else{ n3 ++; }			
			}
			
			//max = max(n1,n2,n3)
			int temp = Math.max(n1,n2);
			int max= Math.max(temp, n3);
			
			//heuristic function = 8- max(n1,n2,n3)
			HeuristicCost = 8 - max;
		}
		else if (modeFlag == 2){
			HeuristicCost = 0;
		}
		else{// modeFlag = 3 DIY heuristic, use the min counters of the center_square, then this element has the max outofCounter number, in another way, the min outofCounter element has the max center square counter.
			//equivalent to modeFlag==1
			int total = 24;
			int temp = Math.max(n1,n2);
			int max = Math.max(temp, n3);
			total = 24-max;
			HeuristicCost = total;
			
		}
		return HeuristicCost;
	}
	

}
