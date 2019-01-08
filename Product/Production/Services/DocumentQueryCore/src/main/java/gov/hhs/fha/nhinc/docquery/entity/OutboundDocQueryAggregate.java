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

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import java.util.Collection;

/**
 * @author bhumphrey
 *
 */
public class OutboundDocQueryAggregate implements Aggregate {

    private OutboundDocQueryOrchestratable request;

    private Collection<OutboundOrchestratable> aggregateRequests;

    public OutboundDocQueryAggregate() {
        this.request = new OutboundDocQueryOrchestratable();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.docquery.entity.Aggregate#aggregate(gov.hhs.fha.nhinc.docquery.entity.AggregationContext)
     */
    @Override
    public void aggregate(OutboundOrchestratable message) {
        request.getAggregator().aggregate(request, message);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.docquery.entity.Aggregate#getContexts()
     */
    @Override
    public Collection<OutboundOrchestratable> getAggregateRequests() {
        return aggregateRequests;
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
     * @param messages the messages to set
     */
    public void setAggregateRequests(Collection<OutboundOrchestratable> aggregateResponses) {
        this.aggregateRequests = aggregateResponses;
    }

}
