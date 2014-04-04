#include <stdio.h>
#include <stdlib.h>
#define MAX_LEVEL 4
#define INF (int)0x7fffffff
#define NINF -(int)0x7fffffff
typedef int value;
typedef struct node node;
struct node
{
	node* forward[MAX_LEVEL];
	node* back[MAX_LEVEL];
	value val;
};

typedef struct skip_list
{
	node* head;
} skip_list;

node* create_node(void){
	int i = 0;
	node* p = (node*)malloc(sizeof(node));
	for(;i<MAX_LEVEL;i++){
		p->forward[i] = NULL;
		p->back[i] = NULL;
	}
	return p;
}
void init(skip_list* list){
	int i = 0;
	list->head = create_node();
	list->head->val = NINF;
}

node* find0(skip_list* list, value v){
	int level = MAX_LEVEL - 1;

	node* p = list->head;
	while(level >= 0){
		
		node* next = (p->forward)[level];
		printf("search in level: %d\n", level);
		if(next == NULL || next->val > v){
			level--;
		}else if( next->val < v){
			p = next;
		}else {
			return next;
		}
		
	}
	return p;
}

node* find(skip_list* list, value v){
	node* p = find0(list, v);
	if(p->val == v){
		return p;
	}else{
		return NULL;
	}
}

void insert(skip_list* list, value v){
	int level = 0;
	int r;
	node* p = find0(list, v);
	if(p->val == v){
		return;
	}else{
		//insert after
		node* new_node = create_node();
		new_node->val = v;
		new_node->forward[level] = p->forward[level];
		if(p->forward[level] != NULL)
			p->forward[level]->back[level] = new_node;
		p->forward[level] = new_node;
		new_node->back[level] = p; 
		r = rand() % 10;
		level++;
		while(r>5 && level<MAX_LEVEL){
			
			node* back = new_node->back[level-1];
			r = rand() % 10;
			printf("insert in level: %d\n", level);
			while(back->forward[level] == NULL && back->back[0] != NULL /*head*/){
				back = back->back[level-1];
			}
			new_node->forward[level] = back->forward[level];
			if(back->forward[level]!=NULL)
				back->forward[level]->back[level] = new_node;
			back->forward[level] = new_node;
			new_node->back[level] = back;
			level++;
		}
	}
}

void print(skip_list* list){
	int level = 0;
	
	for(;level<MAX_LEVEL;level++){
		node* p = list->head;
		printf("printing level: %d\n", level);
		while(p->forward[level] != NULL){
			printf("%d\n", p->forward[level]->val);
			p = p->forward[level];
		}
	}
}

int main(){
	int i = 0;
	skip_list* list = (skip_list*)malloc(sizeof(skip_list));
	init(list);
	for(;i<20;i++)
		insert(list, rand()%100);
	
	print(list);
	return 0;
}
