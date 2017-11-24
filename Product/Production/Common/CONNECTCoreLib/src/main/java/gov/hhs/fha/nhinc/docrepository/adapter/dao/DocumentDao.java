/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docrepository.adapter.dao;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.util.GenericDBUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data access object class for Document data
 *
 * @author Neil Webb
 */
public class DocumentDao {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentDao.class);

    public boolean save(Document document) {
        return GenericDBUtils.save(getSession(), document);
    }

    public boolean saveAll(List<Document> documents) {
        return GenericDBUtils.saveAll(getSession(), documents);
    }

    public boolean delete(Document document) {
        return GenericDBUtils.delete(getSession(), document);
    }

    public Document findById(Long documentId) {
        return GenericDBUtils.readBy(getSession(), Document.class, documentId);
    }

    public List<Document> findAll() {
        return GenericDBUtils.findAll(getSession(), Document.class);
    }

    public List<Document> findAllBy(long patientId) {
        return GenericDBUtils.findAllBy(getSession(), Document.class, Expression.eq("patientRecordId", patientId));
    }

    protected Session getSession() {
        Session session = null;
        try {
            session = HibernateUtilFactory.getDocRepoHibernateUtil().getSessionFactory().openSession();
        } catch (HibernateException e) {
            LOG.error("Fail to openSession: {}, {}", e.getMessage(), e);
        }
        return session;
    }

    /**
     * Perform a query for documents
     *
     * @param params Query parameters
     * @return Query results
     */
    @SuppressWarnings("unchecked")
    public List<Document> findDocuments(DocumentQueryParams params) {
        LOG.debug("Beginning document query");

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
        if (params != null) {
            patientId = params.getPatientId();
            classCodes = params.getClassCodes();
            classCodeScheme = params.getClassCodeScheme();
            creationTimeFrom = params.getCreationTimeFrom();
            creationTimeTo = params.getCreationTimeTo();
            serviceStartTimeFrom = params.getServiceStartTimeFrom();
            serviceStartTimeTo = params.getServiceStartTimeTo();
            serviceStopTimeFrom = params.getServiceStopTimeFrom();
            serviceStopTimeTo = params.getServiceStopTimeTo();
            statuses = params.getStatuses();
            documentUniqueIds = params.getDocumentUniqueIds();
        }
        List<Document> documents = null;
        Session sess = null;
        try {
            sess = getSession();
            if (sess != null) {
                SimpleDateFormat logDateFormatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss a");
                Criteria criteria = sess.createCriteria(Document.class);

                if (patientId != null) {
                    criteria.add(Expression.eq("patientId", patientId));
                }

                if (classCodes != null && !classCodes.isEmpty()) {
                    /**************************************************************
                     * The class code and class code scheme combination can come in two different formats:
                     *
                     * <ns7:Slot name="$XDSDocumentEntryClassCode"> <ns7:ValueList> <ns7:Value>34133-9</ns7:Value>
                     * </ns7:ValueList> </ns7:Slot> <ns7:Slot name="$XDSDocumentEntryClassCodeScheme"> <ns7:ValueList>
                     * <ns7:Value>2.16.840.1.113883.6 .1</ns7:Value> </ns7:ValueList> </ns7:Slot>
                     *
                     * or
                     *
                     * <ns7:Slot name="$XDSDocumentEntryClassCode"> <ns7:ValueList> <ns7:Value>(
                     * '34133-9^^2.16.840.1.113883.6.1')</ns7:Value> </ns7:ValueList> </ns7:Slot>
                     *
                     * The code below can deal with both formats.
                     *
                     *************************************************************/
                    Criterion criterion = null;
                    for (String classCode : classCodes) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Document query - class code: " + classCode);
                        }
                        String newClassCode;
                        String newCodeScheme;

                        if (classCode.contains("^^")) {
                            int index = classCode.indexOf("^^");
                            newClassCode = classCode.substring(0, index);
                            newCodeScheme = classCode.substring(index + 2);
                        } else {
                            newClassCode = classCode;
                            newCodeScheme = classCodeScheme;
                        }

                        Criterion andCrit = Expression.eq("classCode", newClassCode);
                        if (newCodeScheme != null && !newCodeScheme.isEmpty()) {
                            andCrit = Restrictions.and(andCrit, Expression.eq("classCodeScheme", newCodeScheme));
                        }
                        if (criterion == null) {
                            criterion = andCrit;
                        } else {
                            criterion = Restrictions.or(criterion, andCrit);
                        }
                    }
                    criteria.add(criterion);
                }

                if (creationTimeFrom != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Document query - creation time from: " + logDateFormatter.format(creationTimeFrom));
                    }
                    criteria.add(Expression.ge("creationTime", creationTimeFrom));
                }

                if (creationTimeTo != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Document query - creation time to: " + logDateFormatter.format(creationTimeTo));
                    }
                    criteria.add(Expression.le("creationTime", creationTimeTo));
                }

                if (serviceStartTimeFrom != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Document query - service start time from: "
                            + logDateFormatter.format(serviceStartTimeFrom));
                    }
                    criteria.add(Expression.ge("serviceStartTime", serviceStartTimeFrom));
                }

                if (serviceStartTimeTo != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "Document query - service start time to: " + logDateFormatter.format(serviceStartTimeTo));
                    }
                    criteria.add(Expression.le("serviceStartTime", serviceStartTimeTo));
                }

                if (serviceStopTimeFrom != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "Document query - service stop time from: " + logDateFormatter.format(serviceStopTimeFrom));
                    }
                    criteria.add(Expression.ge("serviceStopTime", serviceStopTimeFrom));
                }

                if (serviceStopTimeTo != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "Document query - service stop time to: " + logDateFormatter.format(serviceStopTimeTo));
                    }
                    criteria.add(Expression.le("serviceStopTime", serviceStopTimeTo));
                }

                if (statuses != null && !statuses.isEmpty()) {
                    if (LOG.isDebugEnabled()) {
                        for (String status : statuses) {
                            LOG.debug("Document query - status: " + status);
                        }
                    }
                    criteria.add(Expression.in("status", statuses));
                }

                if (documentUniqueIds != null && !documentUniqueIds.isEmpty()) {
                    if (LOG.isDebugEnabled()) {
                        for (String documentUniqueId : documentUniqueIds) {
                            LOG.debug("Document query - document unique id: " + documentUniqueId);
                        }
                    }
                    criteria.add(Expression.in("documentUniqueId", documentUniqueIds));
                }

                if (params.getOnDemand() != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Document query - onDemand: " + params.getOnDemand());
                    }
                    criteria.add(Expression.eq("onDemand", params.getOnDemand()));
                }

                documents = criteria.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed retrieve of document query. "
                    + (documents == null ? "0" : Integer.toString(documents.size())) + " results returned.");
            }
        } finally {
            GenericDBUtils.closeSession(sess);
        }
        return documents;
    }

}
