package guidelines.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import guidelines.exceptions.StreetNotFoundException;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressResolver {
	
	public static final String app_id = "ZQc2T7A0egItevgCF9iE";
	public static final String app_code = "Lu6ioGBqcHULGjLLFMiDeQ";
	private final double munichLatitude = 48.13642;
	private final double munichLongitude = 11.57755;
	private final int radius = 100000; // Look for results in radius 100 km
	
	/**
	 * Sends a HTTP GET request to the given URL and returns the result as a String
	 * @param link
	 * 			URL to send the GET request to
	 * @return
	 * 			The response body of the GET request as a String
	 * @throws IOException
	 * 			If there was a connection problem
	 */
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
	 */
	public ArrayList<Address> getAddressList(String address) throws IOException, JSONException {
		ArrayList<Address> addressList = getAddresses(address);
		if (addressList.isEmpty()) { // Maybe it was a place and not an exact address
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			addressList = searchForPlacesInMunich(encodedAddress);
		}
		return addressList;
	}
	
	
	/**
	 * Finds a place in Munich based on the user input
	 * @param place
	 * 				The user input for that place
	 * @return
	 * 				The address of the place or an empty ArrayList if the place couldn't be found
	 * @throws IOException
	 * 				If there was a problem with the connection
	 */
	private  ArrayList<Address> searchForPlacesInMunich(String place) throws IOException {
		String response = getResponseFromURL("https://places.cit.api.here.com/places/v1/autosuggest"
				+ "?app_id=" + app_id
				+ "&app_code=" + app_code
				+ "&at=" + munichLatitude + "%2C" + munichLongitude
				+ "&q=" + place
				+ "&callback=");
		response = response.substring(1);
		response = response.substring(0, response.length()-2);
		JSONArray results = new JSONObject(response).getJSONArray("results");
		JSONObject firstResultWithVicinity = null;
		String address = null;
		for (int i=0; i<results.length(); i++) {
			try {
				firstResultWithVicinity = results.getJSONObject(i);
				address = firstResultWithVicinity.getString("vicinity");
			} catch (JSONException e) {
				// do nothing
			}
			if (address != null) {
				break;
			}
		}
		
		if (address == null || firstResultWithVicinity == null) {
			return new ArrayList<>();
		}
		
		address = address.replaceAll("<br>|<\\/br>|<b>|<\\/br>|<br\\/>|<b\\/>", " ");
		ArrayList<Address> addressList = getAddresses(address);
		return addressList;
	}
	
	/**
	 * Gets the addresses based on the user input/vicinity of a place.
	 * @param address
	 * 					The user input/vicinity of a place
	 * @return
	 * 					A list with all addresses
	 * @throws IOException
	 * 					If there was a problem with the connection
	 */
	private ArrayList<Address> getAddresses(String address) throws IOException {
		String encodedAddress = URLEncoder.encode(address, "UTF-8");
		String response = getResponseFromURL("http://autocomplete.geocoder.api.here.com/6.2/suggest.json?query="
				+ encodedAddress
				+ "&country=DEU"
				+ "&prox=" + munichLatitude + "%2C" + munichLongitude + "%2C" + radius
				+ "&app_id=" + app_id
				+ "&app_code=" + app_code);
		JSONObject json = new JSONObject(response);
		JSONArray suggestions = json.getJSONArray("suggestions");
		ArrayList<Address> addressList = new ArrayList<>();
		for (int i=0; i<suggestions.length(); i++) {
			Address addr;
			try {
				addr = getAddress(suggestions.getJSONObject(i));
			} catch (StreetNotFoundException e) {
				addr = null;
			}
			if (addr != null) {
				addressList.add(addr);
			}
		}
		return addressList;
	}
	
	/**
	 * Returns the address as an {@link Address} object
	 * from a suggestion in form of a {@link JSONObject} from Here API
	 * @param suggestion
	 * 				The {@link JSONObject} of a suggestion from Here API
	 * @return
	 * 				An {@link Address} object from the suggestion
	 * @throws StreetNotFoundException
	 * 				If the Street was not found.
	 */
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
