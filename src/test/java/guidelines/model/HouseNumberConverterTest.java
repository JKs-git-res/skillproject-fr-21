package guidelines.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class HouseNumberConverterTest {

	private HouseNumberConverter hnc = new HouseNumberConverter();
	
	@Test
	public void BigNumbersTest() {
		String adresse1 = "Lothstraße neuntausendneunhundertneunundneunzig München";
		String adresse2 = "Römerhang acht tausend acht hundert acht und achtzig Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(9999, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(8888, result);
	}
	
	
	@Test
	public void LeierkastenTest() {
		String adresse1 = "Leierkasten München";
		System.out.println(hnc.getAdressHereAPIFormatted(adresse1));
	}
	
	@Test
	public void MediumNumbersTest() {
		String adresse1 = "Lothstraße sechshunderteinundzwanzig München";
		String adresse2 = "Römerhang sieben hundert zweiundvierzig Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(621, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(742, result);
	}
	
	@Test
	public void LowNumbersTest() {
		String adresse1 = "Lothstraße sechsunddreißig München";
		String adresse2 = "Römerhang sieben und dreissig Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(36, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(37, result);
	}
	
	@Test
	public void SmallNumbersTest() {
		String adresse1 = "Lothstraße zwei München";
		String adresse2 = "Römerhang null Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(2, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(0, result);
	}
	
	@Test
	public void HereApiReadyAddressFormTest() {
		String adresse1 = "Lothstraße vierundsechzig München";
		String result = hnc.getAdressHereAPIFormatted(adresse1);
		assertEquals("Lothstraße  München 64", result);
	}
	
	

}
