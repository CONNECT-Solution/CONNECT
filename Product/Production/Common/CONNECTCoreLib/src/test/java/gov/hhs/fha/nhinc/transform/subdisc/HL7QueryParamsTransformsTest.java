/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
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