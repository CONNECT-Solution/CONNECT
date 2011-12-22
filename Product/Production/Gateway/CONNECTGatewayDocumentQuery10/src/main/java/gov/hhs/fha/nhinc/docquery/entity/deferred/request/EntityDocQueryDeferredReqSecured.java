/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * This is an Entity Secured service for Document Query Deferred Request message
 * @author Mark Goldman
 */
@WebService(serviceName = "EntityDocQueryDeferredRequestSecured",
            portName = "EntityDocQueryDeferredRequestSecuredPortSoap",
            endpointInterface = "gov.hhs.fha.nhinc.entitydocquerydeferredrequestsecured.EntityDocQueryDeferredRequestSecuredPortType",
            targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquerydeferredrequestsecured",
            wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredReqSecured/EntityDocQueryDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocQueryDeferredReqSecured extends EntityDocQueryDeferredReqImpl {

  @Resource
  private WebServiceContext context;

  /**
   * The Entity Secured Method implementation for RespondingGatewayCrossGatewayQueryRequest makes call to actual implementation
   * @param respondingGatewayCrossGatewayQueryRequest
   * @return DocQueryAcknowledgementsType
   */
  public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          RespondingGatewayCrossGatewayQuerySecuredRequestType body) {
    return respondingGatewayCrossGatewayQuery(body, context);
  }
}
