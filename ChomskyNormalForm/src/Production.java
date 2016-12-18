
public class Production {
	
	Character first;
	String second;
	
	//constructor
	Production(String input){
		String[] arg=input.split(":");
		if(arg.length!=2) System.out.println("invalid grammar txt");
		else{
			first=arg[0].charAt(0);			
			second=arg[1];
		}
		
	}
	Production(char V, String V2){
		first=V;
		second=V2;
	}
	//methods
	/**
	 * Check whether given production is unit production, 
	 * unit production: form such as variable->variable
	 * @return true if unit production
	 */
	boolean isUnit(){
		if(second.length()==1)
			if((second.charAt(0)>='a'&&second.charAt(0)<='z')||(second.charAt(0)>='A'&&second.charAt(0)<='Z'))
				return true;
		
		return false;
	}
}