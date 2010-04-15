/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityPatientDiscoveryAsyncReq", portName = "EntityPatientDiscoveryAsyncReqPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoveryasyncreq.EntityPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryAsyncReq/EntityPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class EntityPatientDiscoveryAsyncReq {

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType processPatientDiscoveryAsyncReqAsyncRequest) {
        return new EntityPatientDiscoveryAsyncReqImpl().processPatientDiscoveryAsyncReq(processPatientDiscoveryAsyncReqAsyncRequest);
    }

}
