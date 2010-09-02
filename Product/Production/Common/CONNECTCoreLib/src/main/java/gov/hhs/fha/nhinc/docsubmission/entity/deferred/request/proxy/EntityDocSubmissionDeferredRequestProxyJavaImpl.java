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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.EntityDocSubmissionDeferredRequestOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredRequestProxyJavaImpl implements EntityDocSubmissionDeferredRequestProxy {
    private static Log log = LogFactory.getLog(EntityDocSubmissionDeferredRequestProxyJavaImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        log.debug("Using Java Implementation for Entity Doc Submission Deferred Request Service");

        return new EntityDocSubmissionDeferredRequestOrchImpl().provideAndRegisterDocumentSetBAsyncRequest(request, assertion, targets, urlInfo);
    }

}
