/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docrepository.adapter.service;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Persistence service for Document records
 * 
 * @author Neil Webb
 */
public class DocumentService
{
    private static Log log = LogFactory.getLog(DocumentService.class);

    /**
     * Save a document record.
     * 
     * @param document Document object to save.
     */
    public void saveDocument(Document document)
    {
        log.debug("Saving a document");
        if (document != null)
        {
            if (document.getDocumentid() != null)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Performing an update for document: " + document.getDocumentid().longValue());
                }
                Document ecDoc = getDocument(document.getDocumentid());
                if (ecDoc != null)
                {
                    // Delete existing event codes
                    Set<EventCode> eventCodes = ecDoc.getEventCodes();
                    if ((eventCodes != null) && !eventCodes.isEmpty())
                    {
                        EventCodeDao eventCodeDao = new EventCodeDao();
                        for (EventCode eventCode : eventCodes)
                        {
                            eventCodeDao.delete(eventCode);
                            eventCode.setEventCodeId(null);
                        }
                    }

                    // Reset event code identifiers
                    eventCodes = document.getEventCodes();
                    if ((eventCodes != null) && !eventCodes.isEmpty())
                    {
                        for (EventCode eventCode : eventCodes)
                        {
                            if (eventCode.getEventCodeId() != null)
                            {
                                eventCode.setEventCodeId(null);
                            }
                        }
                    }
                }
            }
            else
            {
                log.debug("Performing an insert");
            }

            // Calculate the hash code.
            //-------------------------
            if (document.getRawData() != null)
            {
                try
                {
                    String sHash = "";
                    sHash = SHA1HashCode.calculateSHA1(new String(document.getRawData()));
                    if (log.isDebugEnabled())
                    {
                        log.debug("Created Hash Code: " + sHash + " for string: " +
                                  new String(document.getRawData()));
                    }
                    document.setHash(sHash);
                }
                catch (Throwable t)
                {
                    String sError = "Failed to create SHA-1 Hash code.  Error: " + t.getMessage() +
                                    "Data Text: " + new String(document.getRawData());
                    log.error(sError, t);
                }
            }
            else
            {
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
    public void deleteDocument(Document document) throws DocumentServiceException
    {
        log.debug("Deleting a document");
        DocumentDao dao = new DocumentDao();

        if ((document != null) && (document.getDocumentid() == null) && (document.getDocumentUniqueId() != null))
        {
            // Query by unique id and delete if only one exists.
            DocumentQueryParams params = new DocumentQueryParams();
            List<String> uniqueIds = new ArrayList<String>();
            uniqueIds.add(document.getDocumentUniqueId());

            List<Document> docs = documentQuery(params);
            if ((docs != null) && (docs.size() == 1))
            {
                document = docs.get(0);
            }
            else
            {
                throw new DocumentServiceException("Single document match not found for document unique id: " + document.getDocumentUniqueId());
            }
        }
        else
        {
            if (document == null)
            {
                throw new DocumentServiceException("Document to delete was null");
            }
            else if (document.getDocumentUniqueId() == null)
            {
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
    public Document getDocument(Long documentId)
    {
        DocumentDao dao = new DocumentDao();
        return dao.findById(documentId);
    }

    /**
     * Retrieves all documents
     * 
     * @return All document records
     */
    public List<Document> getAllDocuments()
    {
        DocumentDao dao = new DocumentDao();
        return dao.findAll();
    }

    /**
     * Document query
     * 
     * @param params Document query parameters
     * @return Query results
     */
    public List<Document> documentQuery(DocumentQueryParams params)
    {
        List<Document> documents = new ArrayList<Document>();
        DocumentDao dao = new DocumentDao();
        List<Document> queryMatchDocs = dao.findDocuments(params);
        List<Document> eventCodeMatchDocs = null;
        if((params != null) && NullChecker.isNotNullish(params.getEventCodeParams()))
        {
            eventCodeMatchDocs = queryByEventCode(params.getEventCodeParams());
            if(NullChecker.isNotNullish(queryMatchDocs))
            {
                // Both doc parameter and event code query doc matches found. Return union of collections
                documents = createUnion(queryMatchDocs, eventCodeMatchDocs);
            }
            else
            {
                // Only event code match docs found.
                documents = eventCodeMatchDocs;
            }
        }
        else
        {
            // Only doc parameter match docs found.
            documents = queryMatchDocs;
        }

        return documents;
    }

    private List<Document> queryByEventCode(List<EventCodeParam> eventCodeParams)
    {
        List<Document> documents = new ArrayList<Document>();
        Set<Document> documentSet = new HashSet<Document>();
        if(NullChecker.isNotNullish(eventCodeParams))
        {
            EventCodeDao eventCodeDao = new EventCodeDao();
            Set<EventCode> eventCodesAggregate = new HashSet<EventCode>();
            for(EventCodeParam eventCodeParam : eventCodeParams)
            {
                List<EventCode> eventCodes = eventCodeDao.eventCodeQuery(eventCodeParam);
                if(NullChecker.isNotNullish(eventCodes))
                {
                    eventCodesAggregate.addAll(eventCodes);
                }
            }

            if(eventCodesAggregate != null)
            {
                for(EventCode ec : eventCodesAggregate)
                {
                    if(ec != null)
                    {
                        documentSet.add(ec.getDocument());
                    }
                }
            }
        }
        if(!documentSet.isEmpty())
        {
            documents.addAll(documentSet);
        }

        return documents;
    }

    private List<Document> createUnion(List<Document> listA, List<Document> listB)
    {
        List<Document> docUnion = new ArrayList<Document>();

        if(NullChecker.isNotNullish(listA) && NullChecker.isNotNullish(listB))
        {
            for(Document docA : listA)
            {
                if(listContainsDoc(listB, docA))
                {
                    docUnion.add(docA);
                }
            }
        }

        return docUnion;
    }

    private boolean listContainsDoc(List<Document> docs, Document doc)
    {
        boolean containsDoc = false;
        if((doc != null) && (doc.getDocumentUniqueId() != null) && NullChecker.isNotNullish(docs))
        {

            for(Document docFromList : docs)
            {
                if((docFromList != null) && (doc.getDocumentUniqueId().equals(docFromList.getDocumentUniqueId())))
                {
                    containsDoc = true;
                    break;
                }
            }
        }
        return containsDoc;
    }
}
