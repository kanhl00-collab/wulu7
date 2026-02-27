/**
 * The Controller class provides methods for controlling the model modules and update view modules
 * 
 * @author Ye Fang
 * Revised: March 24, 2020
 */
package cas.XB3.earthquake.controller;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.CityPostT;
import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.Graph.CityGraph;
import cas.XB3.earthquake.reader.CSVreader;
import cas.XB3.earthquake.search.GeoCollection;
import cas.XB3.earthquake.search.RedBlackBST;
import cas.XB3.earthquake.search.SearchEarthquakes;
import cas.XB3.earthquake.view.ViewList;
import cas.XB3.earthquake.view.ViewRisk;

public class Controller {
	PointT location;
	ArrayList<EarthquakeT> eqList;
	
	/**
	 * Initialized the RedBlackBST, GeoCollection and ArrayList<CityPostT> 
	 * @param bst a Red Black Tree with value of EarthquakeT
	 * @param GeoCollection a collection of CityT objects
	 * @param cityPostList a list of CityPostT objects
	 */
	public void init(RedBlackBST<Double, EarthquakeT> bst, GeoCollection GeoCollection, ArrayList<CityPostT> cityPostList) {
		CSVreader.readEarthquakesBST("./eqarchive-en.csv",bst);
		CSVreader.readPopulation("./T301EN.CSV", GeoCollection);
		CSVreader.readCityPosition("./City_Coordinates.CSV", cityPostList);	
	}

	/**
	 * Controls the SearchEarthquakes module
	 * @param bst a Red Black Tree with value of EarthquakeT
	 * @param location a PointT object that represents the objective location
	 * @param radius a real number represents searching distance(in km) from the
	 *                 given location
	 */
	public void search(RedBlackBST<Double, EarthquakeT> bst,PointT location, double radius){
		eqList = SearchEarthquakes.searchEarthquakeInCircle(bst, location, radius);
		this.location = location;

	}
	
	
	/**
	 * Controls the ViewList module
	 * @param dispalyByOption a ViewList object
	 */
	public void updateViewOfList(ViewList dispalyByOption) {
		dispalyByOption.display(eqList, location);
	}

	/**
	 * Controls the ViewRisk module
	 * @param bst  a Red Black Tree with value of EarthquakeT
	 * @param location a PointT object that represents the objective location
	 * @param cityPostList a list of CityPostT objects
	 * @param graph a cityGraph which represents a connected directed weighted graph  
	 */
	public void updateViewOfRisk(RedBlackBST<Double, EarthquakeT> bst, PointT location,
			ArrayList<CityPostT> cityPostList, CityGraph graph) {
		ViewRisk.showRisk(bst, location, cityPostList, graph);
	}
		

}
