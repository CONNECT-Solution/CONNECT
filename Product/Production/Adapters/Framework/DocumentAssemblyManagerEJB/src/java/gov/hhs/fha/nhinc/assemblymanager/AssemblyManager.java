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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.POCDMT000040ClinicalDocument;

/**
 *
 * @author kim
 */
public class AssemblyManager {

   private static Log log = LogFactory.getLog(AssemblyManager.class);

   public POCDMT000040ClinicalDocument getDocument(String docType, String patientId) throws InvalidTypeException, InvalidIdentifierException, DocumentBuilderException {

      // check for valid document type
      if (docType == null) {
         throw new InvalidTypeException("Must specify a document type.");
      }
      if (!DocumentTypeDAO.getInstance().isValidDocumentType(docType)) {
         log.debug("Requested document type \"" + docType + "\" is not supported!");
         throw new InvalidTypeException("Requested document type \"" + docType + "\" is not supported.");
      }

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
}
