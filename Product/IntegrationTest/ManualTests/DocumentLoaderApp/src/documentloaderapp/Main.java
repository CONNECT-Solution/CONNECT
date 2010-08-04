/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package documentloaderapp;

import gov.hhs.fha.nhinc.repository.model.Document;
import gov.hhs.fha.nhinc.repository.service.DocumentService;
import java.util.Date;

/**
 *
 * @author JHOPPESC
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DocumentService service = new DocumentService();
        Document doc = new Document();
        doc.setAuthorInstitution("CONNECT TEST GATEWAY");
        doc.setAuthorPerson("Dr. Seuss");
        doc.setAuthorRole("Primary Care Provider");
        doc.setAuthorSpecialty("General");
        doc.setAvailablityStatus("Active");
        doc.setClassCode("34133-9");
        doc.setClassCodeDisplayName("Summarization of Episode Note");
        doc.setClassCodeScheme("2.16.840.1.113883.6.1");
        doc.setComments("Test");
        doc.setConfidentialityCode("Confidential");
        doc.setConfidentialityCodeDisplayName("Confidential");
        doc.setConfidentialityCodeScheme("Confidential");
        doc.setCreationTime(new Date());
        doc.setDocumentTitle("Frost CONNECT TESTGATEWAY Document 1");
        doc.setDocumentUniqueId("1.123456.33333");
        doc.setDocumentUri("1.123456.33333");
        // doc.setEventCodes(null);
        doc.setFacilityCode("UTF-8");
        doc.setFacilityCodeDisplayName("UTF-8");
        doc.setFacilityCodeScheme("UTF-8");
        doc.setFormatCode("CDAR2/IHE 1.0");
        doc.setFormatCodeDisplayName("formatCodeDisplayName_value");
        doc.setFormatCodeScheme("formatCodeScheme_value");
        doc.setHash("e2cad0cdfdf929725a883067637d8268f6d9c578");
        doc.setIntendedRecipientOrganization("Chicago Bears");
        doc.setIntendedRecipientPerson("Jay Cutler");
        doc.setLanguageCode("en-US");
        doc.setLegalAuthenticator("legal");
        doc.setMimeType("text/xml");
        doc.setParentDocumentId("1.123401.33333");
        doc.setParentDocumentRelationship("same");
        doc.setPatientId("123456");
        doc.setPersistent(false);
        doc.setPersistentCode(0);
        doc.setPid11("1111 Main Street^^Kingsport^TN^37662^US");
        doc.setPid3("pid1^^^domain");
        doc.setPid5("Frost^Jack^^^");
        doc.setPid7("19990627");
        doc.setPid8("M");
        doc.setPracticeSetting("test");
        doc.setPracticeSettingDisplayName("test");
        doc.setPracticeSettingScheme("test");
        doc.setServiceStartTime(new Date());
        doc.setServiceStopTime(new Date());
        doc.setSize(120290549);
        doc.setSourcePatientId("123456");
        doc.setTypeCode("test");
        doc.setTypeCodeDisplayName("test");
        doc.setTypeCodeScheme("test");

        String url = null;
        if (args.length == 1 &&
                args[0] != null &&
                args[0].length() > 0) {
           url = args[0];
        }
        else {
           url = "file:///C:/Temp/test114mb.zip";
        }

        byte[] rawData = url.getBytes();
        doc.setRawData(rawData);

        service.saveDocument(doc);
    }
}
