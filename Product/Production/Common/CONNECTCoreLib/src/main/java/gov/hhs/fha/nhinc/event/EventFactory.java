/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.event.initiator.BeginNwhinInvocationEvent;
import gov.hhs.fha.nhinc.event.initiator.BeginOutboundMessageEvent;
import gov.hhs.fha.nhinc.event.initiator.BeginOutboundProcessingEvent;
import gov.hhs.fha.nhinc.event.initiator.EndNwhinInvocationEvent;
import gov.hhs.fha.nhinc.event.initiator.EndOutboundMessageEvent;
import gov.hhs.fha.nhinc.event.initiator.EndOutboundProcessingEvent;
import gov.hhs.fha.nhinc.event.responder.BeginAdapterDelegationEvent;
import gov.hhs.fha.nhinc.event.responder.BeginInboundMessageEvent;
import gov.hhs.fha.nhinc.event.responder.BeginInboundProcessingEvent;
import gov.hhs.fha.nhinc.event.responder.EndAdapterDelegationEvent;
import gov.hhs.fha.nhinc.event.responder.EndInboundMessageEvent;
import gov.hhs.fha.nhinc.event.responder.EndInboundProcessingEvent;
import org.springframework.stereotype.Component;

/**
 * @author zmelnick
 *
 */
@Component
public class EventFactory {

    public Event createBeginOutboundMessage() {
        return new BeginOutboundMessageEvent();
    }

    public Event createBeginOutboundProcessing() {
        return new BeginOutboundProcessingEvent();
    }

    public Event createBeginNwhinInvocation() {
        return new BeginNwhinInvocationEvent();
    }

    public Event createEndNwhinInvocation() {
        return new EndNwhinInvocationEvent();
    }

    public Event createEndOutboundProcessing() {
        return new EndOutboundProcessingEvent();
    }

    public Event createEndOutboundMessage() {
        return new EndOutboundMessageEvent();
    }

    public Event createBeginInboundMessage() {
        return new BeginInboundMessageEvent();
    }

    public Event createBeginInboundProcessing() {
        return new BeginInboundProcessingEvent();
    }

    public Event createBeginAdapterDelegation() {
        return new BeginAdapterDelegationEvent();
    }

    public Event createEndAdapterDelegation() {
        return new EndAdapterDelegationEvent();
    }

    public Event createEndInboundProcessing() {
        return new EndInboundProcessingEvent();
    }

    public Event createEndInboundMessage() {
        return new EndInboundMessageEvent();
    }
}
