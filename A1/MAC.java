import java.util.HashSet;
import java.util.Set;

public class MAC extends Problem {
    
	boolean goal_test(Object state) {
        // Wolf_goat_cabbage puzzle_state = (StateNPuzzle) state;
		// for(int i=0; i<4; i++){
		// 	if(puzzle_state.puzzleArray[1][i]!=1){
		// 		return false;
		// 	}
		// }
		State_MAC mac_state = (State_MAC) state;
        if(mac_state.MAC_Array[2] != 3 || mac_state.MAC_Array[3] != 3){
            return false;
        }
		//if(((mac_state.MAC_Array[1] > mac_state.MAC_Array[0])&&mac_state.MAC_Array[0] != 0)  ||((mac_state.MAC_Array[3] > mac_state.MAC_Array[2])&&mac_state.MAC_Array[2]!=0)){
        //        return false;
       // }
        return true;

	}
    Set<Object> getSuccessors(Object state) {
        Set<Object> set = new HashSet<Object>();
        State_MAC s = (State_MAC) state;
		State_MAC ss;
        ss = new State_MAC(s);
        if(ss.MAC_Array[4] == 0){ // boat left 
            for (int i =0; i < 5; i++){
                if(ss.MAC_Array[4] ==1){
                    continue;
                }
                if( i == 0 ){ // 1 传 
                    if((ss.MAC_Array[0] >0 && ss.MAC_Array[0]-1 >ss.MAC_Array[1])||(ss.MAC_Array[0]-1) ==0){
                        ss.MAC_Array[0] -=1;
                        ss.MAC_Array[2] +=1;
                        ss.MAC_Array[4] =1;
                        set.add(ss);
				ss = new State_MAC(s);
                    }else{
                        continue;
                    }
                }
                if( i ==1   ){ // 1吃
                    if((ss.MAC_Array[1]>0 &&  ss.MAC_Array[2] >= ss.MAC_Array[3]+1)||(ss.MAC_Array[2] ==0 &&ss.MAC_Array[1]>0 )){
                        ss.MAC_Array[1] -=1;
                        ss.MAC_Array[3] +=1;
                        ss.MAC_Array[4] =1;
                        set.add(ss);
				        ss = new State_MAC(s);
                    }else{
                        continue;
                    }
                }
                if( i == 2){  // 1传1吃
                    if( ss.MAC_Array[1]>=1 && ss.MAC_Array[0]>=1){
                        ss.MAC_Array[0]-=1;
                        ss.MAC_Array[1]-=1;
                        ss.MAC_Array[2] +=1;
                        ss.MAC_Array[3] +=1;
                        ss.MAC_Array[4] =1;
                        set.add(ss);
				ss = new State_MAC(s);

                    }else{
                        continue;
                    }
                  
                }
                if(i ==3){ //2 传
                    if( (ss.MAC_Array[0]>=2 && ss.MAC_Array[0] -2 >= ss.MAC_Array[1])||(ss.MAC_Array[0]-2==0 )){
                        ss.MAC_Array[0]-=2;
                        ss.MAC_Array[2] +=2;
                        ss.MAC_Array[4] =1;
                        set.add(ss);
				ss = new State_MAC(s);

                    }else{
                        continue;
                    }
                }
                if( i ==4 ){ // 2吃
                    if((ss.MAC_Array[1] >=2 && ss.MAC_Array[2] >= ss.MAC_Array[3] +2)||(ss.MAC_Array[2]==0 && ss.MAC_Array[1] >=2)){
                        ss.MAC_Array[1] -=2;
                        ss.MAC_Array[3] +=2;
                        ss.MAC_Array[4] =1;
                        set.add(ss);
				ss = new State_MAC(s);

                    }else{
                        continue;
                    }
                }
            }
        }else{   // boat right
            for (int j = 0; j<5; j++){
                if(ss.MAC_Array[4] ==0){
                    continue;
                }
                if(j ==0){ // 1传
                    if((ss.MAC_Array[2] >=1 && ss.MAC_Array[2]-1 >=ss.MAC_Array[3])||ss.MAC_Array[2] -1 ==0){
                        ss.MAC_Array[2] -=1;
                        ss.MAC_Array[0]+=1;
                        ss.MAC_Array[4] =0;
                        set.add(ss);
				ss = new State_MAC(s);
                    }else{
                        continue;
                    }
                }
                if(j == 1){ //1吃
                    if( j == 1){
                        if((ss.MAC_Array[3]>=1 &&  ss.MAC_Array[0] >= ss.MAC_Array[1]+1)||(ss.MAC_Array[0] ==0&&ss.MAC_Array[3]>=1)){
                            ss.MAC_Array[3] -=1;
                            ss.MAC_Array[1] +=1;
                            ss.MAC_Array[4] =0;
                            set.add(ss);
				ss = new State_MAC(s);

                        }
                    }else{
                        continue;
                    }
                }
                if(j==2){ // 一传一吃
                    if (ss.MAC_Array[2]>=1 && ss.MAC_Array[3]>=1){
                        ss.MAC_Array[2] -= 1;
                        ss.MAC_Array[3] -= 1;
                        ss.MAC_Array[0] +=1;
                        ss.MAC_Array[1] += 1;
                        ss.MAC_Array[4] =0;
                        set.add(ss);
				ss = new State_MAC(s);

                    }else{
                        continue;
                    } 
                }
                if(j == 3){ //二传
                    if((ss.MAC_Array[2]>=2 && ss.MAC_Array[2] -2 >= ss.MAC_Array[3])||ss.MAC_Array[2]-2 ==0){
                        ss.MAC_Array[2]-=2;
                        ss.MAC_Array[0]+=2;            
                        ss.MAC_Array[4] =0;
                        set.add(ss);
				ss = new State_MAC(s);
   
                    }
                }else{
                    continue;
                }
                if((ss.MAC_Array[3] >=2 && ss.MAC_Array[0] >= ss.MAC_Array[1] +2)||(ss.MAC_Array[0] ==0 &&ss.MAC_Array[3]>=2)){ //二吃
                    ss.MAC_Array[1] +=2;
                    ss.MAC_Array[3] -=2;
                    ss.MAC_Array[4] =0;
                    set.add(ss);
				    ss = new State_MAC(s);

                }else{
                    continue;
                }

            }
        }

        return set;
    }

double step_cost(Object fromState, Object toState) { return 1; }
public double h(Object state) { 
    State_MAC s = (State_MAC) state;
    return 6-(s.MAC_Array[2]+s.MAC_Array[3]); }
public static void main(String[] args) throws Exception {
    MAC problem = new MAC();
    int[] MAC_Array = {3,3,0,0,0};
    // 3 - left Missionaries
    // 3 - left  cannibals
    // 0 - right Missionaries
    // 0 - right cannibals
    State_MAC temp = new State_MAC(MAC_Array); 	
    problem.getSuccessors(temp);
     System.out.println(problem.goal_test(temp));
     System.out.println(problem.getSuccessors(temp));

    problem.initialState = temp; 

    Search search  = new Search(problem);


    
     System.out.println("TreeSearch------------------------");
    System.out.println("BreadthFirstTreeSearch:\t\t" + search.BreadthFirstTreeSearch());
    System.out.println("UniformCostTreeSearch:\t\t" + search.UniformCostTreeSearch());
    System.out.println("DepthFirstTreeSearch:\t\t" + search.DepthFirstTreeSearch());
    System.out.println("GreedyBestFirstTreeSearch:\t" + search.GreedyBestFirstTreeSearch());
    System.out.println("AstarTreeSearch:\t\t" + search.AstarTreeSearch());
    
     System.out.println("\n\nGraphSearch----------------------");
     System.out.println("BreadthFirstGraphSearch:\t" + search.BreadthFirstGraphSearch());
     System.out.println("UniformCostGraphSearch:\t\t" + search.UniformCostGraphSearch());
     System.out.println("DepthFirstGraphSearch:\t\t" + search.DepthFirstGraphSearch());
     System.out.println("GreedyBestGraphSearch:\t\t" + search.GreedyBestFirstGraphSearch());
     System.out.println("AstarGraphSearch:\t\t" + search.AstarGraphSearch());
    
      System.out.println("\n\nIterativeDeepening----------------------");
     System.out.println("IterativeDeepeningTreeSearch:\t" + search.IterativeDeepeningTreeSearch());
      System.out.println("IterativeDeepeningGraphSearch:\t" + search.IterativeDeepeningGraphSearch());

}
}

