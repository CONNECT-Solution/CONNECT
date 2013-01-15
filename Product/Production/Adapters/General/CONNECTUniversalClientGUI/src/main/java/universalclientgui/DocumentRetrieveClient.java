/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import gov.hhs.fha.nhinc.saml.creation.SAMLAssertionCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.HashMap;
/**
 * 
 * @author patlollav
 */
public class DocumentRetrieveClient {
    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_LOCAL_HOME_COMMUNITY = "localHomeCommunityId";
    private static Log log = LogFactory.getLog(DocumentRetrieveClient.class);
    private static EntityDocRetrieve service = new EntityDocRetrieve();

    public String retriveDocument(DocumentInformation documentInformation){

        EntityDocRetrievePortType port = getPort(getEntityDocumentRetrieveProxyAddress());

                RespondingGatewayCrossGatewayRetrieveRequestType request = createCrossGatewayRetrieveRequest(documentInformation);


        RetrieveDocumentSetResponseType response = port.respondingGatewayCrossGatewayRetrieve(request);

        return extractDocument(response);
    }

    private String localPatientId;

    public void setLocalPatientId(String id)
    {
        this.localPatientId = id;
    }

    private String extractDocument(RetrieveDocumentSetResponseType response) {
        String documentInXmlFormat = null;

        if (response == null)
        {
            return null;
        }
        List<DocumentResponse> documentResponseList = response.getDocumentResponse();

        if (documentResponseList != null && documentResponseList.size() > 0)
        {
            DocumentResponse documentResponse = documentResponseList.get(0);

            if (documentResponse != null && documentResponse.getDocument() != null)
            {
                documentInXmlFormat = new String(documentResponse.getDocument());
            }
        }

        // log.debug("Document: " + documentInXmlFormat);

        // convertXMLToHTML(documentInXmlFormat, null);

        return documentInXmlFormat;
    }

    /**
     * 
     * @return
     */
    private RespondingGatewayCrossGatewayRetrieveRequestType createCrossGatewayRetrieveRequest(DocumentInformation documentInformation){
        
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

        // Add Assertions
        AuthenticatedUserInfo authenticationInfo = Page1.getAuthenticationInfo();
        authenticationInfo.updateAssertedPatientId(localPatientId);
        AssertionType assertions = authenticationInfo.getAssertions();
        request.setAssertion(assertions);

        request.setNhinTargetCommunities(createTargetCommunities(documentInformation.getHomeCommunityID()));
        return request;
    }

    private NhinTargetCommunitiesType createTargetCommunities(String homeCommunityId)
    {
        NhinTargetCommunitiesType result = new NhinTargetCommunitiesType();
        NhinTargetCommunityType community = new NhinTargetCommunityType();

        HomeCommunityType hc = new HomeCommunityType();
        hc.setHomeCommunityId(homeCommunityId);

        community.setHomeCommunity(hc);

        result.getNhinTargetCommunity().add(community);

        return result;

    }
    /**
     *
     * @return
     */
    private String getEntityDocumentRetrieveProxyAddress() {
        String endpointAddress = null;

        try {
            // Lookup home community id
            String homeCommunity = getHomeCommunityId();
            // Get endpoint url
        //    endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_DOC_RETRIEVE_PROXY_SERVICE_NAME);
            endpointAddress = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_DOC_RETRIEVE_PROXY_SERVICE_NAME);
            log.debug("Doc Retrive endpoint address: " + endpointAddress);
        } catch (PropertyAccessException pae) {
            log.error("Exception encountered retrieving the local home community: " + pae.getMessage(), pae);
        } catch (ConnectionManagerException cme) {
            log.error("Exception encountered retrieving the entity doc query connection endpoint address: " + cme.getMessage(), cme);
        }
        return endpointAddress;
    }

    /**
     *
     * @param url
     * @return
     */
    private EntityDocRetrievePortType getPort(String url) {
        if (service == null) {
            service = new EntityDocRetrieve();
        }

        EntityDocRetrievePortType port = service.getEntityDocRetrievePortSoap();

        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    /**
     * Retrieve the local home community id
     *
     * @return Local home community id
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    private String getHomeCommunityId() throws PropertyAccessException {
        return PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_LOCAL_HOME_COMMUNITY);
    }



}
