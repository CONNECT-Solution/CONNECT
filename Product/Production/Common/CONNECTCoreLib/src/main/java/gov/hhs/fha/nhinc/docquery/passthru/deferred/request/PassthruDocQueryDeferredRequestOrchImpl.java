/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.request.proxy.NhinDocQueryDeferredRequestProxy;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.request.proxy.NhinDocQueryDeferredRequestProxyObjectFactory;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 * This implementation class contains the flow
 *
 * @author patlollav
 */
public class PassthruDocQueryDeferredRequestOrchImpl {

    //Logger
    private static final Log log = LogFactory.getLog(PassthruDocQueryDeferredRequestOrchImpl.class);

    /**
     * Pass the processing on to the NHIN layer
     *
     * @param adhocQueryRequest
     * @param assertion
     * @param target
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType crossGatewayQueryRequest(AdhocQueryRequest adhocQueryRequest,
            AssertionType assertion, NhinTargetSystemType target) {

        log.debug("Beginning of PassthruDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");

        DocQueryAcknowledgementType docQueryAcknowledgement = null;

        //Call Nhin component proxy
        NhinDocQueryDeferredRequestProxyObjectFactory factory = new NhinDocQueryDeferredRequestProxyObjectFactory();
        NhinDocQueryDeferredRequestProxy proxy = factory.getNhinDocQueryDeferredRequestProxy();
        docQueryAcknowledgement = proxy.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, target);

        log.debug("End of PassthruDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");

        return docQueryAcknowledgement;
    }

}
