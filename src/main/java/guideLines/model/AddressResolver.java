package main.java.guideLines.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.model.Address;

public class AddressResolver {
	
	public static final String app_id = "ZQc2T7A0egItevgCF9iE";
	public static final String app_code = "Lu6ioGBqcHULGjLLFMiDeQ";
	
	
	public String getResponseFromURL(String link) throws IOException {
		URL url = new URL(link);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		return body;
	}
	
	/**
	 * Resolves the address from the user to a correct address (if the address was wrong)
	 * It sends a get request to the here api and then it takes the first suggestion (usually the most aqurate)
	 * @param address
	 * 				  the address to be resolved
	 * @return the correct address from here API
	 * @throws IOException If there was a problem with the connection
	 * @throws StreetNotFoundException If the street was not found in the user input
	 * @throws JSONException if the whole input was wrong
	 */
	public Address getAddress(String address) throws IOException, JSONException {
		String encodedAddress = URLEncoder.encode(address, "UTF-8");
		String response = getResponseFromURL("http://autocomplete.geocoder.api.here.com/6.2/suggest.json?query="
				+ encodedAddress
				+ "&app_id=" + app_id
				+ "&app_code=" + app_code);
		JSONObject json = new JSONObject(response);
		JSONArray suggestions = json.getJSONArray("suggestions");
		JSONObject firstSuggestion = suggestions.getJSONObject(0);
		JSONObject addressJson = firstSuggestion.getJSONObject("address");
		String locationId = firstSuggestion.getString("locationId");
		String city = addressJson.getString("city");
		String street = null;
		street = addressJson.getString("street");
		String postalCode = addressJson.getString("postalCode");
		Integer houseNumber;
		try {
			houseNumber = addressJson.getInt("houseNumber");
		} catch (JSONException e) {
			houseNumber = null;
		}
		
		if (postalCode == null && houseNumber != null) {
			return new Address(street, city, locationId, houseNumber);
		} else if (postalCode != null && houseNumber == null) {
			return new Address(street, city, locationId, postalCode);
		} else if (postalCode == null && houseNumber == null) {
			return new Address(street, city, locationId);
		}
		return new Address(street, city, locationId, houseNumber, postalCode);
	}
	
//	getNearbyStation

}
