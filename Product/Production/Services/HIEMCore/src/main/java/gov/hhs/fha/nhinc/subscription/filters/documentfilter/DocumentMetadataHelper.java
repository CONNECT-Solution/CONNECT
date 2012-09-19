/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
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

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(DocumentMetadataHelper.class);

    public static ExtrinsicObjectType findMatchingDocumentMetadata(RegistryObjectListType documentMetadatas,
            DocumentRequest document) {
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

        if (((NullChecker.isNotNullish(documentMetadataDocumentId)) && (NullChecker
                .isNotNullish(documentMetadataRepositoryId)))
                && ((document.getDocumentUniqueId().contentEquals(documentMetadataDocumentId)) && (document
                        .getRepositoryUniqueId().contentEquals(documentMetadataRepositoryId)))) {
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
        String formattedPatientId = findExternalIdentifierByIdentificationSchema(documentMetaData,
                Constants.PatientIdIdentificationScheme);
        patient.setSubjectIdentifier(PatientIdFormatUtil.parsePatientId(formattedPatientId));
        patient.setAssigningAuthorityIdentifier(PatientIdFormatUtil.parseCommunityId(formattedPatientId));
        return patient;
    }

    public static String findExternalIdentifierByIdentificationSchema(ExtrinsicObjectType documentMetaData,
            String identificationScheme) {
        String value = null;
        List<ExternalIdentifierType> externalIdentifierList = documentMetaData.getExternalIdentifier();
        for (ExternalIdentifierType externalIdentifier : externalIdentifierList) {
            if (externalIdentifier.getIdentificationScheme().contentEquals(identificationScheme)) {
                value = externalIdentifier.getValue();
            }
        }
        return value;
    }

    public static String findClassificationByClassificationSchema(ExtrinsicObjectType documentMetaData,
            String classificationScheme) {
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
        return findClassificationByClassificationSchema(documentMetaData,
                Constants.DocumentClassCodeClassificationScheme);
    }
}
