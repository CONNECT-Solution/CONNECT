/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.subjectdiscovery;

import gov.hhs.fha.nhinc.adaptersubjectdiscoverysecured.AdapterSubjectDiscoverySecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterSubjectDiscoverySecured", portName = "AdapterSubjectDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adaptersubjectdiscoverysecured.AdapterSubjectDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptersubjectdiscoverysecured", wsdlLocation = "META-INF/wsdl/AdapterSubjectDiscoverySecured/AdapterSubjectDiscoverySecured.wsdl")
@Stateless
public class AdapterSubjectDiscoverySecured implements AdapterSubjectDiscoverySecuredPortType {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType pixConsumerPRPAIN201301UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201301UV(pixConsumerPRPAIN201301UVRequest, context);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType pixConsumerPRPAIN201302UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201302UV(pixConsumerPRPAIN201302UVRequest, context);

    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201303UV(org.hl7.v3.PIXConsumerPRPAIN201303UVRequestType pixConsumerPRPAIN201303UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201303UV(pixConsumerPRPAIN201303UVRequest, context);

    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType pixConsumerPRPAIN201304UVRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType pixConsumerPRPAIN201309UVRequest) {
        return new AdapterSubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVRequest, context);

    }

}
