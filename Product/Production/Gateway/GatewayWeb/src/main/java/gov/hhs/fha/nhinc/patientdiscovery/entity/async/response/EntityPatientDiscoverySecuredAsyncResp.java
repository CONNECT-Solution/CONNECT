/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityPatientDiscoverySecuredAsyncResp", portName = "EntityPatientDiscoverySecuredAsyncRespPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncresp.EntityPatientDiscoverySecuredAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecuredasyncresp", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoverySecuredAsyncResp/EntityPatientDiscoverySecuredAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class EntityPatientDiscoverySecuredAsyncResp {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 processPatientDiscoveryAsyncResp(org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType processPatientDiscoveryAsyncRespAsyncRequest) {
        return new EntityPatientDiscoverySecuredAsyncRespImpl().processPatientDiscoveryAsyncResp(processPatientDiscoveryAsyncRespAsyncRequest, context);
    }

}
