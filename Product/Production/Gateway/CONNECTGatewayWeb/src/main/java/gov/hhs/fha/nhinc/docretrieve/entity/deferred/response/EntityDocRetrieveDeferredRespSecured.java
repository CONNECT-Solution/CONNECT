package gov.hhs.fha.nhinc.docretrieve.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieveDeferredResponseSecured", portName = "EntityDocRetrieveDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieveDeferredRespSecured/EntityDocRetrieveDeferredRespSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieveDeferredRespSecured extends EntityDocRetrieveDeferredResponseImpl {

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
        return crossGatewayRetrieveResponse(body, context);
    }
}
