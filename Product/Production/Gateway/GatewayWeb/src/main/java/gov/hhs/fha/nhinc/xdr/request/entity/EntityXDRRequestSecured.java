package gov.hhs.fha.nhinc.xdr.request.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityXDRSecuredRequest_Service", portName = "EntityXDRSecuredRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured:async:request", wsdlLocation = "WEB-INF/wsdl/EntityXDRRequestSecured/EntityXDRSecuredRequest.wsdl")
public class EntityXDRRequestSecured
{
    @Resource
    private WebServiceContext context;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest)
    {
        return new EntityXDRRequestSecuredImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest, context);
    }

}
