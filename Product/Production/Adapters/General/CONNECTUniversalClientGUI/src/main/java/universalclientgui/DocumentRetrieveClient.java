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
package universalclientgui;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docretrieve.entity.proxy.EntityDocRetrieveProxyWebServiceUnsecuredImpl;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author patlollav
 */
public class DocumentRetrieveClient {
    private static Log log = LogFactory.getLog(DocumentRetrieveClient.class);;
    private static final String SERVICE_NAME = NhincConstants.ENTITY_DOC_RETRIEVE_PROXY_SERVICE_NAME;

    public String retriveDocument(DocumentInformation documentInformation) {
        try {
            String url = getUrl();
            if (NullChecker.isNotNullish(url)) {
                RespondingGatewayCrossGatewayRetrieveRequestType request = createCrossGatewayRetrieveRequest(documentInformation);

                EntityDocRetrieveProxyWebServiceUnsecuredImpl instance = new EntityDocRetrieveProxyWebServiceUnsecuredImpl();
                RetrieveDocumentSetResponseType response = instance.respondingGatewayCrossGatewayRetrieve(
                        request.getRetrieveDocumentSetRequest(), request.getAssertion(),
                        request.getNhinTargetCommunities());

                return extractDocument(response);
            } else {
                log.error("Error getting URL for " + SERVICE_NAME);
            }
        } catch (Exception ex) {
            log.error("Error calling respondingGatewayCrossGatewayRetrieve: " + ex.getMessage(), ex);
        }
        return null;
    }

    private String extractDocument(RetrieveDocumentSetResponseType response) {
        String documentInXmlFormat = null;

        if (response == null) {
            return null;
        }
        List<DocumentResponse> documentResponseList = response.getDocumentResponse();

        if (documentResponseList != null && documentResponseList.size() > 0) {
            DocumentResponse documentResponse = documentResponseList.get(0);

            if (documentResponse != null && documentResponse.getDocument() != null) {
                try {
                    byte[] rawData = LargeFileUtils.getInstance().convertToBytes(documentResponse.getDocument());
                    documentInXmlFormat = new String(rawData);
                } catch (IOException ioe) {
                    log.error("Failed to retrieve document.", ioe);
                }
            }
        }

        return documentInXmlFormat;
    }

    /**
     * 
     * @return
     */
    private RespondingGatewayCrossGatewayRetrieveRequestType createCrossGatewayRetrieveRequest(
            DocumentInformation documentInformation) {

        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();

        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = new RetrieveDocumentSetRequestType();

        DocumentRequest docRequest = new DocumentRequest();
        // docRequest.setHomeCommunityId("urn:oid:2.2");
        // docRequest.setRepositoryUniqueId("1");
        docRequest.setHomeCommunityId(documentInformation.getHomeCommunityID());
        docRequest.setRepositoryUniqueId(documentInformation.getRepositoryUniqueID());
        docRequest.setDocumentUniqueId(documentInformation.getDocumentID());

        retrieveDocumentSetRequest.getDocumentRequest().add(docRequest);

        request.setRetrieveDocumentSetRequest(retrieveDocumentSetRequest);

        // Add Assertion
        AssertionCreator assertionCreator = new AssertionCreator();
        request.setAssertion(assertionCreator.createAssertion());

        request.setNhinTargetCommunities(createTargetCommunities(documentInformation.getHomeCommunityID()));
        return request;
    }

    private NhinTargetCommunitiesType createTargetCommunities(String homeCommunityId) {
        NhinTargetCommunitiesType result = new NhinTargetCommunitiesType();
        NhinTargetCommunityType community = new NhinTargetCommunityType();

        HomeCommunityType hc = new HomeCommunityType();
        hc.setHomeCommunityId(homeCommunityId);

        community.setHomeCommunity(hc);

        result.getNhinTargetCommunity().add(community);

        return result;

    }

    protected String getUrl() throws ConnectionManagerException {
        return ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(SERVICE_NAME);
    }

}
