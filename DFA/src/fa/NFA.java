package fa;
import java.util.*;

public class NFA {
	private List<NFAstate> NFAlist;
	
	//Constructor
	public NFA(){
		NFAlist=new ArrayList<NFAstate>();
	}
	//Methods
	/**
	 * Construct NFA(NFA list) input file's line by line
	 * @param one line of file text
	 */
	public void append(String line){
		NFAstate state=new NFAstate(line);
		NFAlist.add(state);
		//System.out.println("state["+state.state_num+"]\n"+state.trans0+"\n"+state.trans1);
	}
	/**Check whether this NFA accepts given string or not
	 * 
	 * @param input string
	 * @return true if accepts, otherwise false
	 */
	public boolean transition(String input){
	
		Set<Integer> current=new HashSet<Integer>();
		current=getEqclass(0);
		//current.add(0);
		boolean flag = false;
		/*
		System.out.println("##current");
		for(Iterator<Integer> itr1 = current.iterator(); itr1.hasNext(); ){
			System.out.print(itr1.next()+" ");
		}
		*/
		
		if(input.length()==0){//empty string;
			for(Iterator<Integer> itr1 = current.iterator(); itr1.hasNext(); ){
				if(getState(itr1.next()).state_final) return true;
			}
		}
		while(input.length()>0){
			char a = input.charAt(0);
			Set<Integer> next=new HashSet<Integer>();
			
			//System.out.println("##[input] "+a);
			
			for(Iterator<Integer> itr = current.iterator(); itr.hasNext(); ){
				
				Integer currentstate= itr.next();
				
				if(a=='0'){
					Iterator<Integer> itr1 = getState(currentstate).trans0.iterator();
					while(itr1.hasNext()){
						next.add(itr1.next());
						//System.out.println("next trans state"+tmp);
						//next.add(tmp);
					}
				}
				else if(a=='1'){
					//System.out.println("for currentstate "+currentstate+" \t\ttrans1 size "+getState(currentstate).trans1.size());
					Iterator<Integer> itr1 = getState(currentstate).trans1.iterator();
					while(itr1.hasNext()){
						next.add(itr1.next());
						//System.out.println("next trans state"+tmp);
						//next.add(tmp);
						
					}
					
				}
				else{
					System.out.println("Wrong input string...");
				}
				
				
								
			}
			current = getEqclass(next);
			input=input.substring(1, input.length());
		}
		
		//Check final state
		for(Iterator<Integer> it=current.iterator(); it.hasNext(); ){
			if(getState(it.next()).state_final) flag = true;
		}
		return flag;
	}
	
	/**
	 * Convert NFA to DFA
	 * @return converted DFA, equivalent to NFA
	 */
	public DFA convert(){
		
		int cnt=0;//Numbering of DFA
		boolean finflag=false;
		Set<Integer> current = getEqclass(0);//epsilon closure.
		
		if(hasfin(current)) finflag=true;
		DFAstate initialstate=new DFAstate(cnt++, finflag, current);
		DFA mydfa=new DFA(initialstate);
		
		DFAstate CURR= mydfa.DFAlist.get(0);
		DFAstate DEAD= new DFAstate();
		DEAD.mark=true;
	        boolean needDead=false;

		while(!mydfa.allmarked()){
			
			for(Iterator<DFAstate> it=mydfa.DFAlist.iterator();it.hasNext(); ){
				CURR=it.next();
                                current = CURR.Eqstate;
                                if(!CURR.mark){
                                    Set<Integer> nextset1 = Move(current, 0);
                                    Set<Integer> nextset2 = Move(current, 1);
                                    if(!nextset1.isEmpty()){
                                        DFAstate pos1= mydfa.has(nextset1);
                                        if(pos1!=null) CURR.trans0=pos1;
                                        if(pos1==null){
                                            if(hasfin(nextset1)) finflag=true;
                                            else finflag=false;
                                            DFAstate NEXT=new DFAstate(cnt++, finflag, nextset1);
                                            CURR.trans0=NEXT;
                                            mydfa.DFAlist.add(NEXT);
                                        }
                                    }
                                    else{
                                        CURR.trans0=DEAD;
                                        needDead=true;
                                    }

                                    if(!nextset2.isEmpty()){
                                        DFAstate pos2= mydfa.has(nextset2);
						if(pos2!=null) CURR.trans1=pos2;
						if(pos2==null){
							if(hasfin(nextset2)) finflag=true;
							else finflag=false;
							DFAstate NEXT=new DFAstate(cnt++, finflag, nextset2);
							CURR.trans1=NEXT;
							mydfa.DFAlist.add(NEXT);
						}
					}
					else{
						CURR.trans1=DEAD;
                                                needDead=true;
                                        }
					
					CURR.mark=true;
					break;
					
				}
			}
		}//end of while
		
		DEAD.state_num=cnt++;
		if(needDead) mydfa.DFAlist.add(DEAD);
		checkfin(mydfa);
		return mydfa;
	}
	////////////////////////////////SUB METHODS////////////////////////////////////
	/**
	 * Ensure whether each DFA states is final or not.
	 * @param DFA
	 */
	public void checkfin(DFA dfa){
		for(Iterator<DFAstate> it= dfa.DFAlist.iterator();it.hasNext(); 	){
			DFAstate state=it.next();
			//System.out.println("!!");
			for(Iterator<Integer> itr=state.Eqstate.iterator();itr.hasNext(); ){
				Integer a = itr.next();
				//System.out.print(a+" "+getState(a).trans0.size());
				if(getState(a).state_final){
					//System.out.println("this state is true");
					state.state_final=true;
				}
			}
		}	

		}
	
