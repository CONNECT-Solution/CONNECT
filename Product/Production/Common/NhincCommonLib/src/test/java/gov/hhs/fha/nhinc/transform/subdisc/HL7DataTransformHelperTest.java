/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import org.hl7.v3.ADExplicit;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author jhoppesc
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
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPN() {
        System.out.println("testConvertENtoPN");
        ENExplicit value = TestHelper.createENexplicit("John", "Hubert", "Jones");
        
        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        TestHelper.assertPNNameEquals(result, "Jones", "John", "Hubert");
    }

    /**
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPNNoLast() {
        System.out.println("testConvertENtoPNNoLast");
        ENExplicit value = TestHelper.createENexplicit("John", "Hubert", null);

        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        TestHelper.assertPNNameEquals(result, null, "John", "Hubert");
    }

    /**
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPNNoMiddle() {
        System.out.println("testConvertENtoPNNoMiddle");
        ENExplicit value = TestHelper.createENexplicit("John", null, "Jones");

        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        TestHelper.assertPNNameEquals(result, "Jones", "John", null);
    }

    /**
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPNNoFirst() {
        System.out.println("testConvertENtoPNNoMiddle");
        ENExplicit value = TestHelper.createENexplicit(null, "Hubert", "Jones");

        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        // Since the NHIN specification say the first Given name is the first name and the second Given name is the middle name
        // in this case the middle name will be treated as the first name.
        TestHelper.assertPNNameEquals(result, "Jones", "Hubert", null);
    }

    /**
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPNLastOnly() {
        System.out.println("testConvertENtoPNLastOnly");
        ENExplicit value = TestHelper.createENexplicit(null, null, "Jones");

        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        // Since the NHIN specification say the first Given name is the first name and the second Given name is the middle name
        // in this case the middle name will be treated as the first name.
        TestHelper.assertPNNameEquals(result, "Jones", null, null);
    }

    /**
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPNFirstOnly() {
        System.out.println("testConvertENtoPNFirstOnly");
        ENExplicit value = TestHelper.createENexplicit("John", null, null);

        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        TestHelper.assertPNNameEquals(result, null, "John", null);
    }

    /**
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPNMiddleOnly() {
        System.out.println("testConvertENtoPNMiddleOnly");
        ENExplicit value = TestHelper.createENexplicit(null, "Hubert", null);

        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        // Since the NHIN specification say the first Given name is the first name and the second Given name is the middle name
        // in this case the middle name will be treated as the first name.
        TestHelper.assertPNNameEquals(result, null, "Hubert", null);
    }

    /**
     * Test of convertENtoPN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertENtoPNNull() {
        System.out.println("testConvertENtoPNNull");
        ENExplicit value = TestHelper.createENexplicit(null, null, null);

        PNExplicit result = HL7DataTransformHelper.convertENtoPN(value);

        TestHelper.assertPNNameEquals(result, null, null, null);
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