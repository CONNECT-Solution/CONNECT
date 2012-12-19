/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import org.apache.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Data Access Object which logs events in the database.
 */
public class DatabaseEventLoggerDao {

    private static final Logger LOG = Logger.getLogger(DatabaseEventLoggerDao.class);    

    private static class SingletonHolder { 
        public static final DatabaseEventLoggerDao INSTANCE = new DatabaseEventLoggerDao();
    }

    /**
     * Get an instance of DatabaseEventLoggerDao.
     * @return singleton instance of DatabaseEventLoggerDao
     */
    public static DatabaseEventLoggerDao getInstance() {
        LOG.debug("getInstance()...");
        return SingletonHolder.INSTANCE;
    }
    
    /**
     * Insert an event in the database.
     * @param databaseEvent to be added to the database
     * @return true if the event was successfully inserted.
     */
    public boolean insertEvent(DatabaseEvent databaseEvent) {

        Session session = null;
        Transaction tx = null;
        boolean result = true;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            session.persist(databaseEvent);

            tx.commit();
            
        } catch (HibernateException e) {
            result = false;
            transactionRollback(tx);
            LOG.error("Exception during insertion caused by :" + e.getMessage(), e);
        } finally {
            closeSession(session, false);
        }
        
        return result;
    }

    private void closeSession(Session session, boolean flush) {
        if (session != null) {
            if (flush) {
                session.flush();
            }
            session.close();
        }
    }

    private void transactionRollback(Transaction tx) {
        if (tx != null) {
            tx.rollback();
        }
    }
    
}
