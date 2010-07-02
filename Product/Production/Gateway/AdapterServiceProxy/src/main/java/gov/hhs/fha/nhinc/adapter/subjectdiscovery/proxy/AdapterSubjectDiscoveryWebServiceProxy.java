package gov.hhs.fha.nhinc.adapter.subjectdiscovery.proxy;

import gov.hhs.fha.nhinc.adaptersubjectdiscoverysecured.AdapterSubjectDiscoverySecured;
import gov.hhs.fha.nhinc.adaptersubjectdiscoverysecured.AdapterSubjectDiscoverySecuredPortType;
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
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterSubjectDiscoveryWebServiceProxy
{
    private static Log log = LogFactory.getLog(AdapterSubjectDiscoveryWebServiceProxy.class);
    private static AdapterSubjectDiscoverySecured service = new AdapterSubjectDiscoverySecured();

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            AdapterSubjectDiscoverySecuredPortType port = getPort(url, request.getAssertion());
            ack = port.pixConsumerPRPAIN201301UV(request);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PIXConsumerPRPAIN201302UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            AdapterSubjectDiscoverySecuredPortType port = getPort(url, request.getAssertion());

            ack = port.pixConsumerPRPAIN201302UV(request);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PIXConsumerPRPAIN201304UVRequestType request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVRequestType request) {
        PIXConsumerPRPAIN201309UVResponseType resp = new PIXConsumerPRPAIN201309UVResponseType();

        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            AdapterSubjectDiscoverySecuredPortType port = getPort(url, request.getAssertion());

            resp = port.pixConsumerPRPAIN201309UV(request);
        }

        return resp;
    }

    private AdapterSubjectDiscoverySecuredPortType getPort(String url, AssertionType assertion)
    {
        AdapterSubjectDiscoverySecuredPortType port = service.getAdapterSubjectDiscoverySecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        return port;
    }

    private String getUrl() {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }
}
