package gov.hhs.fha.nhinc.lift.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class GatewayLiftManagerProxyNoOpImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    /**
     * Test the startLiftTransaction method.
     * 
     */
    @Test
    public void testStartLiftTransaction()
    {
        try
        {
            System.out.println("startLiftTransaction");
            StartLiftTransactionRequestType oRequest = new StartLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("111-111");
            GatewayLiftManagerProxyNoOpImpl oNoOp = new GatewayLiftManagerProxyNoOpImpl();
            StartLiftTransactionResponseType oResponse = new StartLiftTransactionResponseType();
            oResponse = oNoOp.startLiftTransaction(oRequest);
            assertNotNull("Response was null and should not have been: ", oResponse);
            assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testStartLiftTransaction: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStartLiftTransaction: " + t.getMessage());
        }
    }

    /**
     * Test of completeLiftTransaction method, of class GatewayLiftManager.
     */
    @Test
    public void testCompleteLiftTransaction()
    {
        try
        {
            System.out.println("completeLiftTransaction");
            CompleteLiftTransactionRequestType oRequest = new CompleteLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("1111-1111");
            oRequest.setFileURI("/test.xml");
            GatewayLiftManagerProxyNoOpImpl oNoOp = new GatewayLiftManagerProxyNoOpImpl();

            CompleteLiftTransactionResponseType oResponse = new CompleteLiftTransactionResponseType();
            oResponse = oNoOp.completeLiftTransaction(oRequest);
            assertNotNull("Response was null and should not have been: ", oResponse);
            assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCompleteLiftTransaction: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCompleteLiftTransaction: " + t.getMessage());
        }
    }

    /**
     * Test of failedLiftTransaction method, of class GatewayLiftManager.
     */
    @Test
    public void testFailedLiftTransaction()
    {
        try
        {
            System.out.println("failedLiftTransaction");
            FailedLiftTransactionRequestType oRequest = new FailedLiftTransactionRequestType();
            oRequest.setRequestKeyGuid("222-222");
            oRequest.setErrorMessage("This is the error message.");
            GatewayLiftManagerProxyNoOpImpl oNoOp = new GatewayLiftManagerProxyNoOpImpl();

            FailedLiftTransactionResponseType oResponse = new FailedLiftTransactionResponseType();
            oResponse = oNoOp.failedLiftTransaction(oRequest);
            assertNotNull("Response was null and should not have been: ", oResponse);
            assertEquals("Status was incorrect: ", "SUCCESS", oResponse.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFailedLiftTransaction: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFailedLiftTransaction: " + t.getMessage());
        }
    }


}