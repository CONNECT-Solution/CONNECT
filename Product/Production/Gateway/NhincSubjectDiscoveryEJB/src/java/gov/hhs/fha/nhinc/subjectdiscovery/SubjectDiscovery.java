/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subjectdiscovery;

import ihe.iti.pixv3._2007.PIXConsumerPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@WebService(serviceName = "PIXConsumer_Service", portName = "PIXConsumer_Port_Soap", endpointInterface = "ihe.iti.pixv3._2007.PIXConsumerPortType", targetNamespace = "urn:ihe:iti:pixv3:2007", wsdlLocation = "META-INF/wsdl/SubjectDiscovery/NhinSubjectDiscovery.wsdl")
@Stateless
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class SubjectDiscovery implements PIXConsumerPortType {
    @Resource
    private WebServiceContext context;
    private static Log log = LogFactory.getLog(SubjectDiscovery.class);

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PRPAIN201301UV body) {
        return (SubjectDiscoveryImpl.pixConsumerPRPAIN201301UV(body, context));
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PRPAIN201302UV body) {
        return (SubjectDiscoveryImpl.pixConsumerPRPAIN201302UV(body, context));
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201303UV(org.hl7.v3.PRPAIN201303UV body) {
        return (SubjectDiscoveryImpl.pixConsumerPRPAIN201303UV(body, context));
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PRPAIN201304UV body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PRPAIN201310UV pixConsumerPRPAIN201309UV(org.hl7.v3.PRPAIN201309UV body) {
        return (SubjectDiscoveryImpl.pixConsumerPRPAIN201309UV(body, context));
    }

}
