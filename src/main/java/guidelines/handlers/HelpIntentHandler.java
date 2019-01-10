

package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;

import guidelines.OutputStrings;
import guidelines.StatusAttributes;


import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {
	
    @Override
    public boolean canHandle(HandlerInput input) {
    	     return input.matches(intentName("HelpIntent"));
    }
	

	
    @Override
    public Optional<Response> handle(HandlerInput input) {
    	Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
    	Intent intent = ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent();
    	if(sessionAttributes.get("HelpProcess") == null){
			sessionAttributes.put("HelpProcess", "1");
			return input
					.getResponseBuilder()
					.addElicitSlotDirective("YesNoSlot",intent)
					.withSpeech(OutputStrings.EINRICHTUNG_HELP_START.toString())
					.build();

		} else if(intent.getSlots().get("YesNoSlot")
				.getResolutions()
				.getResolutionsPerAuthority()
				.get(0).getValues().get(0)
				.getValue().equals("Ja")){


		}
    	else{
    		String helpSpeech;
    		switch((String)sessionAttributes.get("HelpProcess")) {
				case "1":
					helpSpeech = OutputStrings.EINRICHTUNG_HELP_HOME_OR_DEST.toString();
					break;
				case "2":
					helpSpeech = OutputStrings.EINRICHTUNG_HELP_HOME.toString();
					break;
				case "3":
					helpSpeech = OutputStrings.EINRICHTUNG_HELP_DESTINATION.toString();
					break;
				case "4":
					helpSpeech = OutputStrings.EINRICHTUNG_HELP_ENDE.toString();
					break;
				default:
					break;


    		}
		}
	return Optional.empty();
    }

    private void setHelpProcessToSession(HandlerInput input, String processId){
    	input.getAttributesManager().getSessionAttributes().put("HelpProcess", processId);
	}
}



