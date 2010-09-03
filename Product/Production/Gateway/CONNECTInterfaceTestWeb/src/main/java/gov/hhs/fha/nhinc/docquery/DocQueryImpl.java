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
package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhincdocquery.NhincDocQueryPortType;
import gov.hhs.fha.nhinc.nhincdocquery.NhincDocQueryService;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class DocQueryImpl {

    private static Log log = LogFactory.getLog(DocQueryImpl.class);
    private static final String SERVICE_NAME = "mockdocumentquery";

    AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, WebServiceContext context) {
        log.debug("Entering DocQueryImpl.respondingGatewayCrossGatewayQuery");

        AdhocQueryResponse resp = new AdhocQueryResponse();

        RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
        crossGatewayQueryRequest.setAdhocQueryRequest(body);
        crossGatewayQueryRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        NhincDocQueryService service = new NhincDocQueryService();
        NhincDocQueryPortType port = service.getNhincDocQueryPortTypeBindingPort();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));

        resp = port.respondingGatewayCrossGatewayQuery(crossGatewayQueryRequest);
        log.debug("Exiting DocQueryImpl.respondingGatewayCrossGatewayQuery");
        return resp;
    }
}
