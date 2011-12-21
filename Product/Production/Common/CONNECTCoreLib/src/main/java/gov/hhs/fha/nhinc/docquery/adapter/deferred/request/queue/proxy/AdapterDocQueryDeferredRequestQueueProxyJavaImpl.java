/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue.AdapterDocQueryDeferredReqQueueOrchImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author narendra.reddy
 */
public class AdapterDocQueryDeferredRequestQueueProxyJavaImpl implements AdapterDocQueryDeferredRequestQueueProxy {

    private static Log log = LogFactory.getLog(AdapterDocQueryDeferredRequestQueueProxyJavaImpl.class);

    /**
     *
     * @param msg
     * @param assertion
     * @param targets
     * @return DocQueryAcknowledgementType
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Using Java Implementation for Adapter Doc Query Deferred Request Service");
        return new AdapterDocQueryDeferredReqQueueOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion, targets);
    }
}
