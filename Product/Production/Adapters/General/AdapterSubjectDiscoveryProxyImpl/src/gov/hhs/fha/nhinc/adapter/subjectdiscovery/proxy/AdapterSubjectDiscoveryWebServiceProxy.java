/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.subjectdiscovery.proxy;

import gov.hhs.fha.nhinc.adaptersubjectdiscovery.AdapterSubjectDiscovery;
import gov.hhs.fha.nhinc.adaptersubjectdiscovery.AdapterSubjectDiscoveryPortType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201303UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterSubjectDiscoveryWebServiceProxy implements AdapterSubjectDiscoveryProxy {

    private static Log log = LogFactory.getLog(AdapterSubjectDiscoveryWebServiceProxy.class);
    static AdapterSubjectDiscovery service = new AdapterSubjectDiscovery();

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            AdapterSubjectDiscoveryPortType port = getPort(url);

            ack = port.pixConsumerPRPAIN201301UV(request);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PIXConsumerPRPAIN201302UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            AdapterSubjectDiscoveryPortType port = getPort(url);

            ack = port.pixConsumerPRPAIN201302UV(request);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201303UV(PIXConsumerPRPAIN201303UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Get the URL to the Adapter Subject Discovery
        String url = getUrl();

        if (NullChecker.isNotNullish(url)) {
            AdapterSubjectDiscoveryPortType port = getPort(url);

            ack = port.pixConsumerPRPAIN201303UV(request);
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

        if (NullChecker.isNotNullish(url)) {
            AdapterSubjectDiscoveryPortType port = getPort(url);

            resp = port.pixConsumerPRPAIN201309UV(request);
        }

        return resp;
    }

    private AdapterSubjectDiscoveryPortType getPort(String url) {
        AdapterSubjectDiscoveryPortType port = service.getAdapterSubjectDiscoveryPortSoap11();

        log.info("Setting endpoint address to Adapter Subject Discovery Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    private String getUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.SUBJECT_DISCOVERY_ADAPTER_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.SUBJECT_DISCOVERY_ADAPTER_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }
}
