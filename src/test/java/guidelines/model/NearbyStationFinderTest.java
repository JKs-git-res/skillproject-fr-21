package guidelines.model;



import java.io.IOException;
import static org.junit.Assert.*;

import org.junit.Test;

import guidelines.exceptions.NoFormOfTransportException;

public class NearbyStationFinderTest {

	@Test
	public void FindNearbyStationTest() throws IOException, NoFormOfTransportException {
		NearbyStationFinder nsf = new NearbyStationFinder();
		Address address = new Address("Bayerstraße","München","NT_IIphYQLPRrVnLxhhHePqsB_4A");
		Station station = nsf.findNearestStation(address);
		assertEquals("Hauptbahnhof", station.getName());
		assertEquals("mvv_1000006", station.getId());
		assertEquals("München", station.getCity());
	}
	
	@Test
	public void NoNearestStationFoundTest() throws IOException, NoFormOfTransportException {
		NearbyStationFinder nsf = new NearbyStationFinder();
		Address address = new Address("Wood River", "Saskatchewan", "NT_VPrVIZNQ8Zh5X3WZupItXC");
		Station station = nsf.findNearestStation(address);
		assertEquals(station, null);
	}
	
	@Test
	public void WrongLocationIdTest() throws IOException, NoFormOfTransportException {
		NearbyStationFinder nsf = new NearbyStationFinder();
		Address address = new Address("Wood River", "Saskatchewan", "NotAValidId");
		Station station = nsf.findNearestStation(address);
		assertEquals(station, null);
	}

}
