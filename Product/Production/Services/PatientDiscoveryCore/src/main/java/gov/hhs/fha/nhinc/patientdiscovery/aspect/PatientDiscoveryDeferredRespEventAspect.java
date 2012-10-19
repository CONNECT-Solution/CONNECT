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

import gov.hhs.fha.nhinc.aspect.EventAspectAdvice;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author akong
 *
 */
@Aspect
@SuppressWarnings("unused")
public class PatientDiscoveryDeferredRespEventAspect {

    /**
     * Advice to invoke at pointcut locations.
     */
    private final EventAspectAdvice eventAspectAdvice;
    
    /**
     * Constructor.
     * @param eventAspectAdvice depends on event aspect advice
     */
    public PatientDiscoveryDeferredRespEventAspect(EventAspectAdvice eventAspectAdvice) {
        super();
        this.eventAspectAdvice = eventAspectAdvice;
    }

    /*------InboundMessage----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhinPatientDiscoveryAsyncResp.respondingGatewayDeferredPRPAIN201306UV02(..)))")
    private void inboundMessage(){
    }

    @Before("inboundMessage()")
    public void beginInboundMessageEvent() {
        
        eventAspectAdvice.beginInboundMessageEvent();
    }

    @After("inboundMessage()")
    public void endInboundMessageEvent() {
        eventAspectAdvice.endInboundMessageEvent();
    }

    /*------Inbound Processing----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.deferred.response.NhinPatientDiscoveryAsyncRespImpl.respondingGatewayPRPAIN201306UV02(..))")
    private void processInboundMessage(){
    }

    @Before("processInboundMessage()")
    public void beginInboundProcessingEvent() {
        eventAspectAdvice.beginInboundProcessingEvent();
    }

    @After("processInboundMessage()")
    public void endInboundProcessingEvent() {
        eventAspectAdvice.endInboundProcessingEvent();
    }

    /*------ Adapter Delegation----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscovery*.processPatientDiscoveryAsyncResp(..))")
    private void adapterDelegation() {
    }

    @Before("adapterDelegation()")
    public void beginAdapterDelegationEvent() {
        eventAspectAdvice.beginAdapterDelegationEvent();
    }

    @After("adapterDelegation()")
    public void endAdapterDelegationEvent() {
        eventAspectAdvice.endAdapterDelegationEvent();
    }

    
    /*------OutboundMessage----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.EntityPatientDiscoveryDeferredResponse*.processPatientDiscoveryAsyncResp(..))")
    private void outboundMessage(){
    }
        
    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhincProxyPatientDiscovery*AsyncResp.proxyProcessPatientDiscoveryAsyncResp(..))")
    private void passthruOutboundMessage(){
    }

    @Before("outboundMessage() || passthruOutboundMessage()")
    public void beginOutboundMessageEvent() {
        eventAspectAdvice.beginOutboundMessageEvent();
    }

    @After("outboundMessage() || passthruOutboundMessage()")
    public void endOutboundMessageEvent() {
        eventAspectAdvice.endOutboundMessageEvent();
    }

    /*------Outbound Processing----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.entity.deferred.response.EntityPatientDiscoveryDeferredResponseImpl.processPatientDiscoveryAsyncResp(..))")
    private void processOutboundMessage(){
    }
    
    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.passthru.deferred.response.NhincProxyPatientDiscoveryAsyncRespImpl.proxyProcessPatientDiscoveryAsyncResp(..))")
    private void processPassthruOutboundMessage(){
    }

    @Before("processOutboundMessage() || processPassthruOutboundMessage()")
    public void beginOutboundProcessingEvent() {
        eventAspectAdvice.beginOutboundProcessingEvent();
    }

    @After("processOutboundMessage() || processPassthruOutboundMessage()")
    public void endOutboundProcessingEvent() {
        eventAspectAdvice.endOutboundProcessingEvent();
    }

    /*------ Nwhin Invocation ----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxy*.respondingGatewayPRPAIN201306UV02(..))")
    private void nwhinInvocation() {
    }

    @Before("nwhinInvocation()")
    public void beginNwhinInvocationEvent() {
        eventAspectAdvice.beginNwhinInvocationEvent();
    }

    @After("nwhinInvocation()")
    public void endNwhinInvocationEvent() {
        eventAspectAdvice.endNwhinInvocationEvent();
    }
     
    /*------ Failure ----*/
    @AfterThrowing("inboundMessage() || outboundMessage()")
    public void failEvent() {
        eventAspectAdvice.failEvent();
    }

}
