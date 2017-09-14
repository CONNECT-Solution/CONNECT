/**
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.service.AdapterPatientDiscoverySecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public class AdapterPatientDiscoveryProxyWebServiceHelper {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryProxyWebServiceHelper.class);
    private static AdapterPatientDiscoveryProxyWebServiceHelper INSTANCE = new AdapterPatientDiscoveryProxyWebServiceHelper();
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    AdapterPatientDiscoveryProxyWebServiceHelper() {
    }

    public static AdapterPatientDiscoveryProxyWebServiceHelper getInstance() {
        return INSTANCE;
    }

    public PRPAIN201306UV02 formatGatewayResponsePRPAIN201305UV02(PRPAIN201305UV02 body, AssertionType assertion,
        String sServiceName) throws PatientDiscoveryException {
        String url;
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        try {
            if (body != null) {
                LOG.debug("Before target system URL look up.");
                url = oProxyHelper.getAdapterEndPointFromConnectionManager(sServiceName);
                LOG.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
                    request.setAssertion(assertion);
                    request.setPRPAIN201305UV02(body);
                    request.setNhinTargetCommunities(null);

                    ServicePortDescriptor<AdapterPatientDiscoverySecuredPortType> portDescriptor = new AdapterPatientDiscoverySecuredServicePortDescriptor();
                    CONNECTClient<AdapterPatientDiscoverySecuredPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientSecured(portDescriptor, url, assertion);

                    response = (PRPAIN201306UV02) client.invokePort(AdapterPatientDiscoverySecuredPortType.class,
                        "respondingGatewayPRPAIN201305UV02", request);
                } else {
                    throw new PatientDiscoveryException(
                        "Failed to call the adapter web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                throw new PatientDiscoveryException(
                    "Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            LOG.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  "
                + "Exception: " + e.getMessage(), e);
            throw new PatientDiscoveryException(e.fillInStackTrace());
        }
        return response;
    }

}
