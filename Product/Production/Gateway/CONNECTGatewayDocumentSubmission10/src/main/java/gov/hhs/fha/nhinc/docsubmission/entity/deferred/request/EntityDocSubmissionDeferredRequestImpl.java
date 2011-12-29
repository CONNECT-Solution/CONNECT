/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 *
 * @author Neil Webb
 */
public class EntityDocSubmissionDeferredRequestImpl {

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, null);

        XDRAcknowledgementType response = new EntityDocSubmissionDeferredRequestOrchImpl().provideAndRegisterDocumentSetBAsyncRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetCommunities(), provideAndRegisterRequestRequest.getUrl());
        
        return response;
    }

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, provideAndRegisterAsyncReqRequest.getAssertion());

        XDRAcknowledgementType response = new EntityDocSubmissionDeferredRequestOrchImpl().provideAndRegisterDocumentSetBAsyncRequest(provideAndRegisterAsyncReqRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterAsyncReqRequest.getNhinTargetCommunities(), provideAndRegisterAsyncReqRequest.getUrl());

        return response;
    }

   protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
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
