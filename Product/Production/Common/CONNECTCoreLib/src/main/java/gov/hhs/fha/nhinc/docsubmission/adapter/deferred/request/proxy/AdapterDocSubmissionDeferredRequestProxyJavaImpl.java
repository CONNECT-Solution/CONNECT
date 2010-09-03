/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.AdapterDocSubmissionDeferredRequestOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterDocSubmissionDeferredRequestProxyJavaImpl implements AdapterDocSubmissionDeferredRequestProxy
{
    private Log log = null;

    public AdapterDocSubmissionDeferredRequestProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(this.getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType request, String liftURL, AssertionType assertion)
    {
        log.debug("Begin AdapterDocSubmissionDeferredRequestProxyJavaImpl.provideAndRegisterDocumentSetBRequest");
        XDRAcknowledgementType response = new AdapterDocSubmissionDeferredRequestOrchImpl().provideAndRegisterDocumentSetBRequest(request, liftURL, assertion);

        log.debug("End AdapterDocSubmissionDeferredRequestProxyJavaImpl.provideAndRegisterDocumentSetBRequest");
        return response;
    }

}
