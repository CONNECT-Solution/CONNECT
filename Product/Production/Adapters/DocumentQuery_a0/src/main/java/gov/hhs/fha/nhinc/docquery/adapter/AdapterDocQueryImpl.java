/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;

import javax.xml.ws.WebServiceContext;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.log4j.Logger;

/**
 * 
 * @author svalluripalli
 */
public class AdapterDocQueryImpl extends BaseService {

    private static final Logger LOG = Logger.getLogger(AdapterDocQueryImpl.class);

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, WebServiceContext context) {
        LOG.debug("Enter AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        AssertionType assertion = getAssertion(context, null);

        AdhocQueryResponse response = new AdapterDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(request,
                assertion);

        LOG.debug("End AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        return response;
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request,
            WebServiceContext context) {
        LOG.debug("Enter AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        AssertionType assertion = getAssertion(context, request.getAssertion());

        AdhocQueryResponse response = new AdapterDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(
                request.getAdhocQueryRequest(), assertion);

        LOG.debug("End AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        return response;
    }
}
