/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.example;

import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.StringWriter;
import java.io.StringReader;


/**
 *
 * @author westbergl
 */
public class JAXBExamplePropAccess
{
    public static void main (String[] args)
    {
        try
        {
            
            GetPropertyRequestType oRequest = new GetPropertyRequestType();

            oRequest.setPropertyName("Test");
            oRequest.setPropertyFile("TestFile");
            
            // if the element is not a root element, then you have to wrap it with a JAXB element.
            // This is how you do it...
            //-------------------------------------------------------------------------------------
            ObjectFactory oFactory = new ObjectFactory();
            JAXBElement oElement = oFactory.createGetPropertyRequest(oRequest);
            
            // Marshall the JAXB object into XML
            //-----------------------------------
            JAXBContext oContext = JAXBContext.newInstance("gov.hhs.fha.nhinc.common.propertyaccess");
            Marshaller oMarshaller = oContext.createMarshaller();
            StringWriter swXML = new StringWriter();
            oMarshaller.marshal(oElement, swXML);
            String sXML = swXML.toString();
            
            System.out.println("Starting program...");
            System.out.println("Marshalling...");
            System.out.println("XML: ");
            System.out.println(sXML);
            
            // Now take it from XML back into a set of JAXB objects
            //------------------------------------------------------
            Unmarshaller oUnmarshaller = oContext.createUnmarshaller();
            StringReader srXML = new StringReader(sXML);
            JAXBElement oElementOutput = (JAXBElement) oUnmarshaller.unmarshal(srXML);
            GetPropertyRequestType oRequestOutput = (GetPropertyRequestType) oElementOutput.getValue();
            
            System.out.println("");
            System.out.println("Unmarshalling...");
            System.out.println("FileName: " + oRequestOutput.getPropertyFile());
            System.out.println("Property Name:" + oRequestOutput.getPropertyName());
            System.out.println("Program done...");
            System.out.println("");
        }
        catch (Exception e)
        {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
