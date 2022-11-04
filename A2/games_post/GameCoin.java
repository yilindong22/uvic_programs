import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class GameCoin extends Game {
	
	int count = 0; //'O' for computer, 'X' for human
    //0for computer 1 for human
	int marks[] = {0, 1}; 
    
    int WinningScore = 10;
    int LosingScore = -10;
    int NeutralScore = 0;  
    public GameCoin() {
    	currentState = new StateCoin();
    }

    public boolean isWinState(State state){
        StateCoin cstate = (StateCoin) state;
        if(cstate.point==13)
            return true;
        return false;
    }

    public boolean isStuckState(State state) {
        if (isWinState(state)) 
            return false;
        
        StateCoin cstate = (StateCoin) state;
        if(cstate.point<13){
            return false;
        }
        return true;
    }

    public Set<State> getSuccessors(State state){
        if(isWinState(state) || isStuckState(state))
			return null;

        Set<State> successors = new HashSet<State>();
        StateCoin cstate = (StateCoin) state;
        StateCoin successor_state;

        for(int i=1; i <=3; i++){
            successor_state = new StateCoin(cstate);
            successor_state.point +=i;
            successor_state.player = (state.player==0 ? 1 : 0); 
            successors.add(successor_state);
        }
        return successors;

    }

    public double eval(State state) {
        if(isWinState(state)){
            int previous_player = (state.player==0 ? 1 : 0);

            if (previous_player==0) //computer wins
	            return WinningScore;
	    	else //human wins
	            return LosingScore;
        }
        return NeutralScore;
    }
    public static void main(String[] args) throws Exception {
        //创建game对象
        Game game = new GameCoin(); 
        //由game创建search对象
        Search search = new Search(game);
        
        //needed to get human's move
        //读取keyboard输入
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int depth = 12;
        while (true) {
        	
        	StateCoin nextState = null;
        	boolean error = false;
            switch ( game.currentState.player ) {
              case 1: //Human
                  
            	  //get human's move
                  System.out.print("Enter number between 1-3:");
                  //pos存取读取的数字
                  int pos = Integer.parseInt( in.readLine() );
            	  if(pos!=1 && pos!=2 && pos!=3){
                    error = true;
                    break;
                  }
                  nextState = new StateCoin((StateCoin)game.currentState);
                  nextState.player = 1;
                  nextState.point += pos;
                  System.out.println("Human: \n" + nextState);
                  break;
                  
              case 0: //Computer
            	  
            	  nextState = (StateCoin)search.bestSuccessorState(depth);
            	  nextState.player = 0;
            	  System.out.println("Computer: \n" + nextState);
                  break;
            }
            if (error == true){
                System.out.println("invaild input");
                break;
            }        
            game.currentState = nextState;
            //change player
            game.currentState.player = (game.currentState.player==0 ? 1 : 0);
            
            //Who wins?
            if ( game.isWinState(game.currentState) ) {
            
            	if (game.currentState.player == 1) //i.e. last move was by the computer
            		System.out.println("Computer wins!");
            	else
            		System.out.println("You win!");
            	
            	break;
            }
            
            if ( game.isStuckState(game.currentState) ) { 
            	System.out.println("Cat's game!");
            	break;
            }
        }
    }
}