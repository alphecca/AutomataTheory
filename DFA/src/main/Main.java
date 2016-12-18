package main;

import java.io.*;
import java.util.*;

import fa.*;

public class Main {
	public static void main(String[] args) throws Exception {
		if(args.length==0){
			System.err.println("Input Filename...");
			System.exit(1);
		}
		
		//1.Construct NFA
		NFA myNFA=new NFA();
		try{
			 BufferedReader br = new BufferedReader(new FileReader(args[0]));
			 String s;
			 
			 while((s=br.readLine())!=null){
				 myNFA.append(s);
			 }
       	br.close();
		}catch(IOException e){
			System.err.println(e);
			System.exit(1);
		}
		//2.Convert DFA from NFA
		DFA myDFA=myNFA.convert();
		System.out.println("<Equivalent DFA table>");
		myDFA.print();
		
		//2. Run NFA & DFA
		System.out.println("<Running NFA & DFA>");
		System.out.println("Input string:");
		
		Scanner br = new Scanner(System.in);
		
		while(true){
			String input = br.nextLine();
			if(input.equals("end")) break;
			else{
				if(!input.matches("[0-1]*"))
					System.out.println("Wrong input...");
				else{
					boolean result1 = myNFA.transition(input);
					boolean result2 = myDFA.transition(input);
					if(result1) System.out.println("[NFA] yes");
					else System.out.println("[NFA] no");
					if(result2) System.out.println("[DFA] yes");
					else System.out.println("[DFA] no");
				}
			}
		}
		br.close();
       
    }

}
