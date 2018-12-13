package guidelines.model;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import org.json.JSONException;

class AddressTest {

	private AddressResolver ar = new AddressResolver();
	
	
	@Test
	void AddressTestWithAllData() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Lothstraße 64 München");
		assertEquals("München", address.get(0).getCity());
		assertEquals(64, address.get(0).gethouseNumber());
		assertEquals("Lothstraße", address.get(0).getStreet());
		assertEquals("80335", address.get(0).getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA_2QD", address.get(0).getLocationId());
	}
	
	@Test
	void AddressTestWithoutHouseNumber() throws IOException {
		ArrayList<Address> address = ar.getAddressList("lothstraße München");
		assertEquals("München", address.get(0).getCity());
		assertEquals(-1, address.get(0).gethouseNumber());
		assertEquals("Lothstraße", address.get(0).getStreet());
		assertEquals("80335", address.get(0).getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA", address.get(0).getLocationId());
	}
	
	@Test
	void AddressTestWithoutStreet() throws IOException {		
		assertThrows(JSONException.class, ()-> {
			ar.getAddressList("München");
		});
		
	}
	
	@Test
	void TestRandomLocation() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Olympiapark München");
		assertEquals("Am Olympiapark", address.get(0).getStreet());
	}
	
	@Test
	void TestMultipleResults() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Bahnhofstraße München");
		assertEquals(5,address.size());
	}
	
	@Test
	void TestNotValidAddress() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Irgendwo");
		assertEquals(0,address.size());
	}
	
}
