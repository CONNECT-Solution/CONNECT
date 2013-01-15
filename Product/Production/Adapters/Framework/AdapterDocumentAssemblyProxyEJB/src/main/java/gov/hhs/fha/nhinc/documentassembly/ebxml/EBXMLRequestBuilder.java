/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.documentassembly.ebxml;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.DocumentType;
import gov.hhs.fha.nhinc.assemblymanager.utils.DateUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import gov.hhs.fha.nhinc.common.docmgr.DocumentManagerClient;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Date;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpCity;
import org.hl7.v3.AdxpCountry;
import org.hl7.v3.AdxpPostalCode;
import org.hl7.v3.AdxpState;
import org.hl7.v3.AdxpStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EnSuffix;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.TSExplicit;
import java.util.ListIterator;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.VersionInfoType;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;

/**
 * 
 * @author kim
 */
public class EBXMLRequestBuilder {

    private static final String UUID_PREFIX = "urn:uuid:";
    private static Log log = LogFactory.getLog(EBXMLRequestBuilder.class);
    ProvideAndRegisterDocumentSetRequestType documentSetRequest = null;

    public EBXMLRequestBuilder() {
    }

    public ProvideAndRegisterDocumentSetRequestType createDocumentSetRequest() {
        if (documentSetRequest == null) {
            documentSetRequest = new ProvideAndRegisterDocumentSetRequestType();
        }

        return documentSetRequest;
    }

