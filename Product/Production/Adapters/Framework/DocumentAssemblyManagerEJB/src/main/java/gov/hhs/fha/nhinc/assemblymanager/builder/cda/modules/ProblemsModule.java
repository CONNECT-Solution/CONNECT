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
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.ProblemsSectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.SectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.template.TemplateConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CS;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040Observation;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.XActMoodDocumentObservation;
import org.hl7.v3.POCDMT000040Act;
import org.hl7.v3.XActClassDocumentEntryAct;
import org.hl7.v3.XDocumentActMood;
import org.hl7.v3.CD;
import org.hl7.v3.COCTMT090000UV01AssignedEntity;
import org.hl7.v3.COCTMT090000UV01Person;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.POCDMT000040AssignedEntity;
import org.hl7.v3.POCDMT000040EntryRelationship;
import org.hl7.v3.POCDMT000040Performer2;
import org.hl7.v3.POCDMT000040Person;
import org.hl7.v3.ParticipationPhysicalPerformer;
import org.hl7.v3.REPCMT000100UV01Performer3;
import org.hl7.v3.XActRelationshipEntryRelationship;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.XActRelationshipEntry;

/**
 * This module defines a patient's current medications and pertinent medication history.
 * 
 * @author kim
 */
public class ProblemsModule extends ModuleImpl {

    private static Log log = LogFactory.getLog(ProblemsModule.class);
    private ProblemsSectionImpl section = null;

    public ProblemsModule(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse) {
        super(template, careRecordResponse);
    }

    public ProblemsModule(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse, SectionImpl section) {
        super(template, careRecordResponse);
        this.section = (ProblemsSectionImpl) section;
    }

    @Override
    public List<POCDMT000040Entry> build() throws DocumentBuilderException {
        List<POCDMT000040Entry> entries = new ArrayList<POCDMT000040Entry>();

        QUPCIN043200UV01MFMIMT700712UV01Subject1 careRecord = careRecordResponse.getCareRecord();
        REPCMT004000UV01CareProvisionEvent careProvisionEvent =
            careRecord.getRegistrationEvent().getSubject2().getCareProvisionEvent();

        List<REPCMT004000UV01PertinentInformation5> problemEvents = careProvisionEvent.getPertinentInformation3();

        log.info("*******************  # of PROBLEM EVENTS: " + problemEvents.size());

        //counter for id values
        int counter = 0;

        for (REPCMT004000UV01PertinentInformation5 problemEvent : problemEvents) {
            entries.add(buildProblems(problemEvent, counter++));
        }

        log.info("Entries:");
        for (int i = 0; i < entries.size(); i++) {
            log.info(entries.get(i));
        }

        if (entries.size() > 0) {
            // problems
            return entries;
        } else {
            return null;
        }
    }

    private POCDMT000040Entry buildProblems(REPCMT004000UV01PertinentInformation5 problemEvent, int count) {
        log.info("Inside buildProblems");

        POCDMT000040Entry problemEntry = new POCDMT000040Entry();
        problemEntry.setTypeCode(XActRelationshipEntry.DRIV);

        // Problem Entry
        POCDMT000040Act act = new POCDMT000040Act();
        act.setClassCode(XActClassDocumentEntryAct.ACT);
        act.setMoodCode(XDocumentActMood.EVN);
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

        // REQUIRED! Set template ids to identify that this is a problem entry
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

        POCDMT000040EntryRelationship actEntryRelationship = new POCDMT000040EntryRelationship();
        actEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.SUBJ);
        actEntryRelationship.setInversionInd(Boolean.FALSE);

        // CAL
        REPCMT000100UV01Observation problemObservation = problemEvent.getObservation().getValue();
        // CDA
        POCDMT000040Observation actEntryRelationshipObs = new POCDMT000040Observation();
        actEntryRelationshipObs.getClassCode().add("OBS");
        actEntryRelationshipObs.setMoodCode(XActMoodDocumentObservation.EVN);

        // problem observation template identifier
        II template1 = new II();
        template1.setRoot(TemplateConstants.ccdProblemObservationTemplateId);
        template1.setAssigningAuthorityName("CCD");
        actEntryRelationshipObs.getTemplateId().add(template1);

        II actERTemplateId = objectFactory.createII();
        actERTemplateId.setRoot("1.3.6.1.4.1.19376.1.5.3.1.4.5");
        actEntryRelationshipObs.getTemplateId().add(actERTemplateId);

        //unique act id for this module
        II id = new II();

        //create an id that is sequential and always clinical hash compliant
        String idStr = "Problem Act Id " + String.valueOf(count);
        String idVal = UUIDGenerator.generateUUIDFromString(idStr);

        id.setRoot(idVal);
        act.getId().add(id);
        log.debug("Problem act #" + String.valueOf(count) + " id = " + idVal);

        //unique observation id for this module entry
        if (problemObservation.getId().size() > 0) {
            //value returned from EHR
            //      actEntryRelationshipObs.getId().add(problemObservation.getId().get(0));
            actEntryRelationshipObs.getId().add(id);
        } else {
            //No value returned from EHR - generate random id
            II observationId = new II();

            //create an id that is sequential and always clinical hash compliant
            String obsIdStr = "Problem Observation Id " + String.valueOf(count);
            String obsIdVal = UUIDGenerator.generateUUIDFromString(obsIdStr);

            observationId.setRoot(obsIdVal);
            actEntryRelationshipObs.getId().add(observationId);
            log.debug("Problem observation #" + String.valueOf(count) + " id = " + obsIdVal);
        }

