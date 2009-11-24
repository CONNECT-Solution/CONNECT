/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine.adapterpip.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
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
@Ignore //TODO: move this test to Integration test Suite
public class AdapterPIPProxyObjectFactoryConcreteWebServiceTest
{

    public AdapterPIPProxyObjectFactoryConcreteWebServiceTest()
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
     * This tests the retrievePtConsentByPtId method to make sure it works.
     *
     * @param oAdapterPIPProxy The AdapterPOPProxy interface to be tested.
     */
    private void testRetrievePtConsentByPtId(AdapterPIPProxy oAdapterPIPProxy)
    {
        System.out.println("Begin testRetrievePtConsentByPtId");

        RetrievePtConsentByPtIdRequestType oRequest = new RetrievePtConsentByPtIdRequestType();
        oRequest.setAssigningAuthority("1.1");
        oRequest.setPatientId("D123401");

        RetrievePtConsentByPtIdResponseType oResponse = null;
        try
        {
            oResponse = oAdapterPIPProxy.retrievePtConsentByPtId(oRequest);
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

        System.out.println("End testRetrievePtConsentByPtId");
    }

    /**
     * This tests the retrievePtConsentByPtDocId method to make sure it works.
     *
     * @param oAdapterPIPProxy The AdapterPOPProxy interface to be tested.
     */
    private void testRetrievePtConsentByPtDocId(AdapterPIPProxy oAdapterPIPProxy)
    {
        System.out.println("Begin testRetrievePtConsentByPtDocId");
        RetrievePtConsentByPtDocIdRequestType oRequest = new RetrievePtConsentByPtDocIdRequestType();
        oRequest.setHomeCommunityId("1.1");
        oRequest.setRepositoryId("1");
        oRequest.setDocumentId("555555555");

        RetrievePtConsentByPtDocIdResponseType oResponse = null;
        try
        {
            oResponse = oAdapterPIPProxy.retrievePtConsentByPtDocId(oRequest);
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

        System.out.println("End testRetrievePtConsentByPtDocId");

    }

    /**
     * This tests the storePtConsent method to make sure it works.
     *
     * @param oAdapterPIPProxy The AdapterPOPProxy interface to be tested.
     */
    private void testStorePtConsent(AdapterPIPProxy oAdapterPIPProxy)
    {
        System.out.println("Begin testStorePtConsent");
        StorePtConsentRequestType oRequest = new StorePtConsentRequestType();
        PatientPreferencesType oPtPref = new PatientPreferencesType();
        oRequest.setPatientPreferences(oPtPref);
        oPtPref.setAssigningAuthority("1.1");
        oPtPref.setPatientId("D123401");
        oPtPref.setOptIn(true);

        try
        {
            StorePtConsentResponseType oResponse = oAdapterPIPProxy.storePtConsent(oRequest);
        }
        catch (Exception e)
        {
            System.out.println("Failed to store patient consent information.  Exception: " + e.getMessage());
            e.printStackTrace();
            fail("Failed to store patient consent information.  Exception: " + e.getMessage());
        }


        System.out.println("End testStorePtConsent");

    }

    /**
     * Test of getAdapterPIPProxy method, of class AdapterPIPProxyObjectFactory.
     */
    @Test
    public void testAdapterPIPProxyObjectFactory()
    {
        System.out.println("Begin testAdapterPIPProxyObjectFactory");
        try
        {
            AdapterPIPProxyObjectFactory oFactory = new AdapterPIPProxyObjectFactory();
            AdapterPIPProxy oAdapterPIPProxy = oFactory.getAdapterPIPProxy();
            assertNotNull(oAdapterPIPProxy);
            assertTrue("Adapter PIP was not the Web Service concrete type.", (oAdapterPIPProxy instanceof AdapterPIPWebServiceProxy));
            testRetrievePtConsentByPtId(oAdapterPIPProxy);
            testRetrievePtConsentByPtDocId(oAdapterPIPProxy);
            testStorePtConsent(oAdapterPIPProxy);
        }
        catch (Throwable t)
        {
            System.out.println("Exception in testAdapterPIPProxyObjectFactory: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }

        System.out.println("End testAdapterPIPProxyObjectFactory");

    }

}