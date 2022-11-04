/*
    V00928413
    Yilin Dong
 */
#include <unistd.h>     // fork(), execvp()
#include <stdio.h>      // printf(), scanf(), setbuf(), perror()
#include <stdlib.h>     // malloc()
#include <sys/types.h>  // pid_t 
#include <sys/wait.h>   // waitpid()
#include <signal.h>     // kill(), SIGTERM, SIGKILL, SIGSTOP, SIGCONT
#include <errno.h>      // errno
#include <readline/readline.h> // readline
#include <ctype.h>      // isdigit()
#include "linked_list.h"   //linked list for the process pin
#define MAX_INPUT_SIZE 200
char* VALID_COMMANDS[] = {  // the commands that are valid from the input
	"bg",
	"bgkill",
	"bgstop",
	"bgstart",
	"bglist",
	"pstat"
};
node* head = NULL; // head of the linked list, 

// helper functions:
/*
   parameter:string
   read a string and return 1 if it the string contains all integers
   return 0 if there exist any  characters in the string
   (for detecting if the pid number is valid)
*/
int isNumber(char* str) {
	int i;
	for (i = 0; i < strlen(str); i++) {
		if (!isdigit(str[i])) {
			return 0; // return 0 if there contains characters
		}
	}
	return 1; // return 1 if the string can be trans to integer
}

/*
	parameter: pointer of a string 
	the function is use to read user's inptut and tokenize it and store the command 
	returns 1 on success, 0 on error
*/
int getUserInput(char** input) {
	char* read_input = readline("PMan: > ");
	if (strcmp(read_input, "") == 0) {  // return 0 if thr user didn't input anything 
		return 0;
	}
	char* token = strtok(read_input, " ");  // use strtok to tokenize the input, the first token should be the 
										  // the commande 
	int i;
	for (i = 0; i < MAX_INPUT_SIZE; i++) {
		input[i] = token;
		token = strtok(NULL, " ");
	}
	return 1;
}
/*
	parameter: string (command read from user)
	this function checks if the user's input is valid
	return it's position in the VALID_COMMANDS[] list if it's valid
	return -1 if it's invalid
*/
int check_command(char* command) {
	int i;
	for (i = 0; i < 6; i++) {
		if (strcmp(command, VALID_COMMANDS[i]) == 0) {
			return i;
		}
	}
	return -1;
}

// the command functions :

/*
	parameter: a pointer to a string which includs user's input
	creates a new child process and executes userInput[1] command
*/
void bg(char** userInput) {
	pid_t pid = fork();
	if (pid == 0) {    // child
		char* command = userInput[1];
		execvp(command, &userInput[1]);
		printf("Error: failed execute command %s\n", command);
		exit(1);
	} else if (pid > 0) {		// parent
		printf("Started background process %d\n", pid);
		 // add the node to the list
	 	addLast(&head,pid,userInput[1],1);
		sleep(1); // let the process sleep for a second 
	
}
}
/*	parameter: none
	read an print the status of the nodes from the linked list
*/
void bglist() {
	int c = count(head);
	print(head);
	printf("Total background jobs:\t%d\n", c);
}

/*
	parameter: pid
	kill a process
*/
void bgkill(pid_t pid) {
	if (search(&head,pid) == 0) {
		printf("Error:PId doesn't exist\n");
		return;
	}
	kill(pid, SIGTERM);
	sleep(1); // let the process sleep for a second so the output wouldn't keep showing up 

}

/*
	parameter: pid
	sends the STOP signal to the process pid stop it
*/
void bgstop(pid_t pid) {
	if (search(&head,pid) == 0) {
		printf("ERROR:PID doesn't exist\n");
		return;
	}
	kill(pid, SIGSTOP);
        sleep(1); // let the process sleep for a second so the output wouldn't keep showing up 
	
}

/*
	parameter: pid
	sends the continue signal to start a stopped process so the output wouldn't keep showing up 
*/
void bgstart(pid_t pid) {
	if (search(&head,pid)==0) {
		printf("ERROR:PID doesn't exist\n");
		return;
	}
	kill(pid, SIGCONT);
        sleep(1); // let the progcess sleep for a second, so the output wouldn't keep showing up 
}

/*
parameter:pid
print out the status of process base on the pid
  */
