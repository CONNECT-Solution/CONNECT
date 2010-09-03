/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveOrchImpl;

/**
 *
 * @author dunnek
 */
public class EntityDocRetreiveImpl {

  public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body,  final WebServiceContext context)
  {
    AssertionType assertion = getAssertion(context, null);

    return this.respondingGatewayCrossGatewayQuery(body, assertion);
  }
  public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion, final WebServiceContext context)
  {
    AssertionType assertionWithId = getAssertion(context, assertion);

    return this.respondingGatewayCrossGatewayQuery(body, assertionWithId);
  }
  public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayQuery(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, AssertionType assertion)
  {

    return getEntityOrchImpl().respondingGatewayCrossGatewayRetrieve(body, assertion);
  }
  protected EntityDocRetrieveOrchImpl getEntityOrchImpl()
  {
      return new EntityDocRetrieveOrchImpl();
  }
    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }
}
