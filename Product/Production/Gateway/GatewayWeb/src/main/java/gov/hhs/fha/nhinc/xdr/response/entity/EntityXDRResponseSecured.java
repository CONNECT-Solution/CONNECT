package gov.hhs.fha.nhinc.xdr.response.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityXDRSecuredResponse_Service", portName = "EntityXDRSecuredResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured:async:response", wsdlLocation = "WEB-INF/wsdl/EntityXDRResponseSecured/EntityXDRSecuredResponse.wsdl")
public class EntityXDRResponseSecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest)
    {
        return new EntityXDRResponseSecuredImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetSecuredResponseRequest, context);
    }

}
