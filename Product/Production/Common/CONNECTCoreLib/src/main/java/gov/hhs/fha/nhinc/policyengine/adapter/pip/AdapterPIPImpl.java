/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;

import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumer;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This contains the implementation of the Adapter PIP (Policy Information Point).
 *
 * @author Les Westberg
 */
public class AdapterPIPImpl {

    private Log log = null;
    private static final String ASSERTIONINFO_PROPFILE_NAME = "assertioninfo";
    private static final String GATEWAY_PROPERTIES_FILE = "gateway";
    private static final String ADAPTER_PROPFILE_NAME = "adapter";
    private static final String HOME_COMMUNITY_PROPERTY = "localHomeCommunityId";

    public AdapterPIPImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PatientConsentManager getPatientConsentManager()
    {
        return new PatientConsentManager();
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     * @throws AdapterPIPException This exception is thrown if the data cannot be retrieved.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request)
            throws AdapterPIPException {
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();

        String sPatientId = "";
        String sAssigningAuthority = "";

        if ((request != null) &&
                (request.getAssigningAuthority() != null)) {
            sAssigningAuthority = request.getAssigningAuthority();
        }

        if ((request != null) &&
                (request.getPatientId() != null)) {
            sPatientId = request.getPatientId();
        }

        PatientConsentManager oManager = new PatientConsentManager();
        PatientPreferencesType oPtPref = oManager.retrievePatientConsentByPatientId(sPatientId, sAssigningAuthority);

        if (oPtPref != null) {
            oResponse.setPatientPreferences(oPtPref);
        }

        return oResponse;
    }

