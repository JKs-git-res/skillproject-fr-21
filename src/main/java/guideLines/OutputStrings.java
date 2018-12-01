package main.java.guideLines;

/**
 * In dieser Klasse befinden sich alle Strings, die von Alexa ausgegeben werden.
 * Somit ist der Code uebersichtlicher.
 * 
 * @author hm-schrein2
 *
 */
public enum OutputStrings {
	
	WELCOME_EINRICHTUNG("Willkommen beim GuideLines Einrichtungsassistenten!<break time=\"0.1s\" /> Die Einrichtung dauert circa fünf Minuten. <break time=\"0.05s\" /> Möchtest du die Einrichtung jetzt starten? Dann sage 'Einrichtung starten'."),
	WELCOME_EINRICHTUNG_REPROMPT("Du befindest dich im Hauptmenü des GuideLines Einrichtungsassistenten. <break time=\"0.05s\" /> Möchtest du die Einrichtung jetzt starten? Dann sage 'Einrichtung starten'."),
	WELCOME_EINRICHTUNG_CARD("Willkommen beim GuideLines Einrichtungsassistenten. Die Einrichtung dauert circa fünf Minuten. Möchtest du die Einrichtung jetzt starten?"),
	EINRICHTUNG_SAY_HOME_ADDRESS_CARD("Alles klar! Wir beginnen mit der Einrichtung deiner Privatadresse. Wenn du weitere Informationen zu den verschiedenen Schritten benötigst sage bitte: \"Hilfe\". Um zu beginnen nenne mir deine Heimatadresse."),
	EINRICHTUNG_SAY_HOME_ADDRESS("Alles klar! <break time=\"0.05s\" /> Wir beginnen mit der Einrichtung deiner Privatadresse. <break time=\"0.05s\" /> Wenn du weitere Informationen zu den verschiedenen Schritten benötigst sage bitte:  \"Hilfe\". <break time=\"0.05s\" /> Um zu beginnen nenne mir deine Heimatadresse."),
	EINRICHTUNG_SAY_HOME_ADDRESS_REPROMPT("Bitte nenne mir deine Heimatadresse."),
	WRONG_ADDRESS_PROMPT("Die Adresse ist leider falsch. Kannst du sie mir bitte nochmal sagen?"),
	NO_STREET_PROMPT("Ich brauche von dir die komplette Adresse. Ich brauche mindestens die Stadt und die Straße. Bitte sag mir nochmal deine Adresse.");
	
	private final String text;
	
	OutputStrings (String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

}
