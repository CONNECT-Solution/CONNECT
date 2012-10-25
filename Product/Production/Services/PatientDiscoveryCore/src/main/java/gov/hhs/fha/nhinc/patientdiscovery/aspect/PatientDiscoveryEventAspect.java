/*
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

package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventBuilder;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventContextAccessor;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventDescriptionDirector;
import gov.hhs.fha.nhinc.event.EventDirector;
import gov.hhs.fha.nhinc.event.EventFactory;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.SOAPMessageRoutingAccessor;

import org.aspectj.lang.annotation.Aspect;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * @author akong
 * 
 */
@Aspect
@SuppressWarnings("unused")
public class PatientDiscoveryEventAspect {

    private EventRecorder eventRecorder;

    protected EventFactory eventFactory;

    public PatientDiscoveryEventAspect() {
        // TODO Auto-generated constructor stub
    }

    public void setEventFactory(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    public void setEventRecorder(EventRecorder eventRecorder) {
        this.eventRecorder = eventRecorder;
    }
    
   
    private void recordEvent(BaseEventBuilder builder, EventDescriptionDirector eventDescriptionDirector,
            EventDescriptionBuilder eventDescriptionBuilder) {
        eventDescriptionDirector.setEventDescriptionBuilder(eventDescriptionBuilder);
        builder.setEventDesciptionDirector(eventDescriptionDirector);

        EventDirector eventDirector = new EventDirector();
        eventDirector.setEventBuilder(builder);

        eventDirector.constructEvent();
        Event event = eventDirector.getEvent();
        eventRecorder.recordEvent(event);
    }
    

    protected void recordEvent(BaseEventBuilder builder, PRPAIN201305UV02 body) {
        EventDescriptionDirector eventDescriptionDirector = new EventDescriptionDirector();

        PRPAIN201305UV02EventDescriptionBuilder pRPAIN201305UV02Builder = new PRPAIN201305UV02EventDescriptionBuilder(body);

        //TODO: this isn't correct
        
        pRPAIN201305UV02Builder.setMsgRouting(new SOAPMessageRoutingAccessor());
        pRPAIN201305UV02Builder.setMsgContext(new EventContextAccessor() {
            
            @Override
            public String getServiceType() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getAction() {
                // TODO Auto-generated method stub
                return null;
            }
        });

        recordEvent(builder, eventDescriptionDirector, pRPAIN201305UV02Builder);

    }
    protected void recordEvent(BaseEventBuilder builder, RespondingGatewayPRPAIN201305UV02RequestType requestType) {
        recordEvent(builder, requestType.getPRPAIN201305UV02());
        
    }
    

    protected void recordEvent(BaseEventBuilder builder, ProxyPRPAIN201305UVProxyRequestType requestType) {
        recordEvent(builder, requestType.getPRPAIN201305UV02());
        
    }

}
