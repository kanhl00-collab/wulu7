package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.ADT.CityPostT;
import cas.XB3.earthquake.ADT.PointT;

public class CityPostTTest {
	private String cityName = "cityA";
	private PointT p = new PointT(0,0);
	private CityPostT cp;

	@Before
	public void setUp() throws Exception {
		cp = new CityPostT(cityName, 0, 0);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGetPoint() {
		assertTrue(p.equals(cp.getPoint()));
	}

	@Test
	public void testGetCityName() {
		assertEquals(cityName, cp.getCityName());
	}

}
