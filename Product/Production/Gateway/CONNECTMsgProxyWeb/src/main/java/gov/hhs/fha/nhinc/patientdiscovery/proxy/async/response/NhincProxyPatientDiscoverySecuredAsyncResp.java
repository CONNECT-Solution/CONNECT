/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "NhincProxyPatientDiscoverySecuredAsyncResp", portName = "NhincProxyPatientDiscoverySecuredAsyncRespPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncresp.NhincProxyPatientDiscoverySecuredAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoverysecuredasyncresp", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoverySecuredAsyncResp/NhincProxyPatientDiscoverySecuredAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhincProxyPatientDiscoverySecuredAsyncResp {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType proxyProcessPatientDiscoveryAsyncRespRequest) {
        return new NhincProxyPatientDiscoverySecuredAsyncRespImpl().proxyProcessPatientDiscoveryAsyncResp(proxyProcessPatientDiscoveryAsyncRespRequest, context);
    }

}
