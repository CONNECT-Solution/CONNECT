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

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.template.TemplateConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.ActClassSupply;
import org.hl7.v3.CD;
import org.hl7.v3.COCTMT090000UV01AssignedEntity;
import org.hl7.v3.COCTMT090000UV01Person;
import org.hl7.v3.CS;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EntityDeterminerDetermined;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040AssignedAuthor;
import org.hl7.v3.POCDMT000040AssignedEntity;
import org.hl7.v3.POCDMT000040Author;
import org.hl7.v3.POCDMT000040Consumable;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040EntryRelationship;
import org.hl7.v3.POCDMT000040ManufacturedProduct;
import org.hl7.v3.POCDMT000040Material;
import org.hl7.v3.POCDMT000040Observation;
import org.hl7.v3.POCDMT000040Performer2;
import org.hl7.v3.POCDMT000040Person;
import org.hl7.v3.POCDMT000040SubstanceAdministration;
import org.hl7.v3.POCDMT000040Supply;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.REPCMT000100UV01Author3;
import org.hl7.v3.REPCMT000100UV01Consumable;
import org.hl7.v3.REPCMT000100UV01Material;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.REPCMT000100UV01Performer3;
import org.hl7.v3.REPCMT000100UV01SourceOf3;
import org.hl7.v3.REPCMT000100UV01SubstanceAdministration;
import org.hl7.v3.REPCMT000100UV01Supply;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.RoleClassManufacturedProduct;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.TS;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodDocumentObservation;
import org.hl7.v3.XActRelationshipEntry;
import org.hl7.v3.XActRelationshipEntryRelationship;
import org.hl7.v3.XDocumentSubstanceMood;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.MedicationsSectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.SectionImpl;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import org.hl7.v3.TELExplicit;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.StringTokenizer;

/**
 * This module defines a patient's current medications and pertinent medication history.
 * 
 * @author kim
 */
public class MedicationModule extends ModuleImpl {

    private static Log log = LogFactory.getLog(MedicationModule.class);
    private MedicationsSectionImpl section = null;

    public MedicationModule(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse, SectionImpl section) {
        super(template, careRecordResponse);
        this.section = (MedicationsSectionImpl) section;

    }

    @Override
    public List<POCDMT000040Entry> build() throws DocumentBuilderException {
        List<POCDMT000040Entry> entries = new ArrayList<POCDMT000040Entry>();

        QUPCIN043200UV01MFMIMT700712UV01Subject1 careRecord = careRecordResponse.getCareRecord();
        REPCMT004000UV01CareProvisionEvent careProvisionEvent =
            careRecord.getRegistrationEvent().getSubject2().getCareProvisionEvent();

        List<REPCMT004000UV01PertinentInformation5> medEvents = careProvisionEvent.getPertinentInformation3();

        log.debug("*******************  # of MED EVENTS: " + medEvents.size());

        //counter used to create unique id values
        int counter = 0;

        for (REPCMT004000UV01PertinentInformation5 medEvent : medEvents) {
            entries.add(buildMedication(medEvent, counter++));
        }

        if (entries.size() > 0) {
            // medications
            return entries;
        } else {
            return null;
        }
    }

