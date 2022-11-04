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

typedef struct node
{
    int isRunning; // isrunning or stopped 
    pid_t pid;       // pid number
    char * cmd;    // the command 
    struct node* next;
} node;

/*
parameter: node list, node new_node (node that adding to the list)
 creat a node add the node to the end of the list
source: https://www.log2base2.com/data-structures/linked-list/deleting-a-node-in-linked-list.html
 * */
void addLast(struct node **head, pid_t pid, char *cmd, int isRunning)
{
    //create a new node
    struct node *temp = malloc(sizeof(struct node));
    temp->pid = pid;
    temp->cmd = cmd;
    temp->isRunning = isRunning;
    temp->next     = NULL;

    //if head is NULL, it is an empty list
    if(*head == NULL)
         *head = temp;
    //Otherwise, find the last node and add the newNode
    else
    {
        struct node *lastNode = *head;
        

        //last node's next address will be NULL.
        while(lastNode->next != NULL)
        {
            lastNode = lastNode->next;
        }

        //add the newNode at the end of the linked list
        lastNode->next = temp;
      

    }

}
/*
parameter : node head(the head of the linked list) 
Go through the list and print the pid and command of all of the nodes
*/
void print(node* head)
{
    node* p = head;
    while (p != NULL) {
		char* stopped = "";
		if (p->isRunning == 0) {
			stopped = "(stopped)";
		}
		printf("%d:\t %s %s\n", p->pid, p->cmd, stopped);
				p = p->next;

	}
}

/*
parameter : node head
    return the number of elements in the list
*/
int count(node *head)
{
    node* curr = head;
    int num = 0;
    while(curr != NULL)
    {
        curr = curr->next;
        num++;
    }
    return num;
}

/*
parameter: the head of the linked list, pid of the node we are looking for
search the node base on the pid
*/
int search (node **head, pid_t pid){
     // node curr = the head node
    node *curr = *head;
    // Search for the pid 
    while (curr != NULL && curr->pid != pid) {
        curr = curr->next;
    }
    // If the pid doesn't exist in the list then return 0
    if (curr == NULL){
        return 0;
    }
    // else we will return 1
    return 1;

}
/*
parameter: the head of the linked list, pid of the node we are looking for
search the node and stop it
 * */

void search_and_stop(node **head, pid_t pid){
	struct node *curr = *head;
    // Search for the pid 
    while (curr != NULL && curr->pid != pid) {
        curr = curr->next;
    }
    curr->isRunning = 0;
}

/*
parameter: the head of the linked list, pid of the node we are looking for
search the node and start it
 * */

void search_and_start(node **head, pid_t pid){
        struct node *curr = *head;
    // Search for the pid 
    while (curr != NULL && curr->pid != pid) {
        curr = curr->next;
    }
    curr->isRunning = 1;
}
/*
parameter : node head, int pid (pid number)
search and delete a node and rearrange the list
source code: https://www.log2base2.com/data-structures/linked-list/deleting-a-node-in-linked-list.html
*/

void search_and_delete(node **head, pid_t pid)
{
    //temp is used to freeing the memory
      struct node *temp;

      //key found on the head node.
      //move to head node to the next and free the head.
      if((*head)->pid == pid)
      {
          temp = *head;    //backup to free the memory
          *head = (*head)->next;
          free(temp);
      }
      else
      {
          struct node *current  = *head;
          while(current->next != NULL)
          {
              //if yes, we need to delete the current->next node
              if(current->next->pid == pid)
	      { 
                  temp = current->next;
                  //node will be disconnected from the linked list.
                  current->next = current->next->next;
                  free(temp);
                  break;
              }
              //Otherwise, move the current node and proceed
              else
                  current = current->next;
          }
      }
}
