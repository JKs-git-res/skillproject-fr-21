package main.java.guideLines.model;

public class HouseNumberConverter {
	
	private int value = 0;
	private StringBuilder adressWithoutNumber = new StringBuilder();
	
	/**
	 * Translates the user input (words) into integer values. Used in this application for housenumbers
	 * @param address
	 *             The address to read the housenumber from
	 * @return
	 *             The integer value of the user input
	 */
	public int getHouseNumber(String address) {
		
		String copy = address;
		if (copy.contains("tausend")) {
			copy = resolveTausend(copy);
		}
		if (copy.contains("hundert")) {
			copy = resolveHundert(copy);
		}
		if (copy.contains("zig") || copy.contains("sig") || copy.contains("ßig")) {
			copy = resolveZwanzig99(copy);
		}
		copy = resolveZehnNeunzehn(copy);
		
		copy = resolveSmallNumbers(copy);
		
		int returnVal = value;
		adressWithoutNumber.append(copy).append(" ").append(value);
		value = 0;
		
		return returnVal;
	}
	
	/**
	 * Formats the given address to a form that Here API can process
	 * For example: 
	 *              If input is "Lothstraße vierundsechzig München"
	 *              the output will be:
	 *              "Lothstraße München 64"
	 *              The Here API can process the house number even if it is at the end
	 * @param address
	 *              The address to be formated
	 * @return
	 *              The address in a Here API ready form
	 */
	public String getAdressHereAPIFormatted(String address) {
		getHouseNumber(address);
		return adressWithoutNumber.toString();
	}
	
	private String resolveSmallNumbers(String input) {
		String copy = input;
		if (copy.contains("eins")) {
			value += 1;
			copy = copy.replaceAll("eins", "");
		} else if (copy.contains("zwei")) {
			value += 2;
			copy = copy.replaceAll("zwei", "");
		} else if (copy.contains("drei")) {
			value += 3;
			copy = copy.replaceAll("drei", "");
		} else if (copy.contains("vier")) {
			value += 4;
			copy = copy.replaceAll("vier", "");
		} else if (copy.contains("fünf")) {
			value += 5;
			copy = copy.replaceAll("fünf", "");
		} else if (copy.contains("sechs")) {
			value += 6;
			copy = copy.replaceAll("sechs", "");
		} else if (copy.contains("sieben")) {
			value += 7;
			copy = copy.replaceAll("sieben", "");
		} else if (copy.contains("acht")) {
			value += 8;
			copy = copy.replaceAll("acht", "");
		} else if (copy.contains("neun")) {
			value += 9;
			copy = copy.replaceAll("neun", "");
		} else if (copy.contains("null")) {
			value += 0;
			copy = copy.replaceAll("null", "");
		}
		
		return copy;
	}
	
	private String resolveZwanzig99(String input) {
		String copy = input;
		if (copy.contains("einund") || copy.contains("ein und")) {
			value += 1;
			copy = copy.replaceAll("einund", "").replaceAll("ein und", "");
		} else if (copy.contains("zweiund") || copy.contains("zwei und")) {
			value += 2;
			copy = copy.replaceAll("zweiund", "").replaceAll("zwei und", "");
		} else if (copy.contains("dreiund") || copy.contains("drei und")) {
			value += 3;
			copy = copy.replaceAll("dreiund", "").replaceAll("drei und", "");
		} else if (copy.contains("vierund") || copy.contains("vier und")) {
			value += 4;
			copy = copy.replaceAll("vierund", "").replaceAll("vier und", "");
		} else if (copy.contains("fünfund") || copy.contains("fünf und")) {
			value += 5;
			copy = copy.replaceAll("fünfund", "").replaceAll("fünf und", "");
		} else if (copy.contains("sechsund") || copy.contains("sechs und")) {
			value += 6;
			copy = copy.replaceAll("sechsund", "").replaceAll("sechs und", "");
		} else if (copy.contains("siebenund") || copy.contains("sieben und")) {
			value += 7;
			copy = copy.replaceAll("siebenund", "").replaceAll("sieben und", "");
		} else if (copy.contains("achtund") || copy.contains("acht und")) {
			value += 8;
			copy = copy.replaceAll("achtund", "").replaceAll("acht und", "");
		} else if (copy.contains("neunund") || copy.contains("neun und")) {
			value += 9;
			copy = copy.replaceAll("neunund", "").replaceAll("neun und", "");
		}
		if (copy.contains("zwanzig")) {
			value += 20;
			copy = copy.replaceAll("zwanzig", "");
		} else if (copy.contains("dreissig") || copy.contains("dreißig")) {
			value += 30;
			copy = copy.replaceAll("dreissig", "").replaceAll("dreißig", "");
		} else if(copy.contains("vierzig")) {
			value += 40;
			copy = copy.replaceAll("vierzig", "");
		} else if (copy.contains("fünfzig")) {
			value += 50;
			copy = copy.replaceAll("fünfzig", "");
		} else if (copy.contains("sechzig")) {
			value += 60;
			copy = copy.replaceAll("sechzig", "");
		} else if (copy.contains("siebzig")) {
			value += 70;
			copy = copy.replaceAll("siebzig", "");
		} else if (copy.contains("achtzig")) {
			value += 80;
			copy = copy.replaceAll("achtzig", "");
		} else if (copy.contains("neunzig")) {
			value += 90;
			copy = copy.replaceAll("neunzig", "");
		}
		return copy;
	}
	
