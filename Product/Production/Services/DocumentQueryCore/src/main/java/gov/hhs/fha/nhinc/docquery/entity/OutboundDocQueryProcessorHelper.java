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
package gov.hhs.fha.nhinc.docquery.entity;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import java.math.BigInteger;

import org.apache.log4j.Logger;

/**
 * Helper methods for DQ Processing to create a new cumulativeResponse object for a particular spec level and to
 * transform an individualResponse object from one spec to another.
 *
 * @author paul.eftis
 */
public class OutboundDocQueryProcessorHelper {

    private static final Logger LOG = Logger.getLogger(OutboundDocQueryProcessorHelper.class);


    /**
     * constructs a new OutboundDocQueryOrchestratable_a0 object with associated new cumulativeResponse.
     *
     * @param request DocQuery Orchestrated request received.
     * @return OutboundDocQueryOrchestratable_a0 if specLevel is a0.
     */
    //CHECKSTYLE:OFF
    public static OutboundDocQueryOrchestratable_a0 createNewCumulativeResponse_a0(
            OutboundDocQueryOrchestratable request) {
    //CHECKSTYLE:ON
        OutboundDocQueryOrchestratable_a0 cumulativeResponse = new OutboundDocQueryOrchestratable_a0(null, null, null,
                null, request.getAssertion(), request.getServiceName(), request.getTarget(), request.getRequest());

        // create new cumulativeResponse object
        AdhocQueryResponse newResponse = new AdhocQueryResponse();
        newResponse.setStartIndex(BigInteger.ZERO);
        newResponse.setTotalResultCount(BigInteger.ZERO);

        cumulativeResponse.setCumulativeResponse(newResponse);
        LOG.debug("EntityDocQueryProcessorHelper constructed initial a0 cumulativeResponse");
        return cumulativeResponse;
    }

    /**
     * constructs a new OutboundDocQueryOrchestratable_a1 object with associated new cumulativeResponse.
     *
     * @param request DocQuery Orchestrated request received.
     * @return OutboundDocQueryOrchestratable_a1 if specLevel is a1.
     */
    //CHECKSTYLE:OFF
    public static OutboundDocQueryOrchestratable_a1 createNewCumulativeResponse_a1(
            OutboundDocQueryOrchestratable request) {
    //CHECKSTYLE:ON
        OutboundDocQueryOrchestratable_a1 cumulativeResponse = new OutboundDocQueryOrchestratable_a1(null, null, null,
                null, request.getAssertion(), request.getServiceName(), request.getTarget(), request.getRequest());

        // create new cumulativeResponse object
        AdhocQueryResponse newResponse = new AdhocQueryResponse();
        newResponse.setStartIndex(BigInteger.ZERO);
        newResponse.setTotalResultCount(BigInteger.ZERO);

        cumulativeResponse.setCumulativeResponse(newResponse);
        LOG.debug("EntityDocQueryProcessorHelper constructed initial a1 cumulativeResponse");
        return cumulativeResponse;
    }

    /**
     * takes a response spec a1 and converts to response spec a0.
     *
     * @param original is spec a1
     * @return OutboundDocQueryOrchestratable_a0 with transformed a0 response
     */
    //CHECKSTYLE:OFF
    public static OutboundDocQueryOrchestratable_a0 transformResponse_ToA0(OutboundDocQueryOrchestratable original) {
    //CHECKSTYLE:ON
        // currently a0 is same as a1
        OutboundDocQueryOrchestratable_a0 responsea0 = new OutboundDocQueryOrchestratable_a0(null, null, null, null,
                original.getAssertion(), original.getServiceName(), original.getTarget(), original.getRequest());
        OutboundDocQueryOrchestratable_a1 originala1 = (OutboundDocQueryOrchestratable_a1) original;
        responsea0.setResponse(originala1.getResponse());
        return responsea0;
    }

    /**
     * takes a response spec a0 and converts to response spec a1.
     *
     * @param original is spec a0
     * @return OutboundDocQueryOrchestratable_a1 with transformed a1 response
     */
    //CHECKSTYLE:OFF
    public static OutboundDocQueryOrchestratable_a1 transformResponse_ToA1(OutboundDocQueryOrchestratable original) {
    //CHECKSTYLE:ON
        // currently a0 is same as a1
        OutboundDocQueryOrchestratable_a1 responsea1 = new OutboundDocQueryOrchestratable_a1(null, null, null, null,
                original.getAssertion(), original.getServiceName(), original.getTarget(), original.getRequest());
        OutboundDocQueryOrchestratable_a0 originala0 = (OutboundDocQueryOrchestratable_a0) original;
        responsea1.setResponse(originala0.getResponse());
        return responsea1;
    }
}
