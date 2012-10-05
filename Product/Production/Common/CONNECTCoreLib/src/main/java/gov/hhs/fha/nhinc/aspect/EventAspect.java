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

    private String getDescription() {
        // todo: what needs to be in here?
        return "";
    }

    private Event createEvent(String eventType) {
        WebServiceContext context = new org.apache.cxf.jaxws.context.WebServiceContextImpl();
        
        String messageId = getMessageId(context);
        String transactionId = getTransactionID(context, messageId);
        String description = getDescription();

        return EventFactory.getBeanInstance().createEvent(eventType, messageId, transactionId, description);
    }

    public void beginInboundMessageEvent() {        
        Event event = createEvent(EventType.BEGIN_INBOUND_MESSAGE.toString());
        
        System.out.println("beginInboundMessageEvent" + event.getMessageID());
    }

    public void beginInboundProcessingEvent() {
        Event event = createEvent(EventType.BEGIN_INBOUND_PROCESSING.toString());
        
        System.out.println("beginInboundProcessing" + event.getMessageID());
    }

    public void beginAdapterDelegationEvent() {
        Event event = createEvent(EventType.BEGIN_ADAPTER_DELEGATION.toString());
        
        System.out.println("beginAdapterDelegation" + event.getMessageID());
    }

    public void endAdapterDelegationEvent() {
        Event event = createEvent(EventType.END_ADAPTER_DELEGATION.toString());
        
        System.out.println("endAdapterDelegationEvent" + event.getMessageID());
    }

    public void endInboundProcessingEvent() {
        Event event = createEvent(EventType.END_INBOUND_PROCESSING.toString());
        
        System.out.println("endInboundProcessingEvent" + event.getMessageID());
    }

    public void endInboundMessageEvent() {
        Event event = createEvent(EventType.END_INBOUND_MESSAGE.toString());
        
        System.out.println("endInboundMessageEvent" + event.getMessageID());
    }

    public void messageProcessingFailedEvent() {
        Event event = createEvent(EventType.MESSAGE_PROCESSING_FAILED.toString());
        
        System.out.println("endInboundMessageEvent" + event.getMessageID());
    }

    /*
     * //&&" + "args(*,context)")
     * 
     * @Pointcut("execution(* gov.hhs.fha.nhinc..nhin..*.*rovideAndRegisterDocumentSetB*(..))") private void
     * DSInboundMessage(){ }
     * 
     * @Before("DSInboundMessage()") public void doSomethingBefore(){ // String messageID =
     * AsyncMessageIdExtractor.GetAsyncMessageId(context); // EventFactory.createEvent(BeginInboundMessageEvent.class,
     * messageID, null, null); System.out.println("doSomethingBefore(narf)"); }
     * 
     * @After("DSInboundMessage()") public void doSomethingAfter(){ // String messageID =
     * AsyncMessageIdExtractor.GetAsyncMessageId(context); // EventFactory.createEvent(EndInboundMessageEvent.class,
     * messageID, null, null); System.out.println("doSomethingAfter(narf)");
     */
}
