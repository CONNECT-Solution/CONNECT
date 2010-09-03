/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.asyncmsgs.dao;

import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMsgRecordDao {
    private static Log log = LogFactory.getLog(AsyncMsgRecordDao.class);

    public List queryByMessageId(String messageId) {
        log.debug("Performing database record retrieve using message id: " + messageId);

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(AsyncMsgRecord.class);
                    criteria.add(Restrictions.eq("MessageId", messageId));

                    asyncMsgRecs = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Completed database record retrieve by message id. Results found: " + ((asyncMsgRecs == null) ? "0" : Integer.toString(asyncMsgRecs.size())));
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

        return asyncMsgRecs;
    }

    public List queryByMessageIdAndServiceName(String messageId, String serviceName) {
        log.debug("Performing database record retrieve using message id: " + messageId + "and service name: " + serviceName);

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(AsyncMsgRecord.class);
                    criteria.add(Restrictions.eq("MessageId", messageId));
                    criteria.add(Restrictions.eq("ServiceName", serviceName));
                    asyncMsgRecs = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Completed database record retrieve by message id and service name. Results found: " + ((asyncMsgRecs == null) ? "0" : Integer.toString(asyncMsgRecs.size())));
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

        return asyncMsgRecs;
    }

    public List queryByTime(Date timestamp) {
        log.debug("Performing database retrieve using timestamp");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(AsyncMsgRecord.class);
                    criteria.add(Restrictions.lt("CreationTime", timestamp));
                    asyncMsgRecs = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Completed database record retrieve by timestamp. Results found: " + ((asyncMsgRecs == null) ? "0" : Integer.toString(asyncMsgRecs.size())));
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

        return asyncMsgRecs;
    }

    public boolean insertRecords (List<AsyncMsgRecord> asyncMsgRecs) {
        log.debug("AsyncMsgRecordDao.insertRecords() - Begin");
        
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (NullChecker.isNotNullish(asyncMsgRecs)) {
            int size = asyncMsgRecs.size();
            AsyncMsgRecord dbRecord = null;

            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();

                log.info("Inserting Record...");

                for (int i = 0; i < size; i++) {
                    dbRecord = (AsyncMsgRecord) asyncMsgRecs.get(i);
                    session.persist(dbRecord);
                }

                log.info("AsyncMsgRecord List Inserted successfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Error during insertion caused by :" + e.getMessage());
            } finally {
                // Actual insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }

        log.debug("AsyncMsgRecordDao.insertRecords() - End");
        return result;
    }

    public void delete(AsyncMsgRecord asyncMsgRecord) {
        log.debug("Performing a database record delete on asyncmsgrepo table");

        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.delete(asyncMsgRecord);
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
        log.debug("Completed database record delete");
    }

}
