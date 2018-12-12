/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
 */
package main.java.guideLines;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import main.java.guideLines.handlers.BusStatusIntentHandler;
import main.java.guideLines.handlers.CancelandStopIntentHandler;
import main.java.guideLines.handlers.FallbackIntentHandler;
import main.java.guideLines.handlers.HelpIntentHandler;
import main.java.guideLines.handlers.LaunchRequestHandler;
import main.java.guideLines.handlers.PlanMyTripIntentHandler;
import main.java.guideLines.handlers.SessionEndedRequestHandler;
import main.java.guideLines.handlers.SetUpIntentHandler;

public class GuideLinesStreamHandler extends SkillStreamHandler {

  private static Skill getSkill() {
    return Skills.standard()
            .addRequestHandlers(
                    new SetUpIntentHandler(),
                    new PlanMyTripIntentHandler(),
                    new BusStatusIntentHandler(),
                    new LaunchRequestHandler(),
                    new CancelandStopIntentHandler(),
                    new SessionEndedRequestHandler(),
                    new HelpIntentHandler(),
                    new FallbackIntentHandler())
            // Add your skill id below
            .withTableName("guideLinesData")
            .withSkillId("amzn1.ask.skill.eef26807-1da3-43a9-868c-9cd15183b4dd")
            .build();
  }

  public GuideLinesStreamHandler() {
    super(getSkill());
  }

}
