/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve;

import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "RespondingGateway_Retrieve_Service", portName = "RespondingGateway_Retrieve_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "META-INF/wsdl/DocRetrieve/NhinDocRetrieve.wsdl")
@Stateless
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class DocRetrieve implements RespondingGatewayRetrievePortType {

    @Resource
    private WebServiceContext context;
    private static Log log = LogFactory.getLog(DocRetrieve.class);

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        DocRetrieveImpl impl = new DocRetrieveImpl();
        return(impl.respondingGatewayCrossGatewayRetrieve(body, context));
    }

}
