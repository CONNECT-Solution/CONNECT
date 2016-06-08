/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.event.dao;

import gov.hhs.fha.nhinc.event.model.DatabaseEvent;
import gov.hhs.fha.nhinc.event.persistence.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object which logs events in the database.
 */
public class DatabaseEventLoggerDao {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseEventLoggerDao.class);

    private static final String EVENT_TYPE_NAME = "eventName";
    private static final String EVENT_SERVICETYPE_NAME = "serviceType";
    private static final String DATE_NAME = "eventTime";

    private HibernateUtil hibernateUtil = new HibernateUtil();

    private static class SingletonHolder {
        public static final DatabaseEventLoggerDao INSTANCE = new DatabaseEventLoggerDao();
    }

    /**
     * Get an instance of DatabaseEventLoggerDao.
     *
     * @return singleton instance of DatabaseEventLoggerDao
     */
    public static DatabaseEventLoggerDao getInstance() {
        LOG.debug("getInstance()...");
        return SingletonHolder.INSTANCE;
    }

    /**
     * Insert an event in the database.
     *
     * @param databaseEvent to be added to the database
     * @return true if the event was successfully inserted.
     */
    public boolean insertEvent(final DatabaseEvent databaseEvent) {

        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            session.persist(databaseEvent);

            tx.commit();

        } catch (final HibernateException e) {
            result = false;
            transactionRollback(tx);
            LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);
        } finally {
            closeSession(session);
        }

        return result;
    }

    /**
     * Hibernate Query for event counts for a given Event type grouping by HCID and service type.
     *
     * @param eventType Location of event call in processing.
     * @return List of Object[] with [0] the count, [1] the initiating hcid, and [2] the service type.
     */
    public List getCounts(final String eventType, final String hcidType) {
        final SessionFactory sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();

        List results = session.createCriteria(DatabaseEvent.class).add(Restrictions.eq(EVENT_TYPE_NAME, eventType))
                .setProjection(Projections.projectionList().add(Projections.rowCount())
                        .add(Projections.groupProperty(hcidType))
                        .add(Projections.groupProperty(EVENT_SERVICETYPE_NAME)))
                .list();

        closeSession(session);

        return results;
    }

    public DatabaseEvent getLatestEvent(final String eventType) {

        final SessionFactory sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();

        DatabaseEvent event = (DatabaseEvent) session.createCriteria(DatabaseEvent.class)
                .add(Restrictions.eq(EVENT_TYPE_NAME, eventType)).addOrder(Order.desc(DATE_NAME)).setMaxResults(1)
                .uniqueResult();

        closeSession(session);

        return event;
    }

    private void closeSession(final Session session) {
        if (session != null) {
            session.close();
        }
        hibernateUtil.closeSessionFactory();
    }

    private void transactionRollback(final Transaction tx) {
        if (tx != null) {
            tx.rollback();
        }
    }

    protected SessionFactory getSessionFactory() {
        hibernateUtil.buildSessionFactory();
        return hibernateUtil.getSessionFactory();
    }

}
