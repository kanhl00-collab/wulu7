/**
 * The Edge class represents a ADT of weighted edge in an EdgeWeighted Graph
 * @author Ye Fang
 * Revised: March 24, 2020
 */
package cas.XB3.earthquake.Graph;

public class Edge {
	private String v;
	private String w;
	private int weight;

	/**
	 * Initializes an edge object between vertices v and w of the given weight
	 * 
	 * @param v      the start vertex
	 * @param w      the destination vertex
	 * @param weight the weight of this edge
	 */
	public Edge(String v, String w, int weight) {
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	/**
	 * Returns the weight of this edge
	 * 
	 * @return the weight of this edge
	 */
	public int weight() {
		return this.weight;
	}

	/**
	 * Returns the start point of this edge
	 * 
	 * @return the start point of this edge
	 */
	public String from() {
		return v;
	}

	/**
	 * Returns the destination point of this edge
	 * 
	 * @return the destination point of this edge
	 */
	public String to() {
		return w;
	}

}
