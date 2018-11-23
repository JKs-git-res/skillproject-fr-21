package main.java.guideLines.handlers;

import main.java.guideLines.OutputStrings;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import static com.amazon.ask.request.Predicates.intentName;

public class YesIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("AMAZON.YesIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {

		return input.getResponseBuilder()
				.withSimpleCard("Heimatadresse angeben.", OutputStrings.EINRICHTUNG_SAY_HOME_ADDRESS_CARD)
				.withSpeech(OutputStrings.EINRICHTUNG_SAY_HOME_ADDRESS)
				.withReprompt(OutputStrings.EINRICHTUNG_SAY_HOME_ADDRESS_REPROMPT)
				.withShouldEndSession(false)
				.build();
	}
}