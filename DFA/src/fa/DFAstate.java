package fa;
import java.util.*;

public class DFAstate {
	int state_num;//defined new
	boolean state_final;
	Set<Integer> Eqstate;
	DFAstate trans0;
	DFAstate trans1;
	
	boolean mark;//for converting from NFA
	
	//constructor
	DFAstate(){
		mark=false;
		Eqstate = new HashSet<Integer>();
		state_final=false;
		trans0=null;
		trans1=null;
	}
	DFAstate(int a, boolean b, Set<Integer> c){
		state_num=a;
		state_final=b;
		Eqstate=c;
		trans0=null;
		trans1=null;
		mark=false;
		
	}
}
