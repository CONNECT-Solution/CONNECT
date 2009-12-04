/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.TELExplicit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 *
 * @author dunnek
 */
public class HL7QueryParamsTransformsTest {

    public HL7QueryParamsTransformsTest() {
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

    @Test
    public void testcreateTelecom()
    {
        TELExplicit phone;
        String phoneNumber = "7031231234";
        ArrayList<TELExplicit> phoneList = null;

        PRPAMT201306UV02PatientTelecom result  = HL7QueryParamsTransforms.createTelecom(phoneList);

        assertEquals(null, result);
        
        phoneList = new ArrayList<TELExplicit>();

        phone = HL7DataTransformHelper.createTELExplicit(phoneNumber);

        phoneList.add(phone);
        result  = HL7QueryParamsTransforms.createTelecom(phoneList);

        assertNotNull(result);
        assertEquals(1, result.getValue().size());
        assertEquals(phoneNumber, result.getValue().get(0).getValue());

    }
    @Test
    public void testCreateAddr()
    {
        PRPAMT201306UV02PatientAddress result = null;
        ADExplicit addr;
        String street = "123 Main Street";
        String city = "Fairfax";
        String state = "VA";
        String zip = "20120";

        addr = HL7DataTransformHelper.CreateADExplicit(street, city, state, zip);

        ArrayList<ADExplicit> addList = null;

        result = HL7QueryParamsTransforms.createAddress(addList);
        assertNull(result);

        addList = new ArrayList<ADExplicit>();
        addList.add(addr);

        result = HL7QueryParamsTransforms.createAddress(addList);

        assertNotNull(result);
        assertEquals(addr,result.getValue().get(0));

    }
    @Test
    public void testCreateGender()
    {
        PRPAMT201306UV02LivingSubjectAdministrativeGender result;
        String gender = "MALE";
        result = HL7QueryParamsTransforms.createGender(null);

        assertNotNull(result);

        result = HL7QueryParamsTransforms.createGender(gender);

        assertNotNull(result);
        assertEquals(1, result.getValue().size());
        assertEquals(gender, result.getValue().get(0).getCode());
    }
    @Test
    public void testCreateBirthTime()
    {
        PRPAMT201306UV02LivingSubjectBirthTime result;
        String birthTime = "19000101";

        result = HL7QueryParamsTransforms.createBirthTime(null);

        assertNotNull(result);

        result = HL7QueryParamsTransforms.createBirthTime(birthTime);

        assertNotNull(result);
        assertEquals(1, result.getValue().size());
        assertEquals(birthTime, result.getValue().get(0).getValue());
    }
    @Test
    public void testCreateName()
    {
        PRPAMT201306UV02LivingSubjectName result;
        ArrayList<PNExplicit> nameList = null;
        PNExplicit name = new PNExplicit();
        String fName= "John";
        String lName = "Smith";

        result = HL7QueryParamsTransforms.createName(null);

        assertNull(result);

        nameList = new ArrayList<PNExplicit>();

        //Test not null, but zero names
        HL7QueryParamsTransforms.createName(nameList);

        result = HL7QueryParamsTransforms.createName(nameList);
        assertNull(result);

        name = HL7DataTransformHelper.CreatePNExplicit(fName, lName);

        nameList.add(name);

        result = HL7QueryParamsTransforms.createName(nameList);

        assertNotNull(name);
        assertEquals(1, result.getValue().size());
        assertEquals(2, result.getValue().get(0).getContent().size());
        
    }
    @Test
    public void testCreateId()
    {
        PRPAMT201306UV02LivingSubjectId result;
        II id = new II();
        id.setRoot("1.1");
        id.setExtension("1234");
        result = HL7QueryParamsTransforms.createSubjectId(id);

        assertNotNull(result);
        assertEquals(1, result.getValue().size());
        assertEquals(id.getRoot(), result.getValue().get(0).getRoot());
        assertEquals(id.getExtension(), result.getValue().get(0).getExtension());
    }


}