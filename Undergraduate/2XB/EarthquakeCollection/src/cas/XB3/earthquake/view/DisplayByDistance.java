/**
 * A DisplayByDistance implements ViewList interface   
 * 
 * @author Ye Fang
 * Revised: April 9, 2020
 */
package cas.XB3.earthquake.view;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.sort.Sort;

public class DisplayByDistance implements ViewList {

	/**
	 * Display the list of earthquakes in ascending order based on their distance to the given location
	 * 
	 * @param earthquakeList a list of EarthquakeT objects
	 * @param location a PointT object represent the objective location
	 */
	@Override
	public void display(ArrayList<EarthquakeT> earthquakeList, PointT location) {
		Sort.sortByDistance(location, earthquakeList);
		if (earthquakeList.size() == 0)
			System.out.println("There's no earthquake!");
		else {
			System.out.println(
					"The earthquakes are displayed in the ascending order based on the distance from the input location:");
			System.out.printf("%-15s%-15s%-20s%-15s%-30s\n", "Distance(km)", "Magnitude", "Earthquake class", "Date", "City");
			for (EarthquakeT eq : earthquakeList) {
				System.out.printf("%-15.1f%-15.1f%-20s%-15d%-30s\n", location.distanceTo(eq.getPointT()), eq.getMag(),eq.getColor(),
						eq.getDate().getYear(), eq.getPlace());
			}
			System.out.println();
		}

	}
}
