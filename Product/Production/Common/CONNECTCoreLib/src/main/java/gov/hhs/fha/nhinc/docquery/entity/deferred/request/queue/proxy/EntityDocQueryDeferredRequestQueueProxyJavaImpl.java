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
package gov.hhs.fha.nhinc.docquery.entity.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.docquery.entity.deferred.request.queue.EntityDocQueryDeferredReqQueueOrchImpl;
import gov.hhs.fha.nhinc.docquery.entity.deferred.request.proxy.*;
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
public class EntityDocQueryDeferredRequestQueueProxyJavaImpl implements EntityDocQueryDeferredRequestProxy {

    private static Log log = LogFactory.getLog(EntityDocQueryDeferredRequestQueueProxyJavaImpl.class);

    /**
     *
     * @param msg
     * @param assertion
     * @param targets
     * @return DocQueryAcknowledgementType
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Using Java Implementation for Entity Doc Query Deferred Request Service");
        return new EntityDocQueryDeferredReqQueueOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion, targets);
    }
}
