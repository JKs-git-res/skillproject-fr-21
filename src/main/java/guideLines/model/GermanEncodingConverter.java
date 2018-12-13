package guideLines.model;

public class GermanEncodingConverter {
	
	public static String convert(String input) {
		String copy = input;
		copy = copy.replaceAll("&Auml;", "Ä");
		copy = copy.replaceAll("&Ouml;", "Ö");
		copy = copy.replaceAll("&Uuml;", "Ü");
		copy = copy.replaceAll("&auml;", "ä");
		copy = copy.replaceAll("&ouml;", "ö");
		copy = copy.replaceAll("&uuml;", "ü");
		copy = copy.replaceAll("&szlig;", "ß");
		return copy;
	}

}
