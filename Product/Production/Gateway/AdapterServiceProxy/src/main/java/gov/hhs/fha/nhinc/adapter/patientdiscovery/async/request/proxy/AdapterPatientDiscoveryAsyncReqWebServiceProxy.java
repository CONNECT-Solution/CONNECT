/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncreq.AdapterPatientDiscoverySecuredAsyncReq;
import gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncreq.AdapterPatientDiscoverySecuredAsyncReqPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryAsyncReqWebServiceProxy implements AdapterPatientDiscoveryAsyncReqProxy {
    private static Log log = LogFactory.getLog(AdapterPatientDiscoveryAsyncReqWebServiceProxy.class);
    private static AdapterPatientDiscoverySecuredAsyncReq service = new AdapterPatientDiscoverySecuredAsyncReq();

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        RespondingGatewayPRPAIN201305UV02SecuredRequestType securedRequest = new RespondingGatewayPRPAIN201305UV02SecuredRequestType();
        securedRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        securedRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());


        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            AdapterPatientDiscoverySecuredAsyncReqPortType port = getPort(url, request.getAssertion());
            response = port.processPatientDiscoveryAsyncReq(securedRequest);
        }

        return response;
    }

    private AdapterPatientDiscoverySecuredAsyncReqPortType getPort(String url, AssertionType assertion)
    {
        AdapterPatientDiscoverySecuredAsyncReqPortType port = service.getAdapterPatientDiscoverySecuredAsyncReqPortSoap();

        log.info("Setting endpoint address to Adapter Patient Discovery Async Request Secured Service to " + url);
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
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

    

}
