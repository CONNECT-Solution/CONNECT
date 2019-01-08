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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.services.DocumentRetrieveService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.entity.proxy.EntityDocRetrieveProxyWebServiceUnsecuredImpl;
import gov.hhs.fha.nhinc.docretrieve.messaging.builder.DocumentRetrieveRequestBuilder;
import gov.hhs.fha.nhinc.docretrieve.messaging.builder.impl.DocumentRetrieveRequestBuilderImpl;
import gov.hhs.fha.nhinc.docretrieve.messaging.director.impl.DocumentRetrieveMessageDirectorImpl;
import gov.hhs.fha.nhinc.docretrieve.model.DocumentRetrieve;
import gov.hhs.fha.nhinc.docretrieve.model.DocumentRetrieveResults;
import gov.hhs.fha.nhinc.docretrieve.model.builder.DocumentRetrieveResultsModelBuilder;
import gov.hhs.fha.nhinc.docretrieve.model.builder.impl.DocumentRetrieveResultsModelBuilderImpl;
import gov.hhs.fha.nhinc.messaging.builder.NhinTargetCommunitiesBuilder;
import gov.hhs.fha.nhinc.messaging.builder.impl.NhinTargetCommunitiesBuilderImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * DocumentRetrieveServiceImpl call CONNECT DocumentRetrieve Unsecured Entity interface and return the
 * DocumentRetrieveResults having HCID,DocumentUniqueId,RepositoryId,Document and Document MimeType back to the UI. This
 * class wires up DocumentRetrieveRequest Builder and set DocumentRterieve Entity Request with Assertion,
 * TragetCommunities and the DocumentRetrieve Request built and those are all performed within various private methods.
 *
 *
 * @author achidamb
 */
public class DocumentRetrieveServiceImpl implements DocumentRetrieveService {

    private DocumentRetrieveMessageDirectorImpl messageDirector;
    private DocumentRetrieveResultsModelBuilder docRetrieveResults;

    @Override
    public DocumentRetrieveResults retrieveDocuments(DocumentRetrieve documentModel, AssertionType assertion) {
        RetrieveDocumentSetResponseType response;
        EntityDocRetrieveProxyWebServiceUnsecuredImpl retrieveResults = new EntityDocRetrieveProxyWebServiceUnsecuredImpl();
        messageDirector = setMessageDirector(documentModel, assertion);
        response = retrieveResults.respondingGatewayCrossGatewayRetrieve(
            messageDirector.getMessage().getRetrieveDocumentSetRequest(),
            messageDirector.getMessage().getAssertion(), messageDirector.getMessage().getNhinTargetCommunities());
        docRetrieveResults = getResultsFromResponse(response);
        return docRetrieveResults.getDocumentRetrieveResultsModel();
    }

    private DocumentRetrieveMessageDirectorImpl setMessageDirector(DocumentRetrieve documentModel, AssertionType assertion) {
        messageDirector = new DocumentRetrieveMessageDirectorImpl();
        messageDirector.setAssertion(assertion);
        messageDirector.setTargetCommunitiesBuilder(setNhinTarget(documentModel));
        messageDirector.setDocumentRetrieveBuilder(createRetrieveDocumentSetRequest(documentModel));
        messageDirector.build();
        return messageDirector;
    }

    private static DocumentRetrieveRequestBuilder createRetrieveDocumentSetRequest(DocumentRetrieve documentModel) {
        DocumentRetrieveRequestBuilder request = new DocumentRetrieveRequestBuilderImpl();
        request.setDocumentId(documentModel.getDocumentId());
        request.setHCID(documentModel.getHCID());
        request.setRepositoryId(documentModel.getRepositoryId());
        request.build();
        return request;
    }

    private static NhinTargetCommunitiesBuilder setNhinTarget(DocumentRetrieve documentModel) {
        NhinTargetCommunitiesBuilder targetCommunitiesBuilder = new NhinTargetCommunitiesBuilderImpl();
        targetCommunitiesBuilder.setTarget(documentModel.getHCID());
        return targetCommunitiesBuilder;
    }

    private static DocumentRetrieveResultsModelBuilder getResultsFromResponse(
        RetrieveDocumentSetResponseType response) {
        DocumentRetrieveResultsModelBuilder responseResults = new DocumentRetrieveResultsModelBuilderImpl();
        responseResults.setRetrieveDocumentSetResponseType(response);
        responseResults.build();
        return responseResults;
    }

}
