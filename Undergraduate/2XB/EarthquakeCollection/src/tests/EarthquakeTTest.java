package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.ADT.EarthquakeT.ColorRating;
import cas.XB3.earthquake.ADT.EarthquakeT.MagType;

public class EarthquakeTTest {
	private EarthquakeT e1;
	private EarthquakeT e2;
	private EarthquakeT e3;
	private EarthquakeT e4;
	private EarthquakeT e5;
	private EarthquakeT e6;
	private EarthquakeT e7;
	private EarthquakeT e8;
	LocalDateTime d = LocalDateTime.now();

	@Before
	public void setUp() throws Exception {
		e1 = new EarthquakeT("place","pA",d, 72, 63, 20, 2.8, MagType.M5, ColorRating.ZERO);
		e2 = new EarthquakeT("place","pA",LocalDateTime.now(), 25, 13, 69, 7.3, EarthquakeT.MagType.MS, EarthquakeT.ColorRating.ORANGE);
		e3 = new EarthquakeT("place","pA",d, 71, 63, 20, 2.8, EarthquakeT.MagType.M5, EarthquakeT.ColorRating.ZERO);
		e4 = new EarthquakeT("place2","pA",d, 72, 63, 20, 2.8, EarthquakeT.MagType.M5, EarthquakeT.ColorRating.ZERO);
		e5 = new EarthquakeT("place","pA",d, 72, 63, 60, 2.8, EarthquakeT.MagType.M5, EarthquakeT.ColorRating.ZERO);
		e6 = new EarthquakeT("place","pA",d, 72, 63, 20, 2.7, EarthquakeT.MagType.M5, EarthquakeT.ColorRating.ZERO);
		e7 = new EarthquakeT("place","pA",d, 72, 63, 20, 2.8, EarthquakeT.MagType.MS, EarthquakeT.ColorRating.ZERO);
		e8 = new EarthquakeT("place","pA",d, 72, 63, 20, 2.8, EarthquakeT.MagType.M5, EarthquakeT.ColorRating.RED);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEarthquakeT() {
	}

	@Test
	public void testGetPlace() {
		assertEquals(e1.getPlace(), "place");
	}

	@Test
	public void testGetPointT() {
		assertTrue(e1.getPointT().equals(new PointT(72, 63)));
	}

	@Test
	public void testGetMag() {
		assertEquals(e1.getMag(),2.8, 0.0000001);
	}

	@Test
	public void testGetDph() {
		assertEquals(e1.getDph(),20, 0.0000001);
	}

	@Test
	public void testGetMagitudeType() {
		assertEquals(e1.getMagitudeType(), MagType.M5);
	}

	@Test
	public void testGetColor() {
		assertEquals(e1.getColor(), ColorRating.ZERO);
	}

	@Test
	public void testCompareToCase1() {
		assertEquals(e1.compareTo(e2), -1);
	}
	
	@Test
	public void testCompareToCase2() {
		assertEquals(e2.compareTo(e1), 1);
	}
	
	@Test
	public void testCompareToCase3() {
		assertEquals(e1.compareTo(e1), 0);
	}

	@Test
	public void testEqualsEarthquakeTCase1() {
		assertFalse(e1.equals(e2));
	}
	
	@Test
	public void testEqualsEarthquakeTCase2() {
		assertFalse(e1.equals(e2));
	}
	@Test
	public void testEqualsEarthquakeTCase3() {
		assertFalse(e1.equals(e3));
	}
	@Test
	public void testEqualsEarthquakeTCase4() {
		assertFalse(e1.equals(e4));
	}
	@Test
	public void testEqualsEarthquakeTCase5() {
		assertFalse(e1.equals(e5));
	}
	@Test
	public void testEqualsEarthquakeTCase6() {
		assertFalse(e1.equals(e6));
	}
	@Test
	public void testEqualsEarthquakeTCase7() {
		assertFalse(e1.equals(e7));
	}
	@Test
	public void testEqualsEarthquakeTCase8() {
		assertFalse(e1.equals(e8));
	}
	@Test
	public void testEqualsEarthquakeTCase9() {
		assertTrue(e1.equals(e1));
	}

}
