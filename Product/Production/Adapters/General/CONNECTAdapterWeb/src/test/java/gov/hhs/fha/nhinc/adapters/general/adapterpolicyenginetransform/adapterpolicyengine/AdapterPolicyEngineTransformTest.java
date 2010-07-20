package gov.hhs.fha.nhinc.adapters.general.adapterpolicyenginetransform.adapterpolicyengine;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLResponseType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.FileReader;
import java.io.StringReader; 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westbergl
 */
public class AdapterPolicyEngineTransformTest
{

    public AdapterPolicyEngineTransformTest()
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
     * This method reads the entire contents of a text file and returns the contents in 
     * a string variable.
     * 
     * @param sFileName The path and location of the text file.
     * @return The contents that was read in.
     */
    public String readTextFile(String sFileName)
    {
        String sText = "";
        FileReader frTextFile = null;
        
        try
        {
            frTextFile = new FileReader(sFileName);
            char caBuf[] = new char[1024];
            int iLen = 0;
            StringBuffer sbText = new StringBuffer();
            while ((iLen = frTextFile.read(caBuf, 0, 1024)) != -1)
            {
                sbText.append(caBuf, 0, iLen);
            }
            
            sText = sbText.toString();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to read text file: " + sFileName + ". Error: " + e.getMessage();
            fail(sErrorMessage);
        }
        finally
        {
            if (frTextFile != null)
            {
                try
                {
                    frTextFile.close();
                }
                catch (Exception e)
                {
                    
                }
            }
        }
        
        return sText;
    }

    /**
     * This method unmarshalls the XML into an AdhocQueryResponse object.
     * 
     * @param sAdhocQueryResponseXML The XML of the AdhocQueryResponse object to be unmarshalled.
     * @return The AdhocQueryResponse object.
     */
    public AdhocQueryResponse unmarshalAdhocQueryResponse(String sAdhocQueryResponseXML)
    {
        AdhocQueryResponse oAdhocQueryResponse = null;
        
        if (sAdhocQueryResponseXML != null)
        {
            try
            {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                StringReader srAdhocQueryResponseXML = new StringReader(sAdhocQueryResponseXML);
                oAdhocQueryResponse = (AdhocQueryResponse)unmarshaller.unmarshal(srAdhocQueryResponseXML);
            }
            catch (Exception e)
            {
                fail("Failed to unmarshal the AdhocQueryResponse document.");
            }
        }
        
        return oAdhocQueryResponse;
    }
    

    /**
     * Test of transformXACMLRequestToCppAQR method, of class AdapterPolicyEngineTransform.
     */
    @Test
    public void testTransformXACMLRequestToCppAQR()
    {
//        System.out.println("transformXACMLRequestToCppAQR");
//        TransformXACMLRequestToCppAQRRequestType transformXACMLRequestToCppAQRRequest = new TransformXACMLRequestToCppAQRRequestType();
//        AdapterPolicyEngineTransform instance = new AdapterPolicyEngineTransform();
//        TransformXACMLRequestToCppAQRResponseType expResult = null;
//        TransformXACMLRequestToCppAQRResponseType result = instance.transformXACMLRequestToCppAQR(transformXACMLRequestToCppAQRRequest);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of transformAQRToCppRDSR method, of class AdapterPolicyEngineTransform.
     */
    @Test
    public void testTransformAQRToCppRDSR()
    {
//        System.out.println("transformAQRToCppRDSR");
//        TransformAQRToCppRDSRRequestType transformAQRToCppRDSRRequest = null;
//        AdapterPolicyEngineTransform instance = new AdapterPolicyEngineTransform();
//        TransformAQRToCppRDSRResponseType expResult = null;
//        TransformAQRToCppRDSRResponseType result = instance.transformAQRToCppRDSR(transformAQRToCppRDSRRequest);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of checkPatientOptIn method, of class AdapterPolicyEngineTransform.
     */
    @Test
    public void testCheckPatientOptIn()
    {
//        System.out.println("checkPatientOptIn");
//        CheckPatientOptInRequestType checkPatientOptInRequest = null;
//        AdapterPolicyEngineTransform instance = new AdapterPolicyEngineTransform();
//        CheckPatientOptInResponseType expResult = null;
//        CheckPatientOptInResponseType result = instance.checkPatientOptIn(checkPatientOptInRequest);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of transformXACMLRequestToAQRForPatientId method, of class AdapterPolicyEngineTransform.
     */
    @Test
    public void testTransformXACMLRequestToAQRForPatientId()
    {
//        System.out.println("transformXACMLRequestToAQRForPatientId");
//        TransformXACMLRequestToAQRForPatientIdRequestType transformXACMLRequestToAQRForPatientIdRequest = null;
//        AdapterPolicyEngineTransform instance = new AdapterPolicyEngineTransform();
//        TransformXACMLRequestToAQRForPatientIdResponseType expResult = null;
//        TransformXACMLRequestToAQRForPatientIdResponseType result = instance.transformXACMLRequestToAQRForPatientId(transformXACMLRequestToAQRForPatientIdRequest);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of transformPatientIdAQRToCppXACML method, of class AdapterPolicyEngineTransform.
     */
    @Test
    public void testTransformPatientIdAQRToCppXACML()
    {
        System.out.println("transformPatientIdAQRToCppXACML");
        
        // Get the example text.
        //----------------------
        String sXML = readTextFile("SampleAdhocQueryResponse.xml");
        AdhocQueryResponse oAQR = unmarshalAdhocQueryResponse(sXML);
        
        TransformPatientIdAQRToCppXACMLRequestType oInput = new TransformPatientIdAQRToCppXACMLRequestType();
        oInput.setAdhocQueryResponse(oAQR);
        

        AdapterPolicyEngineTransform instance = new AdapterPolicyEngineTransform();
        TransformPatientIdAQRToCppXACMLResponseType oResponse = instance.transformPatientIdAQRToCppXACML(oInput);

    }
}