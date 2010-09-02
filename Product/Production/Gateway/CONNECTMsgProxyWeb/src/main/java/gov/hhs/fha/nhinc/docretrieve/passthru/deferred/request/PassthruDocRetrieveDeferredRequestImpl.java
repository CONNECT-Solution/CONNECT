/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
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
public class PassthruDocRetrieveDeferredRequestImpl {

    /**
     * 
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        AssertionType assertion = extractAssertionInfo(context, null);
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = body.getRetrieveDocumentSetRequest();
        NhinTargetSystemType nhinTargetSystem = body.getNhinTargetSystem();
        return new NhincProxyDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetSystem);
    }

    protected DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest, WebServiceContext context) {
        AssertionType assertion = extractAssertionInfo(context, crossGatewayRetrieveRequest.getAssertion());
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = crossGatewayRetrieveRequest.getRetrieveDocumentSetRequest();
        NhinTargetSystemType nhinTargetSystem = crossGatewayRetrieveRequest.getNhinTargetSystem();
        return new NhincProxyDocRetrieveDeferredReqOrchImpl().crossGatewayRetrieveRequest(retrieveDocumentSetRequest, assertion, nhinTargetSystem);
    }

    /**
     * 
     * @param context
     * @param oAssertionIn
     * @return AssertionType
     */
    private AssertionType extractAssertionInfo(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (null == oAssertionIn) {
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
