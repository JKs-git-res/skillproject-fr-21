package test.java.guideLines.handlers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import main.java.guideLines.model.Address;
import main.java.guideLines.model.NearestStationFinder;
import main.java.guideLines.model.Station;

class NearestStationFinderTest {

	@Test
	void FindNearestStationTest() throws IOException {
		NearestStationFinder nsf = new NearestStationFinder();
		Address address = new Address("Bayerstraße","München","NT_IIphYQLPRrVnLxhhHePqsB_4A");
		Station station = nsf.findNearestStation(address);
		assertEquals(station.getName(), "Hauptbahnhof");
	}
	
	@Test
	void GetPositionTest() throws IOException {
		Address address = new Address("Lothstraße","München","NT_GgesHsyrnzR3eEhPnUvBpA_2QD");
		NearestStationFinder stationFinder = new NearestStationFinder();
		boolean actual = stationFinder.getPosition(address);
		assertEquals(true, actual);
	}
	
	@Test
	void NoNearestStationFoundTest() throws IOException {
		NearestStationFinder nsf = new NearestStationFinder();
		Address address = new Address("Wood River", "Saskatchewan", "NT_VPrVIZNQ8Zh5X3WZupItXC");
		Station station = nsf.findNearestStation(address);
		assertEquals(station, null);
	}
	
	@Test
	void WrongLocationIdTest() throws IOException {
		NearestStationFinder nsf = new NearestStationFinder();
		Address address = new Address("Wood River", "Saskatchewan", "NotAValidId");
		Station station = nsf.findNearestStation(address);
		assertEquals(station, null);
	}

}
