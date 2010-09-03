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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docsubmission.entity.deferred.response.EntityDocSubmissionDeferredResponseOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class EntityDocSubmissionDeferredResponseProxyJavaImpl implements EntityDocSubmissionDeferredResponseProxy {
    private static Log log = LogFactory.getLog(EntityDocSubmissionDeferredResponseProxyJavaImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RegistryResponseType request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Using NoOp Implementation for Entity Doc Submission Deferred Response Service");
        return new EntityDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBAsyncResponse(request, assertion, targets);
    }

}
