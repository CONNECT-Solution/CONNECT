/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncreqerror.AdapterPatientDiscoverySecuredAsyncReqError;
import gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncreqerror.AdapterPatientDiscoverySecuredAsyncReqErrorPortType;
import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorSecuredRequestType;
import org.hl7.v3.MCCIIN000002UV01;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryAsyncReqErrorWebServiceProxy implements AdapterPatientDiscoveryAsyncReqErrorProxy {
    private static Log log = LogFactory.getLog(AdapterPatientDiscoveryAsyncReqErrorWebServiceProxy.class);
    private static AdapterPatientDiscoverySecuredAsyncReqError service = new AdapterPatientDiscoverySecuredAsyncReqError();

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(AsyncAdapterPatientDiscoveryErrorRequestType request) {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        AsyncAdapterPatientDiscoveryErrorSecuredRequestType securedRequest = new AsyncAdapterPatientDiscoveryErrorSecuredRequestType();
        securedRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        securedRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        securedRequest.setErrorMsg(request.getErrorMsg());
        securedRequest.setPRPAIN201306UV02(request.getPRPAIN201306UV02());

        // Get the URL to the Adapter Patient Discovery Async Req
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            AdapterPatientDiscoverySecuredAsyncReqErrorPortType port = getPort(url, request.getAssertion());
            response = port.processPatientDiscoveryAsyncReqError(securedRequest);
        }

        return response;
    }

    private AdapterPatientDiscoverySecuredAsyncReqErrorPortType getPort(String url, AssertionType assertion)
    {
        AdapterPatientDiscoverySecuredAsyncReqErrorPortType port = service.getAdapterPatientDiscoverySecuredAsyncReqErrorPortSoap();

        log.info("Setting endpoint address to Adapter Patient Discovery Async Request Secured Queue Service to " + url);
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
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_ERROR_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_ERROR_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }
}
