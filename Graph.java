/*
 * Peter Hess
 * CSC 172 Project 4: Graphs - Dijkstra & MWST
 * 4/29/17
 */

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.JFrame;

public class Graph {

	static Scanner input;
	static boolean doDijk;
	static boolean doMWST;
	
	public Comparator<Vertex> vComp = new VComp();
	public Comparator<Edge> eComp = new EComp();
	
	public PriorityQueue<Vertex> dijkstraQ;
	public PriorityQueue<Edge> kruskalQ;
	public ArrayList<Edge> eList;
	private HashMap<String, Vertex> vertList;
	private int vertexCount, edgeCount;
	private double [] extrema = { Double.POSITIVE_INFINITY,  Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,  Double.NEGATIVE_INFINITY};

	public Graph() {
		vertexCount = 0;
		edgeCount = 0;
		eList = new ArrayList<Edge>();
		vertList = new HashMap<String, Vertex>();
		kruskalQ = new PriorityQueue<Edge>(eComp);
		dijkstraQ = new PriorityQueue<Vertex>(vComp);
	}

	public int vertices() {
		return vertexCount;
	}

	public int edges() {
		return edgeCount;
	}
	
	/** Kruskals find algorithm*/
	public Vertex pathStart(Vertex a){
		Vertex s1 = a;
		while(s1.path != null){
			s1 = s1.path;
		}
		return s1;
	}
	
	/** Returns the Vertex with the given name */
	public Vertex nameToVertex(String n){
		return vertList.get(n);
	}
	
	/** Insert road (edge) with roadname that connects intersections i and j */
	public void addEdge(String roadname, Vertex i, Vertex j){
		double length = getWeight(i,j);						//Calculate road length
		Edge e = new Edge(roadname, i, j, length);
		(i.adjList).add(e);									//Add to adjacency list of vertex i
		(j.adjList).add(new Edge(roadname, j, i, length));	//Add to adjacency list of vertex j
		if(doMWST){
			kruskalQ.add(e);
		}
		else{
			eList.add(e);
		}
		edgeCount++;
	}
	
	public void insertVertex(Vertex a){
		vertList.put(a.name, a);
		if(doDijk){
			dijkstraQ.add(a);
		}
		if (a.latitude < extrema[0]) {
			extrema[0] = a.latitude;
		}
		else if (a.latitude > extrema[1]) {
			extrema[1] = a.latitude;
		}
		if (a.longitude < extrema[2]) {
			extrema[2] = a.longitude;
		}
		else if (a.longitude > extrema[3]) {
			extrema[3] = a.longitude;
		}
		vertexCount++;
	}
	
	/*
	 * Kruskal's minimum weight spanning tree algorithm
	 * Returns an arraylist of edges the form the mwst for the given graph
	 */
	public ArrayList<Edge> kruskal(){
		
		double totalWeight = 0.0;
		ArrayList<Edge> mwst = new ArrayList<Edge>();
		
		while(mwst.size() < this.vertices() - 1){ //Graph is connected when mwst has one fewer edge than vertex
			if(kruskalQ.isEmpty()){
				System.out.println("Total weight is " + 69 * totalWeight + " miles.");
				return mwst; //The graph isn't connected
			}
			Edge process = kruskalQ.poll();
			Vertex h1 = pathStart(process.intID1);
			Vertex h2 = pathStart(process.intID2);
			
			if(!(h1.name).equals(h2.name)){ //If not connected, then connect
				mwst.add(process);
				totalWeight += process.getWeight();
				h1.path = h2;
			}
		}
		System.out.println("Total weight is " + 69 * totalWeight + " miles.");
		return mwst;
	}
	
	
	public ArrayList<Vertex> dijkstra(Vertex start, Vertex end){
		start.distance = 0;
		dijkstraQ.remove(start);
		dijkstraQ.add(start);
		while(!dijkstraQ.isEmpty()){
			if(end.isKnown())
			{
				break; //End dijkstra's
			}
			Vertex process = dijkstraQ.poll();
			process.setKnown();
			ListIterator<Edge> listIter = (process.adjList).listIterator();
			while(listIter.hasNext()){
				Edge test = listIter.next();
				Vertex opp = test.intID2;
				if(!opp.isKnown()){
					if(opp.getDist() > test.getWeight() + process.getDist()){
						opp.setDist(test.getWeight() + process.getDist());
						dijkstraQ.remove(opp);
						dijkstraQ.add(opp);
						opp.path = process;
					}
				}
			}
			//if an edge-end has already been processed, skip to next edge
			//Iterate through process' edge list, update distances as necessary, update priority q position if changed (remove and reinsert).
			//If distance to edgeEnd is updated, set edgeEnd.path = edgeStart (process)
		}
		System.out.println("The total distance is: " + 69 * end.distance);
		return getPath(end);
	}
	
