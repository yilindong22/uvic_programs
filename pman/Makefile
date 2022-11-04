
.phony all:
all: pman

pman: PMan.c
	gcc -c linked_list.c -o linked_list.o
	gcc -Wall PMan.c linked_list.o -lreadline -o PMan 

.PHONY clean:
clean:
	-rm -rf *.o *.exe
