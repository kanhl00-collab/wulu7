/**
 * Directed graph data type implemented using a HashMap
 * @author Ye Fang
 * Revised: March 24, 2020
 */
package cas.XB3.earthquake.Graph;

import java.util.HashMap;
import java.util.LinkedList;

public class CityGraph {
	// HashMap: key = string vertex, value = list of edges whose start vertex is the key
	private HashMap<String, LinkedList<Edge>> adj;

	/**
	 * Initializes an empty graph with no vertices or edges
	 */
	public CityGraph() {
		this.adj = new HashMap<>();
	}

	/**
	 * Adds the edge e to this graph
	 * 
	 * @param e the Edge object that needs to be added to this graph
	 */
	public void addEdge(Edge e) {
		if (adj.get(e.from()) == null) {
			adj.put(e.from(), new LinkedList<>());
		}
		if (!adj.get(e.from()).contains(e)) {
			adj.get(e.from()).add(e);
		}
	}

	/**
	 * Gets the list of edges adjacent to v in this graph
	 * 
	 * @param v the given vertex
	 * @return the list of edges adjacent to vertex v
	 */
	public Iterable<Edge> adj(String v) {
		return adj.get(v);
	}
}
