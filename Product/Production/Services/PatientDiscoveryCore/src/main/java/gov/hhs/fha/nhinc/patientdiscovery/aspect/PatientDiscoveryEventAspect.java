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

import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;
import gov.hhs.fha.nhinc.aspect.EventAspectAdvice;
import gov.hhs.fha.nhinc.event.BaseEventBuilder;
import gov.hhs.fha.nhinc.event.ContextEventBuilder;
import gov.hhs.fha.nhinc.event.ContextEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventBuilder;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventDescriptionDirector;
import gov.hhs.fha.nhinc.event.EventDirector;
import gov.hhs.fha.nhinc.event.EventFactory;
import gov.hhs.fha.nhinc.event.EventRecorder;

import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
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

    private EventFactory eventFactory;

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
    
    private void recordPRPAIN201305UV0Event(BaseEventBuilder builder, PRPAIN201305UV02 body) {
        EventDescriptionDirector eventDescriptionDirector = new EventDescriptionDirector();

        ContextEventDescriptionBuilder contextEventDesciptionBuilder = new ContextEventDescriptionBuilder();
        EventDescriptionBuilder pRPAIN201305UV02Builder = new PRPAIN201305UV02EventDescriptionBuilder(
                contextEventDesciptionBuilder, body);

        recordEvent(builder, eventDescriptionDirector, pRPAIN201305UV02Builder);

    }
//
//    /*------InboundMessage----*/
//    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhinPatientDiscovery.respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02,..)) &&"
//            + "args(body)")
//    private void inboundMessage(PRPAIN201305UV02 body) {
//    }
//    
//    
//
//    @Before("inboundMessage(PRPAIN201305UV02)")
//    public void beginInboundMessageEvent(PRPAIN201305UV02 body) {
//
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createBeginInboundMessage();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }
//
//   
//
//    @After("inboundMessage(PRPAIN201305UV02)")
//    public void endInboundMessageEvent(PRPAIN201305UV02 body) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createEndInboundMessage();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }

    /*------Inbound Processing----*/
    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02(..)) &&"
            + " args(requestType)")
    private void processInboundMessage(ProxyPRPAIN201305UVProxyRequestType requestType) {
    }

    @Before("processInboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(requestType)")
    public void beginInboundProcessingEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {

        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createBeginInboundProcessing();
            }
        };
        recordPRPAIN201305UV0Event(builder, requestType.getPRPAIN201305UV02());
    }

  

    @After("processInboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(requestType)")
    public void endInboundProcessingEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createEndInboundProcessing();
            }
        };
        recordPRPAIN201305UV0Event(builder, requestType.getPRPAIN201305UV02());
    }

    /*------ Adapter Delegation----*/
//
//    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy*.respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02,..)) &&"
//            + "args(body)")
//    private void adapterDelegation(PRPAIN201305UV02 body) {
//    }
//
//    @Before("adapterDelegation(PRPAIN201305UV02)")
//    public void beginAdapterDelegationEvent(PRPAIN201305UV02 body) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createBeginAdapterDelegation();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }
//
//    @After("adapterDelegation(PRPAIN201305UV02)")
//    public void endAdapterDelegationEvent(PRPAIN201305UV02 body) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createEndAdapterDelegation();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }

//    /*------OutboundMessage----*/
//    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.EntityPatientDiscovery*.respondingGatewayPRPAIN201305UV02(..)) &&"
//            + "args(requestType)")
//    private void outboundMessage(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
//    }
//    
//    @Before("outboundMessage() && args(requestType)")
//    public void beginOutboundMessageEvent(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createBeginOutboundMessage();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, requestType.getPRPAIN201305UV02());
//    }
//    
//    
//
//    @After("outboundMessage()  && args(requestType)")
//    public void endOutboundMessageEvent(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createEndOutboundMessage();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, requestType.getPRPAIN201305UV02());
//    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhincProxyPatientDiscovery*.proxyPRPAIN201305UV(..)) &&"
            + "args(requestType)")
    private void passthruOutboundMessage(ProxyPRPAIN201305UVProxyRequestType requestType) {
    }

    @Before("passthruOutboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(requestType)")
    public void beginOutboundMessageEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createBeginOutboundMessage();
            }
        };
        recordPRPAIN201305UV0Event(builder, requestType.getPRPAIN201305UV02());
    }

    @After("passthruOutboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(requestType)")
    public void endOutboundMessageEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createEndOutboundMessage();
            }
        };
        recordPRPAIN201305UV0Event(builder, requestType.getPRPAIN201305UV02());
    }

    /*------Outbound Processing----*/
//    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery._10.entity.EntityPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02(..))&&"
//            + "args(body)")
//    private void processOutboundMessage(PRPAIN201305UV02 body) {
//    }
//
//    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery._10.passthru.NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV(..)) &&"
//            + "args(body)")
//    private void processPassthruOutboundMessage(PRPAIN201305UV02 body) {
//    }
//
//    @Before("processOutboundMessage() || processPassthruOutboundMessage() && args(body)")
//    public void beginOutboundProcessingEvent(PRPAIN201305UV02 body) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createBeginOutboundProcessing();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }
//
//    @After("processOutboundMessage() || processPassthruOutboundMessage() && args(body)")
//    public void endOutboundProcessingEvent(PRPAIN201305UV02 body) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createEndOutboundProcessing();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }
//
//    /*------ Nwhin Invocation ----*/
//    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy*.respondingGatewayPRPAIN201305UV02(..))&&"
//            + "args(body)")
//    private void nwhinInvocation(PRPAIN201305UV02 body) {
//    }
//
//    @Before("nwhinInvocation() && args(body)")
//    public void beginNwhinInvocationEvent(PRPAIN201305UV02 body) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createBeginNwhinInvocation();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }
//
//    @After("nwhinInvocation() && args(body)")
//    public void endNwhinInvocationEvent(PRPAIN201305UV02 body) {
//        ContextEventBuilder builder = new ContextEventBuilder() {
//            @Override
//            public void createNewEvent() {
//                event = eventFactory.createEndNwhinInvocation();
//            }
//        };
//        recordPRPAIN201305UV0Event(builder, body);
//    }

    /*------ Failure ----*/
//    @AfterThrowing(pointcut="inboundMessage() || outboundMessage()", throwing="fault")
//    public void failEvent(PRPAIN201305UV02Fault fault) {
////        ContextEventBuilder builder = new ContextEventBuilder() {
////            @Override
////            public void createNewEvent() {
////                event = eventFactory.createMessageProcessingFailed();
////            }
////        };
////        recordPRPAIN201305UV0Event(builder, body);
//    }

}
