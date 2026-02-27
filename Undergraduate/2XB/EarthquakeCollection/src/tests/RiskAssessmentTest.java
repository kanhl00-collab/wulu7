package tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.search.RedBlackBST;
import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.EarthquakeT.ColorRating;
import cas.XB3.earthquake.ADT.EarthquakeT.MagType;
import cas.XB3.earthquake.riskAssessment.RiskAssessment;

public class RiskAssessmentTest {

	@Test
	public void testgetRisk() {
		RedBlackBST<Double, EarthquakeT> bst = new RedBlackBST<>();
		PointT p1 = new PointT(0, 0);
		assertEquals(new RiskAssessment(bst,p1).getRisk(),0);
	}

	@Test
	public void testGetCity() {
		RedBlackBST<Double, EarthquakeT> bst = new RedBlackBST<>();
		PointT p1 = new PointT(0, 0);
		assertEquals(new RiskAssessment(bst,p1).getCity(),null);
	}
	
	@Test
	public void testGetFrequency() {
		RedBlackBST<Double, EarthquakeT> bst = new RedBlackBST<>();
		PointT p1 = new PointT(0, 0);
		assertEquals(new RiskAssessment(bst,p1).getFrequency(),0);
	}
	
	@Test
	public void testGetPopulationDensity() {
		RedBlackBST<Double, EarthquakeT> bst = new RedBlackBST<>();
		PointT p1 = new PointT(0, 0);
		assertEquals(new RiskAssessment(bst,p1).getPopulationDensity(),0,0.0000001);
	}
	
	@Test
	public void testOverallRating() {
		RedBlackBST<Double, EarthquakeT> bst = new RedBlackBST<>();
		PointT p1 = new PointT(0, 0);
		assertEquals(new RiskAssessment(bst,p1).OverallRating(0,0,0),0,0.0000001);
	}
	
	//the method to get population is turned private.
	/**
	@Test
	public void testGetPopulation() {
		assertEquals(RiskAssessment.getPopulation("NOSUCHCITY"),0,0.0001);
	}
	*/

	// method to get frequency is turned private
	/*
	@Test
	public void testFrequencyCase1() {
		ArrayList<EarthquakeT> list1 = new ArrayList<>();
		LocalDateTime d = LocalDateTime.now();
		EarthquakeT e1 = new EarthquakeT("place","PA",d, 72, 63, 20, 2.7, MagType.M5, ColorRating.ZERO);
		EarthquakeT e2 = new EarthquakeT("place2","PA",d, 72, 63, 20, 2.9, MagType.M5, ColorRating.ZERO);
		list1.add(e1);
		list1.add(e2);
		assertEquals(RiskAssessment.getFrequency(list1), 2);
	}
	
	@Test
	public void testFrequencyCase2() {
		ArrayList<EarthquakeT> list1 = new ArrayList<>();
		LocalDateTime d = LocalDateTime.now();
		//EarthquakeT e1 = new EarthquakeT("place",d, 72, 63, 20, 2.7, MagType.M5, ColorRating.ZERO);
		//EarthquakeT e2 = new EarthquakeT("place2",d, 72, 63, 20, 2.9, MagType.M5, ColorRating.ZERO);
		assertEquals(RiskAssessment.getFrequency(list1), 0);
	}
	*/

