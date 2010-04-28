/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.patientdiscovery.async.request.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncreq.NhincProxyPatientDiscoverySecuredAsyncReq;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncreq.NhincProxyPatientDiscoverySecuredAsyncReqPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class PassthruPatientDiscoveryAsyncReqWebServiceProxy implements PassthruPatientDiscoveryAsyncReqProxy {
    private static Log log = LogFactory.getLog(PassthruPatientDiscoveryAsyncReqWebServiceProxy.class);
    private static NhincProxyPatientDiscoverySecuredAsyncReq service = new NhincProxyPatientDiscoverySecuredAsyncReq();

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxyRequestType request) {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        ProxyPRPAIN201305UVProxySecuredRequestType securedRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
        securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        securedRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());


        // Get the URL to the Entity Patient Discovery Async Req Queue
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            NhincProxyPatientDiscoverySecuredAsyncReqPortType port = getPort(url, request.getAssertion());
            response = port.proxyProcessPatientDiscoveryAsyncReq(securedRequest);
        }

        return response;
    }

    private NhincProxyPatientDiscoverySecuredAsyncReqPortType getPort(String url, AssertionType assertion)
    {
        NhincProxyPatientDiscoverySecuredAsyncReqPortType port = service.getNhincProxyPatientDiscoverySecuredAsyncReqPortType();

        log.info("Setting endpoint address to Pass through Patient Discovery Async Request Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PATIENT_DISCOVERY_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        ArrayList<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForMessageId(assertion));

        return port;
    }

    private String getUrl() {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_DISCOVERY_PASSTHRU_SECURED_ASYNC_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_PASSTHRU_SECURED_ASYNC_REQ_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }
}
