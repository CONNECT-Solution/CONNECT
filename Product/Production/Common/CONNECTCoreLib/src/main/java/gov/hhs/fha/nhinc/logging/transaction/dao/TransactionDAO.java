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
package gov.hhs.fha.nhinc.logging.transaction.dao;

import gov.hhs.fha.nhinc.logging.transaction.model.TransactionRepo;
import gov.hhs.fha.nhinc.logging.transaction.persistance.HibernateUtil;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TransactionDAO provides methods to query and update the transrepo database.
 *
 * @author jasonasmith
 *
 */
public class TransactionDAO {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionDAO.class);
    private static final TransactionDAO INSTANCE = new TransactionDAO();

    /**
     * The constructor.
     */
    private TransactionDAO() {
        LOG.info("TransactionDAO initialized");
    }

    /**
     * Returns the instance of the DAO according to the Singleton Pattern.
     *
     * @return TransactionDAO
     */
    public static TransactionDAO getInstance() {
        LOG.debug("getTransactionDAOInstance()...");
        return INSTANCE;
    }

    /**
     * Inserts a single TransactionRepo object into the database, returns boolean on success or failure.
     *
     * @param transactionRepo
     * @return boolean
     */
    public boolean insertIntoTransactionRepo(final TransactionRepo transactionRepo) {

        LOG.debug("TransactionDAO.insertIntoTransactionRepo() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (transactionRepo != null) {
            try {
                final SessionFactory sessionFactory = getSessionFactory();
                if (sessionFactory != null) {
                    session = sessionFactory.openSession();
                }
                if (session == null) {
                    LOG.error("Session equal");
                    return false;
                }

                tx = session.beginTransaction();
                LOG.info("Inserting Record...");

                session.persist(transactionRepo);

                LOG.info("TransactionRepo Inserted successfully...");
                tx.commit();
            } catch (final HibernateException e) {
                result = false;
                transactionRollback(tx);
                LOG.error("Exception during insertion caused by :" + e.getMessage(), e);
            } finally {
                closeSession(session);
            }
        }
        LOG.debug("TransactionDAO.insertIntoTransactionRepo() - End");
        return result;
    }

    /**
     * Queries the database for a transaction record using the messageId.
     *
     * @param messageId
     * @return String
     */
    @SuppressWarnings("unchecked")
    public String getTransactionId(final String messageId) {
        LOG.debug("TransactionDAO.getTransactinId() - Begin");

        if (NullChecker.isNullish(messageId)) {
            LOG.info("-- MessageId Parameter is required for Transaction Query --");
            LOG.debug("TransactionDAO.getTransactinId() - End");
            return null;
        }

        Session session = null;

        try {
            final SessionFactory sessionFactory = getSessionFactory();
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
            }
            if (LOG.isDebugEnabled()) {
                LOG.info("Getting Records");
            }
            if (session == null) {
                LOG.error("Session equal");
                return null;
            }
            final Query namedQuery = session.getNamedQuery("findTransactionByMessageId");

            namedQuery.setString("messageId", messageId);

            final List<TransactionRepo> queryList = namedQuery.list();

            if (!queryList.isEmpty()) {
                final TransactionRepo trans = queryList.get(0);
                return trans.getTransactionId();
            }
        } catch (final Exception e) {
            LOG.error("Exception in getTransactionId() occured due to :" + e.getMessage(), e);
        } finally {
            closeSession(session);
        }

        return null;
    }

    private void closeSession(final Session session) {
        if (session != null) {
            session.close();
        }
    }

    private void transactionRollback(final Transaction tx) {
        if (tx != null) {
            tx.rollback();
        }
    }

    /**
     * Protected method that gets the SingletonHolder's sessionFactory.
     *
     * @return sessionFactory
     */
    protected static SessionFactory getSessionFactory() {
        SessionFactory fact = null;
        HibernateUtil util = HibernateUtilFactory.getTransactionHibernateUtil();
        if (util != null) {
            fact = util.getSessionFactory();
        }
        return fact;
    }

}
