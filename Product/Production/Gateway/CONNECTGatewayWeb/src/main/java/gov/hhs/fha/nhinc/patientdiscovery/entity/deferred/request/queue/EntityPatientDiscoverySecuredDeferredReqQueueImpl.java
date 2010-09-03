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
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoverySecuredDeferredReqQueueImpl {

    private static Log log = LogFactory.getLog(EntityPatientDiscoverySecuredDeferredReqQueueImpl.class);

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02SecuredRequestType request, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, null);

        return new EntityPatientDiscoveryDeferredReqQueueOrchImpl().addPatientDiscoveryAsyncReq(request.getPRPAIN201305UV02(), assertion, request.getNhinTargetCommunities());
    }

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, request.getAssertion());

        return new EntityPatientDiscoveryDeferredReqQueueOrchImpl().addPatientDiscoveryAsyncReq(request.getPRPAIN201305UV02(), assertion, request.getNhinTargetCommunities());
    }


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
        }

        return assertion;
    }
}
