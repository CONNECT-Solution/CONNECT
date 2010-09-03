/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author shawc
 */
public class EntityPatientDiscoverySecuredImpl
{
    private Log log = null;

    public EntityPatientDiscoverySecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
    {
        return new EntityPatientDiscoveryOrchImpl();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context)
    {
        log.debug("Entering EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        if (request == null)
        {
            log.error("The incomming request was null.");
        }
        else if (context == null)
        {
            log.error("The incomming WebServiceContext parameter was null.");
            return null;
        }
        else
        {
            AssertionType assertion = extractAssertion(context);

            EntityPatientDiscoveryOrchImpl processor = getEntityPatientDiscoveryProcessor();
            if(processor != null)
            {
                response = processor.respondingGatewayPRPAIN201305UV02(request, assertion);
            }
            else
            {
                log.error("The EntityPatientDiscoveryProcessor was null.");
            }
        }

        log.debug("Exiting EntityPatientDiscoverySecuredImpl.respondingGatewayPRPAIN201305UV02...");
        return response;
    }

    protected AssertionType extractAssertion(WebServiceContext context)
    {
        return SamlTokenExtractor.GetAssertion(context);
    }

}
