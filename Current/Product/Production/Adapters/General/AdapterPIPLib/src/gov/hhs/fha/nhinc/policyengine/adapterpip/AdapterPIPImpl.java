package gov.hhs.fha.nhinc.policyengine.adapterpip;

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

import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
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
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType.Message;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 * This contains the implementation of the Adapter PIP (Policy Information Point).
 *
 * @author Les Westberg
 */
public class AdapterPIPImpl {

    private static Log log = LogFactory.getLog(AdapterPIPImpl.class);
    private static final String ASSERTIONINFO_PROPFILE_NAME = "assertioninfo";
    private static final String GATEWAY_PROPERTIES_FILE = "gateway";
    private static final String ADAPTER_PROPFILE_NAME = "adapter";
    private static final String HOME_COMMUNITY_PROPERTY = "localHomeCommunityId";

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
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();
        try {
            if ((request != null) &&
                    (request.getPatientPreferences() != null)) {
                PatientConsentManager oManager = new PatientConsentManager();
                oManager.storePatientConsent(request.getPatientPreferences());
                oResponse.setStatus("SUCCESS");
                sendHIEMEntityNotification(request);
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
     * This method sends notification to Entity Notification Consumer gateway
     * @param request
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    private void sendHIEMEntityNotification(StorePtConsentRequestType request) throws AdapterPIPException, PropertyAccessException {
        String sPatientId = "";
        String sHomeCommunityId = "";
        String sAssignAuthority ="";
        String endpointURL = PropertyAccessor.getProperty(ADAPTER_PROPFILE_NAME, CDAConstants.ENTITY_NOTIFICATION_CONSUMER_ENDPOINT_URL);
        if (request != null) {
            PatientPreferencesType patientPreferences = new PatientPreferencesType();
            patientPreferences = request.getPatientPreferences();
            if (patientPreferences != null) {
                sPatientId = patientPreferences.getPatientId();
                sAssignAuthority = patientPreferences.getAssigningAuthority();
            }
            sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTIES_FILE, HOME_COMMUNITY_PROPERTY);
        } else {
            throw new AdapterPIPException("StorePtConsentRequest requires patient preferences");
        }

        try { // Call Web Service Operation
            gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumer service = new gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumer();
            gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType port = service.getEntityNotificationConsumerPortSoap11();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

            // TODO initialize WS operation arguments here
            gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType notifyRequest = new gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType();
            //build and set Assertion
            AssertionType assertion = buildAssertionInfo(sHomeCommunityId);
            notifyRequest.setAssertion(assertion);
            Notify notify = new Notify();
            NotificationMessageHolderType notificationMessageHolder = buildNotificationMessageHolderType(sPatientId, sHomeCommunityId, sHomeCommunityId);
            notify.getNotificationMessage().add(notificationMessageHolder);
            notifyRequest.setNotify(notify);
            gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType result = port.notify(notifyRequest);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is used to build Notification Message Holder Type object
     * @param sHId
     * @param sPid
     * @param sAssignAuthority
     * @return NotificationMessageHolderType
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    private NotificationMessageHolderType buildNotificationMessageHolderType(String sPid, String sHomeCommunityId, String sAssignAuthority)
            throws AdapterPIPException
    {
        NotificationMessageHolderType notificationMessageHolder = new NotificationMessageHolderType();
        Message message = new Message();
        ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = new RetrieveDocumentSetRequestType();
        PatientConsentManager oManager = new PatientConsentManager();
        DocumentRequest documentRequest = oManager.retrieveCPPDocIdentifiers(sPid, sAssignAuthority);
//        DocumentRequest documentRequest = new DocumentRequest();
//        documentRequest.setDocumentUniqueId(sPid+'-'+CDAConstants.METADATA_CLASS_CODE);
//        documentRequest.setHomeCommunityId(sHomeCommunityId);
//        documentRequest.setRepositoryUniqueId("1");
        retrieveDocumentSetRequest.getDocumentRequest().add(documentRequest);
        JAXBElement<RetrieveDocumentSetRequestType> retrieveDocumentSetRequestObject = factory.createRetrieveDocumentSetRequest(retrieveDocumentSetRequest);
        message.setAny(retrieveDocumentSetRequestObject);
        notificationMessageHolder.setMessage(message);
        //notificationMessageHolder.
        return notificationMessageHolder;
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
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.DATE_OF_SIGNATURE);
            if (svalue != null && svalue.length() > 0)
            {
                assertion.setDateOfSignature(svalue.trim());
            } 
            else
            {
                assertion.setDateOfSignature("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.EXPIRATION_DATE);
            if (null != svalue && svalue.length() > 0)
            {
                assertion.setExpirationDate(svalue.trim());
            } else
            {
                assertion.setExpirationDate("");
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
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.CONTENT_REFERENCE);
            if (null != svalue && svalue.length() > 0)
            {
                assertion.setClaimFormRef(svalue.trim());
            } 
            else
            {
                assertion.setClaimFormRef("");
            }
            svalue = PropertyAccessor.getProperty(ASSERTIONINFO_PROPFILE_NAME, CDAConstants.CONTENT);
            svalue = svalue.trim();
            if (null != svalue && svalue.length() > 0)
            {
                assertion.setClaimFormRaw(svalue.getBytes());
            } 
            else
            {
                assertion.setClaimFormRaw("".getBytes());
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
        entitynotificationconsumerPort = service.getEntityNotificationConsumerPortSoap11();
        // Need to load in the correct UDDI endpoint URL address.
        //--------------------------------------------------------
        ((BindingProvider) entitynotificationconsumerPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        return entitynotificationconsumerPort;
    }
}
