package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.Graph.CityGraph;
import cas.XB3.earthquake.Graph.Edge;

public class CityGraphTest {
	private CityGraph cg;

	@Before
	public void setUp() throws Exception {
		cg = new CityGraph();
	}

	@Test
	public void testAddEdge() {
		cg.addEdge(new Edge("a","b",1));
	}

	@Test
	public void testAdj() {
		cg.addEdge(new Edge("a","b",1));
		cg.addEdge(new Edge("a","b",1));
		cg.addEdge(new Edge("a","c",1));
		cg.addEdge(new Edge("b","c",3));
		int i = 0;
		for (Edge e : cg.adj("a")) {
			i++;
		}
		assertTrue(i==3);
	}
	
	@Test
	public void testAdj2() {
		cg.addEdge(new Edge("a","b",1));
		cg.addEdge(new Edge("a","b",1));
		cg.addEdge(new Edge("a","c",1));
		cg.addEdge(new Edge("b","c",3));
		int i = 0;
		for (Edge e : cg.adj("b")) {
			i++;
		}
		assertTrue(i==1);
	}

}
