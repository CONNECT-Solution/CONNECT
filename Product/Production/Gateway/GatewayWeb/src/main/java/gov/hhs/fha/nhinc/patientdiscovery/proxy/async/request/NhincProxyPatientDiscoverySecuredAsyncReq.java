/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyPatientDiscoverySecuredAsyncReq", portName = "NhincProxyPatientDiscoverySecuredAsyncReqPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncreq.NhincProxyPatientDiscoverySecuredAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoverysecuredasyncreq", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoverySecuredAsyncReq/NhincProxyPatientDiscoverySecuredAsyncReq.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhincProxyPatientDiscoverySecuredAsyncReq {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType proxyProcessPatientDiscoveryAsyncReqRequest) {
        return new NhincProxyPatientDiscoverySecuredAsyncReqImpl().proxyProcessPatientDiscoveryAsyncReq(proxyProcessPatientDiscoveryAsyncReqRequest, context);
    }

}
