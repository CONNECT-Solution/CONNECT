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

package gov.hhs.fha.nhinc.subscription.filters.documentfilter;
//import gov.hhs.fha.nhinc.NHINCLib.NullChecker;
//import gov.hhs.fha.nhinc.subscription.*;
//import gov.hhs.fha.nhinc.subscriptiondte.*;
//import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
//import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.xml.bind.JAXBElement;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

/**
 *
 * @author rayj
 */
public class DocumentMetadataHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DocumentMetadataHelper.class);

    public static ExtrinsicObjectType findMatchingDocumentMetadata(RegistryObjectListType documentMetadatas, DocumentRequest document) {
        ExtrinsicObjectType matchingDocumentMetadata = null;

        for (int i = 0; i < documentMetadatas.getIdentifiable().size(); i++) {
            IdentifiableType identifiable = documentMetadatas.getIdentifiable().get(i).getValue();
            ExtrinsicObjectType documentmetadata = (ExtrinsicObjectType) identifiable;
            if (areDocumentsMatching(document, documentmetadata)) {
                matchingDocumentMetadata = documentmetadata;
            }
        }
        return matchingDocumentMetadata;
    }

    public static boolean areDocumentsMatching(DocumentRequest document, ExtrinsicObjectType documentMetadata) {
        boolean match = false;
        String documentMetadataDocumentId = findDocumentId(documentMetadata);
        String documentMetadataRepositoryId = findRepositoryId(documentMetadata);

        if (((NullChecker.isNotNullish(documentMetadataDocumentId)) && (NullChecker.isNotNullish(documentMetadataRepositoryId))) && ((document.getDocumentUniqueId().contentEquals(documentMetadataDocumentId)) && (document.getRepositoryUniqueId().contentEquals(documentMetadataRepositoryId)))) {
            match = true;
        }
        return match;
    }

    public static String findDocumentId(ExtrinsicObjectType documentMetadata) {
        return findExternalIdentifierByIdentificationSchema(documentMetadata, Constants.DocumentIdIdentificationScheme);
    }

    public static String findRepositoryId(ExtrinsicObjectType documentMetadata) {
        String repositoryId = null;
        try {
            repositoryId = AdhocQueryHelper.findSlotValue(documentMetadata.getSlot(), Constants.RepositoryIdSlotName);
        } catch (MultipleSlotValuesFoundException ex) {
            log.error(ex);
        }
        return repositoryId;
    }

    public static QualifiedSubjectIdentifierType getPatient(ExtrinsicObjectType documentMetaData) {
        QualifiedSubjectIdentifierType patient = new QualifiedSubjectIdentifierType();
        String formattedPatientId = findExternalIdentifierByIdentificationSchema(documentMetaData, Constants.PatientIdIdentificationScheme);
        patient.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(formattedPatientId));
        patient.setAssigningAuthorityIdentifier(PatientIdFormatUtil.parseCommunityId(formattedPatientId));
        return patient;
    }

    public static String findExternalIdentifierByIdentificationSchema(ExtrinsicObjectType documentMetaData, String identificationScheme) {
        String value = null;
        List<ExternalIdentifierType> externalIdentifierList = documentMetaData.getExternalIdentifier();
        for (ExternalIdentifierType externalIdentifier : externalIdentifierList) {
            if (externalIdentifier.getIdentificationScheme().contentEquals(identificationScheme)) {
                value = externalIdentifier.getValue();
            }
        }
        return value;
    }

    public static String findClassificationByClassificationSchema(ExtrinsicObjectType documentMetaData, String classificationScheme) {
        String value = null;
        List<ClassificationType> classificationList = documentMetaData.getClassification();
        for (ClassificationType classification : classificationList) {
            if (classification.getClassificationScheme().contentEquals(classificationScheme)) {
                value = classification.getNodeRepresentation();
            }
        }
        return value;
    }

    public static String getDocumentClassCode(ExtrinsicObjectType documentMetaData) {
        return findClassificationByClassificationSchema(documentMetaData, Constants.DocumentClassCodeClassificationScheme);
    }
}
