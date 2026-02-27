/**
 * The RiskAssessment class represents a ADT of the assessment results of future earthquake risk of a given location
 * @author Ye Fang
 * Revised: March 24, 2020
 */
package cas.XB3.earthquake.riskAssessment;

import java.util.ArrayList;

import cas.XB3.earthquake.ADT.CityPostT;
import cas.XB3.earthquake.ADT.CityT;
import cas.XB3.earthquake.ADT.EarthquakeT;
import cas.XB3.earthquake.ADT.PointT;
import cas.XB3.earthquake.Graph.CityGraph;
import cas.XB3.earthquake.Graph.Edge;
import cas.XB3.earthquake.reader.CSVreader;
import cas.XB3.earthquake.search.GeoCollection;
import cas.XB3.earthquake.search.RedBlackBST;
import cas.XB3.earthquake.search.SearchEarthquakes;

public class RiskAssessment {
	private RedBlackBST<Double, EarthquakeT> bst;
	private String[] cityProv;
	private int frequency;
	private double averageMag;
	private double populationDensity;
	private int rating;

	/**
	 * Initializes a risk assessment object for the given location based on the
	 * earthquakes in the EarthquakeT BST. Assuming the future earthquake risk for a
	 * assessing location is determined by three factors: frequency and average
	 * magnitude of the historical earthquakes within 100 km from the assessing
	 * location, as well as the population density in this city
	 * 
	 * @param bst a Red Black Tree with value of EarthquakeT
	 * @param location       a PointT object that represents the objective location
	 */
	public RiskAssessment(RedBlackBST<Double, EarthquakeT> bst, PointT location) {
		this.bst = bst;
		// search earthquakeS within 100km of the given location in the EarthquakeT BST
		ArrayList<EarthquakeT> earthquakeLists = SearchEarthquakes.searchEarthquakeInCircle(bst, location,
				100);
		// gets the city name and its province name in the nearest EathquakeT from the
		// given location
		this.cityProv = getCityProv(location, earthquakeLists);
		// gets the frequency of the given list of earthquakes
		this.frequency = getFrequency(earthquakeLists);
		// gets the average Magnitude of the given list of earthquakes
		this.averageMag = getAverageMagnitude(earthquakeLists);
		// gets the population density of this city
		this.populationDensity = getPopulation();
		// if there is no earthquakes in the range, the rating should be 0
		if (earthquakeLists.size() == 0)
			this.rating = 0;
		else
			this.rating = OverallRating(this.frequency, this.averageMag, this.populationDensity);

	}

	/**
	 * Returns the risk rating of the assessed location
	 * 
	 * @return The risk rating of the assessed location
	 */
	public int getRisk() {
		return this.rating;
	}

	/**
	 * Returns the city name of the nearest earthquake from the the assessing
	 * location
	 * 
	 * @return the city name of the nearest earthquake from the the assessing
	 *         location
	 */
	public String getCity() {
		if (cityProv == null) {
			return null;
		} else {
			return this.cityProv[0];
		}

	}

	/**
	 * Returns the earthquake frequency for this risk assessment
	 * 
	 * @return the earthquake frequency
	 */
	public int getFrequency() {
		return this.frequency;
	}

	/**
	 * Returns the average magnitude for this risk assessment
	 * 
	 * @return The earthquake frequency
	 */
	public double getMag() {
		return this.averageMag;
	}

	/**
	 * Returns the population density in the nearest city from the given location
	 * 
	 * @return The population density in the nearest city from the given location
	 */
	public double getPopulationDensity() {
		return this.populationDensity;
	}

	/**
	 * Finds the nearest from this city in the connected graph, where the risk
	 * rating is lower than this city
	 * 
	 * @param graph a cityGraph which represents a connected directed weighted
	 *              graph, the vertices are cities, the weight is distance between
	 * 
	 * @return The nearest city whose risk rating is lower than this location
	 */
	public String nearestLowerRiskCity(CityGraph graph) {
		String nearestLowerRiskCity = null;
		int minDistance = Integer.MAX_VALUE;
		if (cityProv == null) {
			return null;
		}
		if (graph.adj(this.cityProv[0]) != null) {
			for (Edge city : graph.adj(this.cityProv[0])) {
				if (new RiskAssessment(this.bst, getLocation(city.to())).getRisk() < this.rating
						&& city.weight() < minDistance) {
					minDistance = city.weight();
					nearestLowerRiskCity = city.to();
				}
			}
		}
		if (minDistance == Integer.MAX_VALUE)
			nearestLowerRiskCity = null;
		return nearestLowerRiskCity;
	}

