package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.Graph.Edge;

public class EdgeTest {
	
	private String v = "cityA";
	private String w = "cityB";
	private int weight = 3;
	private Edge e;

	@Before
	public void setUp() throws Exception {
		e = new Edge(v, w, weight);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWeight() {
		assertEquals(e.weight(),3);
	}

	@Test
	public void testFrom() {
		assertEquals(e.from(), v);
	}

	@Test
	public void testTo() {
		assertEquals(e.to(), w);
	}

}
