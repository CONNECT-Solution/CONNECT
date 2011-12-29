/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.entity.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.List;

/**
 *
 * @author Neil Webb
 */
public class EntityDocSubmissionDeferredResponseImpl
{

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest, WebServiceContext context)
    {
        AssertionType assertion = extractAssertionFromContext(context, null);

        XDRAcknowledgementType response = new EntityDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBAsyncResponse(provideAndRegisterDocumentSetSecuredResponseRequest.getRegistryResponse(), assertion, provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetCommunities());

        return response;
    }

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetAsyncRespRequest, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, provideAndRegisterDocumentSetAsyncRespRequest.getAssertion());

        XDRAcknowledgementType response = new EntityDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBAsyncResponse(provideAndRegisterDocumentSetAsyncRespRequest.getRegistryResponse(), assertion, provideAndRegisterDocumentSetAsyncRespRequest.getNhinTargetCommunities());

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
        // Extract the RelatesTo value list and place it in the AssertionClass
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
                assertion.getRelatesToList().addAll(relatesToList);
            }
        }

        return assertion;
    }

    
}
