/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.AdapterDocSubmissionDeferredRequestErrorOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterDocSubmissionDeferredRequestErrorProxyJavaImpl implements AdapterDocSubmissionDeferredRequestErrorProxy
{
    private Log log = null;

    public AdapterDocSubmissionDeferredRequestErrorProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(ProvideAndRegisterDocumentSetRequestType request, String errorMessage, AssertionType assertion)
    {
        log.debug("Begin AdapterDocSubmissionDeferredRequestErrorProxyJavaImpl.provideAndRegisterDocumentSetBRequestError");
        XDRAcknowledgementType ack = new AdapterDocSubmissionDeferredRequestErrorOrchImpl().provideAndRegisterDocumentSetBRequestError(request, errorMessage, assertion);
        log.debug("End AdapterDocSubmissionDeferredRequestErrorProxyJavaImpl.provideAndRegisterDocumentSetBRequestError");
        return ack;
    }
}
