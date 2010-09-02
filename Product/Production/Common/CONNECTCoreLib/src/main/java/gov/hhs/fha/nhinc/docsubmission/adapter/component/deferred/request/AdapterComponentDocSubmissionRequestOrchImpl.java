/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the Java implementation for the AdapterComponentXDRRequest service.  This
 * is intended to be overridden by the adapter.  It really does nothing but returns
 * the ACK message.
 *
 * @author Les Westberg
 */
public class AdapterComponentDocSubmissionRequestOrchImpl
{
   private static Log log = LogFactory.getLog(AdapterComponentDocSubmissionRequestOrchImpl.class);

    /**
     * This method recieves the document information
     * @param body The XDR request message
     * @param assertion The assertion information.
     * @param url The URL if using LiFT.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, String url)
    {
        log.debug("Entering AdapterComponentXDRRequestOrchImpl.provideAndRegisterDocumentSetBRequest");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        return response;
    }
}
