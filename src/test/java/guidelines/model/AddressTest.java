package guidelines.model;


import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;
import org.json.JSONException;

public class AddressTest {

	private AddressResolver ar = new AddressResolver();
	
	
	@Test
	public void AddressTestWithAllData() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Lothstraße 64 München");
		assertEquals("München", address.get(0).getCity());
		assertEquals(64, address.get(0).gethouseNumber());
		assertEquals("Lothstraße", address.get(0).getStreet());
		assertEquals("80335", address.get(0).getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA_2QD", address.get(0).getLocationId());
	}
	
	@Test
	public void AddressTestWithoutHouseNumber() throws IOException {
		ArrayList<Address> address = ar.getAddressList("lothstraße München");
		assertEquals("München", address.get(0).getCity());
		assertEquals(-1, address.get(0).gethouseNumber());
		assertEquals("Lothstraße", address.get(0).getStreet());
		assertEquals("80335", address.get(0).getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA", address.get(0).getLocationId());
	}
	
	@Test(expected = JSONException.class)
	public void AddressTestWithoutStreet() throws IOException {		
		ar.getAddressList("München");
	}
	
	@Test
	public void TestRandomLocation() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Olympiapark München");
		assertEquals("Am Olympiapark", address.get(0).getStreet());
	}
	
	@Test
	public void TestMultipleResults() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Bahnhofstraße München");
		assertEquals(5,address.size());
	}
	
	@Test
	public void TestNotValidAddress() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Irgendwo");
		assertEquals(0,address.size());
	}
	
}
