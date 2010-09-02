/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterXDRSecuredRequestErrorImpl
{
    private Log log = null;

    public AdapterXDRSecuredRequestErrorImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType body, WebServiceContext context)
    {
        log.debug("Begin AdapterXDRSecuredRequestErrorImpl.provideAndRegisterDocumentSetBRequestError(secured)");
        XDRAcknowledgementType response = null;

        ProvideAndRegisterDocumentSetRequestType request = null;
        String errorMessage = null;
        AssertionType assertion = null;
        if (body != null)
        {
            request = body.getProvideAndRegisterDocumentSetRequest();
            errorMessage = body.getErrorMsg();
        }
        assertion = getAssertion(context, assertion);
        response = provideAndRegisterDocumentSetBRequestError(request, errorMessage, assertion);

        log.debug("End AdapterXDRSecuredRequestErrorImpl.provideAndRegisterDocumentSetBRequestError(secured)");
        return response;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(AdapterProvideAndRegisterDocumentSetRequestErrorType body, WebServiceContext context)
    {
        log.debug("Begin AdapterXDRSecuredRequestErrorImpl.provideAndRegisterDocumentSetBRequestError(unsecured)");
        XDRAcknowledgementType response = null;

        ProvideAndRegisterDocumentSetRequestType request = null;
        String errorMessage = null;
        AssertionType assertion = null;
        if (body != null)
        {
            request = body.getProvideAndRegisterDocumentSetRequest();
            errorMessage = body.getErrorMsg();
            assertion = body.getAssertion();
        }
        assertion = getAssertion(context, assertion);
        response = provideAndRegisterDocumentSetBRequestError(request, errorMessage, assertion);

        log.debug("End AdapterXDRSecuredRequestErrorImpl.provideAndRegisterDocumentSetBRequestError(unsecured)");
        return response;
    }

    protected AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn)
    {
        AssertionType assertion = null;
        if (oAssertionIn == null)
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null)
        {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }

    protected XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(ProvideAndRegisterDocumentSetRequestType request, String errorMessage, AssertionType assertion)
    {
        return new AdapterDocSubmissionDeferredRequestErrorOrchImpl().provideAndRegisterDocumentSetBRequestError(request, errorMessage, assertion);
    }
}
