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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
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
     * @param slotList
     * @param urlInfoList
     * @param assertion
     * @param isTargeted
     * @param localHomeCommunity
     * @return subIdList
     */
    public List<QualifiedSubjectIdentifierType> retreiveCorrelations(List<SlotType1> slotList, CMUrlInfos urlInfoList, AssertionType assertion, boolean isTargeted, String localHomeCommunity) {
        log.debug("Begin EntityDocQueryHelper.retreiveCorrelations().....");
        RetrievePatientCorrelationsResponseType results = null;
        RetrievePatientCorrelationsRequestType patientCorrelationReq = new RetrievePatientCorrelationsRequestType();
        QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
        List<QualifiedSubjectIdentifierType> subIdList = new ArrayList<QualifiedSubjectIdentifierType>();
        boolean querySelf = false;

        if (slotList != null) {
            log.debug("retreiveCorrelations slotList.size: " + slotList.size());
            for (SlotType1 slot1 : slotList) {
                if (slot1.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                    if (slot1.getValueList() != null) {
                        log.debug("retreiveCorrelations slot value: " + slot1.getValueList().getValue());
                    } else {
                        log.debug("retreiveCorrelations slot1.getValueList(): null");
                    }
                } else {
                    log.debug("retreiveCorrelations " + NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME + " not found");
                }
            }
        }

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {
            // Find the Patient Id slot
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null &&
                        NullChecker.isNotNullish(slot.getValueList().getValue()) &&
                        NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    qualSubId.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(slot.getValueList().getValue().get(0)));
                    qualSubId.setAssigningAuthorityIdentifier(PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue().get(0)));

                    log.debug("Extracting subject id: " + qualSubId.getSubjectIdentifier());
                    log.debug("Extracting assigning authority id: " + qualSubId.getAssigningAuthorityIdentifier());
                    patientCorrelationReq.setQualifiedPatientIdentifier(qualSubId);
                }

                // Save off the target home community ids to use in the patient correlation query
                if (urlInfoList != null &&
                        NullChecker.isNotNullish(urlInfoList.getUrlInfo())) {
                    for (CMUrlInfo target : urlInfoList.getUrlInfo()) {
                        if (NullChecker.isNotNullish(target.getHcid())) {
                            patientCorrelationReq.getTargetHomeCommunity().add(target.getHcid());

                            if (target.getHcid().equals(localHomeCommunity) &&
                                    isTargeted == true) {
                                querySelf = true;
                            }
                        }
                    }
                }

                break;
            }
        }

        if (!querySelf) {
            querySelf = getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DOC_QUERY_SELF_PROPERTY_NAME);
        }

        // Retreive Patient Correlations this patient
        PatientCorrelationProxyObjectFactory factory = new PatientCorrelationProxyObjectFactory();
        PatientCorrelationProxy proxy = factory.getPatientCorrelationProxy();

        patientCorrelationReq.setAssertion(assertion);
        PRPAIN201309UV02 patCorrelationRequest = PixRetrieveBuilder.createPixRetrieve(patientCorrelationReq);



        results = proxy.retrievePatientCorrelations(patCorrelationRequest, assertion);

        // Make sure the response is valid
        if (results != null &&
                results.getPRPAIN201310UV02() != null &&
                results.getPRPAIN201310UV02().getControlActProcess() != null &&
                NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject()) &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0) != null &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
            for (II id : results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) {
                QualifiedSubjectIdentifierType subId = new QualifiedSubjectIdentifierType();
                subId.setAssigningAuthorityIdentifier(id.getRoot());
                subId.setSubjectIdentifier(id.getExtension());
                subIdList.add(subId);
            }
        }

        // If we are querying ourselves as well then add this community to the list of correlations
        if (querySelf == true) {
            // Examine our patient
            if (patientCorrelationReq.getQualifiedPatientIdentifier() != null) {
                log.debug("***** documentQueryQuerySelf=true - Our Patient Id: " + patientCorrelationReq.getQualifiedPatientIdentifier().getSubjectIdentifier() + " *****");
                log.debug("***** documentQueryQuerySelf=true - Our AA Id: " + patientCorrelationReq.getQualifiedPatientIdentifier().getAssigningAuthorityIdentifier() + " *****");
            }
            subIdList.add(patientCorrelationReq.getQualifiedPatientIdentifier());
        }
        if (subIdList != null) {
            log.debug("retreiveCorrelations subIdList.size(): " + subIdList.size());
            for (QualifiedSubjectIdentifierType qual : subIdList) {
                log.debug("retreiveCorrelations qual.getSubjectIdentifier: " + qual.getSubjectIdentifier());
                log.debug("retreiveCorrelations qual.getAssigningAuthorityIdentifier: " + qual.getAssigningAuthorityIdentifier());
            }
        } else {
            log.debug("retreiveCorrelations subIdList.size(): null");
        }
        log.debug("End EntityDocQueryHelper.retreiveCorrelations().....");
        return subIdList;
    }

    public HomeCommunityType lookupHomeCommunityId(String sAssigningAuthorityId, String sLocalAssigningAuthorityId, String sLocalHomeCommunity) {
        HomeCommunityType targetCommunity = null;
        if ((sAssigningAuthorityId != null) && (sAssigningAuthorityId.equals(sLocalAssigningAuthorityId))) {
            /*
             * If the target is the local home community, the local
             * assigning authority may not be mapped to the local
             * home community in the community mapping. Set manually.
             */
            targetCommunity = new HomeCommunityType();
            targetCommunity.setHomeCommunityId(sLocalHomeCommunity);
            log.debug("Assigning authority was for the local home community. Set target to manual local home community id");
        } else {
            targetCommunity = ConnectionManagerCommunityMapping.getHomeCommunityByAssigningAuthority(sAssigningAuthorityId);
        }
        return targetCommunity;
    }

    private boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
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
                if (slot.getValueList() != null &&
                        NullChecker.isNotNullish(slot.getValueList().getValue()) &&
                        NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    localAssigningAuthorityId = PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue().get(0));
                }
                break;
            }
        }

        return localAssigningAuthorityId;
    }

    /**
     * This method returns uniquePatientId from slot list
     * @param slotList
     * @return uniquePatientId
     */
    public String getUniquePatientId(List<SlotType1> slotList) {
        String uniquePatientId = null;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null &&
                        NullChecker.isNotNullish(slot.getValueList().getValue()) &&
                        NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    uniquePatientId = PatientIdFormatUtil.stripQuotesFromPatientId(slot.getValueList().getValue().get(0));
                }
                break;
            }
        }

        return uniquePatientId;
    }
}
