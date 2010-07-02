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
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        resp = port.respondingGatewayCrossGatewayQuery(crossGatewayQueryRequest);
        log.debug("Exiting DocQueryImpl.respondingGatewayCrossGatewayQuery");
        return resp;
    }
}
