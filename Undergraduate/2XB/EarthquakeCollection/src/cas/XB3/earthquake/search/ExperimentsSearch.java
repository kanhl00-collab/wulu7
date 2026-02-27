/**
 * The ExperimentsSearch class shows the search performance in EarthquakeBag and RedBlackBST respectively.
 * We need to iterate all earthquakes in EarthquakeBag, so the time complexity is always N (size of the EarthquakeBag).
 * For the RedBlackBST, the complexity is much less than N when the radius is small than 1000 km. 
 * In our project, the radius is usually less than 100km. Thus, using BST improves the performance of the application. 
 * 
 * @author Ye Fang
 * Revised: April 9, 2020
 */
package cas.XB3.earthquake.search;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.reader.CSVreader;

public class ExperimentsSearch {
	public static void main(String[] args) {
		EarthquakeBag<EarthquakeT> Earthquakebag = new EarthquakeBag<EarthquakeT>();
		RedBlackBST<Double, EarthquakeT> earthquakeTree = new RedBlackBST<Double, EarthquakeT>();
		CSVreader.readEarthquakes("./eqarchive-en.csv", Earthquakebag);
		CSVreader.readEarthquakesBST("./eqarchive-en.csv", earthquakeTree);

		PointT location = new PointT(46, -70);
		int[] radius = { 10, 50, 100, 200, 300, 400, 500, 1000 };

		System.out.println("Execution time of earthquake bag");
		System.out.format("%1$-10s%2$-25s%3$-20s\n", "Radius", "Total Execution Time", "Size of the List");
		for (int i = 0; i < radius.length; i++) {
			double startTime = System.currentTimeMillis();
			ArrayList<EarthquakeT> eqList = SearchEarthquakes.searchInEarthquakeBag(Earthquakebag, location, radius[i]);
			double endTime = System.currentTimeMillis();
			System.out.printf("%4d%19.5f%24d\n", radius[i],
					(endTime - startTime) / 1000.0, eqList.size());
		}

		System.out.println("\nExecution time of earthquake tree");
		System.out.format("%1$-10s%2$-25s%3$-20s\n", "Radius", "Total Execution Time", "Size of the List");
		for (int i = 0; i < radius.length; i++) {
			double startTime = System.currentTimeMillis();
			ArrayList<EarthquakeT> eqList2 = SearchEarthquakes.searchEarthquakeInCircle(earthquakeTree, location,
					radius[i]);
			double endTime = System.currentTimeMillis();
			System.out.printf("%1$-10d\t%2$-25.5f%3$-20d\n", radius[i],
					(endTime - startTime) / 1000.0, eqList2.size());
		}

	}

}
