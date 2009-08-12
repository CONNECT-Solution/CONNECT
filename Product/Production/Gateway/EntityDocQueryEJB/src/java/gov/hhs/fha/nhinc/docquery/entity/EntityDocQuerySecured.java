package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.entitydocquery.EntityDocQuerySecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityDocQuerySecured", portName = "EntityDocQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "META-INF/wsdl/EntityDocQuerySecured/EntityDocQuerySecured.wsdl")
@Stateless
public class EntityDocQuerySecured implements EntityDocQuerySecuredPortType
{
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType body)
    {
        return new EntityDocQuerySecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }
    
}
