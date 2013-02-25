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
package gov.hhs.fha.nhinc.docrepository.adapter.service;

import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Persistence service for Document records
 * 
 * @author Neil Webb
 */
public class DocumentService {
    private static final Logger LOG = Logger.getLogger(DocumentService.class);
    private DocumentDao documentDao = new DocumentDao();
    private EventCodeDao eventCodeDao = new EventCodeDao();
    
    /**
     * Save a document record.
     * 
     * @param document Document object to save.
     */
    public void saveDocument(Document document) {
        LOG.debug("Saving a document");
        if (document != null) {
            if (document.getDocumentid() != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Performing an update for document: " + document.getDocumentid().longValue());
                }
                Document ecDoc = getDocument(document.getDocumentid());
                if (ecDoc != null) {
                    // Delete existing event codes
                    Set<EventCode> eventCodes = ecDoc.getEventCodes();
                    if ((eventCodes != null) && !eventCodes.isEmpty()) {
                        EventCodeDao eventCodeDao = getEventCodeDao();
                        for (EventCode eventCode : eventCodes) {
                            eventCodeDao.delete(eventCode);
                            eventCode.setEventCodeId(null);
                        }
                    }

                    // Reset event code identifiers
                    eventCodes = document.getEventCodes();
                    if ((eventCodes != null) && !eventCodes.isEmpty()) {
                        for (EventCode eventCode : eventCodes) {
                            if (eventCode.getEventCodeId() != null) {
                                eventCode.setEventCodeId(null);
                            }
                        }
                    }
                }
            } else {
                LOG.debug("Performing an insert");
            }

            // Calculate the hash code.
            // -------------------------
            if (document.getRawData() != null) {
                String documentStr = "";
                try {
                    String sHash = "";
                    documentStr =  StringUtil.convertToStringUTF8(document.getRawData());
                    sHash = SHA1HashCode.calculateSHA1(documentStr);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Created Hash Code: " + sHash + " for string: " + documentStr);
                    }
                    document.setHash(sHash);
                } 
                catch (Throwable t) {
                    String sError = "Failed to create SHA-1 Hash code.  Error: " + t.getMessage() + "Data Text: "
                            + documentStr;
                    LOG.error(sError, t);
                }
            } else {
                document.setHash("");
                LOG.warn("No SHA-1 Hash Code created because document was null.");
            }
        }

        DocumentDao dao = getDocumentDao();
        dao.save(document);
    }

    /**
     * Delete a document
     * 
     * @param document Document to delete
     * @throws DocumentServiceException
     */
    public void deleteDocument(Document document) throws DocumentServiceException {
        LOG.debug("Deleting a document");
        DocumentDao dao = getDocumentDao();

        if ((document != null) && (document.getDocumentid() == null) && (document.getDocumentUniqueId() != null)) {
            // Query by unique id and delete if only one exists.
            DocumentQueryParams params = new DocumentQueryParams();
            List<String> uniqueIds = new ArrayList<String>();
            uniqueIds.add(document.getDocumentUniqueId());

            List<Document> docs = documentQuery(params);
            if ((docs != null) && (docs.size() == 1)) {
                document = docs.get(0);
            } else {
                throw new DocumentServiceException("Single document match not found for document unique id: "
                        + document.getDocumentUniqueId());
            }
        } else {
            if (document == null) {
                throw new DocumentServiceException("Document to delete was null");
            } else if (document.getDocumentUniqueId() == null) {
                throw new DocumentServiceException("Document unique id was null");
            }
        }

        dao.delete(document);
    }

    /**
     * Retrieve a document by identifier
     * 
     * @param documentId Document identifier
     * @return Retrieved document
     */
    public Document getDocument(Long documentId) {
        DocumentDao dao = getDocumentDao();
        return dao.findById(documentId);
    }

    /**
     * Retrieves all documents
     * 
     * @return All document records
     */
    public List<Document> getAllDocuments() {
        DocumentDao dao = getDocumentDao();
        return dao.findAll();
    }

    /**
     * Document query
     * 
     * @param params Document query parameters
     * @return Query results
     */
    public List<Document> documentQuery(DocumentQueryParams params) {
        List<Document> documents = new ArrayList<Document>();
        DocumentDao dao = getDocumentDao();
        List<Document> queryMatchDocs = dao.findDocuments(params);
        List<Document> eventCodeMatchDocs = null;
        if ((params != null) && NullChecker.isNotNullish(params.getEventCodeParams())) {
            eventCodeMatchDocs = queryByEventCode(params.getEventCodeParams());
            if (NullChecker.isNotNullish(queryMatchDocs)) {
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

    private List<Document> queryByEventCode(List<EventCodeParam> eventCodeParams) {
        List<Document> documents = new ArrayList<Document>();
        Set<Document> documentSet = new HashSet<Document>();
        if (NullChecker.isNotNullish(eventCodeParams)) {
            EventCodeDao eventCodeDao = getEventCodeDao();
            Set<EventCode> eventCodesAggregate = new HashSet<EventCode>();
            for (EventCodeParam eventCodeParam : eventCodeParams) {
                List<EventCode> eventCodes = eventCodeDao.eventCodeQuery(eventCodeParam);
                if (NullChecker.isNotNullish(eventCodes)) {
                    eventCodesAggregate.addAll(eventCodes);
                }
            }

            if (eventCodesAggregate != null) {
                for (EventCode ec : eventCodesAggregate) {
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

    private List<Document> createUnion(List<Document> listA, List<Document> listB) {
        List<Document> docUnion = new ArrayList<Document>();

        if (NullChecker.isNotNullish(listA) && NullChecker.isNotNullish(listB)) {
            for (Document docA : listA) {
                if (listContainsDoc(listB, docA)) {
                    docUnion.add(docA);
                }
            }
        }

        return docUnion;
    }

    private boolean listContainsDoc(List<Document> docs, Document doc) {
        boolean containsDoc = false;
        if ((doc != null) && (doc.getDocumentUniqueId() != null) && NullChecker.isNotNullish(docs)) {

            for (Document docFromList : docs) {
                if ((docFromList != null) && (doc.getDocumentUniqueId().equals(docFromList.getDocumentUniqueId()))) {
                    containsDoc = true;
                    break;
                }
            }
        }
        return containsDoc;
    }
    
    protected DocumentDao getDocumentDao(){
    	return documentDao;
    }
    
    protected EventCodeDao getEventCodeDao(){
    	return eventCodeDao;
    }
    
    
}
