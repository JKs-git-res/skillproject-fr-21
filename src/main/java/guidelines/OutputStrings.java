package guidelines;

/**
 * In dieser Klasse befinden sich alle Strings, die von Alexa ausgegeben werden.
 * Somit ist der Code uebersichtlicher.
 * 
 * @author hm-schrein2
 *
 */
public enum OutputStrings {
	SPEECH_BREAK_SHORT("<break time=\"0.05s\" />"),
	SPEECH_BREAK_LONG("<break time=\"0.1s\" />"),

	WELCOME_BEREITS_EINGERICHTET_SPEECH("<break time=\"0.05s\" />. Wo möchtest du heute hinfahren?"),
	WELCOME_BEREITS_EINGERICHTET_CARD(". Wo möchtest du heute hinfahren?"),
	WELCOME_BEREITS_EINGERICHTET_REPROMPT("Wie lautet dein heutiges Ziel?"),

	WELCOME_EINRICHTUNG_SPEECH("Willkommen beim GuideLines Einrichtungsassistenten!<break time=\"0.1s\" /> Die Einrichtung dauert circa fünf Minuten. <break time=\"0.05s\" /> Möchtest du die Einrichtung jetzt starten? Dann sage 'Einrichtung starten'."),
	WELCOME_EINRICHTUNG_REPROMPT("Du befindest dich im Hauptmenü des GuideLines Einrichtungsassistenten. <break time=\"0.05s\" /> Möchtest du die Einrichtung jetzt starten? Dann sage 'Einrichtung starten'."),
	WELCOME_EINRICHTUNG_CARD("Willkommen beim GuideLines Einrichtungsassistenten. Die Einrichtung dauert circa fünf Minuten. Möchtest du die Einrichtung jetzt starten?"),

	EINRICHTUNG_YES_NO_LOCATION_SPEECH("Alles klar. Wir beginnen mit der Einrichtung deiner Privatadresse.<break time=\"0.05s\" /> Wenn du weitere Informationen zu den verschiedenen Schritten benötigst sage bitte: Hilfe.  " +
			"<break time=\"0.05s\" /> Wäre es für dich in Ordnung deine aktuelle Position zu verwenden?"),
	EINRICHTUNG_YES_NO_LOCATION_CARD("Alles klar. Wir beginnen mit der Einrichtung deiner Privatadresse. Wenn du weitere Informationen zu den verschiedenen Schritten benötigst sage bitte: Hilfe.  " +
			"Wäre es für dich in Ordnung deine aktuelle Position zu verwenden?"),
	EINRICHTUNG_YES_NO_LOCATION_REPROMPT("Kann ich den aktuellen Standort verwenden?"),

	EINRICHTUNG_INVALID_ADDRESS_SPEECH("Tut mir Leid. <break time=\"0.05s\" /> Deine angegebene Adresse ist ungültig. Versuche es bitte erneut."),
	EINRICHTUNG_INVALID_ADDRESS_CARD("Tut mir Leid. Deine angegebene Adresse ist ungültig. Versuche es bitte erneut."),
	EINRICHTUNG_ADDRESS_CONFIRMATION_DENIED_SPEECH("Dann wiederhole bitte die Adresse. <break time=\"0.05s\" /> Versuche dabei laut und deutlich zu sprechen."),
	EINRICHTUNG_ADDRESS_CONFIRMATION_DENIED_CARD("Dann wiederhole bitte die Adresse. Versuche dabei laut und deutlich zu sprechen."),
	EINRICHTUNG_ADDRESS_LOCATION_DENIED_SPEECH("Tut mir Leid.<break time=\"0.05s\" /> Amazon Alexa hat nicht die erforerlichen Berechtigungen um auf deine Adresse zuzugreifen."),
	EINRICHTUNG_ADDRESS_LOCATION_DENIED_CARD("Tut mir Leid. Amazon Alexa hat nicht die erforderlichen Berechtigungen um auf deine Adresse zuzugreifen."),


	EINRICHTUNG_STATION_FOUND("Alles klar. Die nächste Haltestelle dieser Adresse lautet: "),
	EINRICHTUNG_NO_STATION_FOUND("In der Nähe dieser Adresse wurde leider keine Haltestelle gefunden."),

