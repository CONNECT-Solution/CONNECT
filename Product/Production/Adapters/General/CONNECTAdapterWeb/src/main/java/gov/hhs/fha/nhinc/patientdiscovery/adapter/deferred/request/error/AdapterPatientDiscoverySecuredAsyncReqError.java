/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoverySecuredAsyncReqError", portName = "AdapterPatientDiscoverySecuredAsyncReqErrorPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncreqerror.AdapterPatientDiscoverySecuredAsyncReqErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecuredasyncreqerror", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoverySecuredAsynReqError/AdapterPatientDiscoverySecuredAsyncReqError.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/" )
public class AdapterPatientDiscoverySecuredAsyncReqError {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(org.hl7.v3.AsyncAdapterPatientDiscoveryErrorSecuredRequestType processPatientDiscoveryAsyncReqErrorRequest) {
        return new AdapterPatientDiscoverySecuredAsyncReqErrorImpl().processPatientDiscoveryAsyncReqError(processPatientDiscoveryAsyncReqErrorRequest.getPRPAIN201305UV02(), processPatientDiscoveryAsyncReqErrorRequest.getPRPAIN201306UV02(), processPatientDiscoveryAsyncReqErrorRequest.getErrorMsg(), context);
    }

}
