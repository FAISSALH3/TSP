package com.Faissal;

class Edge {
    public int value;
    public String left, right; // For convenience in construction process. Not necessary.
    public Edge(String left, String right, int value) {
        this.left = left;
        this.right = right;
        this.value = value;
    }
}