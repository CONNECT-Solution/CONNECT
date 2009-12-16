/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.muralmpi;

import com.sun.mdm.index.webservice.PatientBean;
import com.sun.mdm.index.webservice.SearchPatientResult;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.EnExplicitFamily;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import javax.xml.bind.JAXBContext;

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
      "<PRPA_IN201305UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:ns2=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\" xmlns:ns3=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">" +
      "   <id extension=\"-f6a0b28:121a183116b:-7f1a\" root=\"1.1\"/>                                                                                                                         " +
      "   <creationTime value=\"200962153243\"/>                                                                                                                                              " +
      "   <interactionId extension=\"PRPA_IN201305UV\" root=\"2.16.840.1.113883.1.6\"/>                                                                                                       " +
      "   <processingCode code=\"P\"/>                                                                                                                                                        " +
      "   <processingModeCode code=\"R\"/>                                                                                                                                                    " +
      "   <acceptAckCode code=\"AL\"/>                                                                                                                                                        " +
      "   <receiver typeCode=\"RCV\">                                                                                                                                                         " +
      "      <device determinerCode=\"INSTANCE\">                                                                                                                                             " +
      "         <id root=\"1.1\"/>                                                                                                                                                            " +
      "      </device>                                                                                                                                                                        " +
      "   </receiver>                                                                                                                                                                         " +
      "   <sender typeCode=\"SND\">                                                                                                                                                           " +
      "      <device determinerCode=\"INSTANCE\">                                                                                                                                             " +
      "         <id root=\"1.1\"/>                                                                                                                                                            " +
      "      </device>                                                                                                                                                                        " +
      "   </sender>                                                                                                                                                                           " +
      "   <controlActProcess moodCode=\"EVN\">                                                                                                                                                " +
      "      <code codeSystem=\"2.16.840.1.113883.1.6\" code=\"PRPA_TE201305UV\"/>                                                                                                            " +
      "      <queryByParameter>                                                                                                                                                               " +
      "         <queryId extension=\"-f6a0b28:121a183116b:-7f19\" root=\"1.1\"/>                                                                                                              " +
      "         <statusCode code=\"new\"/>                                                                                                                                                    " +
      "         <parameterList>                                                                                                                                                               " +
      "            <livingSubjectAdministrativeGender>                                                                                                                                        " +
      "               <value code=\"M\"/>                                                                                                                                                     " +
      "               <semanticsText/>                                                                                                                                                        " +
      "            </livingSubjectAdministrativeGender>                                                                                                                                       " +
      "            <livingSubjectBirthTime>                                                                                                                                                   " +
      "               <value value=\"19990627\"/>                                                                                                                                             " +
      "               <semanticsText/>                                                                                                                                                        " +
      "            </livingSubjectBirthTime>                                                                                                                                                  " +
      "            <livingSubjectId>                                                                                                                                                          " +
      "               <value extension=\"500000000\" root=\"2.2\"/>                                                                                                                           " +
      "               <semanticsText/>                                                                                                                                                        " +
      "            </livingSubjectId>                                                                                                                                                         " +
      "            <livingSubjectName>                                                                                                                                                        " +
      "               <value>                                                                                                                                                                 " +
      "                  <family partType=\"FAM\">Younger</family>                                                                                                                            " +
      "                  <given partType=\"GIV\">Gallow</given>                                                                                                                               " +
      "               </value>                                                                                                                                                                " +
      "               <semanticsText/>                                                                                                                                                        " +
      "            </livingSubjectName>                                                                                                                                                       " +
      "         </parameterList>                                                                                                                                                              " +
      "      </queryByParameter>                                                                                                                                                              " +
      "   </controlActProcess>                                                                                                                                                                " +
      "</PRPA_IN201305UV02>                                                                                                                                                                     ";

    
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
    private PatientBean createPatientBean()
    {
        PatientBean result = new PatientBean();

        result.setDOB(MURAL_FORMATTED_DOB);
        result.setFirstName(DEFAULT_FIRST_NAME);
        result.setLastName(DEFAULT_LAST_NAME);
        result.setMiddleName(DEFAULT_MIDDLE_NAME);
        result.setSSN(DEFAULT_SSN);
        result.setGender(DEFAULT_GENDER);
        return result;
    }
    private PRPAIN201305UV02 createPatientRequest()
    {
        PRPAIN201305UV02 result;

        try
        {
            result = jaxbUnmarshalFromString0(DEFAULT_201305);
        }
        catch (Exception ex)
        {
            result = null;
        }
       
        return result;
    }


    @Test
    public void testCreatePatientPerson()
    {
        PRPAIN201305UV02 hl7Req;
        JAXBElement<PRPAMT201301UV02Person> person;

        System.out.println("testCreatePatientPerson");
        hl7Req = createPatientRequest();
        
        PatientBean patient = createPatientBean();


        person = HL7Parser.createPatientPerson(patient);

        assertEquals(1, person.getValue().getName().size() );


        PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(person.getValue().getName());

        assertEquals(DEFAULT_LAST_NAME, name.getFamilyName());
        assertEquals(DEFAULT_FIRST_NAME, name.getGivenName());

        assertEquals(HL7_FORMATTED_DOB, person.getValue().getBirthTime().getValue());
        assertEquals(DEFAULT_GENDER,person.getValue().getAdministrativeGenderCode().getCode());
        
    }

        @Test
    public void testExtraction()
    {
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
        org.hl7.v3.PRPAIN201305UV02 ret = null;
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext jc = oHandler.getJAXBContext(org.hl7.v3.PRPAIN201305UV02.class);
        //javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(org.hl7.v3.PRPAIN201305UV02.class.getPackage().getName());
        javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
        ret = (org.hl7.v3.PRPAIN201305UV02) unmarshaller.unmarshal(new java.io.StringReader(str));
        //NOI18N
        return ret;
    }

}