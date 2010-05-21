package gov.hhs.fha.nhinc.lift;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;


/**
 * Test the GatewayLiftManager class.
 *
 * @author Les Westberg
 */
public class GatewayLiftManagerTest
{

    public GatewayLiftManagerTest()
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
     * Test of startLiftTransaction method, of class GatewayLiftManager.
     */
    @Test
    public void testStartLiftTransaction()
    {
        System.out.println("startLiftTransaction");
        StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
        oRequest.setRequestKeyGuid("111-111");
        GatewayLiftManager oManager = new GatewayLiftManager();
        StartLiftTransactionResponseType oResponse = new StartLiftTransactionResponseType();
        oResponse = oManager.startLiftTransaction(oRequest);
        assertNotNull("Response was null and should not have been: ", oResponse);
        assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
    }

    /**
     * Test of completeLiftTransaction method, of class GatewayLiftManager.
     */
    @Test
    public void testCompleteLiftTransaction()
    {
        System.out.println("completeLiftTransaction");
        CompleteLiftTransactionRequestType oRequest = new CompleteLiftTransactionRequestType();
        oRequest.setRequestKeyGuid("1111-1111");
        oRequest.setFileURI("/test.xml");
        GatewayLiftManager oManager = new GatewayLiftManager();

        CompleteLiftTransactionResponseType oResponse = new CompleteLiftTransactionResponseType();
        oResponse = oManager.completeLiftTransaction(oRequest);
        assertNotNull("Response was null and should not have been: ", oResponse);
        assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
    }

    /**
     * Test of failedLiftTransaction method, of class GatewayLiftManager.
     */
    @Test
    public void testFailedLiftTransaction()
    {
        System.out.println("failedLiftTransaction");
        FailedLiftTransactionRequestType oRequest = new FailedLiftTransactionRequestType();
        oRequest.setRequestKeyGuid("222-222");
        oRequest.setErrorMessage("This is the error message.");
        GatewayLiftManager oManager = new GatewayLiftManager();

        FailedLiftTransactionResponseType oResponse = new FailedLiftTransactionResponseType();
        oResponse = oManager.failedLiftTransaction(oRequest);
        assertNotNull("Response was null and should not have been: ", oResponse);
        assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
    }
}
