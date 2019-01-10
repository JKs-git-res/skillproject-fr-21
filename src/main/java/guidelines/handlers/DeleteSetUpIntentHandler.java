package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import guidelines.StatusAttributes;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

public class DeleteSetUpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("DeleteSetUpIntent"))
                && handlerInput.getAttributesManager()
                .getPersistentAttributes()
                .get(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString()).equals("true");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        AttributesManager attributesManager = handlerInput.getAttributesManager();
        Map<String, Object> persistantAttributes = attributesManager.getPersistentAttributes();
        persistantAttributes.remove("Homeaddress");
        persistantAttributes.remove("DestinationA");
        persistantAttributes.remove("DestinationB");
        persistantAttributes.remove("DestinationC");
        persistantAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(),"false");
        attributesManager.setPersistentAttributes(persistantAttributes);
        attributesManager.savePersistentAttributes();
        return handlerInput.getResponseBuilder()
                .withSpeech("Die Einrichtung wurde erfolgreich gelöscht. Um den Skill neu einzurichten sage bitte 'Einrichtung starten'")
                .withShouldEndSession(false)
                .build();
    }
}
