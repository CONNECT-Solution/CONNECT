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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocSubmissionRequestProxyNoOpImpl implements AdapterComponentDocSubmissionRequestProxy
{

    private Log log = null;

    /**
     * Default constructor.
     */
    public AdapterComponentDocSubmissionRequestProxyNoOpImpl()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Receive document deferred document submission request.
     *
     * @param body The doc submission message.
     * @param assertion The assertion information.
     * @param url The URL if using LiFT.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, String url)
    {

        log.debug("Entering AdapterComponentDocSubmissionRequestProxyNoOpImpl.provideAndRegisterDocumentSetBRequest");
        return new XDRAcknowledgementType();
    }
}
