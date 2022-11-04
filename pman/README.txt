Yilin Dong
V00928413

To compile the program run 'make'
- Make doesn't automatically compile the inf file. 
To run the program run './PMan'

bg : bg <cmd> :
	runs the command at the background
	ex: bg ./inf TES 10
	    bg /home/path/inf TES 100 
	
bglist : bglist:
	list all of the program running in the background 
	ex: bglist 
		123456 ./inf 
		654321 /home/path/inf
bgstop : bgstop <pid> :
	stop the running process base on the given pid 
bgstart : bgstart <pid> :
	start the running process base on the given pid 
bgkill : bgkill <pid> :
	kill the running process base on the given pid 
pstat : pstat <pid> :
	prints the comm, state, utime, stime, rss, voluntary_ctxt_switches, nonvoluntary_ctxt_switches of the process base on the given pid

