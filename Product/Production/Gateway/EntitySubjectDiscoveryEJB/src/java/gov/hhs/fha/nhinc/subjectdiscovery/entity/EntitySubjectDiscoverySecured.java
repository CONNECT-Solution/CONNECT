/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subjectdiscovery.entity;

import gov.hhs.fha.nhinc.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntitySubjectDiscoverySecured", portName = "EntitySubjectDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubjectdiscoverysecured", wsdlLocation = "META-INF/wsdl/EntitySubjectDiscoverySecured/EntitySubjectDiscoverySecured.wsdl")
@Stateless
public class EntitySubjectDiscoverySecured implements EntitySubjectDiscoverySecuredPortType {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVSecuredRequestType body) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201301UV(body, context);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVSecuredRequestType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201303UV(org.hl7.v3.PIXConsumerPRPAIN201303UVSecuredRequestType body) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201303UV(body, context);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVSecuredRequestType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVSecuredRequestType body) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(body, context);
    }

}
