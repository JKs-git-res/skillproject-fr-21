package test.java.guideLines.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import main.java.guideLines.model.DeutscheBahnRoutePlanner;
import main.java.guideLines.model.FormOfTransport;
import main.java.guideLines.model.Station;

class DBRoutePlannerTest {

	DeutscheBahnRoutePlanner dbrp = new DeutscheBahnRoutePlanner();
	@Test
	void GetHTMLTest() throws IOException {
		Station departure = new Station("Lilienthalallee Süd", "01", "München", new HashMap<String, FormOfTransport>());
		Station destination = new Station("Hauptbahnhof", "02", "München", new HashMap<String, FormOfTransport>());
		dbrp.getRouteInfo(departure, destination);
	}

}
