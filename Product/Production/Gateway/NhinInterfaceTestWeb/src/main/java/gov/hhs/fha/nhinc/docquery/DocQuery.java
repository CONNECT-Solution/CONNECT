package gov.hhs.fha.nhinc.docquery;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "RespondingGateway_Query_Service", portName = "RespondingGateway_Query_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.RespondingGatewayQueryPortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/DocQuery/NhinDocQuery.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class DocQuery
{
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body)
    {
        return new DocQueryImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
