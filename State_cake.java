public class State_cake {
    int N;
    int []  cake_Array;
    public State_cake(int[] Array) { 
    	this.cake_Array = Array; 
        N = cake_Array.length;
        // human = WGC_Array[1][1];
    }
    public State_cake(State_cake state) {
        // WGC_Array.type;
        N = state.N;
        cake_Array = new int[N];
        // cake_Array = new int[state.cake_Array.length];
        // WGC_Array[1][3]=1;
    
        for(int i=0; i < N; i++){
            cake_Array[i] = state.cake_Array[i];
        }      

    }
    public String toString() {
    	return java.util.Arrays.toString( cake_Array );
    }
}
