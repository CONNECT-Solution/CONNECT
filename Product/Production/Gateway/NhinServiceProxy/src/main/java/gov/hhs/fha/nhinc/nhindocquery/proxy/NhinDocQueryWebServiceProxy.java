/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.nhindocquery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import ihe.iti.xds_b._2007.RespondingGatewayQueryService;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryWebServiceProxy implements NhinDocQueryProxy {
    private static Log log = LogFactory.getLog(NhinDocQueryWebServiceProxy.class);
    static RespondingGatewayQueryService nhinService = new RespondingGatewayQueryService();

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request) {
        String url = null;
        AdhocQueryResponse response = new AdhocQueryResponse();

        if (request.getNhinTargetSystem() != null) {
            try {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(request.getNhinTargetSystem(), NhincConstants.DOC_QUERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        if (NullChecker.isNotNullish(url)) {
            if(log.isDebugEnabled())
            {
                log.debug("URL for NHIN Proxy call: " + url);
            }
            RespondingGatewayQueryPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            response = port.respondingGatewayCrossGatewayQuery(request.getAdhocQueryRequest());
        } else {
            log.error("The URL for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME + " is null");
        }

        return response;
    }

    private RespondingGatewayQueryPortType getPort(String url) {
        RespondingGatewayQueryPortType port = nhinService.getRespondingGatewayQueryPortSoap();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }

}
