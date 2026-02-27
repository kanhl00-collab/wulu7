/**
 * The MCVDemo class shows how the project work
 * 
 * @author Ye Fang
 * Revised: April 9, 2020
 */
package cas.XB3.earthquake.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import cas.XB3.earthquake.ADT.CityPostT;
import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.Graph.CityGraph;
import cas.XB3.earthquake.controller.Controller;
import cas.XB3.earthquake.search.GeoCollection;
import cas.XB3.earthquake.search.RedBlackBST;
import cas.XB3.earthquake.view.DispalyByMagnitude;
import cas.XB3.earthquake.view.DisplayByDistance;

public class MCVDemo {
	private static RedBlackBST<Double, EarthquakeT> bst = new RedBlackBST<Double, EarthquakeT>();
	private static GeoCollection geoCollection = new GeoCollection();
	private static ArrayList<CityPostT> cityPostList = new ArrayList<>();
	private static CityGraph graph = new CityGraph();

	public static void main(String[] args) {
		Controller controller = new Controller();

		if (bst.size() == 0) {
			controller.init(bst, geoCollection, cityPostList);
		}

		try {
			File file = new File("./input.txt");
			// read the input from input.txt
			Scanner input = new Scanner(file);

			// read the input from keyboard
//			Scanner input = new Scanner(System.in);

			System.out.println("Please enter the latidude, the number should be between 41.0 to 84.0: \n");
			double lat = input.nextDouble();
			System.out.println("Please enter the longitude, the number should be between -150.0 to -42.0:\n");
			double longi = input.nextDouble();
			System.out.println("Please enter the radius with the unit of kilometer:\n");
			int radius = input.nextInt();
			int choice = -1;
			PointT location = new PointT(lat, longi);

			controller.search(bst, location, radius);

			do{
				System.out.println("Please choose the display option:");
				System.out.println(
						"Display by magnitude, choose 1; Display by distance, choose 2; Display risk rating, choose 3; exit, choose 0\n");
				choice = input.nextInt();
				if (choice == 0){
					break;
				} else if (choice == 1) {
					controller.updateViewOfList(new DispalyByMagnitude());
				} else if (choice == 2) {
					controller.updateViewOfList(new DisplayByDistance());
				} else if (choice == 3) {
					controller.updateViewOfRisk(bst, location, cityPostList, graph);
				}
			} while (input.hasNext() || choice > 3);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
