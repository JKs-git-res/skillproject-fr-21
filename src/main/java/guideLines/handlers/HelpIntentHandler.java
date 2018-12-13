
/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package main.java.guideLines.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import main.java.guideLines.OutputStrings;
import main.java.guideLines.StatusAttributes;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {
 	String speech = "";
	String reprompt = "";
	SetUpIntentHandler handler = new SetUpIntentHandler();
	private String session;
	
    @Override
    public boolean canHandle(HandlerInput input) {
    	     return input.matches(intentName("AMAZON.HelpIntent"));
    }
	

	
    @Override
    public Optional<Response> handle(HandlerInput input) {
    	
    	Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
    	session = (String) sessionAttributes.get(StatusAttributes.KEY_PROCESS.toString());
    	
    	switch(session){
    	
    	case"000":
    		
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP0.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"001":
    		
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP1.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"002":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP2.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"003":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP3.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"004":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP4.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"005":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP5.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"006":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP6.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"007":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP7.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"008":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP8.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"009":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP9.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"010":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP10.toString())
                    .withShouldEndSession(false)
                    .build();
    	case"011":
            return input.getResponseBuilder()
                    .withSimpleCard("Information:", speech)
                    .withSpeech(OutputStrings.EINRICHTUNG_HELP11.toString())
                    .withShouldEndSession(false)
                    .build();
    	}
    	speech = "Leider konnte keine Hilfe Option gefunden werden";
		return input.getResponseBuilder()
                .withSimpleCard("Information:", speech)
                .withSpeech("Deine Freundinn Benis Mum wird angerufen!")
                .withShouldEndSession(false)
                .build();


    }
}