	// method to get average magnitude is turned private
	/*
	@Test
	public void testAverageMangenitudeCase1() {
		ArrayList<EarthquakeT> list1 = new ArrayList<>();
		assertEquals(RiskAssessment.getAverageMagnitude(list1), 0, 0.0000001);
	}
	

	@Test
	public void testAverageMangenitudeCase2() {
		ArrayList<EarthquakeT> list1 = new ArrayList<>();
		LocalDateTime d = LocalDateTime.now();
		EarthquakeT e1 = new EarthquakeT("place","PA",d, 72, 63, 20, 2.8, MagType.M5, ColorRating.ZERO);
		list1.add(e1);
		assertEquals(RiskAssessment.getAverageMagnitude(list1), 2.8, 0.0000001);
	}
	
	@Test
	public void testAverageMangenitudeCase3() {
		ArrayList<EarthquakeT> list1 = new ArrayList<>();
		LocalDateTime d = LocalDateTime.now();
		EarthquakeT e1 = new EarthquakeT("place","PA",d, 72, 63, 20, 2.8, MagType.M5, ColorRating.ZERO);
		EarthquakeT e2 = new EarthquakeT("place2","PA",d, 72, 63, 20, 2.8, MagType.M5, ColorRating.ZERO);
		list1.add(e1);
		list1.add(e2);
		assertEquals(RiskAssessment.getAverageMagnitude(list1), 2.8, 0.0000001);
	}
	
	@Test
	public void testAverageMangenitudeCase4() {
		ArrayList<EarthquakeT> list1 = new ArrayList<>();
		LocalDateTime d = LocalDateTime.now();
		EarthquakeT e1 = new EarthquakeT("place","PA",d, 72, 63, 20, 2.7, MagType.M5, ColorRating.ZERO);
		EarthquakeT e2 = new EarthquakeT("place2","PA",d, 72, 63, 20, 2.9, MagType.M5, ColorRating.ZERO);
		list1.add(e1);
		list1.add(e2);
		assertEquals(RiskAssessment.getAverageMagnitude(list1), 2.8, 0.0000001);
	}
	*/
	
	@Test
	public void testFrequencyRatingCase1() {
		assertEquals(RiskAssessment.frequencyRating(0),0);
	}
	
	@Test
	public void testFrequencyRatingCase2() {
		assertEquals(RiskAssessment.frequencyRating(1),1);
	}
	
	@Test
	public void testFrequencyRatingCase3() {
		assertEquals(RiskAssessment.frequencyRating(9),1);
	}
	
	@Test
	public void testFrequencyRatingCase4() {
		assertEquals(RiskAssessment.frequencyRating(10),2);
	}
	
	@Test
	public void testFrequencyRatingCase5() {
		assertEquals(RiskAssessment.frequencyRating(100),3);
	}
	
	@Test
	public void testFrequencyRatingCase6() {
		assertEquals(RiskAssessment.frequencyRating(1000),4);
	}
	
	@Test
	public void testFrequencyRatingCase7() {
		assertEquals(RiskAssessment.frequencyRating(999),3);
	}

	@Test
	public void testMagnitudeRatingCase1() {
		assertEquals(RiskAssessment.magnitudeRating(0),0,0.00000001);
	}
	
	@Test
	public void testMagnitudeRatingCase2() {
		assertEquals(RiskAssessment.magnitudeRating(0.3),0,0.00000001);
	}
	@Test
	public void testMagnitudeRatingCase3() {
		assertEquals(RiskAssessment.magnitudeRating(1),1,0.00000001);
	}
	@Test
	public void testMagnitudeRatingCase4() {
		assertEquals(RiskAssessment.magnitudeRating(3.4),1,0.00000001);
	}
	@Test
	public void testMagnitudeRatingCase5() {
		assertEquals(RiskAssessment.magnitudeRating(4),2,0.00000001);
	}
	@Test
	public void testMagnitudeRatingCase6() {
		assertEquals(RiskAssessment.magnitudeRating(5.8),2,0.00000001);
	}
	@Test
	public void testMagnitudeRatingCase7() {
		assertEquals(RiskAssessment.magnitudeRating(6),3,0.00000001);
	}
	@Test
	public void testMagnitudeRatingCase8() {
		assertEquals(RiskAssessment.magnitudeRating(6.9),3,0.00000001);
	}
	@Test
	public void testMagnitudeRatingCase9() {
		assertEquals(RiskAssessment.magnitudeRating(9.9),4,0.00000001);
	}
	

	@Test
	public void testPopulationdensityRatingCase1() {
		assertEquals(RiskAssessment.populationdensityRating(0),0);
	}
	
	@Test
	public void testPopulationdensityRatingCase2() {
		assertEquals(RiskAssessment.populationdensityRating(99),0);
	}
	
	@Test
	public void testPopulationdensityRatingCase3() {
		assertEquals(RiskAssessment.populationdensityRating(1000),1);
	}
	
	@Test
	public void testPopulationdensityRatingCase4() {
		assertEquals(RiskAssessment.populationdensityRating(5000),2);
	}
	
	@Test
	public void testPopulationdensityRatingCase5() {
		assertEquals(RiskAssessment.populationdensityRating(100000),2);
	}
	


}
