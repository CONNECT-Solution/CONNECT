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

package gov.hhs.fha.nhinc.docsubmission.aspect;

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
public class DocSubmissionEventAspect extends EventAspect {

    /*------InboundMessage----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.nhin.NhinXDR*.documentRepositoryProvideAndRegisterDocumentSetB(..))")
    private void inboundMessage() {
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.nhin.deferred.request.NhinXDRRequest*.provideAndRegisterDocumentSetBDeferredRequest(..))")
    private void inboundMessageDeferredRequest() {
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.nhin.deferred.response.NhinXDRResponse*.provideAndRegisterDocumentSetBDeferredResponse(..))")
    private void inboundMessageDeferredResponse() {
    }

    @Pointcut("inboundMessage() || inboundMessageDeferredRequest() || inboundMessageDeferredResponse()")
    private void anyInboundMessage() {
    }

    @Override
    @Before("anyInboundMessage()")
    public void beginInboundMessageEvent() {
        super.beginInboundMessageEvent();
    }

    @Override
    @After("anyInboundMessage()")
    public void endInboundMessageEvent() {
        super.endInboundMessageEvent();
    }

    /*------Inbound Processing----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.NhinDocSubmissionOrchImpl*.documentRepositoryProvideAndRegisterDocumentSetB(..))")
    private void processInboundMessage() {
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.NhinDocSubmissionDeferredRequestOrchImpl.provideAndRegisterDocumentSetBRequest(..))")
    private void processInboundMessageDeferredRequest() {
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.NhinDocSubmissionDeferredResponseOrchImpl.provideAndRegisterDocumentSetBResponse(..))")
    private void processInboundMessageDeferredResponse() {
    }

    @Override
    @Before("processInboundMessage() || processInboundMessageDeferredRequest() || processInboundMessageDeferredResponse()")
    public void beginInboundProcessingEvent() {
        super.beginInboundProcessingEvent();
    }

    @Override
    @After("processInboundMessage() || processInboundMessageDeferredRequest() || processInboundMessageDeferredResponse()")
    public void endInboundProcessingEvent() {
        super.endInboundProcessingEvent();
    }

    /*------ Adapter Delegation----*/

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy*.provideAndRegisterDocumentSetB(..))")
    private void adapterDelegation() {
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxy*.provideAndRegisterDocumentSetBRequest(..))")
    private void adapterDelegationDeferredRequest() {
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxy*.provideAndRegisterDocumentSetBResponse(..))")
    private void adapterDelegationDeferredResponse() {
    }

    @Pointcut("adapterDelegation() || adapterDelegationDeferredRequest() || adapterDelegationDeferredResponse()")
    private void anyAdapterDelegation(){
    }

    @Override
    @Before("anyAdapterDelegation()")
    public void beginAdapterDelegationEvent() {
        super.beginAdapterDelegationEvent();
    }

    @Override
    @After("anyAdapterDelegation()")
    public void endAdapterDelegationEvent() {
        super.endAdapterDelegationEvent();
    }

    /*------ Outbound Message ----*/
    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.entity.EntityDocSubmissionUnsecured*.provideAndRegisterDocumentSetB(..))")
    private void outboundMessageEntityUnsecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.entity.EntityDocSubmissionSecured*.provideAndRegisterDocumentSetB(..))")
    private void outboundMessageEntitySecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.entity.deferred.request.EntityDocSubmissionDeferredRequestUnsecured*.provideAndRegisterDocumentSetBAsyncRequest(..))")
    private void outboundMessageEntityDeferredRequestUnsecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.entity.deferred.request.EntityDocSubmissionDeferredRequestSecured*.provideAndRegisterDocumentSetBAsyncRequest(..))")
    private void outboundMessageEntityDeferredRequestSecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.entity.deferred.response.EntityDocSubmissionDeferredResponseUnsecured*.provideAndRegisterDocumentSetBAsyncResponse(..))")
    private void outboundMessageEntityDeferredResponseUnsecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.entity.deferred.response.EntityDocSubmissionDeferredResponseSecured*.provideAndRegisterDocumentSetBAsyncResponse(..))")
    private void outboundMessageEntityDeferredResponseSecured(){
    }

    @Pointcut("outboundMessageEntityUnsecured() || outboundMessageEntitySecured() || outboundMessageEntityDeferredRequestUnsecured() || outboundMessageEntityDeferredRequestSecured() || outboundMessageEntityDeferredResponseUnsecured() || outboundMessageEntityDeferredResponseSecured()")
    private void anyEntityOutboundMessage(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.passthru.PassthruDocSubmissionUnsecured*.provideAndRegisterDocumentSetB(..))")
    private void outboundMessagePassthruUnsecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.passthru.PassthruDocSubmissionSecured*.provideAndRegisterDocumentSetB(..))")
    private void outboundMessagePassthruSecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.passthru.deferred.request.PassthruDocSubmissionDeferredRequestUnsecured*.provideAndRegisterDocumentSetBAsyncRequest(..))")
    private void outboundMessagePassthruDeferredRequestUnsecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.passthru.deferred.request.PassthruDocSubmissionDeferredRequestSecured*.provideAndRegisterDocumentSetBAsyncRequest(..))")
    private void outboundMessagePassthruDeferredRequestSecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.passthru.deferred.response.PassthruDocSubmissionDeferredResponseUnsecured*.provideAndRegisterDocumentSetBAsyncResponse(..))")
    private void outboundMessagePassthruDeferredResponseUnsecured(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.*.passthru.deferred.response.EntityDocSubmissionDeferredResponseSecured*.provideAndRegisterDocumentSetBAsyncResponse(..))")
    private void outboundMessagePassthruDeferredResponseSecured(){
    }

    @Pointcut("outboundMessagePassthruUnsecured() || outboundMessagePassthruSecured() || outboundMessagePassthruDeferredRequestUnsecured() || outboundMessagePassthruDeferredRequestUnsecured() || outboundMessagePassthruDeferredRequestSecured() || outboundMessagePassthruDeferredResponseUnsecured() || outboundMessagePassthruDeferredResponseSecured()")
    private void anyPassthruOutboundMessage(){
    }

    @Pointcut("anyEntityOutboundMessage() || anyPassthruOutboundMessage()")
    private void anyOutboundMessage() {
    }

    @Override
    @Before("anyOutboundMessage()")
    public void beginOutboundMessageEvent() {
        super.beginOutboundMessageEvent();
    }

    @Override
    @After("anyOutboundMessage()")
    public void endOutboundMessageEvent() {
        super.endOutboundMessageEvent();
    }


    /*------ Outbound Processing ----*/
    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.entity.EntityDocSubmissionOrchImpl.provideAndRegisterDocumentSetB(..))")
    private void entityOutboundProcessing(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.EntityDocSubmissionDeferredRequestOrchImpl.provideAndRegisterDocumentSetBAsyncRequest(..))")
    private void entityDeferredRequestOutboundProcessing(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.EntityDocSubmissionDeferredResponseOrchImpl.provideAndRegisterDocumentSetBAsyncResponse(..))")
    private void entityDeferredResponseOutboundProcessing(){
    }

    @Pointcut("entityOutboundProcessing() || entityDeferredRequestOutboundProcessing() || entityDeferredResponseOutboundProcessing()")
    private void anyEntityOutboundProcessing(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.passthru.PassthruDocSubmissionOrchImpl.provideAndRegisterDocumentSetB(..))")
    private void passthruOutboundProcessing(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.PassthruDocSubmissionDeferredRequestOrchImpl.provideAndRegisterDocumentSetBRequest(..))")
    private void passthruDeferredRequestOutboundProcessing(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.PassthruDocSubmissionDeferredResponseOrchImpl.provideAndRegisterDocumentSetBResponse(..))")
    private void passthruDeferredResponseOutboundProcessing(){
    }

    @Pointcut("passthruOutboundProcessing() || passthruDeferredRequestOutboundProcessing() || passthruDeferredResponseOutboundProcessing()")
    private void anyPassthruOutboundProcessing() {
    }

    @Override
    @Before("anyEntityOutboundProcessing() || anyPassthruOutboundProcessing()")
    public void beginOutboundProcessingEvent() {
        super.beginOutboundProcessingEvent();
    }

    @Override
    @After("anyEntityOutboundProcessing() || anyPassthruOutboundProcessing()")
    public void endOutboundProcessingEvent() {
        super.endOutboundProcessingEvent();
    }



    /*------ Nwhin Invocation ----*/
    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxy.provideAndRegisterDocumentSetB(..))")
    private void nwhinInvocation(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy*.NhinDocSubmissionDeferredRequestProxy*.provideAndRegisterDocumentSetBRequest*(..))")
    private void nwhinDeferredRequestInvocation(){
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy*.NhinDocSubmissionDeferredResponseProxy*.provideAndRegisterDocumentSetBDeferredResponse*(..))")
    private void nwhinDeferredResponseInvocation(){
    }

    @Override
    @Before("nwhinInvocation() || nwhinDeferredRequestInvocation() || nwhinDeferredResponseInvocation()")
    public void beginNwhinInvocationEvent(){
        super.beginNwhinInvocationEvent();
    }

    @Override
    @After("nwhinInvocation() || nwhinDeferredRequestInvocation() || nwhinDeferredResponseInvocation()")
    public void endNwhinInvocationEvent(){
        super.endNwhinInvocationEvent();
    }


    /*------ Failure ----*/
    @Override
    @AfterThrowing("anyInboundMessage() || anyOutboundMessage()")
    public void failEvent() {
        super.failEvent();
    }

}
