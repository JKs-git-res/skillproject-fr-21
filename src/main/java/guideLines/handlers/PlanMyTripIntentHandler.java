package main.java.guideLines.handlers;


import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class PlanMyTripIntentHandler implements RequestHandler
{

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("PlanMyTripIntent"));	
	}

	@Override
	public Optional<Response> handle(HandlerInput arg0) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}


}
