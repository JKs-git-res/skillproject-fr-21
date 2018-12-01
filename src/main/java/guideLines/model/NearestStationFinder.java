package main.java.guideLines.model;

import main.java.guideLines.model.AddressResolver;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.java.guideLines.model.Address;

public class NearestStationFinder {
	
	private Double latitude = null;
	private Double longitude = null;
	String jsonResponse = null;

	public Station findNearestStation(Address address) throws IOException, JSONException {
		if (getPosition(address)) {
			Document doc = Jsoup.connect("https://places.cit.api.here.com/places/v1/browse/pt-stops?"
					+ "app_id=" + AddressResolver.app_id
					+ "&app_code=" + AddressResolver.app_code
 					+ "&at=" + latitude
					+ "," + longitude).get();
			Elements stations = doc.getElementsByTag("script");
			for (Element e: stations) {
				if (e.toString().contains("jsonResponse")) {
					jsonResponse = e.toString().replaceAll("<script>", "")
							.replaceAll("</script>", "")
							.replaceAll("//<!.*", "")
							.replaceAll("//].*", "")
							.replaceAll("jsonResponse =", "")
							.trim();
					break;
				}
			}
			JSONObject response = new JSONObject(jsonResponse);
			JSONArray results = response.getJSONObject("results").getJSONArray("items");
			JSONObject firstResult = results.getJSONObject(0);
			
			String closestStation = firstResult.getString("title");
			closestStation = GermanEncodingConverter.convert(closestStation);
			
			JSONObject transitLines = firstResult.getJSONObject("transitLines");
			JSONObject lines = transitLines.getJSONObject("lines");
			
			Iterator<String> keys = lines.keys();
			HashMap<String,FormOfTransport> transportWays = new HashMap<>();
			
			while(keys.hasNext()) {
			    String key = keys.next();
			    if (lines.get(key) instanceof JSONObject) {
			    	String mode = ((JSONObject) lines.get(key)).getJSONObject("category").getString("title");
			    	String lineName = ((JSONObject) lines.get(key)).getString("name");
			    	switch (mode) {
			    	case "Tram":
			    	case "Trolley" :
			    		transportWays.put(lineName, FormOfTransport.TRAM);
			    		break;
			    	case "Bus" :
			    		transportWays.put(lineName, FormOfTransport.BUS);
			    		break;
			    	case "Suburban Rail" : 
			    		transportWays.put(lineName, FormOfTransport.SBAHN);
			    		break;
			    	case "Subway" :
			    		transportWays.put(lineName, FormOfTransport.UBAHN);
			    		break;
			    	}
			    }
			}
			
			Station nextStation = new Station(closestStation, transportWays);
			return nextStation;
		}
		return null;
	}
	
	public boolean getPosition(Address address) throws IOException {
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
}
