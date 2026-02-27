/**
 * The SearchEarthquakes class provides static methods for searching 
 * the earthquakes within the given distance of a given location in the 
 * given collection of EarthquakeT objects
 * @author Ye Fang
 * Revised: March 24, 2020
 */
package cas.XB3.earthquake.search;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;

public class SearchEarthquakes {

	/**
	 * search the list of earthquakes within the given radius of a given location in
	 * the BST of EarthquakeT
	 * 
	 * @param bst      a Red Black Tree with value of EarthquakeT
	 * @param location a PointT object that represents the objective location
	 * @param radius   a real number represents searching distance(in km) from the
	 *                 given location
	 * @return a list of EarthquakeT within the given radius of a given location in
	 *         the BST of EarthquakeT
	 */
	public static ArrayList<EarthquakeT> searchEarthquakeInCircle(RedBlackBST<Double, EarthquakeT> bst, PointT location,
			double radius) {
		ArrayList<EarthquakeT> earthquakeList = new ArrayList<>();
		// the range of latitude(min latitude and max latitude) for the given point and
		// radius
		double[] latRange = location.latFilter(radius);
		// gets the keys between min latitude and max latitude and gets the earthquakeT
		// according the keys, then add the earthquake to the list if its distance to
		// the given location is less than the given radius
		for (EarthquakeT earthquake : bst.values(latRange[0], latRange[1])) {
			if (location.distanceTo(earthquake.getPointT()) <= radius) {
				earthquakeList.add(earthquake);
			}
		}
		return earthquakeList;
	}

	// This method is to search earthquakes within the given radius of a given
	// location in the EarthquakeBag. It is used to compare the time
	// complexity between searching in EarthquakeBag and RedBlackBST
	public static ArrayList<EarthquakeT> searchInEarthquakeBag(EarthquakeBag<EarthquakeT> Earthquakebag,
			PointT location, double radius) {
		ArrayList<EarthquakeT> earthquakeList = new ArrayList<>();
		for (EarthquakeT earthquake : Earthquakebag) {
			if (location.distanceTo(earthquake.getPointT()) <= radius) {
				earthquakeList.add(earthquake);
			}
		}
		return earthquakeList;
	}
}
