/**
 * A ViewRisk class provides methods for displaying the assessing result of the potential earthquakes risk    
 * 
 * @author Ye Fang
 * Revised: April 9, 2020
 */
package cas.XB3.earthquake.view;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.CityPostT;
import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.Graph.CityGraph;
import cas.XB3.earthquake.Graph.Edge;
import cas.XB3.earthquake.riskAssessment.RiskAssessment;
import cas.XB3.earthquake.search.RedBlackBST;

public class ViewRisk {
	/**
	 * Display the assessing result of potential earthquakes risk for the given location
	 * @param earthquakeTree  a Red Black Tree with value of EarthquakeT
	 * @param location a PointT object that represents the objective location
	 * @param cityPostList a list of CityPostT objects
	 * @param graph a cityGraph which represents a connected directed weighted graph         
	 */
	public static void showRisk(RedBlackBST<Double, EarthquakeT> earthquakeTree, PointT location,
			ArrayList<CityPostT> cityPostList, CityGraph graph) {
		RiskAssessment riskAssessment = new RiskAssessment(earthquakeTree, location);
		int rating = riskAssessment.getRisk();

		if (riskAssessment.getFrequency() == 0) {
			System.out.printf("The risk rating for the location (%.2f , %.2f) is : % d\n", location.getLat(),
					location.getLong(), rating);
		} else {

			System.out.printf("The risk rating for the location (%.2f , %.2f) is : % d\n\n", location.getLat(),
					location.getLong(), rating);
			System.out.printf("%-67s%-10s\n", "The nearest historical earthquake from your location is in: ",
					riskAssessment.getCity());
			System.out.printf("%-67s%-5d\n", "The number of historical earthquakes within 100 km is :",
					riskAssessment.getFrequency());
			System.out.printf("%-67s%-5.1f\n", "The average magnitude of historical earthquakes within 100 km is :",
					riskAssessment.getMag());
			System.out.printf("%-67s%-5.1f%s\n\n", "The population density in the nearest city is :",
					riskAssessment.getPopulationDensity(), " persons per square kilometre");

			if (riskAssessment.nearestLowerRiskCity(initGraph(riskAssessment, cityPostList, graph)) != null) {
				System.out.printf("The nearest lower risk city is: %s\n\n\n",
						riskAssessment.nearestLowerRiskCity(graph));
			} else {
				System.out.printf("There is no lower risk city within the range of 100 kilometers\n\n\n");
			}
		}
	}

	// initializes the graph
	private static CityGraph initGraph(RiskAssessment riskAssessment,
			ArrayList<CityPostT> cityPostList, CityGraph graph) {
		for (CityPostT cityFrom : cityPostList) {
			// if the assessing city is in the city position list, construct the graph with
			// edges, the origin node is the assessing city, the destination nodes are
			// cities in the city position list and they are within 100 kilometers of the
			// origin node city
			if (cityFrom.getCityName().equals(riskAssessment.getCity())) {
				for (CityPostT cityTo : cityPostList) {
					if (!cityFrom.getCityName().equals(cityTo.getCityName())) {
						int distance = (int) cityFrom.getPoint().distanceTo(cityTo.getPoint());
						if (distance < 100) {
							Edge e = new Edge(cityFrom.getCityName(), cityTo.getCityName(), distance);
							graph.addEdge(e);
						}
					}
				}
			}
		}
		return graph;		
	}
}
