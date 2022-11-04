import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CSPBickriding extends CSP {
	
	static Set<Object> varCol = new HashSet<Object>(Arrays.asList(new String[] {"blue", "green", "black", "red", "white"}));
	static Set<Object> varNam = new HashSet<Object>(Arrays.asList(new String[] {"Adrian", "Charles", "Henry", "Joel", "Richard"}));
	static Set<Object> varSan = new HashSet<Object>(Arrays.asList(new String[] {"bacon", "chicken", "cheese", "pepperoni", "tuna"}));
	static Set<Object> varJuc = new HashSet<Object>(Arrays.asList(new String[] {"apple", "cranbery", "grapefruit", "orange", "pineapple"}));
	static Set<Object> varAge = new HashSet<Object>(Arrays.asList(new String[] {"12 years", "13 years", "14 years", "15 years", "16 years"}));
    static Set<Object> varSpo = new HashSet<Object>(Arrays.asList(new String[] {"baseball", "basketball", "hockey", "soccer", "swimming"}));

	
	public boolean isGood(Object X, Object Y, Object x, Object y) {

		//if X is not even mentioned in by the constraints, just return true
		//as nothing can be violated
		//如果variable list中没有X之间返回true
		if(!C.containsKey(X))
			return true;
		
		//check to see if there is an arc between X and Y
		//if there isn't an arc, then no constraint, i.e. it is good
		//XY之间没有arc
		if(!C.get(X).contains(Y))
			return true;

		// //3.The owner of the White bike is somewhere between the 15-year-old boy and the youngest boy, in that order.
        // //白色在15-12岁之间

        if(X.equals("white") && Y.equals("15 years")){
            if((Integer)x<=(Integer)y){
                return false;
            }
            return true;
        }
        if(X.equals("white") && Y.equals("12 years")){
            if((Integer)x>=(Integer)y){
                return false;
            }
            return true;
        }


        

        //5.Henry is exactly to the left of the Soccer fan.
        //henry<soccer
        //需要x-y<1
        // x-y>=0return false
        if(X.equals("Henry") && Y.equals("soccer") && ((Integer)x-(Integer)y)>=0)
			return false;
			
        // //6.The boy who is going to drink Grapefruit juice is somewhere between who brought Tuna sandwich and who br
        // //ought Pineapple juice, in that order.
        // //grapefruit< or > tu and pi
        if(X.equals("grapefruit") && Y.equals("tuna")){
            if((Integer)x<=(Integer)y){
                return false;
            }
            return true;
        }
        if(X.equals("grapefruit") && Y.equals("pineapple")){
            if((Integer)x>=(Integer)y){
                return false;
            }
            return true;
        }


        //8.The one who likes Swimming is next to the friend who likes Baseball.
        if(X.equals("swimming") && Y.equals("baseball") && Math.abs((Integer)x-(Integer)y)!=1)
			return false;

        //9.The cyclist that brought Pineapple juice is somewhere between the 14-year-old and the boy that brought Orange juice, in that order.
        if(X.equals("pineapple") && Y.equals("14 years")){
            if((Integer)x<=(Integer)y){
                return false;
            }
            return true;
        }
        if(X.equals("pineapple") && Y.equals("orange")){
            if((Integer)x>=(Integer)y){
                return false;
            }
            return true;
        }
 
        //11.The boy who likes the sport played on ice is going to eat Pepperoni sandwich.
        if(X.equals("hockey") && Y.equals("pepperoni") && !x.equals(y))
			return false;

        //12.The boy riding the White bike is somewhere between the boys riding the blue and the black bicycles, in that order.
        if(X.equals("white") && Y.equals("blue")){
            if((Integer)x<=(Integer)y){
                return false;
            }
            return true;
        }
        if(X.equals("white") && Y.equals("black")){
            if((Integer)x>=(Integer)y){
                return false;
            }
            return true;
        }

        //13.Joel is next to the 16-year-old cyclist.
        if(X.equals("Joel") && Y.equals("16 years") && Math.abs((Integer)x-(Integer)y)!=1)
			return false;

        // //14.Adrian is exactly to the left of the boy who is going to eat Pepperoni sandwich.
        if(X.equals("Adrian") && Y.equals("pepperoni") && ((Integer)x-(Integer)y)>=0)
			return false;

        // //15.The 12-year-old is somewhere between the 14-year-old and the oldest boy, in that order.
        if(X.equals("12 years") && Y.equals("14 years")){
            if((Integer)x<=(Integer)y){
                return false;
            }
            return true;
        }
        if(X.equals("12 years") && Y.equals("16 years")){
            if((Integer)x>=(Integer)y){
                return false;
            }
            return true;
        }

         //16.The boy who is going to eat Bacon sandwich is somewhere to the right of the owner of the White bicycle.
        if(X.equals("bacon") && Y.equals("white") && ((Integer)x-(Integer)y)<=0)
            return false;

        //17.The 16-year-old brought Cheese sandwich.
        if(X.equals("16 years") && Y.equals("cheese") && !x.equals(y))
			return false;

        // //19.The cyclist riding the White bike is somewhere between Richard and the boy riding the Red bike, in that order.
        if(X.equals("white") && Y.equals("Richard")){
            if((Integer)x<=(Integer)y){
                return false;
            }
            return true;
        }
        if(X.equals("white") && Y.equals("red")){
            if((Integer)x>=(Integer)y){
                return false;
            }
            return true;
        }

        //20.The Baseball fan is next to the boy who is going to drink Apple juice.
        if(X.equals("baseball") && Y.equals("apple") && Math.abs((Integer)x-(Integer)y)!=1)
            return false;

        // //22.Charles is somewhere between Richard and Adrian, in that order.
        if(X.equals("Charles") && Y.equals("Richard")){
            if((Integer)x<=(Integer)y){
                return false;
            }
            return true;
        }
        if(X.equals("Charles") && Y.equals("Adrian")){
            if((Integer)x>=(Integer)y){
                return false;
            }
            return true;
        }

		//Uniqueness constraints
		//XY不为同一个variable
		if(varCol.contains(X) && varCol.contains(Y) && !X.equals(Y) && x.equals(y))
			return false;
		
		if(varNam.contains(X) && varNam.contains(Y) && !X.equals(Y) && x.equals(y))
			return false;
		
		if(varSan.contains(X) && varSan.contains(Y) && !X.equals(Y) && x.equals(y))
			return false;
		
		if(varJuc.contains(X) && varJuc.contains(Y) && !X.equals(Y) && x.equals(y))
			return false;
		
		if(varAge.contains(X) && varAge.contains(Y) && !X.equals(Y) && x.equals(y))
			return false;

        if(varSpo.contains(X) && varSpo.contains(Y) && !X.equals(Y) && x.equals(y))
			return false;

		
		return true;
	}
		
	public static void main(String[] args) throws Exception {
		CSPBickriding csp = new CSPBickriding();
		//房子的参数是12345
		Integer[] dom = {1,2,3,4,5};
		//每一个value都可以住任何房子，所以根据val值创建domain
		for(Object X : varCol) 
			csp.addDomain(X, dom);
		
		for(Object X : varNam) 
			csp.addDomain(X, dom);
		
		for(Object X : varSan) 
			csp.addDomain(X, dom);
		
		for(Object X : varJuc) 
			csp.addDomain(X, dom);
		
		for(Object X : varAge) 
			csp.addDomain(X, dom);
        
        for(Object X : varSpo) 
			csp.addDomain(X, dom);
		

        //1.In the middle is the boy that likes Baseball.
		for(int i=1; i<=5; i++)
			if(i != 3)
				csp.D.get("baseball").remove(i);
        //2.The cyclist who is going to eat Tuna sandwich is at one of the ends.
        for(int i=1; i<=5; i++)
			if(i != 5 && i!=1) 
				csp.D.get("tuna").remove(i);
        
        //3.The owner of the White bike is somewhere between the 15-year-old boy and the youngest boy, in that order.
        csp.addBidirectionalArc("white", "15 years");
        csp.addBidirectionalArc("white", "12 years");

        //4.The boy that is going to drink Pineapple juice is at the fourth position
        for(int i=1; i<=5; i++)
			if(i != 4)
				csp.D.get("pineapple").remove(i);

        //5.Henry is exactly to the left of the Soccer fan.
        csp.addBidirectionalArc("Henry", "soccer");

        //6.The boy who is going to drink Grapefruit juice is somewhere between who brought Tuna sandwich and who br
        //ought Pineapple juice, in that order.
        csp.addBidirectionalArc("grapefruit", "tuna");
        csp.addBidirectionalArc("grapefruit", "pineapple");

        //7.The boy riding the Black bike is at the third position.
        for(int i=1; i<=5; i++)
			if(i != 3)
				csp.D.get("black").remove(i);

        //8.The one who likes Swimming is next to the friend who likes Baseball.
        csp.addBidirectionalArc("swimming", "baseball");

        //9.The cyclist that brought Pineapple juice is somewhere between the 14-year-old and the boy that brought Orange juice, in that order.
        csp.addBidirectionalArc("pineapple", "14 years");
        csp.addBidirectionalArc("pineapple", "orange");

        //10.At one of the ends is the boy riding the Green bicycle.
        for(int i=1; i<=5; i++)
			if(i != 5)
				csp.D.get("green").remove(i);
        //11.The boy who likes the sport played on ice is going to eat Pepperoni sandwich.
        csp.addBidirectionalArc("hockey", "pepperoni");

        //12.The boy riding the White bike is somewhere between the boys riding the blue and the black bicycles, in that order.
        csp.addBidirectionalArc("white", "blue");
        csp.addBidirectionalArc("pineapple", "orange");

        //13.Joel is next to the 16-year-old cyclist.
        csp.addBidirectionalArc("white", "14 years");

        //14.Adrian is exactly to the left of the boy who is going to eat Pepperoni sandwich.
        csp.addBidirectionalArc("Adrian", "pepperoni");
        
        //15.The 12-year-old is somewhere between the 14-year-old and the oldest boy, in that order.
        csp.addBidirectionalArc("12 years", "14 years");
        csp.addBidirectionalArc("12 years", "16 years");

        //16.The boy who is going to eat Bacon sandwich is somewhere to the right of the owner of the White bicycle.
        csp.addBidirectionalArc("bacon", "white");

        //17.The 16-year-old brought Cheese sandwich.
        csp.addBidirectionalArc("16 years", "cheese");

        //18.In the fifth position is the 13-year-old boy.
        for(int i=1; i<=5; i++)
			if(i != 5)
				csp.D.get("13 years").remove(i);

        //19.The cyclist riding the White bike is somewhere between Richard and the boy riding the Red bike, in that order.
        csp.addBidirectionalArc("white", "Richard");
        csp.addBidirectionalArc("white", "red");

        //20.The Baseball fan is next to the boy who is going to drink Apple juice.
        csp.addBidirectionalArc("baseball", "apple");

        //21.The boy who likes Hockey is at the fifth position.
        for(int i=1; i<=5; i++)
			if(i != 5)
				csp.D.get("hockey").remove(i);

        //22.Charles is somewhere between Richard and Adrian, in that order.
        csp.addBidirectionalArc("Charles", "Richard");
        csp.addBidirectionalArc("Charles", "Adrian");



		//Uniqueness constraints
		//横向建立连接
		for(Object X : varCol)
			for(Object Y : varCol)
				csp.addBidirectionalArc(X,Y);
		
		for(Object X : varNam)
			for(Object Y : varNam)
				csp.addBidirectionalArc(X,Y);

		for(Object X : varSan)
			for(Object Y : varSan)
				csp.addBidirectionalArc(X,Y);

		for(Object X : varJuc)
			for(Object Y : varJuc)
				csp.addBidirectionalArc(X,Y);

		for(Object X : varAge)
			for(Object Y : varAge)
				csp.addBidirectionalArc(X,Y);

        for(Object X : varSpo)
			for(Object Y : varSpo)
				csp.addBidirectionalArc(X,Y);


        // System.out.println(csp.D.get("white"));
        // for(Object Y : csp.D.get("white")) {
        //     System.out.println(Y);
        // }
			// Object y = A.get(Y);
        // isGood
		//Now let's search for solution 
		
		Search search = new Search(csp);
		System.out.println(search.BacktrackingSearch());
	}
}