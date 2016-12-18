import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
public class hw3 {
	public static int TM_flag;
	public static int halting_N;
	public static int halting_Y;
	public static HashMap<Integer,Character> map=new HashMap<Integer,Character>();
	public static ArrayList<Character> tape=new ArrayList<Character>();
	public static ArrayList<state> TMstate=new ArrayList<state>();
	
	public static void main(String args[]) throws IOException{
		if(args.length!=3){
			System.out.println("Enter txt files...");
			System.exit(1);
		}
		int state_num=0;
		boolean flag1=false;
		 BufferedReader fw1 = new BufferedReader(new FileReader(args[0]));
        while(true) {
        	String line=fw1.readLine();
        	if (line==null) break;
        	
        	if(!flag1){
        		flag1=true;
        		String[] token= line.split(" ");
        		state_num=Integer.parseInt(token[1]);
        		TM_flag=Integer.parseInt(token[2]);
        		
        		line=fw1.readLine();
        		String[] Stoken=line.split(" ");
        		for(int i=0;i<Stoken.length;i++){
        			map.put(i, Stoken[i].charAt(0));
        		}  
        	}
        	else
        		TMstate.add(new state(line, map));
        }
       fw1.close();
       
       halting_N=state_num;
       halting_Y=state_num+1;
       TMstate.add(new state(state_num));
       TMstate.add(new state(state_num+1));
       
       BufferedReader fw2 = new BufferedReader(new FileReader(args[1]));
       FileWriter fw3 = new FileWriter(args[2]);
       while(true) {
         String line = fw2.readLine();
         if (line==null) break;
         
         tape.clear();
         tape.add('#');
           for(int i=0;i<line.length();i++){
        	   tape.add(line.charAt(i));
           }
        // tape.add('#');
         boolean result=Turing(line);

         if(TM_flag==0){//text TM
        	 if(result) fw3.write("y\n");
        	 else fw3.write("n\n");
        	 for(int i=1;i<tape.size();i++){
        		 if(tape.get(i).equals('#')) continue;
        		 fw3.write(tape.get(i));
        	 }
    	  	fw3.write("\n\n");
          }else{//function TM
        	   for(int i=1;i<tape.size();i++){
	         		  if(tape.get(i).equals('#')) continue;
	         		  fw3.write(tape.get(i));
        	   }
	         fw3.write("\n\n");   
           }
       }
	   fw2.close();
	   fw3.close();		
	}
	public static boolean Turing(String line){
		/*
		System.out.println("//////////////////////////////////////");
		System.out.println("Initial tape: ");
		for(int i=0;i<tape.size();i++)
			System.out.print(tape.get(i)+" ");
		System.out.println();
	*/
		int cursor=0;
		int curr_state=0;
		Character curr_char;
		
		while(curr_state!=halting_N&&curr_state!=halting_Y){
			//int fordebug=curr_state;
			curr_char=tape.remove(cursor);
			if(curr_char.equals('#')) tape.add('#');
			tape.add(cursor, TMstate.get(curr_state).get_NextSymbol(curr_char));
			cursor+=TMstate.get(curr_state).get_MoveCursor(curr_char);
			curr_state=TMstate.get(curr_state).get_NextState(curr_char);
			
			/*
			if(fordebug==8||fordebug==3||fordebug==6||fordebug==13||fordebug==14){
				for(int i=0;i<tape.size();i++)
					System.out.print(tape.get(i)+" ");
				System.out.println();
				for(int i=0;i<cursor;i++)
					System.out.print("  ");
				System.out.println(cursor+"^");
				System.out.println("next state: "+curr_state);
			}
			*/
		}
		if(curr_state==halting_N) return false;
		else return true;
	}

}
