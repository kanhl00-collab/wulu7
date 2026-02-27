package cas.XB3.earthquake.search;

import cas.XB3.earthquake.ADT.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GeoCollection {
	private HashMap<String, ArrayList<CityT>> cityHashMap = new HashMap<>();

	/**
	 * @details add a CityT object to HashMap, the key is the first letter of
	 * the city.
	 * @param city Should be a CityT object
	 */
    public void add(CityT city){
    	ArrayList<CityT> temp = new ArrayList<>();
    	String firstCityLetter = getFirstCityLetter(city);
    	if(cityHashMap.get(firstCityLetter) == null) {
    		temp.add(city);
    		cityHashMap.put(firstCityLetter, temp);
    	}else {
    		temp = (ArrayList<CityT>) cityHashMap.get(firstCityLetter).clone();
    		temp.remove(city); // avoid potential duplicated cites
    		temp.add(city);
    		cityHashMap.put(firstCityLetter, temp);
    	}
    }

    /**
     * @details check if the HashMap is empty.
     * @return a boolean, true if the HashMap is empty.
     */
    public boolean isEmpty(){
        return cityHashMap.isEmpty();
    }
    
    /**
     * 
     * @param firstLetter a String which indicate the first letter of the city.
     * @return an arrayList<CityT> consisting all cities that start with the
     * given first letter.
     */
    public ArrayList<CityT> getCityArrayList(String firstLetter) {
    	return cityHashMap.get(firstLetter);
    }
    
    /**
     * @details get the HashMap.
     * @return the HashMap cityHashMap.
     */
    public HashMap<String, ArrayList<CityT>> getCityHashMap(){
    	return cityHashMap;
    }
    
    private String getFirstCityLetter(CityT city) {
    	return city.getCityName().substring(0,1);
    }
}
