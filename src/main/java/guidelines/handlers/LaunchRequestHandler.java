/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package guidelines.handlers;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import com.amazon.ask.model.interfaces.system.SystemState;
import guidelines.OutputStrings;
import guidelines.StatusAttributes;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static com.amazon.ask.request.Predicates.requestType;
public class LaunchRequestHandler implements RequestHandler {
    private Map<String,Object> persistantAttributes;
    private AttributesManager attributesManager;
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        attributesManager = input.getAttributesManager();
        persistantAttributes = attributesManager.getPersistentAttributes();
        Map<String,Object> sessionAttributes = attributesManager.getSessionAttributes();
        String userName = "";
        if(persistantAttributes.get("UserName") == null){
            try{
              Optional<Object> ctx = input.getContext();
              if (ctx.isPresent()){
                userName = getUsersFirstName((Context)input.getContext().get());
              }
            } catch (NoSuchElementException | IOException nseEx){
            }
        } else {
            userName = (String)persistantAttributes.get("UserName");
        }


        if(persistantAttributes.get("Homeaddress") == null
                &&persistantAttributes.get("DestinationA") == null)//überprüft, ob der skill bereits eingerichtet wurde.
        {
            List<String> permissions = new ArrayList<>();
            permissions.add("read::alexa:device:all:address");
            permissions.add("alexa::profile:given_name:read");

           sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "false");
           persistantAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "false");
            attributesManager.setPersistentAttributes(persistantAttributes);
            attributesManager.savePersistentAttributes();
            String speech, card;
           if(!userName.equals("")) {
               speech = "Hallo" + userName + ". " + OutputStrings.SPEECH_BREAK_LONG + OutputStrings.WELCOME_EINRICHTUNG_SPEECH.toString();
               card = "Hallo" + userName + ". "+ OutputStrings.WELCOME_EINRICHTUNG_CARD.toString();
           }
            else {
               speech = OutputStrings.WELCOME_EINRICHTUNG_SPEECH.toString();
               card =  OutputStrings.WELCOME_EINRICHTUNG_CARD.toString();
           }
            return input.getResponseBuilder()
                    .withAskForPermissionsConsentCard(permissions)
                    .withSpeech(speech)
                    .withSimpleCard("Einrichtung",card)
                    .withReprompt(OutputStrings.WELCOME_EINRICHTUNG_REPROMPT.toString())
                    .build();
        } else {
            sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "true");
            persistantAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "true");
            attributesManager.setPersistentAttributes(persistantAttributes);
            attributesManager.savePersistentAttributes();

            String speech, card;
            if(!userName.equals("")){
                speech = "Hallo " + userName +OutputStrings.WELCOME_BEREITS_EINGERICHTET_SPEECH.toString();
                card = "Hallo " + userName +OutputStrings.WELCOME_BEREITS_EINGERICHTET_CARD.toString();
            }
            else {
                speech = "Hallo "+OutputStrings.SPEECH_BREAK_LONG+". Wo möchtest du heute hinfahren?";
                card = "Hallo."+ OutputStrings.WELCOME_BEREITS_EINGERICHTET_CARD.toString();
            }

            return input.getResponseBuilder()
                    .withShouldEndSession(false)
                    .withSpeech(speech)
                    .withReprompt(OutputStrings.WELCOME_BEREITS_EINGERICHTET_REPROMPT.toString())
                    .withSimpleCard("Navigation", card)
                    .build();
        }



    }

    private String getUsersFirstName(Context ctx) throws IOException {
        SystemState sys = ctx.getSystem();
        String urlString = sys.getApiEndpoint() + "/v2/accounts/~current/settings/Profile.givenName";
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Accept","application/json");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " +sys.getApiAccessToken());
        StringBuilder response;
      try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
        String inputLine;
        response = new StringBuilder();
        while((inputLine = in.readLine()) != null)
          response.append(inputLine);
      }
        String jsonText = response.toString();
        String userName = (String) new JSONObject(jsonText).get("givenName");
        persistantAttributes.put("UserName", userName);
        attributesManager.setPersistentAttributes(persistantAttributes);
        attributesManager.savePersistentAttributes();
        return userName;
    }
}
