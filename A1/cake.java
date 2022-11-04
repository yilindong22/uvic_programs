import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;  // Import the Scanner class
public class cake extends Problem {
    // public static int a;

	boolean goal_test(Object state) {

        State_cake cake_state = (State_cake) state;
        int k = 0;
        for(int i=0; i<cake_state.N; i++) {
            if(cake_state.cake_Array[i] != k)
                return false;
            k++;
        }
    
        return true;
        // for(int i=0; i<puzzle_state.N; i++)
		// for (int i = 0;  i < a-1; i++){
        //     if(cake_state.cake_Array[i+1] - cake_state.cake_Array[i] !=1){
        //         return false;
        //     }
        // }
        
        // return true;
	}
  
    Set<Object> getSuccessors(Object state) {
    	
        Set<Object> set = new HashSet<Object>();
		//s的原本数据
		State_cake s = (State_cake) state;
		State_cake ss;
		ss = new State_cake(s);
        for(double i =0; i<s.N; i++){
            double j=i/2;
            // System.out.println(j);
            //不是0.5结尾
            if (j%1==0 && j!=0){
                int temp1 = (int)j-1;
                int temp2 = (int)j+1;
                while(true){
                    int t = 0;
                    t=ss.cake_Array[temp1];
                    ss.cake_Array[temp1] = ss.cake_Array[temp2];
                    ss.cake_Array[temp2] = t;
                    if(temp1==0){
                        break;
                    }
                    temp1-=1;
                    temp2+=1;
                }
                // System.out.println("true");
            }
            else{
                int temp1 = (int)(j-0.5);
                int temp2 = (int)(j+0.5);
                while(true){
                    int t = 0;
                    t=ss.cake_Array[temp1];
                    ss.cake_Array[temp1] = ss.cake_Array[temp2];
                    ss.cake_Array[temp2] = t;
                    if(temp1==0){
                        break;
                    }
                    temp1-=1;
                    temp2+=1;
                }
                // System.out.println("false");
            }
            set.add(ss);
            ss = new State_cake(s);
            // System.out.println("false");
            
            
        }
		// ss.WGC_Array[0][3] += 2;
		//means human is on left side so he can take one item to the right
        return set;
    }
	
	double step_cost(Object fromState, Object toState) { return 1; }

	public double h(Object state) { 
        
         int k= 0;
         State_cake s = (State_cake) state;
         double count = s.N-1;
         for(int i=0; i<s.N-1; i++) {

             if(Math.abs(s.cake_Array[i]-s.cake_Array[i+1]) ==1 ){
                 count-=1;
             }

         }
         return count; 
    }

    
	public static void main(String[] args) throws Exception {

    // Scanner sc=new Scanner(System.in);
    // System.out.println("enter size of an array");
    // int n=sc.nextInt();
    // int arr[]=new int[n];
    // System.out.println("enter consecutive positive numbers");
    //     for(int i=0;i<n;i++){
    //     arr[i]=sc.nextInt();
    // }
        //System.out.println(java.util.Arrays.toString( arr ));

        




		cake problem = new cake();
        // a = arr.length;

		int[] cake_Array = {1,0,3,5,2,4};
        // int[] cake_Array = {0,1,2,3,4,5};
        State_cake temp = new State_cake(cake_Array);

        problem.initialState = temp;
        // System.out.println(temp.N); 
        // System.out.println(problem.goal_test(temp));
        // problem.getSuccessors(temp);
        // System.out.println(problem.h(temp));
        // Search search  = new Search(problem);
        // for (int p = 0; p<a;p++){
        //     cake_Array[p] = arr[p];
        // }
		// State_cake temp = new State_cake(cake_Array); 	

		//  problem.getSuccessors(temp);
		//  //System.out.println(problem.goal_test(temp));
		//  //System.out.println(problem.getSuccessors(temp));

		// problem.initialState = temp; 
		
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
