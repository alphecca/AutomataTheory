
import java.util.Map;
import java.util.HashMap;

public class state {
	int state_idx;//actually, don't need
	Map<Character, Integer> next_state=new HashMap<Character,Integer>();
	Map<Character, Character> next_symbol=new HashMap<Character,Character>();
	Map<Character, Integer> move_cursor=new HashMap<Character,Integer>();
	
	state(String input, HashMap<Integer,Character> map){
		
		String[] tokens=input.split(" ");
		state_idx=Integer.parseInt(tokens[0]);
		
		for(int i=1;i<tokens.length;i++){
			
			String[] delim=tokens[i].substring(1, tokens[i].length()-1).split(",");
		
			next_state.put(map.get(i-1).charValue(), Integer.parseInt(delim[0]));
			next_symbol.put(map.get(i-1).charValue(), delim[1].charAt(0));
			if(delim[2].charAt(0)=='R')
				move_cursor.put(map.get(i-1),1);
			else if(delim[2].charAt(0)=='L')
				move_cursor.put(map.get(i-1),-1);
			else
				move_cursor.put(map.get(i-1),0);
		}
	}
	state(int num){
		state_idx=num;
	}
	public int get_NextState(Character symbol){ 
		return next_state.get(symbol);
	}
	public char get_NextSymbol(Character symbol){
		return next_symbol.get(symbol);
	}
	public int get_MoveCursor(Character symbol){
		return move_cursor.get(symbol);
	}
}
