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
package us.bekwam.guestbook.api.ejb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bekwam.guestbook.api.domain.Entry;
import us.bekwam.guestbook.commons.messages.ProfanityFilterRequest;
import us.bekwam.guestbook.commons.messages.ProfanityFilterRequestModeType;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author carl
 */
@Stateless
public class MessageSender {

    private Logger log = LoggerFactory.getLogger(MessageSender.class);

    @Resource(mappedName="java:/ConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(mappedName="java:/jms/queue/GuestbookOutbound")
    Queue queue;

    public boolean requestProfanityFilter(Entry entry) {
        try(
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer messageProducer = session.createProducer(queue);
        ) {

            ProfanityFilterRequest request = new ProfanityFilterRequest();
            request.setMode(ProfanityFilterRequestModeType.PRODUCTION);
            request.setId( entry.getId() );
            request.setText( entry.getText() );

            Message m = session.createObjectMessage(request);
            messageProducer.send( m );

            return true; // submitted

        } catch(JMSException exc) {
            log.error("error sending profanity request to outbound queue", exc);
        }
        return false;
    }
}
