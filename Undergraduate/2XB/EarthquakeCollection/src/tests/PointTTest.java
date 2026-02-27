package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.ADT.PointT;

public class PointTTest {
	private double x;
	private double y;
	private PointT p;

	@Before
	public void setUp() throws Exception {
		p = new PointT(x,y);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = RuntimeException.class)
	public void testPointTException() {
		double x2 = 91;
		double y2 = -108;
		PointT p2 = new PointT(x2, y2);
	}
	
	@Test
	public void testPointT() {
		double x2 = -83;
		double y2 = -198;
		PointT p2 = new PointT(x2, y2);
	}

	@Test
	public void testDistanceToCase1() {
		assertEquals(p.distanceTo(new PointT(38.2, -79.32)), 9076, 1);
	}
	
	@Test
	public void testDistanceToCase2() {
		double x2 = -83;
		double y2 = -198;
		PointT p2 = new PointT(x2, y2);
		assertEquals(p2.distanceTo(new PointT(38.2, -79.32)), 14600, 5);
	}

	@Test
	public void testLatFilterCase1Smaller() {
		assertEquals(p.latFilter(80)[0], -0.7195, 0.001);
	}
	
	@Test
	public void testLatFilterCase2Larger() {
		double x2 = -83;
		double y2 = -198;
		PointT p2 = new PointT(x2, y2);
		assertEquals(p2.latFilter(80)[1], -82.2805, 0.001);
	}

	@Test
	public void testEqualsPointT() {
		assertTrue(p.equals(new PointT(0.00000001,0.00000001)));
	}
	
	@Test
	public void testEqualsPointTCase2() {
		double x2 = -83;
		double y2 = -198;
		PointT p2 = new PointT(x2, y2);
		assertTrue(p2.equals(new PointT(-83,162)));
	}

	@Test
	public void testGetLat() {
		assertEquals(p.getLat(), 0, 0.000001);
	}

	@Test
	public void testGetLong() {
		double x2 = -83;
		double y2 = -198;
		PointT p2 = new PointT(x2, y2);
		assertEquals(p2.getLong(), 162, 0.000001);
	}

}

