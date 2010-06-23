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

/**
 *
 * @author jhoppesc
 */
public class HL7DataTransformHelperTest {

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

}