package test.java.guideLines.handlers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.java.guideLines.model.HouseNumberConverter;

class HouseNumberConverterTest {

	private HouseNumberConverter hnc = new HouseNumberConverter();
	
	@Test
	void BigNumbersTest() {
		String adresse1 = "Lothstraße neuntausendneunhundertneunundneunzig München";
		String adresse2 = "Römerhang acht tausend acht hundert acht und achtzig Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(9999, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(8888, result);
	}
	
	@Test
	void MediumNumbersTest() {
		String adresse1 = "Lothstraße sechshunderteinundzwanzig München";
		String adresse2 = "Römerhang sieben hundert zweiundvierzig Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(621, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(742, result);
	}
	
	@Test
	void LowNumbersTest() {
		String adresse1 = "Lothstraße sechsunddreißig München";
		String adresse2 = "Römerhang sieben und dreissig Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(36, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(37, result);
	}
	
	@Test
	void SmallNumbersTest() {
		String adresse1 = "Lothstraße zwei München";
		String adresse2 = "Römerhang null Landsberg am Lech";
		int result = hnc.getHouseNumber(adresse1);
		assertEquals(2, result);
		result = hnc.getHouseNumber(adresse2);
		assertEquals(0, result);
	}
	
	@Test
	void HereApiReadyAddressFormTest() {
		String adresse1 = "Lothstraße vierundsechzig München";
		String result = hnc.getAdressHereAPIFormatted(adresse1);
		assertEquals("Lothstraße  München 64", result);
	}
	
	

}
