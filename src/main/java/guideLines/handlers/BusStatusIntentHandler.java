package main.java.guideLines.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class BusStatusIntentHandler implements RequestHandler {

	public static final String DESTINATION_KEY = "DESTINATION";
	public static final String DESTINATION_SLOT = "Destination";
	
	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("NextBusIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		
		return input.getResponseBuilder()
				.withSpeech("Ich kann dir momentan nichts sagen")
				.withSimpleCard("Bus direction", "Ich kann dir momentan nichts sagen").build();
	}

	
}
