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
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import org.hl7.v3.PRPAIN201305UV02;

/**
 * @author bhumphrey/paul
 *
 */
public abstract class AbstractOutboundPatientDiscoveryOrchestrationContextBuilder implements
    OrchestrationContextBuilder {

    private NhinTargetSystemType target = null;
    private PRPAIN201305UV02 request = null;
    private AssertionType assertionType = null;
    private PolicyTransformer policyTransformer = null;
    private OutboundDelegate nhinDelegate = null;
    private Optional<OutboundResponseProcessor> nhinProcessor = Optional.absent();
    private String serviceName = "";

    @Override
    public OrchestrationContext build() {
        return new OrchestrationContext(getStrategy(), getOrchestratable());
    }

    abstract protected OutboundPatientDiscoveryOrchestratable getOrchestratable();

    abstract protected OutboundPatientDiscoveryStrategy getStrategy();

    public void setTarget(NhinTargetSystemType t) {
        this.target = t;
    }

    protected NhinTargetSystemType getTargetSystemType() {
        return this.target;
    }

    public void setRequest(PRPAIN201305UV02 pdRequest) {
        this.request = pdRequest;
    }

    protected PRPAIN201305UV02 getRequest() {
        return this.request;
    }

    protected AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    protected PolicyTransformer getPolicyTransformer() {
        return policyTransformer;
    }

    public void setPolicyTransformer(PolicyTransformer policyTransformer) {
        this.policyTransformer = policyTransformer;
    }

    protected OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    public void setNhinDelegate(OutboundDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
    }

    protected Optional<OutboundResponseProcessor> getAggregator() {
        return nhinProcessor;
    }

    public void setAggregator(Optional<OutboundResponseProcessor> processor) {
        Preconditions.checkNotNull(processor);
        nhinProcessor = processor;
    }

    protected Optional<OutboundResponseProcessor> getProcessor() {
        return nhinProcessor;
    }

    public void setProcessor(Optional<OutboundResponseProcessor> processor) {
        Preconditions.checkNotNull(processor);
        nhinProcessor = processor;
    }

    protected String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String name) {
        this.serviceName = name;
    }

}
