// Yilin Dong
// V00928413
// highly inspired by A2-hint_detailed.c

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/time.h>

// some code are inspired by A2-hint_detailed.c
struct customer_info{ /// use this struct to record the customer information read from customers.txt
    int user_id;
	int class_type; //(0 = economy class, 1 = business class)
	int service_time;
	int arrival_time;
	int clerk_id; // which clerk is serving the customer
	double start_time; // start time for waiting
	double end_time; // end of customer's waitting time 
	int position; // the position of the customer
};

struct clerk{
	int clerk_id;
       	int state; // 0 means is serving some custmer, 1 means free

};
// global variables
struct customer_info* customers; // all customers
struct customer_info* queue[2];	//queues queue [0] represents economy class, queue[1] represents business class
struct clerk clerks[5];		//list  clerks
int econ_length = 0;	// length of econnomy queue
int busi_length = 0; // length of busuiness queue
float econ_waitTime = 0;	//average wait times of  economy
float busi_waitTime = 0;        // average wait time of business
int total_lines = 0;//total line lengths
int econ_lines = 0; // line length of econ class
int busi_lines = 0; // line length of busi class
struct timeval init_time; ;//ACS's start time 
pthread_mutex_t mutex[6];// 0: queue, 1: clerk1, 2:clerk2, 3:clerk3, 4:clerk4 5:clerk5
pthread_cond_t convar[7]; //0:econ class, 1: busniness class, 2:clerk1, 3:clerk2, 4:clerk3, 5:clerk4, 6:clerk5

// parameter customer cus, int class
// add the customer to the queue, class represents the customer's class
void add_to_queue(struct customer_info* cus){
	if (cus -> class_type == 0){ // if the custormer is in economy class
		queue[0][econ_length] = *cus;
		econ_length++; // add 1 to the length  of the queue 
	}else if(cus -> class_type == 1 ){ // if the customer is in business class	
		queue[1][busi_length] = *cus;
		busi_length++; 
	}
}
// parameter int class
// pop the customer from the queue
int pop_queue(int class){
	int index = queue[class][0].position;
	int i;
	int length =0;
	if(class == 0){
		length = econ_length;
	}else{
		length = busi_length;
	}
	for(i = 0; i < length-1; i++){
		queue[class][i] = queue[class][i+1];
	}
	if(class == 0){
	econ_length--;
	}else {
	busi_length--;	
	}
	return index;
	
}
// parameter: null
// return the current time
float curTime(){
	struct timeval now;
	gettimeofday(&now, NULL);
	return (now.tv_sec - init_time.tv_sec) + (now.tv_usec - init_time.tv_usec)/1000000.0f;
}
// parameter: char* fikle
// reade the file and put the customers in to customers( which is a list stores all the customers's ndoes)
void read_file(char* file){
        FILE* fp = fopen(file, "r");
        if(fp == NULL){
                printf("Failed reading the file");
                exit(1);
        }
        fscanf(fp, "%d", &total_lines); // scan the file find the number of total lines
        queue[0] = (struct customer_info*) malloc(total_lines * sizeof(struct customer_info));
        queue[1] = (struct customer_info*) malloc(total_lines * sizeof(struct customer_info));
        customers = (struct customer_info*) malloc(total_lines * sizeof(struct customer_info));
        struct customer_info* temp;  // temporary variable to store each line as nodes
        temp = (struct customer_info*) malloc( sizeof(struct customer_info));

        int lines =0;
        for (int i =0; i < total_lines;i++){  // go through the file
                fscanf(fp, "%d:%d,%d,%d", &temp->user_id, &temp->class_type, &temp->arrival_time, &temp->service_time);
                // printf("%d:%d,%d,%d", temp->user_id, temp->class_type, temp->arrival_time, temp->service_time);
                // just testing if scan is working
                temp->position = lines; // it's index in the list
                temp->clerk_id = -1;  // set it's initial clerk to -1 
                customers[lines] = *temp;  // sotre the nodes into the list
                lines++;
                if (temp-> class_type == 0){
                        econ_lines++;
                }else{
                        busi_lines++;
                }
        }
        total_lines = lines;
        fclose(fp);
}

