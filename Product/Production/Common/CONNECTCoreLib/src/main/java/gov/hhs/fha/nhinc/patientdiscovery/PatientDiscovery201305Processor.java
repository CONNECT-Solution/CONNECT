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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.mpi.adapter.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.adapter.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7ReceiverTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscovery201305Processor {

    private static Log log = LogFactory.getLog(PatientDiscovery201305Processor.class);

    public PRPAIN201306UV02 process201305(PRPAIN201305UV02 request, AssertionType assertion) {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        // Set the sender and receiver OID for the response
        String senderOID = extractSenderOID(request);
        String receiverOID = extractReceiverOID(request);

        // Query the MPI to see if we have a match
        response = queryMpi(request, assertion);

        // Check to see if the Patient was found
        if (response != null &&
                response.getControlActProcess() != null) {

            II patIdOverride = NhinPatientDiscoveryUtils.extractPatientIdFrom201306(response);

            if (NullChecker.isNotNullish(response.getControlActProcess().getSubject()) &&
                    response.getControlActProcess().getSubject().get(0) != null &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                    NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                patIdOverride.setExtension(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
                patIdOverride.setRoot(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
            } else {
                patIdOverride = null;
            }

            // Check to make sure the policy is valid

            if (checkPolicy(response, patIdOverride, assertion)) {
                // Store Assigning Authority to Home Community Id Mapping
                storeMapping(request);

                II requestPatId = providedPatientId(request);
                if (requestPatId != null) {
                    // Create a patient correlation
                    createPatientCorrelation(response, patIdOverride, assertion, request);
                }
            } else {
                log.error("Policy Check Failed");
                response = createEmpty201306(senderOID, receiverOID, request);
            }
        } else {
            log.warn("Patient Not Found");
            response = createEmpty201306(senderOID, receiverOID, request);
        }
        return response;
    }

    protected boolean checkPolicy(PRPAIN201306UV02 response, II patIdOverride, AssertionType assertion) {
        PatientDiscoveryPolicyChecker policyChecker = new PatientDiscoveryPolicyChecker();

        return policyChecker.check201305Policy(response, patIdOverride, assertion);
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
}
