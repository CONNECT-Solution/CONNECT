/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.assemblymanager.builder;

import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.Author;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.CustodianModule;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.DocumentationOfModule;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.InformationSource;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.RecordTargetModule;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ActClassClinicalDocument;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040StructuredBody;
import org.hl7.v3.STExplicit;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.TELExplicit;

/**
 *
 * @author kim
 */
public class C32DocumentBuilder extends DocumentBuilder {

    private static Log log = LogFactory.getLog(C32DocumentBuilder.class);
    private List<POCDMT000040ClinicalDocument> cdaDocumentList = null;
    private POCDMT000040ClinicalDocument c32Document = null;
    private String docId = "";

    public C32DocumentBuilder() {
        super();
    }

    public C32DocumentBuilder(List<CdaTemplate> sectionTemplates) {
        super(sectionTemplates);
    }

    public C32DocumentBuilder(String id) {
        super(id);
    }

    public POCDMT000040ClinicalDocument getCdaDocument() {
        return c32Document;
    }

    public String getDocId() {
        return docId;
    }

    public List<POCDMT000040ClinicalDocument> build() throws DocumentBuilderException {

        if (documentType.getTypeId().equalsIgnoreCase(AssemblyConstants.C32_CLASS_CODE)) {
            buildC32();
        } else {
            throw new DocumentBuilderException("Document type \"" + documentType.getTypeId() + "\" is not supported.");
        }

        return cdaDocumentList;
    }

