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

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocRetrieveDeferredRequestErrorImpl {
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequestError(AdapterDocumentRetrieveDeferredRequestErrorSecuredType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);
        AdapterDocumentRetrieveDeferredRequestErrorType   request = new AdapterDocumentRetrieveDeferredRequestErrorType();

        request.setAssertion(assertion);
        request.setRetrieveDocumentSetRequest(body.getRetrieveDocumentSetRequest());
        request.setErrorMsg(body.getErrorMsg());

        return new AdapterDocRetrieveDeferredReqErrorOrchImpl().respondingGatewayCrossGatewayRetrieve(request, assertion, body.getErrorMsg());
    }

    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequestError(AdapterDocumentRetrieveDeferredRequestErrorType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, body.getAssertion());
        AdapterDocumentRetrieveDeferredRequestErrorType   request = new AdapterDocumentRetrieveDeferredRequestErrorType();

        request.setAssertion(assertion);
        request.setRetrieveDocumentSetRequest(body.getRetrieveDocumentSetRequest());
        request.setErrorMsg(body.getErrorMsg());

        return new AdapterDocRetrieveDeferredReqErrorOrchImpl().respondingGatewayCrossGatewayRetrieve(request, assertion, body.getErrorMsg());
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

}
