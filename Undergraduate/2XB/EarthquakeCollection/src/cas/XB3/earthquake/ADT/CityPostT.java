/**
 * The CityPostT class represents a ADT of city position
 * @author Ye Fang
 * Revised: March 24, 2020
 */
package cas.XB3.earthquake.ADT;

public class CityPostT {
	private String cityName;
	private PointT point;

	/**
	 * Initializes a city position object
	 * @param cityName the city name
	 * @param lat the latitude of the city position
	 * @param lon the longitude of the city position
	 */
	public CityPostT(String cityName, double lat, double lon) {
		this.cityName = cityName;
		this.point = new PointT(lat, lon);
	}

	/**
	 * Returns the PointT object represents the location of this city
	 * @return the PointT object represents the location of this city
	 */
	public PointT getPoint() {
		return this.point;
	}

	/**
	 * Returns the city name 
	 * @return the city name 
	 */
	public String getCityName() {
		return this.cityName;
	}

}
