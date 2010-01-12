/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.utils;

import java.io.StringWriter;
import java.util.List;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nux.xom.pool.XOMUtil;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Person;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author kim
 */
public class XMLUtil {

   public static byte[] toCanonicalXMLBytes(POCDMT000040ClinicalDocument pObject) throws JAXBException {
      ObjectFactory factory = new ObjectFactory();
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
//      JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
//      Marshaller marshaller = jc.createMarshaller();
      nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, factory.createClinicalDocument(pObject));
      return XOMUtil.toCanonicalXML(xmlDocument);
   }

   public static String toCanonicalXMLString(POCDMT000040ClinicalDocument pObject) throws JAXBException {
      ObjectFactory factory = new ObjectFactory();
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
//      JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
//      Marshaller marshaller = jc.createMarshaller();
      nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, factory.createClinicalDocument(pObject));
      return new String(XOMUtil.toCanonicalXML(xmlDocument));
   }

   public static String toPrettyXML(POCDMT000040ClinicalDocument pObject) {
      try {
         ObjectFactory factory = new ObjectFactory();
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
//         JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
//         Marshaller marshaller = jc.createMarshaller();
         nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, factory.createClinicalDocument(pObject));
         return XOMUtil.toPrettyXML(xmlDocument);
      } catch (JAXBException ex) {
         return "Unparsable - " + ex.getMessage();
      }
   }

   public static String toPrettyXML(CareRecordQUPCIN043200UV01ResponseType pObject) {
      try {
         ObjectFactory factory = new ObjectFactory();
        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
//         JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
//         Marshaller marshaller = jc.createMarshaller();
         nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(oMarshaller, factory.createCareRecordQUPCIN043200UV01Response(pObject));
         return XOMUtil.toPrettyXML(xmlDocument);
      } catch (JAXBException ex) {
         return "Unparsable - " + ex.getMessage();
      }
   }

   public static Element createElement(String tag) throws Exception {
      DocumentBuilderFactory oDOMFactory = null;
      DocumentBuilder oDOMBuilder = null;
      org.w3c.dom.Document oDOMDocument = null;

      oDOMFactory = DocumentBuilderFactory.newInstance();
      //oDOMFactory.setNamespaceAware(true);
      oDOMBuilder = oDOMFactory.newDocumentBuilder();
      oDOMDocument = oDOMBuilder.newDocument();

      Element oElement = oDOMDocument.createElement(tag);

      return oElement;
   }

   public static void setName(List<ENExplicit> names, POCDMT000040Person person) {
      org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
      PNExplicit personName = factory.createPNExplicit();

      if (names.size() > 0) {
         ENExplicit name = names.get(0);
         for (int i = 0; i < name.getContent().size(); i++) {
            JAXBElement o = (JAXBElement) name.getContent().get(i);
            if (o != null && o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.EnExplicitFamily")) {
               EnExplicitFamily ob = (EnExplicitFamily) o.getValue();
               personName.getContent().add(ob.getContent());
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.EnExplicitGiven")) {
               EnExplicitGiven ob = (EnExplicitGiven) o.getValue();
               personName.getContent().add(ob.getContent());
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.EnExplicitPrefix")) {
               EnExplicitPrefix ob = (EnExplicitPrefix) o.getValue();
               personName.getContent().add(ob.getContent());
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.EnExplicitSuffix")) {
               EnExplicitSuffix ob = (EnExplicitSuffix) o.getValue();
               personName.getContent().add(ob.getContent());
            } else if (o.getValue().getClass().getName().equalsIgnoreCase("java.lang.String")) {
               String ob = (String) o.getValue();
               personName.getContent().add(ob);
            }
         }
      }

      person.getName().add(personName);
   }

   public static org.w3c.dom.Element marshalToElement(javax.xml.bind.JAXBElement o) throws Exception {
      Document doc = null;
      if (o == null) {
         return null;
      }

        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("org.hl7.v3");
        Marshaller oMarshaller = oJaxbContext.createMarshaller();
//      JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
      java.io.StringWriter sw = new StringWriter();
//      Marshaller m = jc.createMarshaller();

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      DocumentBuilder db = dbf.newDocumentBuilder();
      doc = db.newDocument();
      oMarshaller.marshal(o, doc);
      return (doc == null ? null : doc.getDocumentElement());
   }

   public static String toCanonicalXML(JAXBElement a) throws JAXBException {
      JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
      java.io.StringWriter sw = new StringWriter();
      Marshaller marshaller = jc.createMarshaller();
      nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, a);
      return new String(XOMUtil.toCanonicalXML(xmlDocument));
   }
}
