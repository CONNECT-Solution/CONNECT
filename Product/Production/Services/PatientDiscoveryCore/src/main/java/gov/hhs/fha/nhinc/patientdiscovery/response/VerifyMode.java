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
package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.mpi.adapter.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.adapter.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class VerifyMode implements ResponseMode {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyMode.class);

    public VerifyMode() {
        super();
    }

    @Override
    public PRPAIN201306UV02 processResponse(ResponseParams params) {
        PRPAIN201306UV02 response = params.response;
        AssertionType assertion = params.assertion;
        PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

        return processResponse(requestMsg, response, assertion, params.origRequest.getNhinTargetSystem());
    }

    @Override
    public PRPAIN201306UV02 processResponse(PRPAIN201306UV02 response, AssertionType assertion, II localPatientId) {
        LOG.debug("begin processResponse");

        PRPAIN201306UV02 result = response;

        List<II> requestPatientIds = new ArrayList<>();
        if (localPatientId != null) {
            requestPatientIds.add(localPatientId);
        }
        II patId = patientExistsLocally(requestPatientIds, assertion, response, null);

        if (patId != null && NullChecker.isNotNullish(patId.getExtension())
            && NullChecker.isNotNullish(patId.getRoot())) {
            LOG.debug("patient exists locally, adding correlation");
            getTrustMode().processResponse(response, assertion, patId);
        } else {
            LOG.warn("Patient does not exist locally, correlation not added");
        }
        return result;
    }

    public PRPAIN201306UV02 processResponse(PRPAIN201305UV02 requestMsg, PRPAIN201306UV02 response,
        AssertionType assertion, NhinTargetSystemType target) {
        LOG.debug("begin processResponse");

        PRPAIN201306UV02 result = response;

        II patId = patientExistsLocally(getPatientIds(requestMsg), assertion, response, target);

        if (patId != null && NullChecker.isNotNullish(patId.getExtension())
            && NullChecker.isNotNullish(patId.getRoot())) {
            LOG.debug("patient exists locally, adding correlation");
            getTrustMode().processResponse(response, assertion, patId);
        } else {
            LOG.warn("Patient does not exist locally, correlation not added");
        }
        return result;
    }

    protected TrustMode getTrustMode() {
        return new TrustMode();
    }

    protected String getLocalHomeCommunityId() {
        String result = "";

        try {
            result = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return result;
    }

    protected String getSenderCommunityId(PRPAIN201306UV02 response) {
        String hcid = null;

        if (response != null && response.getSender() != null && response.getSender().getDevice() != null
            && response.getSender().getDevice().getAsAgent() != null
            && response.getSender().getDevice().getAsAgent().getValue() != null
            && response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue() != null
            && NullChecker.isNotNullish(response.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0) != null
            && NullChecker.isNotNullish(response.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            hcid = response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot();
        }

        return hcid;
    }

    protected PRPAMT201301UV02Patient extractPatient(PRPAIN201306UV02 response) {
        PRPAMT201301UV02Patient patient = null;

        if (response != null && response.getControlActProcess() != null
            && NullChecker.isNotNullish(response.getControlActProcess().getSubject())
            && response.getControlActProcess().getSubject().get(0) != null
            && response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
            && response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
            && response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
            .getPatient() != null) {
            PRPAMT201310UV02Patient remotePatient = response.getControlActProcess().getSubject().get(0)
                .getRegistrationEvent().getSubject1().getPatient();
            patient = HL7PatientTransforms.createPRPAMT201301UVPatient(remotePatient);
        }

        return patient;
    }

    protected PRPAIN201305UV02 convert201306to201305(PRPAIN201306UV02 response) {
        PRPAIN201305UV02 result = null;
        String localHCID = getLocalHomeCommunityId();
        String remoteHCID = getSenderCommunityId(response);
        PRPAMT201301UV02Patient patient = extractPatient(response);

        if (patient != null) {
            result = HL7PRPA201305Transforms.createPRPA201305(patient, remoteHCID, localHCID, localHCID);
        }

        return result;
    }

    protected List<II> getPatientIds(PRPAIN201305UV02 requestMsg) {
        List<II> requestPatientIds = new ArrayList<>();
        List<PRPAMT201306UV02LivingSubjectId> requestSubjectIds;

        if (requestMsg != null && requestMsg.getControlActProcess() != null
            && requestMsg.getControlActProcess().getQueryByParameter() != null
            && requestMsg.getControlActProcess().getQueryByParameter().getValue() != null
            && requestMsg.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null
            && NullChecker.isNotNullish(requestMsg.getControlActProcess().getQueryByParameter().getValue()
                .getParameterList().getLivingSubjectId())) {
            requestSubjectIds = requestMsg.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getLivingSubjectId();
            LOG.debug("query - original Request Ids " + requestSubjectIds.size());

            for (PRPAMT201306UV02LivingSubjectId livingPatId : requestSubjectIds) {
                for (II id : livingPatId.getValue()) {
                    requestPatientIds.add(id);
                }
            }
        }

        return requestPatientIds;
    }

    protected II patientExistsLocally(List<II> localPatientIds, AssertionType assertion, PRPAIN201306UV02 response,
        NhinTargetSystemType target) {
        II patId = null;

        PRPAIN201305UV02 mpiQuery;
        List<II> mpiIds = new ArrayList<>();

        // Check to see if a patient id was specified in the original request
        mpiQuery = convert201306to201305(response);

        // Only query the MPI and attempt a correlation if a Patient Id was present in the original message.
        if (CollectionUtils.isNotEmpty(localPatientIds)) {
            PRPAIN201306UV02 mpiResult = queryMpi(mpiQuery, assertion, target);

            if (mpiResult != null) {
                try {
                    LOG.debug("Received result from mpi.");
                    if (mpiResult.getControlActProcess() != null
                        && NullChecker.isNotNullish(mpiResult.getControlActProcess().getSubject())) {
                        for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subj1 : mpiResult.getControlActProcess()
                            .getSubject()) {
                            if (subj1.getRegistrationEvent() != null
                                && subj1.getRegistrationEvent().getSubject1() != null
                                && subj1.getRegistrationEvent().getSubject1().getPatient() != null
                                && subj1.getRegistrationEvent().getSubject1().getPatient().getId() != null) {
                                mpiIds.add(subj1.getRegistrationEvent().getSubject1().getPatient().getId().get(0));
                            }
                        }
                    }

                    LOG.debug("mpiIds size: " + mpiIds.size());

                    requestIdsSearch:
                        for (II id : localPatientIds) {
                            for (II mpiId : mpiIds) {
                                if (compareId(mpiId, id)) {
                                    patId = mpiId;
                                    break requestIdsSearch;
                                }
                            }
                        }

                } catch (Exception ex) {
                    LOG.error(ex.getMessage(), ex);
                    patId = null;
                }
            }
        }
        return patId;
    }

    protected boolean compareId(List<PRPAMT201306UV02LivingSubjectId> localIds,
        List<PRPAMT201306UV02LivingSubjectId> remoteIds) {
        boolean result = false;

        try {
            for (PRPAMT201306UV02LivingSubjectId localId : localIds) {
                for (PRPAMT201306UV02LivingSubjectId remoteId : remoteIds) {
                    if (compareId(localId, remoteId)) {
                        result = true;
                        break;

                    }
                }
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return result;
    }

    protected boolean compareId(PRPAMT201306UV02LivingSubjectId localId, PRPAMT201306UV02LivingSubjectId remoteId) {
        boolean result = false;

        for (II localII : localId.getValue()) {
            for (II remoteII : remoteId.getValue()) {
                if (compareId(localII, remoteII)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private boolean compareId(II localId, II remoteId) {
        boolean result = false;

        if (localId != null && remoteId != null) {
            String localExt = localId.getExtension();
            String localRoot = localId.getRoot();
            LOG.debug("Comparing Root: " + localRoot + " to " + remoteId.getRoot());
            LOG.debug("Comparing Ext: " + localExt + " to " + remoteId.getExtension());
            if (remoteId.getExtension().equalsIgnoreCase(localExt) && remoteId.getRoot().equalsIgnoreCase(localRoot)) {
                result = true;
            }

        }

        return result;
    }

    protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion, NhinTargetSystemType target) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();

        if (query != null) {
            // Query the MPI to see if the patient is found
            AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
            AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
            LOG.info("Sending query to the Secured MPI");
            try {
                queryResults = mpiProxy.findCandidates(query, assertion, target);
            } catch (PatientDiscoveryException e) {
                LOG.error("Error queries MPI in verify mode.", e);
            }

        } else {
            LOG.error("MPI Request is null");
            queryResults = null;
        }

        return queryResults;
    }
}
