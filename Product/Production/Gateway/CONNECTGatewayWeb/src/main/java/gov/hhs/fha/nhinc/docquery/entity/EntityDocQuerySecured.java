package gov.hhs.fha.nhinc.docquery.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocQuerySecured", portName = "EntityDocQuerySecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "WEB-INF/wsdl/EntityDocQuerySecured/EntityDocQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocQuerySecured {

    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType body)
    {
        return new EntityDocQuerySecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
