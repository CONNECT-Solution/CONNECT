/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryImpl
{

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryImpl.class);

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, WebServiceContext context)
    {
        log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        NhinPatientDiscoveryOrchImpl oOrchestrator = new NhinPatientDiscoveryOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.respondingGatewayPRPAIN201305UV02(body, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;
    }
}
