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
package gov.hhs.fha.nhinc.docquery.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxy;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxyObjectFactory;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterDocQueryOrchImpl.class);
    private static final String ERROR_CODE_CONTEXT = AdapterDocQueryOrchImpl.class.getName();
    private static final String ERROR_VALUE = "Input has null value";


    /**
     *
     * @param request The AdhocQUeryRequest message.
     * @param assertion Assertion received.
     * @return AdhocQueryResponse The AdhocQuery response received.
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion) {
        LOG.debug("Enter AdapterDocQueryOrchImpl.respondingGatewayCrossGatewayQuery()");
        AdhocQueryResponse response = null;
        try {
            if (request != null) {
                AdapterComponentDocRegistryProxyObjectFactory objFactory = new AdapterComponentDocRegistryProxyObjectFactory();
                AdapterComponentDocRegistryProxy registryProxy = objFactory.getAdapterComponentDocRegistryProxy();
                AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
                adhocQueryRequest.setAdhocQuery(request.getAdhocQuery());
                adhocQueryRequest.setResponseOption(request.getResponseOption());
                adhocQueryRequest.setComment(request.getComment());
                adhocQueryRequest.setFederated(request.isFederated());
                adhocQueryRequest.setFederation(request.getFederation());
                adhocQueryRequest.setId(request.getId());
                adhocQueryRequest.setMaxResults(request.getMaxResults());
                adhocQueryRequest.setRequestSlotList(request.getRequestSlotList());
                adhocQueryRequest.setStartIndex(request.getStartIndex());
                response = registryProxy.registryStoredQuery(request, assertion);
            } else {
                throw new IllegalArgumentException("Request must be provided.");
            }
        } catch (Exception e) {
            RegistryErrorList errorList = new RegistryErrorList();
            response = new AdhocQueryResponse();
            response.setRegistryObjectList(new RegistryObjectListType());
            response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);

            RegistryError err = new RegistryError();
            errorList.getRegistryError().add(err);
            response.setRegistryErrorList(errorList);
            err.setValue(ERROR_VALUE);
            err.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
            err.setCodeContext(ERROR_CODE_CONTEXT);
            err.setErrorCode(DocumentConstants.XDS_ERRORCODE_REPOSITORY_ERROR);
            throw new ErrorEventException(e, response, "Unable to query document registry.");
        }
        LOG.debug("End AdapterDocQueryOrchImpl.respondingGatewayCrossGatewayQuery()");
        return response;

    }

}