    private POCDMT000040Entry buildMedication(REPCMT004000UV01PertinentInformation5 medEvent, int count) {

        REPCMT000100UV01SubstanceAdministration medEventSubsAdmin = null;
        POCDMT000040Entry medEntry = new POCDMT000040Entry();

        medEntry.setTypeCode(XActRelationshipEntry.DRIV);

        POCDMT000040SubstanceAdministration subsAdmin = new POCDMT000040SubstanceAdministration();
        subsAdmin.getClassCode().add(CDAConstants.SUBSTANCE_ADMINISTRATION_CLASS_CODE);
        subsAdmin.setMoodCode(XDocumentSubstanceMood.EVN);

        if (medEvent.getSubstanceAdministration() != null) {
            medEventSubsAdmin = medEvent.getSubstanceAdministration().getValue();

            if (medEventSubsAdmin != null) {
                // REQUIRED! Set template ids to identify that this is a
                // medication entry
                List<II> templateIdList = getConformingTemplateIds();
                for (II templateId : templateIdList) {
                    subsAdmin.getTemplateId().add(templateId);
                }

                //required template
                II subsAdminTemplateId = objectFactory.createII();
                subsAdminTemplateId.setRoot("1.3.6.1.4.1.19376.1.5.3.1.4.7.1");
                subsAdmin.getTemplateId().add(subsAdminTemplateId);

                // Sig
                EDExplicit medEventSubsAdminSigText = medEventSubsAdmin.getText();
                if (medEventSubsAdminSigText != null) {
                    subsAdmin.setText(medEventSubsAdminSigText);

                    //create medication references to be used by MedicationsSectionImpl to create references
                    String medText = medEventSubsAdmin.getText().getContent().get(0).toString();
                    int semicolonIndex = medText.indexOf(";");
                    section.getSigs().put(medText.substring(0, semicolonIndex), medText.substring(semicolonIndex + 1, medText.length()));
                }

                II tempId = new II();

                // unique id for this module entry
                if (medEventSubsAdmin.getId().size() > 0) {
                    //    subsAdmin.getId().add(medEventSubsAdmin.getId().get(0));
                    subsAdmin.getId().add(tempId);
                } else {
                    //no id returned from CAL - create one
                    II id = new II();

                    //create an id that is sequential and always clinical hash compliant
                    String subsAdminIdStr = "Medication Substance Admin Id " + String.valueOf(count);
                    String subsAdminIdVal = UUIDGenerator.generateUUIDFromString(subsAdminIdStr);

                    id.setRoot(subsAdminIdVal);
                    subsAdmin.getId().add(id);
                    log.debug("Medication Substance Admin #" + String.valueOf(count) + " id = " + subsAdminIdVal);

                }

                // status code
                if (medEventSubsAdmin.getStatusCode() != null) {

                    //status code from AllScripts is "Active", but must be "completed" according to the Spec
                    if (medEventSubsAdmin.getStatusCode().getCode().equals("Active")) {
                        //set to completed
                        CS statusCode = new CS();
                        statusCode.setCode(CDAConstants.STATUS_CODE_COMPLETED);
                        subsAdmin.setStatusCode(statusCode);
                    } else {
                        //retain value from EHR
                        subsAdmin.setStatusCode(medEventSubsAdmin.getStatusCode());
                    }
                } else {

                    //no value returned from EHR, set to "completed"
                    CS statusCode = new CS();
                    statusCode.setCode(CDAConstants.STATUS_CODE_COMPLETED);
                    subsAdmin.setStatusCode(statusCode);
                }

                List<TS> effectiveTimes = medEventSubsAdmin.getEffectiveTime();
                for (TS effectiveTime : effectiveTimes) {
                    subsAdmin.getEffectiveTime().add(effectiveTime);
                }

                // route
                subsAdmin.setRouteCode(medEventSubsAdmin.getRouteCode());
                subsAdmin.getRouteCode().setCodeSystem("2.16.840.1.113883.3.26.1.1");

                // dose quantity
                subsAdmin.setDoseQuantity(medEventSubsAdmin.getDoseQuantity());

                // --------- CONSUMABLE (MEDICATION INFORMATION)
                // ----------------
                POCDMT000040Consumable consumable = buildConsumable(medEventSubsAdmin.getConsumable());

                List<REPCMT000100UV01SourceOf3> sourceOfs = medEventSubsAdmin.getSourceOf();

                for (REPCMT000100UV01SourceOf3 sourceOf : sourceOfs) {
                    if (sourceOf.getTypeCode().size() > 0) {
                        // ---------    ORDER INFORMATION   ----------------
                        if (sourceOf.getTypeCode().get(0).equalsIgnoreCase(CDAConstants.TYPE_CODE_REFR) &&
                            sourceOf.getSupply() != null) {
                            POCDMT000040EntryRelationship orderInfo =
                                buildOrderInfo(sourceOf.getSupply().getValue());

                            //fix the supply quantity unit to include {} braces
                            if (orderInfo.getSupply().getQuantity() != null) //supply quantity may be null due to repeatNumber = 0
                            {

                                //if the untit is a unit of measure (as per the property file values, do not include within {}
                                try {

                                    String unitsOfMeasure = PropertyAccessor.getInstance().getProperty("docassembly", "C32_SUPPLY_UNITS_OF_MEASURE");
                                    StringTokenizer strToken = new StringTokenizer(unitsOfMeasure, ",");
                                    boolean inPropertiesFile = false;

                                    while (strToken.hasMoreTokens()) {
                                        //check the units value against the properties file to see if there's a match
                                        if (orderInfo.getSupply().getQuantity().getUnit().trim().equals(strToken.nextToken().trim())) {
                                            //found in properties - set flag to true
                                            inPropertiesFile = true;
                                            break;
                                        }
                                    }

                                    if (inPropertiesFile) {
                                        //do not put the units value in brackets ({})
                                        orderInfo.getSupply().getQuantity().setUnit(orderInfo.getSupply().getQuantity().getUnit());
                                    } else {
                                        orderInfo.getSupply().getQuantity().setUnit("{" + orderInfo.getSupply().getQuantity().getUnit() + "}");
                                    }

                                } catch (PropertyAccessException e) {
                                    log.error("Exception during building of Medication Supply: " + e);

                                }

                            }

                            subsAdmin.getEntryRelationship().add(orderInfo);
                        } // ---------    TYPE OF MEDICATION   ----------------
                        else if (sourceOf.getTypeCode().get(0).equalsIgnoreCase(CDAConstants.TYPE_CODE_SUBJ) &&
                            sourceOf.getObservation() != null) {
                            POCDMT000040EntryRelationship typeOfMedInfo =
                                buildTypeOfMedInfo(sourceOf.getObservation().getValue());
                            subsAdmin.getEntryRelationship().add(typeOfMedInfo);
                        } // ---------    FULLFILLMENT HISTORY INFORMATION   ----------------
                        else if (sourceOf.getTypeCode().get(0).equalsIgnoreCase(CDAConstants.TYPE_CODE_COMP) &&
                            sourceOf.getSupply() != null) {
                            POCDMT000040EntryRelationship fullfillmentsInfo =
                                buildFullillmentsInfo(sourceOf.getSupply().getValue());
                            subsAdmin.getEntryRelationship().add(fullfillmentsInfo);
                        } // ---------    STATUS OF INFORMATION   ----------------
                        else if (sourceOf.getTypeCode().get(0).equalsIgnoreCase(CDAConstants.TYPE_CODE_REFR) &&
                            sourceOf.getObservation() != null) {
                            if (sourceOf.getObservation().getValue() != null) {
                                POCDMT000040EntryRelationship status =
                                    buildStatusOfMed(sourceOf.getObservation().getValue());
                                subsAdmin.getEntryRelationship().add(status);
                            }
                        }
                    }
                }

                subsAdmin.setConsumable(consumable);
            }

            medEntry.setSubstanceAdministration(subsAdmin);
        }
        return medEntry;
    }

