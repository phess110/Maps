/*
 * Peter Hess
 * CSC 172 Project 4: Graphs - Dijkstra & MWST
 * 4/29/17
 */

public class Edge {
	
	public String name;
	public Vertex intID1;
	public Vertex intID2;
	public double weight;
	
	public Edge(String ID, Vertex one, Vertex two, double dist){
		name = ID;
		intID1 = one;
		intID2 = two;
		weight = dist;
	}
	
	public double getWeight(){
		return weight;
	}
}