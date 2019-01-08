

package guideLines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;

import main.java.guideLines.OutputStrings;
import main.java.guideLines.StatusAttributes;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {
	SetUpIntentHandler handler = new SetUpIntentHandler();
	public String session;
	
    @Override
    public boolean canHandle(HandlerInput input) {
    	     return input.matches(intentName("AMAZON.HelpIntent"));
    }
	

	
    @Override
    public Optional<Response> handle(HandlerInput input) {
   	Intent intent = SetUpIntentHandler.intent;
    	
    	Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
    	session = (String) sessionAttributes.get(StatusAttributes.KEY_PROCESS.toString());
    	String repromptText = "Gyros";
    	String speechText = "";
    	String slotName = "";
    	switch(session){
    	
    	case"000":
    		slotName = "YesNoSlot_Location";
            speechText = OutputStrings.EINRICHTUNG_HELP0.toString()+ intent.toString();
            break;
    	case"001":
    		slotName = "Homeaddress";
    		speechText = OutputStrings.EINRICHTUNG_HELP1.toString();
    		break;
    	case"002":
    		speechText = OutputStrings.EINRICHTUNG_HELP2.toString();
    		break;
    	case"003":
    		speechText = OutputStrings.EINRICHTUNG_HELP3.toString();
    		break;
    	case"004":
    		speechText = OutputStrings.EINRICHTUNG_HELP4.toString();
    		break;
    	case"005":
    		speechText = OutputStrings.EINRICHTUNG_HELP5.toString();
    		break;
    	case"006":
    		speechText = OutputStrings.EINRICHTUNG_HELP6.toString();
    		break;
    	case"007":
    		speechText = OutputStrings.EINRICHTUNG_HELP7.toString();
    		break;
    	case"008":
    		speechText = OutputStrings.EINRICHTUNG_HELP8.toString();
    		break;
    	case"009":
    		speechText = OutputStrings.EINRICHTUNG_HELP9.toString();
    		break;
    	case"010":
    		speechText = OutputStrings.EINRICHTUNG_HELP10.toString();
    		break;
    	case"011":
    		speechText = OutputStrings.EINRICHTUNG_HELP11.toString();
    		break;
    	}
    	repromptText = "Lückenfüller";
		return input.getResponseBuilder()
				.withSimpleCard("Information:", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(false)
                .build();


    }
}



