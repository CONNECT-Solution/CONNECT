/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.dao.DocumentTypeDAO;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DocumentType;
import gov.hhs.fha.nhinc.assemblymanager.utils.DocumentIdGenerator;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.POCDMT000040Organization;
import org.hl7.v3.ONExplicit;

/**
 *
 * @author kim
 */
public abstract class DocumentBuilder {

   protected ObjectFactory objectFactory = null;

   protected String orgOID = "";
   protected String orgName = "";

   protected String patientId = null;
   protected List<CdaTemplate> templates = null;   // templates for section
   protected DocumentType documentType = null;
   
   public DocumentBuilder() {
      initialize();
   }

   public DocumentBuilder(List<CdaTemplate> templates) {
      initialize();
      this.templates = templates;
   }

   public DocumentBuilder(String id) {
      initialize();
      this.patientId = id;
   }

   private void initialize() {
      if (objectFactory == null) {
         objectFactory = new ObjectFactory();
      }

      orgOID = PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.ORGANIZATION_OID, true);
      orgName = PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.ORGANIZATION_NAME, true);
   }

   public String getPatientId() {
      return patientId;
   }

   public void setPatientId(String id) {
      this.patientId = id;
   }

   protected Date getCreatedDTM() {
      return Calendar.getInstance().getTime();
   }

   protected String createDocumentId() {
      return DocumentIdGenerator.generateDocumentId();
   }

   protected II getOrganization() {
      II id = new II();
      id.setRoot(orgOID);
      return id;
   }

   public void setDocumentType(String docType) {
      try {
         documentType = DocumentTypeDAO.getInstance().getDocumentType(docType);
      } catch (Exception ex) {
         documentType = new DocumentType();
         documentType.setTypeId(docType);
      }
   }

   public DocumentType getDocumentType() {
      return documentType;
   }

   protected POCDMT000040Organization getRepresentedOrganization() {
      POCDMT000040Organization org = new POCDMT000040Organization();

      org.getId().add(getOrganization());

      ONExplicit onName = objectFactory.createONExplicit();
      onName.getContent().add(orgName);
      org.getName().add(onName);

      return org;
   }
}
