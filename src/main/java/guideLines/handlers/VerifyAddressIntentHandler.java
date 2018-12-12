package main.java.guideLines.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import static com.amazon.ask.request.Predicates.sessionAttribute;

import main.java.guideLines.StatusAttributes;

public class VerifyAddressIntentHandler implements RequestHandler{

	@Override
	public boolean canHandle(HandlerInput input) {
		// TODO Auto-generated method stub
		
		return input.matches(sessionAttribute(StatusAttributes.KEY_FLAG_VERIFY_ADDRESS.toString(),true));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
            
		return null;
	}

}
