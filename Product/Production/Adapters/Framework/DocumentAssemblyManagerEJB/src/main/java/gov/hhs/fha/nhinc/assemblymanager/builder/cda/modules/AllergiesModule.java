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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.AllergiesSectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.SectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import gov.hhs.fha.nhinc.template.TemplateConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.POCDMT000040Participant2;
import org.hl7.v3.POCDMT000040PlayingEntity;
import org.hl7.v3.POCDMT000040EntryRelationship;
import org.hl7.v3.REPCMT000100UV01SourceOf3;
import org.hl7.v3.POCDMT000040Observation;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.XActMoodDocumentObservation;
import org.hl7.v3.POCDMT000040Act;
import org.hl7.v3.XActClassDocumentEntryAct;
import org.hl7.v3.XDocumentActMood;
import org.hl7.v3.CD;
import org.hl7.v3.CEExplicit;
import org.hl7.v3.XActRelationshipEntryRelationship;
import org.hl7.v3.POCDMT000040ParticipantRole;
import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.REPCMT000100UV01AdministerableMaterial;
import org.hl7.v3.REPCMT000100UV01Consumable;
import org.hl7.v3.REPCMT000100UV01Material;
import org.hl7.v3.STExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.XActRelationshipEntry;

/**
 * This module defines a patient's current medications and pertinent medication history.
 *
 *
 */
public class AllergiesModule extends ModuleImpl {

    private static Log log = LogFactory.getLog(AllergiesModule.class);
    private AllergiesSectionImpl section = null;

    public AllergiesModule(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse) {
        super(template, careRecordResponse);
    }

    public AllergiesModule(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse, SectionImpl section) {
        super(template, careRecordResponse);
        this.section = (AllergiesSectionImpl) section;
    }

    @Override
    public List<POCDMT000040Entry> build() throws DocumentBuilderException {
        System.out.println("Inside the AllergiesModule build()");
        List<POCDMT000040Entry> entries = new ArrayList<POCDMT000040Entry>();

        QUPCIN043200UV01MFMIMT700712UV01Subject1 careRecord = careRecordResponse.getCareRecord();
        REPCMT004000UV01CareProvisionEvent careProvisionEvent =
            careRecord.getRegistrationEvent().getSubject2().getCareProvisionEvent();

        List<REPCMT004000UV01PertinentInformation5> allergyEvents = careProvisionEvent.getPertinentInformation3();

        log.info("*******************  # of Allergy EVENTS: " + allergyEvents.size());

        //counter used for act and observation id's
        int counter = 0;

        for (REPCMT004000UV01PertinentInformation5 allergyEvent : allergyEvents) {

            entries.add(buildAllergy(allergyEvent, counter++));
        }
        if (entries.size() > 0) {
            // allergy
            return entries;
        } else {
            return null;
        }
    }

