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
package gov.hhs.fha.nhinc.documentassembly;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyManager;
import gov.hhs.fha.nhinc.assemblymanager.DocumentType;
import gov.hhs.fha.nhinc.utils.HashCodeUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.documentassembly.ebxml.EBXMLRequestBuilder;
import gov.hhs.fha.nhinc.documentassembly.ebxml.parsers.AdhocQueryRequestParser;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.IVXBTSExplicit;

public class AdapterDocumentAssemblyHelper {

    private static final String C32_CLASS_CODE_ID = "34133-9";
    private static final String C62_CLASS_CODE_ID = "18842-5";
    private static final String C62_RR_CLASS_CODE_ID = "18726-0";
    private static final String UUID_PREFIX = "urn:uuid:";
    private static Log log = LogFactory.getLog(AdapterDocumentAssemblyHelper.class);

    /*
     * This method is called once per document type requested for a patient
     */
    public static RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType dynamicAssemblyQuery(RespondingGatewayCrossGatewayQueryRequestType request) {

        /* Instantiate Required Objects */
        RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType response =
            new RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType();

        EBXMLRequestBuilder ebxmlBuilder = new EBXMLRequestBuilder();
        AssemblyManager assembler = new AssemblyManager();

        String documentId = "";

        AdhocQueryRequestParser queryParser =
            new AdhocQueryRequestParser(request.getAdhocQueryRequest());

        /* 
         * If the request Status  ""$XDSDocumentEntryStatus"" is not one of
         *
         * Approved OR None specified
         *
         * The gateway will not respond.
         */
        boolean approvedFound = false;
        boolean statusIsNull = false;
        List<String> statusValues = queryParser.getStatusValues();
        if (statusValues == null) {
            statusIsNull = true;
        } else {
            for (String item : statusValues) {
                if (item.contains("Approved")) {
                    approvedFound = true;
                    log.info("'$XDSDocumentEntryStatus:Approved' found in request");
                    continue;
                }
                if (item.contains("DeferredCreation")) {
                    log.info("'$XDSDocumentEntryStatus:DeferredCreation' found in request");
                    continue;
                }
            }
        }
        /* If status is acceptable, proceed */
        if (approvedFound || statusIsNull) {
            // get query parameters:  document type requested, patient identifier
            String docTypeId = queryParser.getDocType();
            String patientId = queryParser.getPatientId();
            docTypeId = docTypeId.replaceAll("'", "");
            patientId = patientId.replaceAll("'", "");

            // get query parameters:  service start times, service stop times
            Date queryServiceStartTimeFrom = queryParser.getServiceStartTimeFrom();
            Date queryServiceStopTimeTo = queryParser.getServiceStartTimeTo();

            Date serviceStartTimeFrom = null;
            Date serviceStopTimeTo = null;

            DocumentType documentType = null;
            try {
                //TODO: How does the assembler know what document type to process
                documentType = assembler.getDocumentType(docTypeId);

                if (documentType != null) {
                    String documentTypeDisplayName = documentType.getDisplayName();
                    String documentTypeClassCodeId = documentType.getTypeId();

                    log.info("Assembler request to build document " + documentTypeDisplayName +
                        " with document class code = " + documentTypeClassCodeId + " for patientId = " + patientId);

                    // Assemble the document via Common Data Layer calls to the associated EMR/EHR system
                    List<POCDMT000040ClinicalDocument> assembledDocs = assembler.getDocuments(documentTypeClassCodeId, patientId);

                    if (assembledDocs != null) {
                        ProvideAndRegisterDocumentSetRequestType documentSetRequest = null;
                        ExtrinsicObjectType metadata = null;
                        RegistryPackageType registryPkg = null;
                        AssociationType1 association = null;
                        ClassificationType classification = null;
                        String uniqueHash = null;
                        String clinicalUniqueHash = null;
                        int numDocs = assembledDocs.size();
                        oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType rol =
                            new oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType();
                        SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
                        submitObjects.setRegistryObjectList(rol);

                        log.info("Assembler returned " + numDocs + " of type " + documentTypeClassCodeId + " for patientid " + patientId);

                        for (int docNum = 0; docNum < numDocs; docNum++) {
                            // Initialize key variabl;es upon processing a new document
                            uniqueHash = null;
                            clinicalUniqueHash = null;
                            //documentSetRequest = null;

                            /* Filter Out C62 documents that don't meet the requested servie time specification
                             * before we waste any more time processing the C62 document
                             */
                            if (documentTypeClassCodeId.equals(C62_CLASS_CODE_ID) || documentTypeClassCodeId.equals(C62_RR_CLASS_CODE_ID)) {
                                JAXBElement o = (JAXBElement) assembledDocs.get(docNum).getDocumentationOf().get(0).getServiceEvent().getEffectiveTime().getContent().get(0);
                                IVXBTSExplicit ob = (IVXBTSExplicit) o.getValue();
                                String serviceTime = ob.getValue();
                                serviceStartTimeFrom = null;
                                serviceStopTimeTo = null;
                                Date parsedServiceTime = null;
                                String formatString = prepareDateFormatString("yyyyMMddhhmmss", serviceTime);
                                parsedServiceTime = new SimpleDateFormat(formatString).parse(serviceTime);
                                serviceStartTimeFrom = parsedServiceTime;
                                serviceStopTimeTo = parsedServiceTime;
                                if (!(queryServiceStartTimeFrom == null) && !(queryServiceStopTimeTo == null) &&
                                    !isWithinRange(serviceStartTimeFrom, queryServiceStartTimeFrom, queryServiceStopTimeTo)) {
                                    continue;
                                }
                            }

                            Document document = new Document();

                            // Compute a clinical unique hash that represents the document so that it can be compared
                            // with future documents to determine if they are differenbt

                            // Save and then reset the docid to avoid putting it in clinicalHash
                            String savedDocId = assembledDocs.get(docNum).getId().getRoot();
                            assembledDocs.get(docNum).getId().setRoot(""); //temp value
                            log.info("Extracted doc id =" + savedDocId);

                            //Save and then reset document time and author times if they exist
                            TSExplicit savedTime, savedAuthorTime;
                            TSExplicit tempTime = new TSExplicit();
                            tempTime.setValue("000000000000000-0000");//temporary value
                            savedTime = assembledDocs.get(docNum).getEffectiveTime();
                            assembledDocs.get(docNum).setEffectiveTime(tempTime);
                            savedAuthorTime = assembledDocs.get(docNum).getAuthor().get(0).getTime();
                            assembledDocs.get(docNum).getAuthor().get(0).setTime(tempTime);

                            if (assembledDocs.get(docNum).getAuthor().size() == 2) {
                                assembledDocs.get(docNum).getAuthor().get(1).setTime(tempTime);
                            }

                            // save the document text
                            EDExplicit savedText = new EDExplicit();
                            if (assembledDocs.get(docNum).getComponent() != null && assembledDocs.get(docNum).getComponent().getNonXMLBody() != null) {
                                savedText = assembledDocs.get(docNum).getComponent().getNonXMLBody().getText();
                                assembledDocs.get(docNum).getComponent().getNonXMLBody().setText(null);
                            }

                            // generate clinical unique hash for document (without date and id)
                            byte[] xmlBytes = XMLUtil.toCanonicalXMLBytes(assembledDocs.get(docNum));
                            clinicalUniqueHash = HashCodeUtil.calculateHashCode(xmlBytes);

                            // put back ClinicalDocument.id
                            assembledDocs.get(docNum).getId().setRoot(savedDocId);
                            //log.info("Clinical Document- ADDED savedDocId =" + XMLUtil.toCanonicalXMLString(assembledDocs.get(docNum)));

                            //put back the Effective Time and the author time
                            assembledDocs.get(docNum).setEffectiveTime(savedTime);
                            assembledDocs.get(docNum).getAuthor().get(0).setTime(savedAuthorTime);

                            if (assembledDocs.get(docNum).getAuthor().size() == 2) {
                                assembledDocs.get(docNum).getAuthor().get(1).setTime(savedAuthorTime);
                            }

                            // Put back the text
                            if (assembledDocs.get(docNum).getComponent() != null && assembledDocs.get(docNum).getComponent().getNonXMLBody() != null) {
                                //put back Text
                                assembledDocs.get(docNum).getComponent().getNonXMLBody().setText(savedText);
                            }

                            // generate unique hash for document (with dates id and body)
                            xmlBytes = assembler.serializeCDAContentToXMLBytes(assembledDocs.get(docNum));
                            String docSize = "" + xmlBytes.length;
                            uniqueHash = HashCodeUtil.calculateHashCode(xmlBytes);

                            // use the document id from C32
                            documentId = savedDocId;
                            document.setId(UUID_PREFIX + documentId);
                            document.setValue(xmlBytes);
                            log.info("Set documentId to = " + document.getId());

                            //get document title
                            String docTitle = "";
                            docTitle = assembledDocs.get(docNum).getTitle().getContent().get(0).toString();

                            documentSetRequest = ebxmlBuilder.createDocumentSetRequest();
                            /*                           if (docNum > 0 && documentTypeClassCodeId.equals(C62_CLASS_CODE_ID))
                            {
                            //ebxmlBuilder.addAdditionalMetadata(metadata, clinicalUniqueHash, serviceStartTimeFrom, serviceStopTimeTo);
                            }
                            else
                             */
                            {
                                // build metadata - need to pass doc title since it determines whether or not doc is
                                // ER Discharge Summary or just Discharge Summary
                                metadata =
                                    ebxmlBuilder.createMetadata(queryParser.getISOFormatPatientId(),
                                    documentId,
                                    clinicalUniqueHash,
                                    uniqueHash,
                                    (assembledDocs.get(docNum).getRecordTarget().size() > 0 ? assembledDocs.get(docNum).getRecordTarget().get(0) : null),
                                    documentType,
                                    serviceStartTimeFrom,
                                    serviceStopTimeTo,
                                    docSize, docTitle);

                                registryPkg =
                                    ebxmlBuilder.createRegistryPackage(queryParser.getISOFormatPatientId(), documentType, docTitle);

                                association = ebxmlBuilder.createAssociation(documentId);

                                classification = ebxmlBuilder.createClassification();
                            }
                            documentSetRequest.getDocument().add(document);
                            submitObjects = createSubmitObjectsRequest(submitObjects, metadata, registryPkg, association, classification);
                            log.info("Built document " + docNum + " of type = " + documentType.getDisplayName() + ", id = " + documentId + ", " +
                                "hash = " + uniqueHash + ", clinical hash = " + clinicalUniqueHash + " Size = " + docSize);
                        } // end of FOR LOOP on documents of the same type
                        // Build the RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequest response
                        // using the documents already added
                        if (documentSetRequest == null) {
                            response = null;
                        } else {
                            documentSetRequest.setSubmitObjectsRequest(submitObjects);
                            response.setProvideAndRegisterDocumentSetRequest(documentSetRequest);
                            response.setAssertion(request.getAssertion());
                        }
                    } else {
                        log.info("No document found...for document type \"" + documentType.getDisplayName() + "\" and patientId = " + patientId);
                        response = null;
                    }
                } else {
                    log.error("ERROR: Document type not specified on return from document assembler ");
                }
            } catch (JAXBException ex) {
                log.error("Parsing error-" + ex.getMessage(), ex);
            } catch (Exception itp) {
                log.error(itp.getMessage(), itp);
            }
            return response;
        } else // Approved not a requested status
        {
            return null;
        }
    }

