/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docretrieve.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docrepository.adapter.proxy.AdapterComponentDocRepositoryProxy;
import gov.hhs.fha.nhinc.docrepository.adapter.proxy.AdapterComponentDocRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.MessageGenerator;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxyObjectFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author westberg
 */
public class AdapterDocRetrieveOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterDocRetrieveOrchImpl.class);

    /**
     *
     * @param body
     * @param assertion
     * @return RetrieveDocumentSetResponseType
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body,
        AssertionType assertion) {
        LOG.trace("Enter AdapterDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        RetrieveDocumentSetResponseType response;

        try {
            AdapterComponentDocRepositoryProxy proxy = new AdapterComponentDocRepositoryProxyObjectFactory()
                .getAdapterDocumentRepositoryProxy();

            response = proxy.retrieveDocument(body, assertion);
            response = callRedactionEngine(body, response, assertion);
        } catch (Exception e) {
            String errorMsg = "Error processing an adapter document retrieve message: " + e.getMessage();
            LOG.error(errorMsg, e);
            response = MessageGenerator.getInstance().createRegistryResponseError(errorMsg);
        }
        LOG.trace("Leaving AdapterDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

    protected RetrieveDocumentSetResponseType callRedactionEngine(RetrieveDocumentSetRequestType retrieveRequest,
        RetrieveDocumentSetResponseType retrieveResponse, AssertionType assertion) {
        RetrieveDocumentSetResponseType response = null;
        if (retrieveResponse == null) {
            LOG.warn("Did not call redaction engine because the retrieve response was null.");
        } else {
            response = getRedactionEngineProxy().filterRetrieveDocumentSetResults(retrieveRequest, retrieveResponse,
                assertion);
        }
        return response;
    }

    protected AdapterRedactionEngineProxy getRedactionEngineProxy() {
        return new AdapterRedactionEngineProxyObjectFactory().getRedactionEngineProxy();
    }
}
