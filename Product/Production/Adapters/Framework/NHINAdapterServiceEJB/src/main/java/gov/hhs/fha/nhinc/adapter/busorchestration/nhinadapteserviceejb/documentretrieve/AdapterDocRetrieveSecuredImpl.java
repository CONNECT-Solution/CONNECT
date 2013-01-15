/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
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
package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentretrieve;

import gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.AdapterConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.docmgr.ArchiveDocumentRequestType;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implement document retrieve - All documents should actually be in the dynamic document archive
 *
 * @author Jerry Goodnough
 */
/**
 * 
 * @author mflynn02
 */
public class AdapterDocRetrieveSecuredImpl {

    private static AdapterDocRetrieveSecuredImpl singlton = null;
    private static Log log = LogFactory.getLog(
        AdapterDocRetrieveSecuredImpl.class);

    static {
        singlton = new AdapterDocRetrieveSecuredImpl();
    }

    /**
     * Provides public access to the singletion.
     *
     * @return the Singlton
     */
    public static AdapterDocRetrieveSecuredImpl getInstance() {
        return singlton;
    }

    private AdapterDocRetrieveSecuredImpl() {
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, WebServiceContext context) {
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response;
        request.setRetrieveDocumentSetRequest(body);
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        request.setAssertion(assertion);
        response = respondingGatewayCrossGatewayRetrieve(request);
        return response;
    }

    /**
     * The core implementation of the DocumentRetrieve request.
     * The request is vectored to the Document Manager / Dynamic Document Archive
     * to retrieve the document.
     *
     * In additionall documents retrieved are marked to retention, since usage
     * of this interface is considered public publication.
     *
     * @param respondingGatewayCrossGatewayRetrieveRequest Request for the docuents to retrieve
     * @return requested documents if available.
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        log.debug("Entering Document Retrieve");

        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType result = null;
        String docMgrEndpoint = getDocMgrEndpoint();
        try {
            try { // Fetch the requested documents
                ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
                ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
                if (docMgrEndpoint != null && !docMgrEndpoint.isEmpty()) {
                    //Use the BOS Endpoint
                    ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, docMgrEndpoint);
                }
                log.debug("Calling Document Manager to retrieve from the Archive");
                //result = port.documentManagerRetrieveDynamicDocument(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
                result = port.documentManagerRetrieveDocument(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());

                log.debug("Call to Document Manager to retrieve from the Archive complete");
            } catch (Exception ex) {
                log.error("Exception During DocumentManager Retrieve from Archivce", ex);
                throw ex;

            }

            if (result.getDocumentResponse().size() > 0) {
                //In an ideal world the next call would happen in another thread or as
                //as result in JMS queue or the like. Another option would be be to
                //Have the document manager hanlde it as a side effect of the prior
                //call.

                try {
                    // Ask all dynamic documents to be retained in the archive
                    ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
                    ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
                    if (docMgrEndpoint != null && !docMgrEndpoint.isEmpty()) {
                        //Use the BOS Endpoint
                        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, docMgrEndpoint);
                    }
                    log.debug("Call to Document Manager to Mark the Documents to be saved");

                    //old request
                    //oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType results = port.documentManagerArchiveDynamicDocument(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
                    ArchiveDocumentRequestType adrt = new ArchiveDocumentRequestType();
                    adrt.setDocumentUniqueId(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest().getDocumentRequest().get(0).getDocumentUniqueId());
                    adrt.setHomeCommunityId(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest().getDocumentRequest().get(0).getHomeCommunityId());
                    adrt.setRepositoryUniqueId(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest().getDocumentRequest().get(0).getRepositoryUniqueId());

                    //Call the method, but ignore the result.
                    port.documentManagerArchiveDocument(adrt);
                    log.debug("Called to Document Manager to mark the documents as accessed (and thus saved permanently.");
                } catch (Exception ex) {
                    log.error("Exception during Dynamic Document Retrieve - Mark to retention", ex);
                    //Question - If an exception here fatal? In the best possible world the request would be
                    //handled in a independant mamnner and not be considered blocking.
                    //For now if this fails, we should note it, and let the user get the requested
                    //document, since we assume it is clinically important.
                    //In full production it may be better to use the JMS method or at
                    //the very least insure that the document request is dumped to a monitored log
                }
            }
        } catch (Exception ex) {
            log.error("General Exception during Retrieve Documents call, details above", ex);
            throw new WebServiceException("Exception during DocumentRetrieve", ex);
        }
        log.debug("Document Retieve is Complete");

        return result;
    }

    /**
     * Get the DoumentManagerEndpoint
     * @return The Endpoint or null if not mapped
     */
    private String getDocMgrEndpoint() {

        String docMgrEndpoint = "";

        docMgrEndpoint = AdapterConstants.DOCUMENT_MANAGER_ENDPOINT;

        return docMgrEndpoint;
    }
}
