/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocretrieve;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterDocRetrieveSecured", portName = "AdapterDocRetrieveSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievesecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveSecured/AdapterDocRetrieveSecured.wsdl")
public class AdapterDocRetrieveSecured {

    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveSecured/AdapterDocRetrieveSecured.wsdl")

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