    public ExtrinsicObjectType createMetadata(String isoFormatPatientId, String documentId,
        String clinicalUniqueHash, String docUniqueHash, POCDMT000040RecordTarget subject,
        DocumentType docType, Date serviceStartTimeFrom, Date serviceStopTimeTo, String docSize, String docTitle) {

        String rimId = UUID_PREFIX + documentId;

        //display name is dynamic between ER Discharge Summaries and Discharge Summaries
        String docDisplayName = "";
        log.debug("EBXMLRequestBuilder document title from Conemaugh = " + docTitle);

        //create displayName
        if ((docType.getTypeId()).equals(AssemblyConstants.C32_CLASS_CODE)) {
            //C32 - Summarization of Episode Note
            docDisplayName = AssemblyConstants.C32_DISPLAY_NAME;
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_CLASS_CODE)) {
            if (docTitle.contains("Emergency Report")) {
                //ER Discharge Summary
                docDisplayName = AssemblyConstants.C62_DISPLAY_NAME;
            } else if (docTitle.contains("Discharge Summary")) {
                //Discharge Summary
                docDisplayName = AssemblyConstants.C62_DS_DISPLAY_NAME;
            }
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_RR_CLASS_CODE)) {
            //C62 - Radiology Studies
            docDisplayName = AssemblyConstants.C62_RR_DISPLAY_NAME;
        }

        log.debug("EBXMLRequestBuilder: docDisplayName = " + docDisplayName);

        ExtrinsicObjectType extrinsic = new ExtrinsicObjectType();

        extrinsic.setId(rimId);
        extrinsic.setMimeType("text/xml");    //Based on Documentform
        extrinsic.setObjectType(UUID_PREFIX + "7edca82f-054d-47f2-a032-9b2a5b5186c1");    //TODO: Verify that this is invariant
        extrinsic.setStatus("urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
        extrinsic.setLid(rimId);
        VersionInfoType vit = new VersionInfoType();
        vit.setVersionName("1");
        extrinsic.setVersionInfo(vit);

        // Assign the DocumentUnique Identifier
        DocumentManagerClient client = new DocumentManagerClient();
        String uniqueId = client.generateUniqueId(AssemblyConstants.DAS_DOCMGRSERVICE_ENDPOINT);

        // set home community id
        String sHomeCommunityId = null;

        try {
            sHomeCommunityId = PropertyAccessor.getInstance().getProperty(
                NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            extrinsic.setHome("urn:oid:" + sHomeCommunityId);
        } catch (PropertyAccessException e) {
            log.error("Failed to read " +
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY +
                " property from the " + NhincConstants.GATEWAY_PROPERTY_FILE +
                ".properties  file.  Error: " + e.getMessage(), e);
        }

        // creation time - current time
        SlotType1 slot = new SlotType1();
        ValueListType valList = new ValueListType();
        slot.setName("creationTime");
        UTCDateUtil utcTime = new UTCDateUtil();
        valList.getValue().add(utcTime.formatUTCDate(new Date()));
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);

        // language code
        slot = new SlotType1();
        valList = new ValueListType();
        slot.setName("languageCode");
        valList.getValue().add(AssemblyConstants.LANGUAGE);
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);

        // Service start time (return $XDSDocumentEntryCreationTimeFrom)
        if (serviceStartTimeFrom != null) {
            slot = new SlotType1();
            valList = new ValueListType();
            slot.setName("serviceStartTime");
            valList.getValue().add(DateUtil.convertToCDATime(serviceStartTimeFrom));
            slot.setValueList(valList);
            extrinsic.getSlot().add(slot);
        }

        // Service stop time (return $XDSDocumentEntryServiceStopTimeTo)
        if (serviceStopTimeTo != null) {
            slot = new SlotType1();
            valList = new ValueListType();
            slot.setName("serviceStopTime");
            valList.getValue().add(DateUtil.convertToCDATime(serviceStopTimeTo));
            slot.setValueList(valList);
            extrinsic.getSlot().add(slot);
        }

        // Source patient id
        slot = new SlotType1();
        valList = new ValueListType();
        slot.setName("sourcePatientId");
        valList.getValue().add(isoFormatPatientId); // Should be the same as the
        // query conplex Patient Id
        // (HL7 Form)
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);

        // parse Record Target object to provide source patient info
        if (subject != null) {
            slot = new SlotType1();
            valList = new ValueListType();
            slot.setName("sourcePatientInfo");

            POCDMT000040PatientRole ptRole = subject.getPatientRole();
            if (ptRole != null) {
                POCDMT000040Patient patient = ptRole.getPatient();

                // PID-3 segment
                valList.getValue().add("PID-3|" + isoFormatPatientId);

                // PID-5 segment (name), i.e. PID-5|" + "Doe^John^^^
                valList.getValue().add(getPID5(patient.getName()));

                // PID-7 segment (birthtime)
                TSExplicit birthTime = patient.getBirthTime();
                valList.getValue().add("PID-7|" + (birthTime != null ? birthTime.getValue() : ""));

                // PID-8 segment (gender)
                CE gender = patient.getAdministrativeGenderCode();
                valList.getValue().add("PID-8|" + (gender != null ? gender.getDisplayName() : ""));

                // PID-11 (address)
                valList.getValue().add(getPID11(ptRole.getAddr()));
            }

            slot.setValueList(valList);
            extrinsic.getSlot().add(slot);
        }

        // hash, with date + document id
        slot = new SlotType1();
        valList = new ValueListType();
        slot.setName("hash");
        valList.getValue().add(docUniqueHash);
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);

        // clinical unique hash, without date
        slot = new SlotType1();
        valList = new ValueListType();
        slot.setName("urn:gov:hhs:fha:nhinc:xds:clinicalUniqueHash");
        valList.getValue().add(clinicalUniqueHash);
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);

        // set this it marks it as a Dynamic document
        slot = new SlotType1();
        valList = new ValueListType();
        slot.setName("urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed");
        valList.getValue().add("false");
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);

        // Doc Size
        slot = new SlotType1();
        valList = new ValueListType();
        slot.setName("size");
        valList.getValue().add(docSize);
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);

        //URI is MPT a required element
        //Thsi code does NOT generate a valid URI
      /*
        slot = new SlotType1();
        valList = new ValueListType();
        slot.setName("URI");
        valList.getValue().add(uniqueId);
        slot.setValueList(valList);
        extrinsic.getSlot().add(slot);
         */

        // Document name (verify?)
        LocalizedStringType localString = new LocalizedStringType();
        localString.setValue(docDisplayName);
        InternationalStringType intlString = new InternationalStringType();
        intlString.getLocalizedString().add(localString);
        extrinsic.setName(intlString);

        // create Author classification - Establish Data
        String orgSys = null;
        if ((docType.getTypeId()).equals(AssemblyConstants.C32_CLASS_CODE)) {
            orgSys = AssemblyConstants.C32_ORGANIZATION_SYS;
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_CLASS_CODE) || (docType.getTypeId()).equals(AssemblyConstants.C62_RR_CLASS_CODE)) {
            orgSys = AssemblyConstants.C62_ORGANIZATION_SYS;
        }


        // Author Classification
        addClassification(
            extrinsic,
            rimId, //classifiedObject
            UUID_PREFIX + "93606bcf-9494-43ec-9b4e-a7748d1a838d", //scheme
            "Author Code", //node representation
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            null, //name
            new String[]{
                "authorPerson",
                "authorInstitution" /*,
            "authorRole",
            "authorSpecialty",*/}, //slot names
            new String[][]{
                new String[]{(orgSys)},
                new String[]{(AssemblyConstants.ORGANIZATION_NAME)}/*,
            new String[]{""},
            new String[]{""},*/

            } //slot values
            );

        // Create classification for Document Type
        addClassification(extrinsic,
            rimId, //classifiedObject
            UUID_PREFIX + "41a5887f-8865-4c09-adf7-e362475b143a", // - Indictate classCode
            docType.getTypeId(), //node representation - Document Type Class Code
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            docDisplayName, //name
            new String[]{"codingScheme",}, //slot names
            new String[][]{
                new String[]{"2.16.840.1.113883.6.1"},} //slot values
            );

        // Create outpatient evaluation and management classification
        addClassification(extrinsic,
            rimId, //classifiedObject
            UUID_PREFIX + "f0306f51-975f-434e-a61c-c59651d33983", //scheme
            docType.getTypeId(), //node representation
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            docDisplayName, //name
            new String[]{"codingScheme",}, //slot names
            new String[][]{
                new String[]{"2.16.840.1.113883.6.1"},} //slot values
            );

        // Handle the Confidentiality Cpode
        String confCodeDesc = null;
        String confCodeOID = null;
        String confCode = null;

        if ((docType.getTypeId()).equals(AssemblyConstants.C32_CLASS_CODE)) {
            confCode = AssemblyConstants.C32_CONFIDENTIAL_CODE;
            confCodeDesc = AssemblyConstants.C32_CONFIDENTIAL_CODE_DESCR;
            confCodeOID = CDAConstants.CONFIDENTIAL_CODE_SYS_OID;
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_CLASS_CODE) || (docType.getTypeId()).equals(AssemblyConstants.C62_RR_CLASS_CODE)) {
            confCode = AssemblyConstants.C62_CONFIDENTIAL_CODE;
            confCodeDesc = AssemblyConstants.C62_CONFIDENTIAL_CODE_DESCR;
            confCodeOID = CDAConstants.CONFIDENTIAL_CODE_SYS_OID;
        }

        addClassification(extrinsic,
            rimId, //classifiedObject
            UUID_PREFIX + "f4f85eac-e6cb-4883-b524-f2705394840f", //scheme
            confCode, //node representation
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            confCodeDesc, //name
            new String[]{"codingScheme",}, //slot names
            new String[][]{
                new String[]{(confCodeOID)},} //slot values
            );

        // Create CDAR/IHE classification - Formet code (We need to validate this item)
        String formatCodeDesc = null;
        String formatCodeOID = null;
        String formatCode = null;

        if ((docType.getTypeId()).equals(AssemblyConstants.C32_CLASS_CODE)) {
            formatCode = AssemblyConstants.C32_FORMAT_CODE;
            formatCodeDesc = AssemblyConstants.C32_FORMAT_CODE_DESCR;
            formatCodeOID = CDAConstants.FORMAT_CODE_OID;
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_CLASS_CODE) || (docType.getTypeId()).equals(AssemblyConstants.C62_RR_CLASS_CODE)) {
            formatCode = AssemblyConstants.C62_FORMAT_CODE;
            formatCodeDesc = AssemblyConstants.C62_FORMAT_CODE_DESCR;
            formatCodeOID = CDAConstants.FORMAT_CODE_OID;
        }
        addClassification(extrinsic,
            rimId, //classifiedObject
            UUID_PREFIX + "a09d5840-386c-46f2-b5ad-9c3699a4309d", //scheme
            formatCode, //node representation
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            formatCodeDesc, //name
            new String[]{"codingScheme",}, //slot names
            new String[][]{
                new String[]{formatCodeOID},} //slot values
            );

        // Create Healthcare Facility Type Code setting classification
        String hcftCodeDesc = null;
        String hcftCodeOID = null;
        String hcftCode = null;

        if ((docType.getTypeId()).equals(AssemblyConstants.C32_CLASS_CODE)) {
            hcftCode = AssemblyConstants.C32_HCFT_CODE;
            hcftCodeDesc = AssemblyConstants.C32_HCFT_CODE_DESCR;
            hcftCodeOID = CDAConstants.HCFT_CODE_SYS_OID;
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_CLASS_CODE)) {
            //check docuemnt title for ER discharge summary or Discharge Summary
            if (docTitle.contains("Emergency Report")) {
                //ER Discharge Summary
                hcftCode = AssemblyConstants.C62_HCFT_CODE;
                hcftCodeDesc = AssemblyConstants.C62_HCFT_CODE_DESCR;
                hcftCodeOID = CDAConstants.HCFT_CODE_SYS_OID;
            } else if (docTitle.contains("Discharge Summary")) {
                //Discharge Summary
                hcftCode = AssemblyConstants.C62_DS_HCFT_CODE; // This is not correct - AssemblyConstants.C62_HCFT_CODE;
                hcftCodeDesc = AssemblyConstants.C62_DS_HCFT_CODE_DESCR;
                hcftCodeOID = CDAConstants.HCFT_CODE_SYS_OID;
            }

        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_RR_CLASS_CODE)) {
            //Radiology Reports
            hcftCode = AssemblyConstants.C62_RR_HCFT_CODE; // This is not correct - AssemblyConstants.C62_HCFT_CODE;
            hcftCodeDesc = AssemblyConstants.C62_RR_HCFT_CODE_DESCR;
            hcftCodeOID = CDAConstants.HCFT_CODE_SYS_OID;
        }

        addClassification(extrinsic,
            rimId, //classifiedObject
            UUID_PREFIX + "f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1", //scheme
            hcftCode, //hcftCode, //node representation
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            hcftCodeDesc, //name
            new String[]{"codingScheme",}, //slot names
            new String[][]{
                new String[]{hcftCodeOID},} //slot values
            );

        // Create practise Setting code
        String practiceCodeDesc = null;
        String practiceCodeOID = null;
        String practiceCode = null;

        if ((docType.getTypeId()).equals(AssemblyConstants.C32_CLASS_CODE)) {
            practiceCode = AssemblyConstants.C32_PRACTICE_SETTING_CODE;
            practiceCodeDesc = AssemblyConstants.C32_PRACTICE_SETTING_CODE_DESCR;
            practiceCodeOID = CDAConstants.PRACTICE_SETTING_CODE_SYS_OID;
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_CLASS_CODE)) {

            if (docTitle.contains("Emergency Report")) {
                //ER Discharge Summary
                practiceCode = AssemblyConstants.C62_PRACTICE_SETTING_CODE;
                practiceCodeDesc = AssemblyConstants.C62_PRACTICE_SETTING_CODE_DESCR;
                practiceCodeOID = CDAConstants.PRACTICE_SETTING_CODE_SYS_OID;
            } else if (docTitle.contains("Discharge Summary")) {
                //Discharge Summary
                practiceCode = AssemblyConstants.C62_DS_PRACTICE_SETTING_CODE;
                practiceCodeDesc = AssemblyConstants.C62_DS_PRACTICE_SETTING_CODE_DESCR;
                practiceCodeOID = CDAConstants.PRACTICE_SETTING_CODE_SYS_OID;
            }
        } else if ((docType.getTypeId()).equals(AssemblyConstants.C62_RR_CLASS_CODE)) {
            //Radiology Reports
            practiceCode = AssemblyConstants.C62_RR_PRACTICE_SETTING_CODE;
            practiceCodeDesc = AssemblyConstants.C62_RR_PRACTICE_SETTING_CODE_DESCR;
            practiceCodeOID = CDAConstants.PRACTICE_SETTING_CODE_SYS_OID;
        }

        addClassification(extrinsic,
            rimId, //classifiedObject
            UUID_PREFIX + "cccf5598-8b07-4b77-a05e-ae952c785ead", //scheme
            practiceCode,//practiceCode, //node representation for Military Medicine in SNOMED-CT
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            practiceCodeDesc, //name
            new String[]{"codingScheme",}, //slot names
            new String[][]{
                new String[]{(practiceCodeOID)},} //slot values
            );

        // Add patientId identifier
        addExternalIdentifier(extrinsic,
            rimId, //registryObject
            UUID_PREFIX + "58a6f841-87b3-4a3e-92fd-a8ffeff98427", //identificationScheme
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            "XDSDocumentEntry.patientId", //name
            isoFormatPatientId //value
            );

        // Add uniqueId identifier
        addExternalIdentifier(extrinsic,
            rimId, //registryObject
            UUID_PREFIX + "2e82c1f6-a085-4c72-9da3-8640a32e42ab", //identificationScheme
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            "XDSDocumentEntry.uniqueId", //name
            uniqueId //value
            );

        return extrinsic;
    }

    /**
     * Build classification.
     */
    public ClassificationType createClassification() {
        ClassificationType classification = new ClassificationType();
        classification.setClassificationNode(
            UUID_PREFIX + "a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
        classification.setObjectType(
            "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setClassifiedObject("SubmissionSet01");
        classification.setId("ID_25276323_3");

        return classification;
    }

    //Build association
    public AssociationType1 createAssociation(String documentId) {
        AssociationType1 association = new AssociationType1();
        association.setAssociationType(
            "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember");
        association.setObjectType(
            "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
        association.setId("ID_25276323_1");
        association.setSourceObject("SubmissionSet01");
        association.setTargetObject(documentId);

        //Add submission status to assocation
        SlotType1 slot = new SlotType1();
        ValueListType valList = new ValueListType();
        slot.setName("SubmissionSetStatus");
        valList.getValue().add("Original");
        slot.setValueList(valList);
        association.getSlot().add(slot);

        return association;
    }

    //  Create submission set
    public RegistryPackageType createRegistryPackage(String isoFormatPatientId, DocumentType documentType, String documentTitle) {

        //display name is dynamic between ER Discharge Summaries and Discharge Summaries
        String docDisplayName = "";
        if (documentTitle.contains("Emergency Report")) {
            //ER Discharge Summary
            docDisplayName = AssemblyConstants.C62_DISPLAY_NAME;
        } else if (documentTitle.contains("Discharge Summary")) {
            //Discharge Summary
            docDisplayName = AssemblyConstants.C62_DS_DISPLAY_NAME;
        }

        RegistryPackageType registryPackage = new RegistryPackageType();

        registryPackage.setId("SubmissionSet01");
        registryPackage.setObjectType(
            "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

        // Submission time
        SlotType1 slot = new SlotType1();
        ValueListType valList = new ValueListType();
        slot.setName("submissionTime");
        valList.getValue().add(DateUtil.convertToCDATime(new Date()));  //use the current time
        slot.setValueList(valList);
        registryPackage.getSlot().add(slot);

        // Submission name
        LocalizedStringType localString = new LocalizedStringType();
        InternationalStringType intlString = new InternationalStringType();

        try {
            // Submission name
            localString.setValue(docDisplayName);
            intlString.getLocalizedString().add(localString);
            registryPackage.setName(intlString);
        } catch (Exception ex) {
            log.error("Failed setting Submission Name.", ex);
        }

        // Submission description
        localString = new LocalizedStringType();
        localString.setValue("Annual physical");
        intlString = new InternationalStringType();
        intlString.getLocalizedString().add(localString);
        registryPackage.setDescription(intlString);

        // Add submission uniqueId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            UUID_PREFIX + "96fdda7c-d067-4183-912e-bf5ee74998a8", //identificationScheme
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            "XDSSubmissionSet.uniqueId", //name
            "192.168.25.94.113" //value
            );

        //TODO: Fix this up for a submission source - I suspect this need to be a configuration parameter
        //Add submission sourceId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            UUID_PREFIX + "554ac39e-e3fe-47fe-b233-965d2a147832", //identificationScheme
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            "XDSSubmissionSet.sourceId", //name
            "129.6.58.92.1.1" //value
            );

        // Add submission patientId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            UUID_PREFIX + "6b5aea1a-874d-4603-a4bc-96a0a7b38446", //identificationScheme
            UUID_PREFIX + UUIDGenerator.generateRandomUUID(), //id
            "XDSSubmissionSet.patientId", //name
            isoFormatPatientId //value
            );

        return registryPackage;
    }

    // build PID5 segment, i.e. PID-5|" + "Doe^John^^^
    private String getPID5(List<PNExplicit> names) {
        StringBuffer pid5 = new StringBuffer();
        pid5.append("PID-5|");

        if (names.size() > 0) {
            PNExplicit name = names.get(0);
            String lastName = "";
            String firstName = "";
            String suffix = "";

            for (Object item : name.getContent()) {
                if (item instanceof EnFamily) {
                    EnFamily ob = (EnFamily) item;
                    lastName = ob.getRepresentation().value();
                } else if (item instanceof EnGiven) {
                    EnGiven ob = (EnGiven) item;
                    firstName = ob.getRepresentation().value();
                } else if (item instanceof EnSuffix) {
                    EnSuffix ob = (EnSuffix) item;
                    suffix = ob.getRepresentation().value();
                }
            }

            pid5.append(lastName + "^");
            pid5.append(firstName + "^");
            pid5.append(suffix + "^^");
        }

        return pid5.toString();
    }

    // build PID-11 segment, i.e. PID-11|100 MainSt^^Riverton^Ut^84065^USA
    private String getPID11(List<ADExplicit> addresses) {
        StringBuffer pid11 = new StringBuffer();
        pid11.append("PID-11|");

        if (addresses.size() > 0) {
            ADExplicit address = addresses.get(0);
            String streetname = "";
            String city = "";
            String state = "";
            String postalCode = "";
            String country = "";

            for (Object item : address.getContent()) {
                if (item instanceof AdxpStreetAddressLine) {
                    AdxpStreetAddressLine ob = (AdxpStreetAddressLine) item;
                    streetname = ob.getRepresentation().value();
                } else if (item instanceof AdxpCity) {
                    AdxpCity ob = (AdxpCity) item;
                    city = ob.getRepresentation().value();
                } else if (item instanceof AdxpState) {
                    AdxpState ob = (AdxpState) item;
                    state = ob.getRepresentation().value();
                } else if (item instanceof AdxpPostalCode) {
                    AdxpPostalCode ob = (AdxpPostalCode) item;
                    postalCode = ob.getRepresentation().value();
                } else if (item instanceof AdxpCountry) {
                    AdxpCountry ob = (AdxpCountry) item;
                    country = ob.getRepresentation().value();
                }
            }

            //PID-11|100 MainSt^^Riverton^Ut^84065^USA
            pid11.append(streetname + "^^");
            pid11.append(city + "^");
            pid11.append(state + "^");
            pid11.append(postalCode + "^");
            pid11.append(country + "^");
        }

        return pid11.toString();
    }

    private void addClassification(
        RegistryObjectType registry,
        String classifiedObject,
        String classificationScheme,
        String nodeRepresentation,
        String id,
        String name,
        String[] slotNames,
        String[][] slotValues) {

        //Create classification
        ClassificationType classification = new ClassificationType();
        classification.setClassificationScheme(classificationScheme);
        classification.setNodeRepresentation(nodeRepresentation);
        classification.setClassifiedObject(classifiedObject);
        classification.setObjectType(
            "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setId(id);

        // Classification name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            classification.setName(intlName);
        }

        // Slots
        for (int i = 0; i < slotNames.length; i++) {
            String slotName = slotNames[i];
            SlotType1 slot = new SlotType1();
            ValueListType valList = new ValueListType();
            for (String slotValue : slotValues[i]) {
                valList.getValue().add(slotValue);
            }

            slot.setName(slotName);
            slot.setValueList(valList);
            classification.getSlot().add(slot);
        }

        // Add classification
        registry.getClassification().add(classification);
    }

    private void addExternalIdentifier(
        RegistryObjectType registry,
        String registryObject,
        String identificationScheme,
        String id,
        String name,
        String value) {

        ExternalIdentifierType externalId = new ExternalIdentifierType();
        externalId.setObjectType(
            "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
        externalId.setRegistryObject(registryObject);
        externalId.setIdentificationScheme(identificationScheme);
        externalId.setId(id);
        externalId.setValue(value);

        // Identifier name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            externalId.setName(intlName);
        }

        // Add classification
        registry.getExternalIdentifier().add(externalId);
    }

    public void addAdditionalMetadata(ExtrinsicObjectType metadata, String clinicalHash, Date startTime, Date stopTime) {
        SlotType1 s1ClinicalHash = findSlot("urn:gov:hhs:fha:nhinc:xds:clinicalUniqueHash", metadata.getSlot());
        SlotType1 s1StartTime = findSlot("serviceStartTime", metadata.getSlot());
        SlotType1 s1StopTime = findSlot("serviceStopTime", metadata.getSlot());


        if (s1ClinicalHash != null) {
            s1ClinicalHash.getValueList().getValue().add(clinicalHash);
        }

        if (s1StartTime != null) {
            s1StartTime.getValueList().getValue().add(DateUtil.convertToCDATime(startTime));
        }

        if (s1StopTime != null) {
            s1StopTime.getValueList().getValue().add(DateUtil.convertToCDATime(stopTime));
        }
    }

    private static SlotType1 findSlot(String slotName, List<SlotType1> slots) {
        SlotType1 out = null;
        ListIterator<SlotType1> itr = slots.listIterator();

        while (itr.hasNext()) {
            SlotType1 slot = itr.next();

            if (slotName.compareTo(slot.getName()) == 0) {
                out = slot;

                break;
            }
        }
        return out;
    }
}
