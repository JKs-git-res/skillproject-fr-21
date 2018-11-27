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
import com.amazon.ask.model.Slot;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.OutputStrings;
import main.java.guideLines.model.Address;
import main.java.guideLines.model.Profile;

public class SetUpIntentHandler implements RequestHandler {

	private Map<String, Slot> slots;
	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("SetUpIntent"));
	}
	
	private Address getAddress(String SlotName) {
		try {
			return new AddressResolver()
					.getAddress(
							slots.get(SlotName).getValue());
		} catch (IOException e) {
			return null;
		} catch (StreetNotFoundException e) {
			return null;
		}
	}
	private void setAddressName(Address adr, String Slotname) {
		adr.setName(slots.get(Slotname).getValue());
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		 AttributesManager attributesManager = input.getAttributesManager();
         Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
         persistentAttributes.put("key", "value"); //schreiben in DB
         
         attributesManager.setPersistentAttributes(persistentAttributes);
         attributesManager.savePersistentAttributes(); //nach dem schreiben zum "pushen" in die DB
         
         String value = (String) persistentAttributes.get("key"); //lesen aus DB
         
         
         
		IntentRequest req = (IntentRequest) input.getRequestEnvelope().getRequest();
		Intent intent = req.getIntent();
		slots = intent.getSlots();
		Address homeAddress = getAddress("Homeaddress");
		setAddressName(homeAddress, "NameHome");
		Address dest1 = getAddress("DestinationAddressOne");
		setAddressName(dest1, "NameDestOne");
		Address dest2 = getAddress("DestinationAddressTwo");
		setAddressName(dest2, "NameDestTwo");
		Address dest3 = getAddress("DestinationAddressThree");
		setAddressName(dest3, "NameDestThree");
		return null;
	}

}
