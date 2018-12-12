package main.java.guideLines.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.exceptions.NoFormOfTransportException;
import main.java.exceptions.StationNotFoundException;

public class StationResolver {
	private final double munichLatitude = 48.13642;
	private final double munichLongitude = 11.57755;
	private final int max = 10;
	private String name = null;
	private String city = null;
	
	/**
	 * Returns the station from a String
	 * @param station
	 * 				The user input (name of the station)
	 * @return
	 * 				The station based on user input as a Station object
	 * @throws IOException
	 * 				If there was a problem with the connection
	 * @throws StationNotFoundException
	 * 				If the station could't be found
	 * @throws NoFormOfTransportException 
	 * 				If the station has no forms of transport defined in {@link FormOfTransport}
	 */
	public Station getStation(String station) throws IOException, StationNotFoundException, NoFormOfTransportException {
		String stationId;
		try {
			stationId = getStationID(station);
		} catch (JSONException e) {
			stationId = null;
		}
		if (stationId == null) {
			throw new StationNotFoundException();
		}
		String response = getResponseFromURL("https://transit.api.here.com/v3/multiboard/by_stn_ids.json?"
				+ "stnIds=" + stationId
				+ "&app_id=" + AddressResolver.app_id
				+ "&app_code=" + AddressResolver.app_code
				+ "&time=" + getTimeFormatted()
				+ "&max=" + max);
		JSONArray departures = new JSONObject(response)
				.getJSONObject("Res")
				.getJSONObject("MultiNextDepartures")
				.getJSONArray("MultiNextDeparture")
				.getJSONObject(0)
				.getJSONObject("NextDepartures")
				.getJSONArray("Dep");
		HashMap<String, FormOfTransport> lines = getLines(departures);
		if (lines.size() == 0) {
			throw new NoFormOfTransportException();
		}
		return new Station(name, stationId, city, lines);
	}
	
	/**
	 * Returns a HashMap with all the names of the public transport and the form of transport
	 * defined in the enum FormOfTransport
	 * @param departures
	 * 					A JSONArray with all of the transport posibilities
	 * 					(found in the response JSONObject from HereAPI)
	 * @return
	 * 			HashMap with the names of the public transportation and the form of transport
	 */
	private HashMap<String, FormOfTransport> getLines(JSONArray departures) {
		HashMap<String,FormOfTransport> lines = new HashMap<>();
		for (int i=0; i<departures.length(); i++) {
			JSONObject el = departures.getJSONObject(i).getJSONObject("Transport");
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
	 * Returns time in the format: yyyy-MM-ddTHH:mm:ss URL encoded.
	 * @return
	 * 			Time in the format: yyyy-MM-ddTHH:mm:ss URL encoded.
	 * @throws UnsupportedEncodingException
	 * 			If the encoding is not supported 
	 */
	private String getTimeFormatted() throws UnsupportedEncodingException {
	    Calendar cal = Calendar.getInstance();
	    Date date=cal.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	    DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	    String formattedDate = dateFormat.format(date);
	    String formattedDate2 = dateFormat2.format(date);
	    return URLEncoder.encode(formattedDate2 + "T" + formattedDate, "UTF-8");
	}
	
	/**
	 * Searches for the station based on the user input and if it find it,
	 * it returns the station id from the Here API and also sets the name variable with the official name.
	 * Attention!!! Only looks for stations in Munich
	 * @param name
	 * 			Name of the station to look for
	 * @return
	 * 			Station id of the given input
	 * @throws UnsupportedEncodingException
	 * 			If the encoding is not supported
	 * @throws IOException
	 * 			If there was a problem with the connection
	 */
	private String getStationID(String name) throws UnsupportedEncodingException, IOException {
		String response = getResponseFromURL("https://transit.api.here.com/v3/stations/by_name.json?"
				+ "center=" + munichLatitude + "%2C" + munichLongitude
				+ "&name=" + URLEncoder.encode(name, "UTF-8")
				+ "&app_id=" + AddressResolver.app_id
				+ "&app_code=" + AddressResolver.app_code
				+ "&method=fuzzy" // search for stations having similar sounding names (or containing the name)
				+ "&max=" + max);
		JSONArray stations = new JSONObject(response).getJSONObject("Res").getJSONObject("Stations").getJSONArray("Stn");
		JSONObject firstResult = stations.getJSONObject(0); // Most of the cases the first result is the best
		this.name = firstResult.getString("name");
		this.city = firstResult.getString("city");
		return firstResult.getString("id");
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
