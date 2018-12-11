package main.java.guideLines;

public enum StatusAttributes {
	
	KEY_FLAG_VERIFY_ADDRESS("100"), //the key for the flag that signals that there is an adress that must be verified
                KEY_ADDRESS_TO_VERIFY("200)"),
                  KEY_USE_LOCATION("-2"),
	KEY_WANT_SECOND_DEST("-3"),
	KEY_WANT_THIRD_DEST("-4"),
	VALUE_YES("YES"),
	VALUE_NO("NO"),
	
	KEY_PROCESS("-1"),
	VALUE_YES_NO_LOCATION_SET("000"),
	VALUE_HOMEADDRESS_SET("001"),
	VALUE_NAME_HOME_SET("002"),
	VALUE_DESTINATION_A_SET("003"),
	VALUE_NAME_A_SET("004"),
	VALUE_FORM_OF_TRANSPORT_SET("005"),
                  VALUE_YES_NO_WANT_SECOND_DEST_SET("006"),
	VALUE_DESTINATION_B_SET("007"),
	VALUE_NAME_B_SET("008"),
                  VALUE_YES_NO_WANT_THIRD_DEST_SET("009"),
	VALUE_DESTINATION_C_SET("010"),
	VALUE_NAME_C_SET("011");
	
	private final String text;
	
	StatusAttributes (String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

}


