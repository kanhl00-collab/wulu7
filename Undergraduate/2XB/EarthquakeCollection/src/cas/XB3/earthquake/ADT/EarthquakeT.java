package cas.XB3.earthquake.ADT;

import java.time.LocalDateTime;

public class EarthquakeT implements Comparable<EarthquakeT>{
    private LocalDateTime date;
    private String place;
    private double lat;
    private double lng;
    private double dph;
    private double mag;
    private MagType magnitudeType;
    private ColorRating color;
    private String nameOfProv;

    /**
     * Constructor of EarthquakeT ADT.
     * @param place String, the city name where the earthquake happens.
     * @param prov String, the province name where the earthquake happens.
     * @param date LocalDateTime, the date of the earthquake.
     * @param lat double, the latitude of the earthquake.
     * @param lng double, the longitude of the earthquake.
     * @param dph double, the depth of the earthquake.
     * @param mag double, the magnitude of the earthquake.
     * @param mgT Enum MagType, the type of the earthquake.
     * @param color Enum ColorRating, the color rating of the earthquake.
     */
    public EarthquakeT(String place, String prov, LocalDateTime date, double lat, double lng, double dph, double mag, MagType mgT, ColorRating color){
    	this.lat = lat;
    	this.lng = lng;
    	this.place = place;
    	this.nameOfProv = prov;
    	this.date = date;
        this.dph = dph;
        this.mag = mag;
        this.magnitudeType = mgT;
        this.color = color;
    }

    /**
     * 
     * @return String, the name of the province.
     */
    public String getNameOfProv(){
        return nameOfProv;
    }

    /**
     * 
     * @return String, the name of the city.
     */
    public String getPlace() {
        return place;
    }

    /**
     * 
     * @return PointT object, the exact location of the earthquake.
     */
    public PointT getPointT() {
    	return new PointT(lat, lng);
    }

    /**
     * 
     * @return double, the magnitude of the earthquake.
     */
    public double getMag() {
        return mag;
    }
    
    /**
     * 
     * @return double, the depth of the earthquake.
     */
    public double getDph() {
    	return dph;
    }
    
    /**
     * 
     * @return Enum MagType, the type of the earthquake.
     */
    public MagType getMagitudeType() {
    	return magnitudeType;
    }
    
    /**
     * 
     * @return LocalDateTime, the date of the earthquake.
     */
    public LocalDateTime getDate() {
    	return date;
    }

    /**
     * 
     * @return Enum ColorRating, the color rating of the earthquake.
     */
    public ColorRating getColor(){
        return color;
    }
    
    /**
     * @details This method will compare two EarthquakeT object based on their
     * magnitude.
     * @return Integer, 1 if the current object have larger magnitude, -1 if
     * the current object have smaller magnitude, 0 if the two EarthquakeT
     * object have the same magnitude.
     */
    public int compareTo(EarthquakeT eq) {
    	if(this.getMag() < eq.getMag()) return -1;
    	else if(this.getMag() > eq.getMag()) return 1;
    	else return 0;
    }
    
    /**
     * @details this method will determine if two EarthquakeT objects are
     * considered equal based on their magnitude, date, place, location, depth,
     * MagType, and colorRating.
     * @param that EarthquakeT object comparing to.
     * @return boolean true if these two objects are considered equal.
     */
    public boolean equals(EarthquakeT that) {
    	final double TOLERANCE = 0.0000001;
    	int cmp = date.compareTo(that.getDate());
    	if (cmp != 0) {
    		return false;
    	}
    	if(!this.getPointT().equals(that.getPointT())) {
    		return false;
    	}
    	if (!place.equals(that.getPlace())) {
    		return false;
    	}
    	if (Math.abs(dph - that.getDph()) > TOLERANCE) {
    		return false;
    	}
    	if (Math.abs(mag - that.getMag()) > TOLERANCE) {
    		return false;
    	}
    	return magnitudeType == that.getMagitudeType() && color == that.getColor();
    }

    /**
    * ColorRating is assigned based on 6 earthquake magnitude classes; The magnitude is in brackets below:
    * RED: Great (8 or more)
    * ORANGE: Major (7 - 7.9)
    * YELLOW: Strong (6 - 6.9)
    * GREEN: Moderate (5 - 5.9)
    * BLUE: Light (4 - 4.9)
    * PURPLE: Minor (3 - 3.9)
    */
    public enum ColorRating{
        NOCOLOR, ZERO, PURPLE, BLUE, GREEN, YELLOW, ORANGE, RED
    }
    public enum MagType{
        M5, mb, MB, Mb, MC, Mc, mc, ML, MLSn, MN, MS, MW, Ms, Mw, BLANK
    }
}
