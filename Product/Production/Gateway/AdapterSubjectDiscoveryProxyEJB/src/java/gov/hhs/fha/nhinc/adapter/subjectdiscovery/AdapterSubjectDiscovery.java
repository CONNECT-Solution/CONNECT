/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.subjectdiscovery;

import gov.hhs.fha.nhinc.adaptersubjectdiscovery.AdapterSubjectDiscoveryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterSubjectDiscovery", portName = "AdapterSubjectDiscoveryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adaptersubjectdiscovery.AdapterSubjectDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptersubjectdiscovery", wsdlLocation = "META-INF/wsdl/AdapterSubjectDiscovery/AdapterSubjectDiscovery.wsdl")
@Stateless
public class AdapterSubjectDiscovery implements AdapterSubjectDiscoveryPortType {

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType pixConsumerPRPAIN201301UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201301UV(pixConsumerPRPAIN201301UVRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType pixConsumerPRPAIN201302UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201302UV(pixConsumerPRPAIN201302UVRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201303UV(org.hl7.v3.PIXConsumerPRPAIN201303UVRequestType pixConsumerPRPAIN201303UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201303UV(pixConsumerPRPAIN201303UVRequest);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType pixConsumerPRPAIN201304UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201304UV(pixConsumerPRPAIN201304UVRequest);
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType pixConsumerPRPAIN201309UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVRequest);
    }

}
