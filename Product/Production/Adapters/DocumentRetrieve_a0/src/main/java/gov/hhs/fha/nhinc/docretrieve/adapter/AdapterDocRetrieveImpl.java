/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

import org.apache.log4j.Logger;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;

/**
 * 
 * @author svalluripalli
 */
public class AdapterDocRetrieveImpl {
    private static final Logger LOG = Logger.getLogger(AdapterDocRetrieveImpl.class);

    /**
     * Perform Doc Retrieve.
     * 
     * @param bIsSecure TRUE if this is being called from a secure web service.
     * @param respondingGatewayCrossGatewayRetrieveRequest The information about the document that is being retrieved.
     * @param context The web service context information.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieveUnsecured(
            RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest,
            WebServiceContext context) {
        LOG.debug("Entering AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        AssertionType assertion = null;
        RetrieveDocumentSetRequestType request = null;
        if (respondingGatewayCrossGatewayRetrieveRequest != null) {
            request = respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest();
            assertion = respondingGatewayCrossGatewayRetrieveRequest.getAssertion();
        }

        RetrieveDocumentSetResponseType response = callOrchestrator(request, assertion);

        // Send response back to the initiating Gateway
        LOG.debug("Exiting AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return response;

    }

    /**
     * Perform Doc Retrieve.
     * 
     * @param body The information about the document that is being retrieved.
     * @param context The web service context information.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieveSecured(
            RetrieveDocumentSetRequestType body, WebServiceContext context) {
        LOG.debug("Entering AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        AssertionType assertion = null;
        if (context != null) {
            assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
        } else {
            assertion = new AssertionType();
        }

        RetrieveDocumentSetResponseType response = callOrchestrator(body, assertion);

        // Send response back to the initiating Gateway
        LOG.debug("Exiting AdapterDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

    /**
     * Calls the orchestrator.
     * 
     * @param body The message to be sent to the orchestrator.
     * @param assertion The assertion information.
     * @return The response from the orchestrator.
     */
    private RetrieveDocumentSetResponseType callOrchestrator(RetrieveDocumentSetRequestType body,
            AssertionType assertion) {
        AdapterDocRetrieveOrchImpl oOrchestrator = new AdapterDocRetrieveOrchImpl();
        RetrieveDocumentSetResponseType response = oOrchestrator.respondingGatewayCrossGatewayRetrieve(body, assertion);
        return response;
    }
}
