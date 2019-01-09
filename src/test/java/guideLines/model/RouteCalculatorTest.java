package test.java.guideLines.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import main.java.guideLines.model.FormOfTransport;
import main.java.guideLines.model.RouteCalculator;
import main.java.guideLines.model.Station;

class RouteCalculatorTest {
	
	private RouteCalculator rc = new RouteCalculator();

	@Test
	void CalculateRouteTest() throws IOException {
		Station departure = new Station("","","", new HashMap<String, FormOfTransport>(), 48.15427, 11.55383); // Lothstraße
		Station arrival = new Station("","","", new HashMap<String, FormOfTransport>(), 48.17871, 11.55667); // Olympiapark
		String route = rc.getRoute(departure, arrival);
		assertEquals(false,route.isEmpty());
	}

}