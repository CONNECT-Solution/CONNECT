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
package gov.hhs.fha.nhinc.auditrepository.hibernate;

import gov.hhs.fha.nhinc.auditrepository.hibernate.util.HibernateUtil;
import gov.hhs.fha.nhinc.auditrepository.hibernate.util.HibernateUtilFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuditRepositoryDAO Class provides methods to query and update Audit Data to/from MySQL Database using Hibernate
 *
 * @author svalluripalli
 */
public class AuditRepositoryDAO {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRepositoryDAO.class);

    /**
     * Constructor
     */
    public AuditRepositoryDAO() {
        LOG.trace("AuditRepositoryDAO - Initialized");
    }

    /**
     *
     * @param auditList
     * @return boolean
     */
    public boolean insertAuditRepository(final List<AuditRepositoryRecord> auditList) {

        Session session = null;
        boolean result = true;
        if (auditList != null && !auditList.isEmpty()) {
            final int size = auditList.size();
            AuditRepositoryRecord auditRecord = null;
            Transaction tx = null;
            try {
                session = getSession();

                tx = session.beginTransaction();
                LOG.info("Inserting Record...");
                for (int i = 0; i < size; i++) {
                    auditRecord = auditList.get(i);
                    session.persist(auditRecord);
                }
                tx.commit();
                LOG.info("AuditRepository List Inserted successfully...");

            } catch (final HibernateException e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error("Error during insertion caused by : {}", e.getLocalizedMessage(), e);
            } finally {
                closeSession(session);
            }
        }

        return result;
    }

    /**
     * This method does a query to database to get the Audit Log Messages based on user id and/or patient id and/or
     * community id and/or timeframe
     *
     * @param eUserId
     * @param ePatientId
     * @param startDate
     * @param endDate
     * @return List
     */
    public List queryAuditRepositoryOnCriteria(final String eUserId, final String ePatientId, final Date startDate,
            final Date endDate) {

        List<AuditRepositoryRecord> queryList = new ArrayList<>();
        Session session = null;

        if (eUserId == null && ePatientId == null && startDate == null) {
            LOG.info("-- No - Input Parameters found for Audit Query --");
            return queryList;
        }

        try {
            session = getSession();
            LOG.info("Getting Record");

            if (session != null) {
                // Build the criteria
                final Criteria aCriteria = session.createCriteria(AuditRepositoryRecord.class);
                if (eUserId != null && !eUserId.isEmpty()) {
                    aCriteria.add(Expression.eq("userId", eUserId));
                }
                if (ePatientId != null && !ePatientId.isEmpty()) {
                    aCriteria.add(Expression.eq("receiverPatientId", ePatientId));
                }

                if (startDate != null && endDate != null) {
                    aCriteria.add(Expression.between("timeStamp", new Date(startDate.getTime()),
                            new Date(endDate.getTime())));
                } else if (startDate != null) {
                    // Assume that endDate is null here
                    aCriteria.add(Expression.ge("timeStamp", new Date(startDate.getTime())));
                }

                queryList = aCriteria.list();
            }
        } catch (final HibernateException e) {
            LOG.error("Exception in AuditLog.get() occurred due to : {}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session);
        }
        return queryList;
    }

    /**
     * This method does a query to database to get the Audit Log Messages based on params below
     *
     * @param messageId
     * @param relatesTo
     * @return List
     */
    public List<AuditRepositoryRecord> queryAuditRecords(final String messageId, final String relatesTo) {

        Session session = null;
        List<AuditRepositoryRecord> queryList = null;
        try {
            session = getSession();
            LOG.info("Getting Record for Audit Viewer ");

            // Build the criteria
            if (session != null) {
                final Criteria queryCriteria = session.createCriteria(AuditRepositoryRecord.class);

                boolean idHasPrefix = NullChecker.isNotNullish(messageId)
                        && messageId.startsWith(NhincConstants.WS_SOAP_HEADER_MESSAGE_ID_PREFIX);

                if (idHasPrefix) {
                    queryCriteria.add(Restrictions.eq("messageId", messageId));
                } else if (NullChecker.isNotNullish(messageId)) {
                    // Message ID exists but needs to have the prefix prepended
                    queryCriteria.add(
                            Restrictions.eq("messageId", NhincConstants.WS_SOAP_HEADER_MESSAGE_ID_PREFIX + messageId));
                }

                if (NullChecker.isNotNullish(relatesTo)) {
                    queryCriteria.add(Restrictions.eq("relatesTo", relatesTo));
                }
                // if no criteria is passed then it will search full database with above mentioned columns in the result
                queryList = queryCriteria.list();
            }
        } catch (final HibernateException e) {
            LOG.error("Exception in AuditLog.get() occurred due to : {}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session);
        }
        return queryList;
    }

    /**
     * This method does a query to database to get the Audit Log Messages based different options
     *
     * @param startDate - Audit Event Start Date
     * @param userId - Human initiator who initiated transaction. SAML Attribute statement SubjectID.
     * @param eventTypeList - ServiceNames like PatientDiscovery (PD), DocumentQuery (DQ) and other CONNECT supported
     *            Nwhin services
     * @param remoteHcidList - Remote Organization ID's
     * @param endDate - Event End date
     * @return List
     */
    public List<AuditRepositoryRecord> queryByAuditOptions(final List<String> eventTypeList, final String userId,
            final List<String> remoteHcidList, final Date startDate, final Date endDate) {

        Session session = null;
        List<AuditRepositoryRecord> queryList = null;
        try {
            session = getSession();
            LOG.info("Getting Record for Audit Query ");

            if (session != null) {
                // Build the criteria
                final Criteria queryCriteria = session.createCriteria(AuditRepositoryRecord.class);

                if (NullChecker.isNotNullish(eventTypeList)) {
                    queryCriteria.add(Restrictions.in("eventType", eventTypeList));
                }

                if (NullChecker.isNotNullish(userId)) {
                    queryCriteria.add(Restrictions.eq("userId", userId));
                }

                if (NullChecker.isNotNullish(remoteHcidList)) {
                    queryCriteria.add(Restrictions.in("remoteHcid", remoteHcidList));
                }

                if (startDate != null && endDate != null) {
                    queryCriteria.add(Expression.between("eventTimestamp", startDate, endDate));
                } else if (startDate != null) {
                    // Criteria added for when the request has no end date
                    queryCriteria.add(Restrictions.ge("eventTimestamp", startDate));
                } else if (endDate != null) {
                    // The request has an end date, but there is no start date
                    queryCriteria.add(Restrictions.le("eventTimestamp", endDate));
                }

                // if no criteria is passed then it will search full database with above mentioned columns in the result
                queryList = queryCriteria.list();
            }

        } catch (final HibernateException e) {
            LOG.error("Exception in AuditLog.get() occurred due to : {}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session);
        }
        return queryList;
    }

    /**
     * This method does a query to database to get the Audit Log Blob Messages based on the auditId
     *
     * @param auditId - AuidtRepository table Primary Key unique Id
     * @return Blob - Audit Blob message corresponding to Audit Id supplied
     */
    public Blob queryByAuditId(final long auditId) {

        Session session = null;
        Blob message = null;
        try {
            session = getSession();
            LOG.info("Getting Record for Audit Viewer using auditId");

            if (session != null) {
                // Build the criteria
                final Criteria queryCriteria = session.createCriteria(AuditRepositoryRecord.class);
                queryCriteria.add(Restrictions.eq("id", auditId));
                queryCriteria.setProjection(Projections.property("message"));

                message = (Blob) queryCriteria.uniqueResult();
            }
        } catch (final HibernateException e) {
            LOG.error("Exception in AuditLog.get() occurred due to : {}", e.getLocalizedMessage(), e);
        } finally {
            closeSession(session);
        }
        return message;
    }

    /**
     * TODO will be moved into Util class in future. Closes the Hibernate Session
     *
     * @param session Hibernate session instance
     * @param flush
     *
     */
    private void closeSession(final Session session) {
        if (session != null) {
            session.close();
        }
    }

    /**
     *
     * @return Hibernate session
     */
    protected Session getSession() {
        Session session = null;
        HibernateUtil hibernateUtil = HibernateUtilFactory.getAuditRepoHibernateUtil();

        if (hibernateUtil != null) {
            session = hibernateUtil.getSessionFactory().openSession();
        }
        return session;
    }

}
