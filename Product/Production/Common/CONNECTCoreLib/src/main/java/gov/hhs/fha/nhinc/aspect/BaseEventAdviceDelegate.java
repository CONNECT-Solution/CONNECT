/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the United States Government nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.ContextEventBuilder;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventBuilder;
import gov.hhs.fha.nhinc.event.EventContextAccessor;
import gov.hhs.fha.nhinc.event.EventDescriptionDirector;
import gov.hhs.fha.nhinc.event.EventDirector;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.MessageRoutingAccessor;


/**
 * @author bhumphrey
 *
 */
public abstract class BaseEventAdviceDelegate  implements EventAdviceDelegate {
    private EventRecorder eventRecorder;
    private MessageRoutingAccessor messageRoutingAccessor;

    public void setEventRecorder(EventRecorder eventRecorder) {
        this.eventRecorder = eventRecorder;
    }

    public void setMessageRoutingAccessor(MessageRoutingAccessor messageRoutingAccessor) {
        this.messageRoutingAccessor = messageRoutingAccessor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.aspect.BeginEventAdviceDelegate#begin(java.lang.Object[], java.lang.String,
     * java.lang.String)
     */
    @Override
    public void begin(Object[] args, String serviceType, String version) {

        BaseEventDescriptionBuilder eventDescriptionBuilder = getEventDecriptionBuilder(args,
                getEventContextAccessor(serviceType, version), messageRoutingAccessor);
        createAndRecordEvent(args, serviceType, version, getBeginEventBuilder(eventDescriptionBuilder));
    }

    private void createAndRecordEvent(Object[] args, String serviceType, String version, EventBuilder eventBuilder) {
        EventDirector director = new EventDirector();
        director.setEventBuilder(eventBuilder);
        director.constructEvent();
        eventRecorder.recordEvent(director.getEvent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.aspect.EndEventAdviceDelegate#end(java.lang.Object[], java.lang.String, java.lang.String)
     */
    @Override
    public void end(Object[] args, String serviceType, String version) {
        BaseEventDescriptionBuilder eventDescriptionBuilder = getEventDecriptionBuilder(args,
                getEventContextAccessor(serviceType, version), messageRoutingAccessor);
        createAndRecordEvent(args, serviceType, version, getEndEventBuilder(eventDescriptionBuilder));
    }

    protected BaseEventDescriptionBuilder getEventDecriptionBuilder(Object[] args,
            EventContextAccessor eventContextAccessor, MessageRoutingAccessor messageRoutingAccessor) {
        return new BaseEventDescriptionBuilder(messageRoutingAccessor, eventContextAccessor) {

            @Override
            public void buildTimeStamp() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void buildStatuses() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void buildRespondingHCIDs() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void buildPayloadTypes() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void buildPayloadSize() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void buildNPI() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void buildInitiatingHCID() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void buildErrorCodes() {
                // TODO Auto-generated method stub
                
            }

            
        };
    }

    private EventContextAccessor getEventContextAccessor(final String serviceType, final String version) {
        return new EventContextAccessor() {

            @Override
            public String getServiceType() {
                return serviceType;
            }

            @Override
            public String getAction() {
                return version;
            }
        };
    }

    private EventBuilder getBeginEventBuilder(BaseEventDescriptionBuilder eventDescriptionBuilder) {
        EventDescriptionDirector eventDescriptionDirector = new EventDescriptionDirector();
        eventDescriptionDirector.setEventDescriptionBuilder(eventDescriptionBuilder);
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = createBeginEvent();
            }

        };
        builder.setEventDesciptionDirector(eventDescriptionDirector);
        return builder;
    }

    abstract Event createBeginEvent();

    private EventBuilder getEndEventBuilder(BaseEventDescriptionBuilder eventDescriptionBuilder) {
        EventDescriptionDirector eventDescriptionDirector = new EventDescriptionDirector();
        eventDescriptionDirector.setEventDescriptionBuilder(eventDescriptionBuilder);
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = createEndEvent();
            }

        };
        builder.setEventDesciptionDirector(eventDescriptionDirector);
        return builder;
    }

    abstract protected Event createEndEvent();
   
}
