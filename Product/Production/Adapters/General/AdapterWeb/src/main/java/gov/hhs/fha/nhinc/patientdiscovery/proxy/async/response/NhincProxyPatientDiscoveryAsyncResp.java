/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.response;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "NhincProxyPatientDiscoveryAsyncResp", portName = "NhincProxyPatientDiscoveryAsyncRespPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncresp.NhincProxyPatientDiscoveryAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncresp", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoveryAsyncResp/NhincProxyPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhincProxyPatientDiscoveryAsyncResp {

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType proxyProcessPatientDiscoveryAsyncRespRequest) {
        return new NhincProxyPatientDiscoveryAsyncRespImpl().proxyProcessPatientDiscoveryAsyncResp(proxyProcessPatientDiscoveryAsyncRespRequest);
    }

}
