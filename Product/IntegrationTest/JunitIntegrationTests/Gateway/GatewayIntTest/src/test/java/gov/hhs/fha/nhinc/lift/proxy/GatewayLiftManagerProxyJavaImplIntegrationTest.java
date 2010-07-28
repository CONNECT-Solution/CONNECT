/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.proxy;

import gov.hhs.fha.nhinc.lift.dao.GatewayLiftMessageDao;
import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westberg
 */
public class GatewayLiftManagerProxyJavaImplIntegrationTest
{
    private final static String REQUEST_KEY_GUID = "3d36f668-da9f-470c-bd9d-aa8216ab4e1e";
    private final static String OUTPUT_FILE_URL = "document.pdf";
    private final static String MESSAGE_XML =
"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
"<ProvideAndRegisterDocumentSetRequest xmlns=\"urn:ihe:iti:xds-b:2007\" xmlns:ns2=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:ns3=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\" xmlns:ns4=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\" xmlns:ns5=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\">" +
"	<ns4:SubmitObjectsRequest comment=\"comme\" id=\"123\">" +
"		<ns2:RegistryObjectList>" +
"			<ns2:ExtrinsicObject mimeType=\"text/xml\" objectType=\"urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1\" id=\"Document01\">" +
"				<ns2:Slot name=\"creationTime\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>20051224</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Slot name=\"languageCode\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>en-us</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Slot name=\"serviceStartTime\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>200412230800</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Slot name=\"serviceStopTime\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>200412230801</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Slot name=\"sourcePatientId\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>ST-1000^^^&amp;1.3.6.1.4.1.21367.2003.3.9&amp;ISO</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Slot name=\"sourcePatientInfo\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>PID-3|ST-1000^^^&amp;1.3.6.1.4.1.21367.2003.3.9&amp;ISO</ns2:Value>" +
"						<ns2:Value>PID-5|Doe^John^^^</ns2:Value>" +
"						<ns2:Value>PID-7|19560527</ns2:Value>" +
"						<ns2:Value>PID-8|M</ns2:Value>" +
"						<ns2:Value>PID-11|100 Main St^^Metropolis^Il^44130^USA</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Slot name=\"transportServiceProtocol\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>HTTPS</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Slot name=\"transportService\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>LIFT</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Name>" +
"					<ns2:LocalizedString value=\"Physical\"/>" +
"				</ns2:Name>" +
"				<ns2:Description/>" +
"				<ns2:Classification classifiedObject=\"Document01\" classificationScheme=\"urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d\" id=\"cl01\">" +
"					<ns2:Slot name=\"authorPerson\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Gerald Smitty</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Slot name=\"authorInstitution\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Cleveland Clinic</ns2:Value>" +
"							<ns2:Value>Parma Community</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Slot name=\"authorRole\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Attending</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Slot name=\"authorSpecialty\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Orthopedic</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"				</ns2:Classification>" +
"				<ns2:Classification nodeRepresentation=\"History and Physical\" classifiedObject=\"Document01\" classificationScheme=\"urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a\" id=\"cl02\">" +
"					<ns2:Slot name=\"codingScheme\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Connect-a-thon classCodes</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"History and Physical\"/>" +
"					</ns2:Name>" +
"				</ns2:Classification>" +
"				<ns2:Classification nodeRepresentation=\"1.3.6.1.4.1.21367.2006.7.101\" classifiedObject=\"Document01\" classificationScheme=\"urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f\" id=\"cl03\">" +
"					<ns2:Slot name=\"codingScheme\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Connect-a-thon confidentialityCodes</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"Clinical-Staff\"/>" +
"					</ns2:Name>" +
"				</ns2:Classification>" +
"				<ns2:Classification nodeRepresentation=\"CDAR2/IHE 1.0\" classifiedObject=\"Document01\" classificationScheme=\"urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d\" id=\"cl04\">" +
"					<ns2:Slot name=\"codingScheme\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Connect-a-thon formatCodes</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"CDAR2/IHE 1.0\"/>" +
"					</ns2:Name>" +
"				</ns2:Classification>" +
"				<ns2:Classification nodeRepresentation=\"Outpatient\" classifiedObject=\"Document01\" classificationScheme=\"urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1\" id=\"cl05\">" +
"					<ns2:Slot name=\"codingScheme\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Connect-a-thon healthcareFacilityTypeCodes</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"Outpatient\"/>" +
"					</ns2:Name>" +
"				</ns2:Classification>" +
"				<ns2:Classification nodeRepresentation=\"General Medicine\" classifiedObject=\"Document01\" classificationScheme=\"urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead\" id=\"cl06\">" +
"					<ns2:Slot name=\"codingScheme\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Connect-a-thon practiceSettingCodes</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"General Medicine\"/>" +
"					</ns2:Name>" +
"				</ns2:Classification>" +
"				<ns2:Classification nodeRepresentation=\"34108-1\" classifiedObject=\"Document01\" classificationScheme=\"urn:uuid:f0306f51-975f-434e-a61c-c59651d33983\" id=\"cl07\">" +
"					<ns2:Slot name=\"codingScheme\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>LOINC</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"Outpatient Evaluation And Management\"/>" +
"					</ns2:Name>" +
"				</ns2:Classification>" +
"				<ns2:ExternalIdentifier value=\"SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\" identificationScheme=\"urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427\" registryObject=\"Document01\" id=\"ei01\">" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"XDSDocumentEntry.patientId\"/>" +
"					</ns2:Name>" +
"				</ns2:ExternalIdentifier>" +
"				<ns2:ExternalIdentifier value=\"1.3.6.1.4.1.21367.2005.3.9999.32\" identificationScheme=\"urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab\" registryObject=\"Document01\" id=\"ei02\">" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"XDSDocumentEntry.uniqueId\"/>" +
"					</ns2:Name>" +
"				</ns2:ExternalIdentifier>" +
"			</ns2:ExtrinsicObject>" +
"			<ns2:RegistryPackage id=\"SubmissionSet01\">" +
"				<ns2:Slot name=\"submissionTime\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>20041225235050</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"				<ns2:Name>" +
"					<ns2:LocalizedString value=\"Physical\"/>" +
"				</ns2:Name>" +
"				<ns2:Description>" +
"					<ns2:LocalizedString value=\"Annual physical\"/>" +
"				</ns2:Description>" +
"				<ns2:Classification classifiedObject=\"SubmissionSet01\" classificationScheme=\"urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d\" id=\"cl08\">" +
"					<ns2:Slot name=\"authorPerson\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Sherry Dopplemeyer</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Slot name=\"authorInstitution\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Cleveland Clinic</ns2:Value>" +
"							<ns2:Value>Berea Community</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Slot name=\"authorRole\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Purn4ary Surgon</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Slot name=\"authorSpecialty\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Orthopedic</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"				</ns2:Classification>" +
"				<ns2:Classification nodeRepresentation=\"History and Physical\" classifiedObject=\"SubmissionSet01\" classificationScheme=\"urn:uuid:aa543740-bdda-424e-8c96-df4873be8500\" id=\"cl09\">" +
"					<ns2:Slot name=\"codingScheme\">" +
"						<ns2:ValueList>" +
"							<ns2:Value>Connect-a-thon contentTypeCodes</ns2:Value>" +
"						</ns2:ValueList>" +
"					</ns2:Slot>" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"History and Physical\"/>" +
"					</ns2:Name>" +
"				</ns2:Classification>" +
"				<ns2:ExternalIdentifier value=\"1.3.6.1.4.1.21367.2005.3.9999.33\" identificationScheme=\"urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8\" registryObject=\"SubmissionSet01\" id=\"ei03\">" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"XDSSubmissionSet.uniqueId\"/>" +
"					</ns2:Name>" +
"				</ns2:ExternalIdentifier>" +
"				<ns2:ExternalIdentifier value=\"3670984664\" identificationScheme=\"urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832\" registryObject=\"SubmissionSet01\" id=\"ei04\">" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"XDSSubmissionSet.sourceId\"/>" +
"					</ns2:Name>" +
"				</ns2:ExternalIdentifier>" +
"				<ns2:ExternalIdentifier value=\"SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\" identificationScheme=\"urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446\" registryObject=\"SubmissionSet01\" id=\"ei05\">" +
"					<ns2:Name>" +
"						<ns2:LocalizedString value=\"XDSSubmissionSet.patientId\"/>" +
"					</ns2:Name>" +
"				</ns2:ExternalIdentifier>" +
"			</ns2:RegistryPackage>" +
"			<ns2:Classification classificationNode=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\" classifiedObject=\"SubmissionSet01\" id=\"cl10\"/>" +
"			<ns2:Association targetObject=\"Document01\" sourceObject=\"SubmissionSet01\" associationType=\"HasMember\" id=\"as01\">" +
"				<ns2:Slot name=\"SubmissionSetStatus\">" +
"					<ns2:ValueList>" +
"						<ns2:Value>Original</ns2:Value>" +
"					</ns2:ValueList>" +
"				</ns2:Slot>" +
"			</ns2:Association>" +
"		</ns2:RegistryObjectList>" +
"	</ns4:SubmitObjectsRequest>" +
"	<Document id=\"Document01\">PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxMSUZUTWVzc2FnZSB4bWxucz0iaHR0cDovL3d3dy5oaHMuZ292L2hlYWx0aGl0L25oaW4iIHhtbG5zOm5zMj0idXJuOm9hc2lzOm5hbWVzOnRjOmVieG1sLXJlZ3JlcDp4c2Q6cmltOjMuMCIgeG1sbnM6bnMzPSJ1cm46b2FzaXM6bmFtZXM6dGM6ZWJ4bWwtcmVncmVwOnhzZDpyczozLjAiIHhtbG5zOm5zND0idXJuOm9hc2lzOm5hbWVzOnRjOmVieG1sLXJlZ3JlcDp4c2Q6cXVlcnk6My4wIj48RGF0YUVsZW1lbnQ+PFNlcnZlclByb3h5RGF0YT48U2VydmVyUHJveHlBZGRyZXNzPmxvY2FsaG9zdDwvU2VydmVyUHJveHlBZGRyZXNzPjxTZXJ2ZXJQcm94eVBvcnQ+MTMzNzwvU2VydmVyUHJveHlQb3J0PjwvU2VydmVyUHJveHlEYXRhPjxDbGllbnREYXRhPjxDbGllbnREYXRhPi8zZDM2ZjY2OC1kYTlmLTQ3MGMtYmQ5ZC1hYTgyMTZhYjRlMWUvZG9jdW1lbnQucGRmPC9DbGllbnREYXRhPjwvQ2xpZW50RGF0YT48L0RhdGFFbGVtZW50PjxSZXF1ZXN0RWxlbWVudD48UmVxdWVzdEd1aWQ+M2QzNmY2NjgtZGE5Zi00NzBjLWJkOWQtYWE4MjE2YWI0ZTFlPC9SZXF1ZXN0R3VpZD48L1JlcXVlc3RFbGVtZW50PjwvTElGVE1lc3NhZ2U+</Document>" +
"</ProvideAndRegisterDocumentSetRequest>";

