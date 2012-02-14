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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.*;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

public class OutboundDocRetrieveOrchestrationContextBuilder_g0 implements OrchestrationContextBuilder,
        OutboundDocRetrieveContextBuilder {

    private RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
    private AssertionType assertionType;
    private PolicyTransformer policyTransformer;
    private AuditTransformer auditTransformer;
    private OutboundDelegate nhinDelegate;
    private NhinAggregator nhinAggregator;
    private NhinTargetSystemType nhinTarget;

    public RetrieveDocumentSetRequestType getRetrieveDocumentSetRequestType() {
        return retrieveDocumentSetRequestType;
    }

    public OutboundDocRetrieveOrchestrationContextBuilder_g0 setRetrieveDocumentSetRequestType(
            RetrieveDocumentSetRequestType retrieveDocumentSetRequestType) {
        this.retrieveDocumentSetRequestType = retrieveDocumentSetRequestType;
        return this;
    }

    public AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public PolicyTransformer getPolicyTransformer() {
        return policyTransformer;
    }

    public void setPolicyTransformer(PolicyTransformer policyTransformer) {
        this.policyTransformer = policyTransformer;
    }

    public AuditTransformer getAuditTransformer() {
        return auditTransformer;
    }

    public void setAuditTransformer(AuditTransformer auditTransformer) {
        this.auditTransformer = auditTransformer;
    }

    public OutboundDelegate getOutboundDelegate() {
        return nhinDelegate;
    }

    public OutboundDocRetrieveOrchestrationContextBuilder_g0 setOutboundDelegate(OutboundDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
        return this;
    }

    public NhinAggregator getNhinAggregator() {
        return nhinAggregator;
    }

    public void setNhinAggregator(NhinAggregator nhinAggregator) {
        this.nhinAggregator = nhinAggregator;
    }

    void setTarget(NhinTargetSystemType target) {
        nhinTarget = target;
    }

    public NhinTargetSystemType getTarget() {
        return nhinTarget;
    }

    @Override
    public OrchestrationContext build() {
        return new OrchestrationContext(new OutboundDocRetrieveStrategyImpl_g0(),
                new OutboundDocRetrieveOrchestratableImpl(getRetrieveDocumentSetRequestType(), getAssertionType(),
                        getPolicyTransformer(), getAuditTransformer(), getOutboundDelegate(), getNhinAggregator(),
                        getTarget()));
    }

    @Override
    public void setContextMessage(OutboundOrchestratable message) {
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            setContextMessage((OutboundDocRetrieveOrchestratable) message);
        }
    }

    public void setContextMessage(OutboundDocRetrieveOrchestratable message) {
        setAssertionType(message.getAssertion());
        setAuditTransformer(message.getAuditTransformer());
        setNhinAggregator(message.getAggregator());
        setPolicyTransformer(message.getPolicyTransformer());
        setTarget(message.getTarget());
        setRetrieveDocumentSetRequestType(message.getRequest());
        setOutboundDelegate(message.getNhinDelegate());
    }
}
