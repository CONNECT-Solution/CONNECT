/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredResponseUnsecuredImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterComponentDocQueryDeferredResponseUnsecuredImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryResponseType body, WebServiceContext context) {
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body.getAssertion() != null) {
            body.getAssertion().setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
               body.getAssertion().getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }
        return new AdapterComponentDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryResponse(), body.getAssertion());
    }
}
