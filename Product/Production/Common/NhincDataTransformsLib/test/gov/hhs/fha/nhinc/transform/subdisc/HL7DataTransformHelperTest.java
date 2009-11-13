/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7DataTransformHelperTest {

    private static Log log = LogFactory.getLog(HL7DataTransformHelperTest.class);

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
     * Test of IIFactory method, of class HL7DataTransformHelper.
     * root only
     */
    @Test
    public void testIIFactory_1arg() {
        log.info("testIIFactory_1arg");

        String root = "2.16.840.1.113883.3.200";
        II result = HL7DataTransformHelper.IIFactory(root);
        assertEquals(result.getRoot(), "2.16.840.1.113883.3.200");
        assertNull(result.getExtension());
        assertNull(result.getAssigningAuthorityName());
    }

    /**
     * Test of IIFactory method, of class HL7DataTransformHelper.
     * 1 arg, no input
     */
    @Test
    public void testIIFactory_1arg_NoInput() {
        log.info("testIIFactory_1arg_NoInput");

        II result = HL7DataTransformHelper.IIFactory(null);
        assertNull(result.getRoot());
        assertNull(result.getExtension());
        assertNull(result.getAssigningAuthorityName());
    }

    /**
     * Test of IIFactory method, of class HL7DataTransformHelper.
     * root and extension
     */
    @Test
    public void testIIFactory_2args() {
        log.info("testIIFactory_2args");

        String root = "2.16.840.1.113883.3.200";
        String extension = "1234567890";

        II result = HL7DataTransformHelper.IIFactory(root, extension);
        assertEquals(result.getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getExtension(), "1234567890");
        assertNull(result.getAssigningAuthorityName());
    }

    /**
     * Test of IIFactory method, of class HL7DataTransformHelper.
     * 2 args, no input
     */
    @Test
    public void testIIFactory_2args_NoInput() {
        log.info("testIIFactory_2args_NoInput");

        II result = HL7DataTransformHelper.IIFactory(null, null);
        assertNull(result.getRoot());
        assertNull(result.getExtension());
        assertNull(result.getAssigningAuthorityName());
    }

    /**
     * Test of IIFactory method, of class HL7DataTransformHelper.
     * root, extension, and assigning authority name
     */
    @Test
    public void testIIFactory_3args() {
        log.info("testIIFactory_3args");
        String root = "2.16.840.1.113883.3.200";
        String extension = "1234567890";
        String assigningAuthorityName = "Social Security Administration";

        II result = HL7DataTransformHelper.IIFactory(root, extension, assigningAuthorityName);
        assertEquals(result.getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getExtension(), "1234567890");
        assertEquals(result.getAssigningAuthorityName(), "Social Security Administration");
    }

    /**
     * Test of IIFactory method, of class HL7DataTransformHelper.
     * root, extension, and assigning authority name, no inputs
     */
    @Test
    public void testIIFactory_3args_NoInput() {
        log.info("testIIFactory_3args_NoInput");

        II result = HL7DataTransformHelper.IIFactory(null, null, null);
        assertNull(result.getRoot());
        assertNull(result.getExtension());
        assertNull(result.getAssigningAuthorityName());
    }

    /**
     * Test of CSFactory method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCSFactory() {
        log.info("testCSFactory");

        String code = "R";
        CS result = HL7DataTransformHelper.CSFactory(code);
        assertEquals(result.getCode(), "R");
    }

    /**
     * Test of CSFactory method, of class HL7DataTransformHelper.
     * with no input
     */
    @Test
    public void testCSFactory_NoInput() {
        log.info("testCSFactory_NoInput");

        CS result = HL7DataTransformHelper.CSFactory(null);
        assertNull(result.getCode());
    }

    /**
     * Test of CEFactory method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCEFactory() {
        log.info("testCEFactory");

        String code = "T";
        CE result = HL7DataTransformHelper.CEFactory(code);
        assertEquals(result.getCode(), "T");
    }

    /**
     * Test of CEFactory method, of class HL7DataTransformHelper.
     * with no input
     */
    @Test
    public void testCEFactory_NoInput() {
        log.info("testCEFactory_NoInput");

        CE result = HL7DataTransformHelper.CEFactory(null);
        assertNull(result.getCode());
    }

    /**
     * Test of CDFactory method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCDFactory_1arg() {
        log.info("testCDFactory_1arg");

        String code = "R";
        CD result = HL7DataTransformHelper.CDFactory(code);
        assertEquals(result.getCode(), "R");
        assertNull(result.getCodeSystem());
    }

    /**
     * Test of CDFactory method, of class HL7DataTransformHelper.
     * with no input
     */
    @Test
    public void testCDFactory_1arg_NoInput() {
        log.info("testCDFactory_1arg_NoInput");

        CD result = HL7DataTransformHelper.CDFactory(null);
        assertNull(result.getCode());
        assertNull(result.getCodeSystem());
    }

    /**
     * Test of CDFactory method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCDFactory_2args() {
        log.info("testCDFactory_2args");

        String code = "R";
        String codeSystem = "1.1.333";
        CD result = HL7DataTransformHelper.CDFactory(code, codeSystem);
        assertEquals(result.getCode(), "R");
        assertEquals(result.getCodeSystem(), "1.1.333");
    }

    /**
     * Test of CDFactory method, of class HL7DataTransformHelper.
     * with no input
     */
    @Test
    public void testCDFactory_2args_NoInput() {
        log.info("testCDFactory_2args_NoInput");

        CD result = HL7DataTransformHelper.CDFactory(null, null);
        assertNull(result.getCode());
        assertNull(result.getCodeSystem());
    }
    
    /**
     * Test of testTSExplicitFactory method, of class HL7DataTransformHelper.
     */
    @Test
    public void testTSExplicitFactory () {
        log.info("testTSExplicitFactory");
        
        TSExplicit result = HL7DataTransformHelper.TSExplicitFactory("M");
        
        assertEquals ("M", result.getValue());
    }
    
    /**
     * Test of testTSExplicitFactory method, of class HL7DataTransformHelper.
     * no input
     */
    @Test
    public void testTSExplicitFactory_NoInput () {
        log.info("testTSExplicitFactory_NoInput");
        
        TSExplicit result = HL7DataTransformHelper.TSExplicitFactory(null);
        
        assertNull (result.getValue());
    }

    /**
     * Test of CreationTimeFactory method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreationTimeFactory() {
        log.info("CreationTimeFactory");
        TSExplicit result = HL7DataTransformHelper.CreationTimeFactory();
        assertNotNull(result);
    }

    /**
     * Test of ConvertPNToEN method, of class HL7DataTransformHelper.
     */
    @Test
    public void testConvertPNToEN() {
        log.info("testConvertPNToEN");
        
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit pnName = (PNExplicit) (factory.createPNExplicit());
        List pnNamelist = pnName.getContent();

        // Setup input
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setPartType("FAM");
        familyName.setContent("Smith");
        pnNamelist.add(factory.createENExplicitFamily(familyName));
        EnExplicitGiven givenName = new EnExplicitGiven();
        givenName.setPartType("GIV");
        givenName.setContent("Hugo");
        pnNamelist.add(factory.createENExplicitGiven(givenName));

        ENExplicit enName = HL7DataTransformHelper.ConvertPNToEN(pnName);

        // Parse out output
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();
        
        TestHelper.assertNameEquals(iterSerialObjects, familyName, givenName);
    }
    
    /**
     * Test of ConvertPNToEN method, of class HL7DataTransformHelper.
     * Only the family name is specified.
     */
    @Test
    public void testConvertPNToEN_OnlyFamily() {
        log.info("testConvertPNToEN_OnlyFamily");
        
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit pnName = (PNExplicit) (factory.createPNExplicit());
        List pnNamelist = pnName.getContent();

        // Setup input
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setPartType("FAM");
        familyName.setContent("Smith");
        pnNamelist.add(factory.createENExplicitFamily(familyName));
        
        ENExplicit enName = HL7DataTransformHelper.ConvertPNToEN(pnName);

        // Parse out output
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();
        
        TestHelper.assertNameEquals(iterSerialObjects, familyName, null);
    }
    
   /**
     * Test of ConvertPNToEN method, of class HL7DataTransformHelper.
     * Only the given name is specified.
     */
    @Test
    public void testConvertPNToEN_OnlyGiven() {
        log.info("testConvertPNToEN_OnlyGiven");
        
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit pnName = (PNExplicit) (factory.createPNExplicit());
        List pnNamelist = pnName.getContent();

        // Setup input
        EnExplicitGiven givenName = new EnExplicitGiven();
        givenName.setPartType("GIV");
        givenName.setContent("Hugo");
        pnNamelist.add(factory.createENExplicitGiven(givenName));
        
        ENExplicit enName = HL7DataTransformHelper.ConvertPNToEN(pnName);

        // Parse out output
        List<Serializable> choice = enName.getContent();
        Iterator<Serializable> iterSerialObjects = choice.iterator();

        TestHelper.assertNameEquals(iterSerialObjects, null, givenName);
    }
    
    /**
     * Test of createPNExplicit method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreatePNExplicit () {
        log.info("testCreatePNExplicit");
        
        String lastName = "Johnson";
        String firstName = "Frank";
        
        PNExplicit result = HL7DataTransformHelper.CreatePNExplicit(firstName, lastName);
        
        TestHelper.assertPNNameEquals (result, lastName, firstName);
    }
    
    /**
     * Test of createPNExplicit method, of class HL7DataTransformHelper.
     * only family name
     */
    @Test
    public void testCreatePNExplicit_OnlyFamily () {
        log.info("testCreatePNExplicit_OnlyFamily");
        
        String lastName = "Johnson";
        
        PNExplicit result = HL7DataTransformHelper.CreatePNExplicit(null, lastName);
        
        TestHelper.assertPNNameEquals (result, lastName, null);
    }
    
    /**
     * Test of CreatePNExplicit method, of class HL7DataTransformHelper.
     * only given name
     */
    @Test
    public void testCreatePNExplicit_OnlyGiven () {
        log.info("testCreatePNExplicit_OnlyGiven");
        
        String firstName = "Frank";
        
        PNExplicit result = HL7DataTransformHelper.CreatePNExplicit(firstName, null);
        
        TestHelper.assertPNNameEquals (result, null, firstName);
    }
    @Test
    public void testConvertENtoPN()
    {
        log.info("testCreatePNExplicit_OnlyGiven");

        String firstName = "Frank";
        String lastName= "Smith";

        PNExplicit value = HL7DataTransformHelper.CreatePNExplicit(firstName, lastName);
        ENExplicit result = HL7DataTransformHelper.ConvertPNToEN(value);

        TestHelper.assertENNameEquals(result, lastName, firstName);
    }
    @Test
    public void testCreateADExplict()
    {
        ADExplicit addr;
        String street = "123 Main Street";
        String city = "Fairfax";
        String state = "VA";
        String zip = "20120";

        addr = HL7DataTransformHelper.CreateADExplicit(street, city, state, zip);

        assertEquals(4, addr.getUse().size());
        assertEquals(false, addr.isIsNotOrdered());
        assertEquals(street, addr.getUse().get(0));
        assertEquals(city, addr.getUse().get(1));
        assertEquals(state, addr.getUse().get(2));
        assertEquals(zip, addr.getUse().get(3));


    }

    @Test
    public void testCreateTelExplicit()
    {
        TELExplicit phone;

        String phoneNumber = "7031231234";

        phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);

        assertEquals(phoneNumber, phone.getValue());
    }
}