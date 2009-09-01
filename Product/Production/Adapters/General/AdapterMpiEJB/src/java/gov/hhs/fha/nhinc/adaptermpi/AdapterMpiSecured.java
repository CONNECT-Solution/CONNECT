/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpi;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterMpiSecuredService", portName = "AdapterMpiSecuredPortType", endpointInterface = "gov.hhs.fha.nhinc.adaptermpi.AdapterMpiSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptermpi", wsdlLocation = "META-INF/wsdl/AdapterMpiSecured/AdapterMpiSecured.wsdl")
@Stateless
public class AdapterMpiSecured implements AdapterMpiSecuredPortType {
    @Resource
    private WebServiceContext context;
    public org.hl7.v3.PRPAIN201306UV findCandidates(org.hl7.v3.PRPAIN201305UV findCandidatesRequest) {
        return AdapterMpiQuery.query(findCandidatesRequest, context);
    }

}
