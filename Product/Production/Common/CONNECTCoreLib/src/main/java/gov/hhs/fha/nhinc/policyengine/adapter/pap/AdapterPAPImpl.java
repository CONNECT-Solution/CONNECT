/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pap;

import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the policy engine PAP (Policy Access Point).
 *
 * @author mastan.ketha
 */
public class AdapterPAPImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPAPImpl.class);

    /**
     * Get the Access Consent Policy (ACP) document for the patient.
     *
     * @param patientId
     * @return <code>Document</code>
     */
    public DocumentMetadata getPolicyDocumentByPatientId(String patientId) {
        LOG.info("Begin AdapterPAPImpl.getPolicyDocument(...)");
        DocumentMetadata document = new DocumentMetadata();
        try {
            DocumentQueryParams params = new DocumentQueryParams();
            LOG.debug("patientid:" + patientId);
            params.setPatientId(patientId);
            List<String> classCodeValues = new ArrayList<>();
            classCodeValues.add(AdapterPAPConstants.DOCUMENT_CLASS_CODE);
            params.setClassCodes(classCodeValues);
            DocumentService service = new DocumentService();
            List<DocumentMetadata> docs = service.documentQuery(params);
            int docsSize = 0;
            if (CollectionUtils.isNotEmpty(docs)){
                docsSize = docs.size();
                document = docs.get(0);
            }
            LOG.debug("Document size: {}", docsSize);

        } catch (Exception ex) {
            LOG.error("Exception occured while retrieving documents: {}", ex.getLocalizedMessage(), ex);
        }
        LOG.info("End AdapterPAPImpl.getPolicyDocument(...)");
        return document;
    }

    /**
     * Get the Access Consent Policy (ACP) document from the document repository.
     *
     * @param documentId
     * @return <code>Document</code>
     */
    public DocumentMetadata getPolicyDocumentByDocId(Long documentId) {
        LOG.info("Begin AdapterPAPImpl.getPolicyDocumentByDocId(...)");
        DocumentMetadata document = new DocumentMetadata();
        try {
            DocumentService service = new DocumentService();
            document = service.getDocument(documentId);
        } catch (Exception ex) {
            LOG.error("Exception occured while retrieving documents: {}", ex.getLocalizedMessage(), ex);
        }
        LOG.info("End AdapterPAPImpl.getPolicyDocumentByDocId(...)");
        return document;
    }

    /**
     * Saves the document to the repository. If the document id (PK) is null then the document is inserted, else the
     * document is updated
     *
     * @param document
     * @return true - success; false - failure
     */
    public boolean savePolicyDocument(DocumentMetadata document) {
        LOG.info("Begin AdapterPAPImpl.savePolicyDocument(...)");
        boolean isDocSaved = false;
        try {
            if (document == null) {
                LOG.warn("AdapterPAPImpl - Document is null");
            } else {
                DocumentService service = new DocumentService();
                service.saveDocument(document);
                isDocSaved = true;
            }

        } catch (Exception ex) {
            LOG.error("Exception occured while saving document: {}", ex.getLocalizedMessage(), ex);
        }
        LOG.info("End AdapterPAPImpl.savePolicyDocument(...)");
        return isDocSaved;
    }

    /**
     * Deletes the document from the repository.
     *
     * @param document
     * @return true - success; false - failure
     */
    public boolean deletePolicyDocument(DocumentMetadata document) {
        LOG.info("Begin AdapterPAPImpl.deletePolicyDocument(...)");
        boolean isDocSaved = false;

        try {
            DocumentService service = new DocumentService();
            service.deleteDocument(document);
            isDocSaved = true;
        } catch (Exception ex) {
            LOG.error("Exception occured while deleting document: {}", ex.getLocalizedMessage(), ex);
        }
        LOG.info("End AdapterPAPImpl.deletePolicyDocument(...)");
        return isDocSaved;
    }
}