    private static SubmitObjectsRequest createSubmitObjectsRequest(
        SubmitObjectsRequest submitObjects,
        ExtrinsicObjectType metadata,
        RegistryPackageType registryPkg,
        AssociationType1 association,
        ClassificationType classification) {

        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory factory =
            new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

        oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType registryObjectList = submitObjects.getRegistryObjectList();
        // submission
        registryObjectList.getIdentifiable().add(factory.createExtrinsicObject(metadata));
        registryObjectList.getIdentifiable().add(factory.createRegistryPackage(registryPkg));
        registryObjectList.getIdentifiable().add(factory.createAssociation(association));
        registryObjectList.getIdentifiable().add(factory.createClassification(classification));

        return submitObjects;
    }

    /**
     * Prepare a date format string based on the length of the date string
     * to be parsed.
     * @see gov.hhs.fha.nhinc.repository.util.DocumentLoadUtil
     * @param dateFormat Date format string (ex. yyyyMMddhhmmssZ)
     * @param dateString Date string to be parsed (ex. 19990205)
     * @return Modified format string based on the date string length (ex. yyyyMMdd)
     */
    public static String prepareDateFormatString(String dateFormat, String dateString) {
        String formatString = dateFormat;
        if ((dateString != null) && (dateFormat != null) && (dateString.length() > 0) && (dateString.length() < dateFormat.length())) {
            formatString = dateFormat.substring(0, dateString.length());
            if (log.isDebugEnabled()) {
                log.debug("New dateFormat: " + dateFormat);
            }
        }
        return formatString;
    }

    public static boolean isWithinRange(Date serviceDate, Date startDate, Date stopDate) {
        return serviceDate.after(startDate) && serviceDate.before(stopDate);
    }
}
