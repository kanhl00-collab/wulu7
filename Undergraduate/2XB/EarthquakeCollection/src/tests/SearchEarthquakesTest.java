package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.search.RedBlackBST;
import cas.XB3.earthquake.search.SearchEarthquakes;
import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.EarthquakeT.ColorRating;
import cas.XB3.earthquake.ADT.EarthquakeT.MagType;

public class SearchEarthquakesTest {
	private RedBlackBST<Double, EarthquakeT> bst = new RedBlackBST<>();
	private PointT p = new PointT(0,0);
	LocalDateTime d = LocalDateTime.now();
	private EarthquakeT e1 = new EarthquakeT("place","PA",d, 0, 1, 20, 2.8, MagType.M5, ColorRating.ZERO);
	private EarthquakeT e2 = new EarthquakeT("place2","ON",d, 0, 100, 20, 2.8, MagType.M5, ColorRating.ZERO);

	@Before
	public void setUp() throws Exception {
		bst.put(0.0,e1);
		bst.put(1.0,e2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearchEarthquakeInCircleCase1() {
		assertTrue(SearchEarthquakes.searchEarthquakeInCircle(bst, p, 0).size() == 0);
	}
	
	@Test
	public void testSearchEarthquakeInCircleCase2() {
		assertEquals(SearchEarthquakes.searchEarthquakeInCircle(bst, p, 1000).size(), 1);
	}
	
	@Test
	public void testSearchEarthquakeInCircleCase3() {
		assertEquals(SearchEarthquakes.searchEarthquakeInCircle(bst, p, 1000000000).size(), 2);
	}

}
