/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.mpi.adapter.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author westberg
 */
public class AdapterComponentMpiOrchImpl
{
    private static Log log = LogFactory.getLog(AdapterComponentMpiOrchImpl.class);

    /**
     * Call the find candidates on the MPI.
     * 
     * @param findCandidatesRequest The find candidates on the MPI
     * @param assertion The assertion information.
     * @return The patients found.
     */
    public PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion)
    {
//        return PatientChecker.FindPatient(findCandidatesRequest);
        log.debug("Entering AdapterComponentMpiOrchImpl.findCandidates method...");

        AdapterComponentMpiChecker oMpiChecker = null;
        AdapterComponentMpiCheckerObjectFactory oFactory = new AdapterComponentMpiCheckerObjectFactory();
        oMpiChecker = oFactory.getAdapterComponentMpiChecker();
        PRPAIN201306UV02 oResponse = oMpiChecker.FindPatient(findCandidatesRequest);
        return oResponse;
    }
}
