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
package gov.hhs.fha.nhinc.admingui.services.impl;

/**
 *
 * @author tjafri
 */
import gov.hhs.fha.nhinc.admingui.services.PatientService;
import gov.hhs.fha.nhinc.admingui.services.exception.PatientSearchException;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.builder.impl.NhinTargetCommunitiesBuilderImpl;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.entity.proxy.EntityPatientDiscoveryProxyWebServiceUnsecuredImpl;
import gov.hhs.fha.nhinc.patientdiscovery.messaging.builder.impl.PRPAIN201305UV02BuilderImpl;
import gov.hhs.fha.nhinc.patientdiscovery.messaging.director.PatientDiscoveryMessageDirector;
import gov.hhs.fha.nhinc.patientdiscovery.messaging.director.impl.PatientDiscoveryMessageDirectorImpl;
import gov.hhs.fha.nhinc.patientdiscovery.model.Patient;
import gov.hhs.fha.nhinc.patientdiscovery.model.PatientSearchResults;
import gov.hhs.fha.nhinc.patientdiscovery.model.builder.PatientSearchResultsModelBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.model.builder.impl.PatientSearchResultsModelBuilderImpl;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PatientServiceImpl is responsible for building a PRPAIN201305UV02 and making a PatientDiscovery call by employing
 * EntityPatientDiscoveryProxyWebServiceUnsecuredImpl.
 *
 * @author tjafri
 */
public class PatientServiceImpl implements PatientService {

    private PatientSearchResultsModelBuilder resultsBuilder;
    private PatientDiscoveryMessageDirector pdMessageDirector;
    private static Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

    /**
     * Returns PatientSearchResults object
     * <p>
     * Usage: Patient p = new Patient(); p.setFirstName("abc"); p.setLastName("xyz"); p.setOrganization("urn:oid:2.2");
     * queryPatient(p);
     *
     * @param query Patient object populated with patient search criteria.
     * @return PatientSearchResults
     * @see Patient
     * @see PatientSearchResults
     */
    @Override
    public PatientSearchResults queryPatient(Patient query, AssertionType assertion) throws PatientSearchException {

        LOG.trace("Entering PatientServiceImpl.queryPatient()");
        pdMessageDirector = new PatientDiscoveryMessageDirectorImpl();
        pdMessageDirector.setAssertion(assertion);
        NhinTargetCommunitiesBuilderImpl targetCommunity = new NhinTargetCommunitiesBuilderImpl();
        if (NullChecker.isNullish(query.getOrganization())) {
            throw new PatientSearchException("Organization is a required field");
        }
        targetCommunity.setTarget(query.getOrganization());
        pdMessageDirector.setTargetCommunitiesBuilder(targetCommunity);

        setPRPAINBuilder(query);
        pdMessageDirector.build();
        RespondingGatewayPRPAIN201305UV02RequestType request = pdMessageDirector.getMessage();
        EntityPatientDiscoveryProxyWebServiceUnsecuredImpl instance = new EntityPatientDiscoveryProxyWebServiceUnsecuredImpl();
        RespondingGatewayPRPAIN201306UV02ResponseType response = instance.respondingGatewayPRPAIN201305UV02(
            request.getPRPAIN201305UV02(), request.getAssertion(), request.getNhinTargetCommunities());
        resultsBuilder = new PatientSearchResultsModelBuilderImpl();
        resultsBuilder.setMessage(response.getCommunityResponse().get(0).getPRPAIN201306UV02());
        resultsBuilder.build();
        return resultsBuilder.getPatientSearchResultModel();
    }

    protected void setPRPAINBuilder(Patient query) {
        PRPAIN201305UV02BuilderImpl builder = new PRPAIN201305UV02BuilderImpl();
        builder.setPatient(query);
        pdMessageDirector.setPRPAIN201305UV02Builder(builder);
    }
}
