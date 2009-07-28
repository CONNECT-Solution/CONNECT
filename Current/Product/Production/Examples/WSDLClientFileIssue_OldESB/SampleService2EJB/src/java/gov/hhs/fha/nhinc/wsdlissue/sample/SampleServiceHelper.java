package gov.hhs.fha.nhinc.wsdlissue.sample;

import javax.xml.ws.BindingProvider;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.FindBusiness;
import org.uddi.v3_service.UDDIService;
import org.uddi.v3_service.UDDIInquiryPortType;


/**
 *
 * @author westbergl
 */
public class SampleServiceHelper extends Thread
{
    // URL for the UDDI Server.
    private static String m_sUDDIInquiryEndpointURL = "http://12.54.145.57:8080/uddi/services/inquiry";         
    
    /**
     * This method retrieves the port for the UDDI server with the correct endpoint.
     * 
     * @return 
     */
    private static UDDIInquiryPortType getUDDIInquiryWebService()
        throws Exception
    {
        UDDIInquiryPortType oPort = null;
        
        try
        {
            UDDIService oService = new UDDIService();
            oPort = oService.getUDDIInquiryPort();

            // Need to load in the correct UDDI endpoint URL address.
            //--------------------------------------------------------
            ((BindingProvider)oPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, m_sUDDIInquiryEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve the UDDI Inquiry Web Service port.  Error: " + e.getMessage();
            System.out.println(sErrorMessage);
            throw new Exception(sErrorMessage, e);
        }
        
        return oPort;
    }

    /**
     * This method retrieves the business entities from the UDDI server.
     * It does not retrieve the services or bindings.  They are retrieved
     * on other calls.  This only retrieves the business information.
     * 
     * @return the BusinessEntities retrieved from the UDDI server.
     * @throws Exception
     */
    private static void retrieveBusinessesInfoFromUDDI()
        throws Exception
    {
        System.out.println("Retrieving business entities from UDDI using find_business web service call.");
        
        BusinessList oBusinessList = null;
        
        try
        {
            UDDIInquiryPortType oPort = getUDDIInquiryWebService();
            
            // Make the call...
            //-----------------
            FindBusiness oSearchParams = new FindBusiness();
            oSearchParams.setMaxRows(100);
            oBusinessList = oPort.findBusiness(oSearchParams);
            
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to call 'find_business' web service on the NHIN UDDI server.  Errror: " +
                                   e.getMessage();
            System.out.println(sErrorMessage);
            throw new Exception(sErrorMessage, e);
        }
        
    }
    
    /**
     * This method is used to retrieve the data from the UDDI server.  The
     * data is returned in the form of CMBusinessEntities.
     * 
     * 
     * @throws Exception 
     */
    public static void retrieveFromUDDIServer()
        throws Exception
    {
        // Retrieve the high level business information...
        //-----------------------------------------------
        retrieveBusinessesInfoFromUDDI();
    }
    
    @Override
    public void run()
    {
        try
        {
            retrieveFromUDDIServer();
        }
        catch (Exception e)
        {
            System.out.println("Run Method: Unexpected Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }    
    
    public static void main (String[] args)
    {
        System.out.println("Starting test.");
        try
        {
            retrieveFromUDDIServer();
        }
        catch (Exception e)
        {
            System.out.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("End of test.");
    }
    
}
