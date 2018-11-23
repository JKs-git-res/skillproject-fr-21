package main.java.guideLines.handlers;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;


public class AddressIntentHandler implements RequestHandler {
	
	public static final String STREET_SLOT = "Street";
	public static final String NUMBER_SLOT = "Number";
	public static final String PLZ_SLOT = "PLZ";
	public static final String CITY_SLOT = "City";

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("SaveMyAddressIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        Slot streetSlot = slots.get(STREET_SLOT);
        Slot numberSlot = slots.get(NUMBER_SLOT);
        Slot plzSlot = slots.get(PLZ_SLOT);
        Slot citySlot = slots.get(CITY_SLOT);
        
        String street = streetSlot.getValue();
        String city = citySlot.getValue();
        String plz = null;
        String number = null;
        if (plzSlot != null) {
        	plz = plzSlot.getValue();
        }
        if (numberSlot != null) {
        	number = numberSlot.getValue();
        }
        // do magic with Datenbank
        
        
        // say confirmation
        if (plz != null && number != null) {
        	return input.getResponseBuilder()
        			.withSpeech("Deine Adresse Straﬂe: " + street + ", Nummer: " + number + " PLZ: " + plz + ", Stadt: " + city + " " + " wurde gespeichert")
        			.withSimpleCard("Adresse Speichern", "Die Adresse wurde gespeichert!").build();
    
        } else if (plz != null && number == null) {
        	return input.getResponseBuilder()
    				.withSpeech("Deine Adresse Straﬂe: " + street + ", PLZ: " + plz + ", Stadt: " + city + " " + " wurde gespeichert")
    				.withSimpleCard("Adresse Speichern", "Die Adresse wurde gespeichert!").build();
        } else if (plz == null && number != null) {
        	return input.getResponseBuilder()
    				.withSpeech("Deine Adresse Straﬂe: " + street + ", Nummer: " + number + ", Stadt: " + city + " " + " wurde gespeichert")
    				.withSimpleCard("Adresse Speichern", "Die Adresse wurde gespeichert!").build();
        } else {
        	return input.getResponseBuilder()
    				.withSpeech("Deine Adresse Straﬂe: " + street + ", Stadt: " + city + " " + " wurde gespeichert")
    				.withSimpleCard("Adresse Speichern", "Die Adresse wurde gespeichert!").build();
        }
        
	}

}
