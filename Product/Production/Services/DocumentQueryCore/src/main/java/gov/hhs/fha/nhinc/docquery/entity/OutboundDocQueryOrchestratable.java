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
package gov.hhs.fha.nhinc.docquery.entity;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Doc Query implementation of OutboundOrchestratableMessage.
 *
 * @author paul.eftis
 */
public class OutboundDocQueryOrchestratable implements OutboundOrchestratableMessage {

    private OutboundDelegate delegate = null;
    private PolicyTransformer policyTransformer = null;
    private AssertionType assertion = null;
    private String serviceName = null;

    private NhinTargetSystemType target = null;
    private AdhocQueryRequest request = null;

    private AdhocQueryResponse response = null;
    private NhinAggregator aggregator = null;

    /**
     * Default Constructor.
     */
    public OutboundDocQueryOrchestratable() {
    }

    /**
     * @param delegate DocQuery Delegate.
     */
    public OutboundDocQueryOrchestratable(OutboundDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * Constructor.
     *
     * @param d Delegate.
     * @param pt PolicyTransformer.
     * @param a assertion.
     * @param name serviceName.
     * @param t target.
     * @param r AdhocQUery Request.
     */
    // CHECKSTYLE:OFF
    public OutboundDocQueryOrchestratable(OutboundDelegate d, PolicyTransformer pt,
        AssertionType a, String name, NhinTargetSystemType t, AdhocQueryRequest r) {
        // CHECKSTYLE:ON
        this.delegate = d;
        this.policyTransformer = pt;
        this.assertion = a;
        this.serviceName = name;
        this.target = t;
        this.request = r;
    }

    public OutboundDocQueryOrchestratable(OutboundDelegate d, AssertionType a, String name, NhinTargetSystemType t,
        AdhocQueryRequest r) {
        this(d, null, a, name, t, r);
    }

    public OutboundDocQueryOrchestratable(NhinAggregator agg, AssertionType a, String name, AdhocQueryRequest r) {
        // CHECKSTYLE:ON
        this(null, null, a, name, null, r);
        this.aggregator = agg;
    }

    /**
     * @param assertion Assertion received.
     */
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     * @param request AdhocQUery Request.
     */
    public void setRequest(AdhocQueryRequest request) {
        this.request = request;
    }

    /**
     * @param target NhinTarget info.
     */
    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    /**
     * @return AdhocQUery response.
     */
    public AdhocQueryResponse getResponse() {
        return response;
    }

    /**
     * @param r AdhocQuery Response.
     */
    public void setResponse(AdhocQueryResponse r) {
        response = r;
    }

    /**
     * @return delegate.
     */
    @Override
    public OutboundDelegate getDelegate() {
        return delegate;
    }

    /**
     * This is not used.
     *
     * @return Null.
     */
    @Override
    public NhinAggregator getAggregator() {
        return aggregator;
    }

    /**
     * @return absent processor. Does not exists for DQ.
     */
    @Override
    public Optional<OutboundResponseProcessor> getResponseProcessor() {
        return Optional.absent();
    }

    /**
     * @return policyTransformer to check policy.
     */
    @Override
    public PolicyTransformer getPolicyTransformer() {
        return policyTransformer;
    }

    /**
     * @return assertion.
     */
    @Override
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * @return ServiceName (DocumentQuery).
     */
    @Override
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return Nhin Target Info.
     */
    public NhinTargetSystemType getTarget() {
        return target;
    }

    /**
     * @return AdhocQuery Request.
     */
    public AdhocQueryRequest getRequest() {
        return request;
    }

    /**
     * @return false if not in passthru mode.
     */
    @Override
    public boolean isPassthru() {
        return false;
    }

}
