/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.request;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyPatientDiscoveryAsyncReq", portName = "NhincProxyPatientDiscoveryAsyncReqPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncreq.NhincProxyPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoveryAsyncReq/NhincProxyPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class NhincProxyPatientDiscoveryAsyncReq {

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType proxyProcessPatientDiscoveryAsyncReqRequest) {
        return new NhincProxyPatientDiscoveryAsyncReqImpl().proxyProcessPatientDiscoveryAsyncReq(proxyProcessPatientDiscoveryAsyncReqRequest);
    }

}
