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
package gov.hhs.fha.nhinc.docrepository.adapter.dao;

import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.util.GenericDBUtils;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.getOrIsNullIsGe;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.getOrIsNullIsLe;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data access object class for Document data
 *
 * @author Neil Webb
 */
public class DocumentDao {

    private static final String DOCUMENT_UNIQUE_ID = "documentUniqueId";
    private static final Logger LOG = LoggerFactory.getLogger(DocumentDao.class);

    public boolean save(DocumentMetadata document) {
        return GenericDBUtils.save(getSession(), document);
    }

    public DocumentMetadata saveAndGetDocument(DocumentMetadata document) {
        DocumentMetadata s = null;
        if (GenericDBUtils.save(getSession(), document)) {
            s = findById(document.getDocumentid());
        }
        return s;
    }

    public boolean saveAll(List<DocumentMetadata> documents) {
        return GenericDBUtils.saveAll(getSession(), documents);
    }

    public boolean delete(DocumentMetadata document) {
        return GenericDBUtils.delete(getSession(), document);
    }

    public boolean deleteAll(List<DocumentMetadata> documents) {
        return GenericDBUtils.deleteAll(getSession(), documents);
    }

    public DocumentMetadata findById(Long documentId) {
        return GenericDBUtils.readBy(getSession(), DocumentMetadata.class, documentId);
    }

    public List<DocumentMetadata> findAll() {
        return GenericDBUtils.findAll(getSession(), DocumentMetadata.class);
    }

    public List<DocumentMetadata> findAllByPatientId(long patientId) {
        return GenericDBUtils.findAllBy(getSession(), DocumentMetadata.class,
            Restrictions.eq("patientRecordId", patientId));
    }

    public int getNextID() {
        DocumentMetadata result = null;
        try (Session session = getSession()) {
            Criteria crit = session.createCriteria(DocumentMetadata.class);
            crit.add(Restrictions.ilike(DOCUMENT_UNIQUE_ID, "CONNECT%", MatchMode.START));
            crit.addOrder(Order.desc(DOCUMENT_UNIQUE_ID));
            crit.setMaxResults(1);

            result = (DocumentMetadata) crit.uniqueResult();
            return result == null ? 0 : Integer.parseInt(result.getDocumentUniqueId().substring(7)) + 1;
        } catch (NumberFormatException e) {
            if (result != null) {
                LOG.error("Couldnt parse next ID from document ID {}", result.getDocumentUniqueId(), e);
            } else {
                LOG.error("Couldnt parse next ID from document ID. result was null", e);
            }

            throw e;
        }
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
    public List<DocumentMetadata> findDocuments(DocumentQueryParams params) {
        LOG.debug("Beginning document query");

        DocumentQueryParams parameters = params;
        if (parameters == null) {
            parameters = new DocumentQueryParams();
        }

        List<DocumentMetadata> documents = new ArrayList<>();
        Session sess = null;
        try {
            sess = getSession();
            if (sess != null) {
                Criteria criteria = sess.createCriteria(DocumentMetadata.class);

                String patientId = parameters.getPatientId();
                if (patientId != null) {
                    criteria.add(Restrictions.eq("patientId", patientId));
                }

                List<String> statuses = parameters.getStatuses();
                if (CollectionUtils.isNotEmpty(statuses)) {
                    LOG.debug("Document query - statuses: {}", statuses);
                    criteria.add(Restrictions.in("status", statuses));
                }

                List<String> documentUniqueIds = parameters.getDocumentUniqueIds();
                if (CollectionUtils.isNotEmpty(documentUniqueIds)) {
                    LOG.debug("Document query - document unique ids: {}", documentUniqueIds);
                    criteria.add(Restrictions.in(DOCUMENT_UNIQUE_ID, documentUniqueIds));
                }

                Boolean onDemand = parameters.getOnDemand();
                if (onDemand != null) {
                    LOG.debug("Document query - onDemand: {}", onDemand);
                    criteria.add(Restrictions.eq("onDemand", onDemand));
                }

                populateClassCodeCritera(parameters, criteria);
                populateServiceDateCriteria(parameters, criteria);
                populateCreationDateCriteria(parameters, criteria);

                documents = criteria.list();

                LOG.debug("Completed retrieve of document query. {} results returned.", documents.size());
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

        } finally {
            GenericDBUtils.closeSession(sess);
        }
        return documents;
    }

    private static void populateCreationDateCriteria(DocumentQueryParams params, Criteria criteria) {
        SimpleDateFormat logDateFormatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss a");

        Date creationTimeFrom = params.getCreationTimeFrom();
        if (creationTimeFrom != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - creation time from: {}", logDateFormatter.format(creationTimeFrom));
            }
            criteria.add(Restrictions.ge("creationTime", creationTimeFrom));
        }

        Date creationTimeTo = params.getCreationTimeTo();
        if (creationTimeTo != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - creation time to: {}", logDateFormatter.format(creationTimeTo));
            }
            criteria.add(Restrictions.le("creationTime", creationTimeTo));
        }
    }

