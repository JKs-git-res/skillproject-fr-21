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
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import guidelines.OutputStrings;
import guidelines.StatusAttributes;


import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;
public class LaunchRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String,Object> persistantAttributes = input.getAttributesManager().getPersistentAttributes();
        Map<String,Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        if(persistantAttributes.get("Homeaddress") == null
                &&persistantAttributes.get("DestinationA") == null)//überprüft, ob der skill bereits eingerichtet wurde.
        {
           sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "false");
            return input.getResponseBuilder()
                    .withSimpleCard("GuideLines", OutputStrings.WELCOME_EINRICHTUNG_CARD.toString())
                    .withSpeech(OutputStrings.WELCOME_EINRICHTUNG.toString())
                    .withReprompt(OutputStrings.WELCOME_EINRICHTUNG_REPROMPT.toString())
                    .build();
        } else {
            sessionAttributes.put(StatusAttributes.KEY_SETUP_IS_COMPLETE.toString(), "true");
            return Optional.empty(); //TODO
        }



    }
}
