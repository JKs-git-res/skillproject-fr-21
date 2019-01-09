package guidelines;

public enum StatusAttributes {
	KEY_SETUP_IS_COMPLETE("IsSetUpComplete"),
	KEY_USE_LOCATION("-2"),
	KEY_WANT_SECOND_DEST("-3"),
	KEY_WANT_THIRD_DEST("-4"),
	VALUE_YES("YES"),
	VALUE_NO("NO"),
	KEY_HOMEADDRESS_IS_DISTINCT("isHomeDistinct"),
	KEY_DEST_A_IS_DISTINCT("isDest1Distinct"),
	KEY_DEST_B_IS_DISTINCT("isDest2Distinct"),
	KEY_DEST_C_IS_DISTINCT("isDest3Distinct"),
	KEY_PROCESS("SetUpProcess"),
	VALUE_YES_NO_LOCATION_SET("000"),
	VALUE_HOMEADDRESS_SET("001"),
	VALUE_DESTINATION_A_SET("002"),
    VALUE_YES_NO_WANT_SECOND_DEST_SET("003"),
	VALUE_DESTINATION_B_SET("004"),
    VALUE_YES_NO_WANT_THIRD_DEST_SET("005"),
	VALUE_DESTINATION_C_SET("006");
	
	private final String text;
	
	StatusAttributes (String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

}