        // status code
        CS statusCode = new CS();
        statusCode.setCode("completed");
        actEntryRelationshipObs.setStatusCode(statusCode);

        //set text with reference
        EDExplicit problemText = objectFactory.createEDExplicit();
        TELExplicit conditionReference = objectFactory.createTELExplicit();
        String sequenceValue = String.valueOf(problemEvent.getSequenceNumber().getValue());
        conditionReference.setValue("#CondID-" + sequenceValue);
        problemText.getContent().add(0, objectFactory.createEDExplicitReference(conditionReference));
        actEntryRelationshipObs.setText(problemText);


        // ------------------  Problem Date  ------------------
        // needs to be report in the "low" element of effectiveTime
        if (problemObservation.getEffectiveTime().size() > 0) {
            IVLTSExplicit effectiveTime = new IVLTSExplicit();
            SXCMTSExplicit problemEffectiveTime = problemObservation.getEffectiveTime().get(0);

            IVXBTSExplicit lowVal = new IVXBTSExplicit();
            lowVal.setValue(problemEffectiveTime.getValue());
            effectiveTime.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

            //actEntryRelationshipObs.setEffectiveTime(effectiveTime);
            actEntryRelationshipObs.setEffectiveTime(effectiveTime);
            log.debug("Problem Event Date set to : " + effectiveTime.getValue());
        } else if (problemObservation.getText() != null) {

            //CHS returns problem date in text block
            String textValue = getEDItem(problemObservation.getText());
            int index = textValue.indexOf(';');

            if ((index >= 0) && (textValue.length() > index + 2)) {
                String newText = textValue.substring(index + 2);
                log.debug("Parsed effectiveDate from Text = " + newText);

                IVLTSExplicit effectiveTime = new IVLTSExplicit();
                IVXBTSExplicit lowVal = new IVXBTSExplicit();

                if (newText.trim().equals("No Date Recorded")) {
                    //set the low value to null flavor because date is unknown
                    log.debug("No Problem Observation Date available. Set NullFlavor value");
                    //lowVal.getNullFlavor().add("UNK");
                    // lowVal.setValue("00000000");
                    lowVal.getNullFlavor().add("UNK");
                } else {
                    lowVal.setValue(newText);
                }

                log.debug("Setting effectiveTime Low value...");
                effectiveTime.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

                log.debug("Setting effectiveTime value...");
                actEntryRelationshipObs.setEffectiveTime(effectiveTime);
                log.debug("Problem Event Date set to : " + effectiveTime.getValue());
            }
        } else {
            log.debug("Problem Event Date not found.");
        }

        // Problem Type
        if (problemObservation.getCode() != null && !problemObservation.getCode().getCode().isEmpty()) {
            //try to retrieve from CareRecord
            actEntryRelationshipObs.setCode(problemObservation.getCode());
        } else {
            //not available in CareRecord... set static value
            CD probCode = objectFactory.createCD();
            probCode.setCode("64572001");
            probCode.setCodeSystem("2.16.840.1.113883.6.96");
            probCode.setCodeSystemName("SNOMED CT");
            actEntryRelationshipObs.setCode(probCode);
        }

        //Problem Code
        actEntryRelationshipObs.getValue().add(problemObservation.getValue());

        // Treating Provider
        if (problemObservation.getPerformer().size() > 0) {
            POCDMT000040Act problemAct = new POCDMT000040Act();
            II actTemplateId = new II();
            actTemplateId.setRoot(CDAConstants.PROBLEM_ACT_TEMPLATE_ID);
            problemAct.getTemplateId().add(actTemplateId);

            for (REPCMT000100UV01Performer3 performer : problemObservation.getPerformer()) {
                POCDMT000040Performer2 treatingProvider = getTreatingProvider(performer);
                if (treatingProvider != null) {
                    problemAct.getPerformer().add(treatingProvider);
                }
            }
        }

        //set the Problem Reference to be used by ProblemsSectionImpl to build <text> tag
        String key = "CondID-" + sequenceValue;
        String problemTextVal = getEDItem(problemObservation.getText());

        section.getProblems().put(key, problemTextVal);

        actEntryRelationship.setObservation(actEntryRelationshipObs);

        act.getEntryRelationship().add(actEntryRelationship);

        problemEntry.setAct(act);

        return problemEntry;
    }

    private POCDMT000040Performer2 getTreatingProvider(REPCMT000100UV01Performer3 performer) {
        POCDMT000040Performer2 treatingProvider = null;

        if (performer != null) {
            treatingProvider = new POCDMT000040Performer2();
            treatingProvider.setTypeCode(ParticipationPhysicalPerformer.PRF);

            POCDMT000040AssignedEntity assignedEntity = new POCDMT000040AssignedEntity();
            if (performer.getAssignedEntity1() != null) {
                COCTMT090000UV01AssignedEntity performerAssignedEntity = performer.getAssignedEntity1().getValue();
                for (II id : performerAssignedEntity.getId()) {
                    assignedEntity.getId().add(id);
                }

                if (performerAssignedEntity.getAssignedPerson() != null) {
                    COCTMT090000UV01Person performerAssignedPerson = performerAssignedEntity.getAssignedPerson().getValue();
                    POCDMT000040Person treatingProviderPerson = new POCDMT000040Person();
                    XMLUtil.setName(performerAssignedPerson.getName(), treatingProviderPerson);
                    assignedEntity.setAssignedPerson(treatingProviderPerson);
                }
            }

            treatingProvider.setAssignedEntity(assignedEntity);
        }

        return treatingProvider;
    }
}
