package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommon.AddressType;
import gov.hhs.fha.nhinc.common.nhinccommon.AddressesType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyPatientInfoType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class PatientConsentDocumentBuilderHelper {

    private Log log = null;
    private UTCDateUtil utcDateUtil = null;
    private static final String PROP_DIR = "NHINC_PROPERTIES_DIR";
    private static final String FILE_NAME = "XDSUniqueIds.properties";
    private static String sPropertyFile = null;
    private static final String PDF_MIME_TYPE = "application/pdf";

    public PatientConsentDocumentBuilderHelper() {
        log = createLogger();
        if (sPropertyFile == null) {
            sPropertyFile = getPropertiesFilePath();
        }
        utcDateUtil = createUTCDateUtil();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected UTCDateUtil createUTCDateUtil()
    {
        return ((utcDateUtil != null) ? utcDateUtil : new UTCDateUtil());
    }

    protected String getPropertiesFilePath() {
        String propertiesFilePath = null;
        String sValue = System.getenv(PROP_DIR);
        if ((sValue != null) && (sValue.length() > 0)) {
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\"))) {
                propertiesFilePath = sValue;
            } else {
                String sFileSeparator = System.getProperty("file.separator");
                propertiesFilePath = sValue + sFileSeparator + FILE_NAME;
            }
        } else {
            log.error("Path to property files not found");
        }
        if (log.isDebugEnabled()) {
            log.debug("Properties file path: " + propertiesFilePath);
        }
        return propertiesFilePath;
    }

    public SubmitObjectsRequest createSubmitObjectRequest(String sTargetObject, String sHid, String sDocUniqueId, String sMimeType, PatientPreferencesType oPtPref) throws PropertyAccessException {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createSubmitObjectRequest -------");
        log.info("Document: " + sDocUniqueId + " of type: " + sMimeType);
        String hl7PatientId = "";
        SubmitObjectsRequest oSubmitObjectRequest = new SubmitObjectsRequest();
        RegistryObjectListType oRegistryObjectList = new RegistryObjectListType();
        if (oPtPref != null &&
                oPtPref.getPatientId() != null &&
                !oPtPref.getPatientId().contains("&ISO")) {
            hl7PatientId = PatientIdFormatUtil.hl7EncodePatientId(oPtPref.getPatientId(), sHid);
            hl7PatientId = StringUtil.extractStringFromTokens(hl7PatientId, "'");
        } else {
            hl7PatientId = oPtPref.getPatientId();
        }
        List<JAXBElement<? extends IdentifiableType>> oRegistryObjList = oRegistryObjectList.getIdentifiable();
        ExtrinsicObjectType oExtObj = new ExtrinsicObjectType();
        oExtObj.setId(sDocUniqueId);
        oExtObj.setHome(sHid);
        if (sMimeType != null) {
            if (oPtPref == null) {
                oPtPref = new PatientPreferencesType();
            }
            if (oPtPref.getFineGrainedPolicyMetadata() == null) {
                oPtPref.setFineGrainedPolicyCriteria(new FineGrainedPolicyCriteriaType());
            }
            oPtPref.getFineGrainedPolicyMetadata().setMimeType(sMimeType);
            oExtObj.setMimeType(sMimeType);
        }
        setMimeTypeAndStatus(oExtObj, oPtPref);
        oExtObj.setObjectType(CDAConstants.PROVIDE_REGISTER_OBJECT_TYPE);
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots = oExtObj.getSlot();

        setCreationDate(oSlots, oPtPref, oRimObjectFactory, sMimeType, sDocUniqueId);
        setLanguageCode(oSlots, oPtPref, oRimObjectFactory, sMimeType);
        setSourcePatientId(oSlots, oPtPref, oRimObjectFactory, hl7PatientId, sMimeType);
        setPatientInfo(oSlots, oPtPref, oRimObjectFactory, sMimeType, sDocUniqueId);
        setTitle(oExtObj, oPtPref, oRimObjectFactory, sMimeType, sDocUniqueId);

//        ClassificationType oClassificationCDAR2 = createCDARClassification(sDocUniqueId,
//                oRimObjectFactory);
//        oExtObj.getClassification().add(oClassificationCDAR2);
        setClassCode(oExtObj, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);

        setConfidentialityCode(oExtObj, oRimObjectFactory, sDocUniqueId, oPtPref, sMimeType);
        setHealthcareFacilityTypeCode(oExtObj, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setPracticeSettingCode(oExtObj, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setPatientId(oExtObj, oRimObjectFactory, sDocUniqueId, hl7PatientId);
        setDocumentUniqueId(oExtObj, oRimObjectFactory, sDocUniqueId);
        JAXBElement<? extends IdentifiableType> oJAXBExtId = oRimObjectFactory.createExtrinsicObject(oExtObj);
        oRegistryObjList.add(oJAXBExtId);
        RegistryPackageType oRegistryPackage = new RegistryPackageType();
        oRegistryPackage.setObjectType(CDAConstants.XDS_REGISTRY_REGISTRY_PACKAGE_TYPE);
        oRegistryPackage.setId(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlotsReg = oRegistryPackage.getSlot();

        setSubmissionDate(oSlotsReg, oPtPref, oRimObjectFactory);

        ClassificationType oClassificationContentType = createClassification(oRimObjectFactory,
                CDAConstants.XDS_REGISTRY_CONTENT_TYPE_UUID,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                "",
                "History and Physical",
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                "codingScheme",
                "Connect-a-thon contentTypeCodes",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "History and Physical");
        oRegistryPackage.getClassification().add(oClassificationContentType);

        ExternalIdentifierType oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory, CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_PATIENTID,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                hl7PatientId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_PATIENT_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);

        String sSubmissionSetUniqueId = getOidFromProperty("submissionsetuniqueid");
        oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_UNIQUEID,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sSubmissionSetUniqueId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_DOCUMENT_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);
        setHomeCommunityId(oRegistryPackage, oRimObjectFactory, sHid);
        JAXBElement<? extends IdentifiableType> oJAXBRegPack = oRimObjectFactory.createRegistryPackage(oRegistryPackage);
        oRegistryObjList.add(oJAXBRegPack);
        AssociationType1 oAssociation = new AssociationType1();
        oAssociation.setAssociationType(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE);
        oAssociation.setSourceObject(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        oAssociation.setTargetObject(sDocUniqueId);
        oAssociation.setId(UUID.randomUUID().toString());
        oAssociation.setObjectType(CDAConstants.XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE);
        SlotType1 oSlot = createSlot(oRimObjectFactory, "SubmissionSetStatus", "Original");
        oAssociation.getSlot().add(oSlot);
        JAXBElement<? extends IdentifiableType> oJAXBAss = oRimObjectFactory.createAssociation(oAssociation);
        oRegistryObjList.add(oJAXBAss);
        if (sTargetObject != null &&
                !sTargetObject.equals("")) {
            log.info("Found Doc Id - Begin Association RPLC Created");
            AssociationType1 oAssociationRPLC = new AssociationType1();
            oAssociationRPLC.setAssociationType(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE_RPLC);
            oAssociationRPLC.setSourceObject(sDocUniqueId);
            oAssociationRPLC.setTargetObject(sTargetObject);
            oAssociationRPLC.setId(UUID.randomUUID().toString());
            oAssociationRPLC.setObjectType(CDAConstants.XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE);
            JAXBElement<? extends IdentifiableType> oJAXBAssRplc = oRimObjectFactory.createAssociation(oAssociationRPLC);
            oRegistryObjList.add(oJAXBAssRplc);
            log.info("End Association RPLC Created");
        }

        ClassificationType oClassificationSumissionSet = new ClassificationType();
        oClassificationSumissionSet.setClassificationNode(CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_CLASSIFICATION_UUID);
        oClassificationSumissionSet.setClassifiedObject(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        oClassificationSumissionSet.setObjectType(CDAConstants.CLASSIFICATION_REGISTRY_OBJECT);
        oClassificationSumissionSet.setId(UUID.randomUUID().toString());
        JAXBElement<? extends IdentifiableType> oJAXBClass = oRimObjectFactory.createClassification(oClassificationSumissionSet);
        oRegistryObjectList.getIdentifiable().add(oJAXBClass);

        setAuthor(oExtObj, oRimObjectFactory, sDocUniqueId, oPtPref, sMimeType);
        setEventCodes(oExtObj, oRimObjectFactory, sDocUniqueId, oPtPref);
        setLegalAuthenticator(oSlots, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setServiceStartTime(oSlots, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setServiceStopTime(oSlots, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setSize(oSlots, oPtPref, oRimObjectFactory);
        setDocumentUri(oSlots, sDocUniqueId, oRimObjectFactory);
        setComments(oExtObj, oPtPref, oRimObjectFactory);
        setFormatCode(oExtObj, oRimObjectFactory, sDocUniqueId);
        setTypeCode(oExtObj, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setIntendedRecipient(oSlots, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        oSubmitObjectRequest.setRegistryObjectList(oRegistryObjectList);
        log.info("------- End PatientConsentDocumentBuilderHelper.createSubmitObjectRequest -------");
        return oSubmitObjectRequest;
    }

    private void setMimeTypeAndStatus(ExtrinsicObjectType oExtObj, PatientPreferencesType oPtPref) {
        String mimeType = null;
        if ((oExtObj != null) && (oPtPref != null) && (oPtPref.getFineGrainedPolicyMetadata() != null) && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getMimeType())) {
            mimeType = oPtPref.getFineGrainedPolicyMetadata().getMimeType();
        }
        if (mimeType == null) {
            mimeType = CDAConstants.PROVIDE_REGISTER_MIME_TYPE;
        }
        oExtObj.setMimeType(mimeType);
        oExtObj.setStatus(CDAConstants.PROVIDE_REGISTER_STATUS_APPROVED);
    }

    private void setComments(ExtrinsicObjectType oExtObj, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        if ((oExtObj != null) && (oPtPref != null) && (oPtPref.getFineGrainedPolicyMetadata() != null) && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getComments())) {
            String sComments = oPtPref.getFineGrainedPolicyMetadata().getComments();
            InternationalStringType description = oExtObj.getDescription();
            if (description == null) {
                description = new InternationalStringType();
                oExtObj.setDescription(description);
            }
            LocalizedStringType localString = oRimObjectFactory.createLocalizedStringType();
            localString.setValue(sComments);
            description.getLocalizedString().add(localString);
        }
    }

    private void setCreationDate(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sMimeType, String sDocUniqueId) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            String sFormattedDate = null;
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            sFormattedDate = eachBinaryDocumentPolicyCriteria.getEffectiveTime();
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                    sFormattedDate = oPtPref.getFineGrainedPolicyMetadata().getCreationTime();
                }
            }
            if (NullChecker.isNullish(sFormattedDate)) {
                Date date = new Date();
                sFormattedDate = utcDateUtil.formatUTCDate(date);
            }
            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_CREATION_TIME, sFormattedDate));
        }
    }

    private void setSubmissionDate(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlotsReg, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        Date date = new Date();
        oSlotsReg.add(createSlot(oRimObjectFactory, CDAConstants.XDS_REGISTRY_SLOT_NAME_SUBMISSION_TIME, utcDateUtil.formatUTCDate(date)));
    }

    private void setLanguageCode(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sMimeType) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            String sLanguageCode = null;
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType)) {
                    sLanguageCode = CDAConstants.LANGUAGE_CODE_ENGLISH;
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                    sLanguageCode = oPtPref.getFineGrainedPolicyMetadata().getLanguageCode();
                }
            }
            if (sLanguageCode == null) {
                sLanguageCode = CDAConstants.LANGUAGE_CODE_ENGLISH;
            }
            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_LANGUAGE_CODE, sLanguageCode));
        }
    }

    private void setSize(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            String sSize = null;
            if ((oPtPref != null) && (oPtPref.getFineGrainedPolicyMetadata() != null)) {
                sSize = oPtPref.getFineGrainedPolicyMetadata().getSize();
                if (NullChecker.isNotNullish(sSize)) {
                    oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SIZE, sSize));
                }
            }
        }
    }

    private void setLegalAuthenticator(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        String sLegalAuthenticator = "";
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId()) && eachBinaryDocumentPolicyCriteria.getLegalAuthenticator() != null) {
                            sLegalAuthenticator = extractAuthorPerson(eachBinaryDocumentPolicyCriteria.getLegalAuthenticator().getAuthenticatorPersonName());
                            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_LEGAL_AUTHENTICATOR, sLegalAuthenticator));
                            break;
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                    sLegalAuthenticator = oPtPref.getFineGrainedPolicyMetadata().getLegalAuthenticator();
                    if (NullChecker.isNotNullish(sLegalAuthenticator)) {
                        oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_LEGAL_AUTHENTICATOR, sLegalAuthenticator));
                    }
                }
            }
        }
    }

    private void setDocumentUri(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, String sDocumentURI, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            if (NullChecker.isNotNullish(sDocumentURI)) {
                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_URI, sDocumentURI));
            }
        }
    }

    private void setServiceStartTime(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_START_TIME, eachBinaryDocumentPolicyCriteria.getStartDate()));
                            break;
                        }
                    }
                } else if ((oPtPref.getFineGrainedPolicyMetadata() != null) && (NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getServiceStartTime()))) {
                    oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_START_TIME, oPtPref.getFineGrainedPolicyMetadata().getServiceStartTime()));
                }
            }
        }

    }

    private void setServiceStopTime(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_STOP_TIME, eachBinaryDocumentPolicyCriteria.getEndDate()));
                            break;
                        }
                    }
                } else if ((oPtPref.getFineGrainedPolicyMetadata() != null) && (NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getServiceStopTime()))) {
                    oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_STOP_TIME, oPtPref.getFineGrainedPolicyMetadata().getServiceStopTime()));
                }
            }
        }
    }

    private void setIntendedRecipient(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            if (eachBinaryDocumentPolicyCriteria.getIntendedRecipient() != null && !eachBinaryDocumentPolicyCriteria.getIntendedRecipient().equals("")) {
                                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_INTENDED_RECIPIENT, eachBinaryDocumentPolicyCriteria.getIntendedRecipient()));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void setSourcePatientId(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String defaultSourcePatientId, String sMimeType) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            String sSourcePatientId = null;
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType)) {
                    sSourcePatientId = oPtPref.getPatientId();
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                    sSourcePatientId = oPtPref.getFineGrainedPolicyMetadata().getSourcePatientId();
                }
            }
            if (sSourcePatientId == null) {
                sSourcePatientId = defaultSourcePatientId;
            }
            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID, sSourcePatientId));
        }
    }

    private void setTitle(ExtrinsicObjectType oExtObj, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sMimeType, String sDocUniqueId) {
        String sTitle = null;
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                        sTitle = eachBinaryDocumentPolicyCriteria.getDocumentTitle();
                    }
                }
            } else if ((oExtObj != null) && (oPtPref.getFineGrainedPolicyMetadata() != null) && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getDocumentTitle())) {
                sTitle = oPtPref.getFineGrainedPolicyMetadata().getDocumentTitle();
            }
        }
        if (sTitle == null) {
            sTitle = CDAConstants.TITLE;
        }
        oExtObj.setName(createInternationalStringType(oRimObjectFactory, CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH, sTitle));

    }

    private void setPatientInfo(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sMimeType, String sDocUniqueId) {
        if ((oSlots != null) && (oRimObjectFactory != null)) {
            String sPid3 = null;
            String sPid5 = null;
            String sPid7 = null;
            String sPid8 = null;
            String sPid11 = null;
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId()) && eachBinaryDocumentPolicyCriteria.getPatientInfo() != null) {
                            sPid3 = oPtPref.getPatientId();
                            sPid5 = extractPatientName(eachBinaryDocumentPolicyCriteria.getPatientInfo().getName());
                            sPid7 = eachBinaryDocumentPolicyCriteria.getPatientInfo().getBirthTime();
                            sPid8 = extractCeTypeForCode(eachBinaryDocumentPolicyCriteria.getPatientInfo().getGender());
                            sPid11 = extractAddressFromPatInfo(eachBinaryDocumentPolicyCriteria.getPatientInfo());
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                    sPid3 = oPtPref.getFineGrainedPolicyMetadata().getPid3();
                    sPid5 = oPtPref.getFineGrainedPolicyMetadata().getPid5();
                    sPid7 = oPtPref.getFineGrainedPolicyMetadata().getPid7();
                    sPid8 = oPtPref.getFineGrainedPolicyMetadata().getPid8();
                    sPid11 = oPtPref.getFineGrainedPolicyMetadata().getPid11();
                }
            }
