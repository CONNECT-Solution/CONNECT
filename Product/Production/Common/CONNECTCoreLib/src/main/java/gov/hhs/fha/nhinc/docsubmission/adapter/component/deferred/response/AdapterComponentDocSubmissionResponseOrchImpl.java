/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is the Java implementation for the AdapterComponentXDRResponse service.  This
 * is intended to be overridden by the adapter.  It really does nothing but returns
 * the ACK message.
 *
 * @author Les Westberg
 */
public class AdapterComponentDocSubmissionResponseOrchImpl
{
   private static Log log = LogFactory.getLog(AdapterComponentDocSubmissionResponseOrchImpl.class);

    /**
     * This method receives an AdapterComponentXDRResponse and returns an ACK.
     *
     * @param body The actual response message.
     * @param assertion The assertion information.
     * @return The ACK.
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, AssertionType assertion)
    {
        log.debug("Entering AdapterComponentXDRResponseOrchImpl.provideAndRegisterDocumentSetBResponse");
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        return response;
    }
}
