/**
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyWebServiceHelper;
import gov.hhs.fha.nhinc.patientdiscovery.entity.proxy.service.EntityPatientDiscoverySecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.patientdiscovery.entity.proxy.service.EntityPatientDiscoveryServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public abstract class EntityPatientDiscoveryProxyWebServicAbstract {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryProxyWebServiceHelper.class);
    private String connectionManager = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * @return the oProxyHelper
     */
    public WebServiceProxyHelper getoProxyHelper() {
        return oProxyHelper;
    }

    /**
     * @param oProxyHelper the oProxyHelper to set
     */
    public void setoProxyHelper(WebServiceProxyHelper oProxyHelper) {
        this.oProxyHelper = oProxyHelper;
    }

    /**
     * @return the connectionManager
     */
    public String getConnectionManager() {
        return connectionManager;
    }

    /**
     * @param connectionManager the connectionManager to set
     */
    public void setConnectionManager(String connectionManager) {
        this.connectionManager = connectionManager;
    }

    String getEndpointURLForService(String serviceName) {
        String endpointURL = null;
        try {
            endpointURL = invokeConnectionManager(serviceName);
            LOG.debug("Retrieved endpoint URL for service " + serviceName + ": " + endpointURL);
        } catch (ConnectionManagerException ex) {
            LOG.error(
                "Error getting url for " + serviceName + " from the connection manager. Error: " + ex.getMessage(), ex);
        }

        return endpointURL;
    }

    String invokeConnectionManager(String serviceName) throws ConnectionManagerException {
        String connectionManager = ConnectionManagerCache.getInstance()
            .getInternalEndpointURLByServiceName(serviceName);
        setConnectionManager(connectionManager);
        return connectionManager;
    }

    String setInvokeConnectionManager(String serviceName) throws ConnectionManagerException {
        return ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(serviceName);
    }

    abstract RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 pdRequest,
        AssertionType assertion, NhinTargetCommunitiesType targetCommunities);

    /**
     * @param url
     * @param assertion
     * @return
     */
    CONNECTClient<EntityPatientDiscoveryPortType> getDiscoveryPortClient(final String url,
        final AssertionType assertion) {
        ServicePortDescriptor<EntityPatientDiscoveryPortType> portDescriptor = new EntityPatientDiscoveryServicePortDescriptor();
        return CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * @param url
     * @param assertion
     * @return
     */
    CONNECTClient<EntityPatientDiscoverySecuredPortType> getSecuredPortClient(final String url,
        final AssertionType assertion) {
        ServicePortDescriptor<EntityPatientDiscoverySecuredPortType> portDescriptor = new EntityPatientDiscoverySecuredServicePortDescriptor();
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

}
