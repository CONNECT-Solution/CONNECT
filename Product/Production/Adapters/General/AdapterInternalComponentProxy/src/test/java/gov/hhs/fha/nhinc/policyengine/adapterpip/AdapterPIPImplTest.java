package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyMetadataType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.StringReader;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.apache.commons.logging.Log;

/**
 *
 * @author webbn
 */
@RunWith(JMock.class)
public class AdapterPIPImplTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    public AdapterPIPImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStoreOptInTrue() {
        // Create mock objects
        final Log mockLog = context.mock(Log.class);
        final PatientConsentManager consentManager = context.mock(PatientConsentManager.class);

        //AdapterPIPImpl pipImpl = new AdapterPIPImpl();
        AdapterPIPImpl pipImpl = new AdapterPIPImpl() {

            @Override
            protected Log createLogger() {
                return mockLog;
            }

            @Override
            protected PatientConsentManager getPatientConsentManager() {
                return consentManager;
            }
        };
        try {
            // Set expectations
            context.checking(new Expectations() {

                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(consentManager).storePatientConsent(with(aNonNull(PatientPreferencesType.class)));
                }
            });

            gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType request = loadStoreRequestMessage(STORE_OPTIN_CONSENT_MESSAGE);
            StorePtConsentResponseType response = pipImpl.storePtConsent(request);
            assertNotNull("Store consent response was null", response);
            assertEquals("Status not as expected", "SUCCESS", response.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception calling storePtConsent: " + ex.getMessage());
        }
    }

