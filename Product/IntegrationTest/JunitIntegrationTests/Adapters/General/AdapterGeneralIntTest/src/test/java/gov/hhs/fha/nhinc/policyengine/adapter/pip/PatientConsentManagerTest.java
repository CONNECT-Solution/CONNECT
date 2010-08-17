package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author svalluripalli
 */
@Ignore // Failure to get web service port when test is run.
public class PatientConsentManagerTest {

    public PatientConsentManagerTest() {
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

    /**
     * Create the data to be used in the FineGrainedPolicy Test.
     *
     * @return The patient consent data.
     */
    private PatientPreferencesType createFineGrainedPolicyData()
    {
        PatientPreferencesType oPtPref = new PatientPreferencesType();
        oPtPref.setAssigningAuthority("1.1");
        oPtPref.setPatientId("D123401");
        oPtPref.setOptIn(true);
        FineGrainedPolicyCriteriaType oCriteria = new FineGrainedPolicyCriteriaType();
        oPtPref.setFineGrainedPolicyCriteria(oCriteria);
        List<FineGrainedPolicyCriterionType> olCriterion = oCriteria.getFineGrainedPolicyCriterion();

        // First entry has all fields.
        //----------------------------
        FineGrainedPolicyCriterionType oCriterion = new FineGrainedPolicyCriterionType();
        olCriterion.add(oCriterion);

        oCriterion.setPermit(true);

        CeType oCode = new CeType();
        oCriterion.setUserRole(oCode);
        oCode.setCode("UserRole1");

        oCode = new CeType();
        oCriterion.setPurposeOfUse(oCode);
        oCode.setCode("PurposeOfUse1");

        oCode = new CeType();
        oCriterion.setDocumentTypeCode(oCode);
        oCode.setCode("DocumentType1");

        oCode = new CeType();
        oCriterion.setConfidentialityCode(oCode);
        oCode.setCode("Confidentiality1");

        // Second entry has User Role only.
        //---------------------------------
        oCriterion = new FineGrainedPolicyCriterionType();
        olCriterion.add(oCriterion);

        oCriterion.setPermit(false);

        oCode = new CeType();
        oCriterion.setUserRole(oCode);
        oCode.setCode("UserRole2");

        // Third entry has Purpose of Use only
        //-------------------------------------
        oCriterion = new FineGrainedPolicyCriterionType();
        olCriterion.add(oCriterion);

        oCriterion.setPermit(true);

        oCode = new CeType();
        oCriterion.setPurposeOfUse(oCode);
        oCode.setCode("PurposeOfUse3");

        // Fourth entry has Document Type only.
        //-------------------------------------
        oCriterion = new FineGrainedPolicyCriterionType();
        olCriterion.add(oCriterion);

        oCriterion.setPermit(false);

        oCode = new CeType();
        oCriterion.setDocumentTypeCode(oCode);
        oCode.setCode("DocumentType4");

        // Fifth entry has Confidentiality Code only.
        //----------------------------
        oCriterion = new FineGrainedPolicyCriterionType();
        olCriterion.add(oCriterion);

        oCriterion.setPermit(true);

        oCode = new CeType();
        oCriterion.setConfidentialityCode(oCode);
        oCode.setCode("Confidentiality5");


        return oPtPref;

    }

    /**
     * Validate the value of the user role.
     *
     * @param oCriterion The criterion containing the user role
     * @param sValue The value to be tested against.
     */
    private void validateUserRole(FineGrainedPolicyCriterionType oCriterion, String sValue)
    {
        String sError = "User Role Code Failed Test: ";
        assertNotNull(sError, oCriterion);
        assertNotNull(sError, oCriterion.getUserRole());
        assertEquals(sError, sValue, oCriterion.getUserRole().getCode());
    }

    /**
     * Validate the value of the Purpose of use.
     *
     * @param oCriterion The criterion containing the purpose of use
     * @param sValue The value to be tested against.
     */
    private void validatePurposeOfUse(FineGrainedPolicyCriterionType oCriterion, String sValue)
    {
        String sError = "Purpose Of Use Code Failed Test: ";
        assertNotNull(sError, oCriterion);
        assertNotNull(sError, oCriterion.getPurposeOfUse());
        assertEquals(sError, sValue, oCriterion.getPurposeOfUse().getCode());
    }

    /**
     * Validate the value of the document type.
     *
     * @param oCriterion The criterion containing the document type
     * @param sValue The value to be tested against.
     */
    private void validateDocumentType(FineGrainedPolicyCriterionType oCriterion, String sValue)
    {
        String sError = "Document Type Code Failed Test: ";
        assertNotNull(sError, oCriterion);
        assertNotNull(sError, oCriterion.getDocumentTypeCode());
        assertEquals(sError, sValue, oCriterion.getDocumentTypeCode().getCode());
    }

    /**
     * Validate the value of the confidentiality code.
     *
     * @param oCriterion The criterion containing the confidentiality code
     * @param sValue The value to be tested against.
     */
    private void validateConfidentialityCode(FineGrainedPolicyCriterionType oCriterion, String sValue)
    {
        String sError = "Confidentiality Code Failed Test: ";
        assertNotNull(sError, oCriterion);
        assertNotNull(sError, oCriterion.getConfidentialityCode());
        assertEquals(sError, sValue, oCriterion.getConfidentialityCode().getCode());
    }

    /**
     * This validates that the data that came back by retrieving the patient
     * preferences is what we expected.
     *
     * @param oPtPref The patient preferences data.
     */
    private void validateFineGrainedPolicyData(PatientPreferencesType oPtPref)
    {
        assertNotNull(oPtPref);
//        assertEquals("Assigning Authority FAILED: ", "1.1", oPtPref.getAssigningAuthority());
        assertEquals("PatientId Failed: ", "D123401", oPtPref.getPatientId());
        assertEquals("OptIn FAILED: ", true, oPtPref.isOptIn());

        assertNotNull(oPtPref.getFineGrainedPolicyCriteria());
        assertEquals("FineGrainedPolicyCriterionSize FAILED: ", 5, oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion().size());

        boolean bFound[] = {false, false, false, false, false};

        for (FineGrainedPolicyCriterionType oCriterion : oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion())
        {
            assertNotNull(oCriterion);
            if ((oCriterion.getUserRole() != null) &&
                (oCriterion.getUserRole().getCode() != null) &&
                (oCriterion.getUserRole().getCode().equals("UserRole1")))
            {
                validateUserRole(oCriterion, "UserRole1");
                validatePurposeOfUse(oCriterion, "PurposeOfUse1");
                validateDocumentType(oCriterion, "DocumentType1");
                validateConfidentialityCode(oCriterion, "Confidentiality1");
                bFound[0] = true;
            }
            else if ((oCriterion.getUserRole() != null) &&
                     (oCriterion.getUserRole().getCode() != null) &&
                     (oCriterion.getUserRole().getCode().equals("UserRole2")))
            {
                validateUserRole(oCriterion, "UserRole2");
                bFound[1] = true;
            }
            else if ((oCriterion.getPurposeOfUse() != null) &&
                     (oCriterion.getPurposeOfUse().getCode() != null) &&
                     (oCriterion.getPurposeOfUse().getCode().equals("PurposeOfUse3")))
            {
                validatePurposeOfUse(oCriterion, "PurposeOfUse3");
                bFound[2] = true;
            }
            else if ((oCriterion.getDocumentTypeCode() != null) &&
                     (oCriterion.getDocumentTypeCode().getCode() != null) &&
                     (oCriterion.getDocumentTypeCode().getCode().equals("DocumentType4")))
            {
                validateDocumentType(oCriterion, "DocumentType4");
                bFound[3] = true;
            }
            else if ((oCriterion.getConfidentialityCode() != null) &&
                     (oCriterion.getConfidentialityCode().getCode() != null) &&
                     (oCriterion.getConfidentialityCode().getCode().equals("Confidentiality5")))
            {
                validateConfidentialityCode(oCriterion, "Confidentiality5");
                bFound[4] = true;
            }
        }   // for (FineGrainedPolicyCriterionType oCriterion : oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion())

        for (int i = 0; i < 5; i++)
        {
            assertTrue("Fine Grained Policy [" + i + "] FAILED:" , bFound[i]);
        }

    }

    /**
     * This test is used to call the storePatientConsent method.
     *
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    @Test
    public void testStoreAndRetrievePatientConsent()
    {
        System.out.println("Begin testStoreAndRetrievePatientConsent");
        PatientPreferencesType oPtPref = createFineGrainedPolicyData();

        PatientConsentManager oPatientConsentManager = new PatientConsentManager();

        try
        {
            oPatientConsentManager.storePatientConsent(oPtPref);
        }
        catch (Exception e)
        {
            fail("Failed to call storePatientConsent.  Error: " + e.getMessage());
        }

        // Test Retrieving by patient ID
        //------------------------------
        System.out.println("");
        System.out.println("Test retrievePatientConsentByPatientId...");
        System.out.println("");
        oPtPref = null;
        String sAssigningAuthority = "1.1";
        String sPatientId = "D123401";

        try
        {
            oPtPref = oPatientConsentManager.retrievePatientConsentByPatientId(sPatientId, sAssigningAuthority);
        }
        catch (Exception e)
        {
            fail("Failed to call storePatientConsent.  Error: " + e.getMessage());
        }

        assertNotNull(oPtPref);
        validateFineGrainedPolicyData(oPtPref);

        System.out.println("End testStoreAndRetrievePatientConsent");
    }


    /**
     * This test is used to retrieve the patient consent by document ID.  It will normally
     * be commented out because each time a consent document is stored, it gets a
     * new document ID.  So this will only be here to be used when you know that value.
     *
     *
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
//    @Test
//    public void testRetrievePatientConsentByDocumentId()
//    {
//        System.out.println("Begin testStoreAndRetrievePatientConsent");
//        PatientPreferencesType oPtPref = createFineGrainedPolicyData();
//
//        PatientConsentManager oPatientConsentManager = new PatientConsentManager();
//
//        // Test Retrieving by document ID
//        //------------------------------
//        System.out.println("");
//        System.out.println("Test retrievePatientConsentByDocumentId...");
//        System.out.println("");
//        oPtPref = null;
//        String sHomeCommunityId = "1.1";
//        String sRepositoryId = "1";
//        String sDocumentId = "20.200.20.40";
//
//        try
//        {
//            oPtPref = oPatientConsentManager.retrievePatientConsentByDocId(sHomeCommunityId, sRepositoryId, sDocumentId);
//        }
//        catch (Exception e)
//        {
//            fail("Failed to call storePatientConsent.  Error: " + e.getMessage());
//        }
//
//        assertNotNull(oPtPref);
//        validateFineGrainedPolicyData(oPtPref);
//
//        System.out.println("End testStoreAndRetrievePatientConsent");
//    }


//    @Test
//    public void testCheckCPPMetaFromRepositoryUsingXDSb()
//            throws AdapterPIPException
//    {
//        System.out.println("Begin testCheckCPPMetaFromRepositoryUsingXDSb");
//        PatientConsentManager oPatientConsentManager = new PatientConsentManager();
//        assertNotNull(oPatientConsentManager.checkCPPMetaFromRepositoryUsingXDSb("D123401", "1.1"));
//        System.out.println("End testCheckCPPMetaFromRepositoryUsingXDSb");
//    }

}
