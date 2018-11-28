
package main.java.guideLines.handlers;

import java.util.Optional;
import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;

/**
 *
 * @author Alex
 */
public class SetUpInProgressIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		Request req = input.getRequestEnvelope().getRequest();
		DialogState state;
		try {
			state = ((IntentRequest) req).getDialogState();
		} catch (Exception e) {
			return false;
		}
		
		return !state.equals("COMPLETED") && input.matches(intentName("SetUpIntent"))
				&& req.getType().equals("IntentRequest");
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {

		Intent currentIntent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
		return input.getResponseBuilder().addDelegateDirective(currentIntent).build();
	}

}
