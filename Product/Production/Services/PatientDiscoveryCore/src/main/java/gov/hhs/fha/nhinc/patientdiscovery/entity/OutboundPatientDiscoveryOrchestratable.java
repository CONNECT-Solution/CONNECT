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
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 * Patient Discovery implementation of OutboundOrchestratableMessage
 *
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryOrchestratable implements OutboundOrchestratableMessage {

    private OutboundDelegate delegate = null;
    private Optional<OutboundResponseProcessor> processor = Optional.absent();
    private PolicyTransformer policyTransformer = null;
    private AssertionType assertion = null;
    private String serviceName = null;
    private NhinTargetSystemType target = null;
    private PRPAIN201305UV02 request = null;
    private PRPAIN201306UV02 response = null;
    private RespondingGatewayPRPAIN201306UV02ResponseType cumulativeResponse = null;

    public OutboundPatientDiscoveryOrchestratable() {
    }

    public OutboundPatientDiscoveryOrchestratable(OutboundDelegate d, Optional<OutboundResponseProcessor> p,
        PolicyTransformer pt, AssertionType a, String name, NhinTargetSystemType t,
        PRPAIN201305UV02 r) {
        Preconditions.checkNotNull(p);
        this.delegate = d;
        this.processor = p;
        this.policyTransformer = pt;
        this.assertion = a;
        this.serviceName = name;
        this.target = t;
        this.request = r;
    }

    @Override
    public OutboundDelegate getDelegate() {
        return delegate;
    }

    @Override
    public NhinAggregator getAggregator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<OutboundResponseProcessor> getResponseProcessor() {
        return processor;
    }

    @Override
    public PolicyTransformer getPolicyTransformer() {
        return policyTransformer;
    }

    @Override
    public AssertionType getAssertion() {
        return assertion;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public NhinTargetSystemType getTarget() {
        return target;
    }

    public PRPAIN201305UV02 getRequest() {
        return request;
    }

    @Override
    public boolean isPassthru() {
        return false;
    }

    public PRPAIN201306UV02 getResponse() {
        return response;
    }

    public void setResponse(PRPAIN201306UV02 r) {
        response = r;
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType getCumulativeResponse() {
        return cumulativeResponse;
    }

    public void setCumulativeResponse(RespondingGatewayPRPAIN201306UV02ResponseType r) {
        cumulativeResponse = r;
    }

}
