/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpi;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.BindingType;
/**

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterMpiSecuredService", portName = "AdapterMpiSecuredPortType", endpointInterface = "gov.hhs.fha.nhinc.adaptermpi.AdapterMpiSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpi", wsdlLocation = "META-INF/wsdl/AdapterMpiSecured/AdapterMpiSecured.wsdl")
@Stateless

public class AdapterMpiSecured {
    @Resource
    private WebServiceContext context;
    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
