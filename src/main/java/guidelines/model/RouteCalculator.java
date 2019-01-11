package guidelines.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RouteCalculator {

	public long getTime(Station departure, Station arrival, Date departureTime) throws IOException, ParseException {
		if(departureTime.getTime() < new Date().getTime()){
			//falls der Zeitpunkt in der Vergangenheit liegt wird ein Tag dazugerechnet (=86,4 Mio Millisekunden)
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(departureTime.getTime() +86400000);
			departureTime = cal.getTime();
		}
		JSONArray connections = new JSONObject(getJSONresponse(departure, arrival, departureTime, 0))
				.getJSONObject("Res").getJSONObject("Connections").getJSONArray("Connection");
		JSONObject choice = getNextConnection(connections, departureTime, true);
		if (choice == null) {
			return -1;
		}
		String time = choice.getJSONObject("Dep").getString("time");
		SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = ISO8601DATEFORMAT.parse(time);
		Date now = new Date();
		long diff = date.getTime() - now.getTime();
		diff /= 1000;
		diff /= 60;
		return diff;
	}

	/**
	 * Tells the exact route where and where to change and how many stops to ride the {@link FormOfTransport}.
	 * Searches the route that has the specified departure time (or the rout with the departure time closest to it)
	 * @param departure
	 * 					The station where to start from
	 * @param arrival
	 * 					The destination station
	 * @param departureTime
	 * 					The departure time
	 * @return
	 * 					The complete route with the time and stops number.
	 * @throws IOException
	 */
	public String getRouteDeparture(Station departure, Station arrival, Date departureTime) throws IOException {
		JSONArray connections = new JSONObject(getJSONresponse(departure, arrival, departureTime, 0))
				.getJSONObject("Res").getJSONObject("Connections").getJSONArray("Connection");
		JSONObject choice = getNextConnection(connections, departureTime, false);
		if (choice == null) {
			return null;
		}
		String time = choice.getJSONObject("Arr").getString("time").split("T")[1];
		time = getTimePretty(time);
		JSONArray sections = choice.getJSONObject("Sections").getJSONArray("Sec");
		String indications = getIndications(sections) + "Ankunftzeit: " + time;
		return indications;
	}

	/**
	 * Tells the exact route where and where to change and how many stops to ride the {@link FormOfTransport}.
	 * Searches the route that has the specified arrival time (or the rout with the arrival time closest to it)
	 * @param departure
	 * 					The departure station
	 * @param arrival
	 * 					The arrival station
	 * @param arrivalTime
	 * 					The arrival time
	 * @return
	 * 					The complete route with the time and stops number.
	 * @throws IOException
	 */
	public String getRouteArrival(Station departure, Station arrival, Date arrivalTime) throws IOException {
		JSONArray connections = new JSONObject(getJSONresponse(departure, arrival, arrivalTime, 1))
				.getJSONObject("Res").getJSONObject("Connections").getJSONArray("Connection");
		JSONObject choice = getNextConnection(connections, arrivalTime, true);
		if (choice == null) {
			return null;
		}
		JSONArray sections = choice.getJSONObject("Sections").getJSONArray("Sec");
		String time = choice.getJSONObject("Arr").getString("time").split("T")[1];
		time = getTimePretty(time);
		String indications = getIndications(sections) + "Ankunftzeit: " + time;
		return indications;
	}


	/**
	 * Returns inications what {@link FormOfTransport} to take, when and where to change
	 * @param sections
	 * 					The sections from the response of Here API
	 * @return
	 * 					The string with all the indications
	 */
	private String getIndications(JSONArray sections) {
		StringBuilder plan = new StringBuilder();
		for (int i=0; i<sections.length(); i++) {
			JSONObject section = sections.getJSONObject(i);
			String time = section.getJSONObject("Dep").getString("time").split("T")[1];
			time = getTimePretty(time);
			int mode = section.getInt("mode");
			if (mode != 4 && mode != 5 && mode != 7 && mode != 8 && mode != 12) {
				continue;
			}
			String direction = section.getJSONObject("Dep").getJSONObject("Transport").getString("dir");
			String name = section.getJSONObject("Dep").getJSONObject("Transport").getString("name");
			String category = section.getJSONObject("Dep").getJSONObject("Transport").getJSONObject("At").getString("category");
			int stops = section.getJSONObject("Journey").getJSONArray("Stop").length();
			String arr = section.getJSONObject("Arr").getJSONObject("Stn").getString("name");
			arr = arr.replaceAll("[\\*|0-9]", "");
			plan.append("Um ").append(time);
			if (mode == 5) {
				plan.append(" in den ");
			} else {
				plan.append(" in die ");
			}
			plan.append(category).append(" ").append(name).append(" einsteigen und ");
			if (stops == 1) {
				plan.append("eine Station in die Richtung ");
			} else {
				plan.append(stops).append(" Stationen in die Richtung ");
			}
			plan.append(direction).append(" bis ").append(arr).append(" fahren. ");
		}
		return plan.toString();
	}


	/**
	 * Tells the plan how to get from start to destination station.
	 * But doesn't tell when is the next one. It tells just how to get there and the time of the trip.
	 * @param departure
	 * 					The station where to start
	 * @param arrival
	 * 					The destination station
	 * @return
	 * 					The plan how to get from A to B
	 * @throws JSONException
	 * 					If the JSON response from Here API was messed up
	 * @throws IOException
	 * 					If there was a problem with the connection
	 */
	public String getPlan(Station departure, Station arrival) throws JSONException, IOException {
		StringBuilder plan = new StringBuilder();
		JSONArray connections = new JSONObject(getJSONresponse(departure, arrival, new Date(), 0))
				.getJSONObject("Res").getJSONObject("Connections").getJSONArray("Connection");
		JSONObject choice = getNextConnection(connections, new Date(), false);
		if (choice == null) {
			return null;
		}
		JSONArray sections = choice.getJSONObject("Sections").getJSONArray("Sec");
		String duration = choice.getString("duration").replaceAll("PT", "");
		String hours = duration.split("H")[0];
		String minutes = duration.split("H")[1].replaceAll("M", "");
		int transfers = choice.getInt("transfers");
		boolean started = false;
		for (int i=0; i<sections.length(); i++) {
			JSONObject section = sections.getJSONObject(i);
			int mode = section.getInt("mode");
			if (mode != 4 && mode != 5 && mode != 7 && mode != 8) {
				continue;
			}
			String name = section.getJSONObject("Dep").getJSONObject("Transport").getString("name");
			String category = section.getJSONObject("Dep").getJSONObject("Transport").getJSONObject("At").getString("category");
			String arr = section.getJSONObject("Arr").getJSONObject("Stn").getString("name");
			if (mode != 5 && !started) {
				plan.append("Nimm die ");
				started = true;
			} else if (mode == 5 && !started) {
				plan.append("Nimm den ");
				started = true;
			} else if (mode != 5 && started) {
				plan.append(" die ");
			} else {
				plan.append(" den ");
			}
			plan.append(category).append(" ").append(name).append(" nach ").append(arr);
			if (transfers != 0) {
				plan.append(" und von dort");
				transfers--;
			}
		}
		plan.append(". Die Reise dauert ");
		if (!hours.equals("0")) {
			plan.append(hours).append(" Stunden");
		}
		if (!hours.equals("0") && !minutes.equals("0")) {
			if (minutes.startsWith("0")) {
				minutes = minutes.replaceAll("0", "");
			}
			plan.append(hours).append(" Stunden und ").append(minutes).append(" Minuten");
		} else if (!hours.equals("0") && minutes.equals("0")) {
			plan.append(hours).append(" Stunden");
		} else if (hours.equals("0")) {
			plan.append(minutes).append(" Minuten");
		}
		return plan.toString();
	}

	/**
	 * Returns time in german for Alexa to say
	 * @param time
	 * 				Time in the format HH:mm
	 * @return
	 * 				Time in german example: 10:10 -> 10 Uhr 10
	 */
	private String getTimePretty(String time) {
		time = time.substring(0, time.length()-3);
		String hour = time.split(":")[0];
		String minute = time.split(":")[1];
		if (hour.startsWith("0")) {
			hour = hour.replaceAll("0", "");
		}
		if (minute.startsWith("0")) {
			minute = minute.replaceAll("0", "");
		}
		return hour + " Uhr " + minute;
	}

	/**
	 * Some connection are in the past (the bus already left).
	 * This method gives back the next available connection.
	 * Also checks if the connection is in the given time range
	 * @param connections
	 * 					A {@link JSONArray} from the Here API response with all the connections
	 * @param time
	 * 					Either departure time or arrival time
	 * @param isArrivalTime
	 * 					Specifies if the given time is arrival time or not.
	 * 					arrival time (true) and departure time (false)
	 * @return
	 * 					The next available connection
	 */
	private JSONObject getNextConnection(JSONArray connections, Date time, boolean isArrivalTime) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		JSONObject choice;
		JSONObject result = null;
		for (int j=0; j<connections.length(); j++) {
			choice = connections.getJSONObject(j);
			String arr = choice.getJSONObject("Arr").getString("time");
			String dep = choice.getJSONObject("Dep").getString("time");
			Date now;
			Date departure;
			Date arrival;
			String givenTime = parser.format(time);
			try {
				departure = parser.parse(dep);
				arrival = parser.parse(arr);
				now = parser.parse(parser.format(new Date()));
				time = parser.parse(givenTime);
				if (now.before(departure) && now.before(arrival)) {
					if (isArrivalTime) {
						if (time.before(arrival)) {
							result =  choice;
						} else {
							break;
						}
					} else if (time.before(departure)) {
						result =  choice;
					} else {
						break;
					}
				}
			} catch (ParseException e) {
				throw new RuntimeException();
			}
		}
		return result;
	}

	/**
	 * Gets the JSON response from the Here API
	 * @param departure
	 * 					The station where to start from
	 * @param arrival
	 * 					The destination station
	 * @param arrParam
	 * 					Switches between arrival and departure time.
	 * 					If enabled, makes routing request to interpret time as arrival time for the journey.
	 * 					1 (enabled), 0 (disabled)
	 * @return
	 * 					The JSON response from Here API
	 * @throws IOException
	 * 					If there was a problem with the connection
	 */
	private String getJSONresponse(Station departure, Station arrival, Date time, int arrParam) throws IOException {
		String response = new AddressResolver().getResponseFromURL("https://transit.api.here.com/v3/route.json"
				+ "?app_id=" + AddressResolver.app_id
				+ "&app_code=" + AddressResolver.app_code
				+ "&routing=all"
				+ "&dep=" + departure.getLatitude() + "," + departure.getLongitude()
				+ "&arr=" + arrival.getLatitude() + "," + arrival.getLongitude()
				+ "&time=" + getTimeFormatted(time)
				+ "&arrival=" + arrParam);
		return response;
	}

	/**
	 * Returns time in the format: yyyy-MM-ddTHH:mm:ss URL encoded.
	 * @return
	 * 			Time in the format: yyyy-MM-ddTHH:mm:ss URL encoded.
	 * @throws UnsupportedEncodingException
	 * 			If the encoding is not supported
	 */
	private String getTimeFormatted(Date date) throws UnsupportedEncodingException {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = dateFormat.format(date);
		String formattedDate2 = dateFormat2.format(date);
		return URLEncoder.encode(formattedDate2 + "T" + formattedDate, "UTF-8");
	}

}