    /**
     * Build Medication (Product) Information entry
     * @param medEventConsumable
     * @return  POCDMT000040Consumable - medication (Product) Information entry
     */
    private POCDMT000040Consumable buildConsumable(
        REPCMT000100UV01Consumable medEventConsumable) {

        POCDMT000040Consumable consumable = new POCDMT000040Consumable();
        consumable.getTypeCode().add(CDAConstants.TYPE_CODE_CSM);

        POCDMT000040ManufacturedProduct manProduct = new POCDMT000040ManufacturedProduct();
        manProduct.setClassCode(RoleClassManufacturedProduct.MANU);

        // Set template identifier for medication information:
        // CCD: <templateId root="2.16.840.1.113883.3.88.11.32.9"/>
        II templateId1 = new II();
        templateId1.setRoot(TemplateConstants.ccdConsumableTemplateId);
        templateId1.setAssigningAuthorityName("CCD");
        manProduct.getTemplateId().add(templateId1);

        // Set template identifier for medication information:
        // HITSP: 2.16.840.1.113883.3.88.11.32.9 (pre version 2.5) or
        // 2.16.840.1.113883.3.88.11.83.8.2
        II templateId3 = new II();
        if (!AssemblyConstants.usePre25Templates()) {
            // Set template identifier for medication information:
            // IHE: <templateId root="1.3.6.1.4.1.19376.1.5.3.1.4.7.2"/>
            II templateId2 = new II();
            templateId2.setRoot(TemplateConstants.iheConsumableTemplateId);
            templateId2.setAssigningAuthorityName("IHE");
            manProduct.getTemplateId().add(templateId2);

            templateId3.setRoot(TemplateConstants.hitspConsumableTemplateId);
            templateId3.setAssigningAuthorityName("HITSP/C83");
            manProduct.getTemplateId().add(templateId3);
        } else {
            templateId3.setRoot(TemplateConstants.hitspPre25ConsumableHITSPTemplateId);
            templateId3.setAssigningAuthorityName("HITSP/C32");
            manProduct.getTemplateId().add(templateId3);
        }

        POCDMT000040Material manProductMaterial = new POCDMT000040Material();
        manProductMaterial.setClassCode(CDAConstants.CLASS_CODE_MMAT);
        manProductMaterial.setDeterminerCode(EntityDeterminerDetermined.KIND);

        if (medEventConsumable.getAdministerableMaterial() != null) {
            REPCMT000100UV01Material admm = medEventConsumable.getAdministerableMaterial().getAdministerableMaterial();
            if (admm != null) {
                manProductMaterial.setCode(admm.getCode());

                //originalText and reference
                EDExplicit originalText = objectFactory.createEDExplicit();
                TELExplicit telExpl = objectFactory.createTELExplicit();
                telExpl.getNullFlavor().add("UNK");

                originalText.getContent().add(this.objectFactory.createEDExplicitReference(telExpl));

                manProductMaterial.getCode().setOriginalText(originalText);

            }

            if (admm.getDesc() != null) {
                List<Serializable> contents = admm.getDesc().getContent();
                // no need to check for null as getContent() will returns
                // a new ArrayList<Serializable>() when null.
                if (contents.size() > 0) {
                    ENExplicit name = new ENExplicit();
                    Serializable oContent = contents.get(0);
                    name.getContent().add(oContent);
                    manProductMaterial.setName(name);
                }
            }

        }

        manProduct.setManufacturedMaterial(manProductMaterial);
        consumable.setManufacturedProduct(manProduct);

        return consumable;
    }

