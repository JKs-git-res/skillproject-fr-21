package main.java.guideLines.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class BusStatusIntentHandler implements RequestHandler {

	public static final String DESTINATION_KEY = "DESTINATION";
	public static final String DESTINATION_SLOT = "Destination";
	
	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("NextBusIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		
		Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot destinationSlot = slots.get(DESTINATION_SLOT);
		
		return input.getResponseBuilder()
				.withSpeech("Willst du wirklich nach " + destinationSlot.getValue() + " fahren?")
				.withSimpleCard("Bus direction", "Ich kann dir momentan nichts sagen").build();
	}

	
}
