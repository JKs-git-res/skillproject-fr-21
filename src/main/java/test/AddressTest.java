package main.java.test;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.handlers.AddressResolver;
import main.java.guideLines.handlers.NearestStationFinder;
import main.java.guideLines.model.Address;

class AddressTest {

	private AddressResolver ar = new AddressResolver();
	
	
	@Test
	void AddressTestWithAllData() throws IOException, StreetNotFoundException {
		Address address = ar.getAddress("lothstra�e 64 M�nchen");
		assertEquals("M�nchen", address.getCity());
		assertEquals(64, address.gethouseNumber());
		assertEquals("Lothstra�e", address.getStreet());
		assertEquals("80335", address.getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA_2QD", address.getLocationId());
	}
	
	@Test
	void AddressTestWithoutHouseNumber() throws IOException, StreetNotFoundException {
		Address address = ar.getAddress("lothstra�e M�nchen");
		assertEquals("M�nchen", address.getCity());
		assertEquals(-1, address.gethouseNumber());
		assertEquals("Lothstra�e", address.getStreet());
		assertEquals("80335", address.getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA", address.getLocationId());
	}
	
	@Test
	void AddressTestWithoutStreet() throws IOException, StreetNotFoundException {		
		assertThrows(StreetNotFoundException.class, ()-> {
			Address address = ar.getAddress("M�nchen");
		});
	}
	
	@Test
	void GetPositionTest() throws IOException {
		Address address = new Address("Lothstra�e","M�nchen","NT_GgesHsyrnzR3eEhPnUvBpA_2QD");
		NearestStationFinder stationFinder = new NearestStationFinder();
		boolean actual = stationFinder.getPosition(address);
		assertEquals(true, actual);
	}
	
	@Test
	void FindNearestStationTest() throws IOException {
		NearestStationFinder nsf = new NearestStationFinder();
		Address address = new Address("Lothstra�e","M�nchen","NT_GgesHsyrnzR3eEhPnUvBpA_2QD");
		Address station = nsf.findNearestStation(address);
		assertEquals(null, station);
	}
	
	

}
