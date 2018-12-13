package guideLines.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import guideLines.exceptions.NoFormOfTransportException;

public class NearbyStationFinder {

	private Double latitude = null;
	private Double longitude = null;
	private final int radius = 2000; // Radius to look for stations
	private final int max = 3;       // Maximum amount of stations included in the response

	/**
	 * Returns the nearest station in the radius
	 * @param address
	 * 				Address where to look for stations
	 * @return
	 * 				Nearest station in the radius from address
	 * @throws IOException
	 * 				If there was a problem with the connection
	 * @throws NoFormOfTransportException
	 * 				If the station has no forms of transport defined in the enum FormOfTransport
	 */
	public Station findNearestStation(Address address) throws IOException, NoFormOfTransportException {
		boolean isAValidPosition = false;
		try {
			isAValidPosition = getPosition(address);
		} catch (JSONException e) {
			return null;
		}
		if (!isAValidPosition) {
			return null;
		}
		String response = getResponseFromURL("https://transit.api.here.com/v3/stations/by_geocoord.json?"
				+ "center=" + latitude + "%2C" + longitude
				+ "&radius=" + radius
				+ "&app_id=" + AddressResolver.app_id
				+ "&app_code=" + AddressResolver.app_code
				+ "&max=" + max);
		
		JSONArray results;
		JSONObject firstResult;
		JSONArray transports;
		try {
			results = new JSONObject(response).getJSONObject("Res").getJSONObject("Stations").getJSONArray("Stn");
			firstResult = results.getJSONObject(0);
			transports = firstResult.getJSONObject("Transports").getJSONArray("Transport");
		} catch (JSONException e) {
			return null;
		}
		HashMap<String, FormOfTransport> lines = getLines(transports);
		if (lines.size() == 0) {
			throw new NoFormOfTransportException();
		}
		String name = firstResult.getString("name");
		String id = firstResult.getString("id");
		String city = firstResult.getString("city");
		double latitude = firstResult.getDouble("y");
		double longitude = firstResult.getDouble("x");
		
		return new Station(name, id, city, lines, latitude, longitude);
	}
	
	/**
	 * Returns a HashMap with all the names of the public transport and the form of transport
	 * defined in the enum FormOfTransport
	 * @param transports
	 * 					A JSONArray with all of the transport posibilities (found in the response JSONObject from HereAPI)
	 * @return
	 * 			HashMap with the names of the public transportation and the form of transport
	 */
	private HashMap<String, FormOfTransport> getLines(JSONArray transports) {
		HashMap<String,FormOfTransport> lines = new HashMap<>();
		for (int i=0; i<transports.length(); i++) {
			JSONObject el = transports.getJSONObject(i);
			switch (el.getInt("mode")) {
			case 4:
				lines.put(el.getString("name"), FormOfTransport.SBAHN);
				break;
			case 5:
				lines.put(el.getString("name"), FormOfTransport.BUS);
				break;
			case 7:
				lines.put(el.getString("name"), FormOfTransport.UBAHN);
				break;
			case 8:
				lines.put(el.getString("name"), FormOfTransport.TRAM);
				break;
			default:
				//ignore rest of transport ways
				break;
			}
		}
		return lines;
	}
	
	/**
	 * Sets the private variables latitude and longitude
	 * @param address
	 *              The address where it should return the latitude and longitude
	 * @return
	 *              true if the lantitude and longitude were found
	 *              false otherwise
	 * @throws IOException
	 *              if there was a problem with the connection
	 * @throws JSONException
	 *              if the response was the expected JSON
	 */
	private boolean getPosition(Address address) throws IOException, JSONException {
		String responseRaw = new AddressResolver().getResponseFromURL("http://geocoder.api.here.com/6.2/geocode.json?"
				+ "locationid=" + address.getLocationId()
				+ "&jsonattributes=1&gen=9"
				+ "&app_id=" + AddressResolver.app_id
				+ "&app_code=" + AddressResolver.app_code);
		JSONObject responseJson = new JSONObject(responseRaw).getJSONObject("response");
		JSONArray firstResult = responseJson.getJSONArray("view").getJSONObject(0).getJSONArray("result");
		JSONObject displayPosition = firstResult.getJSONObject(0).getJSONObject("location").getJSONObject("displayPosition");
		latitude = displayPosition.getDouble("latitude");
		longitude = displayPosition.getDouble("longitude");
		if (latitude == null || longitude == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Sends a HTTP GET request to the given URL and returns the result as a String
	 * @param link
	 * 			URL to send the GET request to
	 * @return
	 * 			The response body of the GET request as a String
	 * @throws IOException
	 * 			If there was a connection problem
	 */
	private String getResponseFromURL(String link) throws IOException {
		URL url = new URL(link);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		return body;
	}
}
