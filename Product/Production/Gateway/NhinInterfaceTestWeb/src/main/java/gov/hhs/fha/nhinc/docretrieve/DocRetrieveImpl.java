/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve;

import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.nhincdocretrieve.NhincDocRetrievePortType;
import gov.hhs.fha.nhinc.nhincdocretrieve.NhincDocRetrieveService;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class DocRetrieveImpl {

    private static Log log = LogFactory.getLog(DocRetrieveImpl.class);
    private static final String SERVICE_NAME = "mockdocumentretrieve";

    RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, WebServiceContext context) {
        log.debug("Entering DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        RetrieveDocumentSetResponseType resp = new RetrieveDocumentSetResponseType();

        RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
        crossGatewayRetrieveRequest.setRetrieveDocumentSetRequest(body);
        crossGatewayRetrieveRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        NhincDocRetrieveService service = new NhincDocRetrieveService();
        NhincDocRetrievePortType port = service.getNhincDocRetrievePortTypeBindingPort();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        resp = port.respondingGatewayCrossGatewayRetrieve(crossGatewayRetrieveRequest);
        log.debug("Exiting DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return resp;
    }
}
