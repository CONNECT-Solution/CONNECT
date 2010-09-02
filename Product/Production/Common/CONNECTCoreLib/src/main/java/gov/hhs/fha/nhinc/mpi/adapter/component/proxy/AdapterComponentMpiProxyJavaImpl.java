/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.mpi.adapter.component.proxy;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.mpi.adapter.component.AdapterComponentMpiOrchImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used as a java implementation for calling the Adapter MPI.
 * Obviously in order to use the java implementation the caller and the MPI
 * must be on the same system.
 *
 * @author Les Westberg
 */
public class AdapterComponentMpiProxyJavaImpl implements AdapterComponentMpiProxy
{
    private Log log = null;

    /**
     * Default constructor.
     */
    public AdapterComponentMpiProxyJavaImpl()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Find the matching candidates from the MPI.
     *
     * @param request The information to use for matching.
     * @param assertion The assertion data.
     * @return The matches that are found.
     */
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion)
    {
        log.debug("Entering AdapterComponentMpiProxyJavaImpl.findCandidates");
        AdapterComponentMpiOrchImpl oOrchestrator = new AdapterComponentMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.findCandidates(findCandidatesRequest, assertion);
        log.debug("Leaving AdapterComponentMpiProxyJavaImpl.findCandidates");
        return response;
    }
}
