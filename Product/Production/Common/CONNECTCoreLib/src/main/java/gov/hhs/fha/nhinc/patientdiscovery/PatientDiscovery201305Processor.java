/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

/**
 *
 * @author jhoppesc
 */
public class PatientDiscovery201305Processor {

    private static Log log = LogFactory.getLog(PatientDiscovery201305Processor.class);

    /**
     * process201305
     * @param request
     * @param assertion
     * @return org.hl7.PRPAIN201306UV02
     */
    public PRPAIN201306UV02 process201305(PRPAIN201305UV02 request, AssertionType assertion) {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        // Set the sender and receiver OID for the response
        String senderOID = extractSenderOID(request);
        String receiverOID = extractReceiverOID(request);

        // Query the MPI to see if we have a match
        response = queryMpi(request, assertion);

        // Check to see if the a valid response was returned from the local MPI
        if (response != null && response.getControlActProcess() != null) {

            // Check to make sure the policy is valid
            if (checkPolicy(response, assertion)) {
                // Store Assigning Authority to Home Community Id Mapping
                storeMapping(request);

                II requestPatId = providedPatientId(request);
                if (requestPatId != null) {
                    // Create a patient correlation
                    //createPatientCorrelation(response, patIdOverride, assertion, request);
                    createPatientCorrelation(response, assertion, request);
                }
                response = addAuthorOrPerformer(response);
            } else {
                // Policy check on all matching patient ids has failed
                log.warn("Policy Check Failed");
                response = addPolicyDenyReasonOf(response);
            }
        } else {
            log.warn("Response from local MPI is null; generating empty response");
            response = createEmpty201306(senderOID, receiverOID, request);
            response = addValidationReasonOf(response);
        }
        return response;
    }

    protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
        boolean isPermit = false;
        II patId = null;
        PatientDiscoveryPolicyChecker policyChecker = new PatientDiscoveryPolicyChecker();

