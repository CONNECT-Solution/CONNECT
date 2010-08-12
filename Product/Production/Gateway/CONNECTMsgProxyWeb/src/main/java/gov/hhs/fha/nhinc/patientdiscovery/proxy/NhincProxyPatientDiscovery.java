/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mflynn02
 */
@WebService(serviceName = "NhincProxyPatientDiscovery", portName = "NhincProxyPatientDiscoveryPort", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscovery.NhincProxyPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscovery", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscovery/NhincProxyPatientDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyPatientDiscovery {
    private Log log = null;

    public NhincProxyPatientDiscovery() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected NhincProxyPatientDiscoveryImpl getNhincProxyPatientDiscoveryImpl()
    {
        return new NhincProxyPatientDiscoveryImpl();
    }

    public org.hl7.v3.PRPAIN201306UV02 proxyPRPAIN201305UV(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType proxyPRPAIN201305UVProxyRequest) {
        log.info("Entering NhincProxyPatientDiscovery.proxyPRPAIN201305UV");
        org.hl7.v3.PRPAIN201306UV02 response = null;

        NhincProxyPatientDiscoveryImpl impl = getNhincProxyPatientDiscoveryImpl();
        if(impl != null)
        {
            response = impl.proxyPRPAIN201305UV(proxyPRPAIN201305UVProxyRequest);
        }
        log.info("Exiting NhincProxyPatientDiscovery.proxyPRPAIN201305UV");

        return response;
    }

}
