/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReq", portName = "AdapterPatientDiscoveryAsyncReqPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreq.AdapterPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/AdapterAsyncReqPatientDiscovery/AdapterPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterAsyncReqPatientDiscovery {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType processPatientDiscoveryAsyncReqAsyncRequest) {
        return new AdapterAsyncReqPatientDiscoveryImpl().processPatientDiscoveryAsyncReq(processPatientDiscoveryAsyncReqAsyncRequest, context);
    }

}
