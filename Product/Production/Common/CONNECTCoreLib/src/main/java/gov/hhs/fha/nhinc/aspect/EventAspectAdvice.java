/**
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.aspect;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.event.ContextEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventDescriptionDirector;
import gov.hhs.fha.nhinc.event.EventDescriptionJSONDecorator;
import gov.hhs.fha.nhinc.event.EventManager;
import gov.hhs.fha.nhinc.event.EventType;
import gov.hhs.fha.nhinc.logging.transaction.dao.TransactionDAO;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import java.util.List;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;

/**
 * @author zmelnick
 * 
 */
public class EventAspectAdvice {

    private static final Log log = LogFactory.getLog(EventAspectAdvice.class);

    /*--- Inbound Message --*/
    public void beginInboundMessageEvent() {
        recordEvent(EventType.BEGIN_INBOUND_MESSAGE.toString());
    }

    public void endInboundMessageEvent() {
        recordEvent(EventType.END_INBOUND_MESSAGE.toString());
    }

    /*--- Inbound Processing --*/

    public void beginInboundProcessingEvent() {
        recordEvent(EventType.BEGIN_INBOUND_PROCESSING.toString());
    }

    public void endInboundProcessingEvent() {
        recordEvent(EventType.END_INBOUND_PROCESSING.toString());
    }

    /*--- Adapter Delegation --*/

    public void beginAdapterDelegationEvent() {
        recordEvent(EventType.BEGIN_ADAPTER_DELEGATION.toString());
    }

    public void endAdapterDelegationEvent() {
        recordEvent(EventType.END_ADAPTER_DELEGATION.toString());
    }

    /*--- Outbound Message --*/

    public void beginOutboundMessageEvent() {
        recordEvent(EventType.BEGIN_OUTBOUND_MESSAGE.toString());
    }

    public void endOutboundMessageEvent() {
        recordEvent(EventType.END_OUTBOUND_MESSAGE.toString());
    }

    /*--- Outbound Processing --*/

    public void beginOutboundProcessingEvent() {
        recordEvent(EventType.BEGIN_OUTBOUND_PROCESSING.toString());
    }

    public void endOutboundProcessingEvent() {
        recordEvent(EventType.END_OUTBOUND_PROCESSING.toString());
    }

    /*--- Nwhin Invocation --*/

    public void beginNwhinInvocationEvent() {
        recordEvent(EventType.BEGIN_NWHIN_INVOCATION.toString());
    }

    public void endNwhinInvocationEvent() {
        recordEvent(EventType.END_NWHIN_INVOCATION.toString());
    }

    /*--- Failure --*/
    public void failEvent() {
        recordEvent(EventType.MESSAGE_PROCESSING_FAILED.toString());
    }

    private void recordEvent(String eventType) {
        try {
            Event event = createEvent(eventType);
            EventManager.getInstance().recordEvent(event);
        } catch (Exception e) {
            log.warn("Failed to record event.", e);
        }
    }

    private Event createEvent(final String eventType) {
        WebServiceContext context = new WebServiceContextImpl();

        final String messageId = getMessageId(context);
        final String transactionId = getTransactionID(context, messageId);
        final String description = getDescription(context);

        return new Event() {

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public String getEventName() {
                return eventType;
            }

            @Override
            public String getMessageID() {
                return messageId;
            }

            @Override
            public String getTransactionID() {
                return transactionId;
            }

            @Override
            public void setTransactionID(String transactionID) {

            }

            @Override
            public void setMessageID(String messageID) {

            }

            @Override
            public void setDescription(String description) {

            }

        };
    }

    private String getMessageId(WebServiceContext context) {
        return AsyncMessageIdExtractor.getMessageId(context);
    }

    private String getTransactionID(WebServiceContext context, String messageId) {
        String transactionId = null;

        List<String> transactionIdList = AsyncMessageIdExtractor.getAsyncRelatesTo(context);
        if (NullChecker.isNotNullish(transactionIdList)) {
            transactionId = transactionIdList.get(0);
        }

        if ((transactionId == null) && (messageId != null)) {
            transactionId = TransactionDAO.getInstance().getTransactionId(messageId);
        }

        return transactionId;
    }

    @SuppressWarnings("unchecked")
    protected String getDescription(WebServiceContext context) {
        EventDescriptionDirector director = new EventDescriptionDirector();
        director.setEventDescriptionBuilder(new ContextEventDescriptionBuilder());

        director.constructEventDescription();

        EventDescriptionJSONDecorator jsonDescorator = new EventDescriptionJSONDecorator(director.getEventDescription());

        return jsonDescorator.toJSONString();
    }
}
