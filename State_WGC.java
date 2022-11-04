public class State_WGC
{    
    int[][] WGC_Array;
    // int human;
    //用二维数组初始化
    public State_WGC(int[][] Array) { 
    	this.WGC_Array = Array; 
        // human = WGC_Array[1][1];
    }
    public State_WGC(State_WGC state) {
        // WGC_Array.type;
        WGC_Array = new int[2][4];
        // WGC_Array[1][3]=1;
        // System.out.println(WGC_Array[1][3]);
        for(int i=0; i<2; i++){
            for(int j=0; j<4; j++){
                WGC_Array[i][j] = state.WGC_Array[i][j];
            }
        }
        //     for(int j=0; j<4; j++)
        //         WGC_Array = state.WGC_Array[i][j];
        

    }
    //用state初始化
    public String toString() {
    	return java.util.Arrays.deepToString( WGC_Array );
    }
}