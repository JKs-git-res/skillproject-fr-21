

package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;

import guidelines.OutputStrings;
import guidelines.StatusAttributes;


import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {
	
    @Override
    public boolean canHandle(HandlerInput input) {
    	     return input.matches(intentName("HelpIntent"))
                || input.getAttributesManager().getSessionAttributes().get("HelpProcess") != null;
    }
	

	
    @Override
    public Optional<Response> handle(HandlerInput input) {
    	Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
    	Intent intent = ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent();
    	Slot heim_Ziel = intent.getSlots().get("Heimadresse_Zieladresse");
    	Slot yesNo = intent.getSlots().get("YesNoSlot");
        String helpProcess = (String)sessionAttributes.get("HelpProcess");
        String yes_or_no = "";
    	if(yesNo.getValue() != null)
    	    yes_or_no = yesNo.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName();
    	String heim_or_ziel = "";
    	if(helpProcess != null && helpProcess.equals("2a") && yes_or_no.equals("Ja"))
    	    sessionAttributes.put("HelpProcess", "2b");
    	if(helpProcess != null && helpProcess.equals("2b") && yes_or_no.equals("Ja")){
    	    sessionAttributes.put("HelpProcess", "3");
        }
    	if(heim_Ziel.getValue() != null){
            heim_or_ziel = heim_Ziel.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName();
            if(heim_or_ziel.equals("Zieladresse"))
                sessionAttributes.put("HelpProcess","2b");

            if(heim_or_ziel.equals("Heimadresse"))
                sessionAttributes.put("HelpProcess", "2a");
        }

    	if(helpProcess == null){
			sessionAttributes.put("HelpProcess", "1");
			return input
					.getResponseBuilder()
					.addElicitSlotDirective("YesNoSlot",intent)
					.withSpeech(OutputStrings.EINRICHTUNG_HELP_START.toString())
					.build();

		}  else if(yes_or_no.equals("Nein")){
            sessionAttributes.remove("HelpProcess");
            return input.getResponseBuilder()
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP_ENDE_2.toString())
                    .withShouldEndSession(false)
                    .build();
        } else {
            switch (helpProcess){
                case "1":
                    intent.getSlots().put("YesNoSlot", null);
                    return input.getResponseBuilder()
                            .withSpeech(OutputStrings.EINRICHTUNG_HELP_HOME_OR_DEST.toString())
                            .addElicitSlotDirective("Heimadresse_Zieladresse",intent)
                            .build();
                case "2a":
                    intent.getSlots().put("Heimadresse_Zieladresse", null);
                    intent.getSlots().put("YesNoSlot", null);
                    return input.getResponseBuilder()
                            .withSpeech(OutputStrings.EINRICHTUNG_HELP_HOME.toString())
                            .addElicitSlotDirective("YesNoSlot",intent)
                            .withShouldEndSession(false)
                            .build();
                case "2b":
                    intent.getSlots().put("Heimadresse_Zieladresse", null);
                    intent.getSlots().put("YesNoSlot", null);
                    return input.getResponseBuilder()
                            .withShouldEndSession(false)
                            .addElicitSlotDirective("YesNoSlot",intent)
                            .withSpeech(OutputStrings.EINRICHTUNG_HELP_DESTINATION.toString())
                            .build();
                case "3":
                    intent.getSlots().put("YesNoSlot", null);
                    sessionAttributes.remove("HelpProcess");
                    return input.getResponseBuilder()
                            .withSpeech(OutputStrings.EINRICHTUNG_HELP_ENDE.toString())
                            .build();

            }
        }
        return Optional.empty();
        }


}



