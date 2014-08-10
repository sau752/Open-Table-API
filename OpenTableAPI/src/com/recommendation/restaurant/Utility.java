package com.recommendation.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

	private static final int CONNECTION_TIME_OUT = 5000;
	private static final int READ_TIME_OUT = 10000;
	
	public static String getResponse(String urlString) throws IOException {
		
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIME_OUT);
		connection.setReadTimeout(READ_TIME_OUT);
		String response = readFromStream(connection.getInputStream());
		return response;
		
	}
	
	public static String readFromStream(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder str = new StringBuilder();
		String line;
		
		while ((line = br.readLine())!=null) {
			str.append(line);
		}
		br.close();
		
		return str.toString();
	}
	
	public static JSONObject filterJSONResponse(JSONObject obj, String[] keys) throws JSONException {
		
		JSONObject filteredJSON = new JSONObject();
		
		for (int i = 0; i < keys.length; i++) {
			filteredJSON.put(keys[i], obj.get(keys[i]));
		}
		return filteredJSON;
	}
}
