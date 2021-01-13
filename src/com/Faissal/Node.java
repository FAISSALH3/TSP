package com.Faissal;

class Node {
    public String label;
    public boolean visited; // Helps us to keep track of where we've been on the graph
    public Node(String name){
        this.label  = name;
        this.visited = false;
    }
    public void visit(){
        this.visited = true;
    }
    public void unvisit() {
        this.visited = false;
    }
}
