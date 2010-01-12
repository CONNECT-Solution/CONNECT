/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer;

import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.StaticMedicationsQuery;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import nu.xom.Document;
import nux.xom.pool.XOMUtil;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
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
public class CareRecordTest {

    public CareRecordTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
//   @Test
//   public void getAllergies() {
//      System.out.println("getAllergies");
//
//      II subjectId = new II();
//      subjectId.setRoot("2.16.840.1.113883.3.200");
//      subjectId.setExtension("1234");
//
//      CareRecordREPCMT000400UV01ResponseType response = CareRecordResponseHelper.createAllergiesResponse(subjectId);
//
//      ObjectFactory factory = new ObjectFactory();
//      JAXBElement<CareRecordREPCMT000400UV01ResponseType> jaxbResp =
//              factory.createCareRecordREPCMT000400UV01Response(response);
//
//      // Marshals (serializes) the given JAXB object via the given marshaller into a new XOM Document.
//      try {
//         JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
//         Marshaller marshaller = jc.createMarshaller();
//         Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, jaxbResp);
//         System.out.println(XOMUtil.toPrettyXML(xmlDocument));
//      }
//      catch(Exception e ) {}
//
//   }
    @Test
    public void getMedications() {
        System.out.println("getMedications");

        II subjectId = new II();
        subjectId.setRoot("2.16.840.1.113883.3.200");
        subjectId.setExtension("1234");
        CareRecordQUPCIN043100UV01RequestType request = MedicationsRequestHelper.createMedicationsRequest(subjectId);
        CareRecordQUPCIN043200UV01ResponseType response = StaticMedicationsQuery.createMedicationsResponse(request);

        ObjectFactory factory = new ObjectFactory();
        JAXBElement<CareRecordQUPCIN043200UV01ResponseType> jaxbResp =
                factory.createCareRecordQUPCIN043200UV01Response(response);

        // Marshals (serializes) the given JAXB object via the given marshaller into a new XOM Document.
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();

            Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, jaxbResp);
            System.out.println(XOMUtil.toPrettyXML(xmlDocument));
        } catch (Exception e) {
        }
    }
}
