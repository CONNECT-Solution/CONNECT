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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;

/**
 * The implementation for passthru service for unsecured Deferred Doc Query Request.
 *
 * @author Les westberg
 */
public class PassthruDocQueryDeferredRequestUnsecuredImpl
{
    //Logger

    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestUnsecuredImpl.class);

    /**
     * Delegates method call to Implementation class in the core library
     *
     * @param crossGatewayQueryRequest
     * @return
     */
    public DocQueryAcknowledgementType crossGatewayQueryRequest(
            RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest,
            WebServiceContext context)
    {

        getLogger().debug("Beginning of PassthruDocQueryDeferredRequestUnsecured.crossGatewayQueryRequest()");


        AssertionType assertion = crossGatewayQueryRequest.getAssertion();

        if (assertion != null)
        {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        // Fwd request to orchestrator
        DocQueryAcknowledgementType ackResponse = getPassthruDocQueryDeferredRequestOrchImpl().crossGatewayQueryRequest(crossGatewayQueryRequest.getAdhocQueryRequest(),
                assertion, crossGatewayQueryRequest.getNhinTargetSystem());

        getLogger().debug("End of PassthruDocQueryDeferredRequestUnsecured.crossGatewayQueryRequest()");

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
}
