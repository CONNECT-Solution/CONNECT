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
package gov.hhs.fha.nhinc.docquery.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;



/**
 * The implementation for passthru service for secured Deferred Doc Query Request.
 *
 * @author Les Westberg
 */
public class PassthruDocQueryDeferredRequestSecuredImpl
{
    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestSecuredImpl.class);

    public DocQueryAcknowledgementType crossGatewayQueryRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType crossGatewayQueryRequest, WebServiceContext context)
    {
        getLogger().debug("Beginning of PassthruDocQueryDeferredRequestSecured.crossGatewayQueryRequest()");

        AssertionType assertion = extractAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null)
        {
            assertion.setMessageId(getAsyncMessageIdExtractor().GetAsyncMessageId(context));
        }

        // Fwd request to orchestrator
        DocQueryAcknowledgementType ackResponse = getPassthruDocQueryDeferredRequestOrchImpl().crossGatewayQueryRequest(crossGatewayQueryRequest.getAdhocQueryRequest(),
                assertion, crossGatewayQueryRequest.getNhinTargetSystem());

        getLogger().debug("End of PassthruDocQueryDeferredRequestSecured.crossGatewayQueryRequest()");

        return ackResponse;
    }

    /**
     * Returns the static logger for this class
     * @return
     */
    protected Log getLogger()
    {
        return logger;
    }

    /**
     * Implementation class for
     *
     * @return
     */
    protected PassthruDocQueryDeferredRequestOrchImpl getPassthruDocQueryDeferredRequestOrchImpl()
    {
        return new PassthruDocQueryDeferredRequestOrchImpl();
    }

    protected AssertionType extractAssertion(WebServiceContext context)
    {
        return SamlTokenExtractor.GetAssertion(context);
    }

    protected AsyncMessageIdExtractor getAsyncMessageIdExtractor()
    {
        return new AsyncMessageIdExtractor();
    }
}
