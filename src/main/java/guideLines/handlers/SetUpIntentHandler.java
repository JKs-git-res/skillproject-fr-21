package main.java.guideLines.handlers;

import main.java.guideLines.model.AddressResolver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.exceptions.StreetNotFoundException;
import main.java.guideLines.OutputStrings;
import main.java.guideLines.StatusAttributes;
import main.java.guideLines.model.Address;
import main.java.guideLines.model.FormOfTransport;
import main.java.guideLines.model.HouseNumberConverter;
import main.java.guideLines.model.Profile;

public class SetUpIntentHandler implements RequestHandler {

    private Map<String, Slot> slots;
    private AttributesManager attributesManager;
    private Map<String, Object> persistentAttributes;
    private Address homeAddress = null, destinationA = null, destinationB = null, destinationC = null;
    private Slot FormOfTransport_Slot;

    @Override
    public boolean canHandle(HandlerInput input) {
        Request req = input.getRequestEnvelope().getRequest();
        return input.matches(intentName("SetUpIntent")) && req.getType().equals("IntentRequest");
    }

    private Optional<Response> invalidAddress(HandlerInput input, String slot) {
        return input.getResponseBuilder()
                .addElicitSlotDirective(slot, ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent())
                .withSpeech(OutputStrings.EINRICHTUNG_INVALID_ADDRESS.toString())
                .withSimpleCard("Adresse ungültig", OutputStrings.EINRICHTUNG_INVALID_ADDRESS.toString())
                .build();
    }

    private void saveProfileToDataBase(Profile profile) throws JsonProcessingException {
        String ProfileJSON = new ObjectMapper().writeValueAsString(profile);
        persistentAttributes.put("UserProfile", ProfileJSON); // schreiben in DB
        attributesManager.setPersistentAttributes(persistentAttributes);
        attributesManager.savePersistentAttributes(); // nach dem schreiben zum "pushen" in die DB

    }

    /*
     * ich nehme hier die erste Adresse. Bitte anpassen damit es auch für mehrere funktioniert. Unten auch!!!!!
     * Noch zusätzlich formatiere ich die den String vom Slot [slot.getValue()] damit man den Integer Wert hat und nicht Wörter (hnc)
     */
    private Address resolveAddress(Slot slot) {
        try {
        	HouseNumberConverter hnc = new HouseNumberConverter();
            return new AddressResolver().getAddressList(hnc.getAdressHereAPIFormatted(slot.getValue())).get(0);
        } catch (IOException ex) {
            return null;
        } catch (JSONException ex) {
            return null;
        }
        /* Hier habe ich ein bisschen geändert weil AddressResolver keine StreetNotFoundException mehr 
        wirft sondern wenn's keine Straße gefunden wird in der Adresse, dann füge ich sie nicht mehr hinzu
        siehe AddressResolver:70.
       */
    }