	/**
	 * For a set of current states, transit once depending on the input
	 * @param a set of current states
	 * @param one character of input
	 * @return a set of next states
	 */
	public Set<Integer> Move(Set<Integer> curr, int input ){
		//System.out.println("transit next state "+curr.size());
		
		Set<Integer> next = new HashSet<Integer>();
		for(Iterator<Integer> it=curr.iterator(); it.hasNext(); ){
			Integer a = it.next();
			
			Vector<Integer> b=(input==0)? getState(a).trans0: getState(a).trans1;
			
			//System.out.println("**transition: ");
			for(Iterator<Integer> it2=b.iterator(); it2.hasNext(); ){
				Integer tmp=it2.next();
				///System.out.println(tmp	+" ");
				next.add(tmp);
			}
			//System.out.println();
			
		}
		next=getEqclass(next);
		/*
		System.out.println("@@@SHOULDBESAME");
		for(Iterator<Integer> it=next.iterator(); it.hasNext(); ){
			System.out.print(it.next()+" ");
		}
		*/
		
		return next; 
	}
	/**
	 * if given set has final states, return true
	 * @param a set
	 * @return true if a set contains final states
	 */
	public boolean hasfin(Set<Integer> a){
		boolean flag = false;
		for(Iterator<Integer> it=a.iterator(); it.hasNext(); ){
			Integer b = it.next();
			if(getState(b).state_final) flag=true;
		}
		return flag;
	}
	/**
	 * Find a state whose state_num is {@code Integer a}
	 * @param a
	 * @return an NFA states matching to a 
	 */
	public NFAstate getState(Integer a){
		//System.out.println("[getState]"+a.intValue());
		
		NFAstate b = new NFAstate();
		Iterator<NFAstate> itr = NFAlist.iterator();
		while(itr.hasNext()){
			NFAstate c = itr.next();
			//System.out.println(c.state_num+"((");
			if(c.state_num==a.intValue())
				b=c;
		}
		//System.out.println("b is "+b.state_num);
		//System.out.println("b has "+b.trans0.size()+"\t"+b.trans1.size()+"\t"+b.transE.size());
		return b;//How to process error case?
		
	}
	/**
	 * Get equal class of a state whose number is {@code Integer a}
	 * @param Integer a
	 * @return Equivalent set of a NFA state
	 */
	public Set<Integer> getEqclass(Integer a){
		//System.out.println("getEqclass");
		Queue<Integer> queue = new LinkedList<Integer>();
		Queue<Integer> queue2 = new LinkedList<Integer>();//To check if state numbers in queue already exists
		queue.add(a);
		while(!queue.isEmpty()){
			Integer b =queue.remove();
			Iterator<Integer> tmp = getState(b).transE.iterator();
			//System.out.println(b+"'s transE :" );
			while(tmp.hasNext()){
				Integer tmp2=tmp.next();
				//System.out.print(lll+" ");
				if(queue2.contains(tmp2)) continue;
				else queue.add(tmp2);
			}
			//System.out.println();
			queue2.add(b);
		}
		Set<Integer> Eqclass=new HashSet<Integer>(a);
		while(!queue2.isEmpty()) Eqclass.add(queue2.remove());
		return Eqclass; 
	
	}
	
	public Set<Integer> getEqclass(Set<Integer> a){
		Set<Integer> result = new HashSet<Integer>();
		for(Iterator<Integer> itr1 = a.iterator(); itr1.hasNext(); ){
			Integer tmp = itr1.next();
			for(Iterator<Integer> itr2 = getEqclass(tmp).iterator(); itr2.hasNext(); ){
				while(itr2.hasNext()){
					result.add(itr2.next());
				}
			}
		}
		
		return result;
	}
}