    /**
     * 
     * @param rxSupply Order information for this medication
     * @return
     */
    private POCDMT000040EntryRelationship buildOrderInfo(REPCMT000100UV01Supply rxSupply) {
        POCDMT000040EntryRelationship entry = new POCDMT000040EntryRelationship();
        POCDMT000040Supply orderEntry = new POCDMT000040Supply();

        log.debug("***********  MEDICATION ORDER INFORMATION  ***********");

        try {
            entry.setTypeCode(XActRelationshipEntryRelationship.REFR);

            II templateId1 = new II();
            if (AssemblyConstants.usePre25Templates()) {
                templateId1.setRoot(TemplateConstants.hitspPre24OrderTemplateId);
                templateId1.setAssigningAuthorityName("HITSP/C32");
                orderEntry.getTemplateId().add(templateId1);
            } else {
                templateId1.setRoot(TemplateConstants.hitspOrderTemplateId);
                templateId1.setAssigningAuthorityName("HITSP/C83");
                orderEntry.getTemplateId().add(templateId1);
                II templateId2 = new II();
                templateId2.setRoot(TemplateConstants.iheOrderTemplateId);
                templateId2.setAssigningAuthorityName("IHE");
                orderEntry.getTemplateId().add(templateId2);
                //CCD
                II templateId3 = new II();
                templateId3.setRoot(TemplateConstants.ccdSupplyActivityTemplateId);
                templateId3.setAssigningAuthorityName("CCD");
                orderEntry.getTemplateId().add(templateId3);
            }

            orderEntry.setClassCode(ActClassSupply.SPLY);
            orderEntry.setMoodCode(XDocumentSubstanceMood.INT);

            // prescription number
            if (rxSupply.getId().size() > 0) {
                orderEntry.getId().add(rxSupply.getId().get(0));
            } else {
                //no id returned- set to nullFlavor
                II supplyId = objectFactory.createII();
                supplyId.getNullFlavor().add("UNK");
                orderEntry.getId().add(templateId1);
            }

            // number of fills
            orderEntry.setRepeatNumber(rxSupply.getRepeatNumber());

            // quantity ordered
            orderEntry.setQuantity(rxSupply.getQuantity());

            // order expiration date/time
            if (rxSupply.getExpectedUseTime() != null) {
                IVXBTSExplicit hiExpectedUseTime = getExpectedUseTimeElement(rxSupply.getExpectedUseTime());
                if (hiExpectedUseTime != null) {
                    SXCMTSExplicit expirationDate = new SXCMTSExplicit();
                    expirationDate.setValue(hiExpectedUseTime.getValue());
                    orderEntry.getEffectiveTime().add(expirationDate);
                }
            }

            // order information
            List<REPCMT000100UV01Author3> rxSupplyOrders = rxSupply.getAuthor();
            if (rxSupplyOrders.size() > 0) {
                REPCMT000100UV01Author3 rxSupplyOrder = rxSupplyOrders.get(0);
                POCDMT000040Author orderEntryInfo = new POCDMT000040Author();

                // order date/time
                TSExplicit orderDTM = new TSExplicit();
                orderDTM.setValue(rxSupplyOrder.getTime().getValue());
                orderEntryInfo.setTime(orderDTM);

                // ordered by
                if (rxSupplyOrder.getAssignedEntity1() != null) {
                    COCTMT090000UV01AssignedEntity rxOrderAuthor = rxSupplyOrder.getAssignedEntity1().getValue();
                    POCDMT000040AssignedAuthor orderingProv = new POCDMT000040AssignedAuthor();

                    // ordering provider id
                    for (II id : rxOrderAuthor.getId()) {
                        orderingProv.getId().add(id);
                    }

                    //provider telecom and address
                    ADExplicit addr = objectFactory.createADExplicit();
                    addr.getNullFlavor().add("UNK");
                    orderingProv.getAddr().add(addr);
                    TELExplicit telecom = objectFactory.createTELExplicit();
                    telecom.getNullFlavor().add("UNK");
                    orderingProv.getTelecom().add(telecom);

                    // provider name
                    if (rxOrderAuthor.getAssignedPerson() != null) {
                        COCTMT090000UV01Person rxOrderAuthorName = rxOrderAuthor.getAssignedPerson().getValue();
                        POCDMT000040Person provider = new POCDMT000040Person();
                        XMLUtil.setName(rxOrderAuthorName.getName(), provider);
                        orderingProv.setAssignedPerson(provider);
                    }

                    orderEntryInfo.setAssignedAuthor(orderingProv);
                }

                orderEntry.getAuthor().add(orderEntryInfo);
            }

            entry.setSupply(orderEntry);

        } catch (Exception e) {
            log.error("Received exception(s) in buildOrderInfo()!", e);
        }

        return entry;
    }

