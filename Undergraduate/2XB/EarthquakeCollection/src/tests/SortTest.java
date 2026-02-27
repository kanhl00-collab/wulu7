package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.sort.Sort;
import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.EarthquakeT.ColorRating;
import cas.XB3.earthquake.ADT.EarthquakeT.MagType;

public class SortTest {
	
	private ArrayList<EarthquakeT> list1 = new ArrayList<>();
	private PointT p = new PointT(0,0);
	LocalDateTime d = LocalDateTime.now();
	private EarthquakeT e1 = new EarthquakeT("place","PA",d, 0, 1, 20, 2.7, MagType.M5, ColorRating.ZERO);
	private EarthquakeT e2 = new EarthquakeT("place2","PA",d, 0, 100, 20, 2.8, MagType.M5, ColorRating.ZERO);
	private EarthquakeT e3 = new EarthquakeT("place3","PA",d, 0, 10, 20, 2.6, MagType.M5, ColorRating.ZERO);
	private EarthquakeT e4 = new EarthquakeT("place4","PA",d, 10, 100, 20, 2.9, MagType.M5, ColorRating.ZERO);

	@Before
	public void setUp() throws Exception {
		list1.add(e1);
		list1.add(e2);
		list1.add(e3);
		list1.add(e4);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSortByDistanceNoEarthquake() {
		ArrayList<EarthquakeT> list2 = new ArrayList<>();
		Sort.sortByDistance(p, list2);
	}

	@Test
	public void testSortByDistanceGet0() {
		Sort.sortByDistance(p, list1);
		assertEquals(list1.get(0), e1);
	}
	
	@Test
	public void testSortByDistanceGet1() {
		Sort.sortByDistance(p, list1);
		assertEquals(list1.get(1), e3);
	}
	
	@Test
	public void testSortByDistanceGet2() {
		Sort.sortByDistance(p, list1);
		assertEquals(list1.get(2), e4);
	}
	
	@Test
	public void testSortByDistanceGet3() {
		Sort.sortByDistance(p, list1);
		assertEquals(list1.get(3), e2);
	}

	@Test
	public void testSortByMagnitudeGet0() {
		Sort.sortByMagnitude(list1);
		assertEquals(list1.get(0), e4);
	}
	
	@Test
	public void testSortByMagnitudeGet1() {
		Sort.sortByMagnitude(list1);
		assertEquals(list1.get(1), e2);
	}
	
	@Test
	public void testSortByMagnitudeGet2() {
		Sort.sortByMagnitude(list1);
		assertEquals(list1.get(2), e1);
	}
	
	@Test
	public void testSortByMagnitudeGet3() {
		Sort.sortByMagnitude(list1);
		assertEquals(list1.get(3), e3);
	}
	
	@Test
	public void testSortByMagnitudeNoEarthquake() {
		ArrayList<EarthquakeT> list2 = new ArrayList<>();
		Sort.sortByMagnitude(list2);
	}

}
