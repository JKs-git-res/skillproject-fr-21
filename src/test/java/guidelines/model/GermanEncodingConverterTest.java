package guidelines.model;

import static org.junit.Assert.*;

import org.junit.Test;

class GermanEncodingConverterTest {

	@Test
	void test() {
		String test = GermanEncodingConverter.convert("&Auml;&Ouml;&Uuml;&auml;&ouml;&uuml;&szlig;");
		assertEquals("ÄÖÜäöüß", test);
	}

}