//            if (NullChecker.isNullish(sPid3))
//            {
//                // TODO: What is this about? PID3 required?
//                sPid3 = "pid1^^^domain";
//            }
//            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-3|" + sPid3));
            if (NullChecker.isNotNullish(sPid3)) {
                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-3|" + sPid3));
            }
            if (NullChecker.isNotNullish(sPid5)) {
                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-5|" + sPid5));
            }
            if (NullChecker.isNotNullish(sPid7)) {
                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-7|" + sPid7));
            }
            if (NullChecker.isNotNullish(sPid8)) {
                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-8|" + sPid8));
            }
            if (NullChecker.isNotNullish(sPid11)) {
                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-11|" + sPid11));
            }
        }
    }

    private void setClassCode(ExtrinsicObjectType oExtObj, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        ClassificationType oClassificationClassCode = null;
        if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
            for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId()) && eachBinaryDocumentPolicyCriteria.getDocumentTypeCode() != null) {
                    oClassificationClassCode = createClassification(oRimObjectFactory,
                            CDAConstants.XDS_CLASS_CODE_SCHEMA_UUID,
                            sDocUniqueId,
                            "",
                            eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getCode(),
                            CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                            CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                            eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getCodeSystem(),
                            CDAConstants.CHARACTER_SET,
                            CDAConstants.LANGUAGE_CODE_ENGLISH,
                            eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getDisplayName());
                    break;
                }
            }
        }

        if (oClassificationClassCode == null) {
            oClassificationClassCode = createClassification(oRimObjectFactory,
                    CDAConstants.XDS_CLASS_CODE_SCHEMA_UUID,
                    sDocUniqueId,
                    "",
                    CDAConstants.METADATA_CLASS_CODE,
                    CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                    CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                    CDAConstants.CODE_SYSTEM_LOINC_OID,
                    CDAConstants.CHARACTER_SET,
                    CDAConstants.LANGUAGE_CODE_ENGLISH,
                    CDAConstants.METADATA_CLASS_CODE_DISPLAY_NAME);
        }
        oExtObj.getClassification().add(oClassificationClassCode);
    }

    private void setFormatCode(ExtrinsicObjectType oExtObj, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId) {
        String sFormatCode = CDAConstants.METADATA_FORMAT_CODE_XACML;
        if (PDF_MIME_TYPE.equals(oExtObj.getMimeType())) {
            sFormatCode = CDAConstants.METADATA_FORMAT_CODE_PDF;
        }

        ClassificationType oClassificationClassCode = createClassification(oRimObjectFactory,
                CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_FORMAT_CODE,
                sDocUniqueId,
                "",
                sFormatCode,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                CDAConstants.METADATA_FORMAT_CODE_SYSTEM,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "");
        oExtObj.getClassification().add(oClassificationClassCode);
    }

    private void setTypeCode(ExtrinsicObjectType oExtObj, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        String sTypeCd = "";
        String sTypeCdOid = "";
        String sTypeCdDisplayName = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId()) && eachBinaryDocumentPolicyCriteria.getDocumentTypeCode() != null) {
                        sTypeCd = eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getCode();
                        sTypeCdOid = eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getCodeSystem();
                        sTypeCdDisplayName = eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getDisplayName();
                        break;
                    }
                }
            }
        }
        if (NullChecker.isNullish(sTypeCd)) {
            sTypeCd = CDAConstants.METADATA_TYPE_CODE;
        }
        if (NullChecker.isNullish(sTypeCdOid)) {
            sTypeCdOid = CDAConstants.CODE_SYSTEM_LOINC_OID;
        }
        if (NullChecker.isNullish(sTypeCdDisplayName)) {
            sTypeCdDisplayName = CDAConstants.METADATA_TYPE_CODE_DISPLAY_NAME;
        }
        ClassificationType oClassificationClassCode = createClassification(oRimObjectFactory,
                CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_TYPE_CODE,
                sDocUniqueId,
                "",
                sTypeCd,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                sTypeCdOid,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                sTypeCdDisplayName);
        oExtObj.getClassification().add(oClassificationClassCode);
    }

    private void setConfidentialityCode(ExtrinsicObjectType oExtObj, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, PatientPreferencesType oPtPref, String sMimeType) {
        String sConfidentialityCode = null;
        String sConfidentialityCodeScheme = null;
        String sConfidentialityCodeDisplayName = null;
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId()) && eachBinaryDocumentPolicyCriteria.getConfidentialityCode() != null) {
                        sConfidentialityCode = eachBinaryDocumentPolicyCriteria.getConfidentialityCode().getCode();
                        sConfidentialityCodeScheme = eachBinaryDocumentPolicyCriteria.getConfidentialityCode().getCodeSystem();
                        sConfidentialityCodeDisplayName = eachBinaryDocumentPolicyCriteria.getConfidentialityCode().getDisplayName();
                    }
                }

            } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                sConfidentialityCode = oPtPref.getFineGrainedPolicyMetadata().getConfidentialityCode();
                sConfidentialityCodeScheme = oPtPref.getFineGrainedPolicyMetadata().getConfidentialityCodeScheme();
                sConfidentialityCodeDisplayName = oPtPref.getFineGrainedPolicyMetadata().getConfidentialityCodeDisplayName();
            }
        }

        if (NullChecker.isNullish(sConfidentialityCode)) {
            sConfidentialityCode = "R";
            sConfidentialityCodeScheme = "2.16.840.1.113883.5.25";
            sConfidentialityCodeDisplayName = "Restricted";
        }
        ClassificationType oClassificationConfd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_CONFIDENTIALITY_CODE_UUID,
                sDocUniqueId,
                "",
                sConfidentialityCode,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                sConfidentialityCodeScheme,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                sConfidentialityCodeDisplayName);
        oExtObj.getClassification().add(oClassificationConfd);
    }

    private void setHealthcareFacilityTypeCode(ExtrinsicObjectType oExtObj, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        String sCode = "";
        String sCodeSyst = "";
        String sDisplayName = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId()) && eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode() != null) {
                        sCode = eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode().getCode();
                        sCodeSyst = eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode().getCodeSystem();
                        sDisplayName = eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode().getDisplayName();
                        break;
                    }
                }
            }
        }
        if (NullChecker.isNullish(sCode)) {
            sCode = CDAConstants.METADATA_NOT_APPLICABLE_CODE;
        }
        if (NullChecker.isNullish(sCodeSyst)) {
            sCodeSyst = CDAConstants.METADATA_NOT_APPLICABLE_CODE_SYSTEM;
        }
        if (NullChecker.isNullish(sDisplayName)) {
            sDisplayName = CDAConstants.METADATA_NOT_APPLICABLE_DISPLAY_NAME;
        }
        ClassificationType oClassificationFacCd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_FACILITY_TYPE_UUID,
                sDocUniqueId,
                "",
                sCode,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                sCodeSyst,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                sDisplayName);
        oExtObj.getClassification().add(oClassificationFacCd);
    }

    private void setPracticeSettingCode(ExtrinsicObjectType oExtObj, PatientPreferencesType oPtPref, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String sMimeType) {
        String sPracticeSetting = "";
        String sPracticeSettingScheme = "";
        String sPracticeSettingDisplayName = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId()) && eachBinaryDocumentPolicyCriteria.getPracticeSettingCode() != null) {
                        sPracticeSetting = eachBinaryDocumentPolicyCriteria.getPracticeSettingCode().getCode();
                        sPracticeSettingScheme = eachBinaryDocumentPolicyCriteria.getPracticeSettingCode().getCodeSystem();
                        sPracticeSettingDisplayName = eachBinaryDocumentPolicyCriteria.getPracticeSettingCode().getDisplayName();
                        break;
                    }
                }
            }
        }
        if (NullChecker.isNullish(sPracticeSetting)) {
            sPracticeSetting = CDAConstants.METADATA_NOT_APPLICABLE_CODE;
            sPracticeSettingScheme = CDAConstants.METADATA_NOT_APPLICABLE_CODE_SYSTEM;
            sPracticeSettingDisplayName = CDAConstants.METADATA_NOT_APPLICABLE_DISPLAY_NAME;
        }
        ClassificationType oClassificationPractCd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_PRACTICE_SETTING_CD_UUID,
                sDocUniqueId,
                "",
                sPracticeSetting,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                sPracticeSettingScheme,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                sPracticeSettingDisplayName);
        oExtObj.getClassification().add(oClassificationPractCd);
    }

    private void setPatientId(ExtrinsicObjectType oExtObj, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, String hl7PatientId) {
        ExternalIdentifierType oExtIdTypePat = createExternalIdentifier(oRimObjectFactory,
                sDocUniqueId,
                CDAConstants.EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                hl7PatientId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_PATIENT_ID);
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
    }

    private void setDocumentUniqueId(ExtrinsicObjectType oExtObj, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId) {
        ExternalIdentifierType oExtIdTypePat = createExternalIdentifier(oRimObjectFactory,
                sDocUniqueId,
                CDAConstants.DOCUMENT_ID_IDENT_SCHEME,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sDocUniqueId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOCUMENT_ID);
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
    }

    private void setHomeCommunityId(RegistryPackageType oRegistryPackage, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sHomeCommunityId) {
        ExternalIdentifierType oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_SOURCE_ID_UUID,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sHomeCommunityId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_SUBMISSION_SET_SOURCE_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);
    }

    private void setAuthor(ExtrinsicObjectType oExtObj, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, PatientPreferencesType oPtPref, String sMimeType) {
        String sAuthorPerson = "";
        String sAuthorInstitution = "";
        String sAuthorRole = "";
        String sAuthorSpecialty = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                        if (eachBinaryDocumentPolicyCriteria.getAuthorOriginal() != null) {
                            sAuthorPerson = extractAuthorPerson(eachBinaryDocumentPolicyCriteria.getAuthorOriginal().getName());
                            sAuthorInstitution = eachBinaryDocumentPolicyCriteria.getAuthorOriginal().getRepresentedOrganizationName();
                            break;
                        }
                    }
                }

            } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                sAuthorPerson = oPtPref.getFineGrainedPolicyMetadata().getAuthorPerson();
                sAuthorInstitution = oPtPref.getFineGrainedPolicyMetadata().getAuthorInstitution();
                sAuthorRole = oPtPref.getFineGrainedPolicyMetadata().getAuthorRole();
                sAuthorSpecialty = oPtPref.getFineGrainedPolicyMetadata().getAuthorSpecialty();
            }
            // Create if person or instituion is provided
            if (NullChecker.isNotNullish(sAuthorPerson) || NullChecker.isNotNullish(sAuthorInstitution)) {
                ClassificationType oClassificationAuthor = oRimObjectFactory.createClassificationType();
                oClassificationAuthor.setClassificationScheme(CDAConstants.XDS_AUTHOR);
                oClassificationAuthor.setClassifiedObject(sDocUniqueId);
                oClassificationAuthor.setNodeRepresentation("Author");
                oClassificationAuthor.setId(UUID.randomUUID().toString());

                oClassificationAuthor.setObjectType(CDAConstants.CLASSIFICATION_REGISTRY_OBJECT);

                if (NullChecker.isNotNullish(sAuthorPerson)) {
                    oClassificationAuthor.getSlot().add(createSlot(oRimObjectFactory, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_PERSON, sAuthorPerson));
                }
                if (NullChecker.isNotNullish(sAuthorInstitution)) {
                    oClassificationAuthor.getSlot().add(createSlot(oRimObjectFactory, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_INSTITUTION, sAuthorInstitution));
                }
                if (NullChecker.isNotNullish(sAuthorRole)) {
                    oClassificationAuthor.getSlot().add(createSlot(oRimObjectFactory, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_ROLE, sAuthorRole));
                }
                if (NullChecker.isNotNullish(sAuthorSpecialty)) {
                    oClassificationAuthor.getSlot().add(createSlot(oRimObjectFactory, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_SPECIALTY, sAuthorSpecialty));
                }

                oClassificationAuthor.setName(createInternationalStringType(oRimObjectFactory, CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH, "Author"));

                oExtObj.getClassification().add(oClassificationAuthor);
            }
        }
    }

    private void setEventCodes(ExtrinsicObjectType oExtObj, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, PatientPreferencesType oPtPref) {
        if ((oPtPref != null) && (oPtPref.getFineGrainedPolicyMetadata() != null)) {
            for (CeType eventCode : oPtPref.getFineGrainedPolicyMetadata().getEventCodes()) {
                setEventCode(oExtObj, oRimObjectFactory, sDocUniqueId, eventCode);
            }
        }
    }

    private void setEventCode(ExtrinsicObjectType oExtObj, oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, String sDocUniqueId, CeType eventCode) {
        if (eventCode != null) {
            String sCode = eventCode.getCode();
            String sCodeScheme = eventCode.getCodeSystem();
            String sCodeDisplayName = eventCode.getDisplayName();
            if (NullChecker.isNotNullish(sCode)) {
                sCodeScheme = ((sCodeScheme != null) ? sCodeScheme : "");
                sCodeDisplayName = ((sCodeDisplayName != null) ? sCodeDisplayName : "");

                ClassificationType oClassification = createClassification(oRimObjectFactory,
                        CDAConstants.XDS_EVENT_CODE_LIST_CLASSIFICATION,
                        sDocUniqueId,
                        "",
                        sCode,
                        CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                        CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                        CDAConstants.METADATA_EVENT_CODE_SYSTEM,
                        CDAConstants.CHARACTER_SET,
                        CDAConstants.LANGUAGE_CODE_ENGLISH,
                        ""); // Display name is not stored
                oExtObj.getClassification().add(oClassification);
            }
        }
    }

    /**
     * 
     * @param sDocId
     * @param oFactory
     * @param oPatCD
     * @return ClassificationType
     */
