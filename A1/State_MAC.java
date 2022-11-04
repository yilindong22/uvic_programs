public class State_MAC
{
    int[] MAC_Array;
    // int human;
    //用二维数组初始化
    public State_MAC(int[] Array) { 
    	this.MAC_Array = Array; 
        // human = WGC_Array[1][1];
    }
    public State_MAC(State_MAC state) {
        MAC_Array = new int [5];
      //int [0]lefM = 3;
      //int [1]lefC = 3;
      //int [2]rightM = 0;
      //int [3]rightC = 0;
      //int[4] boat position 0 : left, 1:right;
      for(int i =0; i <5; i++){
        MAC_Array[i] = state.MAC_Array[i];
      }
    }
    // 用state初始化
    public String toString() {
    	return java.util.Arrays.toString( MAC_Array );
    }
}