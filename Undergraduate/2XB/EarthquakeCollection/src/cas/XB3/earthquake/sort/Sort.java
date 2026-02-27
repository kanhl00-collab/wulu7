/**
 * The Sort class provides static methods for sorting a list of earthquakes 
 * in ascending or descending order based on a specific parameter   
 * 
 * @author Ye Fang
 * Revised: March 24, 2020
 */

package cas.XB3.earthquake.sort;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;

public class Sort {

	/**
	 * Rearranges the list of earthquakes in ascending order based on its distance
	 * from the given location
	 * 
	 * @param location a PointT object that represent a given location
	 * @param eqList   a list of EarthquakeT objects
	 */
	public static void sortByDistance(PointT location, ArrayList<EarthquakeT> eqList) {
		for (int i = 0; i < eqList.size(); i++) {
			for (int j = i; j > 0; j--) {
				if (location.distanceTo(eqList.get(j - 1).getPointT()) > location
						.distanceTo(eqList.get(j).getPointT())) {
					swap(eqList, j, j - 1);
				}
			}
		}
	}

	/**
	 * Rearranges the list of earthquakes in descending order based on the
	 * magnitudes using quickSort
	 * 
	 * @param eqList a list of EarthquakeT objects
	 */
	public static void sortByMagnitude(ArrayList<EarthquakeT> eqList) {
		quickSort(eqList, 0, eqList.size() - 1);
	}

	// quicksort the the sublist from eqList[low] to eqList[high]
	private static void quickSort(ArrayList<EarthquakeT> eqList, int low, int high) {
		if (low >= high) {
			return;
		}
		int pi = partition(eqList, low, high);
		quickSort(eqList, low, pi - 1);
		quickSort(eqList, pi + 1, high);
	}

	// pick last element as pivot
	private static int partition(ArrayList<EarthquakeT> eqList, int low, int high) {
		EarthquakeT key = eqList.get(high);
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (less(key, eqList.get(j))) {
				i++;
				swap(eqList, i, j);
			}
		}
		i++;
		swap(eqList, high, i);
		return i;
	}

	// is a < b?
	private static boolean less(EarthquakeT a, EarthquakeT b) {
		return a.compareTo(b) < 0;

	}

	// exchange eqList[i] and eqList[j]
	private static void swap(ArrayList<EarthquakeT> eqList, int i, int j) {
		EarthquakeT temp = eqList.get(i);
		eqList.set(i, eqList.get(j));
		eqList.set(j, temp);
	}

}
