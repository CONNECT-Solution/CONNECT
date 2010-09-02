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

package gov.hhs.fha.nhinc.documentassembly.ebxml;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.AssemblyManager;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DocumentType;
import gov.hhs.fha.nhinc.assemblymanager.utils.DateUtil;
import gov.hhs.fha.nhinc.common.docmgr.DocumentManagerClient;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBElement;
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
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitCountry;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.AdxpCity;
import org.hl7.v3.AdxpCountry;
import org.hl7.v3.AdxpPostalCode;
import org.hl7.v3.AdxpState;
import org.hl7.v3.AdxpStreetAddressLine;
import org.hl7.v3.ADXPExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EnSuffix;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.TSExplicit;

/**
 *
 * @author kim
 */
public class EBXMLRequestBuilder {

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
           DocumentType docType, Date serviceStartTimeFrom, Date serviceStopTimeTo) {

      String rimId = "urn:uuid:" + documentId;

      ExtrinsicObjectType extrinsic = new ExtrinsicObjectType();

      extrinsic.setId(rimId);
      extrinsic.setMimeType("text/xml");    //KIM: Based on Documentform
      extrinsic.setObjectType("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1");    //TODO: Verify that this is invariant

      // set home community id
      String sHomeCommunityId = null;

      try {
         sHomeCommunityId = PropertyAccessor.getProperty(
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
      valList.getValue().add(DateUtil.convertToCDATime(new Date()));
      slot.setValueList(valList);
      extrinsic.getSlot().add(slot);

      // language code
      slot = new SlotType1();
      valList = new ValueListType();
      slot.setName("languageCode");
      valList.getValue().add(AssemblyManager.getProperty(AssemblyConstants.DEF_LANGUAGE));
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
      valList.getValue().add(isoFormatPatientId);   //Should be the same as the query conplex Patient Id (HL7 Form)
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

      // clinical unique hash, without date
      slot = new SlotType1();
      valList = new ValueListType();
      slot.setName("urn:gov:hhs:fha:nhinc:xds:clinicalUniqueHash");
      valList.getValue().add(clinicalUniqueHash);
      slot.setValueList(valList);
      extrinsic.getSlot().add(slot);

      // hash, with date + document id
      slot = new SlotType1();
      valList = new ValueListType();
      slot.setName("hash");
      valList.getValue().add(docUniqueHash);
      slot.setValueList(valList);
      extrinsic.getSlot().add(slot);

      // set this it marks it as a Dynamic document
      slot = new SlotType1();
      valList = new ValueListType();
      slot.setName("urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed");
      valList.getValue().add("false");
      slot.setValueList(valList);
      extrinsic.getSlot().add(slot);

      // Document name (verify?)
      LocalizedStringType localString = new LocalizedStringType();
      localString.setValue(docType.getDisplayName());
      InternationalStringType intlString = new InternationalStringType();
      intlString.getLocalizedString().add(localString);
      extrinsic.setName(intlString);

      // create Author classification
      addClassification(
              extrinsic,
              rimId, //classifiedObject
              "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d", //scheme
              "", //node representation
              "id_1", //id
              null, //name
              new String[]{
                 "authorPerson",
                 "authorInstitution",
                 "authorRole",
                 "authorSpecialty",}, //slot names
              new String[][]{
                 new String[]{AssemblyManager.getProperty(AssemblyConstants.ORGANIZATION_SYSTEM)},
                 new String[]{AssemblyManager.getProperty(AssemblyConstants.ORGANIZATION_NAME)},
                 new String[]{""},
                 new String[]{""},} //slot values
              );

      // Create classCode classification/Type
      addClassification(extrinsic,
              rimId, //classifiedObject
              "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a", // - Indictate classCode
              docType.getTypeId(), //node representation - Document Type from input (34133-9 for C32)
              "id_3", //id
              docType.getDisplayName(), //name
              new String[]{"codingScheme",}, //slot names
              new String[][]{
                 new String[]{"2.16.11840.1.113883.6.1"},} //slot values
              );

      // Create outpatient evaluation and management classification
      addClassification(extrinsic,
              rimId, //classifiedObject
              "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983", //scheme
              docType.getTypeId(), //node representation
              "id_8", //id
              docType.getDisplayName(), //name
              new String[]{"codingScheme",}, //slot names
              new String[][]{
                 new String[]{"2.16.11840.1.113883.6.1"},} //slot values
              );

      // Handle the Confidentiality Cpode
      addClassification(extrinsic,
              rimId, //classifiedObject
              "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f", //scheme
              "N", //node representation
              "id_4", //id
              AssemblyManager.getProperty(AssemblyConstants.DEF_CONFIDENTIAL_CODE_DESCR), //name
              new String[]{"codingScheme",}, //slot names
              new String[][]{
                 new String[]{AssemblyManager.getProperty(AssemblyConstants.DEF_CONFIDENTIAL_CODE_SYS_OID)},} //slot values
              );

      // Create CDAR/IHE classification - Formet code (We need to validate this item)
      addClassification(extrinsic,
              rimId, //classifiedObject
              "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d", //scheme
              "2.16.840.1.1.113883.10.20.1", //node representation
              "id_5", //id
              "CDAR2/IHE 1.0 - HL7 CCD Document", //name
              new String[]{"codingScheme",}, //slot names
              new String[][]{
                 new String[]{"ISO"},} //slot values
              );

      // Create Healthcare Facility Type Code setting classification
      addClassification(extrinsic,
              rimId, //classifiedObject
              "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1", //scheme
              AssemblyManager.getProperty(AssemblyConstants.HCFT_CODE), //node representation
              "id_6", //id
              AssemblyManager.getProperty(AssemblyConstants.HCFT_CODE_DESCR), //name
              new String[]{"codingScheme",}, //slot names
              new String[][]{
                 new String[]{AssemblyManager.getProperty(AssemblyConstants.HCFT_CODE_SYS_OID)},} //slot values
              );

      // Create practive Setting code
      addClassification(extrinsic,
              rimId, //classifiedObject
              "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead", //scheme
              AssemblyManager.getProperty(AssemblyConstants.PRACTICE_SETTING_CODE), //node representation for Military Medicine in SNOMED-CT
              "id_7", //id
              AssemblyManager.getProperty(AssemblyConstants.PRACTICE_SETTING_CODE_DESCR), //name
              new String[]{"codingScheme",}, //slot names
              new String[][]{
                 new String[]{AssemblyManager.getProperty(AssemblyConstants.PRACTICE_SETTING_CODE_SYS_OID)},} //slot values
              );

      // Add patientId identifier
      addExternalIdentifier(extrinsic,
              rimId, //registryObject
              "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427", //identificationScheme
              "id_9", //id
              "XDSDocumentEntry.patientId", //name
              isoFormatPatientId //value
              );

      // Add uniqueId identifier
      DocumentManagerClient client = new DocumentManagerClient();
      String uniqueId = client.generateUniqueId(AssemblyManager.getProperty("das.docmgrservice.endpoint"));
      addExternalIdentifier(extrinsic,
              rimId, //registryObject
              //"urn:uuid:61ae39c2-4611-4644a6ac-c751e24a10bf", //identificationScheme
              "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab", //identificationScheme
              "id_10", //id
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
              "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
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
   public RegistryPackageType createRegistryPackage(String isoFormatPatientId, DocumentType documentType) {

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
         localString.setValue(documentType.getDisplayName());
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
              "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8", //identificationScheme
              "id_22", //id
              "XDSSubmissionSet.uniqueId", //name
              "192.168.25.94.113" //value
              );

      //TODO: Fix this up for a submission source - I suspect this need to be a configuration parameter
      //Add submission sourceId identifier
      addExternalIdentifier(registryPackage,
              "SubmissionSet01", //registryObject
              "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832", //identificationScheme
              "id_23", //id
              "XDSSubmissionSet.sourceId", //name
              "129.6.58.92.1.1" //value
              );

      // Add submission patientId identifier
      addExternalIdentifier(registryPackage,
              "SubmissionSet01", //registryObject
              "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446", //identificationScheme
              "id_24", //id
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
/*         for (int i = 0; i < address.getContent().size(); i++) {
            JAXBElement o = (JAXBElement) address.getContent().get(i);
            if (o != null && o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitStreetAddressLine")) {
               AdxpExplicitStreetAddressLine ob = (AdxpExplicitStreetAddressLine) o.getValue();
               streetname = ob.getContent();
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitCity")) {
               AdxpExplicitCity ob = (AdxpExplicitCity) o.getValue();
               city = ob.getContent();
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitState")) {
               AdxpExplicitState ob = (AdxpExplicitState) o.getValue();
               state = ob.getContent();
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitPostalCode")) {
               AdxpExplicitPostalCode ob = (AdxpExplicitPostalCode) o.getValue();
               postalCode = ob.getContent();
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitCountry")) {
               AdxpExplicitCountry ob = (AdxpExplicitCountry) o.getValue();
               country = ob.getContent();
            }
         }
 * */
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
}
