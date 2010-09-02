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
package gov.hhs.fha.nhinc.docquery.adapter;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocQueryImpl {

    private Log log = null;
    private static final String ERROR_CODE_CONTEXT = AdapterDocQueryImpl.class.getName();
    private static final String ERROR_VALUE = "Input has null value";
    private static final String ERROR_SEVERITY = "Error";

    public AdapterDocQueryImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, WebServiceContext context) {
        log.debug("Enter AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        AssertionType assertion = getAssertion(context, null);

        AdhocQueryResponse response = new AdapterDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(request, assertion);

        log.debug("End AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        return response;
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request, WebServiceContext context) {
        log.debug("Enter AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        AssertionType assertion = getAssertion(context, request.getAssertion());

        AdhocQueryResponse response = new AdapterDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(request.getAdhocQueryRequest(), assertion);

        log.debug("End AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        return response;
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
