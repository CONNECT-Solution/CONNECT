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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 * @author bhumphrey
 *
 */
public class OutboundDocQueryAggregate implements Aggregate {
    
    
    private OutboundDocQueryOrchestratable request;
    
    private NhinTargetCommunitiesType targets;
    private AssertionType assertion;
    private AdhocQueryRequest adhocQueryRequest;
    private AggregationService fanoutService;
    
    public OutboundDocQueryAggregate() {
        this.request = new OutboundDocQueryOrchestratable();
        this.fanoutService = new AggregationService();
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.docquery.entity.Aggregate#aggregate(gov.hhs.fha.nhinc.docquery.entity.AggregationContext)
     */
    @Override
    public void aggregate(OutboundOrchestratable message) {
        request.getAggregator().aggregate(request, message);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.docquery.entity.Aggregate#getContexts()
     */
    @Override
    public Collection<OutboundOrchestratable> getMessages() {
       return fanoutService.createChildRequests(adhocQueryRequest, assertion, targets);
    }

    /**
     * @return the request
     */
    public OutboundDocQueryOrchestratable getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(OutboundDocQueryOrchestratable request) {
        this.request = request;
    }

    /**
     * @return the targets
     */
    public NhinTargetCommunitiesType getTargets() {
        return targets;
    }

    /**
     * @param targets the targets to set
     */
    public void setTargets(NhinTargetCommunitiesType targets) {
        this.targets = targets;
    }

    /**
     * @return the assertion
     */
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * @param assertion the assertion to set
     */
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     * @return the adhocQueryRequest
     */
    public AdhocQueryRequest getAdhocQueryRequest() {
        return adhocQueryRequest;
    }

    /**
     * @param adhocQueryRequest the adhocQueryRequest to set
     */
    public void setAdhocQueryRequest(AdhocQueryRequest adhocQueryRequest) {
        this.adhocQueryRequest = adhocQueryRequest;
    }

    /**
     * @return the fanoutService
     */
    public AggregationService getFanoutService() {
        return fanoutService;
    }

    /**
     * @param fanoutService the fanoutService to set
     */
    public void setFanoutService(AggregationService fanoutService) {
        this.fanoutService = fanoutService;
    }
    
    

}