    /**
     * 
     * @param typeOfRx type of medication information
     * @return
     */
    private POCDMT000040EntryRelationship buildTypeOfMedInfo(REPCMT000100UV01Observation typeOfRx) {
        POCDMT000040EntryRelationship entry = new POCDMT000040EntryRelationship();
        POCDMT000040Observation rxTypeEntry = new POCDMT000040Observation();

        log.debug("***********  TYPE OF MEDICATION  ***********");

        try {
            entry.setTypeCode(XActRelationshipEntryRelationship.SUBJ);

            rxTypeEntry.getClassCode().add("OBS");
            rxTypeEntry.setMoodCode(XActMoodDocumentObservation.EVN);

            // template identifier for type of medication
            II templateId = new II();
            if (AssemblyConstants.usePre25Templates()) {
                templateId.setAssigningAuthorityName("HITSP/C32");
                templateId.setExtension(TemplateConstants.hitspPre25TypeOfMedTemplateId);
            } else {
                templateId.setAssigningAuthorityName("HITSP/C83");
                templateId.setExtension(TemplateConstants.hitspTypeOfMedTemplateId);
            }

            rxTypeEntry.getTemplateId().add(templateId);

            rxTypeEntry.setCode(typeOfRx.getCode());
            rxTypeEntry.setStatusCode(typeOfRx.getStatusCode());

            entry.setObservation(rxTypeEntry);
        } catch (Exception e) {
            log.error("Received exception(s) in buildTypeOfMedInfo()!", e);
        }

        return entry;
    }

