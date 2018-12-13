package guidelines.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import guidelines.exceptions.NoFormOfTransportException;
import guidelines.model.Address;
import guidelines.model.NearbyStationFinder;
import guidelines.model.Station;

class NearbyStationFinderTest {

	@Test
	void FindNearbyStationTest() throws IOException, NoFormOfTransportException {
		NearbyStationFinder nsf = new NearbyStationFinder();
		Address address = new Address("Bayerstraße","München","NT_IIphYQLPRrVnLxhhHePqsB_4A");
		Station station = nsf.findNearestStation(address);
		assertEquals("Hauptbahnhof", station.getName());
		assertEquals("mvv_1000006", station.getId());
		assertEquals("München", station.getCity());
	}
	
	@Test
	void NoNearestStationFoundTest() throws IOException, NoFormOfTransportException {
		NearbyStationFinder nsf = new NearbyStationFinder();
		Address address = new Address("Wood River", "Saskatchewan", "NT_VPrVIZNQ8Zh5X3WZupItXC");
		Station station = nsf.findNearestStation(address);
		assertEquals(station, null);
	}
	
	@Test
	void WrongLocationIdTest() throws IOException, NoFormOfTransportException {
		NearbyStationFinder nsf = new NearbyStationFinder();
		Address address = new Address("Wood River", "Saskatchewan", "NotAValidId");
		Station station = nsf.findNearestStation(address);
		assertEquals(station, null);
	}

}
