/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.event.ContextEventBuilder;
import gov.hhs.fha.nhinc.event.DefaultEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventBuilder;
import gov.hhs.fha.nhinc.event.EventContextAccessor;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventDescriptionDirector;
import gov.hhs.fha.nhinc.event.EventDirector;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.MessageRoutingAccessor;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for EventAdviceDelegates. Has two template methods <i>createBeginEvent</i> and <i>createEndEvent</i>.
 *
 * @author bhumphrey
 *
 */
public abstract class BaseEventAdviceDelegate implements EventAdviceDelegate {

    private EventRecorder eventRecorder;
    private MessageRoutingAccessor messageRoutingAccessor;

    private static final Logger LOG = LoggerFactory.getLogger(BaseEventAdviceDelegate.class);

    /**
     * overridden in sub class to return the correct Begin event object.
     *
     * @return Concrete Event object
     */
    abstract protected Event createBeginEvent();

    /**
     * overridden in sub class to return the correct End event object.
     *
     * @return Concrete Event object
     */
    abstract protected Event createEndEvent();

    /**
     * inject the eventRecorder.
     *
     * @param eventRecorder
     */
    @Autowired
    public void setEventRecorder(EventRecorder eventRecorder) {
        this.eventRecorder = eventRecorder;
    }

    /**
     * inject the messageRoutingAccessor.
     *
     * @param messageRoutingAccessor
     */
    @Autowired
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
    public void begin(Object[] args, String serviceType, String version,
        Class<? extends EventDescriptionBuilder> eventDescriptionbuilderClass) {
        if (eventRecorder != null && eventRecorder.isRecordEventEnabled()) {
            EventDescriptionBuilder eventDescriptionBuilder = createAndInitializeEventDecriptionBuilder(args,
                createEventContextAccessor(serviceType, version), eventDescriptionbuilderClass, null);

            createAndRecordEvent(getBeginEventBuilder(eventDescriptionBuilder, args));
        }
    }

    /**
     * Execute the Director pattern of the Event then record the event.
     *
     * @param eventBuilder
     */
    private void createAndRecordEvent(EventBuilder eventBuilder) {
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
    public void end(Object[] args, String serviceType, String version,
        Class<? extends EventDescriptionBuilder> eventDescriptionbuilderClass, Object returnValue) {
        if (eventRecorder != null && eventRecorder.isRecordEventEnabled()) {
            EventDescriptionBuilder eventDescriptionBuilder = createAndInitializeEventDecriptionBuilder(args,
                createEventContextAccessor(serviceType, version), eventDescriptionbuilderClass, returnValue);
            createAndRecordEvent(getEndEventBuilder(eventDescriptionBuilder, args));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.aspect.FailureAdviceDelegate#fail(java.lang.Object[], java.lang.Throwable)
     */
    @Override
    public void fail(Object[] arguments, Throwable throwable) {
        if (eventRecorder != null && eventRecorder.isRecordEventEnabled()) {
            ErrorEventBuilder builder = new ErrorEventBuilder();
            builder.setAssertion(getAssertion(arguments));
            builder.setThrowable(throwable);
            createAndRecordEvent(builder);
        }
    }

    /**
     * after creating the event builder populate with args, context and routing accessor.
     *
     * @param args
     * @param eventContextAccessor
     * @param eventDescriptionbuilderClass
     * @return
     */
    protected EventDescriptionBuilder createAndInitializeEventDecriptionBuilder(Object[] args,
        EventContextAccessor eventContextAccessor,
        Class<? extends EventDescriptionBuilder> eventDescriptionbuilderClass, Object returnValue) {
        EventDescriptionBuilder eventDescriptionBuilder = createEventDescriptionBuilder(eventDescriptionbuilderClass);

        eventDescriptionBuilder.setArguments(args);
        eventDescriptionBuilder.setReturnValue(returnValue);
        eventDescriptionBuilder.setMsgContext(eventContextAccessor);
        eventDescriptionBuilder.setMsgRouting(messageRoutingAccessor);
        return eventDescriptionBuilder;

    }

    /**
     * create a new instance of the eventDescription builder class. If one can't created return the default builder.
     *
     * @param eventDescriptionbuilderClass
     * @return
     */
    private EventDescriptionBuilder createEventDescriptionBuilder(
        Class<? extends EventDescriptionBuilder> eventDescriptionbuilderClass) {
        EventDescriptionBuilder eventDescriptionBuilder;
        try {
            eventDescriptionBuilder = eventDescriptionbuilderClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            eventDescriptionBuilder = new DefaultEventDescriptionBuilder();
            LOG.warn("Could not get event description builder instance, using default instead: {}",
                e.getLocalizedMessage());
            LOG.trace("Event Description Builder exception: {}", e.getLocalizedMessage(), e);
        }
        return eventDescriptionBuilder;
    }

    private EventContextAccessor createEventContextAccessor(final String serviceType, final String version) {
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

    private EventBuilder getBeginEventBuilder(EventDescriptionBuilder eventDescriptionBuilder, Object[] args) {
        EventDescriptionDirector eventDescriptionDirector = new EventDescriptionDirector();
        eventDescriptionDirector.setEventDescriptionBuilder(eventDescriptionBuilder);
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = createBeginEvent();
            }

        };
        builder.setAssertion(getAssertion(args));
        builder.setEventDesciptionDirector(eventDescriptionDirector);
        return builder;
    }

    private EventBuilder getEndEventBuilder(EventDescriptionBuilder eventDescriptionBuilder, Object[] args) {
        EventDescriptionDirector eventDescriptionDirector = new EventDescriptionDirector();
        eventDescriptionDirector.setEventDescriptionBuilder(eventDescriptionBuilder);
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = createEndEvent();
            }

        };
        builder.setAssertion(getAssertion(args));
        builder.setEventDesciptionDirector(eventDescriptionDirector);
        return builder;
    }

    private AssertionType getAssertion(Object[] args) {
        AssertionType assertion = null;
        for (Object obj : args) {
            if (obj instanceof AssertionType) {
                assertion = (AssertionType) obj;
            } else if (obj instanceof RespondingGatewayPRPAIN201305UV02RequestType) {
                assertion = ((RespondingGatewayPRPAIN201305UV02RequestType) obj).getAssertion();
            } else if (obj instanceof RespondingGatewayCrossGatewayQueryRequestType) {
                assertion = ((RespondingGatewayCrossGatewayQueryRequestType) obj).getAssertion();
            } else if (obj instanceof RespondingGatewayCrossGatewayRetrieveRequestType) {
                assertion = ((RespondingGatewayCrossGatewayRetrieveRequestType) obj).getAssertion();
            } else if (obj instanceof RespondingGatewayProvideAndRegisterDocumentSetRequestType) {
                assertion = ((RespondingGatewayProvideAndRegisterDocumentSetRequestType) obj).getAssertion();
            } else if (obj instanceof RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType) {
                assertion = ((RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType) obj).getAssertion();
            } else if (obj instanceof RespondingGatewaySendAlertMessageType) {
                assertion = ((RespondingGatewaySendAlertMessageType) obj).getAssertion();
            } else if (obj instanceof RespondingGatewayPRPAIN201306UV02RequestType) {
                assertion = ((RespondingGatewayPRPAIN201306UV02RequestType) obj).getAssertion();
            }
        }
        return assertion;
    }
}
