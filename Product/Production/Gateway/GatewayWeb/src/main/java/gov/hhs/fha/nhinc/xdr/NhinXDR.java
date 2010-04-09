/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;


/**
 *
 * @author dunnek
 */
@WebService(serviceName = "DocumentRepositoryXDR_Service", portName = "DocumentRepositoryXDR_Port_Soap12", endpointInterface = "ihe.iti.xdr._2007.DocumentRepositoryXDRPortType", targetNamespace = "urn:ihe:iti:xdr:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDR/NhinXDR.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhinXDR {
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        //TODO implement this method
        return new NhinXDRImpl().documentRepositoryProvideAndRegisterDocumentSetB(body, context);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
