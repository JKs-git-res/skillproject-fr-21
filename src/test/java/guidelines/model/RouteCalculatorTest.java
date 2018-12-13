package guidelines.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import guidelines.model.FormOfTransport;
import guidelines.model.RouteCalculator;
import guidelines.model.Station;
/**
 * Da die Outputs sich ständig ändern teste ich nur ob der String leer ist oder nicht.
 * @author Benny
 *
 */
class RouteCalculatorTest {
	
	private RouteCalculator rc = new RouteCalculator();

	@Test
	void CalculateRouteTest() throws IOException {
		Station departure = new Station("","","", new HashMap<String, FormOfTransport>(), 48.15427, 11.55383); // Lothstraße
		Station arrival = new Station("","","", new HashMap<String, FormOfTransport>(), 48.17871, 11.55667); // Olympiapark
		String route = rc.getRoute(departure, arrival);
		assertEquals(false,route.isEmpty());
	}
	
	@Test
	void GetPlanTest() throws JSONException, IOException {
		Station departure = new Station("","","", new HashMap<String, FormOfTransport>(), 48.15427, 11.55383); // Lothstraße
		Station arrival = new Station("","","", new HashMap<String, FormOfTransport>(), 48.17871, 11.55667); // Olympiapark
		String route = rc.getPlan(departure, arrival);
		assertEquals(false, route.isEmpty());
	}

}
