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
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7ReceiverTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public class PatientDiscovery201306Processor {

    private Log log = null;
    
    public PatientDiscovery201306Processor () {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public PRPAIN201306UV02 createNewRequest(PRPAIN201306UV02 request, String targetCommunityId) {
        PRPAIN201306UV02 newRequest = new PRPAIN201306UV02();
        newRequest = request;

        if (request != null &&
                NullChecker.isNotNullish(targetCommunityId)) {
            //the new request will have the target community as the only receiver
            newRequest.getReceiver().clear();
            MCCIMT000300UV01Receiver oNewReceiver = HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(targetCommunityId);
            newRequest.getReceiver().add(oNewReceiver);
            log.debug("Created a new request for target communityId: " + targetCommunityId);
        } else {
            log.error("A null input paramter was passed to the method: createNewRequest in class: PatientDiscovery201305Processor");
            return null;
        }

        return newRequest;
    }

    public void storeMapping(PRPAIN201306UV02 request) {
        log.debug("Begin storeMapping");
        String hcid = getHcid(request);
        String assigningAuthority = getAssigningAuthority(request);

        if (NullChecker.isNullish(hcid)) {
            log.warn("HCID null or empty. Mapping was not stored.");
        } else if (NullChecker.isNullish(assigningAuthority)) {
            log.warn("Assigning authority null or empty. Mapping was not stored.");
        } else {
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = getAssigningAuthorityHomeCommunityMappingDAO();

            if (mappingDao == null) {
                log.warn("AssigningAuthorityHomeCommunityMappingDAO was null. Mapping was not stored.");
            } else {
                if (!mappingDao.storeMapping(hcid, assigningAuthority)) {
                    log.warn("Failed to store home community - assigning authority mapping");
                }
            }
        }
        log.debug("End storeMapping");
    }

    protected String getHcid(PRPAIN201306UV02 request) {
        String hcid = null;
        
        if ((request != null) &&
                (request.getSender() != null) &&
                (request.getSender().getDevice() != null) &&
                (request.getSender().getDevice().getAsAgent() != null) &&
                (request.getSender().getDevice().getAsAgent().getValue() != null) &&
                (request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null) &&
                (request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null) &&
                (NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId())) &&
                (request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null) &&
                (NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot()))) {
            hcid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }
        return hcid;
    }

    protected String getAssigningAuthority(PRPAIN201306UV02 request) {
        String assigningAuthority = null;

        if ((request != null) &&
                (request.getControlActProcess() != null) &&
                (NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer())) &&
                (request.getControlActProcess().getAuthorOrPerformer().get(0) != null) &&
                (request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null) &&
                (request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null) &&
                (NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId())) &&
                (request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null) &&
                (NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot()))) {
            assigningAuthority = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
        }
        return assigningAuthority;
    }

    protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO()
    {
        return new AssigningAuthorityHomeCommunityMappingDAO();
    }
}
