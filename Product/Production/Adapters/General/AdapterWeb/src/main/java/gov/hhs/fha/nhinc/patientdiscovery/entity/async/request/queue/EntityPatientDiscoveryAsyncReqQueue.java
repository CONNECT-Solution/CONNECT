/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request.queue;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityPatientDiscoveryAsyncReqQueue", portName = "EntityPatientDiscoveryAsyncReqQueuePortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoveryasyncreqqueue.EntityPatientDiscoveryAsyncReqQueuePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncreqqueue", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryAsyncReqQueue/EntityPatientDiscoveryAsyncReqQueue.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class EntityPatientDiscoveryAsyncReqQueue {

    public org.hl7.v3.MCCIIN000002UV01 addPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType addPatientDiscoveryAsyncReqAsyncRequest) {
        return new EntityPatientDiscoveryAsyncReqQueueImpl().addPatientDiscoveryAsyncReq(addPatientDiscoveryAsyncReqAsyncRequest);
    }

}
