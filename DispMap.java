/*
 * Peter Hess
 * CSC 172 Project 4: Graphs - Dijkstra & MWST
 * 4/29/17
 */

import java.awt.Color; 
import java.awt.Graphics; 
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.*;

public class DispMap extends JPanel {

	private static final long serialVersionUID = 1L;
	ArrayList<Edge> edgeMap;
	ArrayList<Edge> mwst;
	ArrayList<Vertex> vertexOrder;

	private double[] bounds;

	private double yUnit;
	private double xUnit;
	private boolean displayMWST;

	public DispMap(ArrayList<Edge> graphEdges, ArrayList<Edge> mwstEdges, ArrayList<Vertex> dijkOrder, boolean dispType, double [] windowBounds) {
		setFocusable(true);
		edgeMap = graphEdges;
		mwst = mwstEdges;
		vertexOrder = dijkOrder;
		displayMWST = dispType;
		bounds = windowBounds;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(new BasicStroke(1));			//Line width = 1
		xUnit = getWidth() / (bounds[3] - bounds[2]);	//For scaling
		yUnit = getHeight() / (bounds[1] - bounds[0]);	//For scaling
		
		edgePainter(g2d);	//Paint all edges in graph
		// Display mwst or dijkstra path
		if (displayMWST) {
			minWeight(g2d);
		} else {
			shortPath(g2d);
		}
	}

	public void edgePainter(Graphics2D g2d) {
		for (Edge e : edgeMap) {
			Vertex a = e.intID1;
			Vertex b = e.intID2;
			//Calculate line coordinates: start (x1,y1) - end (x2,y2)
			int x1 = (int) ((getHeight() - Math.abs(a.latitude - Math.abs(bounds[0])) * yUnit));
			int y1 = (int) (((a.longitude * xUnit)) - bounds[2] * xUnit);
			int x2 = (int) ((getHeight() - Math.abs(b.latitude - Math.abs(bounds[0])) * yUnit));
			int y2 = (int) (((b.longitude * xUnit)) - bounds[2] * xUnit);
			g2d.drawLine(y1, x1, y2, x2);
		}
	}

	public void minWeight(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(2));	//Wider line
		g2d.setColor(Color.RED);			//Different color
		for (Edge e : mwst) { 
			Vertex a = e.intID1;
			Vertex b = e.intID2;
			//Coordinates
			int x1 = (int) ((getHeight() - Math.abs(a.latitude - Math.abs(bounds[0])) * yUnit));
			int y1 = (int) (((a.longitude * xUnit)) - bounds[2] * xUnit);
			int x2 = (int) ((getHeight() - Math.abs(b.latitude - Math.abs(bounds[0])) * yUnit));
			int y2 = (int) (((b.longitude * xUnit)) - bounds[2] * xUnit);
			g2d.drawLine(y1, x1, y2, x2);
		}
	}

	public void shortPath(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(2));	//Wider line
		g2d.setColor(Color.BLUE);			//Different color
		Vertex a = vertexOrder.get(vertexOrder.size() - 1);
		for (int i = vertexOrder.size() - 2; i >= 0; i--) {
			Vertex b = vertexOrder.get(i);
			//Coordinates
			int x1 = (int) ((getHeight() - Math.abs(a.latitude - Math.abs(bounds[0])) * yUnit));
			int y1 = (int) (((a.longitude * xUnit)) - bounds[2] * xUnit);
			int x2 = (int) ((getHeight() - Math.abs(b.latitude - Math.abs(bounds[0])) * yUnit));
			int y2 = (int) (((b.longitude * xUnit)) - bounds[2] * xUnit);
			g2d.drawLine(y1, x1, y2, x2);
			a = b;
		}
	}
}