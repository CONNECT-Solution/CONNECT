/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue.EntityDocRetrieveDeferredReqQueueOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author narendra.reddy
 */
public class EntityDocRetrieveDeferredRequestQueueProxyJavaImpl implements EntityDocRetrieveDeferredRequestQueueProxy {

    private static Log log = LogFactory.getLog(EntityDocRetrieveDeferredRequestQueueProxyJavaImpl.class);

    /**
     *
     * @param request
     * @param assertion
     * @param targets
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Using Java Implementation for Entity Doc Retrieve Deferred Request Service");
        return new EntityDocRetrieveDeferredReqQueueOrchImpl().crossGatewayRetrieveResponse(request, assertion, targets);
    }
}
