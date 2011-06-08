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

package gov.hhs.fha.nhinc.docquery.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
public class PassthruDocQueryDeferredResponseUnsecuredImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PassthruDocQueryDeferredResponseUnsecuredImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryResponseType body, WebServiceContext context) {
        log.debug("Begin PassthruDocQueryDeferredResponseUnsecuredImpl.respondingGatewayCrossGatewayQuery(unsecured)");

        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        log.debug("Assertion from AdhocQueryResponse is: " + asyncProcess.marshalAssertionTypeObject(body.getAssertion()));

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body.getAssertion() != null) {
            body.getAssertion().setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
               body.getAssertion().getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        log.debug("Assertion after modify is: " + asyncProcess.marshalAssertionTypeObject(body.getAssertion()));

        DocQueryAcknowledgementType response = new PassthruDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryResponse(), body.getAssertion(), body.getNhinTargetSystem());

        return response;
    }

}
