/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * AuditRepositoryDAO Class provides methods to query and update Audit Data to/from MySQL Database using Hibernate
 *
 * @author svalluripalli
 */
public class AuditRepositoryDAO {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRepositoryDAO.class);
    private static AuditRepositoryDAO auditDAO = new AuditRepositoryDAO();
    public static String JAVA_IO_TMPDIR = "java.io.tmpdir";

    /**
     * Constructor
     */
    private AuditRepositoryDAO() {
        LOG.trace("AuditRepositoryDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     *
     * @return AuditRepositoryDAO
     */
    public static AuditRepositoryDAO getAuditRepositoryDAOInstance() {
        LOG.trace("AuditRepositoryDAO instance initialized");
        return auditDAO;
    }

    /**
     *
     * @param auditList
     * @return boolean
     */
    public boolean insertAuditRepository(List<AuditRepositoryRecord> auditList) {

        Session session = null;
        Transaction tx = null;
        boolean result = true;
        if (auditList != null && auditList.size() > 0) {
            int size = auditList.size();
            AuditRepositoryRecord auditRecord = null;

            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                LOG.info("Inserting Record...");
                for (int i = 0; i < size; i++) {
                    auditRecord = (AuditRepositoryRecord) auditList.get(i);
                    session.persist(auditRecord);
                }
                LOG.info("AuditRepository List Inserted seccussfully...");
                tx.commit();
            } catch (HibernateException e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error("Error during insertion caused by :" + e.getLocalizedMessage(), e);
            } finally {
                // Actual event_log insertion will happen at this step
                if (session != null) {
                    session.close();
                }
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
    public List queryAuditRepositoryOnCriteria(String eUserId, String ePatientId, Date startDate, Date endDate) {

        if (eUserId == null && ePatientId == null && startDate == null) {
            LOG.info("-- No - Input Parameters found for Audit Query --");
            return null;
        }

        Session session = null;
        List<AuditRepositoryRecord> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            LOG.info("Getting Record");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(AuditRepositoryRecord.class);
            if (eUserId != null && !eUserId.equals("")) {
                aCriteria.add(Expression.eq("userId", eUserId));
            }
            if (ePatientId != null && !ePatientId.equals("")) {
                aCriteria.add(Expression.eq("receiverPatientId", ePatientId));
            }

            if (startDate != null && endDate != null) {
                aCriteria.add(Expression.between("timeStamp", new Date(startDate.getTime()),
                    new Date(endDate.getTime())));
            } else if (startDate != null && endDate == null) {
                aCriteria.add(Expression.ge("timeStamp", new Date(startDate.getTime())));
            }
            queryList = aCriteria.list();
        } catch (HibernateException e) {
            LOG.error("Exception in AuditLog.get() occurred due to :" + e.getLocalizedMessage(), e);
        } finally {
            // Actual contact insertion will happen at this step
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        return queryList;
    }

    /**
     * This method does a query to database to get the Audit Log Messages based different options
     *
     * @param messageId
     * @param relatesTo
     * @return List
     */
    public List queryAuditRecords(String messageId, String relatesTo) {

        Session session = null;
        List<AuditRepositoryRecord> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            LOG.info("Getting Record for Audit Viewer ");

            // Build the criteria
            Criteria queryCriteria = session.createCriteria(AuditRepositoryRecord.class);

            if (NullChecker.isNotNullish(messageId)) {
                queryCriteria.add(Restrictions.eq("messageId", messageId));

            }

            if (NullChecker.isNotNullish(relatesTo)) {
                queryCriteria.add(Restrictions.eq("relatesTo", relatesTo));

            }

            queryCriteria.setProjection(Projections.projectionList()
                .add(Projections.property("userId"))
                .add(Projections.property("eventType"))
                .add(Projections.property("eventId"))
                .add(Projections.property("outcome"))
                .add(Projections.property("eventTimestamp"))
                .add(Projections.property("remoteHcid"))
                .add(Projections.property("relatesTo"))
                .add(Projections.property("direction"))
                .add(Projections.property("id"))
                .add(Projections.property("messageId")));

            // if no criteria is passed then it will search full database with above mentioned columns in the result
            queryList = queryCriteria.list();

        } catch (HibernateException e) {
            LOG.error("Exception in AuditLog.get() occurred due to :" + e.getLocalizedMessage(), e);
        } finally {
            // Actual contact insertion will happen at this step
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        return queryList;
    }

    /**
     * This method does a query to database to get the Audit Log Messages based different options
     *
     * @param outcome
     * @param startDate
     * @param userId
     * @param eventTypeList
     * @param remoteHcidList
     * @param endDate
     * @return List
     */
    public List queryByAuditValues(Integer outcome, List<String> eventTypeList, String userId, List<String> remoteHcidList, String startDate, String endDate) {

        Session session = null;
        List<AuditRepositoryRecord> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            LOG.info("Getting Record for Audit Viewer ");

            // Build the criteria
            Criteria queryCriteria = session.createCriteria(AuditRepositoryRecord.class);

            if (outcome != null && outcome >= 0) {
                queryCriteria.add(Restrictions.eq("outcome", outcome));
            }

            if (NullChecker.isNotNullish(eventTypeList)) {
                queryCriteria.add(Restrictions.in("eventType", eventTypeList));
            }

            if (NullChecker.isNotNullish(userId)) {
                queryCriteria.add(Restrictions.eq("userId", userId));
            }

            if (NullChecker.isNotNullish(remoteHcidList)) {
                queryCriteria.add(Restrictions.in("remoteHcid", remoteHcidList));
            }

            if (NullChecker.isNotNullish(startDate) && NullChecker.isNotNullish(endDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                queryCriteria.add(Restrictions.between("eventTimestamp", new Date(sdf.parse(startDate).getTime()),
                    new Date(sdf.parse(endDate).getTime())));

            } else if (NullChecker.isNotNullish(startDate) && NullChecker.isNullish(endDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                queryCriteria.add(Restrictions.ge("eventTimestamp", new Date(sdf.parse(startDate).getTime())));

            }

            queryCriteria.setProjection(Projections.projectionList()
                .add(Projections.property("userId"))
                .add(Projections.property("eventType"))
                .add(Projections.property("eventId"))
                .add(Projections.property("outcome"))
                .add(Projections.property("eventTimestamp"))
                .add(Projections.property("remoteHcid"))
                .add(Projections.property("relatesTo"))
                .add(Projections.property("direction"))
                .add(Projections.property("id"))
                .add(Projections.property("messageId")));

            // if no criteria is passed then it will search full database with above mentioned columns in the result
            queryList = queryCriteria.list();

        } catch (HibernateException | ParseException e) {
            LOG.error("Exception in AuditLog.get() occurred due to :" + e.getLocalizedMessage(), e);
        } finally {
            // Actual contact insertion will happen at this step
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        return queryList;
    }

    /**
     * This method does a query to database to get the Audit Log Blob Messages based on the auditId
     *
     * @param auditId
     * @return List
     */
    public List queryAuditViewerByAuditId(String auditId) {

        Session session = null;
        List<AuditRepositoryRecord> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            LOG.info("Getting Record for Audit Viewer using auditId");

            // Build the criteria
            Criteria queryCriteria = session.createCriteria(AuditRepositoryRecord.class);
            if (NullChecker.isNotNullish(auditId)) {
                queryCriteria.add(Restrictions.eq("id", Integer.parseInt(auditId)));
                queryCriteria.setProjection(Projections.property("message"));

            }

            queryList = queryCriteria.list();

        } catch (HibernateException e) {
            LOG.error("Exception in AuditLog.get() occurred due to :" + e.getLocalizedMessage(), e);
        } finally {
            // Actual contact insertion will happen at this step
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        return queryList;
    }

}
