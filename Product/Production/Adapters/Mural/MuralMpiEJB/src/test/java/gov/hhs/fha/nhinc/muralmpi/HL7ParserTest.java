/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.muralmpi;

import com.sun.mdm.index.webservice.PatientBean;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
public class HL7ParserTest {

    private static final String DEFAULT_FIRST_NAME = "Gallow";
    private static final String DEFAULT_LAST_NAME = "Younger";
    private static final String DEFAULT_MIDDLE_NAME = "";
    private static final String MURAL_FORMATTED_DOB = "06/27/1999";
    private static final String HL7_FORMATTED_DOB = "19990627";
    private static final String DEFAULT_GENDER = "M";
    private static final String DEFAULT_SSN = "999999999";
    private static final String DEFAULT_LOCAL_ID = "99";
    private static final String DEFAULT_201305 = " " +
            "<PRPA_IN201305UV02 xmlns=\"urn:hl7-org:v3\" xmlns:ns2=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\" xmlns:ns3=\"urn:gov:hhs:fha:nhinc:common:patientcorrelationfacade\" xmlns:ns4=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" ITSVersion=\"XML_1.0\">" +
            "  <id extension=\"-5a3e95b1:11d1fa33d45:-7f9b\" root=\"2.2\"/>" +
            "  <creationTime value=\"20110117112703\"/>" +
            "  <interactionId extension=\"PRPA_IN201305UV02\" root=\"2.16.840.1.113883.1.6\"/>" +
            "  <processingCode code=\"T\"/>" +
            "  <processingModeCode code=\"T\"/>" +
            "  <acceptAckCode code=\"AL\"/>" +
            "  <receiver typeCode=\"RCV\">" +
            "    <device determinerCode=\"INSTANCE\" classCode=\"DEV\">" +
            "      <id root=\"1.1\"/>" +
            "      <asAgent classCode=\"AGNT\">" +
            "        <representedOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">" +
            "          <id root=\"1.1\"/>" +
            "        </representedOrganization>" +
            "      </asAgent>" +
            "    </device>" +
            "  </receiver>" +
            "  <sender typeCode=\"SND\">" +
            "    <device determinerCode=\"INSTANCE\" classCode=\"DEV\">" +
            "      <id root=\"2.2\"/>" +
            "      <asAgent classCode=\"AGENT\">" +
            "        <representedOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">" +
            "          <id root=\"2.2\"/>" +
            "        </representedOrganization>" +
            "      </asAgent>" +
            "    </device>" +
            "  </sender>" +
            "  <controlActProcess moodCode=\"EVN\" classCode=\"CACT\">" +
            "    <code codeSystem=\"2.16.840.1.113883.1.6\" code=\"PRPA_TE201305UV02\"/>" +
            "    <authorOrPerformer typeCode=\"AUT\">" +
            "      <assignedDevice classCode=\"\">" +
            "        <id root=\"2.2\"/>" +
            "      </assignedDevice>" +
            "    </authorOrPerformer>" +
            "    <queryByParameter>" +
            "      <queryId extension=\"-abd3453dcd24wkkks545\" root=\"2.2\"/>" +
            "      <statusCode code=\"new\"/>" +
            "      <responseModalityCode code=\"R\"/>" +
            "      <responsePriorityCode code=\"I\"/>" +
            "      <parameterList>" +
            "        <livingSubjectAdministrativeGender>" +
            "          <value code=\"M\"/>" +
            "          <semanticsText representation=\"TXT\"/>" +
            "        </livingSubjectAdministrativeGender>" +
            "        <livingSubjectBirthTime>" +
            "          <value value=\"19990627\"/>" +
            "          <semanticsText representation=\"TXT\"/>" +
            "        </livingSubjectBirthTime>" +
            "        <livingSubjectId>" +
            "          <value extension=\"500000000\" root=\"2.2\"/>" +
            "          <semanticsText representation=\"TXT\"/>" +
            "        </livingSubjectId>" +
            "        <livingSubjectName>" +
            "          <value>" +
            "            <given partType=\"GIV\">Gallow</given>" +
            "            <family partType=\"FAM\">Younger</family>" +
            "          </value>" +
            "          <semanticsText representation=\"TXT\"/>" +
            "        </livingSubjectName>" +
            "      </parameterList>" +
            "    </queryByParameter>" +
            "  </controlActProcess>" +
            "</PRPA_IN201305UV02>";

    public HL7ParserTest() {
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

    private PatientBean createPatientBean() {
        PatientBean result = new PatientBean();

        result.setDOB(MURAL_FORMATTED_DOB);
        result.setFirstName(DEFAULT_FIRST_NAME);
        result.setLastName(DEFAULT_LAST_NAME);
        result.setMiddleName(DEFAULT_MIDDLE_NAME);
        result.setSSN(DEFAULT_SSN);
        result.setGender(DEFAULT_GENDER);
        return result;
    }

    private PRPAIN201305UV02 createPatientRequest() {
        System.out.println("createPatientRequest");

        PRPAIN201305UV02 result;

        try {
            result = jaxbUnmarshalFromString0(DEFAULT_201305);
        } catch (Exception ex) {
            System.out.println("Exception thrown");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            result = null;
        }

        return result;
    }

    @Test
    public void testCreatePatientPerson() {
        System.out.println("testCreatePatientPerson");

        JAXBElement<PRPAMT201301UV02Person> person;

        PatientBean patient = createPatientBean();

        person = HL7Parser.createPatientPerson(patient);

        assertEquals(1, person.getValue().getName().size());

        PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(person.getValue().getName());

        assertEquals(DEFAULT_LAST_NAME, name.getFamilyName());
        assertEquals(DEFAULT_FIRST_NAME, name.getGivenName());

        assertEquals(HL7_FORMATTED_DOB, person.getValue().getBirthTime().getValue());
        assertEquals(DEFAULT_GENDER, person.getValue().getAdministrativeGenderCode().getCode());
    }

    @Test
    public void testExtraction() {
        System.out.println("testExtraction");

        PRPAIN201305UV02 instance;

        instance = createPatientRequest();
        PatientBean patient = HL7Parser.extractPatientSearchCritieria(instance);

        assertEquals(DEFAULT_FIRST_NAME, patient.getFirstName());
        assertEquals(DEFAULT_LAST_NAME, patient.getLastName());
        assertEquals(DEFAULT_MIDDLE_NAME, patient.getMiddleName());
        assertEquals(MURAL_FORMATTED_DOB, patient.getDOB());
        assertEquals(DEFAULT_GENDER, patient.getGender());
    }

    private org.hl7.v3.PRPAIN201305UV02 jaxbUnmarshalFromString0(String str) throws javax.xml.bind.JAXBException {
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext jc = oHandler.getJAXBContext(org.hl7.v3.PRPAIN201305UV02.class);
        javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (org.hl7.v3.PRPAIN201305UV02) unmarshaller.unmarshal(new StreamSource(new StringReader(str)));
    }

}
