package guidelines.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;



import guidelines.exceptions.NoFormOfTransportException;
import guidelines.exceptions.StationNotFoundException;

public class StationResolverTest {

	private StationResolver sr = new StationResolver();
	
	@Test
	public void GetAddressTest() throws IOException, StationNotFoundException, NoFormOfTransportException {
		Station station = sr.getStation("Lilienthalallee");
		assertEquals("Lilienthalallee Süd", station.getName());
		assertEquals("München", station.getCity());
	}
	
	@Test(expected = StationNotFoundException.class)
	public void NotAStationTest() throws IOException, StationNotFoundException, NoFormOfTransportException {
		sr.getStation("NichtEineStation");
	}

	@Test
	public void GetAddressTest2() throws IOException, StationNotFoundException, NoFormOfTransportException {
		Station station = sr.getStation("Hauptbahnhof");
		assertEquals("Hauptbahnhof", station.getName());
		assertEquals("München", station.getCity());
	}
}
