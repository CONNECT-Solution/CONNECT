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
package gov.hhs.fha.nhinc.direct.messagemonitoring.dao.impl;

import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.MessageMonitoringDAO;
import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.MessageMonitoringDAOException;
import gov.hhs.fha.nhinc.direct.messagemonitoring.domain.MonitoredMessage;
import gov.hhs.fha.nhinc.direct.messagemonitoring.domain.MonitoredMessageNotification;
import gov.hhs.fha.nhinc.direct.messagemonitoring.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.closeSession;
import static gov.hhs.fha.nhinc.util.GenericDBUtils.rollbackTransaction;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class provides MessageMonitoringDb database interface services
 *
 * @author Naresh Subramanyan
 */
public class MessageMonitoringDAOImpl implements MessageMonitoringDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MessageMonitoringDAOImpl.class);
    private static final String INSERTION_ERROR_LOG = "Exception during insertion caused by : {}";

    private static class SingletonHolder {

        public static final MessageMonitoringDAO INSTANCE = new MessageMonitoringDAOImpl();

        private SingletonHolder() {
        }
    }

    /**
     * Get an instance of MessageMonitoringDAO.
     *
     * @return singleton instance of DatabaseEventLoggerDao
     */
    public static MessageMonitoringDAO getInstance() {
        LOG.debug("getInstance()...");
        return SingletonHolder.INSTANCE;
    }

    /**
     * Inserts the OutgoingMessage and OutgoingMessageNotification
     *
     * @param trackMessage Outgoing message to track
     *
     * @exception MessageMonitoringDAOException
     *
     * @return true if successful else false
     */
    @Override
    public boolean addOutgoingMessage(final MonitoredMessage trackMessage) throws MessageMonitoringDAOException {
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        try {
            LOG.debug("Inside addOutgoingMessage()");
            session = getSession();
            if (session != null) {
                tx = session.beginTransaction();
                session.persist(trackMessage);
                tx.commit();
            }

        } catch (final HibernateException e) {
            result = false;
            rollbackTransaction(tx);
            LOG.error(INSERTION_ERROR_LOG, e.getMessage(), e);
        } finally {
            closeSession(session);
        }

        return result;

    }

    /**
     * Updates the OutgoingMessage and OutgoingMessageNotification
     *
     * @param trackMessage Outgoing message to track
     *
     * @exception MessageMonitoringDAOException
     *
     * @return true if successful else false
     */
    @Override
    public boolean updateOutgoingMessage(final MonitoredMessage trackMessage) throws MessageMonitoringDAOException {
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        try {
            LOG.debug("Inside updateOutgoingMessage()");
            session = getSession();
            if (session != null) {
                tx = session.beginTransaction();
                session.update(trackMessage);
                tx.commit();
            }
        } catch (final HibernateException e) {
            result = false;
            rollbackTransaction(tx);
            LOG.error(INSERTION_ERROR_LOG, e.getMessage(), e);
        } finally {
            closeSession(session);
        }
        return result;
    }

    /**
     * Updates the OutgoingMessageNotification
     *
     * @param trackMessageNotification Outgoing message to track
     *
     * @exception MessageMonitoringDAOException
     *
     * @return true if successful else false
     */
    @Override
    public boolean updateMessageNotification(final MonitoredMessageNotification trackMessageNotification)
        throws MessageMonitoringDAOException {
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        try {
            LOG.debug("Inside updateMessageNotification()");
            session = getSession();
            if (session != null) {
                tx = session.beginTransaction();
                session.update(trackMessageNotification);
                tx.commit();
            }
        } catch (final HibernateException e) {
            result = false;
            rollbackTransaction(tx);
            LOG.error(INSERTION_ERROR_LOG, e.getMessage(), e);
        } finally {
            closeSession(session);
        }
        return result;
    }

    /**
     * Deletes the OutgoingMessage and OutgoingMessageNotification
     *
     * @param trackMessage Outgoing message to track
     *
     * @exception MessageMonitoringDAOException
     *
     * @return true if successful else false
     */
    @Override
    public boolean deleteCompletedMessages(final MonitoredMessage trackMessage) throws MessageMonitoringDAOException {
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        try {
            LOG.debug("Inside deleteCompletedMessages()");
            session = getSession();
            if (session != null) {
                tx = session.beginTransaction();
                session.delete(trackMessage);
                tx.commit();
            }
        } catch (final HibernateException e) {
            result = false;
            rollbackTransaction(tx);
            LOG.error(INSERTION_ERROR_LOG, e.getMessage(), e);
        } finally {
            closeSession(session);
        }
        return result;
    }

    /**
     * Get all pending Outgoing messages
     *
     * @return
     */
    @Override
    public List<MonitoredMessage> getAllPendingMessages() {
        Session session = null;
        List<MonitoredMessage> pendingList = new ArrayList<>();

        try {
            LOG.debug("Inside getAllPendingMessages()");
            session = getSession();
            if (session != null) {
                pendingList = session.createCriteria(MonitoredMessage.class).list();
            }
        } catch (final HibernateException e) {
            LOG.error(INSERTION_ERROR_LOG, e.getMessage(), e);
        } finally {
            closeSession(session);
        }
        return pendingList;
    }

    protected Session getSession() {
        Session session = null;
        HibernateUtil util = HibernateUtilFactory.getMsgMonitorHibernateUtil();
        SessionFactory fact = util.getSessionFactory();
        if (fact != null) {
            session = fact.openSession();
        } else {
            LOG.error("Session is null");
        }
        return session;
    }

}
