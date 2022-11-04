typedef struct node
{
    int isRunning; // isrunning or stopped 
    pid_t pid;       // pid number
    char * cmd;    // the command 
    struct node* next;
} node;
void addLast(struct node **head, pid_t pid, char *cmd, int isRunning);
void print(node* n);
int count(node *head);
void search_and_delete(node **head, pid_t pid);
int search (node **head, pid_t pid);
void search_and_start(node **head, pid_t pid);
void search_and_stop(node **head, pid_t pid);