	private String resolveZehnNeunzehn(String input) {
		String copy = input;
		if (copy.contains("elf")) {
			value += 11;
			copy = copy.replaceAll("elf", "");
		} else if (copy.contains("zwölf")) {
			value += 12;
			copy = copy.replaceAll("zwölf", "");
		} else if (copy.contains("dreizehn")) {
			value += 13;
			copy = copy.replaceAll("dreizehn", "");
		} else if (copy.contains("vierzehn")) {
			value += 14;
			copy = copy.replaceAll("vierzehn", "");
		} else if (copy.contains("fünfzehn")) {
			value += 15;
			copy = copy.replaceAll("fünfzehn", "");
		} else if (copy.contains("sechzehn")) {
			value += 16;
			copy = copy.replaceAll("sechzehn", "");
		} else if (copy.contains("siebzehn")) {
			value += 17;
			copy = copy.replaceAll("siebzehn", "");
		} else if (copy.contains("achtzehn")) {
			value += 18;
			copy = copy.replaceAll("achtzehn", "");
		} else if (copy.contains("neunzehn")) {
			value += 19;
			copy = copy.replaceAll("neunzehn", "");
		} else if (copy.contains("zehn")) {
			value += 10;
			copy = copy.replaceAll("zehn", "");
		}
		return copy;
	}
	
	private String resolveHundert(String input) {
		String copy = input;
		if(copy.contains("einhundert") || copy.contains("ein hundert")) {
			value += 100;
			copy = copy.replaceAll("einhundert", "").replaceAll("ein hundert", "");
		} else if (copy.contains("zweihundert") || copy.contains("zwei hundert")) {
			value += 200;
			copy = copy.replaceAll("zweihundert", "").replaceAll("zwei hundert", "");
		} else if(copy.contains("dreihundert") || copy.contains("drei hundert")) {
			value += 300;
			copy = copy.replaceAll("dreihundert", "").replaceAll("drei hundert", "");
		} else if(copy.contains("vierhundert") || copy.contains("vier hundert")) {
			value += 400;
			copy = copy.replaceAll("vierhundert", "").replaceAll("vier hundert", "");
		} else if(copy.contains("fünfhundert") || copy.contains("fünf hundert")) {
			value += 500;
			copy = copy.replaceAll("fünfhundert", "").replaceAll("fünf hundert", "");
		} else if (copy.contains("sechshundert") || copy.contains("sechs hundert")) {
			value += 600;
			copy = copy.replaceAll("sechshundert", "").replaceAll("sechs hundert", "");
		} else if (copy.contains("siebenhundert") || copy.contains("sieben hundert")) {
			value += 700;
			copy = copy.replaceAll("siebenhundert", "").replaceAll("sieben hundert", "");
		} else if (copy.contains("achthundert") || copy.contains("acht hundert")) {
			value += 800;
			copy = copy.replaceAll("achthundert", "").replaceAll("acht hundert", "");
		} else if (copy.contains("neunhundert") || copy.contains("neun hundert")) {
			value += 900;
			copy = copy.replaceAll("neunhundert", "").replaceAll("neun hundert", "");
		} else if (copy.contains("hundert")) {
			value += 100;
			copy = copy.replaceAll("hundert", "");
		}
		return copy;
	}
	
	
	private String resolveTausend(String input) {
		String copy = input;
		if(copy.contains("eintausend") || copy.contains("ein tausend")) {
			value += 1000;
			copy = copy.replaceAll("eintausend", "").replaceAll("ein tausend", "");
		} else if (copy.contains("zweitausend") || copy.contains("zwei tausend")) {
			value += 2000;
			copy = copy.replaceAll("zweitausend", "").replaceAll("zwei tausend", "");
		} else if(copy.contains("dreitausend") || copy.contains("drei tausend")) {
			value += 3000;
			copy = copy.replaceAll("dreitausend", "").replaceAll("drei tausend", "");
		} else if(copy.contains("viertausend") || copy.contains("vier tausend")) {
			value += 4000;
			copy = copy.replaceAll("viertausend", "").replaceAll("vier tausend", "");
		} else if(copy.contains("fünftausend") || copy.contains("fünf tausend")) {
			value += 5000;
			copy = copy.replaceAll("fünftausend", "").replaceAll("fünf tausend", "");
		} else if (copy.contains("sechstausend") || copy.contains("sechs tausend")) {
			value += 6000;
			copy = copy.replaceAll("sechstausend", "").replaceAll("sechs tausend", "");
		} else if (copy.contains("siebentausend") || copy.contains("sieben tausend")) {
			value += 7000;
			copy = copy.replaceAll("siebentausend", "").replaceAll("sieben tausend", "");
		} else if (copy.contains("achttausend") || copy.contains("acht tausend")) {
			value += 8000;
			copy = copy.replaceAll("achttausend", "").replaceAll("acht tausend", "");
		} else if (copy.contains("neuntausend") || copy.contains("neun tausend")) {
			value += 9000;
			copy = copy.replaceAll("neuntausend", "").replaceAll("neun tausend", "");
		}
		return copy;
	}

}