//    @Test
//    public void testRetrieveOptInTrue() {
//        AdapterPIPImpl pipImpl = new AdapterPIPImpl();
//        try {
//            gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType request = loadRetrievePtRequestMessage(RETRIEVE_OPTIN_CONSENT_MESSAGE);
//            RetrievePtConsentByPtIdResponseType response = pipImpl.retrievePtConsentByPtId(request);
//            assertNotNull("Retrieve consent response was null", response);
//            assertNotNull("Null Patient preferences was returned", response.getPatientPreferences());
//            PatientPreferencesType oPtPref = response.getPatientPreferences();
//            assertTrue("Mismatch on returned patient: " + oPtPref.getPatientId(), "ADPTPIPTST54325432M".equals(oPtPref.getPatientId()));
//            assertTrue("Mismatch on returned authority: " + oPtPref.getAssigningAuthority(), "1.1".equals(oPtPref.getAssigningAuthority()));
//            assertEquals("Expected to be opt-in", true, (oPtPref.isOptIn()));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception calling retrievePtConsentByPtId: " + ex.getMessage());
//        }
//    }
//
//    @Test
//    public void testStoreOptInFalse() {
//        AdapterPIPImpl pipImpl = new AdapterPIPImpl();
//        try {
//            gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType request = loadStoreRequestMessage(STORE_OPTOUT_CONSENT_MESSAGE);
//            StorePtConsentResponseType response = pipImpl.storePtConsent(request);
//            assertNotNull("Store consent response was null", response);
//            assertEquals("Status not as expected", "SUCCESS", response.getStatus());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception calling storePtConsent: " + ex.getMessage());
//        }
//    }
//
//    @Test
//    public void testRetrieveOptInFalse() {
//        AdapterPIPImpl pipImpl = new AdapterPIPImpl();
//        try {
//            gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType request = loadRetrievePtRequestMessage(RETRIEVE_OPTIN_CONSENT_MESSAGE);
//            RetrievePtConsentByPtIdResponseType response = pipImpl.retrievePtConsentByPtId(request);
//            assertNotNull("Retrieve consent response was null", response);
//            assertNotNull("Null Patient preferences was returned", response.getPatientPreferences());
//            PatientPreferencesType oPtPref = response.getPatientPreferences();
//            assertTrue("Mismatch on returned patient: " + oPtPref.getPatientId(), "ADPTPIPTST54325432M".equals(oPtPref.getPatientId()));
//            assertTrue("Mismatch on returned authority: " + oPtPref.getAssigningAuthority(), "1.1".equals(oPtPref.getAssigningAuthority()));
//            assertEquals("Expected to be opt-our", false, (oPtPref.isOptIn()));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception calling retrievePtConsentByPtId: " + ex.getMessage());
//        }
//    }
//
//    @Test
//    public void testStoreFinePolicy() {
//        AdapterPIPImpl pipImpl = new AdapterPIPImpl();
//        try {
//            gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType request = loadStoreRequestMessage(STORE_FINE_CONSENT_MESSAGE);
//            StorePtConsentResponseType response = pipImpl.storePtConsent(request);
//            assertNotNull("Store consent response was null", response);
//            assertEquals("Status not as expected", "SUCCESS", response.getStatus());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception calling storePtConsent: " + ex.getMessage());
//        }
//    }
//
//    @Test
//    public void testRetrieveFinePolicyDoc() {
//        AdapterPIPImpl pipImpl = new AdapterPIPImpl();
//        try {
//            gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType request = loadRetrieveDocRequestMessage(RETRIEVE_FINE_CONSENT_BY_DOC_MESSAGE);
//            RetrievePtConsentByPtDocIdResponseType response = pipImpl.retrievePtConsentByPtDocId(request);
//            assertNotNull("Retrieve consent response was null", response);
//            assertNotNull("Null Patient preferences was returned", response.getPatientPreferences());
//            PatientPreferencesType oPtPref = response.getPatientPreferences();
//            if (oPtPref != null) {
//                assertTrue("Mismatch on returned patient: " + oPtPref.getPatientId(), "ADPTPIPTST98769876Z".equals(oPtPref.getPatientId()));
//                assertTrue("Mismatch on returned authority: " + oPtPref.getAssigningAuthority(), "1.1".equals(oPtPref.getAssigningAuthority()));
//                // Reset if Fine Gained Policy in place
//                assertEquals("Expected to be opt-in", false, (oPtPref.isOptIn()));
//                FineGrainedPolicyCriteriaType oFineCriteria = oPtPref.getFineGrainedPolicyCriteria();
//                assertNotNull("Null Fine grained policy was returned", oFineCriteria);
//                if (oFineCriteria != null) {
//                    List<FineGrainedPolicyCriterionType> olFineCriterion = oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion();
//                    assertNotNull("Null Fine grained criterion list was returned", olFineCriterion);
//                    if (olFineCriterion != null) {
//                        assertEquals("Expected 2 Fine Grained Criterion Items but received " + olFineCriterion.size(), 2, olFineCriterion.size());
//                        for (FineGrainedPolicyCriterionType oFineCriterion : olFineCriterion) {
//                            if ("1".equals(oFineCriterion.getSequentialId())) {
//                                assertEquals(true, oFineCriterion.isPermit());
//                            } else if ("2".equals(oFineCriterion.getSequentialId())) {
//                                assertEquals(true, oFineCriterion.isPermit());
//                            } else {
//                                fail("Expected sequence id to be either 1 or 2, but found: " + oFineCriterion.getSequentialId());
//                            }
//                        }
//                    }
//                }
//                FineGrainedPolicyMetadataType oFineMetadata = oPtPref.getFineGrainedPolicyMetadata();
//                assertNotNull("Null Fine grained policy metadata was returned", oFineMetadata);
//                if (oFineMetadata != null) {
//                    assertTrue("Expected Metadata Policy OID of 33333333-3333-3333-3333-333333333333 but found: " + oFineMetadata.getPolicyOID(), "33333333-3333-3333-3333-333333333333".equals(oFineMetadata.getPolicyOID()));
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception calling retrievePtConsentByPtDocId: " + ex.getMessage());
//        }
//    }
//    @Test
//    public void testStoreBinaryPolicy() {
//        AdapterPIPImpl pipImpl = new AdapterPIPImpl();
//        try {
//            gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType request = loadStoreRequestMessage(STORE_BINARY_CONSENT_MESSAGE);
//            StorePtConsentResponseType response = pipImpl.storePtConsent(request);
//            assertNotNull("Store consent response was null", response);
//            assertEquals("Status not as expected", "SUCCESS", response.getStatus());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception calling storePtConsent: " + ex.getMessage());
//        }
//    }
//    @Test
//    public void testRetrieveBinInfoByPatient() {
//        AdapterPIPImpl pipImpl = new AdapterPIPImpl();
//        try {
//            gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType request = loadRetrievePtRequestMessage(RETRIEVE_BINARY_CONSENT_BY_PATIENT_MESSAGE);
//            RetrievePtConsentByPtIdResponseType response = pipImpl.retrievePtConsentByPtId(request);
//            assertNotNull("Retrieve consent response was null", response);
//            assertNotNull("Null Patient preferences was returned", response.getPatientPreferences());
//            PatientPreferencesType oPtPref = response.getPatientPreferences();
//            assertTrue("Mismatch on returned patient: " + oPtPref.getPatientId(), "ADPTPIPTST24688642A".equals(oPtPref.getPatientId()));
//            assertTrue("Mismatch on returned authority: " + oPtPref.getAssigningAuthority(), "1.1".equals(oPtPref.getAssigningAuthority()));
//            assertEquals("Expected to be opt-out", false, (oPtPref.isOptIn()));
//            BinaryDocumentPolicyCriteriaType oBinaryCriteria = oPtPref.getBinaryDocumentPolicyCriteria();
//            assertNotNull("Null binary policy was returned", oBinaryCriteria);
//            if (oBinaryCriteria != null) {
//                List<BinaryDocumentPolicyCriterionType> olBinCriterion = oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion();
//                assertNotNull("Null binary criterion list was returned", olBinCriterion);
//                if (olBinCriterion != null) {
//                    assertEquals("Expected 2 Binary Criterion Items but received " + olBinCriterion.size(), 2, olBinCriterion.size());
//                    for (BinaryDocumentPolicyCriterionType oBinaryCriterion : olBinCriterion) {
//                        assertNotNull("Null binary criterion was returned", oBinaryCriterion);
//                        if (oBinaryCriterion != null) {
//                            String sDocId = oBinaryCriterion.getDocumentUniqueId();
//                            assertTrue("Binary document id: " + sDocId + " was not expected", "99.999.9.9".equals(sDocId) || "88.888.8.8".equals(sDocId));
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception calling retrievePtConsentByPtId: " + ex.getMessage());
//        }
//    }
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType loadStoreRequestMessage(String message) {
        gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType request = null;

        Object unmarshalledObject = unmarshallMessage(message);
        if (unmarshalledObject instanceof gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType) {
            request = (gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType) unmarshalledObject;
        } else {
            fail("Unmarshalled object is not a store patient consent message. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return request;
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType loadRetrievePtRequestMessage(String message) {
        gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType request = null;

        Object unmarshalledObject = unmarshallMessage(message);
        if (unmarshalledObject instanceof gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType) {
            request = (gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType) unmarshalledObject;
        } else {
            fail("Unmarshalled object is not a store patient consent message. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return request;
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType loadRetrieveDocRequestMessage(String message) {
        gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType request = null;

        Object unmarshalledObject = unmarshallMessage(message);
        if (unmarshalledObject instanceof gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType) {
            request = (gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType) unmarshalledObject;
        } else {
            fail("Unmarshalled object is not a store patient consent message. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return request;
    }

    private Object unmarshallMessage(String message) {
        Object unmarshalledObject = null;
        String contextPath = "gov.hhs.fha.nhinc.common.nhinccommonadapter";

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(contextPath);
            javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader stringReader = new StringReader(message);
            unmarshalledObject = unmarshaller.unmarshal(stringReader);
            if (unmarshalledObject instanceof JAXBElement) {
                JAXBElement jaxb = (JAXBElement) unmarshalledObject;
                unmarshalledObject = jaxb.getValue();
            }
        } catch (Exception e) {
            unmarshalledObject = null;
            e.printStackTrace();
            fail("Exception unmarshalling message: " + e.getMessage());
        }
        return unmarshalledObject;
    }
    private static final String STORE_OPTIN_CONSENT_MESSAGE =
            "<urn:StorePtConsentRequest xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
            "	<urn:patientPreferences>" +
            "		<urn:patientId>ADPTPIPTST54325432M</urn:patientId>" +
            "		<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
            "		<urn:optIn>true</urn:optIn>" +
            "	</urn:patientPreferences>" +
            "</urn:StorePtConsentRequest>";
    private static final String STORE_OPTOUT_CONSENT_MESSAGE =
            "<urn:StorePtConsentRequest xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
            "	<urn:patientPreferences>" +
            "		<urn:patientId>ADPTPIPTST54325432M</urn:patientId>" +
            "		<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
            "		<urn:optIn>false</urn:optIn>" +
            "	</urn:patientPreferences>" +
            "</urn:StorePtConsentRequest>";
    private static final String RETRIEVE_OPTIN_CONSENT_MESSAGE =
            "<urn:RetrievePtConsentByPtIdRequest xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
            "   <urn:patientId>ADPTPIPTST54325432M</urn:patientId>" +
            "   <urn:assigningAuthority>1.1</urn:assigningAuthority>" +
            "</urn:RetrievePtConsentByPtIdRequest>";
    private static final String RETRIEVE_FINE_CONSENT_BY_DOC_MESSAGE =
            "<urn:RetrievePtConsentByPtDocIdRequest xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
            "   <urn:documentId>33333333-3333-3333-3333-333333333333</urn:documentId>" +
            "   <urn:repositoryId>1</urn:repositoryId>" +
            "   <urn:homeCommunityId>1.1</urn:homeCommunityId>" +
            "</urn:RetrievePtConsentByPtDocIdRequest>";
    private static final String RETRIEVE_BINARY_CONSENT_BY_PATIENT_MESSAGE =
            "<urn:RetrievePtConsentByPtIdRequest xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
            "   <urn:patientId>ADPTPIPTST24688642A</urn:patientId>" +
            "   <urn:assigningAuthority>1.1</urn:assigningAuthority>" +
            "</urn:RetrievePtConsentByPtIdRequest>";
    private static final String STORE_FINE_CONSENT_MESSAGE =
            "<urn:StorePtConsentRequest  xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
            " <urn:patientPreferences>" +
            "	<urn:patientId>ADPTPIPTST98769876Z</urn:patientId>" +
            "	<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
            "	<urn:optIn>true</urn:optIn>" +
            "	<urn:fineGrainedPolicyCriteria>" +
            "      <urn:fineGrainedPolicyCriterion>" +
            "         <urn:sequentialId>1</urn:sequentialId>" +
            "         <urn:permit>true</urn:permit>" +
            "      </urn:fineGrainedPolicyCriterion>" +
            "      <urn:fineGrainedPolicyCriterion>" +
            "         <urn:sequentialId>2</urn:sequentialId>" +
            "         <urn:permit>true</urn:permit>" +
            "      </urn:fineGrainedPolicyCriterion>" +
            "   </urn:fineGrainedPolicyCriteria>" +
            "   <urn:fineGrainedPolicyMetadata>" +
            "      <urn:policyOID>33333333-3333-3333-3333-333333333333</urn:policyOID>" +
            "   </urn:fineGrainedPolicyMetadata>" +
            " </urn:patientPreferences>" +
            "</urn:StorePtConsentRequest>";
    private static final String STORE_BINARY_CONSENT_MESSAGE =
            "<urn:StorePtConsentRequest  xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
            " <urn:patientPreferences>" +
            "	<urn:patientId>ADPTPIPTST24688642A</urn:patientId>" +
            "	<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
            "	<urn:optIn>true</urn:optIn>" +
            "	<urn:fineGrainedPolicyCriteria>" +
            "      <urn:fineGrainedPolicyCriterion>" +
            "         <urn:sequentialId>1</urn:sequentialId>" +
            "         <urn:permit>true</urn:permit>" +
            "      </urn:fineGrainedPolicyCriterion>" +
            "      <urn:fineGrainedPolicyCriterion>" +
            "         <urn:sequentialId>2</urn:sequentialId>" +
            "         <urn:permit>true</urn:permit>" +
            "      </urn:fineGrainedPolicyCriterion>" +
            "   </urn:fineGrainedPolicyCriteria>" +
            "   <urn:binaryDocumentPolicyCriteria>" +
            "	   <urn:binaryDocumentPolicyCriterion>" +
            "         <urn:documentUniqueId>99.999.9.9</urn:documentUniqueId>" +
            "         <urn:storeAction>add</urn:storeAction>" +
            "      </urn:binaryDocumentPolicyCriterion>" +
            "	   <urn:binaryDocumentPolicyCriterion>" +
            "         <urn:documentUniqueId>88.888.8.8</urn:documentUniqueId>" +
            "         <urn:storeAction>update</urn:storeAction>" +
            "      </urn:binaryDocumentPolicyCriterion>" +
            "	   <urn:binaryDocumentPolicyCriterion>" +
            "         <urn:documentUniqueId>77.777.7.7</urn:documentUniqueId>" +
            "         <urn:storeAction>delete</urn:storeAction>" +
            "      </urn:binaryDocumentPolicyCriterion>" +
            "   </urn:binaryDocumentPolicyCriteria>" +
            "   <urn:fineGrainedPolicyMetadata>" +
            "      <urn:policyOID>44444444-4444-4444-4444-4444444444444</urn:policyOID>" +
            "   </urn:fineGrainedPolicyMetadata>" +
            " </urn:patientPreferences>" +
            "</urn:StorePtConsentRequest>";
}
