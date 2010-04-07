/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.*;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReq", portName = "AdapterPatientDiscoveryAsyncReqPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreq.AdapterPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/AdapterAsyncReqPatientDiscovery/AdapterPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class AdapterAsyncReqPatientDiscovery {

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType processPatientDiscoveryAsyncReqAsyncRequest) {
        return new AdapterAsyncReqPatientDiscoveryImpl().processPatientDiscoveryAsyncReq(processPatientDiscoveryAsyncReqAsyncRequest);
    }

}
