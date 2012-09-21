/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 * 
 * @author jhoppesc
 */
public class EntityDocQueryHelper {

    private Log log = null;

    public EntityDocQueryHelper() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * This method retrieves the correlations
     * 
     * @param slotList
     * @param urlInfoList
     * @param assertion
     * @param isTargeted
     * @param localHomeCommunity
     * @return subIdList
     */
    public List<QualifiedSubjectIdentifierType> retreiveCorrelations(List<SlotType1> slotList,
            List<UrlInfo> urlInfoList, AssertionType assertion, boolean isTargeted, String localHomeCommunity) {
        log.debug("Begin EntityDocQueryHelper.retreiveCorrelations().....");
        RetrievePatientCorrelationsResponseType results = null;
        RetrievePatientCorrelationsRequestType patientCorrelationReq = new RetrievePatientCorrelationsRequestType();
        QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
        boolean querySelf = false;
        List<QualifiedSubjectIdentifierType> subIdList = new ArrayList<QualifiedSubjectIdentifierType>();
        if (slotList != null) {
            if (log.isDebugEnabled()) {
                log.debug("retreiveCorrelations slotList.size: " + slotList.size());
            }
            boolean slotPresent = patientIdSlot(slotList);
            // For each slot process each of the Patient Id slots
            if (slotPresent) {
                for (SlotType1 slot : slotList) {
                    // Find the Patient Id slot
                    if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                        if (slot.getValueList() != null && NullChecker.isNotNullish(slot.getValueList().getValue())
                                && NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {

                            qualSubId.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(slot.getValueList()
                                    .getValue().get(0)));
                            qualSubId.setAssigningAuthorityIdentifier(PatientIdFormatUtil.parseCommunityId(slot
                                    .getValueList().getValue().get(0)));

                            if (log.isDebugEnabled()) {
                                log.debug("Extracting subject id: " + qualSubId.getSubjectIdentifier());
                                log.debug("Extracting assigning authority id: "
                                        + qualSubId.getAssigningAuthorityIdentifier());
                            }
                            patientCorrelationReq.setQualifiedPatientIdentifier(qualSubId);
                        }

                        // Save off the target home community ids to use in the patient correlation query
                        if (urlInfoList != null && NullChecker.isNotNullish(urlInfoList)) {
                            for (UrlInfo target : urlInfoList) {
                                if (NullChecker.isNotNullish(target.getHcid())) {
                                    patientCorrelationReq.getTargetHomeCommunity().add(target.getHcid());

                                    if ((target.getHcid().equals(localHomeCommunity)) && (isTargeted)) {
                                        querySelf = true;
                                    }
                                }
                            }
                        }

                        break;
                    }
                }

                if (!querySelf) {
                    querySelf = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                            NhincConstants.DOC_QUERY_SELF_PROPERTY_NAME);
                }

                // Retreive Patient Correlations this patient

                PatientCorrelationProxyObjectFactory factory = new PatientCorrelationProxyObjectFactory();
                PatientCorrelationProxy proxy = factory.getPatientCorrelationProxy();

                patientCorrelationReq.setAssertion(assertion);
                PRPAIN201309UV02 patCorrelationRequest = PixRetrieveBuilder.createPixRetrieve(patientCorrelationReq);

                results = proxy.retrievePatientCorrelations(patCorrelationRequest, assertion);

                // Make sure the response is valid
                if (results != null
                        && results.getPRPAIN201310UV02() != null
                        && results.getPRPAIN201310UV02().getControlActProcess() != null
                        && NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject())
                        && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0) != null
                        && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                                .getRegistrationEvent() != null
                        && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                                .getRegistrationEvent().getSubject1() != null
                        && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                                .getRegistrationEvent().getSubject1().getPatient() != null
                        && NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject()
                                .get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
                    for (II id : results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                            .getRegistrationEvent().getSubject1().getPatient().getId()) {
                        QualifiedSubjectIdentifierType subId = new QualifiedSubjectIdentifierType();
                        subId.setAssigningAuthorityIdentifier(id.getRoot());
                        subId.setSubjectIdentifier(id.getExtension());
                        subIdList.add(subId);
                    }
                }

                // If we are querying ourselves as well then add this community to the list of correlations
                if (querySelf) {
                    // Examine our patient
                    if (patientCorrelationReq.getQualifiedPatientIdentifier() != null) {
                        log.debug("***** documentQueryQuerySelf=true - Our Patient Id: "
                                + patientCorrelationReq.getQualifiedPatientIdentifier().getSubjectIdentifier()
                                + " *****");
                        log.debug("***** documentQueryQuerySelf=true - Our AA Id: "
                                + patientCorrelationReq.getQualifiedPatientIdentifier()
                                        .getAssigningAuthorityIdentifier() + " *****");
                    }
                    subIdList.add(patientCorrelationReq.getQualifiedPatientIdentifier());
                }
                if (log.isDebugEnabled()) {
                    log.debug("retreiveCorrelations subIdList.size(): " + subIdList.size());
                }
                for (QualifiedSubjectIdentifierType qual : subIdList) {
                    log.info("retreiveCorrelations qual.getSubjectIdentifier: " + qual.getSubjectIdentifier());
                    log.info("retreiveCorrelations qual.getAssigningAuthorityIdentifier: "
                            + qual.getAssigningAuthorityIdentifier());
                }
                log.debug("End EntityDocQueryHelper.retreiveCorrelations().....");
            }
        }
        return subIdList;
    }

    public HomeCommunityType lookupHomeCommunityId(String sAssigningAuthorityId, String sLocalAssigningAuthorityId,
            String sLocalHomeCommunity) {
        HomeCommunityType targetCommunity = null;
        if ((sAssigningAuthorityId != null) && (sAssigningAuthorityId.equals(sLocalAssigningAuthorityId))) {
            /*
             * If the target is the local home community, the local assigning authority may not be mapped to the local
             * home community in the community mapping. Set manually.
             */
            targetCommunity = new HomeCommunityType();
            targetCommunity.setHomeCommunityId(sLocalHomeCommunity);
            log.debug("Assigning authority was for the local home community. Set target to manual local home community id");
        } else {
            targetCommunity = ConnectionManagerCommunityMapping
                    .getHomeCommunityByAssigningAuthority(sAssigningAuthorityId);
        }
        return targetCommunity;
    }

    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getInstance().getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }

    public String getLocalAssigningAuthority(List<SlotType1> slotList) {
        String localAssigningAuthorityId = null;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null && NullChecker.isNotNullish(slot.getValueList().getValue())
                        && NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    localAssigningAuthorityId = PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue()
                            .get(0));
                }
                break;
            }
        }

        return localAssigningAuthorityId;
    }

    /**
     * This method returns uniquePatientId from slot list
     * 
     * @param slotList
     * @return uniquePatientId
     */
    public String getUniquePatientId(List<SlotType1> slotList) {
        String uniquePatientId = null;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null && NullChecker.isNotNullish(slot.getValueList().getValue())
                        && NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    uniquePatientId = PatientIdFormatUtil.stripQuotesFromPatientId(slot.getValueList().getValue()
                            .get(0));
                }
                break;
            }
        }

        return uniquePatientId;
    }

    protected boolean patientIdSlot(List<SlotType1> slotList) {
        boolean slotPresent = false;
        for (SlotType1 slot1 : slotList) {
            if (slot1.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot1.getValueList() != null) {
                    if (NullChecker.isNotNullish(slot1.getValueList().getValue())) {
                        slotPresent = true;
                        log.debug("retreiveCorrelations slot value: " + slot1.getValueList().getValue());
                    }
                } else {
                    log.debug("retreiveCorrelations slot1.getValueList(): null");
                }
            } else {
                log.debug("retreiveCorrelations " + NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME + " not found");
            }
        }
        return slotPresent;
    }
}
