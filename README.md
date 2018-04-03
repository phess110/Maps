# Maps
Implements Dijkstra and Kruskal's. Draws maps using Java graphics.

Run Graph.java with appropriate arguments.

First argument should be the formatted text file with graph data. 
Next arguments should be: 

	-show 		: Displays the output using Java graphics
	-meridianmap	: Runs Kruskal's, outputs MWST
	-directions	: Followed by two vertex names (see data files)
 
The program works by taking a formatted textfile as input. Intersections on the graph are stored as vertices in a hash table and roads are stored as edges in an adjacency list. Kruskal's runs with a priority queue of edges. Dijkstra's uses a priority queue of vertices (the shortest distance being first).
  
