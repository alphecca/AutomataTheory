package fa;
import java.util.*;

public class NFAstate {
	int state_num;
	boolean state_final;
	Vector<Integer> trans0;
	Vector<Integer> trans1;
	Vector<Integer> transE;

	//Constructor
	public NFAstate(){
		state_num=0;
		state_final=false;
		trans0= new Vector<Integer>();
		trans1= new Vector<Integer>();
		transE= new Vector<Integer>();
	}
	public NFAstate(String input){
		//System.out.println("input :"+input);
		trans0 = new Vector<Integer>();
		trans1 = new Vector<Integer>();
		transE = new Vector<Integer>();
		String[] parts = input.split("\\s+");
		//System.out.println("parts length:"+parts.length);
		state_num=Integer.parseInt(parts[0]);
		state_final=(parts[1].equals("0"))? false:true;
		
		//add state transition
		if(!parts[2].contains("-")){
			String[] tmp=new String[0];
			if(parts[2].contains(",")){
				tmp=parts[2].split(",");
				for(int i=0;i<tmp.length;i++)
					trans0.add(Integer.parseInt(tmp[i]));
			}
			else trans0.add(Integer.parseInt(parts[2]));
		}
		if(!parts[3].contains("-")){
			if(parts[3].contains(",")){
				String[] tmp=parts[3].split(",");
				for(int i=0;i<tmp.length;i++)
					trans1.add(Integer.parseInt(tmp[i]));
			}
			else trans1.add(Integer.parseInt(parts[3]));
			
		}

		if(!parts[4].contains("-")){
			String[] tmp=new String[0];
			if(parts[4].contains(",")){
				tmp=parts[4].split(",");
				for(int i=0;i<tmp.length;i++)
					transE.add(Integer.parseInt(tmp[i]));				
			}
			else transE.add(Integer.parseInt(parts[4]));
			
			}
	//	System.out.println("@"+state_num+"@"+state_final);
	}
	
}
