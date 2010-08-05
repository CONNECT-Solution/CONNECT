package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 * This is an Entity Secure service for Document Query Deferred Request message
 * @author Mark Goldman
 */
@WebService(serviceName = "EntityDocQueryDeferredRequestSecured", portName = "EntityDocQueryDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocquerydeferredrequestsecured.EntityDocQueryDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquerydeferredrequestsecured", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredReqSecured/EntityDocQueryDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocQueryDeferredReqSecured {

  @Resource
  private WebServiceContext context;
  private EntityDocQueryDeferredRequestHelper serviceHelper;

  /**
   * The Entity Secured Method implementation for RespondingGatewayCrossGatewayQueryRequest makes call to actual implementation
   * @param respondingGatewayCrossGatewayQueryRequest
   * @return DocQueryAcknowledgementsType
   */
  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecuredRequestType respondingGatewayCrossGatewayQueryRequest) {
    serviceHelper = getEntityDocQueryDeferredRequestHelper();

    AdhocQueryRequest retrieveDocumentSetRequest =
            serviceHelper.getAdhocQueryRequest(respondingGatewayCrossGatewayQueryRequest);
    AssertionType assertion =
            serviceHelper.getAssertion(context);
    NhinTargetCommunitiesType nhinTargetCommunities =
            serviceHelper.getNhinTargetCommunities(respondingGatewayCrossGatewayQueryRequest);

    DocQueryAcknowledgementType ack =
            getEntityDocQueryDeferredReqImpl().respondingGatewayCrossGatewayQuery(
            retrieveDocumentSetRequest,
            assertion,
            nhinTargetCommunities);
    return ack;
  }

  /**
   * Create an instance of EntityDocQueryDeferredRequestHelper Class
   * @return EntityDocQueryDeferredRequestHelper
   */
  protected EntityDocQueryDeferredRequestHelper getEntityDocQueryDeferredRequestHelper() {
    return new EntityDocQueryDeferredRequestHelper();
  }

  /**
   * Create an instance of EntityDocRetrieveDeferredReqImpl Class
   * @return EntityDocRetrieveDeferredReqImpl
   */
  protected EntityDocQueryDeferredReqImpl getEntityDocQueryDeferredReqImpl() {
    return new EntityDocQueryDeferredReqImpl();
  }
}
