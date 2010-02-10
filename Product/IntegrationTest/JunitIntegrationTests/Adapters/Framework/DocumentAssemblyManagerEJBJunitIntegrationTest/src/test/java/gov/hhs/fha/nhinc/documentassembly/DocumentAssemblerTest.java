/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.documentassembly;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyManager;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import nu.xom.Document;
import nux.xom.pool.XOMUtil;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitCountry;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kim
 */
public class DocumentAssemblerTest {

   private final static String DOC_TYPE = "34133-9";
   private final static String OID = "2.16.840.1.113883.3.198";
   private final static String SUBJECT_ID = "2.16.840.1.113883.3.198";
   AssemblyManager assembler = null;
   II subject = null;

   public DocumentAssemblerTest() {
   }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   @Before
   public void setUp() {
      subject = new II();
      subject.setRoot(OID);
      subject.setExtension(SUBJECT_ID);

      assembler = new AssemblyManager();
   }

   @After
   public void tearDown() {
   }

   /**
    * Test of getDocument method, of class DocumentAssembler.
    */
   @Test
   public void testGetDocument() throws Exception {
      System.out.println("getDocument");
      String docType = DOC_TYPE;
      String patientId = "1234";

      //POCDMT000040ClinicalDocument expResult = null;
      POCDMT000040ClinicalDocument result = assembler.getDocument(docType, patientId);
      assertNotNull(result);

      POCDMT000040RecordTarget subject = result.getRecordTarget().get(0);
      POCDMT000040PatientRole ptRole = subject.getPatientRole();

      List<ADExplicit> addresses = ptRole.getAddr();
      if (addresses.size() > 0) {
         ADExplicit address = addresses.get(0);
         String streetname = "";
         String city = "";
         String state = "";
         String postalCode = "";
         String country = "";
         for (int i = 0; i < address.getContent().size(); i++) {
            JAXBElement o = (JAXBElement) address.getContent().get(i);
            //EnExplicitFamily ob = (EnExplicitFamily) o.getValue();
            if (o != null && o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitStreetAddressLine")) {
               AdxpExplicitStreetAddressLine ob = (AdxpExplicitStreetAddressLine) o.getValue();
               System.out.println("streetname = " + ob.getContent());
            }
            else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitCity")) {
               AdxpExplicitCity ob = (AdxpExplicitCity) o.getValue();
               System.out.println("city = " + ob.getContent());
            }
            else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitState")) {
               AdxpExplicitState ob = (AdxpExplicitState) o.getValue();
               System.out.println("state = " + ob.getContent());
            }
            else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitPostalCode")) {
               AdxpExplicitPostalCode ob = (AdxpExplicitPostalCode) o.getValue();
               System.out.println("postalCode = " + ob.getContent());
            }
            else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.AdxpExplicitCountry")) {
               AdxpExplicitCountry ob = (AdxpExplicitCountry) o.getValue();
               System.out.println("country = " + ob.getContent());
            }
         }
      }
  
      POCDMT000040Patient patient = ptRole.getPatient();

      List<PNExplicit> names = patient.getName();
      if (names.size() > 0) {
         PNExplicit name = names.get(0);
         String lastName = "";
         String firstName = "";
         String middleInitials = "";
         for (int i = 0; i < name.getContent().size(); i++) {
            JAXBElement o = (JAXBElement) name.getContent().get(i);
            //EnExplicitFamily ob = (EnExplicitFamily) o.getValue();
            if (o != null && o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.EnExplicitFamily")) {
               EnExplicitFamily ob = (EnExplicitFamily) o.getValue();
               lastName = ob.getContent();
            }
            else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.EnExplicitGiven")) {
               EnExplicitGiven ob = (EnExplicitGiven) o.getValue();
               firstName = ob.getContent();
            }
         }
      }

      //JAXBElement o = (JAXBElement) name.getContent().get(0);
      //EnExplicitFamily ob = (EnExplicitFamily) o.getValue();
      //if (ob.getClass().getName().equalsIgnoreCase("org.hl7.v3.EnExplicitFamily")) {
      //   lastname = ob.getContent();
      //}

      ObjectFactory factory = new ObjectFactory();
      JAXBElement<POCDMT000040ClinicalDocument> clinDoc = factory.createClinicalDocument(result);

      // Marshals (serializes) the given JAXB object via the given marshaller into a new XOM Document.
      try {
         JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
         Marshaller marshaller = jc.createMarshaller();
         Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, clinDoc);
         System.out.println("\nXML:\n" + XOMUtil.toPrettyXML(xmlDocument));
      } catch (Exception e) {
         e.printStackTrace();
      }
      }
   /**
    * Test of getDocument method, of class DocumentAssembler.
    */
//   @Test
//   public void testGetDocumentAsString() throws Exception {
//      System.out.println("getDocumentAsString");
//      String docType = DOC_TYPE;
//      String patientId = "1234";
//
//      String result = assembler.getDocumentAsString(docType, patientId);
//      assertNotNull(result);
//
//      System.out.println("\nXML:\n" + result);
//   }
}