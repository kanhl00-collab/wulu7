package cas.XB3.earthquake.ADT;

public class CityT {
	private String cityName;
	private String province;
	private double popDensity;
	/**
	 * Constructor of CityT ADT.
	 * @param cityName Should be a String, name of the city.
	 * @param province Should be a String, name of the province.
	 * @param popDensity Should be a double, the population density of a city.
	 */
	public CityT(String cityName, String province, double popDensity) {
		this.cityName = cityName;
		this.province = province;
		this.popDensity = popDensity;
	}
	
	/**
	 * 
	 * @return String cityName, name of the city.
	 */
	public String getCityName() {
		return cityName;
	}
	
	/**
	 * 
	 * @return String province, the province of the city.
	 */
	public String getProvince() {
		return province;
	}
	
	/**
	 * 
	 * @return Double popDensity, the population density of the city.
	 */
	public double getPopDensity() {
		return popDensity;
	}
	
	/**
	 * @details Override equals in Object. Two cities are considered equal if
	 * they both have same city name, province name, and "same" popDensity, 
	 * with tolerance range 0.0000001 to compare popDensity.
	 * @return Boolean, if two cities are considered equal.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CityT)) {
			return false;
		}
		final double TOLERANCE = 0.0000001;
		CityT that = (CityT) o;
		return that.getCityName().equals(cityName) && that.getProvince().equals(province) &&
				Math.abs(popDensity - that.getPopDensity()) < TOLERANCE;
	}
}