	EINRICHTUNG_HOMEADDRESS_SPEECH("Ok.<break time=\"0.05s\" /> Dann nenne mir bitte deine Privatadresse."),
	EINRICHTUNG_HOMEADDRESS_CARD("Ok.  Dann nenne mir bitte deine Privatadresse. "),
	EINRICHTUNG_HOMEADDRESS_REPROMPT("Bitte sage mir deine Privatadresse."),

    EINRICHTUNG_NAMEHOME_SPEECH("Wie möchtest du deine Heimatadresse benennen?"),
	EINRICHTUNG_NAMEHOME_CARD("Wie möchtest du deine Heimatadresse benennen?"),
	EINRICHTUNG_NAMEHOME_REPROMPT("Bitte sage mir einen Namen für deine Heimatadresse"),

	EINRICHTUNG_DEST_A_SPEECH_1("Super! Nun habe ich <break time=\"0.05s\" /> "),
	EINRICHTUNG_DEST_A_SPEECH_2(" <break time=\"0.05s\" /> eingerichtet <break time=\"0.05s\" />. Als nächstes kümmern wir uns um deine Ziele! " +
			"<break time=\"0.05s\" /> Wie lautet die Adresse deines ersten Ziels?"),
	EINRICHTUNG_DEST_A_CARD_1("Super! Nun habe ich "),
	EINRICHTUNG_DEST_A_CARD_2(" eingerichtet. Als nächstes kümmern wir uns um deine Ziele! " +
			" Wie lautet die Adresse deines ersten Ziels?"),
	EINRICHTUNG_DEST_A_REPROMPT("Bitte nenne mir deine erste Zieladresse."),

	EINRICHTUNG_NAME_A_SPEECH("Ich habe die Adresse gespeichert. Wie möchtest du dein erstes Ziel benennen?"),
	EINRICHTUNG_NAME_A_CARD("Ich habe die Adresse gespeichert. Wie möchtest du dein erstes Ziel benennen?"),
	EINRICHTUNG_NAME_A_REPROMPT("Bitte sage mir einen Namen für deine erste Zieladresse."),

	EINRICHTUNG_YES_NO_WANT_SECOND_DEST_SPEECH("Willst du ein zweites Ziel hinzufügen?"),
	EINRICHTUNG_YES_NO_WANT_SECOND_DEST_CARD("Willst du ein zweites Ziel hinzufügen?"),
	EINRICHTUNG_YES_NO_WANT_SECOND_DEST_REPROMPT("Willst du ein zweites Ziel hinzufügen?"),

	EINRICHTUNG_DEST_B_SPEECH("Wie lautet die Adresse deines zweiten Ziels?"),
EINRICHTUNG_DEST_B_CARD("Wie lautet die Adresse deines zweiten Ziels?"),
	EINRICHTUNG_DEST_B_REPROMPT("Wie lautet deine zweite Zieladresse?"),

	EINRICHTUNG_NAME_B_SPEECH("Ich habe die Adresse gespeichert. <break time=\"0.05s\" />Wie möchtest du dein zweites Ziel benennen?"),
	EINRICHTUNG_NAME_B_CARD("Ich habe die Adresse gespeichert. Wie möchtest du dein zweites Ziel benennen?"),
	EINRICHTUNG_NAME_B_REPROMPT("Wie willst du diese Adresse benennen?"),

    EINRICHTUNG_YES_NO_WANT_THIRD_DEST_SPEECH("Alles klar! Ich habe die Einrichtung deines Ziels abgeschlossen. Möchtest du ein letztes Ziel hinzufügen?"),
	EINRICHTUNG_YES_NO_WANT_THIRD_DEST_CARD("Alles klar! Ich habe die Einrichtung deines Ziels abgeschlossen. Möchtest du ein letztes Ziel hinzufügen?"),
	EINRICHTUNG_YES_NO_WANT_THIRD_DEST_REPROMPT("Willst du ein letztes Ziel hinzufügen?"),

	EINRICHTUNG_DEST_C_SPEECH("Wie lautet die Adresse deines dritten Ziels?"),
	EINRICHTUNG_DEST_C_CARD("Wie lautet die Adresse deines dritten Ziels?"),
	EINRICHTUNG_DEST_C_REPROMPT("Wie lautet dein drittes Ziel?"),

