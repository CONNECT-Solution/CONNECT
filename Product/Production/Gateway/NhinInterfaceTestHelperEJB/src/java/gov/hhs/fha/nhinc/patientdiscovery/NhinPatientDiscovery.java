/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.BindingType;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "RespondingGateway_Service", portName = "RespondingGateway_Port_Soap12", endpointInterface = "ihe.iti.xcpd._2009.RespondingGatewayPortType", targetNamespace = "urn:ihe:iti:xcpd:2009", wsdlLocation = "META-INF/wsdl/NhinPatientDiscovery/NhinPatientDiscovery.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
@Stateless
public class NhinPatientDiscovery {
    @Resource
    private WebServiceContext context;
    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body) {
        //TODO implement this method
        return new NhinPatientDiscoveryImpl().proxyPRPAIN201305UV(body, context);
    }

}
