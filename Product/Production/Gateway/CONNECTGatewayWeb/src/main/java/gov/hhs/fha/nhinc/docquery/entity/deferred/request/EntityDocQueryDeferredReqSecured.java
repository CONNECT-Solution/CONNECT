package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 * This is an Entity Secure service for Document Query Deferred Request message
 * @author Mark Goldman
 */
@WebService(serviceName = "EntityDocQueryDeferredRequestSecured",
            portName = "EntityDocQueryDeferredRequestSecuredPortSoap",
            endpointInterface = "gov.hhs.fha.nhinc.entitydocquerydeferredrequestsecured.EntityDocQueryDeferredRequestSecuredPortType",
            targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquerydeferredrequestsecured",
            wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredReqSecured/EntityDocQueryDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocQueryDeferredReqSecured {

  @Resource
  private WebServiceContext context;
  private EntityDocQueryDeferredReqOrchImpl orchImpl;

  /**
   * The Entity Secured Method implementation for RespondingGatewayCrossGatewayQueryRequest makes call to actual implementation
   * @param respondingGatewayCrossGatewayQueryRequest
   * @return DocQueryAcknowledgementsType
   */
  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType respondingGatewayCrossGatewayQueryRequest) {
    return getEntityDocQueryDeferredReqOrchImpl().respondingGatewayCrossGatewayQuery(
            respondingGatewayCrossGatewayQueryRequest, context);
  }

  /**
   * Create an instance of EntityDocRetrieveDeferredReqImpl Class
   * @return EntityDocRetrieveDeferredReqImpl
   */
  protected EntityDocQueryDeferredReqOrchImpl getEntityDocQueryDeferredReqOrchImpl() {
    if (orchImpl == null) {
      orchImpl = new EntityDocQueryDeferredReqOrchImpl();
    }
    return orchImpl;
  }
}