    private final static String ASSERTION_XML =
"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
"<Assertion xmlns=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\" xmlns:ns2=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">" +
"	<haveSecondWitnessSignature>false</haveSecondWitnessSignature>" +
"	<haveSignature>false</haveSignature>" +
"	<haveWitnessSignature>false</haveWitnessSignature>" +
"	<homeCommunity>" +
"		<homeCommunityId>1.1</homeCommunityId>" +
"	</homeCommunity>" +
"	<uniquePatientId>500000000^^^&amp;1.1&amp;ISO</uniquePatientId>" +
"	<userInfo>" +
"		<personName>" +
"			<familyName>Skagerberg</familyName>" +
"			<givenName>Karl</givenName>" +
"			<secondNameOrInitials>S</secondNameOrInitials>" +
"			<fullName>Karl S Skagerberg</fullName>" +
"		</personName>" +
"		<userName>kskagerb</userName>" +
"		<org>" +
"			<homeCommunityId>2.2</homeCommunityId>" +
"			<name>InternalTest2</name>" +
"		</org>" +
"		<roleCoded>" +
"			<code>307969004</code>" +
"			<codeSystem>2.16.840.1.113883.6.96</codeSystem>" +
"			<codeSystemName>SNOMED_CT</codeSystemName>" +
"			<displayName>Public Health</displayName>" +
"		</roleCoded>" +
"	</userInfo>" +
"	<authorized>false</authorized>" +
"	<purposeOfDisclosureCoded>" +
"		<code>PUBLICHEALTH</code>" +
"		<codeSystem>2.16.840.1.113883.3.18.7.1</codeSystem>" +
"		<codeSystemName>nhin-purpose</codeSystemName>" +
"		<displayName>Use or disclosure of Psychotherapy Notes</displayName>" +
"	</purposeOfDisclosureCoded>" +
"	<samlAuthnStatement>" +
"		<authInstant>2009-04-16T13:15:39.000Z</authInstant>" +
"		<sessionIndex>987</sessionIndex>" +
"		<authContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:X509</authContextClassRef>" +
"		<subjectLocalityAddress>158.147.185.168</subjectLocalityAddress>" +
"		<subjectLocalityDNSName>cs.myharris.net</subjectLocalityDNSName>" +
"	</samlAuthnStatement>" +
"	<samlAuthzDecisionStatement>" +
"		<decision>Permit</decision>" +
"		<resource>https://localhost:8181/CONNECTNhinServicesWeb/NhinService/XDRRequest_Service</resource>" +
"		<action>Execute</action>" +
"		<evidence>" +
"			<assertion>" +
"				<id>40df7c0a-ff3e-4b26-baeb-f2910f6d05a9</id>" +
"				<issueInstant>2009-04-16T13:10:39.093Z</issueInstant>" +
"				<version>2.0</version>" +
"				<issuer>CN=SAML User,OU=Harris,O=HITS,L=Melbourne,ST=FL,C=US</issuer>" +
"				<issuerFormat>urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName</issuerFormat>" +
"				<conditions>" +
"					<notBefore>2009-04-16T13:10:39.093Z</notBefore>" +
"					<notOnOrAfter>2009-12-31T12:00:00.000Z</notOnOrAfter>" +
"				</conditions>" +
"				<accessConsentPolicy>Claim-Ref-1234</accessConsentPolicy>" +
"				<instanceAccessConsentPolicy>Claim-Instance-1</instanceAccessConsentPolicy>" +
"			</assertion>" +
"		</evidence>" +
"	</samlAuthzDecisionStatement>" +
"	<samlSignature>" +
"		<keyInfo>" +
"			<rsaKeyValueModulus/>" +
"			<rsaKeyValueExponent/>" +
"		</keyInfo>" +
"		<signatureValue/>" +
"	</samlSignature>" +
"	<asyncMessageId>uuid:1111111111.111111.111.11</asyncMessageId>" +
"</Assertion>";

