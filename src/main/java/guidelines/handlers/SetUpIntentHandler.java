package guidelines.handlers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.amazon.ask.model.*;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.fasterxml.jackson.databind.JsonMappingException;
import guidelines.exceptions.StreetNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import guidelines.model.*;
import guidelines.OutputStrings;
import guidelines.StatusAttributes;

public class SetUpIntentHandler implements RequestHandler {
    
    private Map<String, Slot> slots;
    private AttributesManager attributesManager;
    private Map<String, Object> persistentAttributes;
    private Address homeAddress;
    private Address destinationA;
    private Address destinationB;
    private Address destinationC;
    private Profile userProfile;
    private Slot HomeAddress_Slot,NameHome_Slot,yesNo_Slot_Loc,yesNo_Slot_secondDest,
            yesNo_Slot_thirdDest,DestinationA_Slot,DestinationB_Slot,DestinationC_Slot,
            DestinationA_Name_Slot,DestinationB_Name_Slot,DestinationC_Name_Slot;

    public Profile getUserProfile(){
        return userProfile;
    }



    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("SetUpIntent")) &&
                input.getAttributesManager().getSessionAttributes().get(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString()).equals("false");
    }

    private Optional<Response> confirmAdress(HandlerInput input, Address toConfirm, String slotName){
        Intent intent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        String speech = "Deine Adresse lautet: "
                +toConfirm.AddressSpeech()
                +OutputStrings.SPEECH_BREAK_LONG.toString()
                +". Ist das richtig?";
        return input.getResponseBuilder()
                .addConfirmSlotDirective(slotName, intent)
                .withSpeech(speech)
                .withShouldEndSession(false)
                .withSimpleCard("Adresse bestätigen", speech)
                .build();

    }


    private Optional<Response> invalidAddress(HandlerInput input, String slot) {
        return input.getResponseBuilder()
                .withShouldEndSession(false)
                .addElicitSlotDirective(slot, ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent())
                .withSpeech(OutputStrings.EINRICHTUNG_INVALID_ADDRESS.toString())
                .withSimpleCard("Adresse ungültig", OutputStrings.EINRICHTUNG_INVALID_ADDRESS.toString())
                .build();
    }

    public void saveProfileToDataBase(Profile profile) throws JsonProcessingException {
        persistentAttributes.put("User Name", "Max Mustermann"); //TODO hier soll noch der Name des Benutzers
        persistentAttributes.put("Homeaddress", new ObjectMapper().writeValueAsString(profile.getHomeAddress())); // schreiben in DB
        persistentAttributes.put("Destination A", new ObjectMapper().writeValueAsString(profile.getDestination(0)));
        if(profile.getDestination(1) != null)
            persistentAttributes.put("Destination B", new ObjectMapper().writeValueAsString(profile.getDestination(1)));
        if(profile.getDestination(2) != null)
            persistentAttributes.put("Destination C", new ObjectMapper().writeValueAsString(profile.getDestination(2)));
        attributesManager.setPersistentAttributes(persistentAttributes);
        attributesManager.savePersistentAttributes(); // nach dem schreiben zum "pushen" in die DB

    }

    private List<Address> resolveAddress(Slot slot) {
        try {
        	/* dies wird später noch implementiert, falls die addresse nicht eindeutig ist
        	 * boolean isDistinct = addresses.size() == 1;
            switch (slot.getName()) {
                case ("Homeaddress"):
                    sessionAttributes.put(StatusAttributes.KEY_HOMEADDRESS_IS_DISTINCT.toString(), isDistinct);
                    break;
                case ("DestinationA"):
                    sessionAttributes.put(StatusAttributes.KEY_DEST_A_IS_DISTINCT.toString(), isDistinct);
                    break;
                case ("DestinationB"):
                    sessionAttributes.put(StatusAttributes.KEY_DEST_B_IS_DISTINCT.toString(), isDistinct);
                    break;
                case ("DestinationC"):
                    sessionAttributes.put(StatusAttributes.KEY_DEST_C_IS_DISTINCT.toString(), isDistinct);
                    break;
                default:
                    break;
            }
        	 */
        	 HouseNumberConverter hnc = new HouseNumberConverter();
             ArrayList<Address> addresses = new AddressResolver().getAddressList(hnc.getAdressHereAPIFormatted(slot.getValue()));
             if(addresses.size() > 0) {
                 return addresses;
             } else
            	 	System.out.println("It was 0. Here is the input: " + hnc.getAdressHereAPIFormatted(slot.getValue()) + " Not translated: " + slot.getValue());
            	 	return null;
        } catch (IOException ex) {
        	System.out.println("IO Exception");
            return null;
        } catch (JSONException ex) {
        	System.out.println("JSON Exception");
            return null;
        }
    }

    private void storeAdressToDB(Address addressToStore, String key){
        try {
            persistentAttributes.put(key, new ObjectMapper().writeValueAsString(addressToStore)); // schreiben in DB
            attributesManager.setPersistentAttributes(persistentAttributes);
            attributesManager.savePersistentAttributes(); // nach dem schreiben zum "pushen" in die DB
        } catch (JsonMappingException ex){
            Logger.getLogger(SetUpIntentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex){}
    }
    
    
    
    
    private Optional<Response> setAdress(Slot addressSlot,HandlerInput input){
        String slotName = addressSlot.getName();
        String slotNameName = "";
        String addressName = "";
        String speech = "";
        String newStatusAttributesValue = "";
        String speechName = "";
        switch(slotName){
            case "Homeaddress":
                addressName = "Heimatadresse";
                speech = OutputStrings.EINRICHTUNG_HOMEADDRESS.toString();
                slotNameName = "NameHome";
                speechName = OutputStrings.EINRICHTUNG_NAMEHOME.toString();
                newStatusAttributesValue = StatusAttributes.VALUE_HOMEADDRESS_SET.toString();
                break;
            case "DestinationA":
                addressName = "Erste Zieladresse";
                speech = OutputStrings.EINRICHTUNG_DEST_A.toString();
                slotNameName = "NameA";
                speechName = OutputStrings.EINRICHTUNG_NAME_A.toString();
                newStatusAttributesValue = StatusAttributes.VALUE_DESTINATION_A_SET.toString();
                break;
            case "DestinationB":
                addressName = "Zweite Zieladresse";
                speech = OutputStrings.EINRICHTUNG_DEST_B.toString();
                slotNameName = "NameB";
                speechName = OutputStrings.EINRICHTUNG_NAME_B.toString();
                newStatusAttributesValue = StatusAttributes.VALUE_DESTINATION_B_SET.toString();
                break;
            case "DestinationC":
                addressName = "Dritte Zieladresse";
                speech = OutputStrings.EINRICHTUNG_DEST_C.toString();
                slotNameName = "NameC";
                speechName = OutputStrings.EINRICHTUNG_NAME_C.toString();
                newStatusAttributesValue = StatusAttributes.VALUE_DESTINATION_C_SET.toString();
                break;
            default:
                break;
        }
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        Intent intent = ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent();
        if(addressSlot.getValue() == null) {
            return input.getResponseBuilder()
                    .addElicitSlotDirective(slotName, intent)
                    .withShouldEndSession(false)
                    .withSpeech(speech)
                    .withSimpleCard(addressName + " angeben", speech)
                    .build();
        }
        else if(addressSlot.getConfirmationStatus().getValue().equals("DENIED")){
            return input.getResponseBuilder()
                    .addElicitSlotDirective(slotName, intent)
                    .withShouldEndSession(false)
                    .withSpeech("Dann wiederhole bitte die Adresse."
                            +OutputStrings.SPEECH_BREAK_SHORT.toString()
                            + " Versuche dabei laut und deutlich zu sprechen.")
                    .withSimpleCard(addressName + " angeben", speech)
                    .build();
            }
        else if(addressSlot.getConfirmationStatus().getValue().equals("NONE")){
            List<Address> adrList = resolveAddress(addressSlot);
            if (adrList == null) {
                return invalidAddress(input, slotName);
            } else {
                    storeAdressToSession(adrList.get(0), slotName);
                    return confirmAdress(input, adrList.get(0),slotName);

            }


        } else{
            sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), newStatusAttributesValue);
            String stationName = "";
            switch (slotName){
                case "Homeaddress":
                    stationName = homeAddress.getNearestStation() != null? homeAddress.getNearestStation().getName():OutputStrings.NO_STATION_FOUND.toString();
                    break;
                case "DestinationA":
                    stationName = destinationA.getNearestStation() != null? destinationA.getNearestStation().getName():OutputStrings.NO_STATION_FOUND.toString();
                    break;
                case "DestinationB":
                    stationName = destinationB.getNearestStation() != null? destinationB.getNearestStation().getName():OutputStrings.NO_STATION_FOUND.toString();
                    break;
                case "DestinationC":
                    stationName = destinationC.getNearestStation() != null? destinationC.getNearestStation().getName():OutputStrings.NO_STATION_FOUND.toString();
                    break;
            }
            String finalSpeech = "Alles klar. Die nächste Haltestelle dieser Adresse lautet: " + stationName
                    + OutputStrings.SPEECH_BREAK_SHORT.toString() +" "
                    + speechName;
            return input.getResponseBuilder()
                    .withShouldEndSession(false)
                    .addElicitSlotDirective(slotNameName, intent)
                    .withSpeech(finalSpeech)
                    .withSimpleCard(addressName +" benennen", finalSpeech)
                    .build();
        }
    }

    private void storeAdressToSession(Address addressToStore, String slotName) {
        try {
            attributesManager.getSessionAttributes().put(slotName, new ObjectMapper().writeValueAsString(addressToStore));
        } catch (JsonMappingException ex){
            Logger.getLogger(SetUpIntentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex){}
    }

    private Optional<Response> setUpComplete(HandlerInput input) {
        input.getAttributesManager().getSessionAttributes().put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "true");
        return input.getResponseBuilder()
                .withSpeech(OutputStrings.EINRICHTUNG_END.toString())
                .withSimpleCard("Einrichtung abgeschlossen", OutputStrings.EINRICHTUNG_END.toString())
                .withShouldEndSession(false)
                .build();
    }

    private Address getAddressFromLocation(String deviceId, String apiEndpoint,String apiAccessToken) throws IOException, JSONException, StreetNotFoundException {
        Address adr;
        StringBuilder result = new StringBuilder();
        String urlString = apiEndpoint +"/v1/devices/" + deviceId + "/settings/address";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer "+apiAccessToken);
        connection.setRequestProperty("GET", urlString);
        connection.setRequestProperty("Host", "api.amazonalexa.com");
        StringBuffer response;
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String inputLine;
        response = new StringBuffer();
        while((inputLine = in.readLine()) != null)
          response.append(inputLine);
      }
        String jsonText = response.toString();
        System.out.println("jsonText: "+jsonText);
        JSONObject json = new JSONObject(jsonText);
        adr = new AddressResolver()
                .getAddressList(json.get("addressLine1") + " "
                        + json.get("addressLine2") + " "
                        + json.get("addressLine3") + " "
                        + json.get("city")).get(0);
        return adr;
    }


    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        List<String> permissions = new ArrayList<>();
        permissions.add("read::alexa:device:all:address");
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intReq = (IntentRequest) request;
        Intent intent = intReq.getIntent();
        slots = intent.getSlots();
        HomeAddress_Slot = slots.get("Homeaddress");
        NameHome_Slot = slots.get("NameHome");
        yesNo_Slot_Loc = slots.get("YesNoSlot_Location");
        yesNo_Slot_secondDest = slots.get("YesNoSlot_wantSecondDest");
        yesNo_Slot_thirdDest = slots.get("YesNoSlot_wantThirdDest");
        DestinationA_Slot = slots.get("DestinationA");
        DestinationB_Slot = slots.get("DestinationB");
        DestinationC_Slot = slots.get("DestinationC");
        DestinationA_Name_Slot = slots.get("NameA");
        DestinationB_Name_Slot = slots.get("NameB");
        DestinationC_Name_Slot = slots.get("NameC");
        attributesManager = input.getAttributesManager();
        persistentAttributes = attributesManager.getPersistentAttributes();
        try {
            String process = (String)sessionAttributes.get(StatusAttributes.KEY_PROCESS.toString());
            if(process != null){
                int proc = Integer.parseInt(process);
                if(proc >= 0 && sessionAttributes.get("Homeaddress") != null) {
                    homeAddress = new ObjectMapper().readValue((String) sessionAttributes.get("Homeaddress"), Address.class);
                }
                if(proc >= 1 && sessionAttributes.get("DestinationA") != null){
                    destinationA = new ObjectMapper().readValue((String) sessionAttributes.get("DestinationA"), Address.class);
                }
                if(proc >= 3 && sessionAttributes.get("DestinationB") != null)
                    destinationB = new ObjectMapper().readValue((String) sessionAttributes.get("DestinationB"), Address.class);
                if(proc >= 5 && sessionAttributes.get("DestinationC") != null)
                    destinationC = new ObjectMapper().readValue((String) sessionAttributes.get("DestinationC"), Address.class);
            }

        } catch (JsonMappingException ex){
            Logger.getLogger(SetUpIntentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex){}


        if (intReq.getDialogState().getValue().equals("STARTED")) {

            //Anfang des Dialogs. Als erstes wird geprüft, ob der aktuelle Standort verwendet werden soll
            sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_YES_NO_LOCATION_SET.toString());
            return input.getResponseBuilder()
                   // .withAskForPermissionsConsentCard(permissions)
                    .addElicitSlotDirective("YesNoSlot_Location", intent)
                    .withSpeech(OutputStrings.EINRICHTUNG_YES_NO_LOCATION.toString())
                    .withShouldEndSession(false)
                    .withSimpleCard("Aktuellen Standort verwenden?", OutputStrings.EINRICHTUNG_YES_NO_LOCATION.toString())
                    .build();

        } else {

            switch ((String) sessionAttributes.get(StatusAttributes.KEY_PROCESS.toString())) {
                case "000": //hier wird nach der Heimatadresse gefragt
                    if (yesNo_Slot_Loc.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName().equals("Nein")) {
                        return setAdress(HomeAddress_Slot, input);
                    }
                    else {
                        Optional<Object> ctxObj = input.getContext();
                        Context ctx;
                        try{
                          if(ctxObj.isPresent()){
                            ctx = (Context) ctxObj.get();
                          } else {
                            throw new NoSuchElementException();
                          }
                          SystemState sys = ctx.getSystem();
                            String deviceId = sys.getDevice().getDeviceId();
                            String apiAccessToken = sys.getApiAccessToken();
                            String apiEndpoint = sys.getApiEndpoint();
                            
                            try {
                                homeAddress = getAddressFromLocation(deviceId,apiEndpoint,apiAccessToken);
                                Station station = homeAddress.getNearestStation();
                                sessionAttributes.put("Homeaddress", new ObjectMapper().writeValueAsString(homeAddress));
                                String speech = "Deine Adresse lautet: "
                                        + homeAddress.getStreet()+ " "
                                        +homeAddress.gethouseNumber()+" "
                                        +homeAddress.getCity()+ OutputStrings.SPEECH_BREAK_LONG.toString()
                                        +" Die nächste Haltestelle ist: "
                                        +station.getName() + OutputStrings.SPEECH_BREAK_LONG.toString()
                                        + OutputStrings.EINRICHTUNG_NAMEHOME.toString();
                                return input.getResponseBuilder()
                                        .withSpeech(speech)
                                        .withSimpleCard("Adresse gefunden", speech)
                                        .withShouldEndSession(false)
                                        .addElicitSlotDirective("NameHome",intent)
                                        .build();
                            } catch (JSONException | IOException | StreetNotFoundException e) {
                                // TODO Auto-generated catch block
                                //e.printStackTrace(); Das stellt ein sicherheitsrisiko dar
                            }
                          // TODO Auto-generated catch block
                          // TODO Auto-generated catch block
                          


                        } catch(NoSuchElementException ex){
                            return input
                                    .getResponseBuilder()
                                    .withAskForPermissionsConsentCard(permissions)
                                    .withShouldEndSession(false)
                                    .withSpeech("Bitte erteile Amazon-Alexa die dafür benötigten Berechtigungen in deinen Alexa-Einstellungen für diesen Skill")
                                    .build();
                        }






            }


            case "001": //hier wird nach der ersten Zieladresse gefragt
                homeAddress.setName(NameHome_Slot.getValue());
                storeAdressToDB(homeAddress, "Homeaddress");
                return setAdress(DestinationA_Slot, input);

            case "002": // hier wird gefragt, ob der User ein zweites Ziel hinzufügen will
                destinationA.setName(DestinationA_Name_Slot.getValue());
                storeAdressToDB(destinationA, "DestinationA");
                sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_YES_NO_WANT_SECOND_DEST_SET.toString());
                return input.getResponseBuilder()
                        .withShouldEndSession(false)
                        .addElicitSlotDirective("YesNoSlot_wantSecondDest", intent)
                        .withSpeech(OutputStrings.EINRICHTUNG_YES_NO_WANT_SECOND_DEST.toString())
                        .withSimpleCard("Zweites Ziel hinzufügen?", OutputStrings.EINRICHTUNG_YES_NO_WANT_SECOND_DEST.toString())
                        .build();

            case "003": // hier wird nach der zweiten Zieladresse gefragt
                if (yesNo_Slot_secondDest.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName().equals("Ja")) {
                    return setAdress(DestinationB_Slot, input);
                } else {
                    return setUpComplete(input);
                }

            case "004": // hier wird gefragt, ob der User ein drittes Ziel hinzufügen will
                destinationB.setName(DestinationB_Name_Slot.getValue());

                storeAdressToDB(destinationB, "DestinationB");
                sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_YES_NO_WANT_THIRD_DEST_SET.toString());
                return input.getResponseBuilder()
                        .withShouldEndSession(false)
                        .addElicitSlotDirective("YesNoSlot_wantThirdDest", intent)
                        .withSpeech(OutputStrings.EINRICHTUNG_YES_NO_WANT_THIRD_DEST.toString())
                        .withSimpleCard("Drittes Ziel hinzufügen?", OutputStrings.EINRICHTUNG_YES_NO_WANT_THIRD_DEST.toString())
                        .build();

            case "005": // hier wird nach der dritten Zieladresse gefragt
                if (yesNo_Slot_thirdDest.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName().equals("Ja")) {
                    return setAdress(DestinationC_Slot, input);
                } else {
                    return setUpComplete(input);
                }

            case "006": //Einrichtung fertig!
                destinationC.setName(DestinationC_Name_Slot.getValue());
                storeAdressToDB(destinationC, "DestinationC");
                return setUpComplete(input);
            default:
            }



            }

        return Optional.empty();
        }



}