	// Gets the coordinate of the given city from the city coordinates list
	private PointT getLocation(String city) {
		PointT location = null;
		ArrayList<CityPostT> cityPostList = new ArrayList<>();
		CSVreader.readCityPosition("EarthquakeCollection/City_Coordinates.CSV", cityPostList);
		for (CityPostT cityPost : cityPostList) {
			if (cityPost.getCityName().equals(city))
				location = cityPost.getPoint();
		}
		return location;
	}

	// get the city name and its province name from the nearest EarthquakeT of the
	// given location
	private String[] getCityProv(PointT location, ArrayList<EarthquakeT> earthquakeList) {
		String[] cityAndProv = new String[2];
		double minDistance = Double.MAX_VALUE;
		EarthquakeT nearestEarthquake = null;
		if (earthquakeList == null) {
			return null;
		}
		for (EarthquakeT earthquake : earthquakeList) {
			if (location.distanceTo(earthquake.getPointT()) < minDistance) {
				minDistance = location.distanceTo(earthquake.getPointT());
				nearestEarthquake = earthquake;
			}
		}
		if (minDistance == Double.MAX_VALUE)
			return null;
		cityAndProv[0] = nearestEarthquake.getPlace();
		cityAndProv[1] = nearestEarthquake.getNameOfProv();
		return cityAndProv;
	}

	// Gets the population density of the given city name from the population
	// dataset
	private double getPopulation() {
		populationDensity = 0;
		if (cityProv == null) {
			return 0;
		}
		if (this.cityProv[0] != null) {
			GeoCollection GeoCollection = new GeoCollection();
			CSVreader.readPopulation("./T301EN.CSV", GeoCollection);
			if (GeoCollection.getCityHashMap().keySet().contains(String.valueOf(this.cityProv[0].charAt(0)))) {
				ArrayList<CityT> listOfCity = GeoCollection
						.getCityArrayList(String.valueOf(this.cityProv[0].charAt(0)));
				if (listOfCity.size() != 0) {
					for (CityT city : listOfCity) {
						if (this.cityProv[0].equals(city.getCityName())
								&& this.cityProv[1].equals(city.getProvince())) {
							populationDensity = city.getPopDensity();
							break;
						}
					}
				}
			}
		}
		return populationDensity;
	}

	// Calculates the earthquake frequency of a given earthquake list
	private int getFrequency(ArrayList<EarthquakeT> earthquakeList) {
		return earthquakeList.size();
	}

	// Calculates the average magnitude of a given earthquake list
	private double getAverageMagnitude(ArrayList<EarthquakeT> earthquakeList) {
		double sum = 0;
		for (EarthquakeT earthquake : earthquakeList) {
			sum += earthquake.getMag();
		}
		int frequency = getFrequency(earthquakeList);
		if (frequency == 0)
			return 0;
		return (double) (sum / (double) getFrequency(earthquakeList));
	}

	// Calculates the overall risk rating regarding a given earthquake
	// frequency,average magnitude and population density
	public int OverallRating(int frequency, double averagerMag, double populationdensity) {
		return frequencyRating(frequency) + magnitudeRating(averagerMag) + populationdensityRating(populationdensity);

	}

	// Calculates the risk rating of a given frequency based on the assuming
	// criterion
	public static int frequencyRating(int frequency) {
		int risk_frequency = 0;
		if (frequency < 1)
			risk_frequency = 0;
		else if (1 <= frequency && frequency < 10)
			risk_frequency = 1;
		else if (10 <= frequency && frequency < 100)
			risk_frequency = 2;
		else if (100 <= frequency && frequency < 1000)
			risk_frequency = 3;
		else
			risk_frequency = 4;
		return risk_frequency;
	}

	// Calculates the risk rating of a given average magnitude based on the assuming
	// criterion
	public static int magnitudeRating(double averageMag) {
		int risk_averagerMag = 0;
		if (averageMag < 1)
			risk_averagerMag = 0;
		else if (1 <= averageMag && averageMag < 4)
			risk_averagerMag = 1;
		else if (4 <= averageMag && averageMag < 6)
			risk_averagerMag = 2;
		else if (6 <= averageMag && averageMag < 7)
			risk_averagerMag = 3;
		else
			risk_averagerMag = 4;
		return risk_averagerMag;
	}

	// Calculates the risk rating of a given population density based on the
	public static int populationdensityRating(double populationdensity) {
		int risk_population = 0;
		if (populationdensity < 1000)
			risk_population = 0;
		else if (1000 <= populationdensity && populationdensity < 5000)
			risk_population = 1;
		else if (5000 <= populationdensity)
			risk_population = 2;
		return risk_population;
	}
}
