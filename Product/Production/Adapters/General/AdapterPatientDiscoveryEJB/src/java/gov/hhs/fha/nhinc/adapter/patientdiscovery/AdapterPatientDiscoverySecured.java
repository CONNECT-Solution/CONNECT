/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoverySecured", portName = "AdapterPatientDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecured", wsdlLocation = "META-INF/wsdl/AdapterPatientDiscoverySecured/AdapterPatientDiscoverySecured.wsdl")
@Stateless
public class AdapterPatientDiscoverySecured {

    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
        return new AdapterPatientDiscoverySecuredImpl().respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, context);
    }

}