    private void buildC32() throws DocumentBuilderException {

        c32Document = objectFactory.createPOCDMT000040ClinicalDocument();
        cdaDocumentList = new ArrayList();

        c32Document.setClassCode(ActClassClinicalDocument.DOCCLIN);

        //build realmCode
        CS csObj = new CS();
        csObj.setCode("US");
        c32Document.getRealmCode().add(csObj);
        // build headers
        POCDMT000040InfrastructureRootTypeId typeId = objectFactory.createPOCDMT000040InfrastructureRootTypeId();
        typeId.setRoot(CDAConstants.TYPE_ID_ROOT);
        typeId.setExtension(CDAConstants.TYPE_ID_EXTENSION_POCD_HD000040);
        c32Document.setTypeId(typeId);

        II templateId = new II();
        templateId.setRoot("2.16.840.1.113883.10.20.1");
        c32Document.getTemplateId().add(templateId);
        templateId = new II();
        templateId.setRoot("2.16.840.1.113883.3.88.11.32.1"); // CCD v1.0 Templates Root
        c32Document.getTemplateId().add(templateId);

        II realmCodeTemplateId = new II();
        realmCodeTemplateId.setRoot("2.16.840.1.113883.10.20.3"); //supports the realmCode value
        c32Document.getTemplateId().add(realmCodeTemplateId);

        II pccMedicalDocsTemplateId = new II();
        pccMedicalDocsTemplateId.setRoot("1.3.6.1.4.1.19376.1.5.3.1.1.1"); //supports PCC Medical Docs
        c32Document.getTemplateId().add(pccMedicalDocsTemplateId);
        // ClinicalDocument.id: Represents the globally unique instance identifier of
        // this version of a clinical document.
        docId = createDocumentId();
        II documentId = new II();
        documentId.setRoot(docId);
        c32Document.setId(documentId);

        // ClinicalDocument.code: The code specifying the particular kind of document.
        CE code = new CE();
        code.setCode(AssemblyConstants.C32_CLASS_CODE);
        code.setDisplayName(AssemblyConstants.C32_DISPLAY_NAME);
        code.setCodeSystem(CDAConstants.LOINC_CODE_SYS_OID);
        code.setCodeSystemName(CDAConstants.LOINC_CODE_SYS_NAME);
        c32Document.setCode(code);

        // Create an UNKNWON address element
        ADExplicit unknownAddr = new ADExplicit();
        unknownAddr.getNullFlavor().add("UNK");

        //Create an UNKNOWN telephone element
        TELExplicit unknownTele = new TELExplicit();
        unknownTele.getNullFlavor().add("UNK");

        //build title of document to conform with validation testing and set value to be same as code display name
        STExplicit STEtitle = new STExplicit();
        STEtitle.getContent().add(AssemblyConstants.C32_DISPLAY_NAME);
        c32Document.setTitle(STEtitle);
        // ClinicalDocument.effectiveTime: Signifies the document creation time, when the document
        // first came into being.  Default to "000000000000000-0000".  Actual value will be set when
        // response is formulated.
        TSExplicit effectiveTimeTS = new TSExplicit();
        // FIXUPS for Document Type C32
        // ClinicalDocument.effectiveTime: Signifies the document creation time, when the document
        // first came into being. For C32s that is the time the document is generated.
        // Cxx must have a time zone specifier.
        UTCDateUtil utcTime = new UTCDateUtil();
        String effectiveTime = utcTime.formatUTCDate(Calendar.getInstance().getTime()) + "-0000";
        effectiveTimeTS.setValue(effectiveTime);
        c32Document.setEffectiveTime(effectiveTimeTS);

        // ClinicalDocument.confidentialityCode: It is customary, but not required, that the value of
        // confidentialityCode be set equal to the most restrictive confidentialityCode of any of the document parts.
        CE confidentialityCode = new CE();
        confidentialityCode.setCode(AssemblyConstants.C32_CONFIDENTIAL_CODE);
        confidentialityCode.setDisplayName(AssemblyConstants.C32_CONFIDENTIAL_CODE_DESCR);
        confidentialityCode.setCodeSystem(CDAConstants.CONFIDENTIAL_CODE_SYS_OID);
        c32Document.setConfidentialityCode(confidentialityCode);

        // ClinicalDocument.languageCode: Specifies the human language of character data
        CS languageCode = new CS();
        languageCode.setCode(AssemblyConstants.LANGUAGE);
        c32Document.setLanguageCode(languageCode);

        // patient
        II subjectId = new II();
        subjectId.setExtension(patientId);
        subjectId.setRoot(orgOID);

        // recordTarget: The recordTarget represents the medical record that this document belongs to.
        RecordTargetModule rtModule = new RecordTargetModule(subjectId);
        org.hl7.v3.POCDMT000040Participant1 participant = objectFactory.createPOCDMT000040Participant1();

        org.hl7.v3.POCDMT000040RecordTarget recordTarget = new org.hl7.v3.POCDMT000040RecordTarget();

        try {
            recordTarget = rtModule.build(participant);
        } catch (DocumentBuilderException dbe) {
            log.error("Unable to build a RECORD TARGET MODULE", dbe);
        }

        if (recordTarget != null) {
            c32Document.getRecordTarget().add(recordTarget);

            // author: information about the author or creator of the information
            Author author = new Author();
            try {
                c32Document.getAuthor().add(author.build());
            } catch (DocumentBuilderException dbe) {
                log.error("Unable to build an AUTHOR MODULE", dbe);
            }

            // Author.time
            if (c32Document.getAuthor().size() > 0) {
                c32Document.getAuthor().get(0).setTime(effectiveTimeTS);
            }
            // informant: This module contains information about the original author
            InformationSource informant = new InformationSource();
            try {
                c32Document.getInformant().add(informant.build());
            } catch (DocumentBuilderException dbe) {
                log.error("Unable to build an INFORMATION SOURCE MODULE", dbe);
            }

            // custodian: This module contains information about the custodian of records
            CustodianModule custodianModule = new CustodianModule();
            try {
                c32Document.setCustodian(custodianModule.build());
            } catch (DocumentBuilderException dbe) {
                log.error("Unable to build a CUSTODIAN MODULE", dbe);
            }

            //participant: This module contains emergency contact information
            try {
                c32Document.getParticipant().add(participant);//participantModule.build());
            } catch (Exception dbe) {
                log.error("Unable to build a PARTICIPANT module", dbe);
            }

            org.hl7.v3.POCDMT000040Person ap = null;
            if ((ap = participant.getAssociatedEntity().getAssociatedPerson()) != null) {
                if (ap.getName().isEmpty()) {
                    participant = null;
                }

            }
            // Fix Participant if telephone is missing.
            List<TELExplicit> tele = participant.getAssociatedEntity().getTelecom();
            if ((tele.size() == 0) || (tele.get(0) == null) || (tele.get(0).getValue() == null) || (tele.get(0).getValue().equals(""))) {
                participant.getAssociatedEntity().getTelecom().add(unknownTele);
            }
            //documentationOf (serviceEvent)
            DocumentationOfModule documentationOfModule = new DocumentationOfModule();
            try {
                c32Document.getDocumentationOf().add(documentationOfModule.build());//documentationOfModule.build();
            } catch (Exception dbe) {
                log.error("Unable to build a documentationOf module", dbe);
            }
            // now build the applicable clinical domain section(s)
            POCDMT000040Component2 clinicalComponent = new POCDMT000040Component2();
            POCDMT000040StructuredBody structBody = new POCDMT000040StructuredBody();

            if (templates != null) {
                CdaTemplate template = null;
                for (int i = 0; i < this.templates.size(); i++) {
                    template = templates.get(i);
                    log.info("Build section(POCDMT000040Component3) - template:" + template);
                    POCDMT000040Component3 component = null;
                    try {
                        component = StructureComponentFactoryBuilder.createHITSPComponent(subjectId, template);
                    } catch (Exception e) {
                        log.error("Failed to build (section) template:" + template + "due to the following: " + e);
                    }
                    if (component != null) {
                        structBody.getComponent().add(component);
                        clinicalComponent.setStructuredBody(structBody);
                        c32Document.setComponent(clinicalComponent);
                    } else {
                        continue;
                    }
                }
                if (structBody.getComponent().size() == 0) {
                    c32Document = null;
                    log.info("No C32 document created for patient(" + subjectId.getExtension() + ")- no medical information returned from EMR system.");
                } else {
                    log.info("Created C32 document for patient(" + subjectId.getExtension() + "), docId(" + docId + ")");
                }
            }
        } else {
            c32Document = null;
            log.info("C32 not created - no Patient Demographic information returned...");
        }

        //Add the current CDA document to the document list
        if (c32Document != null) {
            cdaDocumentList.add(c32Document);
            log.debug("C32 Clinical Document Complete. Content follows:");
            log.debug(XMLUtil.toPrettyXML(c32Document));
        }
    }
}
