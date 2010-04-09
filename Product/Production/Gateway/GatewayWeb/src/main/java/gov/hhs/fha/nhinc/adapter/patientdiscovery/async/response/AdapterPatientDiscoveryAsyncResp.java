/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncResp", portName = "AdapterPatientDiscoveryAsyncRespPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncresp.AdapterPatientDiscoveryAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncresp", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryAsyncResp/AdapterPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoveryAsyncResp {

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType processPatientDiscoveryAsyncRespAsyncRequest) {
        return new AdapterPatientDiscoveryAsyncRespImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest);
    }

}
