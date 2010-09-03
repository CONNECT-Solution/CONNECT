/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 * This abstract class contains business logic common to Entity Secured and Unsecured services for Document Query Deferred Request message
 * @author Mark Goldman
 */
public abstract class EntityDocQueryDeferredReqImpl {

  private EntityDocQueryDeferredReqOrchImpl orchImpl;

  /**
   *
   * @param context
   * @return
   */
  protected AssertionType extractAssertion(WebServiceContext context) {
    AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
    return assertion;
  }

  /**
   *
   * @param assertion
   * @param context
   */
  protected void setMessageID(AssertionType assertion, final WebServiceContext context) {
    if (assertion != null) {
      assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
    }
  }

  /**
   *
   * @param body
   * @param context
   * @return
   */
  protected DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          final RespondingGatewayCrossGatewayQueryRequestType body, final WebServiceContext context) {
    AssertionType assertion = body.getAssertion();
    setMessageID(assertion, context);
    return getOrchImpl().respondingGatewayCrossGatewayQuery(
            body.getAdhocQueryRequest(), assertion, body.getNhinTargetCommunities());
  }

  /**
   *
   * @param body
   * @param context
   * @return
   */
  protected DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          final RespondingGatewayCrossGatewayQuerySecuredRequestType body, final WebServiceContext context) {
    AssertionType assertion = extractAssertion(context);
    setMessageID(assertion, context);
    return getOrchImpl().respondingGatewayCrossGatewayQuery(
            body.getAdhocQueryRequest(), assertion, body.getNhinTargetCommunities());
  }

  /**
   * Create an instance of EntityDocRetrieveDeferredReqImpl Class
   * @return EntityDocRetrieveDeferredReqImpl
   */
  protected EntityDocQueryDeferredReqOrchImpl getOrchImpl() {
    if (orchImpl == null) {
      orchImpl = new EntityDocQueryDeferredReqOrchImpl();
    }
    return orchImpl;
  }
}
