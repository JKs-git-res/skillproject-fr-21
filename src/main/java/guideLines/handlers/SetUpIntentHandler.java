package main.java.guideLines.handlers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.model.Address;

public class SetUpIntentHandler implements RequestHandler {

	private Map<String, Slot> slots;

	@Override
	public boolean canHandle(HandlerInput input) {
		Request req = input.getRequestEnvelope().getRequest();
		return input.matches(intentName("SetUpIntent")) && req.getType().equals("IntentRequest");
	}

	private Address getAddress(String AdressString) {
		try {
			return new AddressResolver().getAddress(AdressString);
		} catch (IOException e) {
			return null;
		} catch (StreetNotFoundException e) {
			return null;
		}
	}
	private boolean isConfirmed(Intent intent) {
		
		return intent.getSlots().get("Homeaddress").getConfirmationStatus().equals("CONFIRMED") && 
				intent.getSlots().get("NameHome").getConfirmationStatus().equals("CONFIRMED") &&
				intent.getConfirmationStatus().equals("CONFIRMED");
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		AttributesManager attributesManager = input.getAttributesManager();
		Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();

		String value = (String) persistentAttributes.get("key"); // lesen aus DB

		Request request = input.getRequestEnvelope().getRequest();
		Session session = input.getRequestEnvelope().getSession();
		IntentRequest req = (IntentRequest) request;
		Intent intent = req.getIntent();
		slots = intent.getSlots();

		if (req.getDialogState().equals("STARTED")) {

			return input.getResponseBuilder().addDelegateDirective(intent).build();
			
		} else if (!req.getDialogState().equals("COMPLETED")) {

			String NameHome = slots.get("NameHome") != null ? slots.get("NameHome").getValue() : null;
			String HomeAddressStr = slots.get("Homeaddress") != null ? slots.get("Homeaddress").getValue() : null;
			Address adr = null;
			if (HomeAddressStr != null && adr == null) {
				adr = getAddress(HomeAddressStr);
			}
			if (NameHome != null && adr != null) {
				adr.setName(NameHome);
				try {
					// adresse in json-String umwandeln und in DB speichern
					String adrJSON = new ObjectMapper().writeValueAsString(adr);
					persistentAttributes.put("Homeaddress", adrJSON); // schreiben in DB
					attributesManager.setPersistentAttributes(persistentAttributes);
					attributesManager.savePersistentAttributes(); // nach dem schreiben zum "pushen" in die DB

					// wiederherstellen der Adresse aus DB
					// Address recovered = new ObjectMapper().readValue((String)
					// persistentAttributes.get("Homeaddress"), Address.class);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return input.getResponseBuilder().addDelegateDirective(intent).build();
		} else /* wird nur ausgeführt, wenn dialog sate = COMPLETED */ {

			if (isConfirmed(intent)) {
				return input.getResponseBuilder()
						.withSpeech("Vielen Dank. Die Einrichtung ist erfolgreich abgeschlossen.")
						.withSimpleCard("Einrichtung abgeschlossen!",
								"Vielen Dank. Die Einrichtung ist erfolgreich abgeschlossen.")
						.withShouldEndSession(false).build();
			} else {
				return input.getResponseBuilder().build();
			}

		}
		/*
		 * Address dest1 = getAddress("DestinationAddressOne"); setAddressName(dest1,
		 * "NameDestOne"); Address dest2 = getAddress("DestinationAddressTwo");
		 * setAddressName(dest2, "NameDestTwo"); Address dest3 =
		 * getAddress("DestinationAddressThree"); setAddressName(dest3,
		 * "NameDestThree");
		 */

	}

}
