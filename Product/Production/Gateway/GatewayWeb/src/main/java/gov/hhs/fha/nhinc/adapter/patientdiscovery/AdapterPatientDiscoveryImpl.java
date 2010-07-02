/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecured;
import gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterPatientDiscoveryImpl.class);
    private static AdapterPatientDiscoverySecured service = new AdapterPatientDiscoverySecured();

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType request) {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        try
        {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_PATIENT_DISCOVERY_SECURED_SERVICE_NAME);
            AdapterPatientDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PATIENT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.respondingGatewayPRPAIN201305UV02(request);
        }
        catch (Exception ex)
        {
            log.error("Error calling adapter patient discovery secured service: " + ex.getMessage(), ex);
        }

        return response;
    }

    private AdapterPatientDiscoverySecuredPortType  getPort(String url) {
        AdapterPatientDiscoverySecuredPortType  port = service.getAdapterPatientDiscoverySecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }

}
