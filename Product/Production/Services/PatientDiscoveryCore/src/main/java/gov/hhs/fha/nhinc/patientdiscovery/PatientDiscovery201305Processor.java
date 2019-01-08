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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.mpi.adapter.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.adapter.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7ReceiverTransforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.COCTMT090300UV01AssignedDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueEvent;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MFMIMT700711UV01AuthorOrPerformer;
import org.hl7.v3.MFMIMT700711UV01Reason;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.XParticipationAuthorPerformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscovery201305Processor implements PatientDiscoveryProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDiscovery201305Processor.class);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryProcessor#process201305(org.hl7.v3.PRPAIN201305UV02,
     * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
     */
    @Override
    public PRPAIN201306UV02 process201305(PRPAIN201305UV02 request, AssertionType assertion)
        throws PatientDiscoveryException {
        // Set the sender and receiver OID for the response
        String senderOID = extractSenderOID(request);
        String receiverOID = extractReceiverOID(request);

        // Query the MPI to see if we have a match
        PRPAIN201306UV02 response = queryMpi(request, assertion);

        // Check to see if the a valid response was returned from the local MPI
        if (response != null && response.getControlActProcess() != null) {

            if (!checkEmptySubject(response)) {
                // Make sure patients are found and then
                // check to make sure the policy is valid
                response = checkPolicyFormatResponse(request, assertion, response);
            } else {
                LOG.warn("No match is found by local MPI");
            }
        } else {
            LOG.warn("Response from local MPI is null; generating empty response");
            response = createEmpty201306(senderOID, receiverOID, request);
            response = addValidationReasonOf(response);
        }
        return response;
    }

    /**
     * @param request
     * @param assertion
     * @param response
     * @return
     */
    private PRPAIN201306UV02 checkPolicyFormatResponse(PRPAIN201305UV02 request, AssertionType assertion,
        PRPAIN201306UV02 response) {
        PRPAIN201306UV02 formatResponse = response;
        if (checkPolicy(formatResponse, assertion)) {
            // Store Assigning Authority to Home Community Id Mapping
            storeMapping(request);
            try {
                II requestPatId = providedPatientId(request);
                if (requestPatId != null) {
                    // Create a patient correlation
                    createPatientCorrelation(formatResponse, assertion, request);
                }
            } catch (Exception ex) {
                LOG.error("Exception found in policy check: {} ", ex.getMessage(), ex);
            }
            formatResponse = addAuthorOrPerformer(formatResponse);
        } else {
            // Policy check on all matching patient ids has failed
            LOG.warn("Policy Check Failed");
            formatResponse = addPolicyDenyReasonOf(formatResponse);
        }
        return formatResponse;
    }

    protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
        boolean isPermit = false;
        PatientDiscovery201306PolicyChecker policyChecker = PatientDiscovery201306PolicyChecker.getInstance();

        // ************************************************************************************************
        boolean hasEmptySubject = checkEmptySubject(response);
        List<PRPAIN201306UV02MFMIMT700711UV01Subject1> pRPAINSubjects = new ArrayList<>();
        if (!hasEmptySubject) {
            pRPAINSubjects = response.getControlActProcess().getSubject();
            LOG.debug("checkPolicy - Before policy Check-Subjects size: {}", pRPAINSubjects.size());
        } else {
            LOG.debug("checkPolicy - Before policy Check-response/subjects is null");
        }

        List<PRPAIN201306UV02MFMIMT700711UV01Subject1> delPRPAINSubjects = new ArrayList<>();
        for (PRPAIN201306UV02MFMIMT700711UV01Subject1 pRPAINSubject : pRPAINSubjects) {
            int pRPAINSubjectInd = -1;
            if (!hasEmptySubject) {
                pRPAINSubjectInd = response.getControlActProcess().getSubject().indexOf(pRPAINSubject);
            }
            LOG.debug("checkPolicy - SubjectIndex: {}", pRPAINSubjectInd);

            PRPAIN201306UV02MFMIMT700711UV01Subject1 subjReplaced = response.getControlActProcess().getSubject().set(0,
                pRPAINSubject);
            response.getControlActProcess().getSubject().set(pRPAINSubjectInd, subjReplaced);

            if (policyChecker.check201305Policy(response, assertion)) {
                LOG.debug("checkPolicy -policy returns permit for patient: {}", pRPAINSubjectInd);
            } else {
                LOG.debug("checkPolicy -policy returns deny for patient: {}", pRPAINSubjectInd);
                delPRPAINSubjects.add(pRPAINSubject);
            }

            response.getControlActProcess().getSubject().set(pRPAINSubjectInd, pRPAINSubject);
            response.getControlActProcess().getSubject().set(0, subjReplaced);
        }

        if (!hasEmptySubject && NullChecker.isNotNullish(delPRPAINSubjects)) {
            LOG.debug("checkPolicy - removing policy denied subjects. Ploicy denied subjects size: {} ",
                delPRPAINSubjects.size());

            response.getControlActProcess().getSubject().removeAll(delPRPAINSubjects);
        }

        if (!hasEmptySubject) {
            pRPAINSubjects = response.getControlActProcess().getSubject();
            LOG.debug("checkPolicy - after policy Check-Subjects size: {} ", pRPAINSubjects.size());
            if (!pRPAINSubjects.isEmpty()) {
                isPermit = true;
            }
        } else {
            LOG.debug("checkPolicy - after policy Check-response/subjects is null");
        }
        // ************************************************************************************************

        return isPermit;
    }

    protected boolean checkEmptySubject(PRPAIN201306UV02 response) {
        boolean isSubjectEmpty = true;

        // ************************************************************************************************
        if (response != null && response.getControlActProcess() != null
            && NullChecker.isNotNullish(response.getControlActProcess().getSubject())) {
            isSubjectEmpty = false;
            LOG.debug("checkEmptySubject - Check-response/subjects is not null");
        } else {
            LOG.debug("checkEmptySubject - Check-response/subjects is null");
        }
        return isSubjectEmpty;
    }

    protected PRPAIN201306UV02 createEmpty201306(String senderOID, String receiverOID, PRPAIN201305UV02 request) {
        // Switch the sender and receiver OIDs before calling the transformation method because the response is going in
        // the opposite direction.
        return HL7PRPA201306Transforms.createPRPA201306(null, receiverOID, null, senderOID, null, request);
    }

    protected void storeMapping(PRPAIN201305UV02 request) {
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String hcid = null;
        String assigningAuthority = null;

        if (request != null) {
            hcid = extractSenderOID(request);
            assigningAuthority = extractAAOID(request);
        }

        if (NullChecker.isNotNullish(hcid) && NullChecker.isNotNullish(assigningAuthority)) {
            boolean result = mappingDao.storeMapping(hcid, assigningAuthority);

            if (result == false) {
                LOG.warn("Failed to store home community - assigning authority mapping");
            }

        }
    }

    /**
     * Method to store local AA and HCID mapping
     *
     * @param request
     * @return
     */
    public void storeLocalMapping(RespondingGatewayPRPAIN201305UV02RequestType request) {
        LOG.debug("Begin storeLocalMapping");

        String hcid = HomeCommunityMap.getLocalHomeCommunityId();
        LOG.debug("Begin storeLocalMapping hcid : {} ", hcid);

        II patId = extractPatientIdFrom201305(request.getPRPAIN201305UV02());

        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();

        if (patId == null) {
            LOG.warn("AssigningAuthorityHomeCommunityMappingDAO or Local Patient Id was null. Mapping was not stored.");
        } else {
            if (!mappingDao.storeMapping(hcid, patId.getRoot())) {
                LOG.warn("Failed to store home community - assigning authority mapping");
            }
        }

        LOG.debug("End storeLocalMapping");
    }

    protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion)
        throws PatientDiscoveryException {
        return queryMpiForPatients(query, assertion);
    }

    public PRPAIN201306UV02 queryMpiForPatients(PRPAIN201305UV02 query, AssertionType assertion)
        throws PatientDiscoveryException {
        PRPAIN201306UV02 queryResults;

        if (query != null) {
            // Query the MPI to see if the patient is found
            AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
            AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
            LOG.info("Sending query to the Secured MPI");
            queryResults = mpiProxy.findCandidates(query, assertion);

        } else {
            LOG.error("MPI Request is null");
            queryResults = null;
        }

        return queryResults;
    }

    protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II localPatId, AssertionType assertion,
        PRPAIN201305UV02 query) {
        PRPAIN201301UV02 request;
        String localAA = null;

        if (queryResult != null && localPatId != null && NullChecker.isNotNullish(localPatId.getRoot())
            && NullChecker.isNotNullish(localPatId.getExtension()) && assertion != null) {
            if (queryResult.getControlActProcess() != null
                && NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject())
                && queryResult.getControlActProcess().getSubject().get(0) != null
                && queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
                && queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
                && queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient() != null
                && NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(0)
                    .getRegistrationEvent().getSubject1().getPatient().getId())
                && queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0) != null
                && NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(0)
                    .getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                localAA = queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                    .getPatient().getId().get(0).getRoot();
            }
            request = HL7PRPA201301Transforms.createPRPA201301(query, localAA);

            if (request != null && request.getControlActProcess() != null
                && NullChecker.isNotNullish(request.getControlActProcess().getSubject())
                && request.getControlActProcess().getSubject().get(0) != null
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient() != null
                && NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                    .getSubject1().getPatient().getId())) {
                // Need to Switch patient ids so the sender and reciever match. This is to avoid an exception by the
                // Patient Correlation Component
                II remotePatient = request.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                    .getSubject1().getPatient().getId().get(0);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().clear();
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().add(localPatId);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().add(remotePatient);

                PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();

                patCorrelationProxy.addPatientCorrelation(request, assertion);
            }
        } else {
            LOG.error("Null parameter passed to createPatientCorrelation method, no correlation created");
        }
    }

    protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion,
        PRPAIN201305UV02 query) {
        if (queryResult != null && assertion != null) {
            int subjectsSize = 0;
            if (queryResult.getControlActProcess() != null
                && NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject())) {
                subjectsSize = queryResult.getControlActProcess().getSubject().size();
            }

            for (int i = 0; i < subjectsSize; i++) {
                processLocalRemotePatientId(queryResult, assertion, query, i);
            }
        } else {
            LOG.error("Null parameter passed to createPatientCorrelation method, no correlation created");
        }
    }

    /**
     * @param queryResult
     * @param assertion
     * @param query
     * @param i
     */
    private static void processLocalRemotePatientId(PRPAIN201306UV02 queryResult, AssertionType assertion,
        PRPAIN201305UV02 query, int i) {
        PRPAIN201301UV02 request;
        II localPatId = new II();
        boolean checkQueryResultRegistrationEvent = NullChecker
            .isNotNullish(queryResult.getControlActProcess().getSubject())
            && queryResult.getControlActProcess().getSubject().get(i) != null
            && queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent() != null
            && queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1() != null
            && queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1()
            .getPatient() != null
            && NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                .getSubject1().getPatient().getId())
            && queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient()
            .getId().get(0) != null
            && NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getExtension())
            && NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getRoot());
        if (queryResult.getControlActProcess() != null && checkQueryResultRegistrationEvent) {
            localPatId.setExtension(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getExtension());
            localPatId.setRoot(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getRoot());
            LOG.debug("local AA {}: {}, pId: {}", i, localPatId.getRoot(), localPatId.getExtension());
        }

        if (localPatId.getRoot() != null && localPatId.getExtension() != null) {

            request = HL7PRPA201301Transforms.createPRPA201301(query, localPatId.getRoot());

            boolean checkRequestControlActRegistrationEvent = NullChecker
                .isNotNullish(request.getControlActProcess().getSubject())
                && request.getControlActProcess().getSubject().get(0) != null
                && checkPatientInRegistrationEvent(request);
            if (request.getControlActProcess() != null && checkRequestControlActRegistrationEvent) {
                // Need to Switch patient ids so the sender and reciever match. This is to avoid an exception by
                // the Patient Correlation Component
                II remotePatient = request.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                    .getSubject1().getPatient().getId().get(0);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().clear();
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().add(localPatId);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                .getId().add(remotePatient);
                LOG.debug("Local AA {}: {}, pId: {}", i, localPatId.getRoot(), localPatId.getExtension());
                LOG.debug("Remote AA: {} ", remotePatient.getRoot(), ", pId: {} ", remotePatient.getExtension());
                LOG.debug("Remote AA: {}, pId: {}", remotePatient.getRoot(), remotePatient.getExtension());

                if (remotePatient.getRoot() != null && remotePatient.getExtension() != null) {
                    PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                    PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();
                    patCorrelationProxy.addPatientCorrelation(request, assertion);
                } else {
                    LOG.error("Remote patient identifiers are null. Could not correlate the patient identifiers.");
                }
            } else {
                LOG.error(
                    "Request (PRPAIN201301UV02) or remote patient identifiers are null. Could not correlate the patient identifiers.");
            }
        } else {
            LOG.error("Local patient identifiers are null. Could not correlate the patient identifiers.");
        }
    }

    private static boolean checkPatientInRegistrationEvent(PRPAIN201301UV02 request) {
        boolean result = false;

        if (request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null) {

            result = request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient() != null
                && NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                    .getSubject1().getPatient().getId());
        }
        return result;
    }

    private II providedPatientId(PRPAIN201305UV02 request) {
        II patId = null;
        String aaId;

        if (request != null && request.getControlActProcess() != null) {

            aaId = extractAAOID(request);

            if (NullChecker.isNotNullish(aaId) && requestHasLivingId(request) && aaId != null) {
                for (PRPAMT201306UV02LivingSubjectId livingSubId : request.getControlActProcess().getQueryByParameter()
                    .getValue().getParameterList().getLivingSubjectId()) {
                    for (II id : livingSubId.getValue()) {
                        if (id != null && NullChecker.isNotNullish(id.getRoot())
                            && NullChecker.isNotNullish(id.getExtension()) && aaId.contentEquals(id.getRoot())) {
                            patId = new II();
                            patId.setRoot(id.getRoot());
                            patId.setExtension(id.getExtension());

                            // break out of inner loop
                            break;
                        }
                    }

                    // If the patient id was found then break out of outer loop
                    if (patId != null) {
                        break;
                    }
                }
            }

        }

        return patId;
    }

    private static boolean requestHasLivingId(PRPAIN201305UV02 request) {
        return request.getControlActProcess().getQueryByParameter() != null
            && request.getControlActProcess().getQueryByParameter().getValue() != null
            && request.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null
            && NullChecker.isNotNullish(request.getControlActProcess().getQueryByParameter().getValue()
                .getParameterList().getLivingSubjectId());
    }

    public PRPAIN201305UV02 createNewRequest(PRPAIN201305UV02 request, String targetCommunityId) {
        PRPAIN201305UV02 newRequest = request;

        if (request != null && NullChecker.isNotNullish(targetCommunityId)) {
            // the new request will have the target community as the only receiver
            newRequest.getReceiver().clear();
            MCCIMT000100UV01Receiver oNewReceiver = HL7ReceiverTransforms
                .createMCCIMT000100UV01Receiver(targetCommunityId);
            newRequest.getReceiver().add(oNewReceiver);
            LOG.debug("Created a new request for target communityId: {}", targetCommunityId);
        } else {
            LOG.error(
                "A null input paramter was passed to the method: createNewRequest in class: PatientDiscovery201305Processor");
            return null;
        }

        return newRequest;
    }

    public II extractPatientIdFrom201305(PRPAIN201305UV02 request) {
        return providedPatientId(request);
    }

    private static String extractSenderOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null && request.getSender() != null && request.getSender().getDevice() != null
            && request.getSender().getDevice().getAsAgent() != null
            && request.getSender().getDevice().getAsAgent().getValue() != null
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(
                request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId())
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
            .get(0) != null
            && NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot();
        }

        return oid;
    }

    private static String extractReceiverOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null && NullChecker.isNotNullish(request.getReceiver()) && request.getReceiver().get(0) != null
            && request.getReceiver().get(0).getDevice() != null
            && request.getReceiver().get(0).getDevice().getAsAgent() != null
            && request.getReceiver().get(0).getDevice().getAsAgent().getValue() != null
            && request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue() != null
            && NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0) != null
            && NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot();
        }

        return oid;
    }

    private static String extractAAOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null && request.getControlActProcess() != null
            && NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer())
            && request.getControlActProcess().getAuthorOrPerformer().get(0) != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null
            && NullChecker.isNotNullish(
                request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId())
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()
            .get(0) != null
            && NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice()
                .getValue().getId().get(0).getRoot())) {
            oid = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()
                .get(0).getRoot();
        }

        return oid;
    }

    private static PRPAIN201306UV02 addAuthorOrPerformer(PRPAIN201306UV02 response) {
        MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
        authorOrPerformer.setTypeCode(XParticipationAuthorPerformer.AUT);

        COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
        II id = new II();
        if (response != null && response.getControlActProcess() != null
            && NullChecker.isNotNullish(response.getControlActProcess().getSubject())
            && response.getControlActProcess().getSubject().get(0) != null
            && response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
            && response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
            && response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
            .getPatient() != null
            && NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId())
            && response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
            .getId().get(0) != null
            && NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getRoot())) {
            id.setRoot(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getRoot());
        }
        assignedDevice.setClassCode(HL7Constants.ASSIGNED_DEVICE_CLASS_CODE);
        assignedDevice.getId().add(id);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedDevice");
        JAXBElement<COCTMT090300UV01AssignedDevice> assignedDeviceJAXBElement = new JAXBElement<>(xmlqname,
            COCTMT090300UV01AssignedDevice.class, assignedDevice);

        authorOrPerformer.setAssignedDevice(assignedDeviceJAXBElement);

        if (response != null && response.getControlActProcess() != null
            && response.getControlActProcess().getAuthorOrPerformer() != null) {
            response.getControlActProcess().getAuthorOrPerformer().add(authorOrPerformer);
        }
        return response;
    }

    private static PRPAIN201306UV02 addPolicyDenyReasonOf(PRPAIN201306UV02 response) {

        MFMIMT700711UV01Reason reasonOf = new MFMIMT700711UV01Reason();
        MCAIMT900001UV01DetectedIssueEvent detectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
        detectedIssueEvent
        .setCode(HL7DataTransformHelper.CDFactory(HL7Constants.DETECTED_ISSUE_EVENT_CODE_AUTHORIZATION,
            HL7Constants.DETECTED_ISSUE_EVENT_OID, HL7Constants.DETECTED_ISSUE_EVENT_CODE_AUTHORIZATION_DESC));
        reasonOf.setDetectedIssueEvent(detectedIssueEvent);

        response.getControlActProcess().getReasonOf().add(reasonOf);

        return response;
    }

    private static PRPAIN201306UV02 addValidationReasonOf(PRPAIN201306UV02 response) {

        MFMIMT700711UV01Reason reasonOf = new MFMIMT700711UV01Reason();
        MCAIMT900001UV01DetectedIssueEvent detectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
        detectedIssueEvent.setCode(HL7DataTransformHelper.CDFactory(HL7Constants.DETECTED_ISSUE_EVENT_CODE_VALIDATION,
            HL7Constants.DETECTED_ISSUE_EVENT_OID, HL7Constants.DETECTED_ISSUE_EVENT_CODE_VALIDATION_DESC));
        reasonOf.setDetectedIssueEvent(detectedIssueEvent);

        response.getControlActProcess().getReasonOf().add(reasonOf);

        return response;
    }
}
