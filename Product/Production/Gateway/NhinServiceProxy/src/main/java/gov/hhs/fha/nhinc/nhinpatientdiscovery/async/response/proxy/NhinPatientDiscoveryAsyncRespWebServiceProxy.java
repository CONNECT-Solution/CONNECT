/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinpatientdiscovery.async.response.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xcpd._2009.RespondingGatewayDeferredRespService;
import ihe.iti.xcpd._2009.RespondingGatewayDeferredResponsePortType;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryAsyncRespWebServiceProxy implements NhinPatientDiscoveryAsyncRespProxy {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryAsyncRespWebServiceProxy.class);
    static RespondingGatewayDeferredRespService nhinService = new RespondingGatewayDeferredRespService();

    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02(PRPAIN201306UV02 body, AssertionType assertion, NhinTargetSystemType target) {
        String url = null;
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        // Get the URL to the Nhin Subject Discovery Service
        url = getUrl(target);

        if (NullChecker.isNotNullish(url)) {
            RespondingGatewayDeferredResponsePortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PATIENT_DISCOVERY_ACTION);

            ArrayList<Handler> handlerSetUp = new ArrayList<Handler>();
            AsyncMessageHandler msgHandler = new AsyncMessageHandler();
            handlerSetUp.add(msgHandler);
            ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

            AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
            requestContext.putAll(msgIdCreator.CreateRequestContextForRelatesTo(assertion));

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.respondingGatewayDeferredPRPAIN201306UV02(body);

        } else {
            log.error("The URL for service: " + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME + " is null");
        }

        return response;
    }

    private RespondingGatewayDeferredResponsePortType getPort(String url) {
        RespondingGatewayDeferredResponsePortType port = nhinService.getRespondingGatewayDeferredResponsePort();

        log.info("Setting endpoint address to Nhin Patient Discovery Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    private String getUrl(NhinTargetSystemType target) {
        String url = null;

        if (target != null) {
            try {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
}
