/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveImpl
{

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyDocRetrieveImpl.class);

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context)
    {
        RetrieveDocumentSetResponseType response = null;

        if (body != null)
        {
            AssertionType assertion = getAssertion(context, null);
            NhincProxyDocRetrieveOrchImpl orchImpl = new NhincProxyDocRetrieveOrchImpl();
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion, body.getNhinTargetSystem());
        }

        return response;
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveRequestType body, WebServiceContext context)
    {
        RetrieveDocumentSetResponseType response = null;

        if (body != null)
        {
            AssertionType assertion = getAssertion(context, body.getAssertion());
            NhincProxyDocRetrieveOrchImpl orchImpl = new NhincProxyDocRetrieveOrchImpl();
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion, body.getNhinTargetSystem());
        }
        return response;
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn)
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
}
