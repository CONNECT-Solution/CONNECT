/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

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
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.util.Date;
import java.util.List;
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
public class PatientConsentDocumentBuilderHelper {

    private static final Logger LOG = LoggerFactory.getLogger(PatientConsentDocumentBuilderHelper.class);
    private UTCDateUtil utcDateUtil = null;
    private static final String FILE_NAME = "XDSUniqueIds";
    private static String sPropertyFile = null;
    private static final String PDF_MIME_TYPE = "application/pdf";

    public PatientConsentDocumentBuilderHelper() {
        if (sPropertyFile == null) {
            sPropertyFile = getPropertiesFilePath();
        }
        utcDateUtil = createUTCDateUtil();
    }

    protected UTCDateUtil createUTCDateUtil() {
        return utcDateUtil != null ? utcDateUtil : new UTCDateUtil();
    }

    protected String getPropertiesFilePath() {
        String propertiesFilePath = null;
        final String sValue = PropertyAccessor.getInstance().getPropertyFileLocation();
        if (StringUtils.isNotEmpty(sValue)) {
            // Set it up so that we always have a "/" at the end - in case
            // ------------------------------------------------------------
            if (sValue.endsWith("/") || sValue.endsWith("\\")) {
                propertiesFilePath = sValue;
            } else {
                final String sFileSeparator = System.getProperty("file.separator");
                propertiesFilePath = sValue + sFileSeparator + FILE_NAME;
            }
        } else {
            LOG.error("Path to property files not found");
        }
        return propertiesFilePath;
    }

