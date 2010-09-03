/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subjectdiscovery.nhin;

import gov.hhs.fha.nhinc.adapter.reident.proxy.AdapterReidentProxy;
import gov.hhs.fha.nhinc.adapter.reident.proxy.AdapterReidentProxyObjectFactory;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscovery201309Processor {

    public PRPAIN201310UV02 process201309(PIXConsumerPRPAIN201309UVRequestType request) {
        PRPAIN201310UV02 result = new PRPAIN201310UV02();

        AdapterReidentProxyObjectFactory reidentFactory = new AdapterReidentProxyObjectFactory();
        AdapterReidentProxy proxy = reidentFactory.getAdapterReidentProxy();
        result = proxy.getRealIdentifier(request.getPRPAIN201309UV02(), request.getAssertion(), request.getNhinTargetCommunities());

        return result;
    }
}
