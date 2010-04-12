/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.documentassembly;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.ObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import nu.xom.Document;
import nux.xom.pool.XOMUtil;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author kim
 */
@Ignore //Resolve the test error srikanth
public class AdapterDocumentAssemblyTest {

   // endpoints
   String nhinint01Endpoint =
           "http://nhinint01.asu.edu:8080/DocumentAssembly/AdapterDocumentAssembly?wsdl";
   String nhindev01Endpoint =
           "http://nhindev01.asu.edu:8080/DocumentAssembly/AdapterDocumentAssembly?wsdl";
   private DocumentAssembly instance = null;

   public AdapterDocumentAssemblyTest() {
   }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   @Before
   public void setUp() {
      try {
         URL baseUrl = DocumentAssembly.class.getResource(".");
         instance =
                 new DocumentAssembly(new URL(baseUrl, nhinint01Endpoint),
                 new QName("urn:gov:hhs:fha:nhinc:documentassembly", "DocumentAssembly"));
      } catch (MalformedURLException ex) {
         Logger.getLogger(AdapterDocumentAssemblyTest.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @After
   public void tearDown() {
   }

   /**
    * Test of dynamicAssemblyQuery method, of class AdapterDocumentAssembly.
    */
   @Test
   public void testDynamicAssemblyQuery() {
      System.out.println("dynamicAssemblyQuery");

      DocumentAssemblyPortType port = instance.getDocumentAssemblyPortSoap();

      RespondingGatewayCrossGatewayQueryRequestType dynamicAssemblyQueryRequest = createRequest();
      RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType response =
              port.dynamicAssemblyQuery(dynamicAssemblyQueryRequest);

      ObjectFactory factory = new ObjectFactory();
      JAXBElement<RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType> jaxbResp =
              factory.createRespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequest(response);

      //ObjectFactory factory = new ObjectFactory();
      //JAXBElement<ProvideAndRegisterDocumentSetRequestType> jaxbResp =
      //factory.createProvideAndRegisterDocumentSetRequest(response.getProvideAndRegisterDocumentSetRequest());

      // Marshals (serializes) the given JAXB object via the given marshaller into a new XOM Document.
      try {
         JAXBContext jc = JAXBContext.newInstance("gov.hhs.fha.nhinc.common.nhinccommonadapter");
         Marshaller marshaller = jc.createMarshaller();
         Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, jaxbResp);
         System.out.println(XOMUtil.toPrettyXML(xmlDocument));
      } catch (Exception e) {
      }

      ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocSetReq =
              response.getProvideAndRegisterDocumentSetRequest();
      ProvideAndRegisterDocumentSetRequestType.Document c32DocBytesArray =
              provideAndRegisterDocSetReq.getDocument().get(0);

      String c32Doc = new String(c32DocBytesArray.getValue());
      System.out.println("c32Doc=\n" + c32Doc);
   }

   private RespondingGatewayCrossGatewayQueryRequestType createRequest() {
      RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();

      AdhocQueryRequest adhocQueryReq = new AdhocQueryRequest();
      AdhocQueryType adhocQuery = new AdhocQueryType();

      adhocQuery.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");

      SlotType1 slot1 = new SlotType1();
      slot1.setName("$XDSDocumentEntryPatientId");
      ValueListType slot1ValueList = new ValueListType();
      slot1ValueList.getValue().add("'st3498702^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO'");
      slot1.setValueList(slot1ValueList);
      adhocQuery.getSlot().add(slot1);

      SlotType1 slot2 = new SlotType1();
      slot2.setName("$XDSDocumentEntryClassCode");
      ValueListType slot2ValueList = new ValueListType();
      slot2ValueList.getValue().add("'34133-9'");
      slot2.setValueList(slot2ValueList);
      adhocQuery.getSlot().add(slot2);

      SlotType1 slot3 = new SlotType1();
      slot3.setName("$XDSDocumentEntryClassCodeScheme");
      ValueListType slot3ValueList = new ValueListType();
      slot3ValueList.getValue().add("'LOINC'");
      slot3.setValueList(slot3ValueList);
      adhocQuery.getSlot().add(slot3);

      SlotType1 slot4 = new SlotType1();
      slot4.setName("$XDSDocumentEntryStatus");
      ValueListType slot4ValueList = new ValueListType();
      slot4ValueList.getValue().add("'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Approved'");
      slot4.setValueList(slot4ValueList);
      adhocQuery.getSlot().add(slot4);

      SlotType1 slot5 = new SlotType1();
      slot5.setName("$XDSDocumentEntryEventCodeList");
      ValueListType slot5ValueList = new ValueListType();
      slot5ValueList.getValue().add("(44950' '44955' '44960' '44970' '44979')");
      slot5.setValueList(slot5ValueList);
      adhocQuery.getSlot().add(slot5);

      SlotType1 slot6 = new SlotType1();
      slot6.setName("$uuid");
      ValueListType slot6ValueList = new ValueListType();
      slot6ValueList.getValue().add("('urn:uuid:b2632772-1de7-480d-94b1-c2074d79c871')");
      slot6.setValueList(slot6ValueList);
      adhocQuery.getSlot().add(slot6);

      adhocQueryReq.setAdhocQuery(adhocQuery);

      request.setAdhocQueryRequest(adhocQueryReq);

      return request;
   }
}