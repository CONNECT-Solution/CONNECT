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
package gov.hhs.fha.nhinc.docmgr.repository.dao;

import gov.hhs.fha.nhinc.docmgr.repository.model.Document;
import gov.hhs.fha.nhinc.docmgr.repository.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docmgr.repository.persistence.HibernateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

/**
 * Data access object class for Document data
 * 
 * @author Neil Webb
 */
public class DocumentDao {

    Log log = LogFactory.getLog(DocumentDao.class);

    /**
     * Save a document record to the database.
     * Insert if document id is null. Update otherwise.
     * 
     * @param document Document object to save.
     */
    public void save(Document document) {
        log.debug("Performing document save");
        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.saveOrUpdate(document);
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        log.debug("Completed document save");
    }

    /**
     * Delete a document
     * 
     * @param document Document to delete
     */
    public void delete(Document document) {
        log.debug("Performing document delete");

        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.delete(document);
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (Throwable t) {
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        log.debug("Completed document delete");
    }

    /**
     * Retrieve a document by identifier
     * 
     * @param documentId Document identifier
     * @return Retrieved document
     */
    public Document findById(Long documentId) {
        log.debug("Performing document retrieve using id: " + documentId);
        Document document = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    document = (Document) sess.get(Document.class, documentId);
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed document retrieve by id. Result was " + ((document == null) ? "not " : "") + "found");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return document;
    }

    /**
     * Retrieves all documents
     * 
     * @return All document records
     */
    @SuppressWarnings("unchecked")
    public List<Document> findAll() {
        log.debug("Performing retrieve of all documents");
        List<Document> documents = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(Document.class);
                    documents = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed retrieve of all documents. " + ((documents == null) ? "0" : Integer.toString(documents.size())) + " results returned.");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return documents;
    }

    /**
     * Perform a query for documents
     * 
     * @param params Query parameters
     * @return Query results
     */
    @SuppressWarnings("unchecked")
    public List<Document> findDocuments(DocumentQueryParams params) {
        log.debug("Beginning document query");

        String patientId = null;
        List<String> classCodes = null;
        String classCodeScheme = null;
        Date creationTimeFrom = null;
        Date creationTimeTo = null;
        Date serviceStartTimeFrom = null;
        Date serviceStartTimeTo = null;
        Date serviceStopTimeFrom = null;
        Date serviceStopTimeTo = null;
        List<String> statuses = null;
        List<String> documentUniqueIds = null;
        List<String> repositoryIds = null;
        if (params != null) {
            patientId = params.getPatientId();
            classCodes = params.getClassCodes();
            classCodeScheme = params.getClassCodeScheme();
            creationTimeFrom = params.getCreationTimeFrom();
            creationTimeTo = params.getCreationTimeTo();

            String classCode = null;
            try {
                PropertyAccessor oProp = PropertyAccessor.getInstance();
                classCode = oProp.getProperty("docassembly", "C62_CLASS_CODE");

            } catch (PropertyAccessException e) {
                log.error("Failed to read C62 document class code" + e.getMessage());
            }

            if (classCodes != null && classCodes.get(0).equalsIgnoreCase(classCode)) {
                serviceStartTimeFrom = params.getServiceStartTimeFrom();
                serviceStartTimeTo = params.getServiceStartTimeTo();
                serviceStopTimeFrom = params.getServiceStopTimeFrom();
                serviceStopTimeTo = params.getServiceStopTimeTo();
            }
            statuses = params.getStatuses();
            documentUniqueIds = params.getDocumentUniqueIds();
            repositoryIds = params.getRepositoryIds();

        }
        List<Document> documents = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    SimpleDateFormat logDateFormatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss a");
                    Criteria criteria = sess.createCriteria(Document.class);

                    if (patientId != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - patient id: " + patientId);
                        }
                        criteria.add(Expression.eq("patientId", patientId));
                    }

                    if ((classCodes != null) && (!classCodes.isEmpty())) {
                        if (log.isDebugEnabled()) {
                            for (String classCode : classCodes) {
                                log.debug("Document query - class code: " + classCode);
                            }
                        }
                        criteria.add(Expression.in("classCode", classCodes));
                    }

                    if (classCodeScheme != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - class code scheme: " + classCodeScheme);
                        }
                        criteria.add(Expression.eq("classCodeScheme", classCodeScheme));
                    }

                    if (creationTimeFrom != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - creation time from: " + logDateFormatter.format(creationTimeFrom));
                        }
                        criteria.add(Expression.ge("creationTime", creationTimeFrom));
                    }

                    if (creationTimeTo != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - creation time to: " + logDateFormatter.format(creationTimeTo));
                        }
                        criteria.add(Expression.le("creationTime", creationTimeTo));
                    }

                    if (serviceStartTimeFrom != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - service start time from: " + logDateFormatter.format(serviceStartTimeFrom));
                        }
                        criteria.add(Expression.ge("serviceStartTime", serviceStartTimeFrom));
                    }

                    if (serviceStartTimeTo != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - service start time to: " + logDateFormatter.format(serviceStartTimeTo));
                        }
                        criteria.add(Expression.le("serviceStartTime", serviceStartTimeTo));
                    }

                    if (serviceStopTimeFrom != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - service stop time from: " + logDateFormatter.format(serviceStopTimeFrom));
                        }
                        criteria.add(Expression.ge("serviceStopTime", serviceStopTimeFrom));
                    }

                    if (serviceStopTimeTo != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Document query - service stop time to: " + logDateFormatter.format(serviceStopTimeTo));
                        }
                        criteria.add(Expression.le("serviceStopTime", serviceStopTimeTo));
                    }

                    if ((statuses != null) && (!statuses.isEmpty())) {
                        if (log.isDebugEnabled()) {
                            for (String status : statuses) {
                                log.debug("Document query - status: " + status);
                            }
                        }
                        criteria.add(Expression.in("status", statuses));
                    }

                    if ((documentUniqueIds != null) && (!documentUniqueIds.isEmpty())) {
                        if (log.isDebugEnabled()) {
                            for (String documentUniqueId : documentUniqueIds) {
                                log.debug("Document query - document unique id: " + documentUniqueId);
                            }
                        }
                        criteria.add(Expression.in("documentUniqueId", documentUniqueIds));
                    }

                    if ((repositoryIds != null) && (!repositoryIds.isEmpty())) {
                        if (log.isDebugEnabled()) {
                            for (String repositoryId : repositoryIds) {
                                log.debug("Document query - document repository id: " + repositoryId);
                            }
                        }
                        criteria.add(Expression.in("repositoryId", repositoryIds));
                    }

                    documents = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed retrieve of document query. " + ((documents == null) ? "0" : Integer.toString(documents.size())) + " results returned.");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return documents;
    }
}
