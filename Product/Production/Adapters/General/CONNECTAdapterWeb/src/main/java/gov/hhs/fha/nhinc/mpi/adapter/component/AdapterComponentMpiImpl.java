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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * This class is the implementation of the AdapterComponentMpi.  It performs any
 * web service specific stuff necessary and then calls the underlying java
 * implementation for this service.
 *
 * @author Les Westberg
 */
public class AdapterComponentMpiImpl
{

    private static Log log = LogFactory.getLog(AdapterComponentMpiImpl.class);

    /**
     * Perform a look up on the MPI.
     * 
     * @param bIsSecure  TRUE if this is being called from a secure web service.
     * @param findCandidatesRequest The information about the patient that is being used for the lookup.
     * @param context The web service context information.
     * @return The results of the lookup.
     */
    public PRPAIN201306UV02 findCandidates(boolean bIsSecure, org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest, WebServiceContext context)
    {
        log.debug("Entering AdapterComponentMpiImpl.findCandidates");

        AssertionType assertion = null;
        if ((bIsSecure) && (context != null))
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            assertion = new AssertionType();
        }

        AdapterComponentMpiOrchImpl oOrchestrator = new AdapterComponentMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.findCandidates(findCandidatesRequest, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterComponentMpiImpl.findCandidates");
        return response;
    }
}
