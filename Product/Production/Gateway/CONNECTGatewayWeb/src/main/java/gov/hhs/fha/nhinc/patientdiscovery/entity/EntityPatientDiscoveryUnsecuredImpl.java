/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author Neil Webb
 */
public class EntityPatientDiscoveryUnsecuredImpl
{
    private Log log = null;

    public EntityPatientDiscoveryUnsecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
    {
        return new EntityPatientDiscoveryOrchImpl();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
    {
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        if(respondingGatewayPRPAIN201305UV02Request == null)
        {
            log.warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
        }
        else
        {
            EntityPatientDiscoveryOrchImpl processor = getEntityPatientDiscoveryProcessor();
            if(processor != null)
            {
                response = processor.respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request, respondingGatewayPRPAIN201305UV02Request.getAssertion());
            }
            else
            {
                log.warn("EntityPatientDiscoveryProcessor was null.");
            }
        }
        return response;
    }

}
