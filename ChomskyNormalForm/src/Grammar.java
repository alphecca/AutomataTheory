import java.io.*;
import java.util.*;

public class Grammar {
    static Character start_symbol;
    static LinkedHashMap<Character, Boolean> literal_symbol=new LinkedHashMap<Character,Boolean>();
    static LinkedList<Production> CFG = new LinkedList<Production>();
    static LinkedList<Production> CNF = new LinkedList<Production>();
    //constructor
    Grammar(String data) throws IOException{
        //initialize literal_symbol table
        for(int i=0;i<26;i++)
            literal_symbol.put(Character.valueOf((char)('A'+i)), false);
        for(int i=0;i<26;i++)
            literal_symbol.put(Character.valueOf((char)('a'+i)), false);

        //make CFG
        boolean flag=true;
        BufferedReader br= new BufferedReader(new FileReader(data));
        while(true){
            String line= br.readLine();

            if(line!=null){
                Production product=new Production(line);
                CFG.add(product);
                //mark used literal
                literal_symbol.put(product.first, true);
                if(flag){
                    start_symbol=product.first;
                    flag=false;
                }
            }
            else break;			
        }
        br.close();
    }	

    //////////////////////////MAIN_METHOD//////////////////////////////////////
    /**
     * Convert from CFG to CNF
     */
    void convert(){
        LinkedList<Production> P1=new LinkedList<Production>();
        LinkedList<Production> P2=new LinkedList<Production>();
        LinkedList<Production> P3=new LinkedList<Production>();
        //1.remove unit production
        //1-1. Put not unit productions in P1
        ListIterator<Production> itr_cfg=CFG.listIterator();
        while(itr_cfg.hasNext()){
            Production tmp=itr_cfg.next();
            if(tmp.isUnit()){
                P2.add(tmp);
            }
            else{
                P1.add(tmp);
            }
        }
        //1-2. Find all unit productions and put them in P2
        if(!P2.isEmpty()){
            for(int i=0;i<P2.size();i++){
                Production curr=P2.get(i);
                Set<Character> curr_before=find_unit(P2, curr.first);
                Set<Character> curr_tmp=find_unit(P2, curr.second.charAt(0));
                for(Iterator<Character> itr_tmp=curr_tmp.iterator();itr_tmp.hasNext();){
                    boolean flag=false;
                    Character tmp=itr_tmp.next();
                    for(Iterator<Character> itr_before=curr_before.iterator();itr_before.hasNext(); ){
                        Character before=itr_before.next();
                        if(tmp.equals(before)) flag=true;
                    }
                    if(!flag){
                        if(!tmp.equals(curr.first)){//if S->S, remove it
                            P2.add(new Production(curr.first,Character.toString(tmp)));
                        }	
                    }
                }
            }
            //1-3. Push from P2 to P3
            for(int i=0;i<P2.size();i++){
                Production curr=P2.get(i);
                Set<String> origin=find(P1, curr.first);
                Set<String> result=find(P1, curr.second.charAt(0));
                for(Iterator<String> itr_result= result.iterator();itr_result.hasNext();){
                    String foo=itr_result.next();
                    boolean flag=false;
                    for(Iterator<String> itr_origin=origin.iterator();itr_origin.hasNext(); ){
                        if(itr_origin.next().equals(foo)) flag=true; break;
                    }
                    if(!flag) P3.add(new Production(curr.first, foo)); 
                }
            }

        }

        //1-4. Union P1 and P3
        ListIterator<Production> itr_p3=P3.listIterator();
        while(itr_p3.hasNext()){
            P1.add(itr_p3.next());
        }
        //2.change to CNF
        while(P1.size()>0){
            Production curr=P1.removeFirst();
            //2-1. if production.length()==1, move into CNF intact
            if((curr.second.length()==1)||(curr.second.length()==2&&check(curr.second))){
                CNF.add(curr);
            }
            //2-3.from at(1)~end, replace into new Variable& update
            else{
                String pos=curr.second;
                for(int j=0;j<pos.length();j++){
                    char tmp=pos.charAt(j);
                    if(tmp<'A'||(tmp>'Z'&&tmp<'a')||tmp>'z'){
                        Character V=give_literal();
                        pos=pos.replace(tmp, V);
                        P1=change(P1, tmp, V);
                        P1.add(new Production(V, Character.toString(tmp)));

                    }
                }
                if(pos.length()>2){					
                    char V=give_literal();
                    String tmp=pos.substring(1);
                    P1=change(P1, tmp, V);
                    pos=pos.replaceAll(tmp, Character.toString(V));
                    P1.add(new Production(V, tmp));
                }
                CNF.add(new Production(curr.first, pos));
            }

        }

        //3.Print CNF
        for(int i=0;i<P1.size();i++)
            CNF.add(P1.get(i));
        printing();
    }

