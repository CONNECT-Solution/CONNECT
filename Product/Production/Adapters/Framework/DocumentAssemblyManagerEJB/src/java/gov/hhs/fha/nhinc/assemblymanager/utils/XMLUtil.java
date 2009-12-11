/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.utils;

import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nux.xom.pool.XOMUtil;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitCountry;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.w3c.dom.Element;

/**
 *
 * @author kim
 */
public class XMLUtil {

   public static byte[] toCanonicalXMLBytes(POCDMT000040ClinicalDocument pObject) throws JAXBException {
      ObjectFactory factory = new ObjectFactory();
      JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
      Marshaller marshaller = jc.createMarshaller();
      nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, factory.createClinicalDocument(pObject));
      return XOMUtil.toCanonicalXML(xmlDocument);
   }

   public static String toCanonicalXMLString(POCDMT000040ClinicalDocument pObject) throws JAXBException {
      ObjectFactory factory = new ObjectFactory();
      JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
      Marshaller marshaller = jc.createMarshaller();
      nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, factory.createClinicalDocument(pObject));
      return new String(XOMUtil.toCanonicalXML(xmlDocument));
   }

   public static String toPrettyXML(POCDMT000040ClinicalDocument pObject) {
      try {
         ObjectFactory factory = new ObjectFactory();
         JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
         Marshaller marshaller = jc.createMarshaller();
         nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, factory.createClinicalDocument(pObject));
         return XOMUtil.toPrettyXML(xmlDocument);
      } catch (JAXBException ex) {
         return "Unparsable - " + ex.getMessage();
      }
   }

   public static String toPrettyXML(CareRecordQUPCIN043200UV01ResponseType pObject) {
      try {
         ObjectFactory factory = new ObjectFactory();
         JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
         Marshaller marshaller = jc.createMarshaller();
         nu.xom.Document xmlDocument = XOMUtil.jaxbMarshal(marshaller, factory.createCareRecordQUPCIN043200UV01Response(pObject));
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
      oDOMFactory.setNamespaceAware(true);
      oDOMBuilder = oDOMFactory.newDocumentBuilder();
      oDOMDocument = oDOMBuilder.newDocument();

      Element oElement = oDOMDocument.createElement(tag);

      return oElement;
   }
}
