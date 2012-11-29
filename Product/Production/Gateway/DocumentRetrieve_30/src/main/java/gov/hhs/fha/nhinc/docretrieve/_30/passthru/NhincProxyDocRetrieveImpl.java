/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve._30.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.passthru.NhincProxyDocRetrieveOrchImpl;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveImpl extends BaseService {

    private NhincProxyDocRetrieveOrchImpl orchImpl;
    
    private static Log log = LogFactory.getLog(NhincProxyDocRetrieveImpl.class);
    
    public NhincProxyDocRetrieveImpl(NhincProxyDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        log.debug("NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve(secured)");

        RetrieveDocumentSetResponseType response = null;

        if (body != null) {
            AssertionType assertion = getAssertion(context, null);
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion,
                    body.getNhinTargetSystem());
        }

        return response;
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RespondingGatewayCrossGatewayRetrieveRequestType body, WebServiceContext context) {
        log.debug("NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve(unsecured)");

        RetrieveDocumentSetResponseType response = null;

        if (body != null) {
            AssertionType assertion = getAssertion(context, body.getAssertion());
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion,
                    body.getNhinTargetSystem());
        }
        return response;
    }
}
