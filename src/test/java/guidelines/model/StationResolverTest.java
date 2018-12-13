package guidelines.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import guidelines.exceptions.NoFormOfTransportException;
import guidelines.exceptions.StationNotFoundException;

class StationResolverTest {

	private StationResolver sr = new StationResolver();
	
	@Test
	void GetAddressTest() throws IOException, StationNotFoundException, NoFormOfTransportException {
		Station station = sr.getStation("Lilienthalallee");
		assertEquals("Lilienthalallee Süd", station.getName());
		assertEquals("München", station.getCity());
	}
	
	@Test
	void NotAStationTest() throws IOException, StationNotFoundException {
		assertThrows(StationNotFoundException.class, ()-> {
			sr.getStation("NichtEineStation");
		});
	}

	@Test
	void GetAddressTest2() throws IOException, StationNotFoundException, NoFormOfTransportException {
		Station station = sr.getStation("Hauptbahnhof");
		assertEquals("Hauptbahnhof", station.getName());
		assertEquals("München", station.getCity());
	}
}
