package guidelines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;
import com.amazon.ask.model.slu.entityresolution.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import guidelines.OutputStrings;
import guidelines.StatusAttributes;
import guidelines.model.Address;
import guidelines.model.AddressResolver;
import guidelines.model.FormOfTransport;
import guidelines.model.Profile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class SetUpIntentHandlerTest {
    private final String[] slotsNameList = {"YesNoSlot_Location","Homeaddress",
            "NameHome","DestinationA","NameA","FormOfTransport",
            "YesNoSlot_wantSecondDest","DestinationB","NameB",
            "YesNoSlot_wantThirdDest","DestinationC","NameC"};

    private final String[] profileValues = {"Untertaxetweg 150 Gauting", "Zuhause",
            "Lothstraße 64 München", "Uni",
            "Landsbergerstraße 184 München", "Arbeit",
            "Flughafen München", "Flughafen"};
    private SetUpIntentHandler handler;



    /**
     *
     * @param slotValues die Werte die in den Slots gespeichert werden sollen.
     *                   Kann auch "null" sein, wenn man nur leere Slots will.
     * @return die fertig gebauten slots
     */
    private Slot[] getSlots(List<String> slotValues){
        Slot[] slots = new Slot[12];
        for(int i = 0; i< 12; i++){
            if(slotValues != null && slotValues.size() > i)
                slots[i] = mockSlotWithValue(slotsNameList[i], slotValues.get(i));
            else
                slots[i] = mockSlotWithValue(slotsNameList[i], null);
        }


        return slots;
    }

    /**
     *
     * @return liefert den Input, den der SetUpIntentHandler zum ersten Aufruf bekommt.
     */
    private HandlerInput mockInputSetUpStart(){
        Slot[] slots = getSlots(null);
        return HandlerInput.builder()
                .withRequestEnvelope(
                        RequestEnvelope.builder()
                                .withRequest(
                                        mockRequest("SetUpIntent",slots,DialogState.STARTED))
                                .build())
                .build();
    }

    /**
     *
     * @param processValue der Wert, der den Fortschritt der Einrichtung angibt (siehe StatusAttributes)
     * @param slotValues die Liste mit den Werten, die in die Slots gehören
     * @return liefert den Input, den der SetUpIntentHandler im Laufe der Einrichtung erhält
     */
    private HandlerInput mockInputSetUpInProcess(StatusAttributes processValue, List<String> slotValues){
        Slot[] slots = getSlots(slotValues);
        return HandlerInput.builder()
                .withRequestEnvelope(
                        RequestEnvelope.builder()
                                .withRequest(
                                        mockRequest("SetUpIntent",slots,DialogState.IN_PROGRESS))
                                .withSession(
                                        mockSessionWithSetUpProcessValue(processValue))
                                .build())
                .build();

    }


    /**
     * liefert einen Slot zurück
     * @param slotName der Name des Slots
     * @param slotValue der Value des Slots
     * @return
     */
    private Slot mockSlotWithValue(String slotName,String slotValue){
        return Slot.builder()
                .withName(slotName)
                .withValue(slotValue)
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
        for(int i = 0; i< slots.length; i++)
            slotMap.put(slots[i].getName(),slots[i]);

        return IntentRequest.builder()
                .withDialogState(state)
                .withIntent(Intent.builder()
                        .withName(requestName)
                        .withSlots(slotMap)
                        .build())
                .build();
    }




    @Before
    public void setup(){
        handler = new SetUpIntentHandler();
    }

    @Test
    public void testCanHandle(){
        HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(true);
        assertTrue(handler.canHandle(inputMock));
    }

    @Test
    public void testSetUpStart(){
        HandlerInput inputMock = mockInputSetUpStart();
        Response response = handler.handle(inputMock).get();
        assertFalse(response.getShouldEndSession());
        assertEquals(response.getDirectives().get(0).getType(),"Dialog.ElicitSlot");
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.EINRICHTUNG_YES_NO_LOCATION.toString()));
    }
    @Test
    public void testSetUp1(){
        List slotValues = new ArrayList<>();
        slotValues.add("Nein");
        HandlerInput inputMock = mockInputSetUpInProcess(StatusAttributes.VALUE_YES_NO_LOCATION_SET,slotValues);
        Response response = handler.handle(inputMock).get();
        assertFalse(response.getShouldEndSession());
        assertEquals(response.getDirectives().get(0).getType(),"Dialog.ElicitSlot");
        assertTrue(response.getOutputSpeech().toString().contains(OutputStrings.EINRICHTUNG_HOMEADDRESS.toString()));
    }

    @Test
    public void testDatabase(){

        HandlerInput inputMock = mockInputSetUpStart();
        Address home = null;
        try {
            home = new AddressResolver().getAddressList(profileValues[0]).get(0);
            home.setName(profileValues[1]);
            Address dest1 = new AddressResolver().getAddressList(profileValues[2]).get(0);
            dest1.setName(profileValues[3]);
            Address dest2 = new AddressResolver().getAddressList(profileValues[4]).get(0);
            dest2.setName(profileValues[5]);
            Address dest3 = new AddressResolver().getAddressList(profileValues[6]).get(0);
            dest3.setName(profileValues[7]);
            Profile userProfileToStore = new Profile(home, dest1, dest2, dest3);
            userProfileToStore.addPreferedFormOfTransport(FormOfTransport.BUS);
            handler.saveProfileToDataBase(userProfileToStore);

            Profile userProfileRecovered = new ObjectMapper()
                    .readValue(
                            (String)inputMock
                                    .getAttributesManager()
                                    .getPersistentAttributes()
                                    .get("UserProfile"),Profile.class);
            assertEquals(userProfileRecovered.getHomeAddress().getFullAddress(), profileValues[0]);
            assertEquals(userProfileRecovered.getHomeAddress().getName(), profileValues[1]);
            assertEquals(userProfileRecovered.getDestination(0).getFullAddress(), profileValues[2]);
            assertEquals(userProfileRecovered.getDestination(0).getName(),profileValues[3]);
            assertEquals(userProfileRecovered.getDestination(1).getFullAddress(), profileValues[4]);
            assertEquals(userProfileRecovered.getDestination(1).getName(),profileValues[5]);
            assertEquals(userProfileRecovered.getDestination(2).getFullAddress(), profileValues[6]);
            assertEquals(userProfileRecovered.getDestination(2).getName(),profileValues[7]);
            assertEquals(userProfileRecovered.getPreferedWayOfTransport(),FormOfTransport.BUS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * testet ob das Objekt der Klasse der
     */
    @Test
    public void testProfileConstruction(){
        List<String> slotValues = new ArrayList<>();
        slotValues.add("Nein");
        slotValues.add(profileValues[0]);
        slotValues.add(profileValues[1]);
        slotValues.add(profileValues[2]);
        slotValues.add(profileValues[3]);
        slotValues.add("Bus");
        slotValues.add("Ja");
        slotValues.add(profileValues[4]);
        slotValues.add(profileValues[5]);
        slotValues.add("Ja");
        slotValues.add(profileValues[6]);
        slotValues.add(profileValues[7]);
        HandlerInput inputMock = mockInputSetUpInProcess(StatusAttributes.VALUE_NAME_C_SET, slotValues);
        handler.handle(inputMock).get();
        Profile profileToTest = handler.getUserProfile();
        assertEquals(profileToTest.getHomeAddress().getFullAddress(), profileValues[0]);
        assertEquals(profileToTest.getHomeAddress().getName(), profileValues[1]);
        assertEquals(profileToTest.getDestination(0).getFullAddress(), profileValues[2]);
        assertEquals(profileToTest.getDestination(0).getName(),profileValues[3]);
        assertEquals(profileToTest.getDestination(1).getFullAddress(), profileValues[4]);
        assertEquals(profileToTest.getDestination(1).getName(),profileValues[5]);
        assertEquals(profileToTest.getDestination(2).getFullAddress(), profileValues[6]);
        assertEquals(profileToTest.getDestination(2).getName(),profileValues[7]);
        assertEquals(profileToTest.getPreferedWayOfTransport(),FormOfTransport.BUS);
    }

}
