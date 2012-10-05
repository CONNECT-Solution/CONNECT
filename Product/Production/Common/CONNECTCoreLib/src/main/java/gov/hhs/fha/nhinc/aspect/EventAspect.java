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

import java.util.List;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventFactory;
import gov.hhs.fha.nhinc.event.EventType;
import gov.hhs.fha.nhinc.logging.transaction.dao.TransactionDAO;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import javax.xml.ws.WebServiceContext;

/**
 * @author zmelnick
 * 
 */
public abstract class EventAspect {

    private String getMessageId(WebServiceContext context) {
        return AsyncMessageIdExtractor.GetAsyncMessageId(context);
    }

    private String getTransactionID(WebServiceContext context, String messageId) {
        String transactionId = null;

        List<String> transactionIdList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
        if (NullChecker.isNotNullish(transactionIdList)) {
            transactionId = transactionIdList.get(0);
        }

        if ((transactionId == null) && (messageId != null)) {
            transactionId = TransactionDAO.getTransactionDAOInstance().getTransactionId(messageId);
        }

        return transactionId;
    }

    protected String getDescription() {
        // todo: what needs to be in here? - json
        return "";
    }

    private Event createEvent(String eventType) {
        WebServiceContext context = new org.apache.cxf.jaxws.context.WebServiceContextImpl();

        String messageId = getMessageId(context);
        String transactionId = getTransactionID(context, messageId);
        String description = getDescription();

        return EventFactory.getBeanInstance().createEvent(eventType, messageId, transactionId, description);
    }

    private void recordEvent(String eventType) {
        Event event = createEvent(eventType);

        System.out.println("Event triggered: " + eventType + " - " + event.getMessageID());
        
        // todo: event logger manager -> recordEvent()
    }

    public void beginInboundMessageEvent() {
        recordEvent(EventType.BEGIN_INBOUND_MESSAGE.toString());
    }

    public void beginInboundProcessingEvent() {
        recordEvent(EventType.BEGIN_INBOUND_PROCESSING.toString());
    }

    public void beginAdapterDelegationEvent() {
        recordEvent(EventType.BEGIN_ADAPTER_DELEGATION.toString());
    }

    public void endAdapterDelegationEvent() {
        recordEvent(EventType.END_ADAPTER_DELEGATION.toString());
    }

    public void endInboundProcessingEvent() {
        recordEvent(EventType.END_INBOUND_PROCESSING.toString());
    }

    public void endInboundMessageEvent() {
        recordEvent(EventType.END_INBOUND_MESSAGE.toString());
    }
    
    public void beginOutboundMessageEvent() {
        recordEvent(EventType.BEGIN_OUTBOUND_MESSAGE.toString());
    }

    public void beginOutboundProcessingEvent() {
        recordEvent(EventType.BEGIN_OUTBOUND_PROCESSING.toString());
    }

    public void beginNwhinInvocationEvent() {
        recordEvent(EventType.BEGIN_NWHIN_INVOCATION.toString());
    }

    public void endNwhinInvocationEvent() {
        recordEvent(EventType.END_NWHIN_INVOCATION.toString());
    }

    public void endOutboundProcessingEvent() {
        recordEvent(EventType.END_OUTBOUND_PROCESSING.toString());
    }

    public void endOutboundMessageEvent() {
        recordEvent(EventType.END_OUTBOUND_MESSAGE.toString());
    }

    public void messageProcessingFailedEvent() {
        recordEvent(EventType.MESSAGE_PROCESSING_FAILED.toString());
    }
    
}
