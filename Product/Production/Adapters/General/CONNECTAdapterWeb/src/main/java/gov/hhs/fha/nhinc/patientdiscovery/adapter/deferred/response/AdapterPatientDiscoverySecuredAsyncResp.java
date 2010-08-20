/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoverySecuredAsyncResp", portName = "AdapterPatientDiscoverySecuredAsyncRespPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncresp.AdapterPatientDiscoverySecuredAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecuredasyncresp", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoverySecuredAsyncResp/AdapterPatientDiscoverySecuredAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoverySecuredAsyncResp {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType processPatientDiscoveryAsyncRespAsyncRequest) {
        return new AdapterPatientDiscoverySecuredAsyncRespImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest.getPRPAIN201306UV02(), context);
    }

}
