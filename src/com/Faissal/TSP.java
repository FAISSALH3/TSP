package com.Faissal;

import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


class TSP {
    // The actual graph of cities
    ImmutableNetwork<Node, Edge> graph;
    int graphSize;

    // Storage variables used when searching for a solution
    Node[] route;        // store the route
    double this_distance;   // store the total distance
    double min_distance;    // store the shortest path found so far

    /**
     * Defaut constructor generates the graph and initializes storage variables
     */
    public TSP() {
        // Build the graph
        this.graph = buildGraph();
        this.graphSize = this.graph.nodes().size();

        // Initialize route variable, shared across recursive method instances
        this.route = new Node[this.graphSize];

        // Initialize distance variable, shared across recursive method instances
        this.this_distance = 0.0;
        this.min_distance = -1.0; // negative min means uninitialized
    }

    /**
     * This method actually constructs the graph.
     */
    public ImmutableNetwork<Node, Edge> buildGraph() {

        // MutableNetwork is an interface requiring a type for nodes and a type for edges
        MutableNetwork<Node, Edge> roads = NetworkBuilder.undirected().build();

        // Construct Nodes for cities,
        // and add them to a map
        String[] cities = {"Wuhan", "shanghai", "Beijing", "Tianjin", "dalian"};

        Map<String, Node> all_nodes = new TreeMap<String, Node>();
        for (int i = 0; i < cities.length; i++) {
            // Add nodes to map
            Node node = new Node(cities[i]);
            all_nodes.put(cities[i], node);

            // Add nodes to network
            roads.addNode(node);
        }

        // Construct Edges for roads,
        // and add them to a map
        String[] distances = {"Wuhan:shanghai:9239", "Wuhan:Beijing:1103", "Wuhan:Tianjin:1162", "Wuhan:dalian:1423", "shanghai:Beijing:1214", "shanghai:Tianjin:20", "Beijing:Tianjin:4", "shanghai:dalian:1076", "Tianjin:dalian:802" };
        //String[] distances = {"A:B:10","A:D:20", "A:C:15", "B:D:25", "B:C:35" , "C:D:30"};
        Map<String, Edge> all_edges = new TreeMap<String, Edge>();
        for (int j = 0; j < distances.length; j++) {
            // Parse out (city1):(city2):(distance)
            String[] splitresult = distances[j].split(":");
            String left = splitresult[0];
            String right = splitresult[1];
            String label = left + ":" + right;
            int value = Integer.parseInt(splitresult[2]);

            // Add edges to map
            Edge edge = new Edge(left, right, value);
            all_edges.put(label, edge);

            // Add edges to network
            roads.addEdge(all_nodes.get(edge.left), all_nodes.get(edge.right), edge);
        }

        // Freeze the network
        ImmutableNetwork<Node, Edge> frozen_roads = ImmutableNetwork.copyOf(roads);

        return frozen_roads;
    }

    /**
     /** Public solve method will call the recursive backtracking method to search for solutions on the graph */
    public void solve() {
        /** To solve the traveling salesman problem:
         * Set up the graph, choose a starting node, then call the recursive backtracking method and pass it the starting node.
         */

        // We need to pass a starting node to recursive backtracking method
        Node startNode = null;

        // Grab a node, any node...
        for( Node n : graph.nodes() ) {
            startNode = n;
            break;
        }

        // Visit the first node
        startNode.visit();

        // Add first node to the route
        this.route[0] = startNode;

        // Pass the number of choices made
        int nchoices = 1;

        // Recursive backtracking
        explore(startNode, nchoices );


    }

    /** Recursive backtracking method: explore possible solutions starting at this node, having made nchoices */
    /**
     * Recursive backtracking method: explore possible solutions starting at this node, having made nchoices
     * @return
     */
    public void explore(Node node, int nchoices) {
        /**
         * Solution strategy: recursive backtracking.
         */

        if (nchoices == graphSize) {
            //
            // BASE CASE
            //
            if (this.this_distance < this.min_distance || this.min_distance < 0) {
                // if this_distance < min_distance, this is our new minimum distance
                // if min_distance < 0, this is our first minimium distance
                this.min_distance = this.this_distance;


                printSolution();


            } else {
                printFailure();
            }

        } else {
            //
            // RECURSIVE CASE
            //
            Set<Node> neighbors = graph.adjacentNodes(node);
            for (Node neighbor : neighbors) {
                if (neighbor.visited == false) {

                    int distance_btwn = 0;

                    for (Edge edge : graph.edgesConnecting(node, neighbor)) {
                        distance_btwn = edge.value;


                    }


                    // Make a choice

                    this.route[nchoices] = neighbor;
                    neighbor.visit();
                    this.this_distance += distance_btwn;

                    // Explore the consequences
                    explore(neighbor, nchoices+ 1);

                    // Unmake the choice

                    this.route[nchoices] = null;
                    neighbor.unvisit();
                    this.this_distance -= distance_btwn;

                }

                // Move on to the next choice (continue loop)
            }


        } // End base/recursive case


    }


    public void printSolution() {

        System.out.print("***********\tNEW SOLUTION\t");
        System.out.println("Route: " + this.route
                + "\tDistance: " + this.min_distance);
    }

    /**
     * Do nothing with failed path
     */
    public void printFailure() {
        //
    }
}