    /**
     * Retrieve the patient consent settings for the patient associated with
     * the given document identifiers.
     *
     * @param request The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with
     *         the given document identifiers.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request)
            throws AdapterPIPException {
        log.debug("Begin AdapterPIPImpl.retrievePtIdFromDocumentId()..");
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();

        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentUniqueId = "";

        if ((request != null) &&
                (request.getHomeCommunityId() != null)) {
            sHomeCommunityId = request.getHomeCommunityId();
        }

        if ((request != null) &&
                (request.getRepositoryId() != null)) {
            sRepositoryId = request.getRepositoryId();
        }

        if ((request != null) &&
                (request.getDocumentId() != null)) {
            sDocumentUniqueId = request.getDocumentId();
        }


        PatientConsentManager oManager = new PatientConsentManager();
        PatientPreferencesType oPtPref = oManager.retrievePatientConsentByDocId(sHomeCommunityId, sRepositoryId, sDocumentUniqueId);

        if (oPtPref != null) {
            oResponse.setPatientPreferences(oPtPref);
        }

        log.debug("End AdapterPIPImpl.retrievePtIdFromDocumentId()..");
        return oResponse;
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param request The patient consent settings to be stored.
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request)
            throws AdapterPIPException {
        log.debug("Begin AdapterPIPImpl.storePtConsent()..");
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();
        try {
            if ((request != null) &&
                    (request.getPatientPreferences() != null)) {
                PatientConsentManager oManager = getPatientConsentManager();                
                oManager.storePatientConsent(request.getPatientPreferences());
                oResponse.setStatus("SUCCESS");
            } else {
                throw new AdapterPIPException("StorePtConsentRequest requires patient preferences");
            }
        } catch (Exception e) {
            oResponse.setStatus("FAILED: " + e.getMessage());
            String sErrorMessage = "Failed to store the patient consent.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }
        return oResponse;
    }

    /**
     * This method is used to build Asserton information to send Notification to Entity Notification Consumer
     * @param sHid
     * @return AssertionType
     */
    private AssertionType buildAssertionInfo(String sHid)
    {
        log.debug("Begin - CPPOperations.buildAssertion() - ");
        AssertionType assertion = new AssertionType();
        String svalue = "";
        try
        {
            assertion.setHaveSignature(true);
            assertion.setHaveWitnessSignature(true);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PERMISSION_DATE);
            if (svalue != null && svalue.length() > 0)
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(svalue.trim());
            }
            else
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.EXPIRATION_DATE);
            if (null != svalue && svalue.length() > 0)
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(svalue.trim());
            } else
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter("");
            }
            PersonNameType aPersonName = new PersonNameType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.FIRST_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                aPersonName.setGivenName(svalue.trim());
            }
            else
            {
                aPersonName.setGivenName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.LAST_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                aPersonName.setFamilyName(svalue.trim());
            }
            else
            {
                aPersonName.setFamilyName("");
            }
            UserType aUser = new UserType();
            aUser.setPersonName(aPersonName);
            HomeCommunityType userHm = new HomeCommunityType();
            svalue = PropertyAccessor.getProperty(CDAConstants.SubscribeeCommunityList_PROPFILE_NAME, sHid);
            if (null != svalue && svalue.length() > 0)
            {
                userHm.setName(svalue.trim());
            }
            else
            {
                userHm.setName("");
            }
            userHm.setHomeCommunityId(sHid);
            aUser.setOrg(userHm);
            CeType userCe = new CeType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setCode(svalue.trim());
            }
            else
            {
                userCe.setCode("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setCodeSystem(svalue.trim());
            }
            else
            {
                userCe.setCodeSystem("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_CD_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setCodeSystemName(svalue.trim());
            }
            else
            {
                userCe.setCodeSystemName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_ROLE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                userCe.setDisplayName(svalue.trim());
            }
            else
            {
                userCe.setDisplayName("");
            }
            aUser.setRoleCoded(userCe);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.USER_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                aUser.setUserName(svalue.trim());
            } else
            {
                aUser.setUserName("");
            }
            assertion.setUserInfo(aUser);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.ORG_NAME);
            HomeCommunityType hm = new HomeCommunityType();
            if (null != svalue && svalue.length() > 0)
            {
                hm.setName(svalue.trim());
            }
            else
            {
                hm.setName("");
            }
            assertion.setHomeCommunity(hm);
            CeType ce = new CeType();
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_ROLE_CD);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setCode(svalue.trim());
            }
            else
            {
                ce.setCode("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setCodeSystem(svalue.trim());
            }
            else
            {
                ce.setCodeSystem("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_CODE_SYSTEM_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setCodeSystemName(svalue.trim());
            }
            else
            {
                ce.setCodeSystemName("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.PURPOSE_FOR_USE_DISPLAY_NAME);
            if (null != svalue && svalue.length() > 0)
            {
                ce.setDisplayName(svalue.trim());
            }
            else
            {
                ce.setDisplayName("");
            }
            assertion.setPurposeOfDisclosureCoded(ce);
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.ACCESS_POLICY_CONSENT);
            if (null != svalue && svalue.length() > 0)
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy(svalue.trim());
            }
            else
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.INSTANCE_ACCESS_POLICY_CONSENT);
            if (null != svalue && svalue.length() > 0)
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setInstanceAccessConsentPolicy(svalue.trim());
            }
            else
            {
                assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setInstanceAccessConsentPolicy("");
            }
        }
        catch (PropertyAccessException propExp)
        {
            propExp.printStackTrace();
        }
        log.debug("End - CPPOperations.buildAssertion() - ");
        return assertion;
    }

    /**
     * This method is used to create dynamic end point for Entity Notification Consumer
     * @return EntityNotificationConsumerPortType
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    private EntityNotificationConsumerPortType buildEndpointURL()
            throws PropertyAccessException
    {
        String endpointURL = PropertyAccessor.getProperty(ADAPTER_PROPFILE_NAME, CDAConstants.ENTITY_NOTIFICATION_CONSUMER_ENDPOINT_URL);
        log.info("EntityNotificationConsumerURL :" + endpointURL);
        EntityNotificationConsumerPortType entitynotificationconsumerPort = null;
        EntityNotificationConsumer service = new EntityNotificationConsumer();
        entitynotificationconsumerPort = service.getEntityNotificationConsumerPortSoap();
        // Need to load in the correct UDDI endpoint URL address.
        //--------------------------------------------------------
        ((BindingProvider) entitynotificationconsumerPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        return entitynotificationconsumerPort;
    }
}
