package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.amazon.ask.model.slu.entityresolution.Resolutions;
import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.StatusAttributes;
import guidelines.model.Address;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonParseException;

import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

public class PlanMyTripIntentHandlerTest {
    private final String userName = "Alex";
    private final String homeAddress = "{" +
            "\"name\":\"zuhause\"," +
            "\"street\":\"Untertaxetweg\"," +
            "\"houseNumber\":150," +
            "\"postCode\":\"82131\"," +
            "\"city\":\"Gauting\"," +
            "\"locationId\":\"NT_WoC4cezHoWBLOhJUtnIjDA_xUDM\"," +
            "\"nearestStation\":{" +
                "\"name\":\"Feldstraße\"," +
                 "\"lines\":{}," +
                 "\"id\":\"mvv_1005614\"," +
                "\"city\":\"Gauting\"," +
                "\"latitude\":48.07076," +
                 "\"longitude\":11.39472}" +
            "}";
    private final String destinationA = "{" +
            "\"name\":\"Uni\"," +
            "\"street\":\"Lothstraße\"," +
            "\"houseNumber\":64," +
            "\"postCode\":\"80335\"," +
            "\"city\":\"München\"," +
            "\"locationId\":\"NT_GgesHsyrnzR3eEhPnUvBpA_2QD\"," +
            "\"nearestStation\":{" +
                "\"name\":\"Hochschule München (Lothstr.)\"," +
                "\"lines\":{\"N20\":\"TRAM\",\"20\":\"TRAM\",\"21\":\"TRAM\"}," +
                "\"id\":\"mvv_1000012\"," +
                "\"city\":\"München\"," +
                "\"latitude\":48.15427," +
                 "\"longitude\":11.55383}" +
            "}";
    private final String destinationB = "{" +
            "\"name\":\"arbeit\"," +
            "\"street\":\"Landsberger Straße\"" +
            ",\"houseNumber\":184," +
            "\"postCode\":\"80687\"," +
            "\"city\":\"München\"," +
            "\"locationId\":\"NT_rYbeKOj-I9mZagZ5dFffxD_xgDN\"," +
            "\"nearestStation\":{" +
                "\"name\":\"Am Lokschuppen\"," +
                "\"lines\":{\"N19\":\"TRAM\",\"18\":\"TRAM\",\"19\":\"TRAM\"}," +
                 "\"id\":\"mvv_1001206\"," +
                 "\"city\":\"München\"," +
                 "\"latitude\":48.14074," +
                "\"longitude\":11.52423}" +
            "}";
    private final String destinationC = "{" +
            "\"name\":\"shopping\"," +
            "\"street\":\"Rosenstraße\"," +
            "\"houseNumber\":1," +
            "\"postCode\":\"80331\"," +
            "\"city\":\"München\"," +
            "\"locationId\":\"NT_4EzmsR.Db5-7xf8qtMKaqA_xA\"," +
            "\"nearestStation\":{" +
                "\"name\":\"Marienplatz Süd\"," +
                "\"lines\":{}," +
                "\"id\":\"db_639038\"," +
                "\"city\":\"München\"," +
                "\"latitude\":48.135572," +
                "\"longitude\":11.573711}" +
            "}";
    private PlanMyTripIntentHandler planMyTripIntentHandler;

    @Before
    public void setup(){
        this.planMyTripIntentHandler = new PlanMyTripIntentHandler();
    }

    @Test
    public void testCanHandle(){
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(),"true");
        AttributesManager attributesManager = Mockito.mock(AttributesManager.class);
        when(attributesManager.getSessionAttributes()).thenReturn(sessionAttributes);
        when(inputMock.getAttributesManager()).thenReturn(attributesManager);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(planMyTripIntentHandler.canHandle(inputMock));
    }

    @Test
    public void testNavigation_toUniversity_8AM(){
        HandlerInput input = Mockito.mock(HandlerInput.class);
        try{
            List<Address> addresses = getAddressList(2);
            AttributesManager attributesManager = mockAttributesManager(addresses);
            RequestEnvelope requestEnvelope = mockRequestEnvelope("Uni","08:00");
            when(input.getAttributesManager()).thenReturn(attributesManager);
            when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
            when(input.getRequestEnvelope()).thenReturn(requestEnvelope);
            Response response = planMyTripIntentHandler.handle(input).get();
            assertFalse(response.getShouldEndSession());
            System.out.println(response.getOutputSpeech().toString());
            assertTrue(response.getOutputSpeech().toString().contains(" Minuten losgehen, um pünktlich zu sein."));
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private RequestEnvelope mockRequestEnvelope(String destinationName,String hh_mm){
        Map<String, Slot> slotMap = new HashMap<>();
        if(hh_mm != null && !hh_mm.equals("")){
            slotMap.put("Ankunftszeit",
                    Slot.builder()
                            .withConfirmationStatus(SlotConfirmationStatus.NONE)
                            .withName("Ankunftszeit")
                            .withValue(hh_mm)
                            .build());
        }

        slotMap.put("GespeichertesZiel",
                Slot.builder()
                        .withConfirmationStatus(SlotConfirmationStatus.NONE)
                        .withName("GespeichertesZiel")
                        .withValue(destinationName)
                        .build());
        return RequestEnvelope
                .builder()
                .withRequest(
                        IntentRequest
                                .builder()
                                .withIntent(Intent
                                        .builder()
                                        .withSlots(slotMap)
                                        .build())
                                .withLocale("de-DE")
                                .withDialogState(DialogState.STARTED)
                                .build())
                .build();
    }

    private List<Address> getAddressList(int amount) throws IOException {

            if(amount < 2)
                amount = 2;
            else if(amount > 4)
                amount  = 4;

            Address home = new ObjectMapper().readValue(homeAddress, Address.class);
            Address destA = new ObjectMapper().readValue(destinationA, Address.class);
            Address destB = new ObjectMapper().readValue(destinationB, Address.class);
            Address destC = new ObjectMapper().readValue(destinationC, Address.class);
            List<Address> addresses = new ArrayList<>();
            addresses.add(home);
            addresses.add(destA);
            addresses.add(destB);
            addresses.add(destC);

            return addresses.subList(0,amount);


    }


    private AttributesManager mockAttributesManager(List<Address> addressList) throws JsonProcessingException{
        AttributesManager attributesManager = Mockito.mock(AttributesManager.class);
        Map<String, Object> persistantAttributes = new HashMap<>();
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "true");
        persistantAttributes.put("UserName", userName);
        persistantAttributes.put("Homeaddress", new ObjectMapper().writeValueAsString(addressList.get(0)));
        persistantAttributes.put("DestinationA", new ObjectMapper().writeValueAsString(addressList.get(1)));
        if(addressList.size() > 2)
            persistantAttributes.put("DestinationB", new ObjectMapper().writeValueAsString(addressList.get(2)));
        if(addressList.size() > 3)
            persistantAttributes.put("DestinationC", new ObjectMapper().writeValueAsString(addressList.get(3)));
        when(attributesManager.getPersistentAttributes()).thenReturn(persistantAttributes);
        when(attributesManager.getSessionAttributes()).thenReturn(sessionAttributes);
        return attributesManager;

    }
}
