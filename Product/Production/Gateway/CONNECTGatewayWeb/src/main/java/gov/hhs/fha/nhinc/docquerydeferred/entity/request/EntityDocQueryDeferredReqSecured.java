package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;

/**
 * This is an Entity Secure service for Document Query Deferred Request message
 * @author Mark Goldman
 */
@WebService(serviceName = "EntityDocQueryDeferredRequestSecured", portName = "EntityDocQueryDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocquerydeferredrequestsecured.EntityDocQueryDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquerydeferredrequestsecured", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredReqSecured/EntityDocQueryDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocQueryDeferredReqSecured {

  /**
   * The Entity Secured Method implementation for RespondingGatewayCrossGatewayQueryRequest makes call to actual implementation
   * @param respondingGatewayCrossGatewayQueryRequest
   * @return DocQueryAcknowledgementsType
   */
  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType respondingGatewayCrossGatewayQueryRequest) {
    return null;
  }
}