        //************************************************************************************************
        List<PRPAIN201306UV02MFMIMT700711UV01Subject1> pRPAINSubjects = new ArrayList<PRPAIN201306UV02MFMIMT700711UV01Subject1>();
        if (response != null &&
                response.getControlActProcess() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject())) {
            pRPAINSubjects = response.getControlActProcess().getSubject();
            log.debug("checkPolicy - Before policy Check-Subjects size: " + pRPAINSubjects.size());
        } else {
            log.debug("checkPolicy - Before policy Check-response/subjects is null");
        }

        List<PRPAIN201306UV02MFMIMT700711UV01Subject1> delPRPAINSubjects = new ArrayList<PRPAIN201306UV02MFMIMT700711UV01Subject1>();
        for (PRPAIN201306UV02MFMIMT700711UV01Subject1 pRPAINSubject : pRPAINSubjects) {
            int pRPAINSubjectInd = response.getControlActProcess().getSubject().indexOf(pRPAINSubject);
            log.debug("checkPolicy - SubjectIndex: " + pRPAINSubjectInd);

            PRPAIN201306UV02MFMIMT700711UV01Subject1 subjReplaced = response.getControlActProcess().getSubject().set(0, pRPAINSubject);
            response.getControlActProcess().getSubject().set(pRPAINSubjectInd, subjReplaced);

            // Extract patient for current subject and perform policy check
            patId = NhinPatientDiscoveryUtils.extractPatientIdFromSubject(pRPAINSubject);
            if (policyChecker.check201305Policy(response, patId, assertion)) {
                log.debug("checkPolicy -policy returns permit for patient: " + pRPAINSubjectInd);
            } else {
                log.debug("checkPolicy -policy returns deny for patient: " + pRPAINSubjectInd);
                delPRPAINSubjects.add(pRPAINSubject);
            }

            response.getControlActProcess().getSubject().set(pRPAINSubjectInd, pRPAINSubject);
            response.getControlActProcess().getSubject().set(0, subjReplaced);
        }

        if (response != null &&
                response.getControlActProcess() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject()) &&
                NullChecker.isNotNullish(delPRPAINSubjects)) {
            log.debug("checkPolicy - removing policy denied subjects. Ploicy denied subjects size:" + delPRPAINSubjects.size());
            response.getControlActProcess().getSubject().removeAll(delPRPAINSubjects);
        }

        if (response != null &&
                response.getControlActProcess() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject())) {
            pRPAINSubjects = response.getControlActProcess().getSubject();
            log.debug("checkPolicy - after policy Check-Subjects size: " + pRPAINSubjects.size());
            if (pRPAINSubjects.size() > 0) {
                isPermit = true;
            }
        } else {
            log.debug("checkPolicy - after policy Check-response/subjects is null");
        }
        //************************************************************************************************

        //return policyChecker.check201305Policy(response, patIdOverride, assertion);
        return isPermit;
    }

    protected PRPAIN201306UV02 createEmpty201306(String senderOID, String receiverOID, PRPAIN201305UV02 request) {
        // Switch the sender and receiver OIDs before calling the transformation method because the response is going in the opposite direction.
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

        if (NullChecker.isNotNullish(hcid) &&
                NullChecker.isNotNullish(assigningAuthority)) {
            boolean result = mappingDao.storeMapping(hcid, assigningAuthority);

            if (result == false) {
                log.warn("Failed to store home community - assigning authority mapping");
            }

        }
    }

    /**
     * Method to store local AA and HCID mapping
     * @param request
     * @return
     */
    public void storeLocalMapping(RespondingGatewayPRPAIN201305UV02RequestType request) {
        log.debug("Begin storeLocalMapping");

        String hcid = HomeCommunityMap.getLocalHomeCommunityId();
        log.debug("Begin storeLocalMapping: hcid" + hcid);

        II patId = extractPatientIdFrom201305(request.getPRPAIN201305UV02());

        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();

        if (mappingDao == null || patId == null) {
            log.warn("AssigningAuthorityHomeCommunityMappingDAO or Local Patient Id was null. Mapping was not stored.");
        } else {
            if (!mappingDao.storeMapping(hcid, patId.getRoot())) {
                log.warn("Failed to store home community - assigning authority mapping" );
            }
        }

        log.debug("End storeLocalMapping");
    }

    protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
        return queryMpiForPatients(query, assertion);
    }

    public PRPAIN201306UV02 queryMpiForPatients(PRPAIN201305UV02 query, AssertionType assertion) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();

        if (query != null) {
            // Query the MPI to see if the patient is found
            AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
            AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
            log.info("Sending query to the Secured MPI");
            queryResults =
                    mpiProxy.findCandidates(query, assertion);

        } else {
            log.error("MPI Request is null");
            queryResults =
                    null;
        }

        return queryResults;
    }

    protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, II localPatId, AssertionType assertion, PRPAIN201305UV02 query) {
        PRPAIN201301UV02 request = new PRPAIN201301UV02();
        String localAA = null;

        if (queryResult != null &&
                localPatId != null &&
                NullChecker.isNotNullish(localPatId.getRoot()) &&
                NullChecker.isNotNullish(localPatId.getExtension()) &&
                assertion != null) {
            if (queryResult.getControlActProcess() != null &&
                    NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject()) &&
                    queryResult.getControlActProcess().getSubject().get(0) != null &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                localAA = queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot();
            }
            request = HL7PRPA201301Transforms.createPRPA201301(query, localAA);

            if (request != null &&
                    request.getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                    request.getControlActProcess().getSubject().get(0) != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
                // Need to Switch patient ids so the sender and reciever match.  This is to avoid an exception by the Patient Correlation Component
                II remotePatient = request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localPatId);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(remotePatient);

                PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();

                patCorrelationProxy.addPatientCorrelation(request, assertion);
            }
        } else {
            log.error("Null parameter passed to createPatientCorrelation method, no correlation created");
        }
    }

    protected void createPatientCorrelation(PRPAIN201306UV02 queryResult, AssertionType assertion, PRPAIN201305UV02 query) {
        PRPAIN201301UV02 request = new PRPAIN201301UV02();
        II localPatId = new II();
        if (queryResult != null &&
                assertion != null) {
            int subjectsSize = 0;
            if (queryResult.getControlActProcess() != null &&
                    NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject())) {
                subjectsSize = queryResult.getControlActProcess().getSubject().size();
            }

            for (int i = 0; i < subjectsSize; i++) {
                localPatId = new II();
                if (queryResult.getControlActProcess() != null &&
                        NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject()) &&
                        queryResult.getControlActProcess().getSubject().get(i) != null &&
                        queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent() != null &&
                        queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1() != null &&
                        queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient() != null &&
                        NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                        queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                        NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                        NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                    localPatId.setExtension(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
                    localPatId.setRoot(queryResult.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
                    log.debug("local AA " + i + ": " + localPatId.getRoot() + ", pId " + ": " + localPatId.getExtension());
                }

                if ((localPatId != null) &&
                        (localPatId.getRoot() != null) &&
                        (localPatId.getExtension() != null)) {

                    request = HL7PRPA201301Transforms.createPRPA201301(query, localPatId.getRoot());

                    if (request != null &&
                            request.getControlActProcess() != null &&
                            NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                            request.getControlActProcess().getSubject().get(0) != null &&
                            request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                            request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                            request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                            NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
                        // Need to Switch patient ids so the sender and reciever match.  This is to avoid an exception by the Patient Correlation Component
                        II remotePatient = request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);
                        request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();
                        request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localPatId);
                        request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(remotePatient);
                        log.debug("Local AA " + i + ": " + localPatId.getRoot() + ", pId: " + localPatId.getExtension());
                        log.debug("Remote AA: " + remotePatient.getRoot() + ", pId: " + remotePatient.getExtension());

                        if ((remotePatient != null) &&
                                (remotePatient.getRoot() != null) &&
                                (remotePatient.getExtension() != null)) {
                            PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                            PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();
                            patCorrelationProxy.addPatientCorrelation(request, assertion);
                        } else {
                            log.error("Remote patient identifiers are null. Could not correlate the patient identifiers.");
                        }
                    } else {
                        log.error("Request (PRPAIN201301UV02) or remote patient identifiers are null. Could not correlate the patient identifiers.");
                    }
                } else {
                    log.error("Local patient identifiers are null. Could not correlate the patient identifiers.");
                }
            }
        } else {
            log.error("Null parameter passed to createPatientCorrelation method, no correlation created");
        }
    }

    private II providedPatientId(PRPAIN201305UV02 request) {
        II patId = null;
        String aaId = null;

        try {
            if (request != null &&
                    request.getControlActProcess() != null) {
                aaId = extractAAOID(request);

                if (NullChecker.isNotNullish(aaId)) {
                    if (request.getControlActProcess().getQueryByParameter() != null &&
                            request.getControlActProcess().getQueryByParameter().getValue() != null &&
                            request.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null &&
                            NullChecker.isNotNullish(request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId())) {
                        for (PRPAMT201306UV02LivingSubjectId livingSubId : request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId()) {
                            for (II id : livingSubId.getValue()) {
                                if (id != null &&
                                        NullChecker.isNotNullish(id.getRoot()) &&
                                        NullChecker.isNotNullish(id.getExtension()) &&
                                        aaId.contentEquals(id.getRoot())) {
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
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            patId = null;
        }


        return patId;
    }

    public PRPAIN201305UV02 createNewRequest(PRPAIN201305UV02 request, String targetCommunityId) {
        PRPAIN201305UV02 newRequest = new PRPAIN201305UV02();
        newRequest = request;

        if (request != null &&
                NullChecker.isNotNullish(targetCommunityId)) {
            //the new request will have the target community as the only receiver
            newRequest.getReceiver().clear();
            MCCIMT000100UV01Receiver oNewReceiver = HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(targetCommunityId);
            newRequest.getReceiver().add(oNewReceiver);
            log.debug("Created a new request for target communityId: " + targetCommunityId);
        } else {
            log.error("A null input paramter was passed to the method: createNewRequest in class: PatientDiscovery201305Processor");
            return null;
        }

        return newRequest;
    }

    public II extractPatientIdFrom201305(PRPAIN201305UV02 request) {
        II patId = null;
        String aaId = null;

        if (request != null &&
                request.getControlActProcess() != null) {

            aaId = extractAAOID(request);

            if (NullChecker.isNotNullish(aaId)) {
                if (request.getControlActProcess().getQueryByParameter() != null &&
                        request.getControlActProcess().getQueryByParameter().getValue() != null &&
                        request.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null &&
                        NullChecker.isNotNullish(request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId())) {
                    for (PRPAMT201306UV02LivingSubjectId livingSubId : request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId()) {
                        for (II id : livingSubId.getValue()) {
                            if (id != null &&
                                    NullChecker.isNotNullish(id.getRoot()) &&
                                    NullChecker.isNotNullish(id.getExtension()) &&
                                    aaId.contentEquals(id.getRoot())) {
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
        }

        return patId;
    }

    private String extractSenderOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null &&
                request.getSender() != null &&
                request.getSender().getDevice() != null &&
                request.getSender().getDevice().getAsAgent() != null &&
                request.getSender().getDevice().getAsAgent().getValue() != null &&
                request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return oid;
    }

    private String extractReceiverOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null &&
                NullChecker.isNotNullish(request.getReceiver()) &&
                request.getReceiver().get(0) != null &&
                request.getReceiver().get(0).getDevice() != null &&
                request.getReceiver().get(0).getDevice().getAsAgent() != null &&
                request.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        return oid;
    }

    private String extractAAOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null &&
                request.getControlActProcess() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer()) &&
                request.getControlActProcess().getAuthorOrPerformer().get(0) != null &&
                request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
            oid = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
        }

        return oid;
    }

    private PRPAIN201306UV02 addAuthorOrPerformer(PRPAIN201306UV02 response) {
        MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
        authorOrPerformer.setTypeCode(XParticipationAuthorPerformer.AUT);

        COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
        II id = new II();
        if (response != null &&
                response.getControlActProcess() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject()) &&
                response.getControlActProcess().getSubject().get(0) != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
            id.setRoot(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        }
        assignedDevice.setClassCode(HL7Constants.ASSIGNED_DEVICE_CLASS_CODE);
        assignedDevice.getId().add(id);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedDevice");
        JAXBElement<COCTMT090300UV01AssignedDevice> assignedDeviceJAXBElement = new JAXBElement<COCTMT090300UV01AssignedDevice>(xmlqname, COCTMT090300UV01AssignedDevice.class, assignedDevice);

        authorOrPerformer.setAssignedDevice(assignedDeviceJAXBElement);

        response.getControlActProcess().getAuthorOrPerformer().add(authorOrPerformer);
        return response;
    }

    private PRPAIN201306UV02 addPolicyDenyReasonOf(PRPAIN201306UV02 response) {

        MFMIMT700711UV01Reason reasonOf = new MFMIMT700711UV01Reason();
        MCAIMT900001UV01DetectedIssueEvent detectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
        detectedIssueEvent.setCode(HL7DataTransformHelper.CDFactory(HL7Constants.DETECTED_ISSUE_EVENT_CODE_AUTHORIZATION, HL7Constants.DETECTED_ISSUE_EVENT_OID, HL7Constants.DETECTED_ISSUE_EVENT_CODE_AUTHORIZATION_DESC));
        reasonOf.setDetectedIssueEvent(detectedIssueEvent);

        response.getControlActProcess().getReasonOf().add(reasonOf);

        return response;
    }

    private PRPAIN201306UV02 addValidationReasonOf(PRPAIN201306UV02 response) {

        MFMIMT700711UV01Reason reasonOf = new MFMIMT700711UV01Reason();
        MCAIMT900001UV01DetectedIssueEvent detectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
        detectedIssueEvent.setCode(HL7DataTransformHelper.CDFactory(HL7Constants.DETECTED_ISSUE_EVENT_CODE_VALIDATION, HL7Constants.DETECTED_ISSUE_EVENT_OID, HL7Constants.DETECTED_ISSUE_EVENT_CODE_VALIDATION_DESC));
        reasonOf.setDetectedIssueEvent(detectedIssueEvent);

        response.getControlActProcess().getReasonOf().add(reasonOf);

        return response;
    }
}
