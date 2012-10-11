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

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.Author;
import gov.hhs.fha.nhinc.assemblymanager.service.DataService;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.Base64Coder;

import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.util.List;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.II;
import org.hl7.v3.CS;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Organization;
import org.hl7.v3.POCDMT000040CustodianOrganization;
import org.hl7.v3.POCDMT000040DataEnterer;
import org.hl7.v3.RCMRIN000032UV01QUQIMT120001UV01Subject2;
import org.hl7.v3.FindDocumentWithContentRCMRIN000032UV01ResponseType;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040Author;
import org.hl7.v3.POCDMT000040AuthoringDevice;
import org.hl7.v3.POCDMT000040Person;
import org.hl7.v3.TSExplicit;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;

/**
 *
 * @author MIDHT Contract Development
 */
public class C62DocumentBuilder extends DocumentBuilder {

    private static Log log = LogFactory.getLog(C62DocumentBuilder.class);
    private static String endpoint = AssemblyConstants.DAS_DATASERVICE_ENDPOINT;
    private static DataService dataService = new DataService(endpoint);
    private List<POCDMT000040ClinicalDocument> c62DocumentList = null;
    private POCDMT000040ClinicalDocument c62Document = null;
    private String docId = "";

    public C62DocumentBuilder() {
        super();
    }

    public C62DocumentBuilder(String id) {
        super(id);
    }

    public POCDMT000040ClinicalDocument getCdaDocument() {
        return c62Document;
    }

    public String getDocId() {
        return docId;
    }

    public List<POCDMT000040ClinicalDocument> build() throws DocumentBuilderException {

        //check for C62 class code or Radiology Report class code
        if (documentType.getTypeId().equalsIgnoreCase(AssemblyConstants.C62_CLASS_CODE) || documentType.getTypeId().equalsIgnoreCase(AssemblyConstants.C62_RR_CLASS_CODE)) {

            log.debug("Requested C62 has a class code of " + documentType.getTypeId());

            buildC62();

        } else {
            throw new DocumentBuilderException("Document type \"" + documentType.getTypeId() + "\" is not supported.");
        }

        return c62DocumentList;
    }

    private String formatDate(String dateString, String inputFormat, String outputFormat) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
        Date date = null;

        try {
            date = inputFormatter.parse(dateString);
        } catch (Exception ex) {
            log.error(ex);
        }

