package main.java.guideLines.handlers;

import main.java.guideLines.model.AddressResolver;
import com.amazon.ask.attributes.AttributesManager;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.json.JSONException;

import static com.amazon.ask.request.Predicates.intentName;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import java.util.Collections;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.OutputStrings;
import main.java.guideLines.model.Address;

public class AddressIntentHandler implements RequestHandler {

  public static final String ADDRESS_SLOT = "Address";
  private AddressResolver ar = new AddressResolver();

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

    Slot addressSlot = slots.get(ADDRESS_SLOT);

    String address = addressSlot.getValue();

    Address realAddress = null;
    try {
      realAddress = ar.getAddress(address);
    } catch (IOException e) {
      // Unknown exception maybe no connection
      e.printStackTrace();
    } catch (StreetNotFoundException e) {
      // No stret found ask the user for street
      return input.getResponseBuilder()
              .withSpeech(OutputStrings.NO_STREET_PROMPT)
              .withSimpleCard("Die adresse ist leider ungültig", address)
              .build();
    } catch (JSONException e) {
      return input.getResponseBuilder()
              .withSpeech(OutputStrings.WRONG_ADDRESS_PROMPT)
              .withSimpleCard("Die adresse ist leider ungültig", address)
              .build();
    }
    
    input.getAttributesManager().setSessionAttributes(Collections.singletonMap("adresse", "Alex ist ein spasst"));
    
    //store persistent
            AttributesManager attributesManager = input.getAttributesManager();
            Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
            persistentAttributes.put("adresse", "Alex ist ein spasst");
            persistentAttributes.put("adresseeeee", "Alex ist ein spasst");
            persistentAttributes.put("adresseesssdee", "Alex ist ein spasst");
            attributesManager.setPersistentAttributes(persistentAttributes);
            attributesManager.savePersistentAttributes();

    return input.getResponseBuilder()
            .withSpeech("Deine Adresse ist: Straße: " + realAddress.getStreet() + " und Stadt: " + realAddress.getCity())
            .withSimpleCard("Adresse: ", "Straße: " + realAddress.getStreet() + " und Stadt: " + realAddress.getCity())
            .build();

  }

}
