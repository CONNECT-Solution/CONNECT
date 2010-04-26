/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscovery;
import gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscoveryPortType;
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
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;

/**
 *
 * @author dunnek
 */
public class AdapterPatientDiscoveryWebServiceUnSecuredProxy implements AdapterPatientDiscoveryProxy{
    private static Log log = null;
    private static AdapterPatientDiscovery service = null;

    public AdapterPatientDiscoveryWebServiceUnSecuredProxy()
    {
        log = createLogger();
        service = createService();
    }
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body, AssertionType assertion,  NhinTargetCommunitiesType target) {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

        log.debug("Begin AdapterPatientDiscoveryWebServiceUnSecuredProxy.respondingGatewayPRPAIN201305UV02()");

        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        request.setAssertion(assertion);
        request.setPRPAIN201305UV02(body);
        request.setNhinTargetCommunities(target);
        if (NullChecker.isNotNullish(url) && (body != null))
        {
            AdapterPatientDiscoveryPortType port = getPort(url, assertion);
            response = port.respondingGatewayPRPAIN201305UV02(request);
        }

        return response;
    }
    protected AdapterPatientDiscovery createService()
    {
        return  new AdapterPatientDiscovery();
    }
    protected AdapterPatientDiscoveryPortType getPort(String url, AssertionType assertion)
    {
        AdapterPatientDiscoveryPortType port = service.getAdapterPatientDiscoveryPortSoap11();

        log.info("Setting endpoint address to Adapter Subject Discovery Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PATIENT_DISCOVERY_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        return port;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    protected String getUrl() {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_DISCOVERY_ADAPTER_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_ADAPTER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }
}
