package main.java.guideLines.handlers;

import main.java.guideLines.model.AddressResolver;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.model.Address;
import main.java.guideLines.model.FormOfTransport;
import main.java.guideLines.model.Profile;
import main.java.guideLines.model.fullDestinationsException;

public class SetUpIntentHandler implements RequestHandler {

    private Map<String, Slot> slots;

    @Override
    public boolean canHandle(HandlerInput input) {
        Request req = input.getRequestEnvelope().getRequest();
        return input.matches(intentName("SetUpIntent")) && req.getType().equals("IntentRequest");
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
                    .getAddress(json.get("addressLine1") + " "
                            + json.get("addressLine2") + " "
                            + json.get("addressLine3") + " "
                            + json.get("city"));
            return adr;
        } finally {
            is.close();
        }
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        List<String> permissions = new ArrayList<String>();
        permissions.add("read::alexa:device:all:address");
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
        Address adr = null, destA = null, destB = null, destC = null;
        //String value = (String) persistentAttributes.get("key"); // lesen aus DB

        /*
		 * Dies wird alles benÃ¶tigt um den aktuellen Standort bestimmen zu kÃ¶nnen
		
    		
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
                DestinationC_Name_Slot = slots.get("NameC"),
                FormOfTransport_Slot = slots.get("FormOfTransport");

        if (intReq.getDialogState().getValue().equals("STARTED")) {

            return input.getResponseBuilder()
                    //.withAskForPermissionsConsentCard(permissions)
                    .addDelegateDirective(intent)
                    .build();

        } else if (!intReq.getDialogState().getValue().equals("COMPLETED")) {
            if (yesNo_Slot_Loc != null &&
                    yesNo_Slot_Loc.getValue().equals("Ja")) {
                //TODO Not Yet implemented
                /*
                Optional<Object> ctxObj = input.getContext();
		
		Context ctx;
		ctx = (Context) ctxObj.get();
    		SystemState sys = ctx.getSystem();
    		String deviceId = sys.getDevice().getDeviceId();
    		String apiAccessToken = sys.getApiAccessToken();
                //User will aktuellen Standort verwenden
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

            }
            if ((yesNo_Slot_secondDest.getValue() != null 
                    &&yesNo_Slot_secondDest.getValue().equals("Ja"))
                    && DestinationB_Slot.getValue() == null) {
                //falls zweites Ziel eingerichtet werden soll
                return input.getResponseBuilder()
                        .addElicitSlotDirective("DestinationB", intent)
                        .withSpeech("Wie lautet deine zweite Zieladresse?")
                        .build();
            }
            if(yesNo_Slot_secondDest.getValue() != null 
            		&& DestinationB_Slot.getValue() != null 
            		&& DestinationB_Name_Slot.getValue() == null) {
		            	return input.getResponseBuilder()
		                        .addElicitSlotDirective("YesNoSlot_wantThirdDest", intent)
		                        .withSpeech("Willst du noch ein drittes Ziel einrichten?")
		                        .build();
            }
            
            
        
            
            if (yesNo_Slot_thirdDest != null && yesNo_Slot_thirdDest.getValue() != null 
                    && (yesNo_Slot_thirdDest.getValue().equals("Ja")
                     && DestinationC_Slot.getValue() == null
                     && yesNo_Slot_secondDest.getValue().equals("Ja"))) {
                // falls drittes ziel eingerichtet werden soll 
                // (nur wenn zweites Ziel bereits eingerichtet ist)
                return input.getResponseBuilder()
                        .addElicitSlotDirective("DestinationC", intent)
                        .withSpeech("Wie lautet deine dritte Zieladresse?")
                        .build();
            }
            if(yesNo_Slot_thirdDest != null && yesNo_Slot_thirdDest.getValue() != null 
            		&& yesNo_Slot_thirdDest.getValue().equals("Ja")
            		&& DestinationC_Slot.getValue() != null
            		&& DestinationC_Name_Slot.getValue() == null) {
            	return input.getResponseBuilder()
                        .addElicitSlotDirective("NameC", intent)
                        .withSpeech("Wie willst du dein drittes Ziel benennen?")
                        .build();
            }
          

            return input.getResponseBuilder()
                    .addDelegateDirective(intent)
                    .build();

        } else /* wird nur ausgeführt, wenn dialog sate = COMPLETED */ {
            Profile UserProfile = null;
            try {

                Address homeAddress = new AddressResolver()
                        .getAddress(HomeAddress_Slot.getValue());
                homeAddress.setName(NameHome_Slot.getValue());

                Address destinationA = new AddressResolver()
                        .getAddress(DestinationA_Slot.getValue());
                destinationA.setName(DestinationA_Name_Slot.getValue());
                
                UserProfile = new Profile(homeAddress, destinationA);
                
                switch (FormOfTransport_Slot.getValue()) {
                    case ("Bus"):
                        UserProfile.addPreferedFormOfTransport(FormOfTransport.BUS);
                        break;
                    case ("UBahn"):
                        UserProfile.addPreferedFormOfTransport(FormOfTransport.UBAHN);
                        break;
                    case ("SBahn"):
                        UserProfile.addPreferedFormOfTransport(FormOfTransport.SBAHN);
                        break;
                    case ("Tram"):
                        UserProfile.addPreferedFormOfTransport(FormOfTransport.TRAM);
                        break;
                    default:
                        break;

                }
                

                if (yesNo_Slot_secondDest.getValue().equals("Ja")) {

                    Address destinationB = new AddressResolver()
                            .getAddress(DestinationB_Slot.getValue());
                    destinationB.setName(DestinationB_Name_Slot.getValue());
                    UserProfile.addDestinationAddress(destinationB);
                }
                if (yesNo_Slot_thirdDest.getValue().equals("ja")) {

                    Address destinationC = new AddressResolver()
                            .getAddress(DestinationC_Slot.getValue());
                    destinationC.setName(DestinationC_Name_Slot.getValue());
                    UserProfile.addDestinationAddress(destinationC);
                }
            } catch (IOException ex) {
                Logger.getLogger(SetUpIntentHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (StreetNotFoundException ex) {
                Logger.getLogger(SetUpIntentHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (fullDestinationsException ex) {
                Logger.getLogger(SetUpIntentHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                // adresse in json-String umwandeln und in DB speichern
                String ProfileJSON = new ObjectMapper().writeValueAsString(UserProfile);
                persistentAttributes.put("UserProfile", ProfileJSON); // schreiben in DB
                attributesManager.setPersistentAttributes(persistentAttributes);
                attributesManager.savePersistentAttributes(); // nach dem schreiben zum "pushen" in die DB

                // wiederherstellen der Adresse aus DB
                // Address recovered = new ObjectMapper().readValue((String)
                // persistentAttributes.get("UserProfile"), Profile.class);
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return input.getResponseBuilder()
                    .withSpeech("Vielen Dank. Die Einrichtung ist erfolgreich abgeschlossen.")
                    .withSimpleCard("Einrichtung abgeschlossen!",
                            "Vielen Dank. Die Einrichtung ist erfolgreich abgeschlossen.")
                    .withShouldEndSession(false).build();
        }

    }

}
