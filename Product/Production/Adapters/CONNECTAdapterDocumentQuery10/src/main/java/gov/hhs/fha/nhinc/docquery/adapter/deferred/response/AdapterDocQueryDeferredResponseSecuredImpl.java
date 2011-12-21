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
package gov.hhs.fha.nhinc.docquery.adapter.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQuerySecureResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredResponseSecuredImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocQueryDeferredResponseSecuredImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQuerySecureResponseType body, WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = msgIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
               assertion.getRelatesToList().add(msgIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return new AdapterDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryResponse(), assertion);
    }
}