    private POCDMT000040Entry buildAllergy(REPCMT004000UV01PertinentInformation5 allergyEvent, int count) {
        // CDA
        POCDMT000040Entry allergyEntry = new POCDMT000040Entry();
        allergyEntry.setTypeCode(XActRelationshipEntry.DRIV);

        POCDMT000040Act act = new POCDMT000040Act();
        act.setClassCode(XActClassDocumentEntryAct.ACT);
        act.setMoodCode(XDocumentActMood.EVN);

        // REQUIRED! Set template identifiers to identify that this is an
        // allergy entry
        List<II> templateIdList = getConformingTemplateIds();
        for (II templateId : templateIdList) {
            act.getTemplateId().add(templateId);
        }

        //additional template id is required to conform to the 1.3.6.1.4.1.19376.1.5.3.1.4.5.2 entry
        II additionalTemplateId = objectFactory.createII();
        additionalTemplateId.setRoot("1.3.6.1.4.1.19376.1.5.3.1.4.5.1");
        act.getTemplateId().add(additionalTemplateId);

        CD actCode = new CD();
        actCode.getNullFlavor().add("NA");
        act.setCode(actCode);
        //statusCode
        CS actStatusCode = new CS();
        actStatusCode.setCode("active");
        act.setStatusCode(actStatusCode);

        //act effectiveTime
        IVLTSExplicit actEffectiveTime = new IVLTSExplicit();
        IVXBTSExplicit lowExpVal = new IVXBTSExplicit();
        lowExpVal.getNullFlavor().add("UNK");
        actEffectiveTime.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowExpVal));
        act.setEffectiveTime(actEffectiveTime);

        POCDMT000040EntryRelationship actEntryRelationship = new POCDMT000040EntryRelationship();
        actEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.SUBJ);
        actEntryRelationship.setInversionInd(Boolean.FALSE);

        // CAL
        REPCMT000100UV01Observation allergyObservation = allergyEvent.getObservation().getValue();
        // CDA
        POCDMT000040Observation actEntryRelationshipObs = new POCDMT000040Observation();
        actEntryRelationshipObs.getClassCode().add("OBS");
        actEntryRelationshipObs.setMoodCode(XActMoodDocumentObservation.EVN);

        // set template identifiers for Adverse Event Entry
        II template1 = new II();
        template1.setRoot(TemplateConstants.ccdAlertObservationTemplateId);
        template1.setAssigningAuthorityName("CCD");
        actEntryRelationshipObs.getTemplateId().add(template1);

        II template2 = new II();
        template2.setRoot(TemplateConstants.iheAlertObservationTemplateId);
        template2.setAssigningAuthorityName("IHE");
        actEntryRelationshipObs.getTemplateId().add(template2);

        // this ihe observation template identifier is required for both problems and allergies
        II template3 = new II();
        template3.setRoot(TemplateConstants.ccdProblemObservationTemplateId);
        actEntryRelationshipObs.getTemplateId().add(template3);

        II template4 = objectFactory.createII();
        template4.setRoot("1.3.6.1.4.1.19376.1.5.3.1.4.6");
        actEntryRelationshipObs.getTemplateId().add(template4);

        //unique act id for this module
        II id = new II();

        //create an id that is sequential and always clinical hash compliant
        String idStr = "Allergy Act Id " + String.valueOf(count);
        String idVal = UUIDGenerator.generateUUIDFromString(idStr);

        id.setRoot(idVal);
        act.getId().add(id);
        log.debug("Allergy act #" + String.valueOf(count) + " id = " + idVal);

        //unique observation id for this module entry
        if (allergyObservation.getId().size() > 0) {
            //value returned from EHR
            actEntryRelationshipObs.getId().add(allergyObservation.getId().get(0));
        } else {
            //No value returned from EHR - generate random id
            II observationId = new II();

            //create an id that is sequential and always clinical hash compliant
            String obsIdStr = "Allergy Observation Id " + String.valueOf(count);
            String obsIdVal = UUIDGenerator.generateUUIDFromString(obsIdStr);

            observationId.setRoot(obsIdVal);
            actEntryRelationshipObs.getId().add(observationId);
            log.debug("Allergy observation #" + String.valueOf(count) + " id = " + obsIdVal);

        }

        // status code
        CS statusCode = new CS();
        statusCode.setCode("completed");
        actEntryRelationshipObs.setStatusCode(statusCode);


        // ------------------  Adverse Event Type  ------------------
        CD obsCode = allergyObservation.getCode();
        obsCode.setCodeSystem("2.16.840.1.113883.6.96");
        obsCode.setCode("420134006");  //this code is the "parent" for all other codes
        actEntryRelationshipObs.setCode(obsCode);



        // ------------------  Adverse Event Date  ------------------
        // needs to be report in the "low" element of effectiveTime
        if (allergyObservation.getEffectiveTime().size() > 0) {
            IVLTSExplicit newt = new IVLTSExplicit();
            SXCMTSExplicit allergyEffectiveTime = allergyObservation.getEffectiveTime().get(0);

            IVXBTSExplicit lowVal = new IVXBTSExplicit();
            lowVal.setValue(allergyEffectiveTime.getValue());
            newt.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

            actEntryRelationshipObs.setEffectiveTime(newt);

            log.debug("Allergy Event Date set to : " + newt.getValue());
        } else if (allergyObservation.getText() != null) {

            //CHS returns allergy date in text block
            String textValue = getEDItem(allergyObservation.getText());
            int index = textValue.indexOf(';');

            if ((index >= 0) && (textValue.length() > index + 2)) {
                String newText = textValue.substring(index + 2);
                log.debug("Parsed effectiveDate from Text = " + newText);

                IVLTSExplicit effectiveTime = new IVLTSExplicit();
                IVXBTSExplicit lowVal = new IVXBTSExplicit();

                if (newText.trim().equals("No Date Recorded")) {
                    //set the low value to null flavor because date is unknown
                    log.debug("No Allergy Observation Date available. Set NullFlavor value");
                    //lowVal.getNullFlavor().add("UNK");
                    // lowVal.setValue("00000000");
                    lowVal.getNullFlavor().add("UNK");
                } else {
                    lowVal.setValue(newText);
                }

                log.debug("Setting Allergy Observation effectiveTime Low value...");
                effectiveTime.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

                log.debug("Setting effectiveTime value...");
                actEntryRelationshipObs.setEffectiveTime(effectiveTime);
                log.debug("Allergy Event Date set to : " + effectiveTime.getValue());
            }

        } else {
            log.debug("Allergy Event Date not found.");
        }

        //reaction value
        CD test = objectFactory.createCD();
        actEntryRelationshipObs.getValue().add(test);

        List<REPCMT000100UV01SourceOf3> allergySourceOf3 = allergyObservation.getSourceOf();

        for (REPCMT000100UV01SourceOf3 thisAllergySourceOf3 : allergySourceOf3) {
            // check type code
            String typeCode = thisAllergySourceOf3.getTypeCode().get(0);
            if (typeCode.equalsIgnoreCase(CDAConstants.TYPE_CODE_MFST)) {
                // ------------------  Product  ------------------
                if (thisAllergySourceOf3.getSubstanceAdministration() != null) {
                    log.debug("***********  ALLERGY SUBSTANCE  ***********");

                    REPCMT000100UV01Consumable calConsumable = thisAllergySourceOf3.getSubstanceAdministration().getValue().getConsumable();

                    if (calConsumable != null) {
                        POCDMT000040Participant2 CDAParticipant2 = new POCDMT000040Participant2();
                        CDAParticipant2.getTypeCode().add(CDAConstants.TYPE_CODE_CSM);

                        REPCMT000100UV01AdministerableMaterial calAdministerableMaterial = calConsumable.getAdministerableMaterial();
                        if (calAdministerableMaterial != null) {
                            actEntryRelationshipObs.getParticipant().add(getProduct(calConsumable.getAdministerableMaterial()));
                        }
                    }
                } // ------------------  Reaction  ------------------
                else if (thisAllergySourceOf3.getObservation() != null) {
                    log.debug("***********  ALLERGY REACTION OBSERVATION  ***********");

                    REPCMT000100UV01Observation calAllergyObs = thisAllergySourceOf3.getObservation().getValue();

                    if (calAllergyObs != null) {
                        POCDMT000040EntryRelationship reaction = getReaction(calAllergyObs);
                        if (reaction != null) {
                            actEntryRelationshipObs.getEntryRelationship().add(reaction);
                        }
                    }
                } else {
                    log.error("Unknown allergy information with typeCode=" + typeCode);
                }
            } // ------------------  Severity  ------------------
            else if (typeCode.equalsIgnoreCase(CDAConstants.TYPE_CODE_SUBJ)) {
                // NEED TO BE FIX AT CAL LAYER AS SEVERITY IS TIE TO A REACTION!
                //if (thisAllergySourceOf3.getObservation() != null) {
                //   log.debug("***********  ALLERGY SEVERITY OBSERVATION  ***********");

                //   REPCMT000100UV01Observation calSeverityObs = thisAllergySourceOf3.getObservation().getValue();

                //   if (calSeverityObs != null) {
                //      POCDMT000040EntryRelationship severity = getSeverity(calSeverityObs);
                //      if (severity != null) {
                //         actEntryRelationshipObs.getEntryRelationship().add(severity);
                //      }
                //   }
                //}
                log.error("Allergy severity information not implemented!");
            } else {
                log.error("Unknown allergy information with typeCode=" + typeCode);
            }
        }

        actEntryRelationship.setObservation(actEntryRelationshipObs);
        act.getEntryRelationship().add(actEntryRelationship);

        allergyEntry.setAct(act);

        return allergyEntry;
    }

    private POCDMT000040Participant2 getProduct(REPCMT000100UV01AdministerableMaterial administerableMaterial) {
        POCDMT000040Participant2 product = new POCDMT000040Participant2();
        if (administerableMaterial != null) {
            product.getTypeCode().add(CDAConstants.TYPE_CODE_CSM);

            REPCMT000100UV01Material calMaterial = administerableMaterial.getAdministerableMaterial();
            if (calMaterial != null) {
                POCDMT000040ParticipantRole productDetail = new POCDMT000040ParticipantRole();
                productDetail.getClassCode().add(CDAConstants.CLASS_CODE_MANU);

                POCDMT000040PlayingEntity productDetailPlayingEntity = new POCDMT000040PlayingEntity();
                productDetailPlayingEntity.getClassCode().add(CDAConstants.CLASS_CODE_MMAT);

                // Product Coded
                if (calMaterial.getCode() != null) {
                    productDetailPlayingEntity.setCode(calMaterial.getCode());
                } else {
                    //playingEntity code and text are not known
                    CEExplicit playingCode = objectFactory.createCEExplicit();
                    EDExplicit playText = objectFactory.createEDExplicit();
                    TELExplicit telExpl = objectFactory.createTELExplicit();
                    telExpl.getNullFlavor().add("UNK");
                    playText.getContent().add(this.objectFactory.createEDExplicitReference(telExpl));
                    playText.getNullFlavor().add("UNK");
                    playingCode.setOriginalText(playText);
                    playingCode.getNullFlavor().add("UNK");
                    productDetailPlayingEntity.setCode(playingCode);
                }

                // Product Free-Text
                if (calMaterial.getDesc() != null) {
                    STExplicit productDesc = calMaterial.getDesc();
                    if (productDesc.getContent().size() > 0) {
                        PNExplicit productFreeText = new PNExplicit();
                        productFreeText.getContent().add(productDesc.getContent().get(0));
                        productDetailPlayingEntity.getName().add(productFreeText);
                    }
                }

                productDetail.setPlayingEntity(productDetailPlayingEntity);
                product.setParticipantRole(productDetail);
            }
        }

        return product;
    }

    /**
     * Create an entryRelationship object with typeCode = MFST.
     * 
     * @param obs
     * @return
     */
    private POCDMT000040EntryRelationship getReaction(REPCMT000100UV01Observation obs) {
        POCDMT000040EntryRelationship reactionEntryRelationship = null;

        if (obs != null) {
            try {
                reactionEntryRelationship = new POCDMT000040EntryRelationship();
                POCDMT000040Observation entryObs = new POCDMT000040Observation();

                reactionEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.MFST);
                reactionEntryRelationship.setInversionInd(true);
                entryObs.getClassCode().add("OBS");
                entryObs.setMoodCode(XActMoodDocumentObservation.EVN);

                // reaction observation template id
                II reactionTemplate = new II();
                reactionTemplate.setRoot(TemplateConstants.ccdReactionObservationTemplateId);
                reactionTemplate.setAssigningAuthorityName("CCD");
                entryObs.getTemplateId().add(reactionTemplate);

                // set required <code> element to UNK, i.e. <code
                // nullFlavor="UNK"/>
                CD reactionCode = new CD();
                reactionCode.getNullFlavor().add(CDAConstants.UNKNOWN_CODE);
                entryObs.setCode(reactionCode);

                // status code
                CS statusCode = new CS();
                statusCode.setCode(CDAConstants.STATUS_CODE_COMPLETED);
                entryObs.setStatusCode(statusCode);

                // Reaction Coded
                if (obs.getValue() != null) {
                    entryObs.getValue().add(obs.getValue());
                }

                // Reaction Free-Text
                if (obs.getText() != null &&
                    obs.getText().getContent() != null &&
                    obs.getText().getContent().size() > 0) {
                    EDExplicit reactionText = new EDExplicit();
                    TELExplicit reactionReference = new TELExplicit();

                    String refKey = "reaction-" + section.getReactionIndex();
                    reactionReference.setValue("#" + refKey);
                    reactionText.getContent().add(this.objectFactory.createEDExplicitReference(reactionReference));
                    entryObs.setText(reactionText);

                    // store reaction in reactions list
                    String refValue = getEDItem(obs.getText());
                    log.debug("Add to REACTIONS list - key:" + refKey + ",value:" + refValue);

                    section.getReactions().put(refKey, refValue);
                }

                reactionEntryRelationship.setObservation(entryObs);
            } catch (Exception e) {
                log.error("Failed to build ALLERGY REACTION OBSERVATION!!", e);
                e.printStackTrace();
            }
        }

        return reactionEntryRelationship;
    }

    /**
     * Create an entryRelationship object with typeCode = SUBJ.
     * 
     * @param obs
     * @return
     */
    private POCDMT000040EntryRelationship getSeverity(REPCMT000100UV01Observation obs) {
        POCDMT000040EntryRelationship severityEntryRelationship = null;

        if (obs != null) {
            try {
                severityEntryRelationship = new POCDMT000040EntryRelationship();
                severityEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.SUBJ);

                POCDMT000040Observation cdaSeverityObs = new POCDMT000040Observation();
                cdaSeverityObs.getClassCode().add("OBS");
                cdaSeverityObs.setMoodCode(XActMoodDocumentObservation.EVN);

                // severity observation template id
                II severityTemplate = new II();
                severityTemplate.setRoot(TemplateConstants.ccdSeverityObservationTemplateId);
                severityTemplate.setAssigningAuthorityName("CCD");
                cdaSeverityObs.getTemplateId().add(severityTemplate);

                // status code
                CS statusCode = new CS();
                statusCode.setCode(CDAConstants.STATUS_CODE_COMPLETED);
                cdaSeverityObs.setStatusCode(statusCode);

                // severity code
                CD severityCode = new CD();
                severityCode.setCode(CDAConstants.ACT_CODE_SEVERITY);
                severityCode.setDisplayName(CDAConstants.ACT_CODE_SEVERITY_LABEL);
                severityCode.setCodeSystem(CDAConstants.ACT_CODE_SYS_OID);
                severityCode.setCodeSystemName(CDAConstants.ACT_CODE_SYS_NAME);
                cdaSeverityObs.setCode(severityCode);

                // Severity Coded
                if (obs.getValue() != null) {
                    cdaSeverityObs.getValue().add(obs.getValue());
                }

                // Severity Free-Text
                // if (obs.getText() != null) {
                // cdaSeverityObs.setText(obs.getText());
                // }

                // Severity Free-Text
                if (obs.getText() != null &&
                    obs.getText().getContent() != null &&
                    obs.getText().getContent().size() > 0) {
                    EDExplicit severityText = new EDExplicit();
                    TELExplicit severityReference = new TELExplicit();

                    String refKey = "severity-" + section.getSeverityIndex();

                    severityReference.setValue("#" + refKey);
                    severityText.getContent().add(this.objectFactory.createEDExplicitReference(severityReference));
                    cdaSeverityObs.setText(severityText);

                    // store reaction in reactions list
                    String refValue = getEDItem(obs.getText());
                    log.debug("Add to SEVERITIES list - key:" + refKey + ",value:" + refValue);

                    section.getSeverities().put(refKey, refValue);
                }

                severityEntryRelationship.setObservation(cdaSeverityObs);
            } catch (Exception e) {
                log.error("Failed to build ALLERGY SEVERITY OBSERVATION!!", e);
                e.printStackTrace();
            }
        }

        return severityEntryRelationship;
    }
}
