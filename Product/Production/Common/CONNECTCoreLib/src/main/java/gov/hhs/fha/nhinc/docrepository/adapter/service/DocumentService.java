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
package gov.hhs.fha.nhinc.docrepository.adapter.service;

import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Persistence service for Document records
 *
 * @author Neil Webb
 */
public class DocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);
    private final DocumentDao documentDao = new DocumentDao();
    private final EventCodeDao eventCodeDao = new EventCodeDao();

    /**
     * Save a document record.
     *
     * @param document Document object to save.
     * @return
     */
    public DocumentMetadata saveDocument(DocumentMetadata document) {

        LOG.debug("Saving a document");
        if (document != null) {
            if (document.getDocumentid() != null) {
                LOG.debug("Performing an update for document: {}", document.getDocumentid());
                DocumentMetadata ecDoc = getDocument(document.getDocumentid());
                if (ecDoc != null) {
                    // Delete existing event codes
                    Set<EventCode> eventCodes = ecDoc.getEventCodes();
                    if (eventCodes != null && !eventCodes.isEmpty()) {
                        for (EventCode eventCode : eventCodes) {
                            eventCodeDao.delete(eventCode);
                            eventCode.setEventCodeId(null);
                        }
                    }

                    // Reset event code identifiers
                    eventCodes = document.getEventCodes();
                    if (eventCodes != null && !eventCodes.isEmpty()) {
                        for (EventCode eventCode : eventCodes) {
                            if (eventCode.getEventCodeId() != null) {
                                eventCode.setEventCodeId(null);
                            }
                        }
                    }
                }
            }

            // Calculate the hash code.
            // -------------------------

            Document doc = document.getDocument();
            if (doc != null && doc.getRawData().length > 0) {
                String documentStr;
                try {
                    String sHash;
                    documentStr = StringUtil.convertToStringUTF8(doc.getRawData());
                    sHash = SHA1HashCode.calculateSHA1(documentStr);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Successfully created Hash Code for string: {}", sHash);
                    }
                    document.setHash(sHash);
                } catch (UnsupportedEncodingException uee) {
                    LOG.error("Failed to create SHA-1 Hash code for Data Text.  Error:" + uee.getMessage(), uee);
                } catch (NoSuchAlgorithmException nsae) {
                    LOG.error("Failed to create SHA-1 Hash code for Data Text.  Error:" + nsae.getMessage(), nsae);
                }
            } else {
                document.setHash("");
                LOG.warn("No SHA-1 Hash Code created because document data was empty.");
            }
        }

        documentDao.save(document);
        return document;

    }

    /**
     * Delete a document
     *
     * @param document Document to delete
     * @throws DocumentServiceException
     */
    public void deleteDocument(DocumentMetadata document) throws DocumentServiceException {
        LOG.debug("Deleting a document");

        if (document != null && document.getDocumentid() == null && document.getDocumentUniqueId() != null) {
            // Query by unique id and delete if only one exists.
            DocumentQueryParams params = new DocumentQueryParams();
            List<String> uniqueIds = new ArrayList<>();
            uniqueIds.add(document.getDocumentUniqueId());
            params.setDocumentUniqueId(uniqueIds);

            List<DocumentMetadata> docs = documentQuery(params);
            if (docs != null && docs.size() == 1) {
                document = docs.get(0);
            } else {
                throw new DocumentServiceException(
                    "Single document match not found for document unique id: " + document.getDocumentUniqueId());
            }

            documentDao.delete(document);
        } else {
            if (document == null) {
                throw new DocumentServiceException("Document to delete was null");
            } else if (document.getDocumentUniqueId() == null) {
                throw new DocumentServiceException("Document unique id was null");
            }
        }

    }

    /**
     * Retrieve a document by identifier
     *
     * @param documentId Document identifier
     * @return Retrieved document
     */
    public DocumentMetadata getDocument(Long documentId) {
        return documentDao.findById(documentId);
    }

    /**
     * Retrieves all documents
     *
     * @return All document records
     */
    public List<DocumentMetadata> getAllDocuments() {
        return documentDao.findAll();
    }

    /**
     * Document query
     *
     * @param params Document query parameters
     * @return Query results
     */
    public List<DocumentMetadata> documentQuery(DocumentQueryParams params) {
        List<DocumentMetadata> documents;
        List<DocumentMetadata> queryMatchDocs = documentDao.findDocuments(params);
        List<DocumentMetadata> eventCodeMatchDocs;
        if (params != null && NullChecker.isNotNullish(params.getEventCodeParams())) {
            eventCodeMatchDocs = queryByEventCode(params.getEventCodeParams(), params.getSlots());
            if (NullChecker.isNotNullish(queryMatchDocs) && NullChecker.isNotNullish(eventCodeMatchDocs)) {
                // Both doc parameter and event code query doc matches found. Return union of collections
                documents = createUnion(queryMatchDocs, eventCodeMatchDocs);
            } else {
                // Only event code match docs found.
                documents = eventCodeMatchDocs;
            }
        } else {
            // Only doc parameter match docs found.
            documents = queryMatchDocs;
        }

        return documents;
    }

    public String getNextID() {
        return "CONNECT" + documentDao.getNextID();
    }

    protected List<DocumentMetadata> queryByEventCode(List<EventCodeParam> eventCodeParams, List<SlotType1> slots) {
        List<EventCode> eventCodes;
        List<DocumentMetadata> documents = new ArrayList<>();
        Set<DocumentMetadata> documentSet = new HashSet<>();
        if (NullChecker.isNotNullish(eventCodeParams)) {
            eventCodes = eventCodeDao.eventCodeQuery(slots);
            if (eventCodes != null && !eventCodes.isEmpty()) {
                for (EventCode ec : eventCodes) {
                    if (ec != null) {
                        documentSet.add(ec.getDocument());
                    }
                }
            }
        }
        if (!documentSet.isEmpty()) {
            documents.addAll(documentSet);
        }

        return documents;
    }

    private List<DocumentMetadata> createUnion(List<DocumentMetadata> listA, List<DocumentMetadata> listB) {
        List<DocumentMetadata> docUnion = new ArrayList<>();

        if (NullChecker.isNotNullish(listA) && NullChecker.isNotNullish(listB)) {
            for (DocumentMetadata docA : listA) {
                if (listContainsDoc(listB, docA)) {
                    docUnion.add(docA);
                }
            }
        }
        return docUnion;
    }

    private boolean listContainsDoc(List<DocumentMetadata> docs, DocumentMetadata doc) {
        boolean containsDoc = false;
        if (doc != null && doc.getDocumentUniqueId() != null && NullChecker.isNotNullish(docs)) {

            for (DocumentMetadata docFromList : docs) {
                if (docFromList != null && doc.getDocumentUniqueId().equals(docFromList.getDocumentUniqueId())) {
                    containsDoc = true;
                    break;
                }
            }
        }
        return containsDoc;
    }

}
