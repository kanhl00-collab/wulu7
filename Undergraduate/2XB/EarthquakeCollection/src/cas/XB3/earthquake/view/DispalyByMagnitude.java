/**
 * A DisplayByMagnitude implements ViewList interface   
 * 
 * @author Ye Fang
 * Revised: April 9, 2020
 */
package cas.XB3.earthquake.view;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.sort.Sort;

public class DispalyByMagnitude implements ViewList {

	/**
	 * Display the list of earthquakes in descending order based on their magnitude
	 * 
	 * @param earthquakeList a list of EarthquakeT objects
	 * @param location a PointT object represent the objective location
	 */
	public void display(ArrayList<EarthquakeT> earthquakeList, PointT location) {
		Sort.sortByMagnitude(earthquakeList);
		if (earthquakeList.size() == 0)
			System.out.println("There's no earthquake!");
		else {
			System.out.println("The earthquakes are displayed in the descending order based on the magnitude:");
			System.out.printf("%-15s%-20s%-15s%-40s\n", "Magnitude", "Earthquake class", "Date", "City");
			for (EarthquakeT eq : earthquakeList) {
				System.out.printf("%-15.1f%-20s%-15d%-40s\n", eq.getMag(), eq.getColor(), eq.getDate().getYear(),
						eq.getPlace());
			}
			System.out.println();
		}
	}

}
