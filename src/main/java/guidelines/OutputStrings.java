package guidelines;

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
	NO_STREET_PROMPT("Ich brauche von dir die komplette Adresse. Ich brauche mindestens die Stadt und die Straße. Bitte sag mir nochmal deine Adresse."),
	EINRICHTUNG_HELP0("Möchtest du mir die Erlaubnis geben deinen Aktuellen Standort zu ermitteln?"),
	EINRICHTUNG_HELP1("Bitte gebe mir deine Heimatdresse im format: Straße, Hausnummer dann die Stadt an."),
	EINRICHTUNG_HELP2("Bitte geben sie einen Namen für ihre Heimatadresse an um das Aufrufen dieser Heimatadresse zu vereinfachen."),
	EINRICHTUNG_HELP3("Bitte geben sie ihre erste Zieladresse an die sie speichern möchten."),
	EINRICHTUNG_HELP4("Bitte geben sie einen Namen für diese Zieladresse an um das Aufrufen dieser Zieladresse zu vereinfachen"),
	EINRICHTUNG_HELP5("Wählen sie bitte einen der zu verfügung stehenende Öffentlichenverkehrsmittel:Bus,Tram,UBahn,Sbahn."),
	EINRICHTUNG_HELP6("Möchten sie eine zweite Zieladresse einrichten?"),
	EINRICHTUNG_HELP7("Bitte geben sie ihre zweite Zieladresse an die sie speichern möchten."),
	EINRICHTUNG_HELP8("Bitte geben sie einen Namen für diese Zieladresse an um das Aufrufen dieser Zieladresse zu vereinfachen"),
	EINRICHTUNG_HELP9("Möchten sie eine zweite Zieladresse einrichten?"),
	EINRICHTUNG_HELP10("Bitte geben sie ihre dritte Zieladresse an die sie speichern möchten."),
	EINRICHTUNG_HELP11("Bitte geben sie einen Namen für diese Zieladresse an um das Aufrufen dieser Zieladresse zu vereinfachen");
	
	private final String text;
	
	OutputStrings (String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

}