void pstat(pid_t pid) {
if (search(&head,pid) == 0){
	printf("ERROR:PID doesn't exist"); 
}else {
	char stat_file[200];
	char status_file[200];
	sprintf(stat_file, "/proc/%d/stat", pid);  
	sprintf(status_file, "/proc/%d/status", pid);

	FILE *stat = fopen(stat_file, "r");
	FILE *status = fopen(status_file, "r");

	if(stat == NULL || status == NULL){
                printf("ERROR:failed reading the file.\n");
	}else{
		char comm[200];
   		char state;
	unsigned int utime;
	unsigned int stime;
	int rss;
	char voluntary_ctxt_switches[200];
	char nonvoluntary_ctxt_switches[200];
	int trash; // stores the int that we don't need from the stat file
	char temporarily[200];
	fscanf(stat, "%d %s %c %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d ", &trash,comm, &state, &trash, &trash, &trash, &trash, &trash, &trash, &trash, &trash, &trash, &trash, &utime, &stime, &trash, &trash, &trash, &trash, &trash, &trash, &trash, &trash, &rss);
		// since we konw the type variables and their position in the file, we can just copy out the data we need in the same format
		// for example we know the second variable is comm and it's a string, we can just copy it down 

		fclose(stat);
		utime = utime/sysconf(_SC_CLK_TCK);
		stime = stime/sysconf(_SC_CLK_TCK);
		int i;
	for(i = 0; i < 41; i++){ // read the file line by line 
		fgets(temporarily, 200, status);
		if(i == 39){
			strcpy(voluntary_ctxt_switches, temporarily);
		}
		if(i == 40){
			strcpy(nonvoluntary_ctxt_switches, temporarily);
		}
	}
		fclose(status);
		printf("comm:\t%s\n", comm);
		printf("state:\t%c\n", state);
		printf("utime:\t%d\n", utime);
		printf("stime:\t%d\n", stime);
		printf("rss:\t%d\n", rss);
		printf("%s", voluntary_ctxt_switches);
		printf("%s", nonvoluntary_ctxt_switches);
		}

	}
}
/*
	parameter: the pointe of the string that contains user's input
	executes commands from input
*/
void run_input(char** input) {
	int command_position = check_command(input[0]);
		if (command_position == 0) // if command_position == 0 then we know it's a bg command
			{
			if (input[1] == NULL) { // if there's nothing comes after the bg command
									// we return error
				printf("Error: invalid command\n");
				return;
			}else{
			bg(input);
			}
		}else if (command_position == 1) // if command_position == 1 then we know it's a bgkill command
			{
			if (input[1] == NULL || !isNumber(input[1])) {// if there's nothing comes after the bg kill command
															// or the comming input is not a integer then we
															//return error
				printf("Error: invalid pid\n");
				return;
			}
			pid_t pid = atoi(input[1]);
			if (pid != 0) {
				bgkill(pid);
			}
		}else if (command_position == 2) // if command_position == 2 then we know it's a bgstop command
			{
			if (input[1] == NULL || !isNumber(input[1])) { // if there's nothing comes after the bg stop command
															// or the comming input is not a integer then we
															//return error
				printf("Error: invalid pid\n");
				return;
			}
			pid_t pid = atoi(input[1]); // trans the pid to int from the input string

			bgstop(pid);
		}else if (command_position == 3)// if command_position == 3 then we know it's a bgstart command
			{

			if (input[1] == NULL || !isNumber(input[1])) {// if there's nothing comes after the bg start command
															// or the comming input is not a integer then we
															//return error
				printf("Error: invalid pid\n");
				return;
			}
			pid_t pid = atoi(input[1]);// trans the pid to int from the input string

			bgstart(pid);
		}else if (command_position == 4) // if command_position == 4 then we know it's a bglist command
		{
			bglist();
		}else if (command_position == 5)// if command_position == 5 then we know it's a pstat command
				{
			if (input[1] == NULL || !isNumber(input[1])) {// if there's nothing comes after the pstat command
															// or the comming input is not a integer then we
															//return error
				printf("Error: invalid pid\n");
				return;
			}
			pid_t pid = atoi(input[1]); // trans the pid to int from the input string
			pstat(pid);
		}else if (command_position == -1){ // if the command_position == -1, then we know the command doesn't match any command in the list
										  // then we return error
		printf("command----%s doesn't exist", input[0]);
		}

	}

/*
	updates process list running statuses
*/
void send_singnal() {
	// part of the code are found from demo2.c
	pid_t pid;
	int	status;
	while (1) {	
		pid = waitpid(-1, &status, WCONTINUED | WNOHANG | WUNTRACED);
		if (pid > 0) {
			if (WIFSTOPPED(status)) {
				printf("%d stopped.\n",pid);
				search_and_stop(&head,pid);
			}
			if (WIFCONTINUED(status)) {
				printf("%d continued.\n",pid);
				search_and_start(&head,pid);
			}
			if (WIFSIGNALED(status)) {
		    		printf("process %d killed \n",pid);
				search_and_delete(&head,pid);	

			}
			if (WIFEXITED(status)) {
				printf("process %d terminated\n", pid);
				search_and_delete(&head,pid);
			}
		} else {
			break;
		}
	}
}

int main() {

   while (1) {
		char* userInput[MAX_INPUT_SIZE];
		int input_is_valid = getUserInput(userInput);
		send_singnal();
		if (input_is_valid == 1) {
			run_input(userInput);
		}
		send_singnal();
	}

   return 0;
}


















































