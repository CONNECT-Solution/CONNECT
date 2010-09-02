/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is the orchestration code for the patient discovery.
 * 
 * @author westberg
 */
public class AdapterPatientDiscoveryOrchImpl
{
   private static Log log = LogFactory.getLog(AdapterPatientDiscoveryOrchImpl.class);

   /**
     * Since this method is for pass through mode.  There is nothing we do with it.
     * It is intended that this class and the web services are completely overridden
     * by the customer.  We are just returning an empty result.
     * 
     * @param request The patient discovery message that came into the gateway.
     * @param assertion The assertion information.
     * @return An emtpy response
     */
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
    {
        log.debug("Entering AdapterPatientDiscoveryOrchImpl.respondingGatewayPRPAIN201305UV02");
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        return response;
    }

}
