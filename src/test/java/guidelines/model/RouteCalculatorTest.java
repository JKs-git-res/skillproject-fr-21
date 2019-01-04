package guidelines.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;

/**
 * Da die Outputs sich ständig ändern teste ich nur ob der String leer ist oder nicht.
 * @author Benny
 *
 */
public class RouteCalculatorTest {
	
	private RouteCalculator rc = new RouteCalculator();

	@Test
	public void CalculateRouteArrivalTest() throws IOException, ParseException {
		Station departure = new Station("","","", new HashMap<String, FormOfTransport>(), 48.15427, 11.55383); // Lothstraße
		Station arrival = new Station("","","", new HashMap<String, FormOfTransport>(), 48.17871, 11.55667); // Olympiapark
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse ( "2018-12-16 14:00:00" ); 
		String route = rc.getRouteArrival(departure, arrival, date);
		assertEquals(false,route.isEmpty());
	}
	@Test
	public void CalculateRouteDepartureTest() throws IOException, ParseException {
		Station departure = new Station("","","", new HashMap<String, FormOfTransport>(), 48.15427, 11.55383); // Lothstraße
		Station arrival = new Station("","","", new HashMap<String, FormOfTransport>(), 48.17871, 11.55667); // Olympiapark
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse ( "2018-12-16 14:00:00" ); 
		String route = rc.getRouteDeparture(departure, arrival, date);
		assertEquals(false,route.isEmpty());
	}
	
	@Test
	public void GetPlanTest() throws JSONException, IOException {
		Station departure = new Station("","","", new HashMap<String, FormOfTransport>(), 48.15427, 11.55383); // Lothstraße
		Station arrival = new Station("","","", new HashMap<String, FormOfTransport>(), 48.17871, 11.55667); // Olympiapark
		String route = rc.getPlan(departure, arrival);
		assertEquals(false, route.isEmpty());
	}

}