    public SubmitObjectsRequest createSubmitObjectRequest(final String sTargetObject, final String sHid,
            final String sDocUniqueId, final String sMimeType, PatientPreferencesType oPtPref)
                    throws PropertyAccessException {
        LOG.info("------- Begin PatientConsentDocumentBuilderHelper.createSubmitObjectRequest -------");
        LOG.info("Document: " + sDocUniqueId + " of type: " + sMimeType);
        String hl7PatientId = "";
        final SubmitObjectsRequest oSubmitObjectRequest = new SubmitObjectsRequest();
        final RegistryObjectListType oRegistryObjectList = new RegistryObjectListType();

        if (oPtPref != null) {
            if (oPtPref.getPatientId() != null && !oPtPref.getPatientId().contains("&ISO")) {
                hl7PatientId = PatientIdFormatUtil.hl7EncodePatientId(oPtPref.getPatientId(),
                        oPtPref.getAssigningAuthority());
                hl7PatientId = StringUtil.extractStringFromTokens(hl7PatientId, "'");
            } else {
                hl7PatientId = oPtPref.getPatientId();
            }
        }

        final List<JAXBElement<? extends IdentifiableType>> oRegistryObjList = oRegistryObjectList.getIdentifiable();
        final ExtrinsicObjectType oExtObj = new ExtrinsicObjectType();
        oExtObj.setId(sDocUniqueId);
        oExtObj.setHome(sHid);

        if (StringUtils.isNotEmpty(sMimeType)) {
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
        final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots = oExtObj.getSlot();

        setCreationDate(oSlots, oPtPref, oRimObjectFactory, sMimeType, sDocUniqueId);
        setLanguageCode(oSlots, oPtPref, oRimObjectFactory, sMimeType);
        setSourcePatientId(oSlots, oPtPref, oRimObjectFactory, hl7PatientId, sMimeType);
        setPatientInfo(oSlots, oPtPref, oRimObjectFactory, sMimeType, sDocUniqueId);
        setTitle(oExtObj, oPtPref, oRimObjectFactory, sMimeType, sDocUniqueId);

        // ClassificationType oClassificationCDAR2 = createCDARClassification(sDocUniqueId,
        // oRimObjectFactory);
        // oExtObj.getClassification().add(oClassificationCDAR2);
        setClassCode(oExtObj, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);

        setConfidentialityCode(oExtObj, oRimObjectFactory, sDocUniqueId, oPtPref, sMimeType);
        setHealthcareFacilityTypeCode(oExtObj, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setPracticeSettingCode(oExtObj, oPtPref, oRimObjectFactory, sDocUniqueId, sMimeType);
        setPatientId(oExtObj, oRimObjectFactory, sDocUniqueId, hl7PatientId);
        setDocumentUniqueId(oExtObj, oRimObjectFactory, sDocUniqueId);
        final JAXBElement<? extends IdentifiableType> oJAXBExtId = oRimObjectFactory.createExtrinsicObject(oExtObj);
        oRegistryObjList.add(oJAXBExtId);
        final RegistryPackageType oRegistryPackage = new RegistryPackageType();
        oRegistryPackage.setObjectType(CDAConstants.XDS_REGISTRY_REGISTRY_PACKAGE_TYPE);
        oRegistryPackage.setId(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlotsReg = oRegistryPackage.getSlot();

        setSubmissionDate(oSlotsReg, oPtPref, oRimObjectFactory);

        final ClassificationType oClassificationContentType = createClassification(oRimObjectFactory,
                CDAConstants.XDS_REGISTRY_CONTENT_TYPE_UUID, CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                "", "History and Physical", CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, "codingScheme",
                "Connect-a-thon contentTypeCodes", CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                "History and Physical");
        oRegistryPackage.getClassification().add(oClassificationContentType);

        ExternalIdentifierType oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_PATIENTID, CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                hl7PatientId, CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_PATIENT_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);

        final String sSubmissionSetUniqueId = PropertyAccessor.getInstance().getProperty(FILE_NAME,
                "submissionsetuniqueid");
        // getOidFromProperty("submissionsetuniqueid");
        oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_UNIQUEID, CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sSubmissionSetUniqueId, CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_DOCUMENT_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);
        setHomeCommunityId(oRegistryPackage, oRimObjectFactory, sHid);
        final JAXBElement<? extends IdentifiableType> oJAXBRegPack = oRimObjectFactory
                .createRegistryPackage(oRegistryPackage);
        oRegistryObjList.add(oJAXBRegPack);
        final AssociationType1 oAssociation = new AssociationType1();
        oAssociation.setAssociationType(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE);
        oAssociation.setSourceObject(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        oAssociation.setTargetObject(sDocUniqueId);
        oAssociation.setId(UUID.randomUUID().toString());
        oAssociation.setObjectType(CDAConstants.XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE);
        final SlotType1 oSlot = createSlot(oRimObjectFactory, "SubmissionSetStatus", "Original");
        oAssociation.getSlot().add(oSlot);
        final JAXBElement<? extends IdentifiableType> oJAXBAss = oRimObjectFactory.createAssociation(oAssociation);
        oRegistryObjList.add(oJAXBAss);
        if (sTargetObject != null && !sTargetObject.isEmpty()) {
            LOG.info("Found Doc Id - Begin Association RPLC Created");
            final AssociationType1 oAssociationRPLC = new AssociationType1();
            oAssociationRPLC.setAssociationType(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE_RPLC);
            oAssociationRPLC.setSourceObject(sDocUniqueId);
            oAssociationRPLC.setTargetObject(sTargetObject);
            oAssociationRPLC.setId(UUID.randomUUID().toString());
            oAssociationRPLC.setObjectType(CDAConstants.XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE);
            final JAXBElement<? extends IdentifiableType> oJAXBAssRplc = oRimObjectFactory
                    .createAssociation(oAssociationRPLC);
            oRegistryObjList.add(oJAXBAssRplc);
            LOG.info("End Association RPLC Created");
        }

        final ClassificationType oClassificationSumissionSet = new ClassificationType();
        oClassificationSumissionSet
                .setClassificationNode(CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_CLASSIFICATION_UUID);
        oClassificationSumissionSet.setClassifiedObject(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        oClassificationSumissionSet.setObjectType(CDAConstants.CLASSIFICATION_REGISTRY_OBJECT);
        oClassificationSumissionSet.setId(UUID.randomUUID().toString());
        final JAXBElement<? extends IdentifiableType> oJAXBClass = oRimObjectFactory
                .createClassification(oClassificationSumissionSet);
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
        LOG.info("------- End PatientConsentDocumentBuilderHelper.createSubmitObjectRequest -------");
        return oSubmitObjectRequest;
    }

    private void setMimeTypeAndStatus(final ExtrinsicObjectType oExtObj, final PatientPreferencesType oPtPref) {
        String mimeType = null;
        if (oExtObj != null) {
            if (oPtPref != null && oPtPref.getFineGrainedPolicyMetadata() != null
                    && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getMimeType())) {
                mimeType = oPtPref.getFineGrainedPolicyMetadata().getMimeType();
            }

            if (mimeType == null) {
                mimeType = CDAConstants.PROVIDE_REGISTER_MIME_TYPE;
            }

            oExtObj.setMimeType(mimeType);
            oExtObj.setStatus(CDAConstants.PROVIDE_REGISTER_STATUS_APPROVED);
        }
    }

    private void setComments(final ExtrinsicObjectType oExtObj, final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        if (oExtObj != null && oPtPref != null && oPtPref.getFineGrainedPolicyMetadata() != null
                && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getComments())) {
            final String sComments = oPtPref.getFineGrainedPolicyMetadata().getComments();
            InternationalStringType description = oExtObj.getDescription();
            if (description == null) {
                description = new InternationalStringType();
                oExtObj.setDescription(description);
            }
            final LocalizedStringType localString = oRimObjectFactory.createLocalizedStringType();
            localString.setValue(sComments);
            description.getLocalizedString().add(localString);
        }
    }

    private void setCreationDate(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sMimeType,
            final String sDocUniqueId) {
        if (oSlots != null && oRimObjectFactory != null) {
            String sFormattedDate = null;
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                        && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                        && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                            .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            sFormattedDate = eachBinaryDocumentPolicyCriteria.getEffectiveTime();
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                    sFormattedDate = oPtPref.getFineGrainedPolicyMetadata().getCreationTime();
                }
            }
            if (NullChecker.isNullish(sFormattedDate)) {
                final Date date = new Date();
                sFormattedDate = utcDateUtil.formatUTCDate(date);
            }
            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_CREATION_TIME, sFormattedDate));
        }
    }

    private void setSubmissionDate(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlotsReg,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        final Date date = new Date();
        oSlotsReg.add(createSlot(oRimObjectFactory, CDAConstants.XDS_REGISTRY_SLOT_NAME_SUBMISSION_TIME,
                utcDateUtil.formatUTCDate(date)));
    }

    private void setLanguageCode(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sMimeType) {
        if (oSlots != null && oRimObjectFactory != null) {
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

    private void setSize(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        if (oSlots != null && oRimObjectFactory != null) {
            String sSize;
            if (oPtPref != null && oPtPref.getFineGrainedPolicyMetadata() != null) {
                sSize = oPtPref.getFineGrainedPolicyMetadata().getSize();
                if (NullChecker.isNotNullish(sSize)) {
                    oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SIZE, sSize));
                }
            }
        }
    }

    private void setLegalAuthenticator(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        String sLegalAuthenticator;
        if (oSlots != null && oRimObjectFactory != null) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                        && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                        && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                            .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())
                                && eachBinaryDocumentPolicyCriteria.getLegalAuthenticator() != null) {
                            sLegalAuthenticator = extractAuthorPerson(eachBinaryDocumentPolicyCriteria
                                    .getLegalAuthenticator().getAuthenticatorPersonName());
                            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_LEGAL_AUTHENTICATOR,
                                    sLegalAuthenticator));
                            break;
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                    sLegalAuthenticator = oPtPref.getFineGrainedPolicyMetadata().getLegalAuthenticator();
                    if (NullChecker.isNotNullish(sLegalAuthenticator)) {
                        oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_LEGAL_AUTHENTICATOR,
                                sLegalAuthenticator));
                    }
                }
            }
        }
    }

    private void setDocumentUri(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final String sDocumentURI, final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory) {
        if (oSlots != null && oRimObjectFactory != null) {
            if (NullChecker.isNotNullish(sDocumentURI)) {
                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_URI, sDocumentURI));
            }
        }
    }

    private void setServiceStartTime(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        if (oSlots != null && oRimObjectFactory != null) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                        && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                        && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                            .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_START_TIME,
                                    eachBinaryDocumentPolicyCriteria.getStartDate()));
                            break;
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null
                        && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getServiceStartTime())) {
                    oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_START_TIME,
                            oPtPref.getFineGrainedPolicyMetadata().getServiceStartTime()));
                }
            }
        }

    }

    private void setServiceStopTime(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        if (oSlots != null && oRimObjectFactory != null) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                        && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                        && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                            .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_STOP_TIME,
                                    eachBinaryDocumentPolicyCriteria.getEndDate()));
                            break;
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null
                        && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getServiceStopTime())) {
                    oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SERVICE_STOP_TIME,
                            oPtPref.getFineGrainedPolicyMetadata().getServiceStopTime()));
                }
            }
        }
    }

    private void setIntendedRecipient(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        if (oSlots != null && oRimObjectFactory != null) {
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                        && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                        && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                            .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            if (eachBinaryDocumentPolicyCriteria.getIntendedRecipient() != null
                                    && !eachBinaryDocumentPolicyCriteria.getIntendedRecipient().isEmpty()) {
                                oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_INTENDED_RECIPIENT,
                                        eachBinaryDocumentPolicyCriteria.getIntendedRecipient()));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void setSourcePatientId(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory,
            final String defaultSourcePatientId, final String sMimeType) {
        if (oSlots != null && oRimObjectFactory != null) {
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

    private void setTitle(final ExtrinsicObjectType oExtObj, final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sMimeType,
            final String sDocUniqueId) {

        if (oExtObj != null) {
            String sTitle = null;
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                        && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                        && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                            .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                            sTitle = eachBinaryDocumentPolicyCriteria.getDocumentTitle();
                        }
                    }
                } else if (oPtPref.getFineGrainedPolicyMetadata() != null
                        && NullChecker.isNotNullish(oPtPref.getFineGrainedPolicyMetadata().getDocumentTitle())) {
                    sTitle = oPtPref.getFineGrainedPolicyMetadata().getDocumentTitle();
                }
            }

            if (sTitle == null) {
                sTitle = CDAConstants.TITLE;
            }

            oExtObj.setName(createInternationalStringType(oRimObjectFactory, CDAConstants.CHARACTER_SET,
                    CDAConstants.LANGUAGE_CODE_ENGLISH, sTitle));
        }
    }

    private void setPatientInfo(final List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots,
            final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sMimeType,
            final String sDocUniqueId) {
        if (oSlots != null && oRimObjectFactory != null) {
            String sPid3 = null;
            String sPid5 = null;
            String sPid7 = null;
            String sPid8 = null;
            String sPid11 = null;
            if (oPtPref != null) {
                if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                        && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                        && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                    for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                            .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                        if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())
                                && eachBinaryDocumentPolicyCriteria.getPatientInfo() != null) {
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
            // if (NullChecker.isNullish(sPid3))
            // {
            // // TODO: What is this about? PID3 required?
            // sPid3 = "pid1^^^domain";
            // }
            // oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-3|" + sPid3));
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
                oSlots.add(
                        createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_INFO, "PID-11|" + sPid11));
            }
        }
    }

    private void setClassCode(final ExtrinsicObjectType oExtObj, final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        ClassificationType oClassificationClassCode = null;
        if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
            for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                    .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())
                        && eachBinaryDocumentPolicyCriteria.getDocumentTypeCode() != null) {
                    oClassificationClassCode = createClassification(oRimObjectFactory,
                            CDAConstants.XDS_CLASS_CODE_SCHEMA_UUID, sDocUniqueId, "",
                            eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getCode(),
                            CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                            eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getCodeSystem(),
                            CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                            eachBinaryDocumentPolicyCriteria.getDocumentTypeCode().getDisplayName());
                    break;
                }
            }
        }

        if (oClassificationClassCode == null) {
            oClassificationClassCode = createClassification(oRimObjectFactory, CDAConstants.XDS_CLASS_CODE_SCHEMA_UUID,
                    sDocUniqueId, "", CDAConstants.METADATA_CLASS_CODE, CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                    CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, CDAConstants.CODE_SYSTEM_LOINC_OID,
                    CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                    CDAConstants.METADATA_CLASS_CODE_DISPLAY_NAME);
        }
        oExtObj.getClassification().add(oClassificationClassCode);
    }

    private void setFormatCode(final ExtrinsicObjectType oExtObj,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId) {
        String sFormatCode = CDAConstants.METADATA_FORMAT_CODE_XACML;
        if (PDF_MIME_TYPE.equals(oExtObj.getMimeType())) {
            sFormatCode = CDAConstants.METADATA_FORMAT_CODE_PDF;
        }

        final ClassificationType oClassificationClassCode = createClassification(oRimObjectFactory,
                CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_FORMAT_CODE, sDocUniqueId, "", sFormatCode,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                CDAConstants.METADATA_FORMAT_CODE_SYSTEM, CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH, "");
        oExtObj.getClassification().add(oClassificationClassCode);
    }

    private void setTypeCode(final ExtrinsicObjectType oExtObj, final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        String sTypeCd = "";
        String sTypeCdOid = "";
        String sTypeCdDisplayName = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                    && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                    && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                        .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())
                            && eachBinaryDocumentPolicyCriteria.getDocumentTypeCode() != null) {
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
        final ClassificationType oClassificationClassCode = createClassification(oRimObjectFactory,
                CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_TYPE_CODE, sDocUniqueId, "", sTypeCd,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, sTypeCdOid,
                CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH, sTypeCdDisplayName);
        oExtObj.getClassification().add(oClassificationClassCode);
    }

    private void setConfidentialityCode(final ExtrinsicObjectType oExtObj,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final PatientPreferencesType oPtPref, final String sMimeType) {
        String sConfidentialityCode = null;
        String sConfidentialityCodeScheme = null;
        String sConfidentialityCodeDisplayName = null;
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                    && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                    && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                        .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())
                            && eachBinaryDocumentPolicyCriteria.getConfidentialityCode() != null) {
                        sConfidentialityCode = eachBinaryDocumentPolicyCriteria.getConfidentialityCode().getCode();
                        sConfidentialityCodeScheme = eachBinaryDocumentPolicyCriteria.getConfidentialityCode()
                                .getCodeSystem();
                        sConfidentialityCodeDisplayName = eachBinaryDocumentPolicyCriteria.getConfidentialityCode()
                                .getDisplayName();
                    }
                }

            } else if (oPtPref.getFineGrainedPolicyMetadata() != null) {
                sConfidentialityCode = oPtPref.getFineGrainedPolicyMetadata().getConfidentialityCode();
                sConfidentialityCodeScheme = oPtPref.getFineGrainedPolicyMetadata().getConfidentialityCodeScheme();
                sConfidentialityCodeDisplayName = oPtPref.getFineGrainedPolicyMetadata()
                        .getConfidentialityCodeDisplayName();
            }
        }

        if (NullChecker.isNullish(sConfidentialityCode)) {
            sConfidentialityCode = "R";
            sConfidentialityCodeScheme = "2.16.840.1.113883.5.25";
            sConfidentialityCodeDisplayName = "Restricted";
        }
        final ClassificationType oClassificationConfd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_CONFIDENTIALITY_CODE_UUID, sDocUniqueId, "", sConfidentialityCode,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                sConfidentialityCodeScheme, CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                sConfidentialityCodeDisplayName);
        oExtObj.getClassification().add(oClassificationConfd);
    }

    private void setHealthcareFacilityTypeCode(final ExtrinsicObjectType oExtObj, final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        String sCode = "";
        String sCodeSyst = "";
        String sDisplayName = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                    && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                    && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                        .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())
                            && eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode() != null) {
                        sCode = eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode().getCode();
                        sCodeSyst = eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode().getCodeSystem();
                        sDisplayName = eachBinaryDocumentPolicyCriteria.getHealthcareFacilityTypeCode()
                                .getDisplayName();
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
        final ClassificationType oClassificationFacCd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_FACILITY_TYPE_UUID, sDocUniqueId, "", sCode,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME, sCodeSyst,
                CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH, sDisplayName);
        oExtObj.getClassification().add(oClassificationFacCd);
    }

    private void setPracticeSettingCode(final ExtrinsicObjectType oExtObj, final PatientPreferencesType oPtPref,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String sMimeType) {
        String sPracticeSetting = "";
        String sPracticeSettingScheme = "";
        String sPracticeSettingDisplayName = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                    && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                    && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                        .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())
                            && eachBinaryDocumentPolicyCriteria.getPracticeSettingCode() != null) {
                        sPracticeSetting = eachBinaryDocumentPolicyCriteria.getPracticeSettingCode().getCode();
                        sPracticeSettingScheme = eachBinaryDocumentPolicyCriteria.getPracticeSettingCode()
                                .getCodeSystem();
                        sPracticeSettingDisplayName = eachBinaryDocumentPolicyCriteria.getPracticeSettingCode()
                                .getDisplayName();
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
        final ClassificationType oClassificationPractCd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_PRACTICE_SETTING_CD_UUID, sDocUniqueId, "", sPracticeSetting,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                sPracticeSettingScheme, CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                sPracticeSettingDisplayName);
        oExtObj.getClassification().add(oClassificationPractCd);
    }

    private void setPatientId(final ExtrinsicObjectType oExtObj,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final String hl7PatientId) {
        final ExternalIdentifierType oExtIdTypePat = createExternalIdentifier(oRimObjectFactory, sDocUniqueId,
                CDAConstants.EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE, hl7PatientId, CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH, CDAConstants.PROVIDE_REGISTER_SLOT_NAME_PATIENT_ID);
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
    }

    private void setDocumentUniqueId(final ExtrinsicObjectType oExtObj,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId) {
        final ExternalIdentifierType oExtIdTypePat = createExternalIdentifier(oRimObjectFactory, sDocUniqueId,
                CDAConstants.DOCUMENT_ID_IDENT_SCHEME, CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE, sDocUniqueId,
                CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOCUMENT_ID);
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
    }

    private void setHomeCommunityId(final RegistryPackageType oRegistryPackage,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory,
            final String sHomeCommunityId) {
        final ExternalIdentifierType oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_SOURCE_ID_UUID,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE, sHomeCommunityId, CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH, CDAConstants.PROVIDE_REGISTER_SLOT_NAME_SUBMISSION_SET_SOURCE_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);
    }

    private void setAuthor(final ExtrinsicObjectType oExtObj,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final PatientPreferencesType oPtPref, final String sMimeType) {
        String sAuthorPerson = "";
        String sAuthorInstitution = "";
        String sAuthorRole = "";
        String sAuthorSpecialty = "";
        if (oPtPref != null) {
            if (PDF_MIME_TYPE.equals(sMimeType) && oPtPref.getBinaryDocumentPolicyCriteria() != null
                    && oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null
                    && !oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().isEmpty()) {
                for (final BinaryDocumentPolicyCriterionType eachBinaryDocumentPolicyCriteria : oPtPref
                        .getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                    if (sDocUniqueId.equals(eachBinaryDocumentPolicyCriteria.getDocumentUniqueId())) {
                        if (eachBinaryDocumentPolicyCriteria.getAuthorOriginal() != null) {
                            sAuthorPerson = extractAuthorPerson(
                                    eachBinaryDocumentPolicyCriteria.getAuthorOriginal().getName());
                            sAuthorInstitution = eachBinaryDocumentPolicyCriteria.getAuthorOriginal()
                                    .getRepresentedOrganizationName();
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
                final ClassificationType oClassificationAuthor = oRimObjectFactory.createClassificationType();
                oClassificationAuthor.setClassificationScheme(CDAConstants.XDS_AUTHOR);
                oClassificationAuthor.setClassifiedObject(sDocUniqueId);
                oClassificationAuthor.setNodeRepresentation("Author");
                oClassificationAuthor.setId(UUID.randomUUID().toString());

                oClassificationAuthor.setObjectType(CDAConstants.CLASSIFICATION_REGISTRY_OBJECT);

                if (NullChecker.isNotNullish(sAuthorPerson)) {
                    oClassificationAuthor.getSlot().add(createSlot(oRimObjectFactory,
                            CDAConstants.CLASSIFICATION_SLOT_AUTHOR_PERSON, sAuthorPerson));
                }
                if (NullChecker.isNotNullish(sAuthorInstitution)) {
                    oClassificationAuthor.getSlot().add(createSlot(oRimObjectFactory,
                            CDAConstants.CLASSIFICATION_SLOT_AUTHOR_INSTITUTION, sAuthorInstitution));
                }
                if (NullChecker.isNotNullish(sAuthorRole)) {
                    oClassificationAuthor.getSlot().add(
                            createSlot(oRimObjectFactory, CDAConstants.CLASSIFICATION_SLOT_AUTHOR_ROLE, sAuthorRole));
                }
                if (NullChecker.isNotNullish(sAuthorSpecialty)) {
                    oClassificationAuthor.getSlot().add(createSlot(oRimObjectFactory,
                            CDAConstants.CLASSIFICATION_SLOT_AUTHOR_SPECIALTY, sAuthorSpecialty));
                }

                oClassificationAuthor.setName(createInternationalStringType(oRimObjectFactory,
                        CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH, "Author"));

                oExtObj.getClassification().add(oClassificationAuthor);
            }
        }
    }

    private void setEventCodes(final ExtrinsicObjectType oExtObj,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final PatientPreferencesType oPtPref) {
        if (oPtPref != null && oPtPref.getFineGrainedPolicyMetadata() != null) {
            for (final CeType eventCode : oPtPref.getFineGrainedPolicyMetadata().getEventCodes()) {
                setEventCode(oExtObj, oRimObjectFactory, sDocUniqueId, eventCode);
            }
        }
    }

    private void setEventCode(final ExtrinsicObjectType oExtObj,
            final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory, final String sDocUniqueId,
            final CeType eventCode) {
        if (eventCode != null) {
            final String sCode = eventCode.getCode();
            final String sCodeScheme = eventCode.getCodeSystem();
            final String sCodeDisplayName = eventCode.getDisplayName();
            if (NullChecker.isNotNullish(sCode)) {

                final ClassificationType oClassification = createClassification(oRimObjectFactory,
                        CDAConstants.XDS_EVENT_CODE_LIST_CLASSIFICATION, sDocUniqueId, "", sCode,
                        CDAConstants.CLASSIFICATION_REGISTRY_OBJECT, CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                        CDAConstants.METADATA_EVENT_CODE_SYSTEM, CDAConstants.CHARACTER_SET,
                        CDAConstants.LANGUAGE_CODE_ENGLISH, ""); // Display name is not stored
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
    // private ClassificationType createCDARClassification(String sDocId, ObjectFactory oFactory)
    // {
    // log.info("------- Begin PatientConsentDocumentBuilderHelper.createCDARClassification -------");
    // ClassificationType cClass = createClassification(oFactory,
    // CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_CDAR2,
    // sDocId,
    // "",
    // CDAConstants.NODE_REPRESENTATION_CDAR2,
    // CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
    // CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
    // CDAConstants.CDAR2_VALUE,
    // CDAConstants.CHARACTER_SET,
    // CDAConstants.LANGUAGE_CODE_ENGLISH,
    // CDAConstants.NODE_REPRESENTATION_CDAR2);
    // log.info("------- End PatientConsentDocumentBuilderHelper.createCDARClassification -------");
    // return cClass;
    // }
    /**
     *
     * @param fact
     * @param name
     * @param value
     * @return SlotType1
     */
    private SlotType1 createSlot(final ObjectFactory fact, final String name, final String value) {
        LOG.info("------- Begin PatientConsentDocumentBuilderHelper.createSlot -------");
        final SlotType1 slot = fact.createSlotType1();
        slot.setName(name);
        final ValueListType valList = new ValueListType();
        valList.getValue().add(value);
        slot.setValueList(valList);
        LOG.info("------- End PatientConsentDocumentBuilderHelper.createSlot -------");
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
    private ExternalIdentifierType createExternalIdentifier(final ObjectFactory fact, final String regObject,
            final String identScheme, final String objType, final String value, final String nameCharSet,
            final String nameLang, final String nameVal) {
        LOG.info("------- Begin PatientConsentDocumentBuilderHelper.createExternalIdentifier -------");
        final String id = UUID.randomUUID().toString();
        final ExternalIdentifierType idType = fact.createExternalIdentifierType();
        idType.setId(id);
        idType.setRegistryObject(regObject);
        idType.setIdentificationScheme(identScheme);
        idType.setObjectType(objType);
        idType.setValue(value);
        idType.setName(createInternationalStringType(fact, nameCharSet, nameLang, nameVal));
        LOG.info("------- End PatientConsentDocumentBuilderHelper.createExternalIdentifier -------");
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
    private ClassificationType createClassification(final ObjectFactory fact, final String scheme,
            final String clObject, final String id, final String nodeRep, final String objType, final String slotName,
            final String slotVal, final String nameCharSet, final String nameLang, final String nameVal) {
        LOG.info("------- Begin PatientConsentDocumentBuilderHelper.createClassification -------");
        final ClassificationType cType = fact.createClassificationType();
        cType.setClassificationScheme(scheme);
        cType.setClassifiedObject(clObject);
        cType.setNodeRepresentation(nodeRep);
        if (StringUtils.isNotEmpty(id)) {
            cType.setId(id);
        } else {
            cType.setId(UUID.randomUUID().toString());
        }
        cType.setObjectType(objType);
        cType.getSlot().add(createSlot(fact, slotName, slotVal));
        cType.setName(createInternationalStringType(fact, nameCharSet, nameLang, nameVal));
        LOG.info("------- End PatientConsentDocumentBuilderHelper.createClassification -------");
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
    private InternationalStringType createInternationalStringType(final ObjectFactory fact, final String charSet,
            final String language, final String value) {
        LOG.info("------- Begin PatientConsentDocumentBuilderHelper.createInternationalStringType -------");
        final InternationalStringType intStr = fact.createInternationalStringType();
        final LocalizedStringType locStr = fact.createLocalizedStringType();
        locStr.setCharset(charSet);
        locStr.setLang(language);
        locStr.setValue(value);
        intStr.getLocalizedString().add(locStr);
        LOG.info("------- End PatientConsentDocumentBuilderHelper.createInternationalStringType -------");
        return intStr;
    }

    private String extractAddressFromPatInfo(final PolicyPatientInfoType patInfo) {
        final StringBuffer sAddr = new StringBuffer();
        if (patInfo != null) {
            final AddressesType addressesType = patInfo.getAddr();
            if (addressesType != null && addressesType.getAddress() != null && !addressesType.getAddress().isEmpty()) {
                final AddressType addressType = addressesType.getAddress().get(0);
                sAddr.append(addressType.getStreetAddress());
                sAddr.append(addressType.getCity());
                sAddr.append(addressType.getState());
                sAddr.append(addressType.getZipCode());
                sAddr.append(addressType.getCountry());
            }
        }
        return sAddr.toString();
    }

    private String extractCeTypeForCode(final CeType sType) {
        String sCode = "";
        if (sType != null) {
            sCode = sType.getCode();
        }
        return sCode;
    }

    private String extractPatientName(final PersonNameType oPersonName) {
        String sName = "";
        if (oPersonName != null) {
            sName = oPersonName.getFamilyName() + "^" + oPersonName.getGivenName() + "^^^";
        }
        return sName;
    }

    private String extractAuthorPerson(final PersonNameType oPersonName) {
        final StringBuffer authorName = new StringBuffer();
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
