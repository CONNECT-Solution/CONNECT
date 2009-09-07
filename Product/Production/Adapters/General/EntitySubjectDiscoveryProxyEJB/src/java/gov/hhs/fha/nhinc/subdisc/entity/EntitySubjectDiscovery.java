/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subdisc.entity;

import gov.hhs.fha.nhinc.entitysubjectdiscovery.EntitySubjectDiscoveryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntitySubjectDiscovery", portName = "EntitySubjectDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitysubjectdiscovery.EntitySubjectDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubjectdiscovery", wsdlLocation = "META-INF/wsdl/EntitySubjectDiscovery/EntitySubjectDiscovery.wsdl")
@Stateless
public class EntitySubjectDiscovery implements EntitySubjectDiscoveryPortType {

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType pixConsumerPRPAIN201301UVRequest) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201301UV(pixConsumerPRPAIN201301UVRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType pixConsumerPRPAIN201302UVRequest) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201302UV(pixConsumerPRPAIN201302UVRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201303UV(org.hl7.v3.PIXConsumerPRPAIN201303UVRequestType pixConsumerPRPAIN201303UVRequest) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201303UV(pixConsumerPRPAIN201303UVRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType pixConsumerPRPAIN201304UVRequest) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201304UV(pixConsumerPRPAIN201304UVRequest);
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType pixConsumerPRPAIN201309UVRequest) {
        return new EntitySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVRequest);
    }

}
