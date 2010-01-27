/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.document;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "DocumentRepository_Service", portName = "DocumentRepository_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.DocumentRepositoryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/DocumentRepositoryService/AdapterComponentDocRepository.wsdl")
public class DocumentRepositoryService {

    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/DocumentRepositoryService/AdapterComponentDocRepository.wsdl")

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
