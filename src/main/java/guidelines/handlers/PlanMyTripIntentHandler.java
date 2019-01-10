package guidelines.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.OutputStrings;
import guidelines.StatusAttributes;
import guidelines.model.RouteCalculator;
import guidelines.model.Address;
import guidelines.model.AddressResolver;
import guidelines.model.Profile;

public class PlanMyTripIntentHandler implements RequestHandler
{

    private Map<String, Slot> slots;
    private AttributesManager attributesManager;
    private Map<String, Object> persistentAttributes;
    private Address TripStart;
    private Address TripDestination;
    private Profile userProfile;
    private Date Ankuftszeit;
    private Date Abfahrtszeit;
	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("PlanMyTripIntent") )
                && input
                .getAttributesManager()
                .getSessionAttributes()
                .get(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString())
                .equals("true");

	}

	private boolean isEmpty(Slot s){
	    return s.getValue() == null;
    }

    /**
     *
     * @param timeString The String that the Alexa Console returns. The Format is "HH:MM"
     *                   this also can be "MO", "AF", "EV" or "NI"
     * @return the date Object that represents the time
     */
    private Date resolveTime(String timeString){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

	    DateFormat format = new SimpleDateFormat("HH:mm", Locale.GERMAN);
            switch (timeString) {
                case ("MO"): //Morning
                    cal.set(Calendar.HOUR_OF_DAY,8);
                    cal.set(Calendar.MINUTE,0);
                    break;
                case ("AF"): //Afternoon
                    cal.set(Calendar.HOUR_OF_DAY,14);
                    cal.set(Calendar.MINUTE,0);
                    break;
                case ("EV"): //Evening
                    cal.set(Calendar.HOUR_OF_DAY,19);
                    cal.set(Calendar.MINUTE,0);
                    break;
                case ("NI"): //Night
                    cal.set(Calendar.HOUR_OF_DAY,23);
                    cal.set(Calendar.MINUTE,59);
                    break;
                default:
                    String hours = timeString.split(":")[0];
                    String minutes = timeString.split(":")[1];
                    if(hours.startsWith("0"))
                        hours = hours.substring(1);
                    if(minutes.startsWith("0"))
                        minutes = minutes.substring(1);
                    cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hours));
                    cal.set(Calendar.MINUTE,Integer.parseInt(minutes));
                    break;
            }
            return cal.getTime();


    }

	@Override
	public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        attributesManager = input.getAttributesManager();
        persistentAttributes = attributesManager.getPersistentAttributes();
        Address start = null,
                destinationA = null,
                destinationB = null,
                destinationC = null,
                realDestination = null;
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intReq = (IntentRequest) request;
        Intent intent = intReq.getIntent();
        slots = intent.getSlots();

        Slot ankunftszeit_slot = slots.get("Ankunftszeit"),
                gespeichertesZiel_slot = slots.get("GespeichertesZiel");
        if (ankunftszeit_slot.getValue() != null && gespeichertesZiel_slot.getValue() != null) {
            Date arrivalTime = resolveTime(ankunftszeit_slot.getValue());

            try {
                start = new ObjectMapper().readValue((String) persistentAttributes.get("Homeaddress"), Address.class);
                destinationA = new ObjectMapper().readValue((String)persistentAttributes.get("DestinationA"),Address.class);
                if(persistentAttributes.get("DestinationB") != null)
                    destinationB = new ObjectMapper().readValue((String)persistentAttributes.get("DestinationB"),Address.class);
                if(persistentAttributes.get("DestinationC") != null)
                    destinationC = new ObjectMapper().readValue((String)persistentAttributes.get("DestinationC"),Address.class);

                if(destinationA.getName().equals(gespeichertesZiel_slot.getValue()))
                    realDestination = destinationA;
                else if(destinationB != null && destinationB.getName().equals(gespeichertesZiel_slot.getValue()))
                    realDestination = destinationB;
                else if(destinationC != null && destinationC.getName().equals(gespeichertesZiel_slot.getValue()))
                    realDestination = destinationC;
                else {
                    return input.getResponseBuilder()
                            .withSpeech(OutputStrings.PLANMYTRIP_DESTINATION_NOT_FOUND_SPEECH.toString())
                            .withSimpleCard("Ziel nicht gespeichert", OutputStrings.PLANMYTRIP_DESTINATION_NOT_FOUND_CARD.toString())
                            .withShouldEndSession(false)
                            .build();
                }






            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            try{
                long minutesRemaining = new RouteCalculator().getTime(start.getNearestStation(),realDestination.getNearestStation(),arrivalTime);
                String speech;
                if(minutesRemaining != -1)
                    speech = "Du musst in "+minutesRemaining +" Minuten losgehen, um pünktlich zu sein.";
                else
                    speech = "Tut mir Leid. Es gibt keine Verbindung (mehr).";
                return input.getResponseBuilder()
                        .withSpeech(speech)
                        .withShouldEndSession(true)
                        .withSimpleCard("In "+minutesRemaining+" losgehen",speech)
                        .build();
            } catch (Exception ex){
                ex.printStackTrace();
            }



        }
        return Optional.empty();
    }

}
