/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subjectdiscovery.proxy;

import gov.hhs.fha.nhinc.nhincproxysubjectdiscoverysecured.NhincProxySubjectDiscoverySecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxySubjectDiscoverySecured", portName = "NhincProxySubjectDiscoverySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubjectdiscoverysecured.NhincProxySubjectDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubjectdiscoverysecured", wsdlLocation = "META-INF/wsdl/NhincSubjectDiscoverySecured/NhincProxySubjectDiscoverySecured.wsdl")
@Stateless
public class NhincSubjectDiscoverySecured implements NhincProxySubjectDiscoverySecuredPortType {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVProxySecuredRequestType pixConsumerPRPAIN201301UVProxyRequest) {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201301UV(pixConsumerPRPAIN201301UVProxyRequest, context);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVProxySecuredRequestType pixConsumerPRPAIN201302UVProxyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201303UV(org.hl7.v3.PIXConsumerPRPAIN201303UVProxySecuredRequestType pixConsumerPRPAIN201303UVProxyRequest) {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201303UV(pixConsumerPRPAIN201303UVProxyRequest, context);
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVProxySecuredRequestType pixConsumerPRPAIN201304UVProxyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PRPAIN201310UV pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVProxySecuredRequestType pixConsumerPRPAIN201309UVProxyRequest) {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVProxyRequest, context);
    }

}
