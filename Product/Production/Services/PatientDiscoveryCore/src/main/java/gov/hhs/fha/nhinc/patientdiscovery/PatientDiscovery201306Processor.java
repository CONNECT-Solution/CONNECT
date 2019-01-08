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
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7ReceiverTransforms;
import java.util.ArrayList;
import java.util.List;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.PRPAIN201306UV02;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JHOPPESC
 */
public class PatientDiscovery201306Processor {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDiscovery201306Processor.class);

    /**
     * createNewRequest
     *
     * @param request
     * @param targetCommunityId
     * @return
     */
    public PRPAIN201306UV02 createNewRequest(PRPAIN201306UV02 request, String targetCommunityId) {
        PRPAIN201306UV02 newRequest = request;

        if (request != null && NullChecker.isNotNullish(targetCommunityId)) {
            newRequest.getReceiver().clear();
            MCCIMT000300UV01Receiver oNewReceiver = HL7ReceiverTransforms
                .createMCCIMT000300UV01Receiver(targetCommunityId);
            newRequest.getReceiver().add(oNewReceiver);
            LOG.debug("Created a new request for target communityId: " + targetCommunityId);
        } else {
            LOG.error("A null input paramter was passed to the method: createNewRequest in class: PatientDiscovery201305Processor");
            return null;
        }

        return newRequest;
    }

    /**
     * storeMapping Method to store AA and HCID mappings
     *
     * @param request
     * @return
     */
    public void storeMapping(PRPAIN201306UV02 request) {
        LOG.debug("Begin storeMapping");
        String hcid = getHcid(request);
        LOG.debug("Begin storeMapping: hcid" + hcid);
        List<String> assigningAuthorityIds;
        assigningAuthorityIds = extractAAListFrom201306(request);
        for (String assigningAuthority : assigningAuthorityIds) {
            mapAssigningAuthorityHomeCommunity(hcid, assigningAuthority);
        }

        LOG.debug("End storeMapping");
    }

    /**
     * @param hcid
     * @param assigningAuthority
     */
    private void mapAssigningAuthorityHomeCommunity(String hcid, String assigningAuthority) {
        LOG.debug("storeMapping: assigningAuthority" + assigningAuthority);
        if (NullChecker.isNullish(hcid)) {
            LOG.warn("HCID null or empty. Mapping was not stored.");
        } else if (NullChecker.isNullish(assigningAuthority)) {
            LOG.warn("Assigning authority null or empty. Mapping was not stored.");
        } else {
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = getAssigningAuthorityHomeCommunityMappingDAO();

            if (mappingDao == null) {
                LOG.warn("AssigningAuthorityHomeCommunityMappingDAO was null. Mapping was not stored.");
            } else {
                if (!mappingDao.storeMapping(hcid, assigningAuthority)) {
                    LOG.warn("Failed to store home community - assigning authority mapping");
                }
            }
        }
    }

    protected String getHcid(PRPAIN201306UV02 request) {
        String hcid = null;

        if (request != null
            && request.getSender() != null
            && request.getSender().getDevice() != null
            && request.getSender().getDevice().getAsAgent() != null
            && request.getSender().getDevice().getAsAgent().getValue() != null
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId())
            && request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0) != null
            && NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            hcid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot();
        }
        return hcid;
    }

    protected String getAssigningAuthority(PRPAIN201306UV02 request) {
        String assigningAuthority = null;

        if (request != null
            && request.getControlActProcess() != null
            && NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer())
            && request.getControlActProcess().getAuthorOrPerformer().get(0) != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null
            && NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0)
                .getAssignedDevice().getValue().getId())
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()
            .get(0) != null
            && NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0)
                .getAssignedDevice().getValue().getId().get(0).getRoot())) {
            assigningAuthority = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice()
                .getValue().getId().get(0).getRoot();
        }
        return assigningAuthority;
    }

    protected String extractAAFrom201306(PRPAIN201306UV02 msg) {
        LOG.debug("Begin extractAAFrom201306");
        String assigningAuthority = null;

        if (msg != null
            && msg.getControlActProcess() != null
            && NullChecker.isNotNullish(msg.getControlActProcess().getSubject())
            && msg.getControlActProcess().getSubject().get(0) != null
            && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
            && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
            && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null
            && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId())
            && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
            .getId().get(0) != null
            && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getExtension())
            && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getRoot())) {
            assigningAuthority = msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getRoot();
            LOG.debug("extractAAFrom201306 - assigningAuthority: " + assigningAuthority);
        }
        return assigningAuthority;
    }

    protected List<String> extractAAListFrom201306(PRPAIN201306UV02 msg) {
        LOG.debug("Begin extractAAFrom201306");
        List<String> assigningAuthorityIds = new ArrayList<>();
        String assigningAuthority;
        int subjCount = 0;

        if (msg != null && msg.getControlActProcess() != null
            && NullChecker.isNotNullish(msg.getControlActProcess().getSubject())) {
            subjCount = msg.getControlActProcess().getSubject().size();
        }
        LOG.debug("storeMapping - Subject Count: " + subjCount);

        for (int i = 0; i < subjCount; i++) {
            if (msg != null
                && msg.getControlActProcess() != null
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject())
                && msg.getControlActProcess().getSubject().get(i) != null
                && msg.getControlActProcess().getSubject().get(i).getRegistrationEvent() != null
                && msg.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1() != null
                && msg.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient() != null
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                    .getSubject1().getPatient().getId())
                && msg.getControlActProcess().getSubject().get(i).getRegistrationEvent().getSubject1().getPatient()
                .getId().get(0) != null
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                    .getSubject1().getPatient().getId().get(0).getExtension())
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                    .getSubject1().getPatient().getId().get(0).getRoot())) {
                assigningAuthority = msg.getControlActProcess().getSubject().get(i).getRegistrationEvent()
                    .getSubject1().getPatient().getId().get(0).getRoot();
                LOG.debug("extractAAFrom201306 - assigningAuthority" + i + " :" + assigningAuthority);
                assigningAuthorityIds.add(assigningAuthority);
            }
        }
        return assigningAuthorityIds;
    }

    protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO() {
        return new AssigningAuthorityHomeCommunityMappingDAO();
    }
}
