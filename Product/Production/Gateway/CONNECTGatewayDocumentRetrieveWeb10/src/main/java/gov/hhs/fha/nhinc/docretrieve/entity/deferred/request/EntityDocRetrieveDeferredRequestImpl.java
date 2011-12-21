/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRequestImpl {

    /**
     * 
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, null);
        RetrieveDocumentSetRequestType message = body.getRetrieveDocumentSetRequest();
        NhinTargetCommunitiesType target = body.getNhinTargetCommunities();
        return new EntityDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(message, assertion, target);
    }

    /**
     *
     * @param crossGatewayRetrieveRequest
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest, WebServiceContext context) {
        RetrieveDocumentSetRequestType message = crossGatewayRetrieveRequest.getRetrieveDocumentSetRequest();
        NhinTargetCommunitiesType target = crossGatewayRetrieveRequest.getNhinTargetCommunities();
        AssertionType assertion = extractAssertionFromContext(context, crossGatewayRetrieveRequest.getAssertion());
        return new EntityDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(message, assertion, target);
    }

    /**
     * 
     * @param context
     * @param oAssertionIn
     * @return AssertionType
     */
    protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }
}
