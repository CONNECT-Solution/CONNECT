/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Spring Proxy interface for deferred processing of Doc Submission response.
 *
 * @author Les Westberg
 */
public interface AdapterComponentDocSubmissionResponseProxy
{

    /**
     * Receive document deferred document submission response.
     *
     * @param body The doc submission response message.
     * @param assertion The assertion information.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, AssertionType assertion);
}
