/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.documentassembly.service;

import gov.hhs.fha.nhinc.assemblymanager.service.DataService;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import nu.xom.Document;
import nux.xom.pool.XOMUtil;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
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
public class DataServiceTest {

   private static final String SERVICE_ENDPOINT = "http://208.75.163.55:8080/CommonDataLayerService/AdapterCommonDataLayer?wsdl";
   DataService instance = null;

   public DataServiceTest() {
   }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   @Before
   public void setUp() {
      instance = new DataService();
   }

   @After
   public void tearDown() {
   }

   /**
    * Test of getPatientDemographics method, of class DataService.
    */
   @Test
   public void testGetPatientDemographics() {
      System.out.println("getPatientDemographics");
      II subjectId = new II();
      subjectId.setExtension("1234");
      //PatientDemographicsPRPAMT201303UVResponseType expResult = null;
      PatientDemographicsPRPAMT201303UV02ResponseType result = instance.getPatientDemographics(subjectId, SERVICE_ENDPOINT);

      ObjectFactory factory = new ObjectFactory();
      JAXBElement<PatientDemographicsPRPAMT201303UV02ResponseType> jaxbResp =
              factory.createPatientDemographicsPRPAMT201303UV02Response(result);

      // Marshals (serializes) the given JAXB object via the given marshaller into a new XOM Document.
      try {
         JAXBContextHandler oHandler = new JAXBContextHandler();
         JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
         Marshaller oMarshaller = oJaxbContext.createMarshaller();
         Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, jaxbResp);
         System.out.println("\nXML:\n" + XOMUtil.toPrettyXML(xmlDocument));
      }
      catch(Exception e ) {
         e.printStackTrace();
      }

      //assertEquals(expResult, result);
   }
}