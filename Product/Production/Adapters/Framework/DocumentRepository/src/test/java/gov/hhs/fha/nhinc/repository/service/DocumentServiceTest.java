package gov.hhs.fha.nhinc.repository.service;

import gov.hhs.fha.nhinc.repository.model.Document;
import gov.hhs.fha.nhinc.repository.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.repository.model.EventCode;
import gov.hhs.fha.nhinc.repository.model.ExtraSlot;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for the DocumentService class
 * 
 * @author Neil Webb
 */
public class DocumentServiceTest
{
    /**
     * Test of DocumentService operations.
     */
    @Test
    public void testDocumentOperations()
    {
        System.out.println("Start testDocumentOperations");
        try
        {
            DocumentService docService = new DocumentService();
            Document document = null;

            // Insert document - minimal
            document = new Document();
            Long documentId = null;
            String documentUniqueId = "1234abcd";

            document.setDocumentid(documentId);
            document.setDocumentUniqueId(documentUniqueId);
            document.setRepositoryId("1");

            docService.saveDocument(document);
            documentId = document.getDocumentid();

            // Retrieve initial insert
            Document retrieved = docService.getDocument(documentId);
            assertNotNull("Retrieved doc - minmal was null", retrieved);
            assertNotNull("After insert - Document id null", documentId);
            assertEquals("After insert - Document unique id", documentUniqueId, retrieved.getDocumentUniqueId());
            assertFalse("After insert - Persistent flag", retrieved.isPersistent());

            // Update
            documentUniqueId = "1234abcdupdated";
            String documentTitle = "Doc title";
            String authorPerson = "authorPerson";
            String authorInstitution = "authorInstitution";
            String authorRole = "authorRole";
            String authorSpecialty = "authorSpecialty";
            String availablityStatus = "availablityStatus";
            String classCode = "classCode";
            String classCodeScheme = "classCodeScheme";
            String classCodeDisplayName = "classCodeDisplayName";
            String confidentialityCode = "confidentialityCode";
            String confidentialityCodeScheme = "confidentialityCodeScheme";
            String confidentialityCodeDisplayName = "confidentialityCodeDisplayName";
            Date creationTime = new SimpleDateFormat("yyyyMMddhhmmss").parse("20040404040404");
            String formatCode = "formatCode";
            String formatCodeScheme = "formatCodeScheme";
            String formatCodeDisplayName = "formatCodeDisplayName";
            String patientId = "patientId";
            Date serviceStartTime = new SimpleDateFormat("yyyyMMddhhmmss").parse("20050505050505");
            Date serviceStopTime = new SimpleDateFormat("yyyyMMddhhmmss").parse("20060606060606");
            String status = "status";
            String comments = "comments";
            String hash = null; //"hash";
            String facilityCode = "facilityCode";
            String facilityCodeScheme = "facilityCodeScheme";
            String facilityCodeDisplayName = "facilityCodeDisplayName";
            String intendedRecipientPerson = "intendedRecipientPerson";
            String intendedRecipientOrganization = "intendedRecipientOrganization";
            String languageCode = "languageCode";
            String legalAuthenticator = "legalAuthenticator";
            String mimeType = "mimeType";
            String parentDocumentId = "parentDocumentId";
            String parentDocumentRelationship = "parentDocumentRelationship";
            String practiceSetting = "practiceSetting";
            String practiceSettingScheme = "practiceSettingScheme";
            String practiceSettingDisplayName = "practiceSettingDisplayName";
            Integer size = new Integer(8989);
            String sourcePatientId = "sourcePatientId";
            String pid3 = "pid3";
            String pid5 = "pid5";
            String pid8 = "pid8";
            String pid7 = "pid7";
            String pid11 = "pid11";
            String typeCode = "typeCode";
            String typeCodeScheme = "typeCodeScheme";
            String typeCodeDisplayName = "typeCodeDisplayName";
            String documentUri = "documentUri";
//            String rawData = "rawData";
            String rawData = sampleC32();
            String sCompareHash = SHA1HashCode.calculateSHA1(rawData);  // Calculate the hash code for comparison later...

            retrieved.setDocumentUniqueId(documentUniqueId);
            retrieved.setDocumentTitle(documentTitle);
            retrieved.setAuthorPerson(authorPerson);
            retrieved.setAuthorInstitution(authorInstitution);
            retrieved.setAuthorRole(authorRole);
            retrieved.setAuthorSpecialty(authorSpecialty);
            retrieved.setAvailablityStatus(availablityStatus);
            retrieved.setClassCode(classCode);
            retrieved.setClassCodeScheme(classCodeScheme);
            retrieved.setClassCodeDisplayName(classCodeDisplayName);
            retrieved.setConfidentialityCode(confidentialityCode);
            retrieved.setConfidentialityCodeScheme(confidentialityCodeScheme);
            retrieved.setConfidentialityCodeDisplayName(confidentialityCodeDisplayName);
            retrieved.setCreationTime(creationTime);
            retrieved.setFormatCode(formatCode);
            retrieved.setFormatCodeScheme(formatCodeScheme);
            retrieved.setFormatCodeDisplayName(formatCodeDisplayName);
            retrieved.setPatientId(patientId);
            retrieved.setServiceStartTime(serviceStartTime);
            retrieved.setServiceStopTime(serviceStopTime);
            retrieved.setStatus(status);
            retrieved.setComments(comments);
            retrieved.setHash(hash);
            retrieved.setFacilityCode(facilityCode);
            retrieved.setFacilityCodeScheme(facilityCodeScheme);
            retrieved.setFacilityCodeDisplayName(facilityCodeDisplayName);
            retrieved.setIntendedRecipientPerson(intendedRecipientPerson);
            retrieved.setIntendedRecipientOrganization(intendedRecipientOrganization);
            retrieved.setLanguageCode(languageCode);
            retrieved.setLegalAuthenticator(legalAuthenticator);
            retrieved.setMimeType(mimeType);
            retrieved.setParentDocumentId(parentDocumentId);
            retrieved.setParentDocumentRelationship(parentDocumentRelationship);
            retrieved.setPracticeSetting(practiceSetting);
            retrieved.setPracticeSettingScheme(practiceSettingScheme);
            retrieved.setPracticeSettingDisplayName(practiceSettingDisplayName);
            retrieved.setSize(size);
            retrieved.setSourcePatientId(sourcePatientId);
            retrieved.setPid3(pid3);
            retrieved.setPid5(pid5);
            retrieved.setPid8(pid8);
            retrieved.setPid7(pid7);
            retrieved.setPid11(pid11);
            retrieved.setTypeCode(typeCode);
            retrieved.setTypeCodeScheme(typeCodeScheme);
            retrieved.setTypeCodeDisplayName(typeCodeDisplayName);
            retrieved.setDocumentUri(documentUri);
            retrieved.setRawData(rawData.getBytes());
            retrieved.setPersistent(true);

            Set<EventCode> eventCodes = new HashSet<EventCode>();
            EventCode code1 = new EventCode();
            String eventCode1 = "eventCode1";
            String eventCodeScheme1 = "eventCodeScheme1";
            String eventCodeDisplayName1 = "eventCodeDisplayName1";
            code1.setEventCode(eventCode1);
            code1.setEventCodeScheme(eventCodeScheme1);
            code1.setEventCodeDisplayName(eventCodeDisplayName1);
            code1.setDocument(retrieved);
            eventCodes.add(code1);
            EventCode code2 = new EventCode();
            String eventCode2 = "eventCode2";
            String eventCodeScheme2 = "eventCodeScheme2";
            String eventCodeDisplayName2 = "eventCodeDisplayName2";
            code2.setEventCode(eventCode2);
            code2.setEventCodeScheme(eventCodeScheme2);
            code2.setEventCodeDisplayName(eventCodeDisplayName2);
            code2.setDocument(retrieved);
            eventCodes.add(code2);
            retrieved.setEventCodes(eventCodes);

            Set<ExtraSlot> extraSlots = new HashSet<ExtraSlot>();
            ExtraSlot slot1 = new ExtraSlot();
            String extraSlot1Name = "urn:extra1";
            String extraSlot1Value = "value1";
            slot1.setExtraSlotName(extraSlot1Name);
            slot1.setExtraSlotValue(extraSlot1Value);
            slot1.setDocument(retrieved);
            extraSlots.add(slot1);
            ExtraSlot slot2 = new ExtraSlot();
            String extraSlot2Name = "urn:extra2";
            String extraSlot2Value = "value2";
            slot2.setExtraSlotName(extraSlot2Name);
            slot2.setExtraSlotValue(extraSlot2Value);
            slot2.setDocument(retrieved);
            extraSlots.add(slot2);
            retrieved.setExtraSlots(extraSlots);

            docService.saveDocument(retrieved);

            // Retrieve updated
            retrieved = docService.getDocument(documentId);
            assertNotNull("Retrieved doc - updated was null", retrieved);
            assertEquals("After update - Document id", documentId, retrieved.getDocumentid());
            assertEquals("After update - Document unique id", documentUniqueId, retrieved.getDocumentUniqueId());
            assertEquals("After update - authorInstitution", authorInstitution, retrieved.getAuthorInstitution());
            assertEquals("After update - authorRole", authorRole, retrieved.getAuthorRole());
            assertEquals("After update - authorSpecialty", authorSpecialty, retrieved.getAuthorSpecialty());
            assertEquals("After update - availablityStatus", availablityStatus, retrieved.getAvailablityStatus());
            assertEquals("After update - classCode", classCode, retrieved.getClassCode());
            assertEquals("After update - classCodeScheme", classCodeScheme, retrieved.getClassCodeScheme());
            assertEquals("After update - classCodeDisplayName", classCodeDisplayName, retrieved.getClassCodeDisplayName());
            assertEquals("After update - confidentialityCode", confidentialityCode, retrieved.getConfidentialityCode());
            assertEquals("After update - confidentialityCodeScheme", confidentialityCodeScheme, retrieved.getConfidentialityCodeScheme());
            assertEquals("After update - confidentialityCodeDisplayName", confidentialityCodeDisplayName, retrieved.getConfidentialityCodeDisplayName());
            assertEquals("After update - creationTime", creationTime, retrieved.getCreationTime());
            assertEquals("After update - formatCode", formatCode, retrieved.getFormatCode());
            assertEquals("After update - formatCodeScheme", formatCodeScheme, retrieved.getFormatCodeScheme());
            assertEquals("After update - formatCodeDisplayName", formatCodeDisplayName, retrieved.getFormatCodeDisplayName());
            assertEquals("After update - patientId", patientId, retrieved.getPatientId());
            assertEquals("After update - serviceStartTime", serviceStartTime, retrieved.getServiceStartTime());
            assertEquals("After update - serviceStopTime", serviceStopTime, retrieved.getServiceStopTime());
            assertEquals("After update - status", status, retrieved.getStatus());
            assertEquals("After update - comments", comments, retrieved.getComments());
            assertEquals("After update - hash", sCompareHash, retrieved.getHash());
            assertEquals("After update - facilityCode", facilityCode, retrieved.getFacilityCode());
            assertEquals("After update - facilityCodeScheme", facilityCodeScheme, retrieved.getFacilityCodeScheme());
            assertEquals("After update - facilityCodeDisplayName", facilityCodeDisplayName, retrieved.getFacilityCodeDisplayName());
            assertEquals("After update - intendedRecipientPerson", intendedRecipientPerson, retrieved.getIntendedRecipientPerson());
            assertEquals("After update - intendedRecipientOrganization", intendedRecipientOrganization, retrieved.getIntendedRecipientOrganization());
            assertEquals("After update - languageCode", languageCode, retrieved.getLanguageCode());
            assertEquals("After update - legalAuthenticator", legalAuthenticator, retrieved.getLegalAuthenticator());
            assertEquals("After update - mimeType", mimeType, retrieved.getMimeType());
            assertEquals("After update - parentDocumentId", parentDocumentId, retrieved.getParentDocumentId());
            assertEquals("After update - parentDocumentRelationship", parentDocumentRelationship, retrieved.getParentDocumentRelationship());
            assertEquals("After update - practiceSetting", practiceSetting, retrieved.getPracticeSetting());
            assertEquals("After update - practiceSettingScheme", practiceSettingScheme, retrieved.getPracticeSettingScheme());
            assertEquals("After update - practiceSettingDisplayName", practiceSettingDisplayName, retrieved.getPracticeSettingDisplayName());
            assertEquals("After update - size", size, retrieved.getSize());
            assertEquals("After update - sourcePatientId", sourcePatientId, retrieved.getSourcePatientId());
            assertEquals("After update - pid3", pid3, retrieved.getPid3());
            assertEquals("After update - pid5", pid5, retrieved.getPid5());
            assertEquals("After update - pid8", pid8, retrieved.getPid8());
            assertEquals("After update - pid7", pid7, retrieved.getPid7());
            assertEquals("After update - pid11", pid11, retrieved.getPid11());
            assertEquals("After update - typeCode", typeCode, retrieved.getTypeCode());
            assertEquals("After update - typeCodeScheme", typeCodeScheme, retrieved.getTypeCodeScheme());
            assertEquals("After update - typeCodeDisplayName", typeCodeDisplayName, retrieved.getTypeCodeDisplayName());
            assertEquals("After update - documentUri", documentUri, retrieved.getDocumentUri());
            assertEquals("After update - rawData", rawData, new String(retrieved.getRawData()));
            assertTrue("After update - persistent flag", retrieved.isPersistent());

            eventCodes = retrieved.getEventCodes();
            assertNotNull("After update - audit codes null", eventCodes);
            assertEquals("After update - audit codes size", 2, eventCodes.size());

//            Iterator<EventCode> iter = eventCodes.iterator();
//            while(iter.hasNext())
//            {
//                EventCode code = iter.next();
//                assertNotNull("After insert - contained event code was null", code);
//                assertNotNull("After insert - event code id was null", code.getEventCodeId());
//                assertEquals("After insert - eventCode1", eventCode1, code.getEventCode());
//                assertEquals("After insert - eventCodeScheme1", eventCodeScheme1, code.getEventCodeScheme());
//                assertEquals("After insert - eventCodeDisplayName1", eventCodeDisplayName1, code.getEventCodeDisplayName());
//            }

            extraSlots = retrieved.getExtraSlots();
            assertNotNull("After update - extra slots null", extraSlots);
            assertEquals("After update - extra slots size", 2, extraSlots.size());

            // Query by patient id only
            DocumentQueryParams queryParams = buildQueryParams(patientId, null, null, null, null, null, null, null, null, null, null);
            List<Document> queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by patient id results null", queryResults);
            assertFalse("Query by patient id results empty", queryResults.isEmpty());

            // Query by class code
            queryParams = buildQueryParams(patientId, classCode, null, null, null, null, null, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by class code results null", queryResults);
            assertFalse("Query by class code results empty", queryResults.isEmpty());

            // Query by class code scheme
            queryParams = buildQueryParams(patientId, null, classCodeScheme, null, null, null, null, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by class code scheme results null", queryResults);
            assertFalse("Query by class code scheme results empty", queryResults.isEmpty());

            // Query by creation date from
            Date creationTimeFrom = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss").parse("04/04/2004.04:04:03");
            queryParams = buildQueryParams(patientId, null, null, creationTimeFrom, null, null, null, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by creation date from null results null", queryResults);
            assertFalse("Query by creation date from results empty", queryResults.isEmpty());

            // Query by creation date to
            Date creationTimeTo = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss").parse("04/04/2004.04:04:05");
            queryParams = buildQueryParams(patientId, null, null, null, creationTimeTo, null, null, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by creation date to results null", queryResults);
            assertFalse("Query by creation date to results empty", queryResults.isEmpty());

            // Query by creation date from and to
            queryParams = buildQueryParams(patientId, null, null, creationTimeFrom, creationTimeTo, null, null, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by creation date from and to results null", queryResults);
            assertFalse("Query by creation date from and to results empty", queryResults.isEmpty());

            // Query by service start date from
            Date serviceStartTimeFrom = new SimpleDateFormat("yyyyMMddhhmmss").parse("20050505050504");
            queryParams = buildQueryParams(patientId, null, null, null, null, serviceStartTimeFrom, null, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by service start date from results null", queryResults);
            assertFalse("Query by service start date from results empty", queryResults.isEmpty());

            // Query by service start date to
            Date serviceStartTimeTo = new SimpleDateFormat("yyyyMMddhhmmss").parse("20050505050506");
            queryParams = buildQueryParams(patientId, null, null, null, null, null, serviceStartTimeTo, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by service start date to results null", queryResults);
            assertFalse("Query by service start date to results empty", queryResults.isEmpty());

            // Query by service start date from and to
            queryParams = buildQueryParams(patientId, null, null, null, null, serviceStartTimeFrom, serviceStartTimeTo, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by service start date from and to results null", queryResults);
            assertFalse("Query by service start date from and to results empty", queryResults.isEmpty());

            // Query by service stop time from
            Date serviceStopTimeFrom = new SimpleDateFormat("yyyyMMddhhmmss").parse("20060606060605");
            queryParams = buildQueryParams(patientId, null, null, null, null, null, null, serviceStopTimeFrom, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by service stop time from results null", queryResults);
            assertFalse("Query by service stop time from results empty", queryResults.isEmpty());

            // Query by service stop time to
            Date serviceStopTimeTo = new SimpleDateFormat("yyyyMMddhhmmss").parse("20060606060607");
            queryParams = buildQueryParams(patientId, null, null, null, null, null, null, null, serviceStopTimeTo, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by service stop time to results null", queryResults);
            assertFalse("Query by service stop time to results empty", queryResults.isEmpty());

            // Query by service stop time from and to
            queryParams = buildQueryParams(patientId, null, null, null, null, null, null, serviceStopTimeFrom, serviceStopTimeTo, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by service stop time from and to results null", queryResults);
            assertFalse("Query by service stop time from and to results empty", queryResults.isEmpty());

            // Query by status
            queryParams = buildQueryParams(patientId, null, null, null, null, null, null, null, null, status, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by status results null", queryResults);
            assertFalse("Query by status results empty", queryResults.isEmpty());

            // Query by document unique id
            queryParams = buildQueryParams(null, null, null, null, null, null, null, null, null, null, documentUniqueId);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by document unique id results null", queryResults);
            assertFalse("Query by document unique id results empty", queryResults.isEmpty());

            // Query by all parameters
            queryParams = buildQueryParams(patientId, classCode, classCodeScheme, creationTimeFrom, creationTimeTo, serviceStartTimeFrom, serviceStartTimeTo, serviceStopTimeFrom, serviceStopTimeTo, status, documentUniqueId);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by all parameters results null", queryResults);
            assertFalse("Query by all parameters results empty", queryResults.isEmpty());

            // Query with invalid patient id - Should return null or empty list - not exception
            queryParams = buildQueryParams("invalidpatientid", classCode, classCodeScheme, creationTimeFrom, creationTimeTo, serviceStartTimeFrom, serviceStartTimeTo, serviceStopTimeFrom, serviceStopTimeTo, status, documentUniqueId);
            queryResults = docService.documentQuery(queryParams);
            assertTrue("Query using invalid patient id not empty", ((queryResults == null) || (queryResults.isEmpty())));

            // Query by creation date from (inclusive)
            queryParams = buildQueryParams(patientId, null, null, creationTime, null, null, null, null, null, null, null);
            queryResults = docService.documentQuery(queryParams);
            assertNotNull("Query by creation date from (inclusive) results null", queryResults);
            assertFalse("Query by creation date from (inclusive) results empty", queryResults.isEmpty());

            // Delete
            docService.deleteDocument(retrieved);
            retrieved = docService.getDocument(documentId);
            assertNull("Document not null after delete", retrieved);

        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("testDocumentOperations: " + t.getMessage());
        }
        System.out.println("End testDocumentOperations");
    }

    private DocumentQueryParams buildQueryParams(String patientId, String classCode, String classCodeScheme, Date creationTimeFrom, Date creationTimeTo, Date serviceStartTimeFrom, Date serviceStartTimeTo, Date serviceStopTimeFrom, Date serviceStopTimeTo, String status, String documentUniqueId)
    {
        DocumentQueryParams params = new DocumentQueryParams();

        params.setPatientId(patientId);
        if(classCode != null)
        {
            List<String> classCodes = new ArrayList<String>();
            classCodes.add(classCode);
            params.setClassCodes(classCodes);
        }
        params.setClassCodeScheme(classCodeScheme);
        params.setCreationTimeFrom(creationTimeFrom);
        params.setCreationTimeTo(creationTimeTo);
        params.setServiceStartTimeFrom(serviceStartTimeFrom);
        params.setServiceStartTimeTo(serviceStartTimeTo);
        params.setServiceStopTimeFrom(serviceStopTimeFrom);
        params.setServiceStopTimeTo(serviceStopTimeTo);
        if(status != null)
        {
            List<String> statuses = new ArrayList<String>();
            statuses.add(status);
            params.setStatuses(statuses);
        }
        if (documentUniqueId != null)
        {
            List<String> documentUniqueIds = new ArrayList<String>();
            documentUniqueIds.add(documentUniqueId);
            params.setDocumentUniqueId(documentUniqueIds);
        }

        return params;
    }

    private String sampleC32()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("<ClinicalDocument xsi:schemaLocation=\"urn:hl7-org:v3 http://xreg2.nist.gov:8080/hitspValidation/schema/cdar2c32/infrastructure/cda/C32_CDA.xsd\" xmlns:sdtc=\"urn:hl7-org:sdtc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\">");
        sb.append("  <typeId extension=\"POCD_HD000040\" root=\"2.16.840.1.113883.1.3\"/>");
        sb.append("  <templateId assigningAuthorityName=\"CDA/R2\" root=\"2.16.840.1.113883.3.27.1776\"/>");
        sb.append("  <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1\"/>");
        sb.append("  <templateId assigningAuthorityName=\"HITSP/C32\" root=\"2.16.840.1.113883.3.88.11.32.1\"/>");
        sb.append("  <id extension=\"Laika C32 Test\" assigningAuthorityName=\"Laika: An Open Source EHR Testing Framework projectlaika.org\" root=\"2.16.840.1.113883.3.72\"/>");
        sb.append("  <code code=\"34133-9\" displayName=\"Summarization of patient data\" codeSystemName=\"LOINC\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("  <title>Katy Bradford</title>");
        sb.append("  <effectiveTime value=\"20080424103703-0500\"/>");
        sb.append("  <confidentialityCode/>");
        sb.append("  <languageCode code=\"en-US\"/>");
        sb.append("  <recordTarget>");
        sb.append("    <patientRole>");
        sb.append("      <id extension=\"000-00-0002\"/>");
        sb.append("      <addr>");
        sb.append("        <streetAddressLine>437 TURNER LANE</streetAddressLine>");
        sb.append("        <city>LEESBURG</city>");
        sb.append("        <state>VA</state>");
        sb.append("        <postalCode>20176</postalCode>");
        sb.append("        <country>US</country>");
        sb.append("      </addr>");
        sb.append("      <telecom value=\"tel:703-555-0000\" use=\"HP\"/>");
        sb.append("      <telecom value=\"tel:703-555-0001\" use=\"WP\"/>");
        sb.append("      <telecom value=\"tel:703-555-0002\" use=\"MC\"/>");
        sb.append("      <telecom value=\"mailto:katy.bradford@here.com\"/>");
        sb.append("      <telecom value=\"http://www.here.com/katy\"/>");
        sb.append("      <patient>");
        sb.append("        <name>");
        sb.append("          <prefix>MRS.</prefix>");
        sb.append("          <given qualifier=\"CL\">KATY</given>");
        sb.append("          <family qualifier=\"BR\">BRADFORD</family>");
        sb.append("        </name>");
        sb.append("        <administrativeGenderCode code=\"F\" displayName=\"Female\" codeSystemName=\"HL7 AdministrativeGenderCodes\" codeSystem=\"2.16.840.1.113883.5.1\">");
        sb.append("          <originalText>AdministrativeGender codes are: M (Male), F (Female) or UN (Undifferentiated).</originalText>");
        sb.append("        </administrativeGenderCode>");
        sb.append("        <birthTime value=\"19740730\"/>");
        sb.append("        <maritalStatusCode code=\"M\" displayName=\"Married\" codeSystemName=\"MaritalStatusCode\" codeSystem=\"2.16.840.1.113883.5.2\"/>");
        sb.append("        <religiousAffiliationCode code=\"1005\" displayName=\"Anglican\" codeSystemName=\"Religious Affiliation\" codeSystem=\"2.16.840.1.113883.5.1076\"/>");
        sb.append("        <raceCode code=\"2111-3\" displayName=\"French\" codeSystemName=\"CDC Race and Ethnicity\" codeSystem=\"2.16.840.1.113883.6.238\"/>");
        sb.append("        <ethnicGroupCode code=\"2142-8\" displayName=\"Belearic Islander\" codeSystemName=\"CDC Race and Ethnicity\" codeSystem=\"2.16.840.1.113883.6.238\"/>");
        sb.append("        <languageCommunication>");
        sb.append("          <templateId root=\"2.16.840.1.113883.3.88.11.32.2\"/>");
        sb.append("          <languageCode code=\"as-AZ\"/>");
        sb.append("          <modeCode code=\"RSGN\" displayName=\"Received Signed\" codeSystemName=\"LanguageAbilityMode\" codeSystem=\"2.16.840.1.113883.5.60\"/>");
        sb.append("          <preferenceInd value=\"true\"/>");
        sb.append("        </languageCommunication>");
        sb.append("        <languageCommunication>");
        sb.append("          <templateId root=\"2.16.840.1.113883.3.88.11.32.2\"/>");
        sb.append("          <languageCode code=\"cs-AU\"/>");
        sb.append("          <modeCode code=\"EWR\" displayName=\"Expressed Written\" codeSystemName=\"LanguageAbilityMode\" codeSystem=\"2.16.840.1.113883.5.60\"/>");
        sb.append("          <preferenceInd value=\"true\"/>");
        sb.append("        </languageCommunication>");
        sb.append("      </patient>");
        sb.append("    </patientRole>");
        sb.append("  </recordTarget>");
        sb.append("  <author>");
        sb.append("    <time value=\"20080401\"/>");
        sb.append("    <assignedAuthor>");
        sb.append("      <id root=\"8798704325\"/>");
        sb.append("      <assignedPerson>");
        sb.append("        <name>");
        sb.append("          <prefix>MR</prefix>");
        sb.append("          <given qualifier=\"CL\">BOB</given>");
        sb.append("          <family qualifier=\"BR\">DEVRIESE</family>");
        sb.append("          <prefix>ESQ</prefix>");
        sb.append("        </name>");
        sb.append("      </assignedPerson>");
        sb.append("      <representedOrganization>");
        sb.append("        <id root=\"2.16.840.1.113883.19.5\"/>");
        sb.append("        <name>AJAX INSURANCE</name>");
        sb.append("      </representedOrganization>");
        sb.append("    </assignedAuthor>");
        sb.append("  </author>");
        sb.append("  <custodian>");
        sb.append("    <assignedCustodian>");
        sb.append("      <representedCustodianOrganization>");
        sb.append("        <id/>");
        sb.append("      </representedCustodianOrganization>");
        sb.append("    </assignedCustodian>");
        sb.append("  </custodian>");
        sb.append("  <participant typeCode=\"IND\">");
        sb.append("    <templateId root=\"2.16.840.1.113883.3.88.11.32.3\"/>");
        sb.append("    <time>");
        sb.append("      <low value=\"20080401\"/>");
        sb.append("      <high value=\"20080430\"/>");
        sb.append("    </time>");
        sb.append("    <associatedEntity classCode=\"AGNT\">");
        sb.append("      <code code=\"FRND\" displayName=\"Unrelated Friend\" codeSystemName=\"RoleCode\" codeSystem=\"2.16.840.1.113883.5.111\"/>");
        sb.append("      <addr>");
        sb.append("        <streetAddressLine>439 TURNER LANE</streetAddressLine>");
        sb.append("        <city>LEESBURG</city>");
        sb.append("        <state>VA</state>");
        sb.append("        <postalCode>20176</postalCode>");
        sb.append("        <country>US</country>");
        sb.append("      </addr>");
        sb.append("      <telecom value=\"tel:703-555-0003\" use=\"HP\"/>");
        sb.append("      <telecom value=\"tel:703-555-0004\" use=\"WP\"/>");
        sb.append("      <telecom value=\"tel:703-555-0005\" use=\"MC\"/>");
        sb.append("      <telecom value=\"tel:703-555-0006\" use=\"HV\"/>");
        sb.append("      <telecom value=\"mailto:terry.shaw@here.com\"/>");
        sb.append("      <telecom value=\"http://www.here.com/terry\"/>");
        sb.append("      <associatedPerson>");
        sb.append("        <name>");
        sb.append("          <prefix>MS.</prefix>");
        sb.append("          <given qualifier=\"CL\">TERRY</given>");
        sb.append("          <family qualifier=\"BR\">SHAW</family>");
        sb.append("          <suffix>PHD</suffix>");
        sb.append("        </name>");
        sb.append("      </associatedPerson>");
        sb.append("    </associatedEntity>");
        sb.append("  </participant>");
        sb.append("  <documentationOf>");
        sb.append("    <serviceEvent classCode=\"PCPR\">");
        sb.append("      <effectiveTime>");
        sb.append("        <low value=\"0\"/>");
        sb.append("        <high value=\"2010\"/>");
        sb.append("      </effectiveTime>");
        sb.append("      <performer typeCode=\"PRF\">");
        sb.append("        <templateId assigningAuthorityName=\"HITSP/C32\" root=\"2.16.840.1.113883.3.88.11.32.4\"/>");
        sb.append("        <functionCode code=\"PP\" displayName=\"Primary Care Provider\" codeSystemName=\"Provider Role\" codeSystem=\"2.16.840.1.113883.12.443\">");
        sb.append("          <originalText>PCP</originalText>");
        sb.append("        </functionCode>");
        sb.append("        <time>");
        sb.append("          <low value=\"20080409\"/>");
        sb.append("          <high value=\"20080430\"/>");
        sb.append("        </time>");
        sb.append("        <assignedEntity>");
        sb.append("          <id/>");
        sb.append("          <code code=\"300000000X\" displayName=\"Managed Care Organizations\" codeSystemName=\"ProviderCodes\" codeSystem=\"2.16.840.1.113883.6.101\"/>");
        sb.append("          <addr>");
        sb.append("            <streetAddressLine>10300 MAIN ST.</streetAddressLine>");
        sb.append("            <streetAddressLine>SUITE 100</streetAddressLine>");
        sb.append("            <city>ASHBURN</city>");
        sb.append("            <state>VA</state>");
        sb.append("            <postalCode>20176</postalCode>");
        sb.append("            <country>AF</country>");
        sb.append("          </addr>");
        sb.append("          <telecom value=\"tel:703-555-0007\" use=\"HP\"/>");
        sb.append("          <telecom value=\"tel:703-555-0008\" use=\"WP\"/>");
        sb.append("          <telecom value=\"tel:703-555-0009\" use=\"MC\"/>");
        sb.append("          <telecom value=\"tel:703-555-0010\" use=\"HV\"/>");
        sb.append("          <telecom value=\"mailto:jw@doctors.com\"/>");
        sb.append("          <telecom value=\"http://www.doctors.com/jw\"/>");
        sb.append("          <assignedPerson>");
        sb.append("            <name>");
        sb.append("              <prefix>DR.</prefix>");
        sb.append("              <given qualifier=\"CL\">JANE</given>");
        sb.append("              <family qualifier=\"BR\">WATSON</family>");
        sb.append("              <suffix>MD</suffix>");
        sb.append("            </name>");
        sb.append("          </assignedPerson>");
        sb.append("          <representedOrganization>");
        sb.append("            <id assigningAuthorityName=\"Doctors Inc.\" root=\"2.16.840.1.113883.3.72.5\"/>");
        sb.append("            <name>Doctors Inc.</name>");
        sb.append("          </representedOrganization>");
        sb.append("          <patient xmlns=\"urn:hl7-org:sdtc\">");
        sb.append("            <id extension=\"MedicalRecordNumber\" root=\"x164353Y1343\" xmlns=\"urn:hl7-org:sdtc\"/>");
        sb.append("          </patient>");
        sb.append("        </assignedEntity>");
        sb.append("      </performer>");
        sb.append("    </serviceEvent>");
        sb.append("  </documentationOf>");
        sb.append("  <component>");
        sb.append("    <structuredBody>");
        sb.append("      <component>");
        sb.append("        <section>");
        sb.append("          <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.11\"/>");
        sb.append("          <code code=\"11450-4\" displayName=\"Problems\" codeSystemName=\"LOINC\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("          <title>Conditions or Problems</title>");
        sb.append("          <text>");
        sb.append("            <table border=\"1\" width=\"100%\">");
        sb.append("              <thead>");
        sb.append("                <tr>");
        sb.append("                  <th>Problem Name</th>");
        sb.append("                  <th>Problem Type</th>");
        sb.append("                  <th>Problem Date</th>");
        sb.append("                </tr>");
        sb.append("              </thead>");
        sb.append("              <tbody>");
        sb.append("                <tr>");
        sb.append("                  <td>");
        sb.append("                    <content ID=\"problem-1021520383\">Sprained ankle</content>");
        sb.append("                  </td>");
        sb.append("                  <td>Diagnosis</td>");
        sb.append("                  <td>20080401</td>");
        sb.append("                </tr>");
        sb.append("                <tr>");
        sb.append("                  <td>");
        sb.append("                    <content ID=\"problem-1021520384\">Migraines</content>");
        sb.append("                  </td>");
        sb.append("                  <td>Problem</td>");
        sb.append("                  <td>20080421</td>");
        sb.append("                </tr>");
        sb.append("              </tbody>");
        sb.append("            </table>");
        sb.append("          </text>");
        sb.append("          <entry>");
        sb.append("            <act classCode=\"ACT\" moodCode=\"EVN\">");
        sb.append("              <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.27\"/>");
        sb.append("              <templateId assigningAuthorityName=\"HITSP/C32\" root=\"2.16.840.1.113883.3.88.11.32.7\"/>");
        sb.append("              <id/>");
        sb.append("              <code nullFlavor=\"NA\"/>");
        sb.append("              <effectiveTime>");
        sb.append("                <low value=\"20080401\"/>");
        sb.append("                <high value=\"20080430\"/>");
        sb.append("              </effectiveTime>");
        sb.append("              <entryRelationship typeCode=\"SUBJ\">");
        sb.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("                  <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.28\"/>");
        sb.append("                  <code code=\"282291009\" displayName=\"Diagnosis\" codeSystemName=\"SNOMED CT\" codeSystem=\"2.16.840.1.113883.6.96\"/>");
        sb.append("                  <text>");
        sb.append("                    <reference value=\"#problem-1021520383\"/>");
        sb.append("                  </text>");
        sb.append("                  <statusCode code=\"completed\"/>");
        sb.append("                </observation>");
        sb.append("              </entryRelationship>");
        sb.append("            </act>");
        sb.append("          </entry>");
        sb.append("          <entry>");
        sb.append("            <act classCode=\"ACT\" moodCode=\"EVN\">");
        sb.append("              <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.27\"/>");
        sb.append("              <templateId assigningAuthorityName=\"HITSP/C32\" root=\"2.16.840.1.113883.3.88.11.32.7\"/>");
        sb.append("              <id/>");
        sb.append("              <code nullFlavor=\"NA\"/>");
        sb.append("              <effectiveTime>");
        sb.append("                <low value=\"20080421\"/>");
        sb.append("                <high value=\"20080425\"/>");
        sb.append("              </effectiveTime>");
        sb.append("              <entryRelationship typeCode=\"SUBJ\">");
        sb.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("                  <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.28\"/>");
        sb.append("                  <code code=\"55607006\" displayName=\"Problem\" codeSystemName=\"SNOMED CT\" codeSystem=\"2.16.840.1.113883.6.96\"/>");
        sb.append("                  <text>");
        sb.append("                    <reference value=\"#problem-1021520384\"/>");
        sb.append("                  </text>");
        sb.append("                  <statusCode code=\"completed\"/>");
        sb.append("                </observation>");
        sb.append("              </entryRelationship>");
        sb.append("            </act>");
        sb.append("          </entry>");
        sb.append("        </section>");
        sb.append("      </component>");
        sb.append("      <component>");
        sb.append("        <section>");
        sb.append("          <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.2\"/>");
        sb.append("          <code code=\"48765-2\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("          <title>Allergies, Adverse Reactions, Alerts</title>");
        sb.append("          <text>");
        sb.append("            <table border=\"1\" width=\"100%\">");
        sb.append("              <thead>");
        sb.append("                <tr>");
        sb.append("                  <th>Substance</th>");
        sb.append("                  <th>Event Type</th>");
        sb.append("                  <th>Severity</th>");
        sb.append("                </tr>");
        sb.append("              </thead>");
        sb.append("              <tbody>");
        sb.append("                <tr>");
        sb.append("                  <td>Trucare</td>");
        sb.append("                  <td>Drug Allergy</td>");
        sb.append("                  <td>");
        sb.append("                    <content ID=\"severity-823358778\">Mild</content>");
        sb.append("                  </td>");
        sb.append("                </tr>");
        sb.append("                <tr>");
        sb.append("                  <td>peanuts</td>");
        sb.append("                  <td>Food Allergy</td>");
        sb.append("                  <td>");
        sb.append("                    <content ID=\"severity-823358779\">Severe</content>");
        sb.append("                  </td>");
        sb.append("                </tr>");
        sb.append("              </tbody>");
        sb.append("            </table>");
        sb.append("          </text>");
        sb.append("          <entry>");
        sb.append("            <act classCode=\"ACT\" moodCode=\"EVN\">");
        sb.append("              <templateId root=\"2.16.840.1.113883.10.20.1.27\"/>");
        sb.append("              <templateId root=\"2.16.840.1.113883.3.88.11.32.6\"/>");
        sb.append("              <id root=\"2C748172-7CC2-4902-8AF0-23A105C4401B\"/>");
        sb.append("              <code nullFlavor=\"NA\"/>");
        sb.append("              <entryRelationship typeCode=\"SUBJ\">");
        sb.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("                  <templateId root=\"2.16.840.1.113883.10.20.1.18\"/>");
        sb.append("                  <code code=\"416098002\" displayName=\"Drug Allergy\" codeSystemName=\"SNOMED CT\" codeSystem=\"2.16.840.1.113883.6.96\"/>");
        sb.append("                  <statusCode code=\"completed\"/>");
        sb.append("                  <effectiveTime>");
        sb.append("                    <low value=\"20080401\"/>");
        sb.append("                    <high value=\"20080430\"/>");
        sb.append("                  </effectiveTime>");
        sb.append("                  <participant typeCode=\"CSM\">");
        sb.append("                    <participantRole classCode=\"MANU\">");
        sb.append("                      <playingEntity classCode=\"MMAT\">");
        sb.append("                        <code code=\"00504-554-4433\" displayName=\"Trucare\" codeSystemName=\"RxNorm\" codeSystem=\"2.16.840.1.113883.6.88\"/>");
        sb.append("                        <name>Trucare</name>");
        sb.append("                      </playingEntity>");
        sb.append("                    </participantRole>");
        sb.append("                  </participant>");
        sb.append("                </observation>");
        sb.append("              </entryRelationship>");
        sb.append("            </act>");
        sb.append("          </entry>");
        sb.append("          <entry>");
        sb.append("            <act classCode=\"ACT\" moodCode=\"EVN\">");
        sb.append("              <templateId root=\"2.16.840.1.113883.10.20.1.27\"/>");
        sb.append("              <templateId root=\"2.16.840.1.113883.3.88.11.32.6\"/>");
        sb.append("              <id root=\"2C748172-7CC2-4902-8AF0-23A105C4401B\"/>");
        sb.append("              <code nullFlavor=\"NA\"/>");
        sb.append("              <entryRelationship typeCode=\"SUBJ\">");
        sb.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("                  <templateId root=\"2.16.840.1.113883.10.20.1.18\"/>");
        sb.append("                  <code code=\"414285001\" displayName=\"Food Allergy\" codeSystemName=\"SNOMED CT\" codeSystem=\"2.16.840.1.113883.6.96\"/>");
        sb.append("                  <statusCode code=\"completed\"/>");
        sb.append("                  <effectiveTime>");
        sb.append("                    <low value=\"20080414\"/>");
        sb.append("                    <high value=\"20080430\"/>");
        sb.append("                  </effectiveTime>");
        sb.append("                  <participant typeCode=\"CSM\">");
        sb.append("                    <participantRole classCode=\"MANU\">");
        sb.append("                      <playingEntity classCode=\"MMAT\">");
        sb.append("                        <code code=\"75443544\" displayName=\"peanuts\" codeSystemName=\"RxNorm\" codeSystem=\"2.16.840.1.113883.6.88\"/>");
        sb.append("                        <name>peanuts</name>");
        sb.append("                      </playingEntity>");
        sb.append("                    </participantRole>");
        sb.append("                  </participant>");
        sb.append("                </observation>");
        sb.append("              </entryRelationship>");
        sb.append("            </act>");
        sb.append("          </entry>");
        sb.append("        </section>");
        sb.append("      </component>");
        sb.append("      <component>");
        sb.append("        <section>");
        sb.append("          <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.9\"/>");
        sb.append("          <code code=\"48768-6\" codeSystemName=\"LOINC\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("          <title>Insurance Providers</title>");
        sb.append("          <text>");
        sb.append("            <table border=\"1\" width=\"100%\">");
        sb.append("              <thead>");
        sb.append("                <tr>");
        sb.append("                  <th>Insurance Provider Name</th>");
        sb.append("                  <th>Insurance Provider Type</th>");
        sb.append("                  <th>Insurance Provider Group Number</th>");
        sb.append("                </tr>");
        sb.append("              </thead>");
        sb.append("              <tbody>");
        sb.append("                <tr>");
        sb.append("                  <td>AJAX INSURANCE</td>");
        sb.append("                  <td>AJAX INSURANCE</td>");
        sb.append("                  <td>G1854093</td>");
        sb.append("                </tr>");
        sb.append("              </tbody>");
        sb.append("            </table>");
        sb.append("          </text>");
        sb.append("          <entry>");
        sb.append("            <act classCode=\"ACT\" moodCode=\"DEF\">");
        sb.append("              <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.20\"/>");
        sb.append("              <id extension=\"GroupOrContract#\" root=\"G1854093\"/>");
        sb.append("              <code code=\"48768-6\" displayName=\"Payment Sources\" codeSystemName=\"LOINC\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("              <statusCode code=\"completed\"/>");
        sb.append("              <entryRelationship typeCode=\"COMP\">");
        sb.append("                <act classCode=\"ACT\" moodCode=\"EVN\">");
        sb.append("                  <templateId root=\"2.16.840.1.113883.10.20.1.26\"/>");
        sb.append("                  <templateId root=\"2.16.840.1.113883.3.88.11.32.5\"/>");
        sb.append("                  <id extension=\"GroupOrContract#\" root=\"G1854093\"/>");
        sb.append("                  <code code=\"GP\" displayName=\"Group Policy\" codeSystemName=\"X12N-1336\" codeSystem=\"2.16.840.1.113883.6.255.1336\"/>");
        sb.append("                  <statusCode code=\"completed\"/>");
        sb.append("                  <performer typeCode=\"PRF\">");
        sb.append("                    <assignedEntity classCode=\"ASSIGNED\">");
        sb.append("                      <id root=\"2.16.840.1.113883.3.88.3.1\"/>");
        sb.append("                      <representedOrganization classCode=\"ORG\">");
        sb.append("                        <id root=\"2.16.840.1.113883.19.5\"/>");
        sb.append("                        <name>AJAX INSURANCE</name>");
        sb.append("                      </representedOrganization>");
        sb.append("                    </assignedEntity>");
        sb.append("                  </performer>");
        sb.append("                  <participant typeCode=\"HLD\">");
        sb.append("                    <participantRole classCode=\"IND\">");
        sb.append("                      <id extension=\"7095365879\" root=\"AssignAuthorityGUID\"/>");
        sb.append("                      <addr>");
        sb.append("                      </addr>");
        sb.append("                      <playingEntity>");
        sb.append("                        <name>");
        sb.append("                        </name>");
        sb.append("                        <sdtc:birthTime value=\"19630408\"/>");
        sb.append("                      </playingEntity>");
        sb.append("                    </participantRole>");
        sb.append("                  </participant>");
        sb.append("                </act>");
        sb.append("              </entryRelationship>");
        sb.append("            </act>");
        sb.append("          </entry>");
        sb.append("        </section>");
        sb.append("      </component>");
        sb.append("      <component>");
        sb.append("        <section>");
        sb.append("          <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.8\"/>");
        sb.append("          <code code=\"10160-0\" displayName=\"History of medication use\" codeSystemName=\"LOINC\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("          <title>Medications</title>");
        sb.append("          <text>");
        sb.append("            <table border=\"1\" width=\"100%\">");
        sb.append("              <thead>");
        sb.append("                <tr>");
        sb.append("                  <th>Product Display Name</th>");
        sb.append("                  <th>Free Text Brand Name</th>");
        sb.append("                  <th>Ordered Value</th>");
        sb.append("                  <th>Ordered Unit</th>");
        sb.append("                  <th>Expiration Time</th>");
        sb.append("                </tr>");
        sb.append("              </thead>");
        sb.append("              <tbody>");
        sb.append("                <tr>");
        sb.append("                  <td>");
        sb.append("                    <content ID=\"medication-809983199\">Hyaline</content>");
        sb.append("                  </td>");
        sb.append("                  <td>Hyalinia</td>");
        sb.append("                  <td>30.0</td>");
        sb.append("                  <td>TAB</td>");
        sb.append("                  <td>20080430</td>");
        sb.append("                </tr>");
        sb.append("              </tbody>");
        sb.append("            </table>");
        sb.append("          </text>");
        sb.append("          <entry>");
        sb.append("            <substanceAdministration classCode=\"SBADM\" moodCode=\"EVN\">");
        sb.append("              <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.24\"/>");
        sb.append("              <templateId assigningAuthorityName=\"HITSP/C32\" root=\"2.16.840.1.113883.3.88.11.32.8\"/>");
        sb.append("              <id/>");
        sb.append("              <consumable>");
        sb.append("                <manufacturedProduct classCode=\"MANU\">");
        sb.append("                  <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.53\"/>");
        sb.append("                  <templateId assigningAuthorityName=\"HITSP/C32\" root=\"2.16.840.1.113883.3.88.11.32.9\"/>");
        sb.append("                  <manufacturedMaterial classCode=\"MMAT\" determinerCode=\"KIND\">");
        sb.append("                    <code code=\"87643566\" displayName=\"Hyaline\" codeSystemName=\"RxNorm\" codeSystem=\"2.16.840.1.113883.6.88\">");
        sb.append("                      <originalText>Hyaline</originalText>");
        sb.append("                    </code>");
        sb.append("                    <name>Hyalinia</name>");
        sb.append("                  </manufacturedMaterial>");
        sb.append("                </manufacturedProduct>");
        sb.append("              </consumable>");
        sb.append("              <entryRelationship typeCode=\"SUBJ\">");
        sb.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("                  <templateId root=\"2.16.840.1.113883.3.88.11.32.10\"/>");
        sb.append("                  <code code=\"73639000\" displayName=\"Prescription Drug\" codeSystemName=\"SNOMED CT\" codeSystem=\"2.16.840.1.113883.6.96\"/>");
        sb.append("                  <statusCode code=\"completed\"/>");
        sb.append("                </observation>");
        sb.append("              </entryRelationship>");
        sb.append("              <entryRelationship typeCode=\"REFR\">");
        sb.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("                  <code code=\"33999-4\" displayName=\"Status\" codeSystemName=\"LOINC\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("                  <statusCode code=\"Dispensed\"/>");
        sb.append("                  <value code=\"55561003\" displayName=\"Active\" xsi:type=\"CE\" codeSystemName=\"SNOMED CT\" codeSystem=\"2.16.840.1.113883.6.96\"/>");
        sb.append("                </observation>");
        sb.append("              </entryRelationship>");
        sb.append("              <entryRelationship typeCode=\"REFR\">");
        sb.append("                <supply classCode=\"SPLY\" moodCode=\"INT\">");
        sb.append("                  <templateId root=\"2.16.840.1.113883.3.88.1.11.32.11\"/>");
        sb.append("                  <id extension=\"SCRIP#\" root=\"TAB\"/>");
        sb.append("                  <effectiveTime value=\"20080430\"/>");
        sb.append("                  <quantity value=\"30.0\"/>");
        sb.append("                </supply>");
        sb.append("              </entryRelationship>");
        sb.append("            </substanceAdministration>");
        sb.append("          </entry>");
        sb.append("        </section>");
        sb.append("      </component>");
        sb.append("      <component>");
        sb.append("        <section>");
        sb.append("          <templateId root=\"2.16.840.1.113883.10.20.1.1\"/>");
        sb.append("          <code code=\"42348-3\" codeSystemName=\"LOINC\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("          <title>Advance Directive</title>");
        sb.append("          <text>");
        sb.append("            <content ID=\"advance-directive-989570598\">Blood transfusion OK</content>");
        sb.append("          </text>");
        sb.append("          <entry>");
        sb.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("              <templateId assigningAuthorityName=\"CCD\" root=\"2.16.840.1.113883.10.20.1.17\"/>");
        sb.append("              <templateId assigningAuthorityName=\"HITSP/C32\" root=\"2.16.840.1.113883.3.88.11.32.13\"/>");
        sb.append("              <id/>");
        sb.append("              <code code=\"116859006\" displayName=\"Transfusion of Blood\" codeSystemName=\"SNOMED CT\" codeSystem=\"2.16.840.1.113883.6.96\">");
        sb.append("                <originalText>");
        sb.append("                  <reference value=\"advance-directive-989570598\"/>");
        sb.append("                </originalText>");
        sb.append("              </code>");
        sb.append("              <statusCode code=\"completed\"/>");
        sb.append("              <effectiveTime>");
        sb.append("                <low value=\"20080401\"/>");
        sb.append("                <high value=\"20080430\"/>");
        sb.append("              </effectiveTime>");
        sb.append("              <participant typeCode=\"CST\">");
        sb.append("                <participantRole classCode=\"AGNT\">");
        sb.append("                  <addr>");
        sb.append("                    <streetAddressLine>9435 BELL ROAD</streetAddressLine>");
        sb.append("                    <streetAddressLine>2ND FLOOR</streetAddressLine>");
        sb.append("                    <city>LEESBURG</city>");
        sb.append("                    <state>VA</state>");
        sb.append("                    <postalCode>20155</postalCode>");
        sb.append("                    <country>US</country>");
        sb.append("                  </addr>");
        sb.append("                  <telecom value=\"tel:703-555-0011\" use=\"HP\"/>");
        sb.append("                  <telecom value=\"tel:703-555-0012\" use=\"WP\"/>");
        sb.append("                  <telecom value=\"tel:703-555-0013\" use=\"MC\"/>");
        sb.append("                  <telecom value=\"tel:703-555-0014\" use=\"HV\"/>");
        sb.append("                  <telecom value=\"mailto:smith@doctors.com\"/>");
        sb.append("                  <telecom value=\"http://www.here.com/smith\"/>");
        sb.append("                  <playingEntity>");
        sb.append("                    <name>");
        sb.append("                      <prefix>MR</prefix>");
        sb.append("                      <given qualifier=\"CL\">FRED</given>");
        sb.append("                      <family qualifier=\"BR\">SMITH</family>");
        sb.append("                      <suffix>ESQ</suffix>");
        sb.append("                    </name>");
        sb.append("                  </playingEntity>");
        sb.append("                </participantRole>");
        sb.append("              </participant>");
        sb.append("              <entryRelationship typeCode=\"REFR\">");
        sb.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">");
        sb.append("                  <templateId root=\"2.16.840.1.113883.10.20.1.37\"/>");
        sb.append("                  <code code=\"33999-4\" displayName=\"Status\" codeSystem=\"2.16.840.1.113883.6.1\"/>");
        sb.append("                  <statusCode code=\"completed\"/>");
        sb.append("                  <value code=\"15240007\" displayName=\"Current and verified\" xsi:type=\"CE\" codeSystem=\"2.16.840.1.113883.6.96\"/>");
        sb.append("                </observation>");
        sb.append("              </entryRelationship>");
        sb.append("            </observation>");
        sb.append("          </entry>");
        sb.append("        </section>");
        sb.append("      </component>");
        sb.append("    </structuredBody>");
        sb.append("  </component>");
        sb.append("</ClinicalDocument>");
        return sb.toString();
    }
}