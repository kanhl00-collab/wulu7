package cas.XB3.earthquake.ADT;

public class PointT {
	private double x;
	private double y;

	/**
	 * @details Constructor of PointT ADT. longitude will be normalized.
	 * @param x Double, the latitude.
	 * @param y Double, the longitude.
	 * @exception RuntimeException when the latitude is greater than 90 or less
	 * than -90, RuntimeException will raise.
	 */
	public PointT(double x, double y) {
		if(x > 90 || x < -90)
			throw new RuntimeException("Invalid Latitude: "+x);
		this.x = x;
		this.y = (y+540)%360-180;
		
	}

	/**
	 * 
	 * @param that PointT Object.
	 * @return A double of distance between this PointT object and that PointT
	 *  object.
	 */
	public double distanceTo(PointT that) {
		int earthRadius = 6371;
		double latSelf = x;
		double latThat = that.getLat();
		double longSelf = y;
		double longThat = that.getLong();
		double deltaLat = Math.toRadians(latThat - latSelf);
		double deltaLong = Math.toRadians(longThat - longSelf);
		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(Math.toRadians(latSelf))
				* Math.cos(Math.toRadians(latThat)) * Math.sin(deltaLong / 2) * Math.sin(deltaLong / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;
		return distance;
	}
	/**
	 * @details This method will calculate range of latitude by a given radius.
	 * @param radius Double, the radius to filter latitude
	 * @return a double array, index 0 will be the smaller latitude, index 1
	 * will be the larger latitude
	 */
	public double[] latFilter(double radius) {
		double result[] = new double[2];
        int earthRadius = 6371;
        double b=180;
        for(int i = 0; i < 2; i++) {
        	if(i == 1) b = 0;
        	b = Math.toRadians(b);
        	double latSelf = Math.toRadians(x);
        	double longSelf = Math.toRadians(y);
        	double resultLat = Math.asin(Math.sin(latSelf)*Math.cos(radius/earthRadius) +
                     Math.cos(latSelf)*Math.sin(radius/earthRadius)*Math.cos(b));
        	double resultLong = longSelf + Math.atan2(Math.sin(b)*Math.sin(radius/earthRadius)*
                      Math.cos(latSelf), Math.cos(radius/earthRadius)-Math.sin(latSelf)*
                      Math.sin(resultLat));
        	resultLat = Math.toDegrees(resultLat);
        	resultLong = Math.toDegrees(resultLong);
        	result[i] = resultLat;
        }
        if(result[0] > result[1]) {
        	double temp = result[0];
        	result[0] = result[1];
        	result[1] = temp;
        }
		return result;
	}
	
	/**
	 * @details Two points are considered equal if they have "same" x and y
	 * value with a tolerance of 0.0000001
	 * @param that PointT object that comparing to.
	 * @return boolean if the two PointT objects are considered equal.
	 */
    public boolean equals(PointT that) {
    	final double TOLERANCE = 0.0000001;
    	if (Math.abs(x - that.getLat()) > TOLERANCE) {
    		return false;
    	}
    	if (Math.abs(y - that.getLong()) > TOLERANCE) {
    		return false;
    	}
    	return true;
    }

    /**
     * 
     * @return double get the Latitude of the this object.
     */
	public double getLat() {
		return x;
	}
	
	/**
	 * 
	 * @return double get the longitude of this object.
	 */
	public double getLong() {
		return y;
	}
}