    private Optional<Response> setUpComplete(HandlerInput input) {
        Profile userProfile = new Profile(homeAddress, destinationA, destinationB, destinationC);

        switch (FormOfTransport_Slot.getResolutions()
        		.getResolutionsPerAuthority().get(0).getValues().get(0)
        		.getValue().getName()) {
            case ("Bus"):
                userProfile.addPreferedFormOfTransport(FormOfTransport.BUS);
                break;
            case ("UBahn"):
                userProfile.addPreferedFormOfTransport(FormOfTransport.UBAHN);
                break;
            case ("SBahn"):
                userProfile.addPreferedFormOfTransport(FormOfTransport.SBAHN);
                break;
            case ("Tram"):
                userProfile.addPreferedFormOfTransport(FormOfTransport.TRAM);
                break;
            default:
                break;

        }
        try {
            saveProfileToDataBase(userProfile);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SetUpIntentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return input.getResponseBuilder()
                .withSpeech(OutputStrings.EINRICHTUNG_END.toString())
                .withSimpleCard("Einrichtung abgeschlossen", OutputStrings.EINRICHTUNG_END.toString())
                .build();
    }

    private Address getAddressFromLocation(String deviceId) throws IOException, JSONException, StreetNotFoundException {
        Address adr;
        StringBuilder result = new StringBuilder();
        URL url = new URL("http://api.amazonalexa.com/v1/devices/" + deviceId + "/settings/address");
        InputStream is = url.openStream();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))){
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            JSONObject json = new JSONObject(jsonText);
            /*
             * Hier nehme ich wieder die erste Adresse !!!
             */
            adr = new AddressResolver()
                    .getAddressList(json.get("addressLine1") + " "
                            + json.get("addressLine2") + " "
                            + json.get("addressLine3") + " "
                            + json.get("city")).get(0);
            return adr;
        } finally {
            is.close();
        }
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

        List<String> permissions = new ArrayList<String>();
        permissions.add("read::alexa:device:all:address");
        attributesManager = input.getAttributesManager();
        persistentAttributes = attributesManager.getPersistentAttributes();
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
        FormOfTransport_Slot = slots.get("FormOfTransport");
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

        if (intReq.getDialogState().getValue().equals("STARTED")) {
            //Anfang des Dialogs. Als erstes wird geprüft, ob der aktuelle Standort verwendet werden soll
            sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_YES_NO_LOCATION_SET.toString());
            return input.getResponseBuilder()
                    .addElicitSlotDirective("YesNoSlot_Location", intent)
                    .withSpeech(OutputStrings.EINRICHTUNG_YES_NO_LOCATION.toString())
                    .withSimpleCard("Aktuellen Standort verwenden?", OutputStrings.EINRICHTUNG_YES_NO_LOCATION.toString())
                    .build();

        } else {

            switch ((String) sessionAttributes.get(StatusAttributes.KEY_PROCESS.toString())) {

                case "000": //hier wird nach der Heimatadresse gefragt
                    sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_HOMEADDRESS_SET.toString());
                    if (yesNo_Slot_Loc.getResolutions()
                    		.getResolutionsPerAuthority()
                    		.get(0).getValues().get(0).getValue()
                    		.getName().equals("Nein")) {
                        return input.getResponseBuilder()
                                .addElicitSlotDirective("Homeaddress", intent)
                                .withSpeech(OutputStrings.EINRICHTUNG_HOMEADDRESS.toString())
                                .withSimpleCard("Heimatadresse angeben", OutputStrings.EINRICHTUNG_HOMEADDRESS.toString())
                                .build();
                    } else {
                        //TODO Falls der aktuelle Standort verwendet werden soll
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

                case "001": //hier wird nach dem Namen der Heimatadresse gefragt
                    homeAddress = resolveAddress(HomeAddress_Slot);
                    if (homeAddress == null) {
                        return invalidAddress(input, "Homeaddress");
                    } else {
                        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_NAME_HOME_SET.toString());
                        return input.getResponseBuilder()
                                .addElicitSlotDirective("NameHome", intent)
                                .withSpeech(OutputStrings.EINRICHTUNG_NAMEHOME.toString())
                                .withSimpleCard("Heimatadresse benennen", OutputStrings.EINRICHTUNG_NAMEHOME.toString())
                                .build();
                    }

                case "002": //hier wird nach der ersten Zieladresse gefragt
                    homeAddress.setName(NameHome_Slot.getValue());
                    sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_DESTINATION_A_SET.toString());
                    return input.getResponseBuilder()
                            .addElicitSlotDirective("DestinationA", intent)
                            .withSpeech(OutputStrings.EINRICHTUNG_DEST_A.toString())
                            .withSimpleCard("Zieladresse benennen", OutputStrings.EINRICHTUNG_DEST_A.toString())
                            .build();

                case "003": //hier wird nach dem Namen der ersten Zieladresse gefragt
                    destinationA = resolveAddress(DestinationA_Slot);
                    if (destinationA == null) {
                        return invalidAddress(input, "DestinationA");
                    } else {
                        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_NAME_A_SET.toString());
                        return input.getResponseBuilder()
                                .addElicitSlotDirective("NameA", intent)
                                .withSpeech(OutputStrings.EINRICHTUNG_NAME_A.toString())
                                .withSimpleCard("Heimatadresse benennen", OutputStrings.EINRICHTUNG_NAME_A.toString())
                                .build();
                    }

                case "004": //hier wird nach dem Lieblingstransportmittel gefragt
                    destinationA.setName(DestinationA_Name_Slot.getValue());
                    sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_FORM_OF_TRANSPORT_SET.toString());
                    return input.getResponseBuilder()
                            .addElicitSlotDirective("FormOfTransport", intent)
                            .withSpeech(OutputStrings.EINRICHTUNG_FORM_OF_TRANSPORT.toString())
                            .withSimpleCard("Lieblingstransportmittel angeben", OutputStrings.EINRICHTUNG_FORM_OF_TRANSPORT.toString())
                            .build();

                case "005": // und so weiter...
                    sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_YES_NO_WANT_SECOND_DEST_SET.toString());
                    return input.getResponseBuilder()
                            .addElicitSlotDirective("YesNoSlot_wantSecondDest", intent)
                            .withSpeech(OutputStrings.EINRICHTUNG_YES_NO_WANT_SECOND_DEST.toString())
                            .withSimpleCard("Zweites Ziel hinzufügen?", OutputStrings.EINRICHTUNG_YES_NO_WANT_SECOND_DEST.toString())
                            .build();

                case "006":
                    if (yesNo_Slot_secondDest.getResolutions()
                    		.getResolutionsPerAuthority().get(0).getValues().get(0)
                    		.getValue().getName().equals("Ja")) {
                        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_DESTINATION_B_SET.toString());
                        return input.getResponseBuilder()
                                .addElicitSlotDirective("DestinationB", intent)
                                .withSpeech(OutputStrings.EINRICHTUNG_DEST_B.toString())
                                .withSimpleCard("Zweites Ziel angeben", OutputStrings.EINRICHTUNG_DEST_B.toString())
                                .build();
                    } else {
                        return setUpComplete(input);
                    }

                case "007":
                    destinationB = resolveAddress(DestinationB_Slot);
                    if (destinationB == null) {
                        return invalidAddress(input, "DestinationB");
                    } else {
                        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_NAME_B_SET.toString());
                        return input.getResponseBuilder()
                                .addElicitSlotDirective("NameB", intent)
                                .withSpeech(OutputStrings.EINRICHTUNG_NAME_B.toString())
                                .withSimpleCard("Zweites Ziel benennen", OutputStrings.EINRICHTUNG_NAME_B.toString())
                                .build();
                    }

                case "008":
                    destinationB.setName(DestinationB_Name_Slot.getValue());
                    sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_YES_NO_WANT_THIRD_DEST_SET.toString());
                    return input.getResponseBuilder()
                            .addElicitSlotDirective("YesNoSlot_wantThirdDest", intent)
                            .withSpeech(OutputStrings.EINRICHTUNG_YES_NO_WANT_THIRD_DEST.toString())
                            .withSimpleCard("Drittes Ziel hinzufügen?", OutputStrings.EINRICHTUNG_YES_NO_WANT_THIRD_DEST.toString())
                            .build();

                case "009":
                    if (yesNo_Slot_thirdDest.getResolutions()
                    		.getResolutionsPerAuthority().get(0).getValues().get(0)
                    		.getValue().getName().equals("Ja")) {
                        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_DESTINATION_C_SET.toString());
                        return input.getResponseBuilder()
                                .addElicitSlotDirective("DestinationC", intent)
                                .withSpeech(OutputStrings.EINRICHTUNG_DEST_C.toString())
                                .withSimpleCard("Drittes Ziel angeben", OutputStrings.EINRICHTUNG_DEST_C.toString())
                                .build();
                    } else {
                        return setUpComplete(input);
                    }
                case "010":
                    destinationC = resolveAddress(DestinationC_Slot);
                    if (destinationC == null) {
                        return invalidAddress(input, "DestinationC");
                    } else {
                        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(), StatusAttributes.VALUE_NAME_C_SET.toString());
                        return input.getResponseBuilder()
                                .addElicitSlotDirective("NameC", intent)
                                .withSpeech(OutputStrings.EINRICHTUNG_NAME_C.toString())
                                .withSimpleCard("Drittes Ziel benennen", OutputStrings.EINRICHTUNG_NAME_C.toString())
                                .build();
                    }
                case "011":
                    destinationC.setName(DestinationC_Name_Slot.getValue());
                    return setUpComplete(input);
                default:

            }
           
        }
        return Optional.empty();

    }

}
