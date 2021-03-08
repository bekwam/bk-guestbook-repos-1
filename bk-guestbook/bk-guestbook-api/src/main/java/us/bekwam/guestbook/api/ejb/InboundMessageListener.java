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

import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bekwam.guestbook.api.domain.Entry;
import us.bekwam.guestbook.api.domain.EntryStateType;
import us.bekwam.guestbook.commons.messages.ProfanityFilterResponse;

import javax.annotation.security.PermitAll;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Optional;

/**
 * @author carl
 */
@MessageDriven(activationConfig =  {
        @ActivationConfigProperty(propertyName = "destination",
                propertyValue = "java:/jms/queue/GuestbookInbound"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue")
})
@SecurityDomain("BKGEJBSecurityDomain")
@PermitAll
public class InboundMessageListener implements MessageListener {

    private Logger log = LoggerFactory.getLogger(InboundMessageListener.class);

    @Inject
    EntryBean entryBean;

    @Override
    public void onMessage(Message message) {

        try {

            ObjectMessage objectMessage = (ObjectMessage) message;

            ProfanityFilterResponse response =
                    (ProfanityFilterResponse) objectMessage.getObject();

            Optional<Entry> entryOpt = entryBean.findEntryById(response.getId());

            if( entryOpt.isPresent() ) {
                Entry entry = entryOpt.get();
                switch (response.getResponse()) {
                    case ACCEPT:
                        entry.setState(EntryStateType.APPROVED);
                        break;
                    case ERROR:
                    case REJECT:
                        entry.setState(EntryStateType.REJECTED);
                        break;
                }
                entryBean.updateEntry(entry);
            }

        } catch(JMSException exc) {
            log.error("error processing inbound message", exc);
        }
    }
}
