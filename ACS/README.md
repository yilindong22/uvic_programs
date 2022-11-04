Yilin Dong 
V00928413

1 run make 
2 run ./ACS <input.txt> 
	example: ./ACS input01.txt
	provided two examples 
input format should be:
	The first character specifies the unique ID of customers.
	A colon(:) immediately follows the unique number of the customer.
	Immediately following is an integer equal to either 1 (indicating the customer belongs to business class) or 0 (indicating the customer belongs to economy class).
	A comma(,) immediately follows the previous number.
	Immediately following is an integer that indicates the arrival time of the customer. 
	A comma(,) immediately follows the previous number.
	Immediately following is an integer that indicates the service time of the customer. 
	A newline (\n) ends a line.
	
	example:
	8
	1:0,2,60
	2:0,4,70
	3:0,5,50
	4:1,7,30
	5:1,7,40
	6:1,8,50
	7:0,10,30
	8:0,11,50
