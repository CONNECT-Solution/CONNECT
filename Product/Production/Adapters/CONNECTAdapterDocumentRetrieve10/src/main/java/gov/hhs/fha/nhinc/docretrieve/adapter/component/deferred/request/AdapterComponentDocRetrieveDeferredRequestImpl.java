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

package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.AdapterDocRetrieveDeferredReqOrchImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
public class AdapterComponentDocRetrieveDeferredRequestImpl {
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, crossGatewayRetrieveRequest.getAssertion());

        return new AdapterComponentDocRetrieveDeferredReqOrchImpl().respondingGatewayCrossGatewayRetrieve(crossGatewayRetrieveRequest.getRetrieveDocumentSetRequest(), assertion);
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = oAssertionIn;
        
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

}
