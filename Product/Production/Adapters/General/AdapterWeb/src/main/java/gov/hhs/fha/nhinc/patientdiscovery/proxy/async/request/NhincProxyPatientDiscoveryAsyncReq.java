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
@WebService(serviceName = "NhincProxyPatientDiscoveryAsyncReq", portName = "NhincProxyPatientDiscoveryAsyncReqPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncreq.NhincProxyPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoveryAsyncReq/NhincProxyPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhincProxyPatientDiscoveryAsyncReq {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType proxyProcessPatientDiscoveryAsyncReqRequest) {
        return new NhincProxyPatientDiscoveryAsyncReqImpl().proxyProcessPatientDiscoveryAsyncReq(proxyProcessPatientDiscoveryAsyncReqRequest, context);
    }

}
