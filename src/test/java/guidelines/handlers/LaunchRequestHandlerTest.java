package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import guidelines.OutputStrings;
import guidelines.StatusAttributes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;

public class LaunchRequestHandlerTest {

    private final String userName = "Alex";
    private LaunchRequestHandler launchRequestHandler;

    @Before
    public void setup(){
        launchRequestHandler = new LaunchRequestHandler();
    }
    @Test
    public void testCanHandle(){
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(launchRequestHandler.canHandle(inputMock));
    }

    /**
     *
     * @return HandlerInput für den LaunchRequest
     */
    private HandlerInput mockInput_LaunchRequest(){
        HandlerInput mockInput = Mockito.mock(HandlerInput.class);
        when(mockInput.getRequestEnvelope()).thenReturn(
                RequestEnvelope
                        .builder()
                        .withRequest(
                                LaunchRequest
                                        .builder()
                                        .withLocale("de-DE")
                                        .build()
                        )
                        .build());
        when(mockInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
        Map<String, Object> persistantAttributes = new HashMap<>();
        persistantAttributes.put("UserName", userName);
        AttributesManager attributesManager = mockAttributesManager(null,persistantAttributes);
        when(mockInput.getAttributesManager()).thenReturn(attributesManager);
        return mockInput;
    }

    private HandlerInput mockInput_LaunchRequest_alreadySetUp() {
        HandlerInput mockInput = Mockito.mock(HandlerInput.class);
        Map<String, Object> persistantAttributes = new HashMap<>();
        persistantAttributes.put("Homeaddress", "placeholder");
        persistantAttributes.put("DestinationA", "placeholder");
        persistantAttributes.put("UserName", "Alex");
        AttributesManager attributesManager = mockAttributesManager(null, persistantAttributes);
        when(mockInput.getAttributesManager()).thenReturn(attributesManager);
        when(mockInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(mockInput.getRequestEnvelope()).thenReturn(
                RequestEnvelope
                        .builder()

                        .withRequest(
                                LaunchRequest
                                        .builder()
                                        .withLocale("de-DE")
                                        .build()
                        )
                        .build());
        return mockInput;
    }

        private AttributesManager mockAttributesManager(Map<String, Object> sessionList, Map<String, Object> persList){
        AttributesManager attributesManager = Mockito.mock(AttributesManager.class);
        Map<String, Object> sessionAttributes = new HashMap<>();
        Map<String, Object> persistantAttributes = new HashMap<>();
        if(sessionList != null)
            sessionAttributes.putAll(sessionList);
        if(persList != null)
            persistantAttributes.putAll(persList);
        when(attributesManager.getSessionAttributes()).thenReturn(sessionAttributes);
        when(attributesManager.getPersistentAttributes()).thenReturn(persistantAttributes);
        return attributesManager;
    }

    private AttributesManager mockAttributesManager(){
        AttributesManager attributesManager = Mockito.mock(AttributesManager.class);
        Map<String,Object> emptyMap = new HashMap<String, Object>();
        when(attributesManager.getSessionAttributes()).thenReturn(emptyMap);
        when(attributesManager.getPersistentAttributes()).thenReturn(emptyMap);
        return attributesManager;
    }

    /**
     * dieser Test überprüft den LaunchRequest. Also das öffnen des Skills, falls dieser noch nicht eingerichtet ist
     */
    @Test
    public void test_launchRequest_notSetUp(){
        HandlerInput inputMock = mockInput_LaunchRequest();
        Response response = launchRequestHandler.handle(inputMock).get();
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.WELCOME_EINRICHTUNG_SPEECH.toString()));
        assertFalse(response.getShouldEndSession());
        assertTrue(response.getReprompt().getOutputSpeech().toString().contains(OutputStrings.WELCOME_EINRICHTUNG_REPROMPT.toString()));
    }

    /**
     * dieser Test überprüft den LaunchRequest, falls dieser bereits eingerichtet ist.
     */
    @Test
    public void test_launchRequest_IsSetUp(){
        HandlerInput inputMock = mockInput_LaunchRequest_alreadySetUp();
        Response response = launchRequestHandler.handle(inputMock).get();
        assertTrue(response.getOutputSpeech().toString().contains("Hallo " +userName));
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.WELCOME_BEREITS_EINGERICHTET_SPEECH.toString()));
        assertTrue(inputMock.getAttributesManager()
                .getSessionAttributes()
                .get(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString())
                .equals("true"));
        assertFalse(response.getShouldEndSession());
        assertTrue(response.getReprompt().getOutputSpeech().toString().contains(OutputStrings.WELCOME_BEREITS_EINGERICHTET_REPROMPT.toString()));
    }


}
