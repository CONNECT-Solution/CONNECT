/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocquery;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterDocQuerySecured", portName = "AdapterDocQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerysecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocQuerySecured/AdapterDocQuerySecured.wsdl")
public class AdapterDocQuerySecured {
    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/AdapterDocQuerySecured/AdapterDocQuerySecured.wsdl")
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
