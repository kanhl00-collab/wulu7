/**
 * A view interface   
 * 
 * @author Ye Fang
 * Revised: April 9, 2020
 */
package cas.XB3.earthquake.view;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;

public interface ViewList {
	
	/**
	 * Display the list of earthquakes
	 */
	public void display(ArrayList<EarthquakeT> earthquakeList, PointT location);

}
