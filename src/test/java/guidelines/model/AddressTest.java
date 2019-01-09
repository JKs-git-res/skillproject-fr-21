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
		ArrayList<Address> address = ar.getAddressList("Lothstraße 64");
		assertEquals("München", address.get(0).getCity());
		assertEquals(64, address.get(0).gethouseNumber());
		assertEquals("Lothstraße", address.get(0).getStreet());
		assertEquals("80335", address.get(0).getPostCode());
		assertEquals("NT_GgesHsyrnzR3eEhPnUvBpA_2QD", address.get(0).getLocationId());
	}
	
	@Test
	public void LeierkastenTest() throws IOException {
		ArrayList<Address> address = ar.getAddressList("Leierkasten München");
		assertEquals(2, address.size());
		assertEquals(address.get(0).getStreet(), "Ingolstädter Straße");
		assertEquals(address.get(0).getCity(), "München");
		assertEquals(address.get(0).gethouseNumber(), 38);
	}
	
	@Test
	public void AddressTestWithoutHouseNumber() throws IOException {
		ArrayList<Address> address = ar.getAddressList("lothstraße München");
		assertEquals("München", address.get(0).getCity());
		assertEquals(-1, address.get(0).gethouseNumber());
		assertEquals("Lothstraße", address.get(0).getStreet());
		assertEquals("80797", address.get(0).getPostCode());
		assertEquals("NT_4iUGFZ3CdNSj94BaliwqmD", address.get(0).getLocationId());
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
		ArrayList<Address> address = ar.getAddressList("SoEinSchwachsinn");
		assertEquals(0,address.size());
	}
	
	@Test
	public void HofbrauTest() throws JSONException, IOException {
		ArrayList<Address> address = ar.getAddressList("Hofbräuhaus München");
		assertEquals("München",address.get(0).getCity());
		assertEquals("Platzl",address.get(0).getStreet());
		assertEquals(9, address.get(0).gethouseNumber());
	}
	
	@Test
	public void LandsbergerStrTest() throws JSONException, IOException {
		ArrayList<Address> address = ar.getAddressList("Landsberger straße 184");
		assertEquals("München",address.get(0).getCity());
		assertEquals("Landsberger Straße",address.get(0).getStreet());
		assertEquals(184, address.get(0).gethouseNumber());
		}
	
}
