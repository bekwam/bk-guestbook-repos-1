/*
 * Copyright 2021 Bekwam, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.bekwam.guestbook.profanity.ejb;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bekwam.guestbook.commons.messages.ProfanityFilterRequest;
import us.bekwam.guestbook.commons.messages.ProfanityFilterResponse;
import us.bekwam.guestbook.commons.messages.ProfanityFilterResponseType;
import us.bekwam.guestbook.profanity.client.TestModeClient;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;

/**
 * @author carl
 */
@MessageDriven(activationConfig =  {
        @ActivationConfigProperty(propertyName = "destination",
                propertyValue = "java:/jms/queue/GuestbookOutbound"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="maxPoolSize", propertyValue="1")
})
public class ProfanityFilterMessageListener implements MessageListener {

    private Logger log = LoggerFactory.getLogger(ProfanityFilterMessageListener.class);

    @Resource(mappedName="java:/ConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(mappedName="java:/jms/queue/GuestbookInbound")
    Queue queue;

    @Override
    public void onMessage(Message message) {

        try(
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer messageProducer = session.createProducer(queue);
        )  {
            ObjectMessage objectMessage = (ObjectMessage) message;
            ProfanityFilterRequest request =
                    (ProfanityFilterRequest)objectMessage.getObject();
            Message m = null;

            switch(request.getMode()) {
                case TEST:
                    String result = new TestModeClient().filterText(request.getText());
                    if( StringUtils.isEmpty(result) ) {
                        ProfanityFilterResponse response =
                                new ProfanityFilterResponse();
                        response.setId(request.getId());
                        response.setResponse(ProfanityFilterResponseType.ACCEPT);
                        response.setExplanation("");
                        m = session.createObjectMessage(response);
                    } else {
                        ProfanityFilterResponse response =
                                new ProfanityFilterResponse();
                        response.setId(request.getId());
                        response.setResponse(ProfanityFilterResponseType.REJECT);
                        response.setExplanation(result);
                        m = session.createObjectMessage(response);
                    }
                    break;
                case PRODUCTION:
                    throw new UnsupportedOperationException("production not implemented yet");
            }

            messageProducer.send( m );

        } catch(Exception exc) {
            log.error("error processing outbound message", exc);
        }
    }
}
