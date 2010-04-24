/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncreq.NhincProxyPatientDiscoverySecuredAsyncReqPortType;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncresp.NhincProxyPatientDiscoverySecuredAsyncResp;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncresp.NhincProxyPatientDiscoverySecuredAsyncRespPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class PassthruPatientDiscoveryAsyncRespWebServiceProxy implements PassthruPatientDiscoveryAsyncRespProxy {
    private static Log log = LogFactory.getLog(PassthruPatientDiscoveryAsyncRespWebServiceProxy.class);
    private static NhincProxyPatientDiscoverySecuredAsyncResp service = new NhincProxyPatientDiscoverySecuredAsyncResp();

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxyRequestType request) {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        ProxyPRPAIN201306UVProxySecuredRequestType securedRequest = new ProxyPRPAIN201306UVProxySecuredRequestType();
        securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        securedRequest.setPRPAIN201306UV02(request.getPRPAIN201306UV02());


        // Get the URL to the Entity Patient Discovery Async Response
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            NhincProxyPatientDiscoverySecuredAsyncRespPortType port = getPort(url, request.getAssertion());
            response = port.proxyProcessPatientDiscoveryAsyncResp(securedRequest);
        }

        return response;
    }

    private NhincProxyPatientDiscoverySecuredAsyncRespPortType getPort(String url, AssertionType assertion)
    {
        NhincProxyPatientDiscoverySecuredAsyncRespPortType port = service.getNhincProxyPatientDiscoverySecuredAsyncRespPortType();

        log.info("Setting endpoint address to Pass through Patient Discovery Async Response Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PATIENT_DISCOVERY_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        return port;
    }

    private String getUrl() {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_DISCOVERY_PASSTHRU_SECURED_ASYNC_RESP_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_PASSTHRU_SECURED_ASYNC_RESP_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

}
