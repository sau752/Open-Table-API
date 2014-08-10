package com.recommendation.restaurant;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.recommendation.restaurant.RestaurantData;
import com.recommendation.restaurant.Utility;

@Path("restaurant_data/")

public class RestaurantData extends Application{
	
	private static Logger log = Logger.getLogger(RestaurantData.class.getName());
	
	// http://opentable.herokuapp.com/api/restaurants?name=""&address=""&state""&city=""&zip=""&country=""
	// http://opentable.herokuapp.com/api/restaurants?zip=60601
	// http://localhost:5050/OpenTableAPI/rest/restaurant_data/restaurant/?name=&address=&state=&city=&zip=60601&country=
	
	private static final String CATALOG_URL = "http://opentable.herokuapp.com/api/restaurants?";
	
	/**Gets URL for top Box Office Hits
	 * @param limit Limits the number of box office movies returned
	 * @param country Provides localized data for the selected country (ISO 3166-1 alpha-2) if available. Otherwise, returns US data.
	 * @return URL
	 */
	private static String getURLForRestaurantSearch(String name, String address, String state, String city, String zip, String country) {
		
		String urlStr = CATALOG_URL +
						"name=" + name +
						"&address=" + address +
						"&state=" + state +
						"&city=" + city +
						"&zip=" + zip +
						"&county" + country;
		
		return urlStr.replace(" ", "%20");
	}
	
	
	public static String getJSONResponseforRestaurantSearched(String name, String address, String state, String city, String zip, String country) {
		
		String url = getURLForRestaurantSearch(name, address, state, city, zip, country);
		String response = "";
		
		try {
			response = Utility.getResponse(url);
			log.debug("Resonse recieved from URL :- " +response);
		} catch (IOException e) {
			log.error("ERROR: Response not recieved fro  URL :-" +url, e);
		}
		return response;
	}
	

	@GET
	
	@Path("restaurant/")
	
	@Produces(MediaType.TEXT_PLAIN)
	
	
	public static String getRestaurantSearched(@QueryParam("name") String name, @QueryParam("address") String address, @QueryParam("state") String state, @QueryParam("city") String city, @QueryParam("zip") String zip, @QueryParam("country") String country) {
		JSONArray response = new JSONArray();
    	JSONObject json;

		try {
			String rawResponse = getJSONResponseforRestaurantSearched(name, address, state, city, zip, country);
			json = new JSONObject(rawResponse);
			JSONArray movieArray = json.getJSONArray("restaurants");
			
			for (int i = 0; i < movieArray.length(); i++) {
				JSONObject movies = movieArray.getJSONObject(i);
				String[] keys = {"id", "name", "address", "city", "state", "area", "postal_code", "country", "phone", "reserve_url", "mobile_reserve_url"};
				JSONObject filteredMovies;
				filteredMovies = Utility.filterJSONResponse(movies, keys);
				
				response.put(filteredMovies);
				log.debug("Parsed the response from the server, Incident fetched");
			}
		} catch (JSONException e) {
			log.debug("ERROR: Cannot parse the JSON response receieved from server", e);
		}
		return response.toString();
	}
	
	public static void main(String args[]) {
		System.out.println(getRestaurantSearched("Aria", "", "", "", "60601", ""));
	}
}
