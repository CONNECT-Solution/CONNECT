/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.

 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information. *
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
package gov.hhs.fha.nhinc.docmgr.repository.service;

import gov.hhs.fha.nhinc.docmgr.repository.model.Document;
import gov.hhs.fha.nhinc.docmgr.repository.dao.DocumentDao;
import gov.hhs.fha.nhinc.docmgr.repository.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docmgr.repository.dao.ExtraSlotDao;
import gov.hhs.fha.nhinc.docmgr.repository.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docmgr.repository.model.EventCode;
import gov.hhs.fha.nhinc.docmgr.repository.model.ExtraSlot;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Persistence service for Document records
 * 
 * @author Neil Webb
 */
public class DocumentService {

    private static Log log = LogFactory.getLog(DocumentService.class);

    /**
     * Save a document record.
     * 
     * @param document Document object to save.
     */
    public void saveDocument(Document document) {
        log.debug("Saving a document");
        if (document != null) {
            if (document.getDocumentid() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Performing an update for document: " + document.getDocumentid().longValue());
                }
                Document ecDoc = getDocument(document.getDocumentid());
                if (ecDoc != null) {
                    // Delete existing event codes
                    Set<EventCode> eventCodes = ecDoc.getEventCodes();
                    if ((eventCodes != null) && !eventCodes.isEmpty()) {
                        EventCodeDao eventCodeDao = new EventCodeDao();
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

                    // Delete existing extra slots
                    Set<ExtraSlot> extraSlots = ecDoc.getExtraSlots();
                    if ((extraSlots != null) && !extraSlots.isEmpty()) {
                        ExtraSlotDao extraSlotDao = new ExtraSlotDao();
                        for (ExtraSlot extraSlot : extraSlots) {
                            extraSlotDao.delete(extraSlot);
                            extraSlot.setExtraSlotId(null);
                        }
                    }

                    // Reset extra slot identifiers
                    extraSlots = document.getExtraSlots();
                    if ((extraSlots != null) && !extraSlots.isEmpty()) {
                        for (ExtraSlot extraSlot : extraSlots) {
                            if (extraSlot.getExtraSlotId() != null) {
                                extraSlot.setExtraSlotId(null);
                            }
                        }
                    }
                }
            } else {
                log.debug("Performing an insert");
            }

            // Calculate the hash code.
            //-------------------------
            if (document.getRawData() != null) {
                try {
                    String sHash = "";
                    sHash = SHA1HashCode.calculateSHA1(new String(document.getRawData()));
                    if (log.isDebugEnabled()) {
                        log.debug("Created Hash Code: " + sHash + " for string: " +
                            new String(document.getRawData()));
                    }
                    document.setHash(sHash);
                } catch (Throwable t) {
                    String sError = "Failed to create SHA-1 Hash code.  Error: " + t.getMessage() +
                        "Data Text: " + new String(document.getRawData());
                    log.error(sError, t);
                }
            } else {
                document.setHash("");
                log.warn("No SHA-1 Hash Code created because document was null.");
            }
        }

        DocumentDao dao = new DocumentDao();
        dao.save(document);
    }

    /**
     * Delete a document
     * 
     * @param document Document to delete
     * @throws DocumentServiceException 
     */
    public void deleteDocument(Document document) throws DocumentServiceException {
        log.debug("Deleting a document");
        DocumentDao dao = new DocumentDao();

        if ((document != null) && (document.getDocumentid() == null) && (document.getDocumentUniqueId() != null)) {
            // Query by unique id and delete if only one exists.
            DocumentQueryParams params = new DocumentQueryParams();
            List<String> uniqueIds = new ArrayList<String>();
            uniqueIds.add(document.getDocumentUniqueId());

            List<Document> docs = documentQuery(params);
            if ((docs != null) && (docs.size() == 1)) {
                document = docs.get(0);
            } else {
                throw new DocumentServiceException("Single document match not found for document unique id: " + document.getDocumentUniqueId());
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
        DocumentDao dao = new DocumentDao();
        return dao.findById(documentId);
    }

    /**
     * Retrieves all documents
     * 
     * @return All document records
     */
    public List<Document> getAllDocuments() {
        DocumentDao dao = new DocumentDao();
        return dao.findAll();
    }

    /**
     * Document query
     * 
     * @param params Document query parameters
     * @return Query results
     */
    public List<Document> documentQuery(DocumentQueryParams params) {
        DocumentDao dao = new DocumentDao();
        return dao.findDocuments(params);
    }
}
