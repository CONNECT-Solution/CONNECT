/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer;

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import nu.xom.Document;
import nux.xom.pool.XOMUtil;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kim
 */
public class PatientDemographicsTest {

    public PatientDemographicsTest() {
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
     * Test of patient info request message.
     */
    @Test
    public void PatientDemographicsMessage() {
        System.out.println("PatientDemographicsMessage");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.198");
        subjectId.setExtension("1234");

        PatientDemographicsPRPAIN201307UV02RequestType query = PDRequestHelper.createPatientDemographicsRequest(subjectId);

        ObjectFactory factory = new ObjectFactory();
        JAXBElement<PatientDemographicsPRPAIN201307UV02RequestType> jaxbReq = factory.createPatientDemographicsPRPAIN201307UV02Request(query);

        // Marshals (serializes) the given JAXB object via the given marshaller into a new XOM Document.
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();

            Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, jaxbReq);
            System.out.println(XOMUtil.toPrettyXML(xmlDocument));
        } catch (Exception e) {
        }
    }

    /**
     * Test of FindPatient method, of class PatientChecker.
     */
    @Test
    public void PatientDemographicsResponse() {
        System.out.println("PatientDemographicsResponse");
        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.198");
        subjectId.setExtension("1234");

        PatientDemographicsPRPAMT201303UV02ResponseType response = PDResponseHelper.createPatientDemographicsResponse(subjectId);

        ObjectFactory factory = new ObjectFactory();
        JAXBElement<PatientDemographicsPRPAMT201303UV02ResponseType> jaxbResp =
                factory.createPatientDemographicsPRPAMT201303UV02Response(response);

        // Marshals (serializes) the given JAXB object via the given marshaller into a new XOM Document.
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller marshaller = jc.createMarshaller();
            Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, jaxbResp);
            System.out.println(XOMUtil.toPrettyXML(xmlDocument));
        } catch (Exception e) {
        }
    }
}
