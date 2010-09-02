/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy.AdapterComponentDocSubmissionRequestProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy.AdapterComponentDocSubmissionRequestProxyObjectFactory;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterDocSubmissionDeferredRequestOrchImpl
{

    private Log log = null;

    public AdapterDocSubmissionDeferredRequestOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(this.getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, String liftURL, AssertionType assertion)
    {
        log.debug("Begin AdapterDocSubmissionDeferredRequestOrchImpl.provideAndRegisterDocumentSetBRequest");

        XDRAcknowledgementType ack = null;

        AdapterComponentDocSubmissionRequestProxyObjectFactory oFactory = new AdapterComponentDocSubmissionRequestProxyObjectFactory();
        AdapterComponentDocSubmissionRequestProxy oProxy = oFactory.getAdapterComponentDocSubmissionRequestProxy();
        ack = oProxy.provideAndRegisterDocumentSetBRequest(body, assertion, liftURL);

        log.debug("End AdapterDocSubmissionDeferredRequestOrchImpl.provideAndRegisterDocumentSetBRequest");
        return ack;
    }
}
