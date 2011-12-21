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
package gov.hhs.fha.nhinc.docquery.adapter.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQuerySecureRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestSecuredImpl {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecureRequestType body, WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), assertion);
    }
}
