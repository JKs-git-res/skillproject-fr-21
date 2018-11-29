package main.java.guideLines.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.interfaces.system.SystemState;
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
	
	private Address getAddressFromLocation(String deviceId) throws IOException, JSONException, StreetNotFoundException {
		Address adr;
		StringBuilder result = new StringBuilder();
        URL url = new URL("http://api.amazonalexa.com/v1/devices/" + deviceId + "/settings/address");
        InputStream is = url.openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          StringBuilder sb = new StringBuilder();
          int cp;
          while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
          }
          String jsonText = sb.toString();
          JSONObject json = new JSONObject(jsonText);
          adr = new AddressResolver()
        		  .getAddress(json.get("addressLine1") +" " 
        				  		+json.get("addressLine2") +" "
        				  		+json.get("addressLine3") +" "
        				  		+json.get("city"));
          return adr;
        } finally {
          is.close();
        }
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		/* slots:
		 * slots.get("Homeaddress")
		 * slots.get("NameHome")
		 * slots.get("DestinationA")
		 * slots.get("DestinationB")
		 * slots.get("DestinationC")
		 * slots.get("NameA")
		 * slots.get("NameB")
		 * slots.get("NameC")
		 */
    	
    	
    	
        
		AttributesManager attributesManager = input.getAttributesManager();
		Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
		Address adr = null, destA = null, destB = null, destC = null;
		String value = (String) persistentAttributes.get("key"); // lesen aus DB
		
		/*
		 * Dies wird alles benötigt um den aktuellen Standort bestimmen zu können
		Optional<Object> ctxObj = input.getContext();
		//System.out.println("input.getContext() -> " + input.getContext());
		
		Context ctx;
		if(ctxObj.isPresent()) {
			ctx = (Context) ctxObj.get();
		}
		
		
    		SystemState sys = ctx.getSystem();
    		String deviceId = sys.getDevice().getDeviceId();
    		String apiAccessToken = sys.getApiAccessToken();
    		
    		*/
		
		Request request = input.getRequestEnvelope().getRequest();
		Session session = input.getRequestEnvelope().getSession();
		IntentRequest intReq = (IntentRequest) request;
		Intent intent = intReq.getIntent();
		slots = intent.getSlots();
		Slot HomeAddress_Slot = slots.get("Homeaddress"), 
				NameHome_Slot = slots.get("NameHome"),
				yesNo_Slot_Loc = slots.get("YesNoSlot_Location"),
				yesNo_Slot_secondDest = slots.get("YesNoSlot_wantSecondDest"),
				yesNo_Slot_thirdDest = slots.get("YesNoSlot_wantThirdDest"),
				DestinationA_Slot = slots.get("DestinationA"),
				DestinationB_Slot = slots.get("DestinationB"),
				DestinationC_Slot = slots.get("DestinationC"),
				DestinationA_Name_Slot = slots.get("NameA"),
				DestinationB_Name_Slot = slots.get("NameB"),
				DestinationC_Name_Slot = slots.get("NameC");
		
		if (intReq.getDialogState().equals("STARTED")) {

			return input.getResponseBuilder().addDelegateDirective(intent).build();
			
		} else if (!intReq.getDialogState().equals("COMPLETED")) {
			
			if(HomeAddress_Slot.getValue() == null) {
				//Einrichtung Homeaddresse
				if(yesNo_Slot_Loc.equals("Ja")) {
					//TODO NOT YET IMPLEMENTED!
					//User will aktuellen Standort verwenden
					/*
					try {
						adr = getAddressFromLocation(deviceId);
						slots.put(adr.getFullAddress(), HomeAddress_Slot);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (StreetNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
		
				} else if(yesNo_Slot_Loc.equals("Nein")) {
					return input.getResponseBuilder()
							.addDelegateDirective(intent)
							.build();
					//User will nicht den aktuellen Standort verwenden ->Alexa übernimmt 
					
				}
				else {}
				return input.getResponseBuilder()
						.addDelegateDirective(intent)
						.build();
				
			}
			if(NameHome_Slot.getValue() != null && HomeAddress_Slot.getValue() != null) {
				//Name der HomeAddress wird gesetzt
				adr.setName(NameHome_Slot.getValue());
				return input.getResponseBuilder()
						.addDelegateDirective(intent)
						.build();
				
				
			}
			if(DestinationA_Slot == null) {
				return input.getResponseBuilder()
						.addDelegateDirective(intent)
						.build();
			}
			
			
			/* try {
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
			*/
			return input.getResponseBuilder()
					.addDelegateDirective(intent)
					.build();
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

	}

}
