package fa;
import java.util.*;

import fa.DFAstate;

public class DFA {
	List<DFAstate> DFAlist;
	//Constructor
	public DFA(){
		DFAlist = new ArrayList<DFAstate>();
	}
	public DFA(DFAstate state){
		DFAlist = new ArrayList<DFAstate>();
		DFAlist.add(state);
		
	}
	/**
	 * Check whether this DFA accepts given string or not
	 * @param input string
	 * @return true if DFA accepts given string, otherwise false
	 */
	public boolean transition(String input){
		DFAstate current = DFAlist.get(0);
		DFAstate next = new DFAstate();
		boolean flag = false;
		
		while(input.length()>0){
			char a = input.charAt(0);
			
			if(a=='0')	{
				next = current.trans0;
				if(next.state_num==DFAlist.size()-1) return false;
			}
			else if(a=='1')	{
				next = current.trans1;
				if(next.state_num==DFAlist.size()-1) return false;
			}
			else{
			
				System.out.println("Wrong input string...");
			}
			current=next;
			input=input.substring(1,input.length());
		}
		if(current!=null && current.state_final) flag = true;
		return flag;
	}
	

	/**
	 * print converted DFA
	 */
	public void print(){
		//Print what is initial state		
		System.out.println("state\tfinal\tinput0\tinput1");
		System.out.println("--------------------------------");
		for(Iterator<DFAstate> it=DFAlist.iterator(); it.hasNext(); ){
			DFAstate state =it.next();
			int a =(state.state_final==true)? 1:0;
			System.out.print(state.state_num+"\t"+a+"\t");
			if(state.trans0!=null) System.out.print(state.trans0.state_num+"\t");
			else System.out.print((DFAlist.size()-1)+"\t");
			if(state.trans1!=null) System.out.println(state.trans1.state_num);
			else System.out.println((DFAlist.size()-1)+"\t");
		}
		System.out.println("--------------------------------");
		System.out.println("*NFAstates equals to each DFAstate");
		for(Iterator<DFAstate> it=DFAlist.iterator(); it.hasNext(); ){
			DFAstate state =it.next();
			System.out.print("["+state.state_num+"] : ");
			Iterator<Integer> itr = state.Eqstate.iterator();
			if(!itr.hasNext()) System.out.print("Dead State");
			while(itr.hasNext())
				System.out.print(itr.next()+" "	);
			if(state.state_num==0) System.out.print("-> InitialState");
			System.out.println();
			
		}
	}
	/////////////////////////////////////////SUB METHODS/////////////////////////////////
	public boolean allmarked(){
		
		for(Iterator<DFAstate> itr=DFAlist.iterator(); itr.hasNext(); 	){
			if(!itr.next().mark)
				return false;
		}
		return true;

	}
	/**
	 * Check whether DFA has already have a set equal to {@code set2} 
	 * @param set2
	 * @return If DFA has same set, return the set.
	 * 			Otherwise, return null
	 */
	public DFAstate has(Set<Integer> set2){
		if(set2.isEmpty()) return null;
		for(Iterator<DFAstate> it=DFAlist.iterator(); it.hasNext(); ){
			DFAstate state = it.next();
			Set<Integer> set1 =state.Eqstate;
			/*
			System.out.println("\nset1(already states)num ");
			for(Iterator<Integer> it2=set1.iterator(); it2.hasNext(); ){
				System.out.print(it2.next()+" ");
			}
			System.out.println("\nset2(newly states)num ");
			for(Iterator<Integer> it2=set2.iterator(); it2.hasNext(); ){
				System.out.print(it2.next()+" ");
			}*/
			if(set1.containsAll(set2)&&set2.containsAll(set1)){
				//System.out.println("There are are states already");
				return state; 
			}
		}
		//System.out.println("There are no same state ");
		return null;
	}
	
		
}
