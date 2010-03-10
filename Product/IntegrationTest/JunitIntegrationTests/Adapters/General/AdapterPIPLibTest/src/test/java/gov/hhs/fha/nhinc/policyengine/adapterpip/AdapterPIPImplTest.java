package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westbergl
 */
@Ignore // Failure to get web service port when test is run.
public class AdapterPIPImplTest {

    public AdapterPIPImplTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of retrievePtConsentByPtId method, of class AdapterPIPImpl.
     */
    @Test
    public void testRetrievePtConsentByPtId()
    {
        System.out.println("Begin retrievePtConsentByPtId");
        RetrievePtConsentByPtIdRequestType oRequest = new RetrievePtConsentByPtIdRequestType();
        oRequest.setAssigningAuthority("1.1");
        oRequest.setPatientId("123413");

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();
        RetrievePtConsentByPtIdResponseType oResponse = null;
        try
        {
            oResponse = oAdapterPIPImpl.retrievePtConsentByPtId(oRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Exception occurred when calling retrivePtConsentByPtId.  Error: " + e.getMessage();
            System.out.println(sErrorMessage);
            e.printStackTrace();
            fail(sErrorMessage);
        }
        assertNotNull(oResponse);
        assertNotNull(oResponse.getPatientPreferences());
        assertEquals("Showed as patient opt out when it should have been opt in.", true, oResponse.getPatientPreferences().isOptIn());

        System.out.println("End retrievePtConsentByPtId");
    }

    /**
     * Test of retrievePtConsentByPtDocId method, of class AdapterPIPImpl.
     */
    @Test
    public void testRetrievePtConsentByPtDocId()
    {
        System.out.println("Begin retrievePtConsentByPtDocId");
        RetrievePtConsentByPtDocIdRequestType oRequest = new RetrievePtConsentByPtDocIdRequestType();
        oRequest.setHomeCommunityId("1.1");
        oRequest.setRepositoryId("1");
        oRequest.setDocumentId("20.200.20.36");

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();
        RetrievePtConsentByPtDocIdResponseType oResponse = null;

        try
        {
            oResponse = oAdapterPIPImpl.retrievePtConsentByPtDocId(oRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Exception occurred when calling retrivePtConsentByDocId.  Error: " + e.getMessage();
            System.out.println(sErrorMessage);
            e.printStackTrace();
            fail(sErrorMessage);
        }
        assertNotNull(oResponse);
        assertNotNull(oResponse.getPatientPreferences());
        assertEquals("Showed as patient opt out when it should have been opt in.", true, oResponse.getPatientPreferences().isOptIn());

        System.out.println("End retrievePtConsentByPtDocId");
    }

    /**
     * Test of storePtConsent method, of class AdapterPIPImpl.
     */
    @Test
    public void testStorePtConsent()
    {
        System.out.println("Begin storePtConsent");
        StorePtConsentRequestType oRequest = new StorePtConsentRequestType();
        PatientPreferencesType oPtPref = new PatientPreferencesType();
        oRequest.setPatientPreferences(oPtPref);
        oPtPref.setAssigningAuthority("1.1");
        oPtPref.setPatientId("123413");
        oPtPref.setOptIn(true);
        StorePtConsentResponseType oResponse = null;
        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();
        try
        {
            oResponse = oAdapterPIPImpl.storePtConsent(oRequest);
        }
        catch (Exception e)
        {
            System.out.println("Failed to store patient consent information.  Exception: " + e.getMessage());
            e.printStackTrace();
            fail("Failed to store patient consent information.  Exception: " + e.getMessage());
        }
        if(oResponse!=null &&
                !oResponse.getStatus().equals(""))
        {
            System.out.println("Status = "+oResponse.getStatus());
        }
        assertNotNull(oResponse.getStatus());
        System.out.println("End storePtConsent");
    }
}