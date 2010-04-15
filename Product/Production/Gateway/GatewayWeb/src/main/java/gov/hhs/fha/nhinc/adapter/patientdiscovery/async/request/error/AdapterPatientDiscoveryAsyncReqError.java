/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReqError", portName = "AdapterPatientDiscoveryAsyncReqErrorPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreqerror.AdapterPatientDiscoveryAsyncReqErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncreqerror", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryAsyncReqError/AdapterPatientDiscoveryAsyncReqError.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/" )
public class AdapterPatientDiscoveryAsyncReqError {

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType processPatientDiscoveryAsyncReqErrorRequest) {
        return new AdapterPatientDiscoveryAsyncReqErrorImpl().processPatientDiscoveryAsyncReqError(processPatientDiscoveryAsyncReqErrorRequest);
    }

}