//    private ClassificationType createCDARClassification(String sDocId, ObjectFactory oFactory)
//    {
//        log.info("------- Begin PatientConsentDocumentBuilderHelper.createCDARClassification -------");
//        ClassificationType cClass = createClassification(oFactory,
//                CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_CDAR2,
//                sDocId,
//                "",
//				CDAConstants.NODE_REPRESENTATION_CDAR2,
//				CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
//                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
//                CDAConstants.CDAR2_VALUE,
//                CDAConstants.CHARACTER_SET,
//                CDAConstants.LANGUAGE_CODE_ENGLISH,
//                CDAConstants.NODE_REPRESENTATION_CDAR2);
//        log.info("------- End PatientConsentDocumentBuilderHelper.createCDARClassification -------");
//        return cClass;
//    }
    /**
     * 
     * @param fact
     * @param name
     * @param value
     * @return SlotType1
     */
    private SlotType1 createSlot(ObjectFactory fact, String name, String value) {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createSlot -------");
        SlotType1 slot = fact.createSlotType1();
        slot.setName(name);
        ValueListType valList = new ValueListType();
        valList.getValue().add(value);
        slot.setValueList(valList);
        log.info("------- End PatientConsentDocumentBuilderHelper.createSlot -------");
        return slot;
    }

    /**
     *
     * @param fact
     * @param id
     * @param regObject
     * @param identScheme
     * @param objType
     * @param value
     * @param nameCharSet
     * @param nameLang
     * @param nameVal
     * @return ExternalIdentifierType
     */
    private ExternalIdentifierType createExternalIdentifier(ObjectFactory fact, String regObject, String identScheme, String objType, String value, String nameCharSet, String nameLang, String nameVal) {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createExternalIdentifier -------");
        String id = UUID.randomUUID().toString();
        ExternalIdentifierType idType = fact.createExternalIdentifierType();
        idType.setId(id);
        idType.setRegistryObject(regObject);
        idType.setIdentificationScheme(identScheme);
        idType.setObjectType(objType);
        idType.setValue(value);
        idType.setName(createInternationalStringType(fact, nameCharSet, nameLang, nameVal));
        log.info("------- End PatientConsentDocumentBuilderHelper.createExternalIdentifier -------");
        return idType;
    }

    /**
     *
     * @param fact
     * @param scheme
     * @param clObject
     * @param id
     * @param nodeRep
     * @param objType
     * @param slotName
     * @param slotVal
     * @param nameCharSet
     * @param nameLang
     * @param nameVal
     * @return ClassificationType
     */
    private ClassificationType createClassification(ObjectFactory fact, String scheme, String clObject, String id, String nodeRep, String objType, String slotName, String slotVal, String nameCharSet, String nameLang, String nameVal) {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createClassification -------");
        ClassificationType cType = fact.createClassificationType();
        cType.setClassificationScheme(scheme);
        cType.setClassifiedObject(clObject);
        cType.setNodeRepresentation(nodeRep);
        if (id != null && !id.equals("")) {
            cType.setId(id);
        } else {
            cType.setId(UUID.randomUUID().toString());
        }
        cType.setObjectType(objType);
        cType.getSlot().add(createSlot(fact, slotName, slotVal));
        cType.setName(createInternationalStringType(fact, nameCharSet, nameLang, nameVal));
        log.info("------- End PatientConsentDocumentBuilderHelper.createClassification -------");
        return cType;
    }

    /**
     *
     * @param fact
     * @param charSet
     * @param language
     * @param value
     * @return InternationalStringType
     */
    private InternationalStringType createInternationalStringType(ObjectFactory fact, String charSet, String language, String value) {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createInternationalStringType -------");
        InternationalStringType intStr = fact.createInternationalStringType();
        LocalizedStringType locStr = fact.createLocalizedStringType();
        locStr.setCharset(charSet);
        locStr.setLang(language);
        locStr.setValue(value);
        intStr.getLocalizedString().add(locStr);
        log.info("------- End PatientConsentDocumentBuilderHelper.createInternationalStringType -------");
        return intStr;
    }

    protected synchronized String getOidFromProperty(String sPropertyName) {
        String sUniqueId = "";
        try {
            Properties tempProp = new Properties();
            InputStream propsInFile = new FileInputStream(sPropertyFile);
            tempProp.load(propsInFile);
            propsInFile.close();
            sUniqueId = tempProp.getProperty(sPropertyName);
            String sFirstPart = sUniqueId.substring(0, sUniqueId.lastIndexOf("."));
            String sDotString = sUniqueId.substring(sUniqueId.lastIndexOf("."));
            sDotString = sDotString.substring(1);
            int iSUniqueId = Integer.parseInt(sDotString);
            iSUniqueId = iSUniqueId + 1;
            String sNewId = sFirstPart + "." + iSUniqueId;
            tempProp.setProperty(sPropertyName, sNewId);
            OutputStream propsOutFile = new FileOutputStream(sPropertyFile);
            tempProp.store(propsOutFile, "Update property file with new sequence");
            propsOutFile.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (log.isDebugEnabled()) {
            log.debug("Generated unique id: " + sUniqueId);
        }
        return sUniqueId;
    }

    private String extractAddressFromPatInfo(PolicyPatientInfoType patInfo) {
        StringBuffer sAddr = new StringBuffer();
        if (patInfo != null) {
            AddressesType addressesType = patInfo.getAddr();
            if (addressesType != null && addressesType.getAddress() != null && !addressesType.getAddress().isEmpty()) {
                AddressType addressType = addressesType.getAddress().get(0);
                sAddr.append(addressType.getStreetAddress());
                sAddr.append(addressType.getCity());
                sAddr.append(addressType.getState());
                sAddr.append(addressType.getZipCode());
                sAddr.append(addressType.getCountry());
            }
        }
        return sAddr.toString();
    }

    private String extractCeTypeForCode(CeType sType) {
        String sCode = "";
        if (sType != null) {
            sCode = sType.getCode();
        }
        return sCode;
    }

    private String extractPatientName(PersonNameType oPersonName) {
        String sName = "";
        if (oPersonName != null) {
            sName = oPersonName.getFamilyName() + "^" + oPersonName.getGivenName() + "^^^";
        }
        return sName;
    }

    private String extractAuthorPerson(PersonNameType oPersonName) {
        StringBuffer authorName = new StringBuffer();
        if (oPersonName != null) {
            authorName.append(oPersonName.getPrefix());
            authorName.append("");
            authorName.append(oPersonName.getGivenName());
            authorName.append(", ");
            authorName.append(oPersonName.getFamilyName());
        }
        return authorName.toString();
    }
}
