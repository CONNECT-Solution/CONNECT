/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subjectdiscovery;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "PIXConsumer_Service", portName = "PIXConsumer_Port_Soap", endpointInterface = "ihe.iti.pixv3._2007.PIXConsumerPortType", targetNamespace = "urn:ihe:iti:pixv3:2007", wsdlLocation = "META-INF/wsdl/SubjectDiscovery/NhinSubjectDiscovery.wsdl")
@Stateless
public class SubjectDiscovery {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PRPAIN201301UV02 body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PRPAIN201302UV02 body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PRPAIN201304UV02 body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PRPAIN201310UV02 pixConsumerPRPAIN201309UV(org.hl7.v3.PRPAIN201309UV02 body) {
        return (SubjectDiscoveryImpl.pixConsumerPRPAIN201309UV(body, context));
    }

}
