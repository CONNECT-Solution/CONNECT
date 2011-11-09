/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.mpi.adapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxy;

/**
 * This is the business logic for the AdapterMpi.  This is a thin layer,
 * it simply takes the request and calls the AdapterComponentMpi.
 *
 * @author Les Westberg
 */
public class AdapterMpiOrchImpl
{
    private static Log log = LogFactory.getLog(AdapterMpiOrchImpl.class);

    /**
     * Send the patient query request to the actual MPI that is implemented
     *
     * @param findCandidatesRequest The request containing the query information.
     * @param assertion The assertion for this message.
     * @return The results of the query.
     */
    public PRPAIN201306UV02 query(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion)
    {
        log.debug("Entering AdapterMpiOrchImpl.query method...");

        AdapterComponentMpiProxy oMpiProxy = null;
        AdapterComponentMpiProxyObjectFactory oFactory = new AdapterComponentMpiProxyObjectFactory();
        oMpiProxy = oFactory.getAdapterComponentMpiProxy();
        PRPAIN201306UV02 oResponse = oMpiProxy.findCandidates(findCandidatesRequest, assertion);
        return oResponse;
    }
}
