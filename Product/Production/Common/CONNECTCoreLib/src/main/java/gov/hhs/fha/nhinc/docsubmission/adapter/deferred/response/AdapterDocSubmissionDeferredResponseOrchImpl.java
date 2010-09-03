/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy.AdapterComponentDocSubmissionResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy.AdapterComponentDocSubmissionResponseProxyObjectFactory;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterDocSubmissionDeferredResponseOrchImpl
{
    private Log log = null;

    public AdapterDocSubmissionDeferredResponseOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType regResponse, AssertionType assertion)
    {
        log.debug("Begin AdapterDocSubmissionDeferredResponseOrchImpl.provideAndRegisterDocumentSetBResponse");
        AdapterComponentDocSubmissionResponseProxyObjectFactory oFactory = new AdapterComponentDocSubmissionResponseProxyObjectFactory();
        AdapterComponentDocSubmissionResponseProxy oProxy = oFactory.getAdapterComponentDocSubmissionResponseProxy();
        XDRAcknowledgementType ack = oProxy.provideAndRegisterDocumentSetBResponse(regResponse, assertion);
        log.debug("End AdapterDocSubmissionDeferredResponseOrchImpl.provideAndRegisterDocumentSetBResponse");
        return ack;
    }
}
