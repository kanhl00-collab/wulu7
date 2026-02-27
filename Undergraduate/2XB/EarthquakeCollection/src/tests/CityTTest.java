package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.ADT.CityT;

public class CityTTest {
	private String cityName;
	private String province;
	private double popDensity;
	private CityT city;

	@Before
	public void setUp() throws Exception {
		cityName = "cityA";
		province = "provinceB";
		city = new CityT(cityName, province, popDensity);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCityT() {
	}

	@Test
	public void testGetCityName() {
		assertEquals(city.getCityName(), "cityA");
	}

	@Test
	public void testGetProvince() {
		assertEquals(city.getProvince(), "provinceB");
	}

	@Test
	public void testGetPopDensity() {
		assertEquals(city.getPopDensity(), 0, 0.00001);
	}

	@Test
	public void testEqualsObjectFalse() {
		CityT city2 = new CityT("cityA", "provinceC", 0);
		assertFalse(city2.equals(city));
	}
	
	@Test
	public void testEqualsObjectTrue() {
		assertTrue(city.equals(city));
	}

}
