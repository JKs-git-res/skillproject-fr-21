package test.java.guideLines.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.java.guideLines.model.GermanEncodingConverter;

class GermanEncodingConverterTest {

	@Test
	void test() {
		String test = GermanEncodingConverter.convert("&Auml;&Ouml;&Uuml;&auml;&ouml;&uuml;&szlig;");
		assertEquals("ÄÖÜäöüß", test);
	}

}
