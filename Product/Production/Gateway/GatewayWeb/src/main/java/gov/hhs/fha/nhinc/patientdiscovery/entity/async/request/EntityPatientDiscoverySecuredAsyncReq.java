/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityPatientDiscoverySecuredAsyncReq", portName = "EntityPatientDiscoverySecuredAsyncReqPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncreq.EntityPatientDiscoverySecuredAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecuredasyncreq", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoverySecuredAsyncReq/EntityPatientDiscoverySecuredAsyncReq.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class EntityPatientDiscoverySecuredAsyncReq {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType processPatientDiscoveryAsyncReqAsyncRequest) {
        return new EntityPatientDiscoverySecuredAsyncReqImpl().processPatientDiscoveryAsyncReq(processPatientDiscoveryAsyncReqAsyncRequest, context);
    }

}
