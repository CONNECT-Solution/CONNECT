/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.asyncmsgs.dao;

import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMsgRecordDao {

    private static Log log = LogFactory.getLog(AsyncMsgRecordDao.class);

    public static final String QUEUE_DIRECTION_INBOUND = "INBOUND";
    public static final String QUEUE_DIRECTION_OUTBOUND = "OUTBOUND";

    public static final String QUEUE_RESPONSE_TYPE_AUTO = "AUTO";
    public static final String QUEUE_RESPONSE_TYPE_EXTERNAL = "EXTERNAL";

    public static final String QUEUE_STATUS_EXPIRED = "EXPIRED";

    public static final String QUEUE_STATUS_REQPROCESS = "REQPROCESS";
    public static final String QUEUE_STATUS_REQRCVD = "REQRCVD";
    public static final String QUEUE_STATUS_REQRCVDACK = "REQRCVDACK";
    public static final String QUEUE_STATUS_REQRCVDERR = "REQRCVDERR";
    public static final String QUEUE_STATUS_REQSENT = "REQSENT";
    public static final String QUEUE_STATUS_REQSENTACK = "REQSENTACK";
    public static final String QUEUE_STATUS_REQSENTERR = "REQSENTERR";

    public static final String QUEUE_STATUS_RSPSELECT = "RSPSELECT";
    public static final String QUEUE_STATUS_RSPPROCESS = "RSPPROCESS";
    public static final String QUEUE_STATUS_RSPRCVD = "RSPRCVD";
    public static final String QUEUE_STATUS_RSPRCVDACK = "RSPRCVDACK";
    public static final String QUEUE_STATUS_RSPRCVDERR = "RSPRCVDERR";
    public static final String QUEUE_STATUS_RSPSENT = "RSPSENT";
    public static final String QUEUE_STATUS_RSPSENTACK = "RSPSENTACK";
    public static final String QUEUE_STATUS_RSPSENTERR = "RSPSENTERR";

    /**
     * Query by Message Id.  This should return only one record.
     *
     * @param messageId
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByMessageId(String messageId) {
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

    /**
     * Query by Message Id and Service Name.  This should return only one record.
     *
     * @param messageId
     * @param serviceName
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByMessageIdAndServiceName(String messageId, String serviceName) {
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

    /**
     * Query for Creation Time less than passed timestamp.
     *
     * @param timestamp
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByTime(Date timestamp) {
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

    /**
     * Query for Creation Time less than passed timestamp and status equal to
     * Request Receieved Acknowledged [REQRCVDACK]
     *
     * @param timestamp
     * @return matching records
     */
    public List<AsyncMsgRecord> queryForExpired(Date timestamp) {
        log.debug("Performing database retrieve for expired requests");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(AsyncMsgRecord.class);
                    criteria.add(Restrictions.lt("CreationTime", timestamp));
                    criteria.add(Restrictions.or(
                            Restrictions.eq("Status", QUEUE_STATUS_REQRCVD),
                            Restrictions.eq("Status", QUEUE_STATUS_REQRCVDACK)));
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

    /**
     * Query for all records to be processed by the Deferred Queue Manager.
     *
     * @return matching records
     */
    public List<AsyncMsgRecord> queryForDeferredQueueProcessing() {
        log.debug("Performing database record retrieve for deferred queue manager processing.");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(AsyncMsgRecord.class);
                    criteria.add(Restrictions.eq("Direction", QUEUE_DIRECTION_INBOUND));
                    criteria.add(Restrictions.eq("ResponseType", QUEUE_RESPONSE_TYPE_AUTO));
                    criteria.add(Restrictions.eq("Status", QUEUE_STATUS_REQRCVDACK));
                    asyncMsgRecs = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Completed database record retrieve for deferred queue manager processing. Results found: " + ((asyncMsgRecs == null) ? "0" : Integer.toString(asyncMsgRecs.size())));
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

    /**
     * Query for all records that are already selected.  This will occur if a prior
     * process was interrupted before all selected records processing was complete.
     *
     * @return matching records
     */
    public List<AsyncMsgRecord> queryForDeferredQueueSelected() {
        log.debug("Performing database record retrieve for deferred queue manager selected.");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(AsyncMsgRecord.class);
                    criteria.add(Restrictions.eq("Direction", QUEUE_DIRECTION_INBOUND));
                    criteria.add(Restrictions.eq("ResponseType", QUEUE_RESPONSE_TYPE_AUTO));
                    criteria.add(Restrictions.eq("Status", QUEUE_STATUS_RSPSELECT));
                    asyncMsgRecs = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Completed database record retrieve for deferred queue manager selected. Results found: " + ((asyncMsgRecs == null) ? "0" : Integer.toString(asyncMsgRecs.size())));
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

    /**
     * Query by Message Id and Service Name.  This should return only one record.
     *
     * @param messageId
     * @param serviceName
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByCriteria(QueryDeferredQueueRequestType queryCriteria) {
        log.debug("Performing database record retrieve using AsyncMsgRecord instance to hold criteria.");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    // Instantiate Criteria class and populate based on non-null queryCriteria attributes
                    boolean criteriaPopulated = false;
                    Criteria criteria = sess.createCriteria(AsyncMsgRecord.class);

                    if (queryCriteria.getCreationBeginTime() != null) {
                        Date date = XMLDateUtil.gregorian2date(queryCriteria.getCreationBeginTime());
                        criteria.add(Restrictions.ge("CreationTime", date));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getCreationEndTime() != null) {
                        Date date = XMLDateUtil.gregorian2date(queryCriteria.getCreationEndTime());
                        criteria.add(Restrictions.le("CreationTime", date));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getResponseBeginTime() != null) {
                        Date date = XMLDateUtil.gregorian2date(queryCriteria.getResponseBeginTime());
                        criteria.add(Restrictions.ge("ResponseTime", date));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getResponseEndTime() != null) {
                        Date date = XMLDateUtil.gregorian2date(queryCriteria.getResponseEndTime());
                        criteria.add(Restrictions.le("ResponseTime", date));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getServiceName() != null && queryCriteria.getServiceName().size() > 0) {
                        criteria.add(Restrictions.in("ServiceName", queryCriteria.getServiceName()));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getDirection() != null) {
                        criteria.add(Restrictions.eq("Direction", queryCriteria.getDirection()));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getCommunityId() != null && queryCriteria.getCommunityId().size() > 0) {
                        criteria.add(Restrictions.in("CommunityId", queryCriteria.getCommunityId()));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getStatus() != null && queryCriteria.getStatus().size() > 0) {
                        criteria.add(Restrictions.in("Status", queryCriteria.getStatus()));
                        criteriaPopulated = true;
                    }
                    if (queryCriteria.getResponseType() != null) {
                        criteria.add(Restrictions.eq("ResponseType", queryCriteria.getResponseType()));
                        criteriaPopulated = true;
                    }

                    // If at least one queryCriteria value was populated, get the results
                    if (criteriaPopulated) {
                        asyncMsgRecs = criteria.list();
                    } else {
                        log.error("No query criteria defined.  At least one criteria value must be defined.");
                    }
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("Completed database record retrieve by criteria. Results found: " + ((asyncMsgRecs == null) ? "0" : Integer.toString(asyncMsgRecs.size())));
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

    /**
     * Insert list of records
     *
     * @param asyncMsgRecs
     * @return true - success; false - failure
     */
    public boolean insertRecords(List<AsyncMsgRecord> asyncMsgRecs) {
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

    /**
     * Save a record to the database. Insert if pk is null. Update otherwise.
     *
     * @param object to save.
     */
    public void save(AsyncMsgRecord asyncMsgRecord) {
        log.debug("AsyncMsgRecordDao.save() - Begin");

        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();
                    sess.saveOrUpdate(asyncMsgRecord);
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

        log.debug("AsyncMsgRecordDao.save() - End");
    }

    /**
     * Save records to the database. Insert if pk is null. Update otherwise.
     *
     * @param asyncMsgRecs
     */
    public void save(List<AsyncMsgRecord> asyncMsgRecs) {
        log.debug("AsyncMsgRecordDao.save(list) - Begin");

        Session sess = null;
        Transaction trans = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    trans = sess.beginTransaction();

                    int size = asyncMsgRecs.size();
                    AsyncMsgRecord dbRecord = null;

                    log.info("Saving Records...");

                    for (int i = 0; i < size; i++) {
                        dbRecord = (AsyncMsgRecord) asyncMsgRecs.get(i);
                        sess.saveOrUpdate(dbRecord);
                    }

                    log.info("AsyncMsgRecord List Saved successfully...");
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

        log.debug("AsyncMsgRecordDao.save(list) - End");
    }

    /**
     * Delete the specified record.
     *
     * @param asyncMsgRecord
     */
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

    /**
     * Examine all asyncmsgrepo records to determine if their creation time has
     * expired based on the gateway async expiration settings.  All expired
     * records will be updated with an expired status.
     */
    public void checkExpiration() {
        log.debug("AsyncMsgRecordDao.checkExpiration() - Begin");

        // Read the delta properties from the gateway.properties file
        long value = 0;
        String units = null;

        try {
            value = PropertyAccessor.getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ASYNC_DB_REC_EXP_VAL_PROP);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.ASYNC_DB_REC_EXP_VAL_PROP + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        try {
            units = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_PROP);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_PROP + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        // Determine the time to query on
        Date expirationValue = calculateExpirationValue(value, units);

        // Query the database for all records older then the calculated time
        List<AsyncMsgRecord> asyncMsgRecs = queryForExpired(expirationValue);

        // Update all of the records that were returned from the query
        for (AsyncMsgRecord rec : asyncMsgRecs) {
            rec.setDuration(new Long(0));
            rec.setStatus(QUEUE_STATUS_EXPIRED);
            save(rec);
        }

        log.debug("AsyncMsgRecordDao.checkExpiration() - End");
    }

    private Date calculateExpirationValue(long value, String units) {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Convert the long to a Long Object and change the sign to negative so our query value ends up in the past.
        Long longObj = Long.valueOf(0 - value);

        if (units.equalsIgnoreCase(NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_SEC)) {
            currentTime.add(Calendar.SECOND, longObj.intValue());
        } else if (units.equalsIgnoreCase(NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_MIN)) {
            currentTime.add(Calendar.MINUTE, longObj.intValue());
        } else if (units.equalsIgnoreCase(NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_HOUR)) {
            currentTime.add(Calendar.HOUR_OF_DAY, longObj.intValue());
        } else {
            // Default to days
            currentTime.add(Calendar.DAY_OF_YEAR, longObj.intValue());
        }

        Date expirationValue = currentTime.getTime();

        return expirationValue;
    }

}
