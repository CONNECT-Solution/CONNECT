package gov.hhs.fha.nhinc.document;

import gov.hhs.fha.nhinc.repository.service.DocumentService;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.apache.commons.logging.Log;
import org.junit.runner.RunWith;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Test case for the documentRepositoryProvideAndRegisterDocumentSet method of the DocumentRepositoryHelper class
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class DocumentRepositoryHelperProvideAndRegisterTest
{

    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testDocumentRepositoryProvideAndRegisterDocumentSet()
    {
        // Create mock objects
        final Log mockLog = context.mock(Log.class);
        final DocumentService docService = context.mock(DocumentService.class);
        final UTCDateUtil mockDateUtil = context.mock(UTCDateUtil.class);

        DocumentRepositoryHelper helper = new DocumentRepositoryHelper()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected DocumentService getDocumentService()
            {
                return docService;
            }

            @Override
            protected UTCDateUtil createDateUtil()
            {
                return mockDateUtil;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            allowing (mockLog).isDebugEnabled();
            allowing (mockLog).debug(with(any(String.class)));
            allowing (mockDateUtil).parseUTCDateOptionalTimeZone(with(any(String.class)));
            oneOf (docService).saveDocument(with(aNonNull(gov.hhs.fha.nhinc.repository.model.Document.class)));
        }});

        ProvideAndRegisterDocumentSetRequestType request = loadDocument(FULL_PROVIDE_AND_REGISTER_MESSAGE);
        assertNotNull("Loaded document is null", request);

        RegistryResponseType response = helper.documentRepositoryProvideAndRegisterDocumentSet(request);
        assertNotNull("Response was null", response);

    }

    private ProvideAndRegisterDocumentSetRequestType loadDocument(String serializedDoc)
    {
        ProvideAndRegisterDocumentSetRequestType request = null;
        String contextPath = "ihe.iti.xds_b._2007";
        Object unmarshalledObject = null;

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(contextPath);
            javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader stringReader = new StringReader(serializedDoc);
            unmarshalledObject = unmarshaller.unmarshal(stringReader);
            if (unmarshalledObject instanceof JAXBElement)
            {
                JAXBElement jaxb = (JAXBElement) unmarshalledObject;
                unmarshalledObject = jaxb.getValue();
            }
        } catch (Exception e)
        {
            unmarshalledObject = null;
            e.printStackTrace();
            fail("Exception unmarshalling adhoc query: " + e.getMessage());
        }
        if(unmarshalledObject instanceof ProvideAndRegisterDocumentSetRequestType)
        {
            request = (ProvideAndRegisterDocumentSetRequestType)unmarshalledObject;
        }
        else
        {
            fail("Unmarshalled object is not a ProvideAndRegisterDocumentSetRequestType. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return request;

    }

    private static final String FULL_PROVIDE_AND_REGISTER_MESSAGE = 
        "<xdsb:ProvideAndRegisterDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\">" +
        " <lcm:SubmitObjectsRequest xmlns:lcm=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\">" +
        "	<rim:RegistryObjectList xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\">" +
        "	   <rim:ExtrinsicObject home=\"urn:oid:1.1\" id=\"Document01\" isOpaque=\"false\" mimeType=\"text/xml\" objectType=\"urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1\">" +
        "		  <rim:Slot name=\"creationTime\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>2008-06-27 13:49:24</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Slot name=\"intendedRecipient\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>Agilex Technologies|Scott Borst</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Slot name=\"languageCode\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>en-US</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Slot name=\"legalAuthenticator\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>legal authenticator for a consent document</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Slot name=\"serviceStartTime\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>2007-09-11 00:00:00</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Slot name=\"serviceStopTime\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>2008-06-27 00:00:00</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Slot name=\"sourcePatientId\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>500000001^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Slot name=\"sourcePatientInfo\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>PID-3|pid1^^^domain</rim:Value>" +
        "				<rim:Value>PID-5|Schnur^Anna^^^</rim:Value>" +
        "				<rim:Value>PID-7|19560813</rim:Value>" +
        "				<rim:Value>PID-8|F</rim:Value>" +
        "				<rim:Value>PID-11|312 HILL ROAD^^HILLSBORO^MO^37660^US</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Name>" +
        "			 <rim:LocalizedString value=\"ProvideAndRegisterTestDocument\"/>" +
        "		  </rim:Name>" +
        "		  <rim:Description>" +
        "			 <rim:LocalizedString value=\"Consent document 1 Schnur VA comments\"/>" +
        "		  </rim:Description>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d\" classifiedObject=\"Document01\" id=\"\" nodeRepresentation=\"\">" +
        "			 <rim:Slot name=\"authorPerson\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Axolotl Elysium</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Slot name=\"authorInstitution\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Department of Veterans Affairs</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Slot name=\"authorRole\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Primary Care Provider</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Slot name=\"authorSpecialty\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>General</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a\" classifiedObject=\"Document01\" id=\"\" nodeRepresentation=\"XNHIN-CONSENT\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>2.16.840.1.113883.6.1</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"NHIN Consent Document\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f\" classifiedObject=\"Document01\" id=\"\" nodeRepresentation=\"R\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>2.16.840.1.113883.5.25</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"Restricted\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d\" classifiedObject=\"Document01\" id=\"\" nodeRepresentation=\"CDAR2/IHE 1.0\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Connect-a-thon formatCodes</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"CDAR2/IHE 1.0\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1\" classifiedObject=\"Document01\" id=\"\" nodeRepresentation=\"Outpatient\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Connect-a-thon healthcareFacilityTypeCodes</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"Outpatient\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead\" classifiedObject=\"Document01\" id=\"\" nodeRepresentation=\"394802001\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>2.16.840.1.113883.6.96</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"General Medicine\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:f0306f51-975f-434e-a61c-c59651d33983\" classifiedObject=\"Document01\" id=\"\" nodeRepresentation=\"34133-9\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>LOINC</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"Summarization of Episode Note\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4\" classifiedObject=\"Document01\" nodeRepresentation=\"EventCodeValue\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>EventCodeSchemeValue</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"EventCodeScheme\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:ExternalIdentifier id=\"\" identificationScheme=\"urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab\" registryObject=\"Document01\" value=\"2.1.11111.1.1\">" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"XDSDocumentEntry.uniqueId\"/>" +
        "			 </rim:Name>" +
        "		  </rim:ExternalIdentifier>" +
        "		  <rim:ExternalIdentifier id=\"\" identificationScheme=\"urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427\" registryObject=\"Document01\" value=\"500000001^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\">" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"XDSDocumentEntry.patientId\"/>" +
        "			 </rim:Name>" +
        "		  </rim:ExternalIdentifier>" +
        "	   </rim:ExtrinsicObject>" +
        "	   <rim:RegistryPackage id=\"SubmissionSet01\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage\">" +
        "		  <rim:Slot name=\"submissionTime\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>200905211223</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "		  <rim:Name>" +
        "			 <rim:LocalizedString value=\"Consent SubmissionSet1 Name\"/>" +
        "		  </rim:Name>" +
        "		  <rim:Description>" +
        "			 <rim:LocalizedString value=\"Consent SubmissionSet1\"/>" +
        "		  </rim:Description>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d\" classifiedObject=\"SubmissionSet01\" nodeRepresentation=\"\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\" id=\"id_20\">" +
        "			 <rim:Slot name=\"authorPerson\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Axolotl Elysium</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Slot name=\"authorInstitution\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Department of Veterans Affairs</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Slot name=\"authorRole\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Primary Care Provider</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Slot name=\"authorSpecialty\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>General</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "		  </rim:Classification>" +
        "		  <rim:Classification classificationScheme=\"urn:uuid:aa543740-bdda-424e-8c96-df4873be8500\" classifiedObject=\"SubmissionSet01\" nodeRepresentation=\"History and Physical\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\" id=\"id_21\">" +
        "			 <rim:Slot name=\"codingScheme\">" +
        "				<rim:ValueList>" +
        "				   <rim:Value>Connect-a-thon contentTypeCodes</rim:Value>" +
        "				</rim:ValueList>" +
        "			 </rim:Slot>" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"History and Physical\"/>" +
        "			 </rim:Name>" +
        "		  </rim:Classification>" +
        "		  <rim:ExternalIdentifier identificationScheme=\"urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8\" value=\"2.1.11111.1.2\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\" id=\"id_22\" registryObject=\"SubmissionSet01\">" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"XDSSubmissionSet.uniqueId\"/>" +
        "			 </rim:Name>" +
        "		  </rim:ExternalIdentifier>" +
        "		  <rim:ExternalIdentifier identificationScheme=\"urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832\" value=\"129.6.58.92.1.1\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\" id=\"id_23\" registryObject=\"SubmissionSet01\">" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"XDSSubmissionSet.sourceId\"/>" +
        "			 </rim:Name>" +
        "		  </rim:ExternalIdentifier>" +
        "		  <rim:ExternalIdentifier identificationScheme=\"urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446\" value=\"500000001^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier\" id=\"id_24\" registryObject=\"SubmissionSet01\">" +
        "			 <rim:Name>" +
        "				<rim:LocalizedString value=\"XDSSubmissionSet.patientId\"/>" +
        "			 </rim:Name>" +
        "		  </rim:ExternalIdentifier>" +
        "	   </rim:RegistryPackage>" +
        "	   <rim:Association associationType=\"urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember\" sourceObject=\"SubmissionSet01\" targetObject=\"Document01\" id=\"ID_25276323_1\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association\">" +
        "		  <rim:Slot name=\"SubmissionSetStatus\">" +
        "			 <rim:ValueList>" +
        "				<rim:Value>Original</rim:Value>" +
        "			 </rim:ValueList>" +
        "		  </rim:Slot>" +
        "	   </rim:Association>" +
        "	   <rim:Classification classifiedObject=\"SubmissionSet01\" classificationNode=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\" id=\"ID_25276323_3\" objectType=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification\"/>" +
        "	</rim:RegistryObjectList>" +
        " </lcm:SubmitObjectsRequest>" +
        " <xdsb:Document id=\"Document01\">PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCjxQb2xpY3kgeG1sbnM9InVybjpvYXNpczpuYW1lczp0Yzp4YWNtbDoyLjA6cG9saWN5OnNjaGVtYTpvcyIgeG1sbnM6bmhpbj0iaHR0cDovL3d3dy5oaHMuZ292L2hlYWx0aGl0L25oaW4iIHBvbGljeWlkPSIxMjM0NTY3OC0xMjM0LTEyMzQtMTIzNC0xMjM0NTY3ODlhYmMiIHJ1bGVjb21iaW5pbmdhbGdpZD0idXJuOm9hc2lzOm5hbWVzOnRjOnhhY21sOjEuMDpydWxlLWNvbWJpbmluZy1hbGdvcml0aG06Zmlyc3QtYXBwbGljYWJsZSI+DQoJPERlc2NyaXB0aW9uPlNhbXBsZSBYQUNNTCBwb2xpY3kgc2hvd2luZyBhY2Nlc3MgYnkgdXNlciByb2xlPC9EZXNjcmlwdGlvbj4NCgk8VGFyZ2V0Pg0KCQk8QWN0aW9ucz4NCgkJCTxBY3Rpb24+DQoJCQkJPEFjdGlvbk1hdGNoIE1hdGNoSWQ9InVybjpvYXNpczpuYW1lczp0Yzp4YWNtbDoxLjA6ZnVuY3Rpb246c3RyaW5nLWVxdWFsIj4NCgkJCQkJPEF0dHJpYnV0ZVZhbHVlIERhdGFUeXBlPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSNhbnlVUkkiPmh0dHA6Ly93d3cuaGhzLmdvdi9oZWFsdGhpdC9uaGluI3JldHJpZXZlRG9jdW1lbnQ8L0F0dHJpYnV0ZVZhbHVlPg0KCQkJCQk8QWN0aW9uQXR0cmlidXRlRGVzaWduYXRvciBBdHRyaWJ1dGVJZD0idXJuOm9hc2lzOm5hbWVzOnRjOnhhY21sOjIuMDphY3Rpb24iIERhdGFUeXBlPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSNhbnlVUkkiLz4NCgkJCQk8L0FjdGlvbk1hdGNoPg0KCQkJPC9BY3Rpb24+DQoJCTwvQWN0aW9ucz4NCgk8L1RhcmdldD4NCgk8RW52aXJvbm1lbnRzPg0KCQk8RW52aXJvbm1lbnQ+DQoJCQk8RW52aXJvbm1lbnRNYXRjaCBNYXRjaElkPSJodHRwOi8vd3d3Lmhocy5nb3YvaGVhbHRoaXQvbmhpbi9mdW5jdGlvbiNpbnN0YW5jZS1pZGVudGlmaWVyLWVxdWFsIj4NCgkJCQk8QXR0cmlidXRlVmFsdWUgRGF0YVR5cGU9Imh0dHA6Ly93d3cuaGhzLmdvdi9oZWFsdGhpdC9uaGluI2luc3RhbmNlLWlkZW50aXRpZmVyIj4NCgkJCQkJPFBhdGllbnRJZCBFeHRlbnNpb249IjUwMDAwMDAwMSIgUm9vdD0iMS4xIi8+DQoJCQkJPC9BdHRyaWJ1dGVWYWx1ZT4NCgkJCQk8RW52aXJvbm1lbnRBdHRyaWJ1dGVEZXNpZ25hdG9yIEF0dHJpYnV0ZUlkPSJodHRwOi8vd3d3Lmhocy5nb3YvaGVhbHRoaXQvbmhpbiNzdWJqZWN0LWlkIiBEYXRhVHlwZT0iaHR0cDovL3d3dy5oaHMuZ292L2hlYWx0aGl0L25oaW4jaW5zdGFuY2UtaWRlbnRpdGlmZXIiLz4NCgkJCTwvRW52aXJvbm1lbnRNYXRjaD4NCgkJPC9FbnZpcm9ubWVudD4NCgk8L0Vudmlyb25tZW50cz4NCgk8UnVsZSBFZmZlY3Q9IlBlcm1pdCIgUnVsZUlkPSIxMjYiPg0KCQk8RGVzY3JpcHRpb24+cGVybWl0IGFsbCBhY2Nlc3MgdG8gZG9jdW1lbnRzLjwvRGVzY3JpcHRpb24+DQoJCTxUYXJnZXQvPg0KCTwvUnVsZT4NCjwvUG9saWN5Pg0K</xdsb:Document>" +
        "</xdsb:ProvideAndRegisterDocumentSetRequest>";


}