    private POCDMT000040EntryRelationship buildFullillmentsInfo(REPCMT000100UV01Supply rxHistory) {
        POCDMT000040EntryRelationship entry = new POCDMT000040EntryRelationship();
        POCDMT000040Supply rxSupply = new POCDMT000040Supply();

        log.debug("***********  FULLFILLMENT HISTORY INFORMATION  ***********");

        try {
            entry.setTypeCode(XActRelationshipEntryRelationship.REFR);

            rxSupply.setClassCode(ActClassSupply.SPLY);
            rxSupply.setMoodCode(XDocumentSubstanceMood.EVN);

            // dispensed supply activity template
            II supplyActivityTemplate = new II();
            supplyActivityTemplate.setRoot(TemplateConstants.ccdSupplyActivityTemplateId);
            rxSupply.getTemplateId().add(supplyActivityTemplate);

            // prescription number(s)
            List<II> prescriptionIdsList = rxHistory.getId();
            for (II prescriptionId : prescriptionIdsList) {
                rxSupply.getId().add(prescriptionId);
            }

            // dispense date/time
            List<SXCMTSExplicit> effectiveTimes = rxHistory.getEffectiveTime();
            for (SXCMTSExplicit effectiveTime : effectiveTimes) {
                rxSupply.getEffectiveTime().add(effectiveTime);
            }

            // quantity dispensed
            rxSupply.setQuantity(rxHistory.getQuantity());

            // pharmacy
            if (rxHistory.getPerformer().size() > 0) {
                REPCMT000100UV01Performer3 rxHistoryPerformer = rxHistory.getPerformer().get(0);
                if (rxHistoryPerformer.getAssignedEntity1() != null) {
                    POCDMT000040Performer2 rxSupplyDispenserInfo = new POCDMT000040Performer2();
                    POCDMT000040AssignedEntity rxSupplyDispenser = new POCDMT000040AssignedEntity();

                    COCTMT090000UV01AssignedEntity rxHistoryDispenser = rxHistoryPerformer.getAssignedEntity1().getValue();

                    // dispensing location address
                    if (rxHistoryDispenser.getAddr().size() > 0) {
                        for (ADExplicit addr : rxHistoryDispenser.getAddr()) {
                            rxSupplyDispenser.getAddr().add(addr);
                        }
                    }

                    // dispensing location name
                    if (rxHistoryDispenser.getAssignedPerson() != null) {
                        COCTMT090000UV01Person pharmacy = rxHistoryDispenser.getAssignedPerson().getValue();
                        POCDMT000040Person dispenser = new POCDMT000040Person();

                        if (pharmacy.getName().size() > 0) {
                            List<ENExplicit> names = pharmacy.getName();
                            if (names.size() > 0) {
                                ENExplicit name = names.get(0);
                                for (int i = 0; i < name.getContent().size(); i++) {
                                    Object obj = name.getContent().get(i);
                                    if (obj != null && obj.getClass().getName().equalsIgnoreCase("java.lang.String")) {
                                        PNExplicit pObj = new PNExplicit();
                                        pObj.getContent().add((String) obj);
                                        dispenser.getName().add(pObj);
                                        rxSupplyDispenser.setAssignedPerson(dispenser);

                                        log.debug("Added pharmacy=" + (String) obj);

                                        break;
                                    }
                                }
                            }
                        }
                    }

                    rxSupplyDispenserInfo.setAssignedEntity(rxSupplyDispenser);
                    rxSupply.getPerformer().add(rxSupplyDispenserInfo);
                }
            }

            entry.setSupply(rxSupply);
        } catch (Exception e) {
            log.error("Received exception(s) in buildFullillmentsInfo()!", e);
        }

        return entry;
    }

    private POCDMT000040EntryRelationship buildStatusOfMed(REPCMT000100UV01Observation rxStatusObs) {
        POCDMT000040EntryRelationship statusEntryRelationship = new POCDMT000040EntryRelationship();

        statusEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.REFR);

        POCDMT000040Observation statusObs = new POCDMT000040Observation();

        statusObs.getClassCode().add(CDAConstants.CLASS_CODE_OBS);
        statusObs.setMoodCode(XActMoodDocumentObservation.INT);

        CD code = new CD();
        code.setCode(CDAConstants.LOINC_STATUS_CODE);
        code.setDisplayName(AssemblyConstants.LOINC_STATUS_CODE_DISPLAY_NAME);
        code.setCodeSystem(CDAConstants.LOINC_CODE_SYS_OID);
        code.setCodeSystemName(CDAConstants.LOINC_SYS_NAME);
        statusObs.setCode(code);

        CS statusCode = new CS();
        statusCode.setCode(CDAConstants.STATUS_CODE_COMPLETED);
        statusObs.setStatusCode(statusCode);

        statusObs.getValue().add(rxStatusObs.getValue());

        statusEntryRelationship.setObservation(statusObs);

        return statusEntryRelationship;
    }

    private IVXBTSExplicit getExpectedUseTimeElement(IVLTSExplicit time) {
        if (time != null && time.getContent().size() > 0) {
            JAXBElement o = (JAXBElement) time.getContent().get(0);
            if (o != null && o.getValue().getClass().getName().equalsIgnoreCase("org.hl7.v3.IVXBTSExplicit")) {
                IVXBTSExplicit ob = (IVXBTSExplicit) o.getValue();
                return ob;
            }
        }
        return null;
    }
}
