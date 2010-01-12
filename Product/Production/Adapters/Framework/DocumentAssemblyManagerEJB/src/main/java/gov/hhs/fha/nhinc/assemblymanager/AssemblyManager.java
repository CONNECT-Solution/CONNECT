/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager;

import gov.hhs.fha.nhinc.assemblymanager.builder.CDADocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.dao.DocumentTypeDAO;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DocumentType;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.POCDMT000040ClinicalDocument;

/**
 *
 * @author kim
 */
public class AssemblyManager {

   private static Log log = LogFactory.getLog(AssemblyManager.class);

   private String documentType = "";
   
   public POCDMT000040ClinicalDocument getDocument(String docType, String patientId) throws InvalidTypeException, InvalidIdentifierException, DocumentBuilderException {

      // check for valid document type
      if (docType == null) {
         throw new InvalidTypeException("Must specify a document type.");
      }
      if (!DocumentTypeDAO.getInstance().isValidDocumentType(docType)) {
         log.debug("Requested document type \"" + docType + "\" is not supported!");
         throw new InvalidTypeException("Requested document type \"" + docType + "\" is not supported.");
      }

      documentType = docType;

      // check for valid patient identifier parameter
      if (patientId == null || patientId.length() < 1) {
         log.debug("Patient identifier \"" + patientId + "\" is invalid!");
         throw new InvalidIdentifierException("Identifier is invalid.");
      }

      log.debug("Requesting document of type=" + docType + " for patient=" + patientId);

      // get a document builder
      CDADocumentBuilder docBuilder = AssemblerFactory.cdaBuilder(docType);

      docBuilder.setPatientId(patientId);
      POCDMT000040ClinicalDocument clinDoc = docBuilder.build();

      log.debug("NEW DOCUMENT=" + XMLUtil.toPrettyXML(clinDoc));
      log.debug("Request for document=\"" + docBuilder.getDocumentType().getDisplayName() +
              "\" for patient[" + patientId + "] completed!");
      return clinDoc;
   }

   public String getDocumentAsString(String docType, String patientId) throws InvalidTypeException, InvalidIdentifierException, DocumentBuilderException {

      POCDMT000040ClinicalDocument clinDoc = getDocument(docType, patientId);

      try {
         return XMLUtil.toCanonicalXMLString(clinDoc);
      } catch (JAXBException ex) {
         log.error(ex);
         throw new DocumentBuilderException("Failed to convert POCDMT000040ClinicalDocument object to String.");
      }
   }

   public byte[] getDocumentAsBytes(String docType, String patientId) throws InvalidTypeException, InvalidIdentifierException, DocumentBuilderException {

      POCDMT000040ClinicalDocument clinDoc = getDocument(docType, patientId);
      try {
         return XMLUtil.toCanonicalXMLBytes(clinDoc);
      } catch (JAXBException ex) {
         log.error(ex);
         throw new DocumentBuilderException("Failed to convert POCDMT000040ClinicalDocument object to String.");
      }
   }

   public DocumentType getDocumentType(String docType) {
      DocumentType d = null;
      try {
         d = DocumentTypeDAO.getInstance().getDocumentType(docType);
      } catch (Exception ex) {
         log.error(ex);
      }

      return d;
   }

   public List<DocumentType> getDocumentTypes() {
      List<DocumentType> docTypes = new ArrayList<DocumentType>();
      try {
         docTypes = DocumentTypeDAO.getInstance().getDocumentTypes();
      } catch (Exception ex) {
         log.error(ex);
      }

      return docTypes;
   }

   public static String getProperty(String name) {
      if (name != null) {
         return PropertiesDAO.getInstance().getAttributeValue(name, true);
      }

      return "";
   }

   /**
    * Returns a serialized copy of this POCDMT000040ClinicalDocument object.
    * @param pObject
    * @return
    * @throws javax.xml.bind.JAXBException
    */
   public String serializeCDAContentToXMLString(POCDMT000040ClinicalDocument pObject) throws JAXBException {

      if (pObject == null) {
         throw new JAXBException("POCDMT000040ClinicalDocument object cannot be NULL!");
      }

      JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
      java.io.StringWriter sw = new StringWriter();
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
      marshaller.marshal(factory.createClinicalDocument(pObject), sw);

      String clinDocString = sw.toString();

      // add stylesheet reference:
      //   <?xml-stylesheet type="text/xsl" href="CCD.xsl"?>
      String replacementString =
              "<?xml-stylesheet type=\"text/xsl\" href=\"" + getStyleSheet() + "\"?>" +
              System.getProperty("line.separator") + "<ClinicalDocument";

      clinDocString = clinDocString.replaceFirst("<ClinicalDocument", replacementString);

      // remove empty xmlns="" which is an issue for stylesheet
      clinDocString = clinDocString.replaceAll("xmlns=\"\"", "");

      // quick fix for getting the <content> tag correctly shown in XML
      clinDocString = clinDocString.replaceAll("&lt;content", "<content");
      clinDocString = clinDocString.replaceAll("&lt;/content&gt;", "</content>");
      clinDocString = clinDocString.replaceAll("\"&gt;", "\">");

      // quick fix for getting the <paragraph> tag correctly shown in XML
      clinDocString = clinDocString.replaceAll("&lt;paragraph", "<paragraph");
      clinDocString = clinDocString.replaceAll("&lt;/paragraph&gt;", "</paragraph>");
      clinDocString = clinDocString.replaceAll("\"&gt;", "\">");

      return clinDocString;
   }

   public byte[] serializeCDAContentToXMLBytes(POCDMT000040ClinicalDocument pObject) throws JAXBException {
      String xmlString = serializeCDAContentToXMLString(pObject);
      return xmlString.getBytes();
   }

   private String getStyleSheet() {

      if (documentType.equalsIgnoreCase(AssemblyConstants.C32_DOCUMENT)) {
         return PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.DAS_C32_STYLESHEET, true);
      } else {
         log.error("No stylesheet available for document type \"" + documentType);
      }

      return "CCD.xsl";
   }
}