	EINRICHTUNG_NAME_C_SPEECH("Gut! Ich habe die Adresse gespeichert. <break time=\"0.05s\" />Wie möchtest du dein drittes Ziel benennen?"),
	EINRICHTUNG_NAME_C_CARD("Gut! Ich habe die Adresse gespeichert. Wie möchtest du dein drittes Ziel benennen?"),
	EINRICHTUNG_NAME_C_REPROMPT("Wie willst du dein letztes Ziel benennen?"),

	EINRICHTUNG_END_SPEECH("Vielen Dank für deine Zeit, <break time=\"0.05s\" /> " +
			"ich habe die Einrichtung abgeschlossen. <break time=\"0.1s\" />" +
			"GuideLines kann jetzt verwendet werden. <break time=\"0.1s\" /> " +
			"Um die Anwendung zu starten sage: <break time=\"0.05s\" /> Navigation."),
	EINRICHTUNG_END_CARD("Vielen Dank für deine Zeit, " +
			"ich habe die Einrichtung abgeschlossen." +
			"GuideLines kann jetzt verwendet werden. " +
			"Um die Anwendung zu starten sage: Navigation."),
	EINRICHTUNG_END_REPROMPT("Die Einrichtung ist abgeschlossen. Sage <break time=\"0.05s\" /> 'Navigation' um die Anwendung zu starten."),

	EINRICHTUNG_HELP_START("Alles klar, der Einrichtungsassistent besteht aus drei grundlegenden Schritten. " +
			"Du startest mit der Einrichtung deiner Heimadresse. " +
			"Anschließend kannst du bis zu 3 Ziele definieren und diese benennen. " +
			"GuideLines kann dir so schnell helfen, deine schnellste und pünktlichste Verbindung zu finden. " +
			"Wenn du noch ein bisschen Zeit hast kann ich dir gerne Informationen zu den weiteren Schritten geben. " +
			"Wie sieht's aus, hast du noch ein wenig Zeit?"),
	EINRICHTUNG_HELP_HOME_OR_DEST("Möchtest du Information zur Heimadresse oder zu den Zieladressen?"),
	EINRICHTUNG_HELP_ENDE_2("Alles klar. Möchtest du mit der EInrichtung beginnen? Dann sage 'Einrichtung starten'."),
	EINRICHTUNG_HELP_ENDE("GuideLines ist dein persönlicher Assistent für deinen Weg zu deinen Zielen. " +
			"Ich kann dir mit wenigen Angaben deine kürzeste Strecke zum Ziel in Echtzeit berechnen. " +
			"Nach der Einrichtung wird GuideLines jederzeit Einsatzbereit sein. Möchtest du mit der Einrichtung beginnen?"),
	EINRICHTUNG_HELP_HOME("Bitte gebe mir deine Heimatdresse im format Straße, Hausnummer, Stadt an. Deine Heimadresse benötigt GuideLines, " +
			"um einen Startpunkt im System festzulegen. Neben deiner Adresse,<break time=\"0.05s\" /> deiner Haltestelle und einem bevorzugten Verkehrsmittel kannst du " +
			"deinen Startpunkt selbst benennen. Vorschlagen würde ich dir hier die Benennung <break time=\"0.05s\" /> Zuhause. Willst du weitere Infos zu den Zieladressen?"),
	EINRICHTUNG_HELP_DESTINATION("Deine Zieladressen stellen häufig von dir im Alltag verwendete Ziele dar. " +
			"Viele Leute haben hier Ziele wie ihren Arbeitsplatz, Universitätscampus oder ihre Schule hinterlegt." +
			" <break time=\"0.05s\" /> Auch hier brauche ich die Adresse und einen Namen für dein Ziel. Ich kann dir noch weitere Informationen zur Anwendung geben, möchtest du das ?"),
	PLANMYTRIP_DESTINATION_NOT_FOUND_SPEECH("Dieses Ziel ist nicht gepeichert. <break time=\"0.05s\" Bitte versuche es erneut."),
	PLANMYTRIP_DESTINATION_NOT_FOUND_CARD("Dieses Ziel ist nicht gepeichert. Bitte versuche es erneut.");

	private final String text;
	OutputStrings (String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

}
