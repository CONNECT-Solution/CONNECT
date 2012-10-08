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

package gov.hhs.fha.nhinc.docquery.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import gov.hhs.fha.nhinc.aspect.EventAspect;

/**
 * @author akong
 *
 */
@Aspect
@SuppressWarnings("unused")
public class DocQueryEventAspect extends EventAspect {


    /*------InboundMessage----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.*.nhin.DocQuery*.respondingGatewayCrossGatewayQuery(..))")
    private void inboundMessage(){
    }

    @Override
    @Before("inboundMessage()")
    public void beginInboundMessageEvent() {
        super.beginInboundMessageEvent();
    }

    @Override
    @After("inboundMessage()")
    public void endInboundMessageEvent() {
        super.endInboundMessageEvent();
    }

    /*------Inbound Processing----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.nhin.NhinDocQueryOrchImpl*.respondingGatewayCrossGatewayQuery(..))")
    private void processInboundMessage(){
    }

    @Override
    @Before("processInboundMessage()")
    public void beginInboundProcessingEvent() {
        super.beginInboundProcessingEvent();
    }

    @Override
    @After("processInboundMessage()")
    public void endInboundProcessingEvent() {
        super.endInboundProcessingEvent();
    }

    /*------ Adapter Delegation----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy*.respondingGatewayCrossGatewayQuery(..))")
    private void adapterDelegation() {
    }

    @Override
    @Before("adapterDelegation()")
    public void beginAdapterDelegationEvent() {
        super.beginAdapterDelegationEvent();
    }

    @Override
    @After("adapterDelegation()")
    public void endAdapterDelegationEvent() {
        super.endAdapterDelegationEvent();
    }

    
    /*------OutboundMessage----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.*.entity.EntityDocQuery*.respondingGatewayCrossGatewayQuery(..))")
    private void outboundMessage(){
    }
        
    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.*.passthru.NhincProxyDocQuery*.respondingGatewayCrossGatewayQuery(..))")
    private void passthruOutboundMessage(){
    }

    @Override
    @Before("outboundMessage() || passthruOutboundMessage()")
    public void beginOutboundMessageEvent() {
        super.beginOutboundMessageEvent();
    }

    @Override
    @After("outboundMessage() || passthruOutboundMessage()")
    public void endOutboundMessageEvent() {
        super.endOutboundMessageEvent();
    }

    /*------Outbound Processing----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryOrchImpl.respondingGatewayCrossGatewayQuery(..))")
    private void processOutboundMessage(){
    }
    
    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.passthru.PassthruDocQueryOrchImpl.respondingGatewayCrossGatewayQuery(..))")
    private void processPassthruOutboundMessage(){
    }

    @Override
    @Before("processOutboundMessage() || processPassthruOutboundMessage()")
    public void beginOutboundProcessingEvent() {
        super.beginOutboundProcessingEvent();
    }

    @Override
    @After("processOutboundMessage() || processPassthruOutboundMessage()")
    public void endOutboundProcessingEvent() {
        super.endOutboundProcessingEvent();
    }

    /*------ Nwhin Invocation ----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxy*.respondingGatewayCrossGatewayQuery(..))")
    private void nwhinInvocation() {
    }

    @Override
    @Before("nwhinInvocation()")
    public void beginNwhinInvocationEvent() {
        super.beginNwhinInvocationEvent();
    }

    @Override
    @After("nwhinInvocation()")
    public void endNwhinInvocationEvent() {
        super.endNwhinInvocationEvent();
    }
     
    /*------ Failure ----*/

    @AfterThrowing("inboundMessage() || processInboundMessage() || adapterDelegation()")
    public void failEvent() {
        super.failEvent();
    }

}
