/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.*;
import java.io.Serializable;
import java.util.Iterator;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class HL7DataTransformHelperTest {

    private static class PatientName
    {
        public String FirstName = "";
        public String LastName = "";
        public String MiddleName = "";
        public String Title = "";
        public String Suffix = "";
    }
    public HL7DataTransformHelperTest() {
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
     * Test of CreatePNExplicit method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreatePNExplicit() {
        System.out.println("CreatePNExplicit");
        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        PNExplicit explicitName = HL7DataTransformHelper.CreatePNExplicit(firstExpectedName, lastExpectedName);

        PatientName result = extractName(explicitName);

        assertEquals(result.LastName, lastExpectedName);
        assertEquals(result.FirstName, firstExpectedName);
        assertEquals(result.MiddleName, "");
    }
    @Test
    public void testCreateEnExplicit() {
        System.out.println("testCreateEnExplicit");
        String firstExpectedName = "Joe";
        String lastExpectedName = "Smith";
        String middleExpectedName = "Middle";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        ENExplicit enExplicit = HL7DataTransformHelper.createEnExplicit(firstExpectedName,middleExpectedName, lastExpectedName, expectedTitle, expectedSuffix);

        PatientName result = extractName(enExplicit);

        assertEquals(lastExpectedName,result.LastName);
        assertEquals(firstExpectedName, result.FirstName);
        assertEquals(middleExpectedName, result.MiddleName);
        assertEquals(expectedTitle, result.Title);
     
        assertEquals(expectedSuffix, result.Suffix);
    }

    @Test
    public void testCreatePNExplicit_Middle() {
        System.out.println("testCreatePNExplicit_Middle");
        String firstExpectedName = "Joe";
        String middleExpectedName = "Bob";
        String lastExpectedName = "Smith";
        PNExplicit explicitName = HL7DataTransformHelper.CreatePNExplicit(firstExpectedName, middleExpectedName, lastExpectedName);


        PatientName result = extractName(explicitName);
        
        assertEquals(result.LastName, lastExpectedName);
        assertEquals(result.FirstName, firstExpectedName);
        assertEquals(result.MiddleName, middleExpectedName);
    }
    @Test
    public void testCreatePNExplicit_Title() {
        System.out.println("testCreatePNExplicit_Middle");
        String firstExpectedName = "Joe";
        String middleExpectedName = "Bob";
        String lastExpectedName = "Smith";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";
        
        PNExplicit explicitName = HL7DataTransformHelper.CreatePNExplicit(firstExpectedName, 
                middleExpectedName, lastExpectedName, expectedTitle, expectedSuffix);


        PatientName result = extractName(explicitName);

        assertEquals(lastExpectedName, result.LastName);
        assertEquals(firstExpectedName, result.FirstName);
        assertEquals(middleExpectedName, result.MiddleName);
        assertEquals(expectedTitle, result.Title);
        assertEquals(expectedSuffix, result.Suffix);
    }


    @Test
    public void testCreateConvertPNToEN() {
        System.out.println("testCreatePNExplicit_Middle");

        String firstExpectedName = "Joe";
        String middleExpectedName = "Bob";
        String lastExpectedName = "Smith";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        PNExplicit explicitName = HL7DataTransformHelper.CreatePNExplicit(firstExpectedName,
                middleExpectedName, lastExpectedName, expectedTitle, expectedSuffix);
        
        ENExplicit enExplicit = HL7DataTransformHelper.ConvertPNToEN(explicitName);
        PatientName result = extractName(enExplicit);
        assertEquals(lastExpectedName, result.LastName);
        assertEquals(firstExpectedName, result.FirstName);
        assertEquals(middleExpectedName, result.MiddleName);
        assertEquals(expectedTitle, result.Title);
        assertEquals(expectedSuffix, result.Suffix);
    }

    @Test
    public void testCreateConvertENToPN()
    {
        System.out.println("testCreateConvertENToPN");

        String firstExpectedName = "Joe";
        String middleExpectedName = "Bob";
        String lastExpectedName = "Smith";
        String expectedTitle = "Title";
        String expectedSuffix = "Suffix";

        ENExplicit enExplicit = HL7DataTransformHelper.createEnExplicit(
                firstExpectedName,middleExpectedName, lastExpectedName,
                expectedTitle, expectedSuffix);

        PatientName result = extractName(enExplicit);

        assertEquals(lastExpectedName, result.LastName);
        assertEquals(firstExpectedName, result.FirstName);
        assertEquals(middleExpectedName, result.MiddleName);
        assertEquals(expectedTitle, result.Title);
        assertEquals(expectedSuffix, result.Suffix);

    }
    private static PatientName extractName (PNExplicit name) {
        String nameString = "";
        Boolean hasName = false;
        PatientName result = new PatientName();
        List<Serializable> choice = name.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        EnExplicitFamily familyName = new EnExplicitFamily();
        EnExplicitGiven givenName = new EnExplicitGiven();

        while (iterSerialObjects.hasNext()) {
            Serializable contentItem = iterSerialObjects.next();

            if (contentItem instanceof JAXBElement) {
                JAXBElement oJAXBElement = (JAXBElement) contentItem;
                if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
                    familyName = (EnExplicitFamily) oJAXBElement.getValue();
                    result.LastName = familyName.getContent();
                    hasName = true;
                } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
                    givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    if(result.FirstName == "")
                    {
                        result.FirstName = givenName.getContent();  ;
                    }
                    else
                    {
                        result.MiddleName = givenName.getContent();  ;
                    }
                    hasName = true;
                }
                else if (oJAXBElement.getValue() instanceof EnExplicitPrefix) {
                    EnExplicitPrefix prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                    result.Title = prefix.getContent();
                }else if (oJAXBElement.getValue() instanceof EnExplicitSuffix) {
                    EnExplicitSuffix suffix = (EnExplicitSuffix) oJAXBElement.getValue();
                    result.Suffix = suffix.getContent();
                }
            }
        }

        if (hasName == true) {
            nameString = familyName.getContent();
            System.out.println(nameString);
        }

        return result;
    }

    private static PatientName extractName(ENExplicit name)
    {
        String lastName = "";
        String firstName = "";
        String middleName = "";

        PatientName result = new PatientName();

        for (Object item : name.getContent())
        {
            if (item instanceof JAXBElement)
            {
                JAXBElement oJAXBElement = (JAXBElement) item;

                if (oJAXBElement.getValue() instanceof EnExplicitFamily)
                {
                    EnExplicitFamily familyName = (EnExplicitFamily) oJAXBElement.getValue();
                    result.LastName = familyName.getContent();
                }
                else if (oJAXBElement.getValue() instanceof EnFamily)
                {
                    EnFamily familyName = (EnFamily) oJAXBElement.getValue();
                    result.LastName = familyName.getRepresentation().value();
                }
                else if (oJAXBElement.getValue() instanceof EnExplicitGiven)
                {
                    EnExplicitGiven givenName = (EnExplicitGiven) oJAXBElement.getValue();
                    if(result.FirstName == "")
                    {
                        result.FirstName = givenName.getContent();
                    }
                    else
                    {
                        result.MiddleName = givenName.getContent();
                    }
                }
                else if (oJAXBElement.getValue() instanceof EnGiven)
                {
                    EnGiven givenName = (EnGiven) oJAXBElement.getValue();
                    if(result.FirstName == "")
                    {
                        result.FirstName = givenName.getRepresentation().value();
                    }
                    else
                    {
                        result.MiddleName = givenName.getRepresentation().value();
                    }
                }
                else if (oJAXBElement.getValue() instanceof EnExplicitPrefix)
                {
                    EnExplicitPrefix prefix = (EnExplicitPrefix) oJAXBElement.getValue();
                    result.Title = prefix.getContent();
                }
                else if (oJAXBElement.getValue() instanceof EnExplicitSuffix)
                {
                    EnExplicitSuffix suffix = (EnExplicitSuffix) oJAXBElement.getValue();
                    result.Suffix = suffix.getContent();
                }
            }

        }
        
        return result;
    }
 

}