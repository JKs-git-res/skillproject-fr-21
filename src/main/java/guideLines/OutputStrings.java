package main.java.guideLines;

/**
 * In dieser Klasse befinden sich alle Strings, die von Alexa ausgegeben werden.
 * Somit ist der Code uebersichtlicher.
 * 
 * @author hm-schrein2
 *
 */
public class OutputStrings {

	public static final String WELCOME_EINRICHTUNG = "Willkommen beim GuideLines Einrichtungsassistenten!<break time=\"0.1s\" /> "
			+ "Die Einrichtung dauert circa fünf Minuten. <break time=\"0.05s\" /> Moechtest du die Einrichtung jetzt starten?";
	// public static final String WELCOME_EINRICHTUNG_REPROMPT = "Wo würdest du
	// gerne hinfahren?";
	public static final String WELCOME_EINRICHTUNG_REPROMPT = "Du befindest dich im HauptmenÃ¼ des GuideLines Einrichtungsassistenten. "
			+ "<break time=\"0.05s\" /> Möchtest du die Einrichtung jetzt starten?";
	public static final String WELCOME_EINRICHTUNG_CARD = "Willkommen beim GuideLines Einrichtungsassistenten. "
			+ "Die Einrichtung dauert circa fünf Minuten. Möchtest du die Einrichtung jetzt starten?";
	public static final String EINRICHTUNG_SAY_HOME_ADDRESS_CARD = "Alles klar! Wir beginnen mit der Einrichtung deiner Privatadresse. "
			+ "Wenn du weitere Informationen zu den verschiedenen Schritten benötigst sage bitte: "
			+ " \"Hilfe\". Um zu beginnen nenne mir deine Heimatadresse.";
	public static final String EINRICHTUNG_SAY_HOME_ADDRESS = "Alles klar! <break time=\"0.05s\" /> Wir beginnen mit der Einrichtung deiner Privatadresse. "
			+ "<break time=\"0.05s\" /> Wenn du weitere Informationen zu den verschiedenen Schritten benötigst sage bitte: "
			+ " \"Hilfe\". <break time=\"0.05s\" /> Um zu beginnen nenne mir deine Heimatadresse.";
	public static final String EINRICHTUNG_SAY_HOME_ADDRESS_REPROMPT = "Bitte nenne mir deine Heimatadresse.";
	
	public static final String WRONG_ADDRESS_PROMPT = "Die Adresse ist leider falsch. Kannst du sie mir bitte nochmal sagen?";
	
	public static final String NO_STREET_PROMPT = "Ich brauche von dir die komplette Adresse. Ich brauche mindestens die Stadt und die Straße. Bitte sag mir nochmal deine Adresse.";

}