/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquery;

import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author vvickers
 */
@WebService(serviceName = "RespondingGateway_Query_Service", portName = "RespondingGateway_Query_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.RespondingGatewayQueryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "META-INF/wsdl/DocQuery/NhinDocQuery.wsdl")
@Stateless
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class DocQuery implements RespondingGatewayQueryPortType {

    @Resource
    private WebServiceContext context;
    private static Log log = LogFactory.getLog(DocQuery.class);

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        DocQueryImpl impl = new DocQueryImpl();
        return(impl.respondingGatewayCrossGatewayQuery(body, context));
    }
}
