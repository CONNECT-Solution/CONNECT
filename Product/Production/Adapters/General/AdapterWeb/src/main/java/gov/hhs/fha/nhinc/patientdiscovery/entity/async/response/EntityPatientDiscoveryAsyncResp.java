/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.response;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityPatientDiscoveryAsyncResp", portName = "EntityPatientDiscoveryAsyncRespPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoveryasyncresp.EntityPatientDiscoveryAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncresp", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryAsycResp/EntityPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class EntityPatientDiscoveryAsyncResp {

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType processPatientDiscoveryAsyncRespAsyncRequest) {
        return new EntityPatientDiscoveryAsyncRespImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest);
    }

}
