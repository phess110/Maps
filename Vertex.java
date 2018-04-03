/*
 * Peter Hess
 * CSC 172 Project 4: Graphs - Dijkstra & MWST
 * 4/29/17
 */

import java.util.LinkedList;

public class Vertex {
	static final double INFINITY = Double.POSITIVE_INFINITY;

	public String name; 
	public Vertex path; 	// Previous vertex in shortest path
	public double latitude;
	public double longitude;
	public double distance; // Distance from initial vertex
	public boolean known; 	// Visited marker
	public LinkedList<Edge> adjList; //List of all roads that head through this vertex

	public Vertex(String intID, double lat, double lon) {
		name = intID;
		known = false;
		latitude = lat;
		longitude = lon;
		path = null;
		distance = INFINITY;
		known = false;
		adjList = new LinkedList<Edge>();
	}
	
	public double getDist(){
		return distance;
	}

	public void setDist(double val) {
		distance = val;
	}

	public void setKnown() {
		known = true;
	}

	public boolean isKnown() {
		return known;
	}
}