    boolean CYK(String input){
        int SIZE=input.length();
        String[][] V=new String[SIZE][SIZE];
        //1.Vii
        for(int i=0;i<input.length();i++){
            boolean flag=true;
            char w=input.charAt(i);
            Set<Character> tmp=reverse_find(CNF, Character.toString(w));
            for(Iterator<Character> itr_tmp=tmp.iterator();itr_tmp.hasNext();){
                if(flag){
                    V[i][i]=Character.toString(itr_tmp.next());
                    flag=false;
                }					
                else V[i][i]+=Character.toString(itr_tmp.next());
            }
        }
        //2.Vij
        for(int dist=1;dist<SIZE;dist++){
            for(int i=0;i<SIZE-dist;i++){
                int j=i+dist;
                for(int k=i;k<j;k++){
                    String B=V[i][k];
                    String C=V[k+1][j];
                    if(B!=null&&C!=null){
                        for(int p=0;p<B.length();p++){
                            for(int q=0;q<C.length();q++){
                                String w=Character.toString(B.charAt(p))+Character.toString(C.charAt(q));
                                Set<Character> tmp=reverse_find(CNF,w);
                                for(Iterator<Character> itr_tmp=tmp.iterator();itr_tmp.hasNext();){
                                    if(V[i][j]==null) V[i][j]=Character.toString(itr_tmp.next());
                                    else{
                                        Character literal=itr_tmp.next();
                                        if(!V[i][j].contains(Character.toString(literal)))
                                            V[i][j]+=literal;
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }
        if(V[0][SIZE-1]!=null){
            if(V[0][SIZE-1].contains(Character.toString(start_symbol)))
                return true;
            else return false;
        }
        return false;
    }

    ///////////////////SUB_METHOD////////////////////////////////////

    void printing(){
        try(BufferedWriter out= new BufferedWriter(new FileWriter("grammar_output.txt"))){
            ListIterator<Production> listItr=CNF.listIterator();
            while(listItr.hasNext()){
                Production prd=listItr.next();
                out.write(prd.first+":"+prd.second+"\n");
            }
            out.close();
        }catch(IOException e){
            ;
        }
    }

    char give_literal(){
        for(Character key: literal_symbol.keySet()){
            if(!literal_symbol.get(key)){
                literal_symbol.put(key, true);
                return key;
            }
        }
        return ':';//there's no more variable I can use
    }
    /**
     * only for unit production LinekdList
     * @param list
     * @param V
     * @return
     */
    Set<Character> find_unit(LinkedList<Production> list, char V){
        Set<Character> set=new HashSet<Character>();
        ListIterator<Production> itr=list.listIterator();
        while(itr.hasNext()){
            Production curr=itr.next();
            if(V==curr.first) set.add(curr.second.charAt(0));
        }
        return set;
    }
    Set<String> find(LinkedList<Production> list, char V){
        Set<String> set=new HashSet<String>();
        ListIterator<Production> itr=list.listIterator();
        while(itr.hasNext()){
            Production curr=itr.next();
            if(V==curr.first) set.add(curr.second);
        }
        return set;
    }

    boolean check(String input){
        boolean flag=true;
        for(int i=0;i<input.length();i++){
            char a=input.charAt(i);
            if(a<'A'||(a>'Z'&&a<'a')||a>'z') flag=false;
        }
        return flag;
    }
    LinkedList<Production> change(LinkedList<Production> list, char a,char V){
        LinkedList<Production> result=new LinkedList<Production>();
        ListIterator<Production> itr=list.listIterator();
        while(itr.hasNext()){
            Production curr=itr.next();
            if(curr.second.length()>1)
                result.add(new Production(curr.first,curr.second.replace(a, V)));
            else
                result.add(curr);
        }
        return result;
    }
    LinkedList<Production> change(LinkedList<Production> list, String W, char V){
        LinkedList<Production> result=new LinkedList<Production>();
        ListIterator<Production> itr=list.listIterator();
        while(itr.hasNext()){
            Production prd=itr.next();
            String curr=prd.second;
            if(curr.length()<3||curr.length()<W.length())
                result.add(prd);
            else{
                curr=curr.replaceAll(W, Character.toString(V));
                result.add(new Production(prd.first, curr));
            }
        }
        return result;
    }

    Set<Character> reverse_find(LinkedList<Production> list, String a){
        Set<Character> result=new HashSet<Character>();
        if(a!=null){
            ListIterator<Production> itr_list=list.listIterator();
            while(itr_list.hasNext()){
                Production curr=itr_list.next();
                if(curr.second.equals(a)){
                    result.add(curr.first);
                }
            }
        }
        return result;
    }
}
