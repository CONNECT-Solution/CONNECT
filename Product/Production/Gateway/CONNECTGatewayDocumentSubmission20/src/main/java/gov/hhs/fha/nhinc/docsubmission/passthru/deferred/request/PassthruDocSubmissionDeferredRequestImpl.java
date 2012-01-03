/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocSubmissionDeferredRequestImpl
{
    

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, WebServiceContext context)
    {
        AssertionType assertion = extractAssertionFromContext(context, null);

        return new PassthruDocSubmissionDeferredRequestOrchImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetSystem());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterRequestRequest, WebServiceContext context)
    {
        AssertionType assertion = extractAssertionFromContext(context, provideAndRegisterRequestRequest.getAssertion());

        return new PassthruDocSubmissionDeferredRequestOrchImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetSystem());
        
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