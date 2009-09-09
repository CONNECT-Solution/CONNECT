package com.example.soap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 *
 * @author Neil Webb
 */
public class RawSoapSenderTest
{

    @Test
    public void testSoapSender()
    {
        System.out.println("Start testSoapSender");
        try
        {
            String endpointAddress = "http://webservice.webserviceshare.com/currencyconverter/rates.asmx";
            String fileName = "C:/Projects/nhinc/Current/Product/Production/Examples/SOAP/RawSoapClientApp/GetRates.xml";
            String soapAction = null;
            String response = RawSoapSender.sendMessage(endpointAddress, fileName, soapAction);
            assertNotNull("Response was null", response);
            System.out.println("Response...");
            System.out.println(response);
        }
        catch(Throwable t)
        {
            System.out.println("Error encountered running testSoapSender test: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
            printCause(t);
        }
        System.out.println("End testSoapSender");
    }

    private void printCause(Throwable t)
    {
        if((t != null) && (t.getCause() != null))
        {
            Throwable cause = t.getCause();
            System.out.println("Cause: " + cause.getMessage());
            cause.printStackTrace();
            printCause(cause);
        }
    }

}