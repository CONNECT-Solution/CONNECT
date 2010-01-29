package gov.hhs.fha.nhinc.docquery.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityDocQuerySecured", portName = "EntityDocQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "WEB-INF/wsdl/EntityDocQuerySecured/EntityDocQuerySecured.wsdl")
public class EntityDocQuerySecured
{
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType body)
    {
        return new EntityDocQuerySecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
