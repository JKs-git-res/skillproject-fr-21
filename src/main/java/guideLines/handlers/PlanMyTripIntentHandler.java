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
import guidelines.model.Address;
import guidelines.model.AddressResolver;
import guidelines.model.Profile;
import guidelines.model.RouteCalculator;

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
		return input.matches(intentName("PlanMyTripIntent"));	
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

	    DateFormat format = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        try {
            switch (timeString) {
                case ("MO"): //Morning
                    return format.parse("08:00");
                case ("AF"): //Afternoon
                    return format.parse("14:00");
                case ("EV"): //Evening
                    return format.parse("19:00");
                case ("NI"): //Night
                    return format.parse("24:00");
                default:
                    return format.parse(timeString);
            }
        } catch (ParseException e) {
                return null;
            }


    }

	@Override
	public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        attributesManager = input.getAttributesManager();
        persistentAttributes = attributesManager.getPersistentAttributes();
        Address adr = null, destA = null, destB = null, destC = null;
        Request request = input.getRequestEnvelope().getRequest();
        Session session = input.getRequestEnvelope().getSession();
        IntentRequest intReq = (IntentRequest) request;
        Intent intent = intReq.getIntent();
        slots = intent.getSlots();

        Slot start_slot = slots.get("Start"),
                ziel_slot = slots.get("Ziel"),
                ankunftszeit_slot = slots.get("Ankunftszeit"),
                abfahrtszeit_slot = slots.get("Abfahrszeit"),
                gespeichertesZiel_slot = slots.get("GespeichertesZiel"),
                fragewort_slot = slots.get("Fragewort");

        try {
            userProfile = new ObjectMapper().readValue((String)persistentAttributes.get("UserProfile"),Profile.class);
        } catch (IOException e) {
        }

        if(isEmpty(start_slot)){
		    TripStart = userProfile.getHomeAddress();
        }
        if(isEmpty(ziel_slot)){
		    TripDestination = userProfile.findByName(gespeichertesZiel_slot.getValue());
        }
        if(isEmpty(gespeichertesZiel_slot)){
            try {
                TripDestination = new AddressResolver().getAddressList(ziel_slot.getValue()).get(0);
            } catch (IOException e) {
            }
        }
        if(!isEmpty(ankunftszeit_slot)){
		    Ankuftszeit = resolveTime(ankunftszeit_slot.getValue());

        } else if(!isEmpty(abfahrtszeit_slot)){
		    Abfahrtszeit = resolveTime(abfahrtszeit_slot.getValue());
        } else {
        		Abfahrtszeit = new Date();
        }
        String routeInfo = "";
        try {
			routeInfo =  	new RouteCalculator().getRoute(TripStart.getStation(), TripDestination.getStation());
		} catch (IOException e) {
          // TODO Auto-generated catch block

		}
        
        if(!isEmpty(fragewort_slot)){
            if(fragewort_slot.getValue().equals("Wie")){
           
            }
            else if(fragewort_slot.getValue().equals("Wann")) {}


        }


        return input.getResponseBuilder()
        		.withSpeech(routeInfo)
        		.withSimpleCard("Routeninformation", routeInfo)
        		.withShouldEndSession(true)
        		.build();
	}


}
