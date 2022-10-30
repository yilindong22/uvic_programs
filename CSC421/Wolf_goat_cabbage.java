import java.util.HashSet;
import java.util.Set;

public class Wolf_goat_cabbage extends Problem {
    
	boolean goal_test(Object state) {
		//为object state创建对象
        // Wolf_goat_cabbage puzzle_state = (StateNPuzzle) state;
		// for(int i=0; i<4; i++){
		// 	if(puzzle_state.puzzleArray[1][i]!=1){
		// 		return false;
		// 	}
		// }
		State_WGC wgc_state = (State_WGC) state;
		for(int i=0; i<4; i++){
			if(wgc_state.WGC_Array[1][i]!=1){
				return false;
			}
		}
        return true;

	}
  
    Set<Object> getSuccessors(Object state) {
    	
        Set<Object> set = new HashSet<Object>();
		//s的原本数据
		State_WGC s = (State_WGC) state;
		State_WGC ss;
		ss = new State_WGC(s);
		// ss.WGC_Array[0][3] += 2;
		// System.out.println(ss);
		//means human is on left side so he can take one item to the right

		if(ss.WGC_Array[0][3]==1){
			//they all on the left
			//狼羊菜人-1111
			//人在左边会出现
			//选取左边任意一个数值是的val转移到右边，同时人也转移到右边
			//如果左边剩下的不是狼羊或者羊菜那么将array加入到set中
			/*人必须带一个从左往右 */
			for(int i = 0; i < 3; i++){
				if(i==0 && ss.WGC_Array[0][1]==1 && ss.WGC_Array[0][2]==1){
					continue;
				}
				else if(i==2 && ss.WGC_Array[0][0]==1 && ss.WGC_Array[0][1]==1){
					continue;
				}
				ss.WGC_Array[0][i] = 0;
				ss.WGC_Array[1][i] = 1;
				ss.WGC_Array[0][3] = 0;
				ss.WGC_Array[1][3] = 1;
				set.add(ss);
				ss = new State_WGC(s);
			}
			//人在右边
			//当狼羊或者羊菜在右边时需要将羊带回去
		} else {
			//人回去什么也没有带
			for(int i = 0; i < 3; i++){
				//当狼羊 羊菜在右边时不能直接回去 continue
				if(ss.WGC_Array[1][0]==1 && ss.WGC_Array[1][1]==1){
					continue;
				}
				else if(ss.WGC_Array[1][1]==1 && ss.WGC_Array[1][1]==1){
					continue;
				}
				ss.WGC_Array[0][3] = 1;
				ss.WGC_Array[1][3] = 0;
				ss = new State_WGC(s);
			}
			for(int i = 0; i < 3; i++){
				if(i==0 && ss.WGC_Array[1][1]==1 && ss.WGC_Array[1][2]==1){
					continue;
				}
				else if(i==2 && ss.WGC_Array[1][0]==1 && ss.WGC_Array[1][1]==1){
					continue;
				}
				ss.WGC_Array[0][i] = 1;
				ss.WGC_Array[1][i] = 0;
				ss.WGC_Array[0][3] = 1;
				ss.WGC_Array[1][3] = 0;
				set.add(ss);
				ss = new State_WGC(s);
			}
			//人回去带一个


		}
			

        return set;
    }
	
	double step_cost(Object fromState, Object toState) { return 1; }

	public double h(Object state) { 
		State_WGC s = (State_WGC) state;
		double h = 0;
		for(int i = 0; i < 3; i++){
			if (s.WGC_Array[1][i] !=1){
				h+=1;
			}
		}
		return h; 
	}


	public static void main(String[] args) throws Exception {
		//创建problem对象
		Wolf_goat_cabbage problem = new Wolf_goat_cabbage();
		//设置ini-state
		//Wolf,Goat,Cabbge,Human
		int[][] wgc_Array = {{1,1,1,1}, {0,0,0,0}};
		State_WGC temp = new State_WGC(wgc_Array); 	
		//如果左边羊=1
		// problem.getSuccessors(temp);
		// System.out.println(problem.goal_test(temp));
		// System.out.println(problem.getSuccessors(temp));

		problem.initialState = temp; 
		System.out.println(problem.h(temp));
		// Search search  = new Search(problem);
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
