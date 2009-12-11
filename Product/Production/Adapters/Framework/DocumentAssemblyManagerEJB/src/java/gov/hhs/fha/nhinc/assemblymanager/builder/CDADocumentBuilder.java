/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.RecordTargetModule;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.List;
import org.hl7.v3.ActClassClinicalDocument;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040StructuredBody;

/**
 *
 * @author kim
 */
public class CDADocumentBuilder extends DocumentBuilder {

   private POCDMT000040ClinicalDocument cdaDocument = null;
   private String docId = "";

   public CDADocumentBuilder() {
      super();
   }

   public CDADocumentBuilder(List<CdaTemplate> sectionTemplates) {
      super(sectionTemplates);
   }

   public CDADocumentBuilder(String id) {
      super(id);
   }

   public POCDMT000040ClinicalDocument getCdaDocument() {
      return cdaDocument;
   }

   public String getDocId() {
      return docId;
   }

   public POCDMT000040ClinicalDocument build() throws DocumentBuilderException {

      if (documentType.getTypeId().equalsIgnoreCase(AssemblyConstants.C32_DOCUMENT)) {
         buildC32();
      } else {
         throw new DocumentBuilderException("Document type \"" + documentType.getTypeId() + "\" is not supported.");
      }

      return cdaDocument;
   }

   private void buildC32() throws DocumentBuilderException {
      cdaDocument = objectFactory.createPOCDMT000040ClinicalDocument();

      cdaDocument.setClassCode(ActClassClinicalDocument.DOCCLIN);

      // build headers
      POCDMT000040InfrastructureRootTypeId typeId = objectFactory.createPOCDMT000040InfrastructureRootTypeId();
      typeId.setRoot(CDAConstants.TYPE_ID_ROOT);
      typeId.setExtension(CDAConstants.TYPE_ID_EXTENSION_POCD_HD000040);
      cdaDocument.setTypeId(typeId);

      II templateId = new II();
      templateId.setRoot("2.16.840.1.113883.10.20.1");
      cdaDocument.getTemplateId().add(templateId);
      templateId = new II();
      templateId.setRoot("2.16.840.1.113883.3.88.11.32.1"); // CCD v1.0 Templates Root
      cdaDocument.getTemplateId().add(templateId);

      // ClinicalDocument.id: Represents the globally unique instance identifier of
      // this version of a clinical document.
      docId = createDocumentId();
      II documentId = new II();
      documentId.setExtension(docId);
      cdaDocument.setId(documentId);

      // ClinicalDocument.code: The code specifying the particular kind of document.
      CE code = new CE();
      code.setCode(AssemblyConstants.C32_DOCUMENT);
      code.setDisplayName(documentType.getDisplayName());
      code.setCodeSystem(documentType.getCodeSystemOid());
      code.setCodeSystemName(AssemblyConstants.LOINC_CODE_NAME);
      cdaDocument.setCode(code);

      // ClinicalDocument.effectiveTime: Signifies the document creation time, when the document
      // first came into being.
      //TSExplicit effectiveTime = new TSExplicit();
      //effectiveTime.setValue(DateUtil.convertToCDATime(getCreatedDTM()));
      //cdaDocument.setEffectiveTime(effectiveTime);

      // ClinicalDocument.confidentialityCode: It is customary, but not required, that the value of
      // confidentialityCode be set equal to the most restrictive confidentialityCode of any of the document parts.
      CE confidentialityCode = new CE();
      confidentialityCode.setCode(PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.DEF_CONFIDENTIAL_CODE, true));
      confidentialityCode.setDisplayName(PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.DEF_CONFIDENTIAL_CODE_DESCR, true));
      confidentialityCode.setCodeSystem(PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.DEF_CONFIDENTIAL_CODE_SYS_OID, true));
      cdaDocument.setConfidentialityCode(confidentialityCode);

      // ClinicalDocument.languageCode: Specifies the human language of character data
      CS languageCode = new CS();
      languageCode.setCode(PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.DEF_LANGUAGE, true));
      cdaDocument.setLanguageCode(languageCode);

      // patient
      II subjectId = new II();
      subjectId.setExtension(patientId);
      subjectId.setRoot(this.orgOID);

      // recordTarget: The recordTarget represents the medical record that this document belongs to.
      RecordTargetModule rtModule = new RecordTargetModule(subjectId);
      cdaDocument.getRecordTarget().add(rtModule.build());

      // now build the applicable clinical domain section(s)
      POCDMT000040Component2 clinicalComponent = new POCDMT000040Component2();
      POCDMT000040StructuredBody structBody = new POCDMT000040StructuredBody();

      if (templates != null) {
         CdaTemplate template = null;
         for (int i = 0; i < this.templates.size(); i++) {
            template = templates.get(i);
            log.debug("Build section(POCDMT000040Component3) according to template:" + template);
            POCDMT000040Component3 section =
                    StructureComponentFactoryBuilder.createHITSPComponent(subjectId, template);
            structBody.getComponent().add(section);
         }
      }

      clinicalComponent.setStructuredBody(structBody);
      cdaDocument.setComponent(clinicalComponent);

      log.info("Created C32 document for patient(" + subjectId.getExtension() + "), docId(" + docId + ")");
   }
}