//parameter void cus_info
// function entry for customer threads
void* customer_entry(void * cus_info){
        struct customer_info *p_myInfo = (struct customer_info*)cus_info;
        usleep(p_myInfo->arrival_time * 100000); // usleep has unit of mircroseconds, therefore times 100000
	fprintf(stdout, "A customer arrives: customer ID %2d. \n", p_myInfo->user_id);
	if(pthread_mutex_lock(&mutex[0]) != 0){
		printf("Mutex lock error\n");
		exit(1);
	}
	add_to_queue(p_myInfo);
	int queue_l = 0; // queue length base on cu_info's class type
	if(p_myInfo-> class_type == 0){
		queue_l = econ_length;
	}else if (p_myInfo->class_type == 1){
		queue_l = busi_length;
	}
	printf("A customer enters a queue: the queue ID %1d, and length of the queue %2d.\n", p_myInfo->class_type, queue_l);
	p_myInfo->start_time = curTime(); // stores the time that customer enters a queue
	while(p_myInfo->clerk_id == -1){ // while this customer is not being servised
		if(pthread_cond_wait(&convar[p_myInfo->class_type], &mutex[0]) != 0){ // cond_var of selected queue mutexLock of the queue
			printf("Waitting failed\n");
			exit(1);
		}
	}
	p_myInfo->end_time = curTime(); // getting service
	if ( p_myInfo -> class_type ==0){
		econ_waitTime += (p_myInfo->end_time - p_myInfo->start_time);
	}else{
                busi_waitTime += (p_myInfo->end_time - p_myInfo->start_time);

	}
	printf("A clerk starts serving a customer: start time %.2f, the customer ID %2d, the clerk ID %1d.\n", p_myInfo->end_time, p_myInfo->user_id, p_myInfo->clerk_id);
	if(pthread_mutex_unlock(&mutex[0]) != 0){ // unlock the mutex queue
		printf("Unlock mutex failed\n"); // catch the error
		exit(1);
	}
	usleep(p_myInfo->service_time *  100000); 
	printf("A clerk finishes serving a customer: end time %.2f, the customer ID %2d, the clerk ID %1d.\n", curTime(), p_myInfo->user_id, p_myInfo->clerk_id);
	int temp_clerk = p_myInfo->clerk_id;
	clerks[temp_clerk-1].state = 0; // set the clerk's status to free 
	if(pthread_cond_signal(&convar[temp_clerk+1]) != 0){ //  Notify the clerk that service is finished, it can serve another customer
		printf("Signal convar failed\n");
		exit(1);
	}

	return NULL;
}
// parameter : void clerkNUm
// function entry for clerk threads
void *clerk_entry(void * clerkNum){
	struct clerk* clerk_Info = (struct clerk*) clerkNum;
	while(1){ 
		if(pthread_mutex_lock(&mutex[0]) != 0){ // lock the queue and check is it working
			printf("Lock mutex failed\n");
			exit(1);
		}
	int index = 0;
	if(busi_length > 0 || econ_length >0){ // if the business queue or the economy queue is not empty
		if (busi_length >0){
		index = pop_queue(1); // pop from the business class
		customers[index].clerk_id = clerk_Info->clerk_id;
		clerks[clerk_Info->clerk_id-1].state = 1;
		if (pthread_cond_broadcast(&convar[1]) != 0){
			printf("Broadcast error "); // catch the error
		
		}
		if(pthread_mutex_unlock(&mutex[0]) != 0){
			printf("mutex unlock error"); // catch the error
		
		} //unlock the queue
		}else if (econ_length >0){
		index = pop_queue(0); // pop from the econ class
                customers[index].clerk_id = clerk_Info->clerk_id;
                clerks[clerk_Info->clerk_id-1].state = 1;
               if( pthread_cond_broadcast(&convar[0]) != 0  ){
	       		 printf("Broadcast error ");
	       }
		if(pthread_mutex_unlock(&mutex[0])!=0){
                    printf("mutex unlock error"); // catch the error

		}// unlock the queue
		}
		}else{
		if(pthread_mutex_unlock(&mutex[0])!=0){
	               printf("mutex unlock error"); // catch the error
		} // unlock the queue 
		usleep(250);	// make sure that all the other waiting threads have already got back
		}
		pthread_mutex_lock(&mutex[clerk_Info-> clerk_id]); // lock the clerk 
		if(clerks[(clerk_Info->clerk_id)-1].state){ // if state == 1, then it's busy
			if(pthread_cond_wait(&convar[clerk_Info->clerk_id+1], &mutex[clerk_Info->clerk_id]) != 0){
				printf("Waitting failed\n");
				exit(1);
			}
		}
			pthread_mutex_unlock(&mutex[clerk_Info->clerk_id]);
	}
return NULL;
}
	
int main(int argc, char* argv[]){
	if(argc != 2){
		printf("INPUT IS INVALID\nPleause use ./ACS <file.txt> \n");
		exit(1);
	}
	gettimeofday(&init_time, NULL);
	read_file(argv[1]);
	int i = 0 ;
	for(i = 0; i < 7; i++){//initialize mutex and convar
		if(i < 5){//set clerk id and availability
			clerks[i].clerk_id = i+1;
			clerks[i].state = 0;
		}
		if(i < 6 && pthread_mutex_init(&mutex[i], NULL) != 0){ //
			printf("Error: failed to initialize mutex.\n");
			exit(1);
		}
		if(pthread_cond_init(&convar[i], NULL) != 0){
			printf("Error: failed to initialize convar.\n");
			exit(1);
		}
	}
	for(i = 0; i < 5; i++){//create clerk threads
		pthread_t clerkThread;
		if(pthread_create(&clerkThread, NULL, clerk_entry, (void*)&clerks[i]) != 0){
			printf("Error: failed to create thread.\n");
			exit(1);
		}
	}
	pthread_t customerThread[total_lines];
	for(i = 0; i < total_lines; i++){//create customer threads
		if(pthread_create(&customerThread[i], NULL, customer_entry, (void*)&customers[i]) != 0){
			printf("Error: failed to create thread.\n");  // check is it working?
			exit(1);
		}
	}
	for(i = 0; i < total_lines; i++){//join customer threads
		pthread_join(customerThread[i], NULL);
	}
	printf("The average waiting time for all customers in the system is: %.2f seconds.\n", ((econ_waitTime + busi_waitTime)/total_lines)); // print the average time of all customers
	printf("The average waiting time for all business-class customers is: %.2f seconds.\n", busi_waitTime/busi_lines);
	printf("The average waiting time for all economy-class customers is: %.2f seocnds.\n", econ_waitTime/econ_lines);

	return 0;
}