    public GatewayLiftManagerProxyJavaImplIntegrationTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * This method writes a GATEWAY_LIFT_MESSAGE record to the table.
     */
    private void writeGatewayLiftMessageRecord()
    {
        GatewayLiftMsgRecord oRecord = new GatewayLiftMsgRecord();
        oRecord.setInitialEntryTimestamp(new Date());
        oRecord.setMessageState("PROCESSING");
        oRecord.setProcessingStartTimestamp(new Date());
        oRecord.setProducerProxyAddress("localhost");
        oRecord.setProducerProxyPort(1337L);
        oRecord.setFileNameToRetrieve("/3d36f668-da9f-470c-bd9d-aa8216ab4e1e/document.pdf");
        oRecord.setRequestKeyGuid(REQUEST_KEY_GUID);
        oRecord.setMessageType("DEFERRED_DOCUMENT_SUBMISSION");
        String sMessage = MESSAGE_XML;
        Blob oMessageBlob = Hibernate.createBlob(sMessage.getBytes());
        oRecord.setMessage(oMessageBlob);
        String sAssertion = ASSERTION_XML;
        Blob oAssertionBlob = Hibernate.createBlob(sAssertion.getBytes());
        oRecord.setAssertion(oAssertionBlob);

        GatewayLiftMessageDao oDao = new GatewayLiftMessageDao();
        ArrayList<GatewayLiftMsgRecord> olRecord = new ArrayList<GatewayLiftMsgRecord>();
        olRecord.add(oRecord);
        oDao.insertRecords(olRecord);
    }

    /**
     * There must be one test - for this to pass - we cannot comment out all tests.
     */
    @Test
    public void testDummy()
    {
    }

// This has been commented out because the completeLiftTransaction makes a web service
// call to complete.  This cannot be done in a JUnit integration test.  So this basically
// is mainly used for debugging purposes and cannot be run automated.
//    /**
//     * Test the GatewayLiftManagerProxyJavaImpl.completeLiftTransaction method.
//     */
//    @Test
//    public void testCompleteLiftTransactionHappyPath()
//    {
//        try
//        {
//            writeGatewayLiftMessageRecord();        // Write the record that we can process.
//
//            GatewayLiftManagerProxyJavaImpl oImpl = new GatewayLiftManagerProxyJavaImpl();
//            CompleteLiftTransactionRequestType oRequest = new CompleteLiftTransactionRequestType();
//            oRequest.setRequestKeyGuid(REQUEST_KEY_GUID);
//            oRequest.setFileURI(OUTPUT_FILE_URL);
//            oImpl.completeLiftTransaction(oRequest);
//        }
//        catch (Exception e)
//        {
//            String sErrorMessage = "An unexpected exception occurred.  Error: " + e.getMessage();
//            fail(sErrorMessage);
//        }
//    }
}
