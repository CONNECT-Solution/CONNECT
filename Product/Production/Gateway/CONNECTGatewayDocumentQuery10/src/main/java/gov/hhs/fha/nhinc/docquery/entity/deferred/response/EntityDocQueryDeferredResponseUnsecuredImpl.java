/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.docquery.entity.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocQueryDeferredResponseUnsecuredImpl {

    private Log log = null;

    public EntityDocQueryDeferredResponseUnsecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Implementation of EntityDocQueryDeferredResponse service
     *
     * @param request
     * @param context
     * @return Doc Query Acknowledgement
     */
    public DocQueryAcknowledgementType crossGatewayQueryResponse(RespondingGatewayCrossGatewayQueryResponseType request, WebServiceContext context) {

        log.debug("Begin EntityDocQueryDeferredResponseUnsecuredImpl.crossGatewayQueryResponse(unsecured)");

        DocQueryAcknowledgementType response = null;
        AssertionType assertion = null;
        AdhocQueryResponse body = null;
        NhinTargetCommunitiesType target = null;

        if (request != null) {
            body = request.getAdhocQueryResponse();
            assertion = request.getAssertion();
            target = request.getNhinTargetCommunities();
        }
        assertion = getAssertion(context, assertion);

        response = new EntityDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(body, assertion, target);

        log.debug("End EntityDocQueryDeferredResponseUnsecuredImpl.crossGatewayQueryResponse(unsecured)");

        return response;
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
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }
}
