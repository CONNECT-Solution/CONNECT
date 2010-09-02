/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.documentassembly;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyManager;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DocumentType;
import gov.hhs.fha.nhinc.assemblymanager.utils.DateUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.HashCodeUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.documentassembly.ebxml.EBXMLRequestBuilder;
import gov.hhs.fha.nhinc.documentassembly.ebxml.parsers.AdhocQueryRequestParser;
import ihe.iti.xds_b._2007.ObjectFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import nux.xom.pool.XOMUtil;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.TSExplicit;

/**
 *
 * @author kim
 */
public class AdapterDocumentAssemblyHelper {

    private static Log log = LogFactory.getLog(AdapterDocumentAssemblyHelper.class);

    public static RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType dynamicAssemblyQuery(RespondingGatewayCrossGatewayQueryRequestType request) {

        RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType response =
                new RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType();

        EBXMLRequestBuilder ebxmlBuilder = new EBXMLRequestBuilder();
        AssemblyManager assembler = new AssemblyManager();

        //Build document object
        Document document = new Document();
        String documentId = "";

        AdhocQueryRequestParser queryParser =
                new AdhocQueryRequestParser(request.getAdhocQueryRequest());

        // get query parameters:  document type requested, patient identifier
        String docTypeId = queryParser.getDocType();
        String patientId = queryParser.getPatientId();
        docTypeId = docTypeId.replaceAll("'", "");
        patientId = patientId.replaceAll("'", "");

        log.info("Document requested: " + docTypeId + "(len:" + docTypeId.length() + ")");

        // get query parameters:  service start times, service stop times
        Date serviceStartTimeFrom = queryParser.getServiceStartTimeFrom();
        Date serviceStopTimeTo = queryParser.getServiceStopTimeTo();

        DocumentType documentType = null;
        try {
            documentType = assembler.getDocumentType(docTypeId);

            if (documentType != null) {
                log.info("Assembler to build: DOCTYPE=" + documentType.getDisplayName());

                POCDMT000040ClinicalDocument assembledDoc = assembler.getDocument(documentType.getTypeId(), patientId);

                // save ClinicalDocument.id
                String savedDocId = assembledDoc.getId().getExtension();
                assembledDoc.getId().setExtension("");
                //log.info("C32-REMOVED ID=" + XMLUtil.toCanonicalXMLString(assembledDoc));
                log.info("C32 - savedDocId =" + savedDocId);

                // generate clinical unique hash for document (without date and id)
                byte[] xmlBytes = XMLUtil.toCanonicalXMLBytes(assembledDoc);
                String clinicalUniqueHash = HashCodeUtil.calculateHashCode(xmlBytes);

                // ClinicalDocument.effectiveTime: Signifies the document creation time, when the document
                // first came into being.
                TSExplicit effectiveTime = new TSExplicit();
                effectiveTime.setValue(DateUtil.convertToCDATime(Calendar.getInstance().getTime()));
                assembledDoc.setEffectiveTime(effectiveTime);

                // Author.time
                if (assembledDoc.getAuthor().size() > 0) {
//                    TSExplicit authorTime = new TSExplicit();
//                    authorTime.setValue(effectiveTime.getValue());
                    assembledDoc.getAuthor().get(0).setTime(effectiveTime);
                }

                // put back ClinicalDocument.id
                //documentId = DocumentIdGenerator.generateDocumentId();
                assembledDoc.getId().setExtension(savedDocId);
                log.info("C32- ADDED savedDocId =" + XMLUtil.toCanonicalXMLString(assembledDoc));

                // generate unique hash for document (with date and id)
                xmlBytes = assembler.serializeCDAContentToXMLBytes(assembledDoc);
                String uniqueHash = HashCodeUtil.calculateHashCode(xmlBytes);

                // use the document id from C32
                documentId = savedDocId;
                document.setId("urn:uuid:" + documentId);
                document.setValue(xmlBytes);
                log.info("Set documentId to =" + document.getId());

                ProvideAndRegisterDocumentSetRequestType documentSetRequest =
                        ebxmlBuilder.createDocumentSetRequest();

                // build metadata
                ExtrinsicObjectType metadata =
                        ebxmlBuilder.createMetadata(queryParser.getISOFormatPatientId(),
                        documentId,
                        clinicalUniqueHash,
                        uniqueHash,
                        (assembledDoc.getRecordTarget().size() > 0 ? assembledDoc.getRecordTarget().get(0) : null),
                        documentType,
                        serviceStartTimeFrom,
                        serviceStopTimeTo);

                RegistryPackageType registryPkg =
                        ebxmlBuilder.createRegistryPackage(queryParser.getISOFormatPatientId(), documentType);

                AssociationType1 association = ebxmlBuilder.createAssociation(documentId);

                ClassificationType classification = ebxmlBuilder.createClassification();

                documentSetRequest.setSubmitObjectsRequest(createSubmitObjectsRequest(metadata, registryPkg, association, classification));
                documentSetRequest.getDocument().add(document);

                response.setProvideAndRegisterDocumentSetRequest(documentSetRequest);
                //response.setAssertion(createAssertion());
                response.setAssertion(request.getAssertion());

                log.info("Built document type=" + documentType + ", id=" + documentId + ", hash=" + uniqueHash +
                        ", clinical hash = " + clinicalUniqueHash);
            } else {
                log.error("Request document \"" + docTypeId + "\" not supported!");
                documentType = new DocumentType();
                documentType.setTypeId(docTypeId);
            }
        } catch (JAXBException ex) {
            log.error("Parsing error-" + ex.getMessage(), ex);
        } catch (Exception itp) {
            log.error(itp.getMessage(), itp);
        }

        return response;
    }

    private static SubmitObjectsRequest createSubmitObjectsRequest(
            ExtrinsicObjectType metadata, RegistryPackageType registryPkg,
            AssociationType1 association, ClassificationType classification) {

        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory factory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

        // build RegistryObjectList
        RegistryObjectListType registryObjectList = new RegistryObjectListType();

        // SubmitObjectsRequest type has two elements: RequestSlotList and RegistryObjectList
        SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();

        // submission
        registryObjectList.getIdentifiable().add(factory.createExtrinsicObject(metadata));
        registryObjectList.getIdentifiable().add(factory.createRegistryPackage(registryPkg));
        registryObjectList.getIdentifiable().add(factory.createAssociation(association));
        registryObjectList.getIdentifiable().add(factory.createClassification(classification));

        submitObjects.setRegistryObjectList(registryObjectList);

        return submitObjects;
    }

}