	/**Order of vertices from start to end*/ 
	public static ArrayList<Vertex> getPath(Vertex end) {
		ArrayList<Vertex> theOrder = new ArrayList<Vertex>();
		theOrder.add(end);
		Vertex curr = end.path;
		while(curr != null){
			theOrder.add(curr);
			curr = curr.path;
		}
		return theOrder;
	}
	
	/*
	 * Constructs a graph from @param filename
	 */
	public static Graph makeGraph(String filename) throws FileNotFoundException{
		Graph g = new Graph();
		File readFile = new File(filename);

		try{
			input = new Scanner(readFile);
	    }
		catch(Exception e){
	     		System.out.println("File Not Found");
	      		return null;
	    }

		input.useDelimiter("\\t|\\n");

		while(input.hasNext()){
      		if (input.next().equals("i")){ // Add vertex
      			Vertex t = new Vertex(input.next(), input.nextFloat(), input.nextFloat());
        		g.insertVertex(t);
      		}
      		else{ //Add edge
				g.addEdge(input.next(), g.nameToVertex(input.next()), g.nameToVertex(input.next()));
      		}
    	}
		return g;
	}
	
	public static void main(String[] args){
		String filename = args[0];
		boolean show = false;
		String start = "", end = "";
		try {
			if (args[1].equals("-show")) {
				show = true;
				if (args[2].equals("-meridianmap")) {
					doMWST = true;
				} else if (args[2].equals("-directions")) {
					doDijk = true;
					start = args[3];
					end = args[4];
				}
			} else {
				if (args[1].equals("-meridianmap")) {
					doMWST = true;
				} else if (args[1].equals("-directions")) {
					doDijk = true;
					start = args[2];
					end = args[3];
				}
			}
			Graph gr = makeGraph(filename);
			ArrayList<Edge> totalEdges = new ArrayList<Edge>(gr.kruskalQ);
			if (doMWST){
				ArrayList<Edge> ans = gr.kruskal();
				if(show){
					gr.mwspShow(totalEdges, ans);
				}
				else{
					for (Edge e : ans){
						System.out.print(e.name + ", ");
					}
					System.out.println();
				}
			}else if (doDijk){
				ArrayList<Vertex> dijk = gr.dijkstra(gr.nameToVertex(start), gr.nameToVertex(end));
				if(show){
					gr.directionShow(gr.eList, dijk);
				}
				else{
					for (int i = dijk.size() - 1; i >= 0; i--){
						System.out.print(dijk.get(i).name + ", ");
					}
				}
			}
			else{
				throw new Exception("Invalid input.");
			}
		}catch(Exception l){
			System.out.println(l.toString());
		}
	}
	
	//Vertex comparator based on distance (see dijkstra's)
	class VComp implements Comparator<Vertex> {
	    public int compare(Vertex a, Vertex b) {
	        if(a.getDist() < b.getDist()){
	        	return -1;
	        }
	        if(a.getDist() > b.getDist()){
	        	return 1;
	        }
	        return 0;
	    }
	}
	
	//Edge Comparator based on edge weight (see kruskal's)
	class EComp implements Comparator<Edge> {
		public int compare(Edge a, Edge b){
	    	if(a.getWeight() < b.getWeight()){
	        	return -1;
	        }
	        if(a.getWeight() > b.getWeight()){
	        	return 1;
	        }
	        return 0;
	    }
	}
	
	/** Calculate distance between given vertices */
	public static double getWeight(Vertex a, Vertex b){
	    double x = Math.abs(b.latitude) - Math.abs(a.latitude);
	    double y = Math.abs(b.longitude) - Math.abs(a.longitude);
	    return Math.sqrt(x*x + y*y);
	}
	
	public void mwspShow(ArrayList<Edge> edgeMap, ArrayList<Edge> mst) {
		MapFrame window = new MapFrame("MWST");
		DispMap theMap = new DispMap(edgeMap, mst, null, true, extrema);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.pack();
		window.setSize(400, 400);
		window.add(theMap);
		window.setVisible(true);
	}

	public void directionShow(ArrayList<Edge> edgeMap, ArrayList<Vertex> vOrder) {
		MapFrame window = new MapFrame("Dijkstra's Shortest Path");
		DispMap theMap = new DispMap(edgeMap, null, vOrder, false, extrema);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.pack();
		window.setSize(400, 400);
		window.add(theMap);
		window.setVisible(true);
	}
}