        return outputFormatter.format(date);
    }

    private void buildC62() throws DocumentBuilderException {

        c62DocumentList = new ArrayList();
        // patient
        II subjectId = new II();
        subjectId.setExtension(patientId);
        subjectId.setRoot(orgOID);

        //call CAL FDWC endpoint - we need to pass appropriate class code in request
        String documentClassCode = documentType.getTypeId();
        log.debug("Building a FDWC request with the following class code entry - " + documentClassCode);

        FindDocumentWithContentRCMRIN000032UV01ResponseType response =
            dataService.findDocumentWithContent(subjectId, endpoint, documentClassCode);

        if (response != null) {
            log.debug("******************  RCMR Response Received from Internal Provider *************");
            log.debug("RCMR Response from EMR: " + XMLUtil.toPrettyXML(response));
        }
        if (response != null && response.getResponse() != null &&
            response.getResponse().getControlActProcess() != null &&
            response.getResponse().getControlActProcess().getSubject() != null &&
            response.getResponse().getControlActProcess().getSubject().size() > 0) {

            //retrieve the home community id for this gateway
            String localHomeCommunityOID = "";

            //retrive the phone number used to build the information source module
            String localHealthSystemTelecom = "";

            try {
                localHomeCommunityOID = PropertyAccessor.getInstance().getProperty("gateway", "localHomeCommunityId");
                localHealthSystemTelecom = PropertyAccessor.getInstance().getProperty("infoSource", "TelecomC62");
            } catch (PropertyAccessException pae) {
                log.error("Error reading from properties files: " + pae);
            }

            for (int i = 0; i < response.getResponse().getControlActProcess().getSubject().size(); i++) {
                // Extract CDA from RCMR response
                // The C62 CDA document is encoded in the RCMR package as non-XML text
                RCMRIN000032UV01QUQIMT120001UV01Subject2 subject = response.getResponse().getControlActProcess().getSubject().get(i);
                if (subject.getClinicalDocument() != null && subject.getClinicalDocument().getText() != null) {

                    // Change effective time to complete time with TimeZone offset
                    String effectiveTimeC62 = subject.getClinicalDocument().getEffectiveTime().getValue();
                    log.debug("EffectiveTime from RCMR = " + effectiveTimeC62);
                    effectiveTimeC62 = formatDate(effectiveTimeC62, "yyyyMMddHHmmss", "yyyyMMddHHmmssZ");
                    log.debug("EffectiveTime after time zone conversion = " + effectiveTimeC62);

                    EDExplicit text = subject.getClinicalDocument().getText();
                    String encodedDocument = text.getContent().get(0).toString();
                    String xmlDocument = Base64Coder.decodeString(encodedDocument);

                    // Determine if the sender is the Local Home Community ID. Used later to apply patches
                    List<II> senderList = response.getResponse().getSender().getDevice().getId();
                    boolean senderIsLocalOID = false;
                    if (!senderList.isEmpty()) {
                        int k = 0;
                        while (senderList.get(k) != null) {

                            II senderId = senderList.get(k);
                            if (senderId.getRoot().equals(localHomeCommunityOID)) {
                                log.debug("The sender OID matches the local home community OID");
                                senderIsLocalOID = true;
                                break;
                            } else {
                                k++;
                            }
                        }
                    }

                    JAXBElement element = null;
                    try {
                        JAXBContextHandler jch = new JAXBContextHandler();
                        JAXBContext jc = jch.getJAXBContext("org.hl7.v3");

                        Unmarshaller u = jc.createUnmarshaller();
                        StringBuffer xmlStr = new StringBuffer(xmlDocument);
                        Object o = u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
                        element = (JAXBElement) o;
                    } catch (javax.xml.bind.JAXBException e) {
                        log.error("Unable to process RCMR Response from EMR. Document number " + i + " skipped");
                        continue;
                    }
                    org.hl7.v3.ObjectFactory obf = new org.hl7.v3.ObjectFactory();
                    JAXBElement<POCDMT000040ClinicalDocument> clinDocElement = obf.createClinicalDocument((POCDMT000040ClinicalDocument) element.getValue());

                    c62Document = clinDocElement.getValue();
                    c62Document.getId().setRoot(UUIDGenerator.generateUUIDFromString(subject.getClinicalDocument().getId().getExtension()));
                    // Specific Fixes for data from McKesson System
                    // Not able to engineer these fixes into the RCMR document
                    // So we patch up the C62 document returned in the RCMR below

                    // FIXUPS for Document Type C62
                    // ClinicalDocument.effectiveTime: Signifies the document creation time, when the document
                    // first came into being. For C32s that is the time the document is generated.
                    // Cxx must have a time zone specifier.
                    TSExplicit effectiveTimeTS = obf.createTSExplicit();
                    UTCDateUtil utcTime = new UTCDateUtil();
                    String effectiveTime = utcTime.formatUTCDate(Calendar.getInstance().getTime()) + "-0000";
                    effectiveTimeTS.setValue(effectiveTime);
                    c62Document.setEffectiveTime(effectiveTimeTS);

                    //build & add the realmCode
                    CS csObj = new CS();
                    csObj.setCode("US");
                    c62Document.getRealmCode().add(csObj);

                    List<II> templateIdList = c62Document.getTemplateId();

                    II realmCodeTemplateId = new II();
                    realmCodeTemplateId.setRoot("2.16.840.1.113883.10.20.3"); //supports the realmCode value
                    templateIdList.add(realmCodeTemplateId);

                    // build & add the MedicalDocumentTemplate
                    II pccMedicalDocsTemplateId = new II();
                    pccMedicalDocsTemplateId.setRoot("1.3.6.1.4.1.19376.1.5.3.1.1.1"); //supports PCC Medical Docs
                    templateIdList.add(pccMedicalDocsTemplateId);

                    // Add the Unstructured Document Template Id to the List of the Root Template Ids
                    II unstructuredDocumentTemplateId = new II();
                    unstructuredDocumentTemplateId.setRoot("2.16.840.1.113883.10.20.19.1");
                    templateIdList.add(unstructuredDocumentTemplateId);

                    // Add the C62 TemplateId to the List of the Root Template Ids
                    II c62TemplateId = new II();
                    c62TemplateId.setRoot("2.16.840.1.113883.3.88.11.62.1");
                    c62TemplateId.setAssigningAuthorityName("HITSP C62");
                    templateIdList.add(c62TemplateId);

                    if (senderIsLocalOID) {
                        // Create an UNKNWON address element
                        ADExplicit unknownAddr = new ADExplicit();
                        unknownAddr.getNullFlavor().add("UNK");

                        //Create an UNKNOWN telephone element
                        TELExplicit unknownTele = new TELExplicit();
                        unknownTele.getNullFlavor().add("UNK");

                        //parse assigned person value from RCMR response...

                        //assigned person name comes from McKesson
                        org.hl7.v3.POCDMT000040Person assignedPerson = new org.hl7.v3.POCDMT000040Person();
                        assignedPerson.getName().add(subject.getClinicalDocument().getAuthor().get(0).getAssignedAuthor().getAssignedPerson().getValue().getName().get(0));
                        assignedPerson.getName().get(0).getUse().add("L");  //indicates legal name

                        // Fix up the content of the recordTarget
                        if (c62Document.getRecordTarget() != null) {
                            List<TELExplicit> tele = c62Document.getRecordTarget().get(0).getPatientRole().getTelecom();
                            if (tele.isEmpty()) {
                                // Say we ton't know the telephone numhber
                                tele.add(0, unknownTele);
                            }
                        }

                        //Fix author templateId
                        // c62Document.getTemplateId().clear();
                        //II authTemplateId = obf.createII();
                        // authTemplateId.setRoot("1.3.6.1.4.1.19376.1.2.20.1");
                        c62Document.getAuthor().get(0).getTemplateId().get(0).setRoot("1.3.6.1.4.1.19376.1.2.20.1");

                        // Fix author time
                        c62Document.getAuthor().get(0).getTime().setValue(effectiveTimeC62);

                        //create a temp authoring device before deleting it from C62documentobject
                        POCDMT000040AuthoringDevice tempAuthoringDevice = c62Document.getAuthor().get(0).getAssignedAuthor().getAssignedAuthoringDevice();

                        // Fix up the content of AssignedAuthor
                        if (c62Document.getAuthor().get(0).getAssignedAuthor() != null) {
                            // Provide an assignedPerson
                            c62Document.getAuthor().get(0).getAssignedAuthor().setAssignedPerson(assignedPerson);

                            // (1) Provide a required id value for AssignedAuthor
                            List<II> idList = c62Document.getAuthor().get(0).getAssignedAuthor().getId();
                            if (idList.isEmpty()) {
                                II id = new II();
                                id.setRoot(orgOID);
                                idList.add(id);
                            }

                            // (2) AssignedAuthor must have an addr element
                            List<ADExplicit> addrList = c62Document.getAuthor().get(0).getAssignedAuthor().getAddr();
                            if (addrList.isEmpty()) {
                                addrList.add(unknownAddr);
                            }

                            // (3) AssignedAuthor must have a telcom element
                            List<TELExplicit> telList = c62Document.getAuthor().get(0).getAssignedAuthor().getTelecom();
                            if (telList.isEmpty()) {
                                telList.add(unknownTele);
                            }
                            // Cannot have both <AssignedAuthor> and <AssignedAuthoringDevice>
                            c62Document.getAuthor().get(0).getAssignedAuthor().setAssignedAuthoringDevice(null);
                        }

                        //  POCDMT000040Organization  tempRepresentedOrganization = obf.createPOCDMT000040Organization();

                        POCDMT000040Organization tempRepresentedOrganization = obf.createPOCDMT000040Organization();

                        //declare objects to hold values from property file
                        String rOTelecom = null;
                        String rOCity = null;
                        String rOStreet = null;
                        String rOState = null;
                        String rOZip = null;

                        // Fix up the content of RepresentedOrganization
                        if (subject.getClinicalDocument().getAuthor().get(0).getAssignedAuthor().getRepresentedOrganization().getValue().getName() != null) {

                            //grab the name from McKesson and populate temp object
                            tempRepresentedOrganization.getName().add(subject.getClinicalDocument().getAuthor().get(0).getAssignedAuthor().getRepresentedOrganization().getValue().getName().get(0));

                            log.debug("Parsed Represented Organization Name from RCMR = " + extractStringFromSerializableArray(subject.getClinicalDocument().getAuthor().get(0).getAssignedAuthor().getRepresentedOrganization().getValue().getName().get(0).getContent()));

                            try {
                                //check the name value from RCMR response and then do lookup for appropriate addr and telecom
                                if (extractStringFromSerializableArray(subject.getClinicalDocument().getAuthor().get(0).getAssignedAuthor().getRepresentedOrganization().getValue().getName().get(0).getContent()).equals(getInfoSourceProperty("HospitalName"))) {
                                    log.debug("C62 Represented Organization Name = " + getInfoSourceProperty("HospitalName"));

                                    rOTelecom = getInfoSourceProperty("TelecomC62");
                                    rOCity = getInfoSourceProperty("City");
                                    rOStreet = getInfoSourceProperty("Street");
                                    rOState = getInfoSourceProperty("State");
                                    rOZip = getInfoSourceProperty("Zip");
                                } else if (extractStringFromSerializableArray(subject.getClinicalDocument().getAuthor().get(0).getAssignedAuthor().getRepresentedOrganization().getValue().getName().get(0).getContent()).equals(getInfoSourceProperty("MedicalCenterName"))) {
                                    log.debug(" C62 Represented Organization Name = " + getInfoSourceProperty("MedicalCenterName"));

                                    rOTelecom = getInfoSourceProperty("MedicalCenterTelecom");
                                    rOCity = getInfoSourceProperty("MedicalCenterCity");
                                    rOStreet = getInfoSourceProperty("MedicalCenterStreet");
                                    rOState = getInfoSourceProperty("State");
                                    rOZip = getInfoSourceProperty("MedicalCenterZip");
                                } else if (extractStringFromSerializableArray(subject.getClinicalDocument().getAuthor().get(0).getAssignedAuthor().getRepresentedOrganization().getValue().getName().get(0).getContent()).equals(getInfoSourceProperty("SecondaryMedicalCenterName"))) {
                                    log.debug("C62 Represented Organization Name =  " + getInfoSourceProperty("SecondaryMedicalCenterName"));

                                    rOTelecom = getInfoSourceProperty("SecondaryMedicalCenterTelecom");
                                    rOCity = getInfoSourceProperty("SecondaryMedicalCenterCity");
                                    rOStreet = getInfoSourceProperty("SecondaryMedicalCenterStreet");
                                    rOState = getInfoSourceProperty("State");
                                    rOZip = getInfoSourceProperty("SecondaryMedicalCenterZip");
                                }
                            } catch (Exception e) {
                                log.error("Exception creating C62 RepresnetedOrganization: " + e);
                            }

                            //build out temp RO telecom & addr with values from properties file

                            //telecom
                            TELExplicit tele = obf.createTELExplicit();

                            if (rOTelecom != null & !rOTelecom.isEmpty()) {
                                tele.setValue(rOTelecom);
                            } else {
                                //telecom does not exist - build out null flavor
                                tele = unknownTele;
                            }

                            //addr
                            ADExplicit roAddr = obf.createADExplicit();

                            //city
                            org.hl7.v3.AdxpExplicitCity city = new org.hl7.v3.AdxpExplicitCity();
                            city.setContent(rOCity);

                            //street
                            org.hl7.v3.AdxpExplicitStreetAddressLine street = new org.hl7.v3.AdxpExplicitStreetAddressLine();
                            street.setContent(rOStreet);

                            //state
                            org.hl7.v3.AdxpExplicitState state = new org.hl7.v3.AdxpExplicitState();
                            state.setContent(rOState);

                            //zip
                            org.hl7.v3.AdxpExplicitPostalCode zip = new org.hl7.v3.AdxpExplicitPostalCode();
                            zip.setContent(rOZip);

                            roAddr.getContent().add(obf.createADExplicitStreetAddressLine(street));
                            roAddr.getContent().add(obf.createADExplicitCity(city));
                            roAddr.getContent().add(obf.createADExplicitState(state));
                            roAddr.getContent().add(obf.createADExplicitPostalCode(zip));

                            if (roAddr.getContent() == null || roAddr.getContent().isEmpty()) {
                                roAddr = unknownAddr;
                            }

                            tempRepresentedOrganization.getAddr().add(roAddr);
                            tempRepresentedOrganization.getTelecom().add(tele);

                        }

                        c62Document.getAuthor().get(0).getAssignedAuthor().setRepresentedOrganization(tempRepresentedOrganization);

                        //create device author section for addition to C62
                        Author authInstance = new Author();
                        POCDMT000040Author deviceAuthor = authInstance.build();
                        deviceAuthor.setContextControlCode("OP");
                        deviceAuthor.getTypeCode().add(subject.getClinicalDocument().getAuthor().get(0).getTypeCode().get(0));

                        //fix time
                        deviceAuthor.getTime().setValue(effectiveTimeC62);

                        //fix template id
                        II authDeviceTemplateId = obf.createII();
                        authDeviceTemplateId.setRoot("1.3.6.1.4.1.19376.1.2.20.2");
                        deviceAuthor.getTemplateId().add(authDeviceTemplateId);

                        //fix assigned author
                        deviceAuthor.getAssignedAuthor().setClassCode("ASSIGNED");
                        deviceAuthor.getAssignedAuthor().getAddr().clear();
                        deviceAuthor.getAssignedAuthor().getAddr().add(unknownAddr);
                        deviceAuthor.getAssignedAuthor().getTelecom().clear();
                        deviceAuthor.getAssignedAuthor().getTelecom().add(unknownTele);

                        //delete assigned person - if it exists
                        if (deviceAuthor.getAssignedAuthor().getAssignedPerson() != null) {
                            deviceAuthor.getAssignedAuthor().setAssignedPerson(null);
                        }

                        //fix assigned authoring device
                        deviceAuthor.getAssignedAuthor().setAssignedAuthoringDevice(tempAuthoringDevice);

                        //fix represented organization telecom
                        deviceAuthor.getAssignedAuthor().getRepresentedOrganization().getTelecom().get(0).setValue(rOTelecom);

                        c62Document.getAuthor().add(deviceAuthor);



                        // Fix up /ClinicalDocument/DateEnterer
                        if (c62Document.getDataEnterer().getAssignedEntity() != null) {
                            // Fix the time with TZ
                            POCDMT000040DataEnterer de = c62Document.getDataEnterer();
                            de.getTime().setValue(effectiveTimeC62);

                            // Add the assignedPerson
                            //value is equal to the assignedauthoring device name
                            POCDMT000040Person tempAssignedPerson = obf.createPOCDMT000040Person();
                            PNExplicit assPersonName = obf.createPNExplicit();
                            assPersonName.getContent().add(tempAuthoringDevice.getSoftwareName().getContent().get(0));
                            tempAssignedPerson.getName().add(assPersonName);

                            de.getAssignedEntity().setAssignedPerson(tempAssignedPerson);

                            // (1) Add addr
                            List<ADExplicit> addrList = c62Document.getDataEnterer().getAssignedEntity().getAddr();
                            if (addrList.isEmpty()) {
                                addrList.add(unknownAddr);
                            }
                            // (2) Add Tele
                            List<TELExplicit> telList = c62Document.getDataEnterer().getAssignedEntity().getTelecom();
                            if (telList.isEmpty()) {
                                telList.add(unknownTele);
                            }
                        }
                        // Add the Health System Telephone Number to /ClinicalDOcument/AssignedEntity/RepresentedOrganization
                        POCDMT000040Organization ro = c62Document.getDataEnterer().getAssignedEntity().getRepresentedOrganization();
                        List<TELExplicit> telList = ro.getTelecom();
                        if (telList.isEmpty()) {
                            TELExplicit tele = new TELExplicit();
                            tele.setValue("tel:+1-" + localHealthSystemTelecom);
                            telList.add(tele);
                        }

                        // Add the Health System Telephone Number to ClinicalDocument[1]/custodian[1]/assignedCustodian[1]/representedCustodianOrganization[1]
                        POCDMT000040CustodianOrganization co = c62Document.getCustodian().getAssignedCustodian().getRepresentedCustodianOrganization();
                        TELExplicit tele = co.getTelecom();
                        if (tele == null) {
                            tele = new TELExplicit();
                            tele.setValue("tel:+1-" + localHealthSystemTelecom);
                            co.setTelecom(tele);
                        }
                    }

                    //fix service event effectiveTime
                    IVLTSExplicit time = c62Document.getDocumentationOf().get(0).getServiceEvent().getEffectiveTime();
                    time.getContent().clear();
                    IVXBTSExplicit lowVal = new IVXBTSExplicit();
                    lowVal.setValue(effectiveTimeC62);
                    time.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

                    IVXBTSExplicit highVal = new IVXBTSExplicit();
                    highVal.setValue(effectiveTimeC62);
                    time.getContent().add(this.objectFactory.createIVLTSExplicitHigh(highVal));

                    c62DocumentList.add(c62Document);

                    log.debug("C62 Clinical Document Assembly Complete. Content follows:");
                    log.debug(XMLUtil.toPrettyXML(c62Document));

                }
            }
        } else {
            log.info("No documents for patient(" + subjectId.getExtension() + "), docId(" + docId + ")");
        }
    }

    private String extractStringFromSerializableArray(List<Serializable> olHL7Serializable) {
        String sText = null;

        if ((olHL7Serializable != null) &&
            (olHL7Serializable.size() > 0)) {
            StringBuilder sbText = new StringBuilder();
            for (Serializable oSerialElement : olHL7Serializable) {
                if (oSerialElement instanceof String) {
                    String sElement = (String) oSerialElement;
                    if (sElement.length() > 0) {
                        sbText.append(sElement);
                    }
                }
            }   // for (Serializable oSerialElement : oHL7Sc.getContent())

            if (sbText.length() > 0) {
                sText = sbText.toString();
            }
        }   // if ((oHL7Sc != null) && ...

        return sText;
    }
}
