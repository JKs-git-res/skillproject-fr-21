package guidelines.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class RouteCalculator {
	
	public String getRoute(Station departure, Station arrival) throws IOException {
		String response = new AddressResolver().getResponseFromURL("https://transit.api.here.com/v3/route.json"
				+ "?app_id=" + AddressResolver.app_id
				+ "&app_code=" + AddressResolver.app_code
				+ "&routing=all"
				+ "&dep=" + departure.getLatitude() + "," + departure.getLongitude()
				+ "&arr=" + arrival.getLatitude() + "," + arrival.getLongitude()
				+ "&time=" + getTimeFormatted());

		StringBuilder plan = new StringBuilder();
		
		JSONArray connections = new JSONObject(response).getJSONObject("Res").getJSONObject("Connections").getJSONArray("Connection");
		JSONObject lastChoice = connections.getJSONObject(connections.length()-1);
		JSONArray sections = lastChoice.getJSONObject("Sections").getJSONArray("Sec");
		for (int i=0; i<sections.length(); i++) {
			JSONObject section = sections.getJSONObject(i);
			int mode = section.getInt("mode");
			if (mode != 4 && mode != 5 && mode != 7 && mode != 8) {
				continue;
			}
			String time = section.getJSONObject("Dep").getString("time").split("T")[1];
			time = time.substring(0, time.length()-3);
			String direction = section.getJSONObject("Dep").getJSONObject("Transport").getString("dir");
			String name = section.getJSONObject("Dep").getJSONObject("Transport").getString("name");
			String category = section.getJSONObject("Dep").getJSONObject("Transport").getJSONObject("At").getString("category");
			int stops = section.getJSONObject("Journey").getJSONArray("Stop").length();
			String arr = section.getJSONObject("Arr").getJSONObject("Stn").getString("name");
			plan.append("Um ").append(time);
			if (mode == 5) {
				plan.append(" in den ");
			} else {
				plan.append(" in die ");
			}
			plan.append(category).append(" ").append(name).append(" einsteigen und ").append(stops).append(" Stationen in die Richtung ").append(direction).append(" bis ").append(arr).append(" fahren. ");
		}
		return plan.toString();
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

}
