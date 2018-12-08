package main.java.guideLines.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

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
	 * Returns a list with all posible addresses based on the input
	 * @param address
	 * 				The input address. All the suggesstions are based on this input
	 * @return
	 *              An ArrayList<Address> with all possible addresses based on input
	 *              An empty ArrayList<Address> [.size() == 0] if the input was wrong
	 * @throws IOException
	 *              If the connection fails
	 * @throws JSONException
	 *              If the response JSON was not in the expected form
	 * @throws StreetNotFoundException
	 *              If the street was not in the input or couldn't be found
	 */
	public ArrayList<Address> getAddressList(String address) throws IOException, JSONException, StreetNotFoundException {
		String encodedAddress = URLEncoder.encode(address, "UTF-8");
		String response = getResponseFromURL("http://autocomplete.geocoder.api.here.com/6.2/suggest.json?query="
				+ encodedAddress
				+ "&app_id=" + app_id
				+ "&app_code=" + app_code);
		JSONObject json = new JSONObject(response);
		JSONArray suggestions = json.getJSONArray("suggestions");
		ArrayList<Address> addressList = new ArrayList<>();
		for (int i=0; i<suggestions.length(); i++) {
			addressList.add(getAddress(suggestions.getJSONObject(i)));
		}
		
		return addressList;
	}
	
	private Address getAddress(JSONObject suggestion) throws StreetNotFoundException {
		JSONObject addressJson = suggestion.getJSONObject("address");
		String locationId = suggestion.getString("locationId");
		String city = addressJson.getString("city");
		String street = null;
		try {
			street = addressJson.getString("street");
		} catch (JSONException e) {
			throw new StreetNotFoundException();
		}
		
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

}
