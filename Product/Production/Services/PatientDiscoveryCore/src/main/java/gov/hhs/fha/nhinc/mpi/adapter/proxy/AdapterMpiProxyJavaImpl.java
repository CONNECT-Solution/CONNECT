/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.mpi.adapter.proxy;

import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.mpi.adapter.AdapterMpiOrchImpl;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02AdapterEventDescBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a java implementation for calling the Adapter MPI. Obviously in order to use the java
 * implementation the caller and the MPI must be on the same system.
 *
 * @author Les Westberg
 */
public class AdapterMpiProxyJavaImpl implements AdapterMpiProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterMpiProxyJavaImpl.class);

    /**
     * Find the matching candidates from the MPI.
     *
     * @param findCandidatesRequest The information to use for matching.
     * @param assertion The assertion data.
     * @return The matches that are found.
     */
    @Override
    @AdapterDelegationEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
        afterReturningBuilder = PRPAIN201305UV02AdapterEventDescBuilder.class,
        serviceType = "Patient Discovery MPI", version = "1.0")
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion) {
        LOG.trace("Entering AdapterMpiProxyJavaImpl.findCandidates");
        return findCandidatesMpi(findCandidatesRequest, assertion);
    }

    /**
     * The sole purpose of this method is to pass NhinTargetSystemType for Adapter Delegation Events. We need
     * NhinTargetSystemType to log responding_hcids for BEGIN and END ADAPTER DELEGATION EVENTS.
     *
     * @param findCandidatesRequest The information to use for matching.
     * @param assertion The assertion data.
     * @param nhinTargetSystem
     * @return The matches that are found.
     */
    @Override
    @AdapterDelegationEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
        afterReturningBuilder = PRPAIN201305UV02AdapterEventDescBuilder.class,
        serviceType = "Patient Discovery MPI", version = "1.0")
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion,
        NhinTargetSystemType nhinTargetSystem) {
        LOG.trace("Entering AdapterMpiProxyJavaImpl.findCandidates");
        return findCandidatesMpi(findCandidatesRequest, assertion);
    }

    private static PRPAIN201306UV02 findCandidatesMpi(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertion) {
        AdapterMpiOrchImpl oOrchestrator = new AdapterMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.query(findCandidatesRequest, assertion);
        LOG.trace("Leaving AdapterMpiProxyJavaImpl.findCandidates");
        return response;
    }
}
