package guidelines.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.amazon.ask.model.slu.entityresolution.*;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.OutputStrings;
import guidelines.StatusAttributes;
import guidelines.model.Address;
import guidelines.model.AddressResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetUpIntentHandlerTest {

    private final String[] slotsNameList = {"YesNoSlot_Location","Homeaddress",
            "NameHome","DestinationA","NameA",
            "YesNoSlot_wantSecondDest","DestinationB","NameB",
            "YesNoSlot_wantThirdDest","DestinationC","NameC"};
    private final String[] slotValuesList = {
            "Nein", "Untertaxetweg 150 Gauting",
            "Zuhause", "Lothstraße 64 München",
            "Uni","Ja","Landsbergerstraße 184 München",
            "Arbeit","Ja","Flughafen München","Flughafen"
    };

    private final String[] profileValues = {"Untertaxetweg 150 Gauting", "Zuhause",
            "Lothstraße 64 München", "Uni",
            "Landsbergerstraße 184 München", "Arbeit",
            "Flughafen München", "Flughafen"};

    private final String[] testAddresses = {
            //man kann hier gerne noch weitere Testadressen zum Array hinzufügen, die werden dann alle automatisch getestet
            "Untertaxetweg 150",
            "Untertaxetweg hundertfünfzig",
            "Hauptplatz zwei gauting",
            "Leierkasten München",
            "Flughafen München",
            "Olympiapark München",
            "Marienplatz",
            "Oktoberfest",
            "Apple Store München",
            "Hofbräuhaus München", 
            "Landsberger Straße hundertvierundachtzig", 
            "Lothstraße 64"
    };


    private SetUpIntentHandler handler;



    /**
     *
     * @param slotValues die Werte die in den Slots gespeichert werden sollen.
     *                   Kann auch "null" sein, wenn man nur leere Slots will.
     * @return die fertig gebauten slots
     */
    private Slot[] getSlots(List<String> slotValues){
        Slot[] slots = new Slot[12];
        for(int i = 0; i< 11; i++){
            if(slotValues != null && slotValues.size() > i)
                slots[i] = mockSlotWithValue(slotsNameList[i], slotValues.get(i),SlotConfirmationStatus.NONE);
            else
                slots[i] = mockSlotWithValue(slotsNameList[i], null,SlotConfirmationStatus.NONE);
        }


        return slots;
    }





    /**
     *
     * @param processValue der Wert, der den Fortschritt der Einrichtung angibt (siehe StatusAttributes)
     * @param slotValues die Liste mit den Werten, die in die Slots gehören
     * @return liefert den Input, den der SetUpIntentHandler im Laufe der Einrichtung erhält
     */
    private HandlerInput mockInputSetUpInProcess(StatusAttributes processValue, List<String> slotValues){
        Slot[] slots = getSlots(slotValues);
        Map<String, Object> sessionList = new HashMap<>();
        sessionList.put(StatusAttributes.KEY_PROCESS.toString(),processValue.toString());
        AttributesManager attributesManager = mockAttributesManager(sessionList, null);
        HandlerInput mockInput = Mockito.mock(HandlerInput.class);
        when(mockInput.getRequestEnvelope()).thenReturn(
                RequestEnvelope.builder()
                                .withRequest(
                                        mockRequest("SetUpIntent", slots, DialogState.IN_PROGRESS))
                                .build());
        when(mockInput.getAttributesManager()).thenReturn(attributesManager);
        when(mockInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
        return mockInput;

    }


    /**
     * liefert einen Slot zurück
     * @param slotName der Name des Slots
     * @param slotValue der Value des Slots
     * @return
     */
    private Slot mockSlotWithValue(String slotName,String slotValue, SlotConfirmationStatus confirmationStatus){
        return Slot.builder()
                .withName(slotName)
                .withValue(slotValue)
                .withConfirmationStatus(confirmationStatus)
                .withResolutions(Resolutions.builder()
                        .addResolutionsPerAuthorityItem(Resolution.builder()
                                .addValuesItem(ValueWrapper.builder()
                                        .withValue(Value.builder()
                                                .withName(slotValue).build())
                                        .build())
                                .build())
                        .build())
                .build();
    }

    /**
     *
     * @param setUpProcessValue der Wert, der den Fortschritt der Einrichtung angibt (siehe StatusAttributes)
     * @return liefert ein Session Objekt mit dem richtigen Session Attribut zurück,
     *      das den Fortschritt der Einrichtung anzeigen soll.
     */
    private Session mockSessionWithSetUpProcessValue(StatusAttributes setUpProcessValue){
        HashMap<String, Object> attributeMap = new HashMap<>();
            attributeMap.put(StatusAttributes.KEY_PROCESS.toString(), setUpProcessValue.toString());
        return Session.builder()
                .withAttributes(attributeMap)
                .build();

    }



    private IntentRequest mockRequest(String requestName, Slot[] slots,DialogState state){
        Map <String, Slot> slotMap = new HashMap<>();

            for(int i = 0; i< slots.length; i++) {
                if (slots[i] != null) {
                    slotMap.put(slots[i].getName(), slots[i]);
                }
            }


        return IntentRequest.builder()
                .withDialogState(state)
                .withLocale("de-DE")
                .withIntent(Intent.builder()
                        .withName(requestName)
                        .withConfirmationStatus(IntentConfirmationStatus.NONE)
                        .withSlots(slotMap)
                        .build())
                .build();
    }
    private AttributesManager mockAttributesManager(Map<String, Object> sessionList, Map<String, Object> persList){
        AttributesManager attributesManager = Mockito.mock(AttributesManager.class);
        Map<String, Object> sessionAttributes = new HashMap<>();
        Map<String, Object> persistantAttributes = new HashMap<>();
        if(sessionList != null)
            sessionAttributes.putAll(sessionList);
        if(persList != null)
            persistantAttributes.putAll(persList);
        /*
        System.out.println("Session Attributes: ");
        System.out.println("\t" +"Process: "+sessionAttributes.get(StatusAttributes.KEY_PROCESS.toString()));
        System.out.println("\t" +"Is Complete: " +sessionAttributes.get(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString()));
        for(int i= 0; i< slotsNameList.length; i++)
            System.out.println("\t" +slotsNameList[i]+" : " + sessionAttributes.get(slotsNameList[i]));
        */

        when(attributesManager.getSessionAttributes()).thenReturn(sessionAttributes);
        when(attributesManager.getPersistentAttributes()).thenReturn(persistantAttributes);
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        doNothing().when(attributesManager).setPersistentAttributes(arg.capture());
        doNothing().when(attributesManager).savePersistentAttributes();
        return attributesManager;
    }
    private AttributesManager mockAttributesManager(){
        AttributesManager attributesManager = Mockito.mock(AttributesManager.class);
        Map<String,Object> emptyMap = new HashMap<String, Object>();
        when(attributesManager.getSessionAttributes()).thenReturn(emptyMap);
        when(attributesManager.getPersistentAttributes()).thenReturn(emptyMap);
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        doNothing().when(attributesManager).setPersistentAttributes(arg.capture());
        doNothing().when(attributesManager).savePersistentAttributes();
        return attributesManager;
    }

    private Response mockResponse(int stage,StatusAttributes statusAttribute ,boolean isSetUpComplete,
                                  SlotConfirmationStatus confirmationStatusOfNewFeature){
        Map<String,Object> sessionAttributes = new HashMap<>();
        Map<String,Slot> slots = new HashMap<>();
        sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), Boolean.toString(isSetUpComplete));
        if(statusAttribute != null)
            sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(),statusAttribute.toString());
        else
            sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(),null);

        for(int j = 0; j < slotsNameList.length; j++){
            slots.put(slotsNameList[j], mockSlotWithValue(slotsNameList[j],null,SlotConfirmationStatus.NONE));
        }
        for(int i = 0; i <= stage; i++){
            if(i == 1 ^ i == 3 ^ i == 6 ^ i == 9){
                try{
                    sessionAttributes.put(slotsNameList[i],
                            new ObjectMapper()
                                    .writeValueAsString(
                                            new AddressResolver()
                                                    .getAddressList(
                                                            slotValuesList[i])
                                                    .get(0)));

                } catch (IOException ex) {}

            } else {
                sessionAttributes.put(slotsNameList[i],slotValuesList[i]);
            }
            if(i != stage)
                slots.put(slotsNameList[i],mockSlotWithValue(slotsNameList[i],slotValuesList[i],SlotConfirmationStatus.NONE));
            else
                slots.put(slotsNameList[i],mockSlotWithValue(slotsNameList[i],slotValuesList[i],confirmationStatusOfNewFeature));


        }

        AttributesManager attributesManager = mockAttributesManager(sessionAttributes, null);
        DialogState state;
        if(stage == -1)
            state = DialogState.STARTED;
        else
            state = DialogState.IN_PROGRESS;

        RequestEnvelope req = RequestEnvelope
                .builder()
                .withRequest(
                        IntentRequest
                                .builder()
                                .withDialogState(state)
                                .withIntent(Intent
                                        .builder()
                                        .withConfirmationStatus(IntentConfirmationStatus.NONE)
                                        .withSlots(slots)
                                        .build())
                                .build()
                )
                .build();
        HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.getRequestEnvelope()).thenReturn(req);
        when(inputMock.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(inputMock.getAttributesManager()).thenReturn(attributesManager);

        return handler.handle(inputMock).get();
    }




    @Before
    public void setup(){
        handler = new SetUpIntentHandler();
}

    @Test
    public void testCanHandle(){
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        Map<String,Object> sessionList = new HashMap<>();
        sessionList.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "false");
        AttributesManager attributesManager = mockAttributesManager(sessionList, null);
        when(inputMock.getAttributesManager()).thenReturn(attributesManager);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }



    /**
     *
     * @return liefert den Input, den der SetUpIntentHandler zum ersten Aufruf bekommt.
     */
    private HandlerInput mockInputSetUpStart(){
        AttributesManager attributesManager = mockAttributesManager();

        Slot[] slots = getSlots(null);
        HandlerInput mockInput = Mockito.mock(HandlerInput.class);
        when(mockInput.getRequestEnvelope()).thenReturn(
                RequestEnvelope.builder()
                        .withRequest(
                                mockRequest("SetUpIntent",slots,DialogState.STARTED))
                        .build());

        when(mockInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(mockInput.getAttributesManager()).thenReturn(attributesManager);
        return mockInput;
    }


    /**
     * dieser Test überprüft den Start der Einrichtung. Falls der User sagt : "Einrichtung starten"
     */
    @Test
    public void testSetUpStart(){
        Response response = mockResponse(-1,null,false,SlotConfirmationStatus.NONE);
        assertFalse(response.getShouldEndSession());
        assertEquals("Dialog.ElicitSlot",response.getDirectives().get(0).getType());
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.EINRICHTUNG_YES_NO_LOCATION.toString()));
        assertTrue(response.getCard().getType().equals("Simple"));
        assertTrue(response.getCard().toString().contains(OutputStrings.EINRICHTUNG_YES_NO_LOCATION.toString()));
    }

    @Test
    public void testSetUp_notUseLocation(){
        Response response = mockResponse(0,StatusAttributes.VALUE_YES_NO_LOCATION_SET,false,SlotConfirmationStatus.NONE);
        assertFalse(response.getShouldEndSession());
        assertEquals("Dialog.ElicitSlot",response.getDirectives().get(0).getType());
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.EINRICHTUNG_HOMEADDRESS.toString()));
        assertTrue(response.getCard().getType().equals("Simple"));
        assertTrue(response.getCard().toString().contains(OutputStrings.EINRICHTUNG_HOMEADDRESS.toString()));
    }
    //@Test
    public void testSetUp_doUseLocation(){
        HandlerInput input = Mockito.mock(HandlerInput.class);
        Map<String,Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(),StatusAttributes.VALUE_YES_NO_LOCATION_SET.toString());
        AttributesManager attributesManager = mockAttributesManager(sessionAttributes,null);
        Map<String, Slot> slots = new HashMap<>();
        Context context = Context
                .builder()
                .withSystem(SystemState
                        .builder()
                        .withApiAccessToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjEifQ.eyJhdWQiOiJodHRwczovL2FwaS5hbWF6b25hbGV4YS5jb20iLCJpc3MiOiJBbGV4YVNraWxsS2l0Iiwic3ViIjoiYW16bjEuYXNrLnNraWxsLmVlZjI2ODA3LTFkYTMtNDNhOS04NjhjLTljZDE1MTgzYjRkZCIsImV4cCI6MTU0NzAwMjc2NiwiaWF0IjoxNTQ2OTk5MTY2LCJuYmYiOjE1NDY5OTkxNjYsInByaXZhdGVDbGFpbXMiOnsiY29uc2VudFRva2VuIjoiQXR6YXxJd0VCSVBSbDllbm84MFVYc2tsNG05SG1FS0ttSzJUU2F5ZEZuNmo0WU5OaURKbnJWZjdUam5CUGtzU05OX3hYVTMwSlJianhEYTVUOWlvUVlGUXAzS2kyNk9WZXZTcnplMUg4TFgxV0tKMVNuRElVUC1OVHpfWW52TnpzYnB1TXBKTnZ5TExVZTlqcHIzRW53aFdKbnB2ZC05VTNLYVlJNHNYazA1NXI1dlN6aFRMTTB1TjNrVTl5cVNjS3YtYjF1RVZ2TnROb3gzSXFqallfdnh1ZWs3WjNEX21SdEJaMFREVXU0UDNZRk9qbUNXV1NHZWV0UFN0dmdXWlF3bnVIZjF3enZsbzV5MGV5UUdiX3o3dllVaFI4a28yc0pzMEladkVSVjZyWVU0RTlrMVVoOTZpeC1CT1BUVHpKN3g1eDJiNHEzbTBwbGZrX01NeV9iNG9qVHJiY19yZ1F4XzJZdU5FcTJ6cUV1QTNzeHp6aWh3VXdpYmF6bVVHd1NxWFQyY2xSb1JQQWRFaVIxWGVPSDJuYlR6NVlFQmNJU3JrcHFhVnZ0S0FDSWljOHZPWEZVRDlHSEtldmp0anlILW4zWFg4M3pTQzdqWjNzNnlQcDktQ01VQlhRdTFjbzgtaEdkVERhOHFPUE5RZDFMTVpVUFFNV3dFbEhxSzZPTVoyRlRHc2N3Z2xmZWIyZUpKUDJtSVM5ejRoWE54WVlETFh5YXY5djctYUhLa1pfejBrckljZG1RUkMwWG1FWndTV0E1UUVMcG9rIiwiZGV2aWNlSWQiOiJhbXpuMS5hc2suZGV2aWNlLkFIM0ZKSElXR0hEM0xKRVNISko0TkQ3N0NNN0pZQUlMSkROUFdYWUpURzVINEJEVE1BR1lMTUQ0TkRaQjdGRk5QQzZFMkxLTDNNSkhNNkxJRTRNNUwyRUxJSVZZQ1ZKTVROQzRWNU5WWkxYSlJBUEFRQ0VGWjdQWUhaRFBUWE9aMzVNSE5ETEZaMzRLWVNLNldFTjNSQ1BWS1EzQVNQRFBEUkVXVVpYVEhLSFBJM1pQTUFPSjIiLCJ1c2VySWQiOiJhbXpuMS5hc2suYWNjb3VudC5BSE9IVkFESUs1RzRZVkRMTVRRUVI2M0JOVVY1NkZBS0FQN0NMM1MzSEY0T0RaNlZUM0RVTzZKTFRXWDVVMzI1M1dRM0RLMzJNWDMzM0k0SFc3R1hOVDZVQjRYTkRWR09PMzVCWE5FR1ZLMkxTMktXM1NSVlJEWDZIWEI2S1RUWVRJNkpQSk5ZWElYWFhFTVdXRzVHTkdNSU1NM01JRDRGUkxGN1JYSzdTR0dVVEJVRVRLTEZHNTVDQkhCQ05PSTRDR0syVVJaRFpBQTZJWkEifX0.F-WbKWFxb3_YJZSq_rN1J3eJRFquhkdAjvJMLzI0ieL2bN-Auux4BZk8oaJ1F3-3ELuYSDEOM1eKkhNeEESTVTpyqFBsxs5fSf3K4CLVRm1h28KtkhOfHmFlGyspDWixzOJMhUOSCs2qeVAlcJF7Tl15Ga-tpCIB58HNc5bzEITJkbfHuXjYYHL-amWCejsti2464yWg05jflNhVQ5Q-aZcP5cV3g0qMveeyf6x8wQclw9xNxTCbt5IloqaDdmI6mqdwhzUABYUy_-iTJmXn4pOLXHSgydqEmRQdnx0spWzmC7614fvMs16kuvcKIx6SweGhzv_1MzzMYfAOjKYPng")
                        .withApiEndpoint("https://api.eu.amazonalexa.com")
                        .withUser(User
                                .builder()
                                .withUserId("amzn1.ask.account.AHOHVADIK5G4YVDLMTQQR63BNUV56FAKAP7CL3S3HF4ODZ6VT3DUO" +
                                        "6JLTWX5U3253WQ3DK32MX333I4HW7GXNT6UB4XNDVGOO35BXNEGVK2LS2KW3SRVRDX6HXB6KTTY" +
                                        "TI6JPJNYXIXXXEMWWG5GNGMIMM3MID4FRLF7RXK7SGGUTBUETKLFG55CBHBCNOI4CGK2URZDZAA6IZA")
                                .build())
                        .withDevice(Device
                                .builder()
                                .withDeviceId("amzn1.ask.device.AH3FJHIWGHD3LJESHJJ4ND77CM7JYAILJDNPWXYJTG5H4BDTMAGYL" +
                                        "MD4NDZB7FFNPC6E2LKL3MJHM6LIE4M5L2ELIIVYCVJMTNC4V5NVZLXJRAPAQCEFZ7PYHZDPTXOZ3" +
                                        "5MHNDLFZ34KYSK6WEN3RCPVKQ3ASPDPDREWUZXTHKHPI3ZPMAOJ2")
                                .build())
                        .withApplication(Application
                                .builder()
                                .withApplicationId("amzn1.ask.skill.eef26807-1da3-43a9-868c-9cd15183b4dd")
                                .build())
                        .build())
                .build();
        slots.put(slotsNameList[0],mockSlotWithValue(slotsNameList[0],"Ja",SlotConfirmationStatus.NONE));
        RequestEnvelope requestEnvelope = RequestEnvelope
                .builder()
                .withRequest(IntentRequest
                        .builder()
                        .withIntent(Intent
                                .builder()
                                .withName("SetUpIntent")
                                .withSlots(slots)
                                .build())
                        .withDialogState(DialogState.IN_PROGRESS)
                        .build())
                .build();
        when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(input.getRequestEnvelope()).thenReturn(requestEnvelope);
        when(input.getContext()).thenReturn(Optional.ofNullable(context));
        when(input.getAttributesManager()).thenReturn(attributesManager);
        Response res = handler.handle(input).get();
        assertEquals("AskForPermissionsConsent",res.getCard().getType());
    }


    @Test
    public void testSetUp_usePrescribedAddresses(){
        for(int i = 0; i<testAddresses.length;i++){
            Map<String, Slot> slots = new HashMap<>();
            slots.put(slotsNameList[0],mockSlotWithValue(slotsNameList[0],"Nein", SlotConfirmationStatus.NONE));
            slots.put(slotsNameList[1],mockSlotWithValue(slotsNameList[1],testAddresses[i], SlotConfirmationStatus.NONE));
            HandlerInput mockInput = Mockito.mock(HandlerInput.class);
            Map<String, Object> sessionAttributes = new HashMap<>();
            sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(),StatusAttributes.VALUE_YES_NO_LOCATION_SET.toString());
            sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(),"false");
            AttributesManager attributesManager = mockAttributesManager(sessionAttributes, new HashMap<String, Object>());
            RequestEnvelope req = RequestEnvelope
                    .builder()
                    .withRequest(
                            IntentRequest
                                    .builder()
                                    .withIntent(
                                            Intent
                                                    .builder()
                                                    .withConfirmationStatus(IntentConfirmationStatus.NONE)
                                                    .withName("SetUpIntent")
                                                    .withSlots(slots)
                                                    .build())
                                    .withDialogState(DialogState.IN_PROGRESS)
                                    .withLocale("de-DE")
                                    .build()
                    )
                    .build();
            when(mockInput.getRequestEnvelope()).thenReturn(req);
            when(mockInput.getResponseBuilder()).thenReturn(new ResponseBuilder());
            when(mockInput.getAttributesManager()).thenReturn(attributesManager);
            Response response = handler.handle(mockInput).get();
            assertEquals("Dialog.ConfirmSlot",response.getDirectives().get(0).getType());
            assertTrue(response.getCard().getType().equals("Simple"));
            assertTrue(response.getOutputSpeech().toString().contains("Deine Adresse lautet:"));
            assertTrue(response.getCard().toString().contains("Deine Adresse lautet:"));
        }
    }


    @Test
    public void testSetUp_confirmationOnHomeAddressConfirmed(){
        Response response = mockResponse(1,StatusAttributes.VALUE_YES_NO_LOCATION_SET,false,SlotConfirmationStatus.CONFIRMED);
        assertTrue(response.getOutputSpeech().toString().contains("Alles klar. Die nächste Haltestelle dieser Adresse lautet: "));
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.EINRICHTUNG_NAMEHOME.toString()));
        assertEquals("Dialog.ElicitSlot", response.getDirectives().get(0).getType());
        assertFalse(response.getShouldEndSession());
        assertTrue(response.getCard().getType().equals("Simple"));

    }
    @Test
    public void testSetUp_confirmationOnHomeAddressDenied(){
        Response response = mockResponse(1,StatusAttributes.VALUE_YES_NO_LOCATION_SET,false,SlotConfirmationStatus.DENIED);
        assertTrue(response.getOutputSpeech().toString().contains("Dann wiederhole bitte die Adresse."));
        assertTrue(response.getOutputSpeech().toString().contains(" Versuche dabei laut und deutlich zu sprechen."));
        assertEquals("Dialog.ElicitSlot", response.getDirectives().get(0).getType());
        assertFalse(response.getShouldEndSession());
        assertTrue(response.getCard().getType().equals("Simple"));

    }



    @Test
    public void testSetUp_setNameHomeAddress(){
        Map<String,Slot> slots = new HashMap<>();
        slots.put(slotsNameList[0], mockSlotWithValue(slotsNameList[0],"Nein",SlotConfirmationStatus.NONE));
        slots.put(slotsNameList[1],mockSlotWithValue(slotsNameList[1],testAddresses[0], SlotConfirmationStatus.CONFIRMED));
        slots.put(slotsNameList[2],mockSlotWithValue(slotsNameList[2],"Zuhause",SlotConfirmationStatus.NONE));
        slots.put(slotsNameList[3],mockSlotWithValue(slotsNameList[3], null,SlotConfirmationStatus.NONE));

        Map<String,Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(StatusAttributes.KEY_PROCESS.toString(),StatusAttributes.VALUE_HOMEADDRESS_SET.toString());
        try{
            sessionAttributes.put(slotsNameList[1],
                    new ObjectMapper()
                            .writeValueAsString(
                                    new AddressResolver()
                                            .getAddressList(
                                                    testAddresses[0])
                                            .get(0)));
        } catch (IOException ex) {}
        sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "false");
        AttributesManager attributesManager = mockAttributesManager(sessionAttributes, null);
        RequestEnvelope req = RequestEnvelope
                .builder()
                .withRequest(
                        IntentRequest
                                .builder()
                                .withDialogState(DialogState.IN_PROGRESS)
                                .withIntent(Intent
                                        .builder()
                                        .withConfirmationStatus(IntentConfirmationStatus.NONE)
                                        .withSlots(slots)
                                        .build())
                                .build()
                )
                .build();
        HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.getRequestEnvelope()).thenReturn(req);
        when(inputMock.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(inputMock.getAttributesManager()).thenReturn(attributesManager);

        Response response = handler.handle(inputMock).get();
        try {
            Address adr = new ObjectMapper().readValue((String)inputMock.getAttributesManager().getPersistentAttributes().get("Homeaddress"),Address.class);
            assertEquals("Zuhause",adr.getName()); //testet, ob der Name der Addresse gespeichert wurde

        } catch(IOException ex){}
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.EINRICHTUNG_DEST_A.toString()));
        assertEquals("Dialog.ElicitSlot",response.getDirectives().get(0).getType());
        assertTrue(response.getCard().getType().equals("Simple"));
    }

    @Test
    public void testSetUp_setDestinationA(){
        Response response = mockResponse(3,StatusAttributes.VALUE_HOMEADDRESS_SET,false,SlotConfirmationStatus.NONE);
        assertTrue(response.getOutputSpeech().toString().contains("Deine Adresse lautet:"));
        assertTrue(response.getOutputSpeech().toString().contains("Ist das richtig?"));
        assertEquals("Dialog.ConfirmSlot", response.getDirectives().get(0).getType());
        assertFalse(response.getShouldEndSession());
        assertTrue(response.getCard().getType().equals("Simple"));
    }

    @Test
    public void testSetUp_confirmationOnDestinationAConfirmed(){
        Response response = mockResponse(3,StatusAttributes.VALUE_HOMEADDRESS_SET,false,SlotConfirmationStatus.CONFIRMED);
        assertTrue(response.getOutputSpeech().toString().contains("Alles klar. Die nächste Haltestelle dieser Adresse lautet: "));
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.EINRICHTUNG_NAME_A.toString()));
        assertEquals("Dialog.ElicitSlot",response.getDirectives().get(0).getType());
        assertFalse(response.getShouldEndSession());
        assertTrue(response.getCard().getType().equals("Simple"));

    }
    @Test
    public void testSetUp_confirmationOnDestinationADenied(){
        Response response = mockResponse(3,StatusAttributes.VALUE_HOMEADDRESS_SET,false,SlotConfirmationStatus.DENIED);
        assertTrue(response.getOutputSpeech().toString().contains("Dann wiederhole bitte die Adresse."));
        assertTrue(response.getOutputSpeech().toString().contains(" Versuche dabei laut und deutlich zu sprechen."));
        assertEquals("Dialog.ElicitSlot",response.getDirectives().get(0).getType());
        assertFalse(response.getShouldEndSession());
        assertTrue(response.getCard().getType().equals("Simple"));
    }


    @Test
    public void testStoreData(){
        Map<String, Object> persistantAttributes = new HashMap<String, Object>();

        try {
            Address homeAddress = new AddressResolver().getAddressList(profileValues[0]).get(0);
            homeAddress.setName(profileValues[1]);
            Address destinationA = new AddressResolver().getAddressList(profileValues[2]).get(0);
            destinationA.setName(profileValues[3]);
            Address destinationB = new AddressResolver().getAddressList(profileValues[4]).get(0);
            destinationB.setName(profileValues[5]);
            Address destinationC = new AddressResolver().getAddressList(profileValues[6]).get(0);
            destinationC.setName(profileValues[7]);
            persistantAttributes.put("HomeAddress",new ObjectMapper().writeValueAsString(homeAddress));
            persistantAttributes.put("DestinationA",new ObjectMapper().writeValueAsString(destinationA));
            persistantAttributes.put("DestinationB",new ObjectMapper().writeValueAsString(destinationB));
            persistantAttributes.put("DestinationC",new ObjectMapper().writeValueAsString(destinationC));
            AttributesManager attributesManager = mockAttributesManager(null,persistantAttributes);
            Map<String,Object> persistantAttributesRestored = attributesManager.getPersistentAttributes();
            Address homeAddressRestored = new ObjectMapper().readValue((String)persistantAttributesRestored.get("HomeAddress"),Address.class);
            Address destinationARestored = new ObjectMapper().readValue((String)persistantAttributesRestored.get("DestinationA"),Address.class);
            Address destinationBRestored = new ObjectMapper().readValue((String)persistantAttributesRestored.get("DestinationB"),Address.class);
            Address destinationCRestored = new ObjectMapper().readValue((String)persistantAttributesRestored.get("DestinationC"),Address.class);

            assertEquals(homeAddress,homeAddressRestored);
            assertEquals(destinationA,destinationARestored);
            assertEquals(destinationB,destinationBRestored);
            assertEquals(destinationC,destinationCRestored);


        } catch(IOException ex){}
    }







}