    private static void populateServiceDateCriteria(DocumentQueryParams params, Criteria criteria) {
        SimpleDateFormat logDateFormatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss a");

        Date serviceStartTimeFrom = params.getServiceStartTimeFrom();
        if (serviceStartTimeFrom != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - service start time from: {}",
                    logDateFormatter.format(serviceStartTimeFrom));
            }

            criteria.add(getOrIsNullIsGe("serviceStartTime", serviceStartTimeFrom));
        }

        Date serviceStartTimeTo = params.getServiceStartTimeTo();
        if (serviceStartTimeTo != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - service start time to: {}",
                    logDateFormatter.format(serviceStartTimeTo));
            }

            criteria.add(getOrIsNullIsLe("serviceStartTime", serviceStartTimeTo));
        }

        Date serviceStopTimeFrom = params.getServiceStopTimeFrom();
        if (serviceStopTimeFrom != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - service stop time from: {}",
                    logDateFormatter.format(serviceStopTimeFrom));
            }

            criteria.add(getOrIsNullIsGe("serviceStopTime", serviceStopTimeFrom));
        }

        Date serviceStopTimeTo = params.getServiceStopTimeTo();
        if (serviceStopTimeTo != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - service stop time to: {}",
                    logDateFormatter.format(serviceStopTimeTo));
            }

            criteria.add(getOrIsNullIsLe("serviceStopTime", serviceStopTimeTo));
        }
    }

    /**
     * ************************************************************
     * The class code and class code scheme combination can come in two different formats:
     * <p>
     * <ns7:Slot name="$XDSDocumentEntryClassCode"> <ns7:ValueList> <ns7:Value>34133-9</ns7:Value> </ns7:ValueList>
     * </ns7:Slot> <ns7:Slot name="$XDSDocumentEntryClassCodeScheme"> <ns7:ValueList> <ns7:Value>2.16.840.1.113883.6
     * .1</ns7:Value> </ns7:ValueList> </ns7:Slot>
     * <p>
     * or
     * <p>
     * <ns7:Slot name="$XDSDocumentEntryClassCode"> <ns7:ValueList> <ns7:Value>(
     * '34133-9^^2.16.840.1.113883.6.1')</ns7:Value> </ns7:ValueList> </ns7:Slot>
     * <p>
     * This method deals with both formats.
     * <p>
     * Any critera generated with this method will be added to the Criteria parameter
     * <p>
     ************************************************************
     */
    private static void populateClassCodeCritera(DocumentQueryParams params, Criteria criteria) {
        List<String> classCodes = params.getClassCodes();
        String classCodeScheme = params.getClassCodeScheme();

        if (CollectionUtils.isNotEmpty(classCodes)) {
            Criterion criterion = null;
            for (String classCode : classCodes) {

                LOG.debug("Document query - class code: {}", classCode);

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

                Criterion andCrit = Restrictions.eq("classCode", newClassCode);
                if (newCodeScheme != null && !newCodeScheme.isEmpty()) {
                    andCrit = Restrictions.and(andCrit, Restrictions.eq("classCodeScheme", newCodeScheme));
                }

                if (criterion == null) {
                    criterion = andCrit;
                } else {
                    criterion = Restrictions.or(criterion, andCrit);
                }
            }
            criteria.add(criterion);
        }
    }
}
