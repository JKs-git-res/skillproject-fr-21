package test.java.guideLines.handlers;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.model.AddressResolver;
import main.java.guideLines.model.NearestStationFinder;
import main.java.guideLines.model.Address;

class AddressTest {

	private AddressResolver ar = new AddressResolver();
	
	
	@Test
	void AddressTestWithAllData() throws IOException, StreetNotFoundException {
		Address address = ar.getAddress("Lothstraße 64 München");
		assertEquals("München", address.getCity());
		assertEquals(64, address.gethouseNumber());
		assertEquals("Lothstraße", address.getStreet());
		assertEquals("80335", address.getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA_2QD", address.getLocationId());
	}
	
	@Test
	void AddressTestWithoutHouseNumber() throws IOException, StreetNotFoundException {
		Address address = ar.getAddress("lothstraße München");
		assertEquals("München", address.getCity());
		assertEquals(-1, address.gethouseNumber());
		assertEquals("Lothstraße", address.getStreet());
		assertEquals("80335", address.getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA", address.getLocationId());
	}
	
	@Test
	void AddressTestWithoutStreet() throws IOException, StreetNotFoundException {		
		assertThrows(StreetNotFoundException.class, ()-> {
			Address address = ar.getAddress("München");
		});
	}
	
	@Test
	void GetPositionTest() throws IOException {
		Address address = new Address("Lothstraße","München","NT_GgesHsyrnzR3eEhPnUvBpA_2QD");
		NearestStationFinder stationFinder = new NearestStationFinder();
		boolean actual = stationFinder.getPosition(address);
		assertEquals(true, actual);
	}
	
	@Test
	void FindNearestStationTest() throws IOException {
		NearestStationFinder nsf = new NearestStationFinder();
		Address address = new Address("Lothstraße","München","NT_GgesHsyrnzR3eEhPnUvBpA_2QD");
		Address station = nsf.findNearestStation(address);
		assertEquals(null, station);
	}

	
	

}
