/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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

import static gov.hhs.fha.nhinc.util.GenericDBUtils.getOrIsNullIsGe;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.getOrIsNullIsLe;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.util.GenericDBUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
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
    private static final String LOG_DOCUMENT_QUERY = "Document query - {}: {}";

    public boolean save(Document document) {
        return GenericDBUtils.save(getSession(), document);
    }

    public Document saveAndGetDocument(Document document) {
        Document s = null;
        if (GenericDBUtils.save(getSession(), document)) {
            s = findById(document.getDocumentid());
        }
        return s;
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
        return GenericDBUtils.findAllBy(getSession(), Document.class, Restrictions.eq("patientRecordId", patientId));
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

        List<Document> documents = null;
        Session sess = null;
        try {
            sess = getSession();
            if (sess != null) {
                Criteria criteria = createDocumentCriteria(sess, params);

                documents = criteria.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed retrieve of document query. {} results returned.",
                    documents == null ? "0" : Integer.toString(documents.size()));
            }
        } finally {
            GenericDBUtils.closeSession(sess);
        }
        return documents;
    }

    private static Criteria createDocumentCriteria(Session sess, DocumentQueryParams params){

        Criteria criteria = sess.createCriteria(Document.class);

        if (StringUtils.isNotBlank(params.getPatientId())) {
            criteria.add(Restrictions.eq("patientId", params.getPatientId()));
        }

        if (CollectionUtils.isNotEmpty(params.getClassCodes())) {
            criteria.add(getClassCode(params.getClassCodes(), params.getClassCodeScheme()));
        }

        addCriteriaBetweenDate(criteria, "creationTime", params.getCreationTimeFrom(), params.getCreationTimeTo());

        addCriteriaBetweenDateWithNull(criteria, "serviceStartTime", params.getServiceStartTimeFrom(),
            params.getServiceStartTimeTo());

        addCriteriaBetweenDateWithNull(criteria, "serviceStopTime", params.getServiceStopTimeFrom(),
            params.getServiceStopTimeTo());

        addCriteriaWithIn(criteria, "status", params.getStatuses());

        addCriteriaWithIn(criteria, "documentUniqueId", params.getDocumentUniqueIds());

        addCriteriaWithEq(criteria, "onDemand", params.getOnDemand());

        return criteria;
    }

    private static void addCriteriaWithIn(Criteria criteria, String colName, List<String> valueList) {
        if (CollectionUtils.isNotEmpty(valueList)) {
            if (LOG.isDebugEnabled()) {
                for (String item : valueList) {
                    LOG.debug(LOG_DOCUMENT_QUERY, colName, item);
                }
            }
            criteria.add(Restrictions.in(colName, valueList));
        }
    }

    private static void addCriteriaWithEq(Criteria criteria, String colName, Object colValue) {
        if (colValue != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(LOG_DOCUMENT_QUERY, colName, colValue);
            }
            criteria.add(Restrictions.eq(colName, colValue));
        }
    }

    private static void addCriteriaBetweenDateWithNull(Criteria criteria, String colName, Date dateFrom, Date dateTo) {
        SimpleDateFormat logDateFormatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss a");
        if (dateFrom != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - {} from: {}", colName, logDateFormatter.format(dateFrom));
            }
            criteria.add(getOrIsNullIsGe(colName, dateFrom));
        }

        if (dateTo != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - {} to: {}", colName, logDateFormatter.format(dateTo));
            }
            criteria.add(getOrIsNullIsLe(colName, dateTo));
        }
    }

    private static void addCriteriaBetweenDate(Criteria criteria, String colName, Date dateFrom, Date dateTo) {
        SimpleDateFormat logDateFormatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss a");
        if (dateFrom != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - {} from: {}", colName, logDateFormatter.format(dateFrom));
            }
            criteria.add(Restrictions.ge(colName, dateFrom));
        }

        if (dateTo != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Document query - {} to: {}", colName, logDateFormatter.format(dateTo));
            }
            criteria.add(Restrictions.le(colName, dateTo));
        }
    }

    private static Criterion getClassCode(List<String> classCodes, String classCodeScheme) {

        /**************************************************************
         * The class code and class code scheme combination can come in two different formats:
         *
         * <ns7:Slot name="$XDSDocumentEntryClassCode"> <ns7:ValueList> <ns7:Value>34133-9</ns7:Value> </ns7:ValueList>
         * </ns7:Slot> <ns7:Slot name="$XDSDocumentEntryClassCodeScheme"> <ns7:ValueList> <ns7:Value>2.16.840.1.113883.6
         * .1</ns7:Value> </ns7:ValueList> </ns7:Slot>
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
                LOG.debug(LOG_DOCUMENT_QUERY, "Class Code", classCode);
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

            Criterion andCrit = Restrictions.eq("classCode", newClassCode);
            if (StringUtils.isNotEmpty(newCodeScheme)) {
                andCrit = Restrictions.and(andCrit, Restrictions.eq("classCodeScheme", newCodeScheme));
            }
            if (criterion == null) {
                criterion = andCrit;
            } else {
                criterion = Restrictions.or(criterion, andCrit);
            }
        }
        return criterion;
    }
}
