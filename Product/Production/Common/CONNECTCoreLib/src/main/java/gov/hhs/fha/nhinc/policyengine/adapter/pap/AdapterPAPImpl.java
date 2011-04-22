/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pap;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class implements the policy engine PAP (Policy Access Point).
 *
 * @author mastan.ketha
 */
public class AdapterPAPImpl {

    private static Log log = LogFactory.getLog(AdapterPAPImpl.class);

    /**
     * Get the Access Consent Policy (ACP) document for the patient.
     * @param patientId
     * @return <code>Document</code>
     */
    public Document getPolicyDocumentByPatientId(String patientId) {
        log.info("Begin AdapterPAPImpl.getPolicyDocument(...)");
        Document document = new Document();
        try {
            DocumentQueryParams params = new DocumentQueryParams();
            log.debug("patientid:" + patientId);
            params.setPatientId(patientId);
            List<String> classCodeValues = new ArrayList<String>();
            classCodeValues.add(AdapterPAPConstants.DOCUMENT_CLASS_CODE);
            params.setClassCodes(classCodeValues);
            DocumentService service = new DocumentService();
            List<Document> docs = service.documentQuery(params);
            int docsSize = 0;
            if (docs != null) {
                docsSize = docs.size();
            }
            log.debug("Document size:" + String.valueOf(docsSize));
            if (docsSize > 0) {
                document = docs.get(0);
            }
        } catch (Exception ex) {
            log.error("Exception occured while retrieving documents");
            log.error(ex.getMessage());
        }
        log.info("End AdapterPAPImpl.getPolicyDocument(...)");
        return document;
    }

    /**
     * Get the Access Consent Policy (ACP) document from the document
     * repository.
     * @param documentId
     * @return <code>Document</code>
     */
    public Document getPolicyDocumentByDocId(Long documentId) {
        log.info("Begin AdapterPAPImpl.getPolicyDocumentByDocId(...)");
        Document document = new Document();
        try {
            DocumentService service = new DocumentService();
            document = service.getDocument(documentId);
        } catch (Exception ex) {
            log.error("Exception occured while retrieving documents");
            log.error(ex.getMessage());
        }
        log.info("End AdapterPAPImpl.getPolicyDocumentByDocId(...)");
        return document;
    }

    /**
     * Saves the document to the repository. If the document id (PK) is null
     * then the document is inserted, else the document is updated
     * @param document
     * @return true - success; false - failure
     */
    public boolean savePolicyDocument(Document document) {
        log.info("Begin AdapterPAPImpl.savePolicyDocument(...)");
        boolean isDocSaved = false;
        try {
            if (document == null) {
                log.warn("AdapterPAPImpl - Document is null");
            } else {
                DocumentService service = new DocumentService();
                service.saveDocument(document);
                isDocSaved = true;
            }

        } catch (Exception ex) {
            log.error("Exception occured while saving document");
            log.error(ex.getMessage());
        }
        log.info("End AdapterPAPImpl.savePolicyDocument(...)");
        return isDocSaved;
    }

    /**
     * Deletes the document from the repository.
     * @param document
     * @return true - success; false - failure
     */
    public boolean deletePolicyDocument(Document document) {
        log.info("Begin AdapterPAPImpl.deletePolicyDocument(...)");
        boolean isDocSaved = false;

        try {
            DocumentService service = new DocumentService();
            service.deleteDocument(document);
            isDocSaved = true;
        } catch (Exception ex) {
            log.error("Exception occured while deleting document");
            log.error(ex.getMessage());
        }
        log.info("End AdapterPAPImpl.deletePolicyDocument(...)");
        return isDocSaved;

    }

}
