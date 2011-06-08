/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.passthru;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocQueryImpl {

    private static Log log = LogFactory.getLog(NhincProxyDocQueryImpl.class);

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType body, WebServiceContext context) {
        log.debug("NhincProxyDocQueryImpl.respondingGatewayCrossGatewayQuery(unsecured)");

        return new PassthruDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), body.getAssertion(), body.getNhinTargetSystem());
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType body, WebServiceContext context) {
        log.debug("NhincProxyDocQueryImpl.respondingGatewayCrossGatewayQuery(secured)");

        AssertionType assertion = getAssertion (context, null);
        
        return new PassthruDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), assertion, body.getNhinTargetSystem());
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
