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
	EINRICHTUNG_YES_NO_LOCATION("Alles klar. Wir beginnen mit der Einrichtung deiner Privatadresse. Kann ich dafür deinen aktuellen Standort verwenden?"),
	EINRICHTUNG_HOMEADDRESS("Wie lautet deine Heimatadresse?"),
                  EINRICHTUNG_NAMEHOME("Wie willst du deine Heimatadresse benennen?"),
                  EINRICHTUNG_DEST_A("Nun sage mir eine Zieladresse. Du kannst bis zu drei Zieladressen speichern."),
                  EINRICHTUNG_NAME_A("Wie willst du die Adresse benennen?"),
                  EINRICHTUNG_INVALID_ADDRESS("Tut mir Leid. Deine angegebene Adresse ist ungültig. Versuche es bitte erneut."),
                  EINRICHTUNG_FORM_OF_TRANSPORT("Okay, und welches öffentliche Verkehrsmittel nutzt du am liebsten, wenn du Zuhause verlässt?"),
                  EINRICHTUNG_YES_NO_WANT_SECOND_DEST("Willst du ein zweites Ziel hinzufügen?"),
                  EINRICHTUNG_DEST_B("Wie lautet deine zweite Zieladresse?"),
                  EINRICHTUNG_NAME_B("Wie willst du diese Adresse benennen?"),
                  EINRICHTUNG_YES_NO_WANT_THIRD_DEST("Willst du ein drittes Ziel hinzufügen?"),
                  EINRICHTUNG_DEST_C("Wie lautet dein drittes Ziel?"),
                  EINRICHTUNG_NAME_C("Wie willst du diese Adresse benennen?"),
                  EINRICHTUNG_END("Vielen Dank. Die Einrichtung ist erfolgreich abgeschlossen."),
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
