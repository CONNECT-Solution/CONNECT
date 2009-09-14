/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocquery;

import gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterDocQuerySecured", portName = "AdapterDocQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerysecured", wsdlLocation = "META-INF/wsdl/AdapterDocQuerySecured/AdapterDocQuerySecured.wsdl")
@Stateless
public class AdapterDocQuerySecured implements AdapterDocQuerySecuredPortType {

    @Resource
    private WebServiceContext context;
    
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        return new AdapterDocQuerySecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
