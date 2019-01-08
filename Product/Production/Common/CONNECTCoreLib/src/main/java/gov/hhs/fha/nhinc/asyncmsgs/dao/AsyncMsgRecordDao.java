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
package gov.hhs.fha.nhinc.asyncmsgs.dao;

import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JHOPPESC
 */

public class AsyncMsgRecordDao {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncMsgRecordDao.class);

    private final PropertyAccessor accessor;

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

    public AsyncMsgRecordDao() {
        accessor = PropertyAccessor.getInstance();
    }

    public AsyncMsgRecordDao(PropertyAccessor accessor) {
        this.accessor = accessor;
    }

    /**
     * Query by Message Id. This should return only one record.
     *
     * @param messageId
     * @param direction
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByMessageIdAndDirection(String messageId, String direction) {
        LOG.debug(
            "Performing database record retrieve using message id: " + messageId + " and direction: " + direction);

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            sess = getSession();
            if (sess != null) {
                Query query = sess.getNamedQuery("queryByMessageIdAndDirection");
                query.setParameter("MessageId", messageId);
                query.setParameter("Direction", direction);
                asyncMsgRecs = query.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve by message id. Results found: "
                    + (asyncMsgRecs == null ? "0" : Integer.toString(asyncMsgRecs.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }

        return asyncMsgRecs;
    }

    /**
     * Query by Message Id and Service Name. This should return only one record.
     *
     * @param messageId
     * @param serviceName
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByMessageIdAndServiceName(String messageId, String serviceName) {
        LOG.debug(
            "Performing database record retrieve using message id: " + messageId + "and service name: " + serviceName);

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            sess = getSession();
            if (sess != null) {
                Query query = sess.getNamedQuery("queryByMessageIdAndServiceName");
                query.setParameter("MessageId", messageId);
                query.setParameter("ServiceName", serviceName);
                asyncMsgRecs = query.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve by message id and service name. Results found: "
                    + (asyncMsgRecs == null ? "0" : Integer.toString(asyncMsgRecs.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }

        return asyncMsgRecs;
    }

    /**
     * Query for Creation Time less than passed timestamp.
     *
     * @param timestamp A timestamp
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByTime(Date timestamp) {
        LOG.debug("Performing database retrieve using timestamp");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            sess = getSession();
            if (sess != null) {
                Query query = sess.getNamedQuery("queryByTime");
                query.setParameter("CreationTime", timestamp);
                asyncMsgRecs = query.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve by timestamp. Results found: "
                    + (asyncMsgRecs == null ? "0" : Integer.toString(asyncMsgRecs.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }

        return asyncMsgRecs;
    }

    /**
     * Query for Creation Time less than passed timestamp and status equal to Request Received Acknowledged [REQRCVDACK]
     *
     * @param timestamp A timestamp
     * @return matching records
     */
    public List<AsyncMsgRecord> queryForExpired(Date timestamp) {
        LOG.debug("Performing database retrieve for expired requests");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            sess = getSession();
            if (sess != null) {
                Query query = sess.getNamedQuery("queryForExpired");
                query.setParameter("CreationTime", timestamp);
                asyncMsgRecs = query.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve by timestamp. Results found: "
                    + (asyncMsgRecs == null ? "0" : Integer.toString(asyncMsgRecs.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
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
        LOG.debug("Performing database record retrieve for deferred queue manager processing.");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            sess = getSession();
            if (sess != null) {
                Query query = sess.getNamedQuery("queryForDeferredQueueProcessing");
                asyncMsgRecs = query.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve for deferred queue manager processing. Results found: "
                    + (asyncMsgRecs == null ? "0" : Integer.toString(asyncMsgRecs.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }

        return asyncMsgRecs;
    }

    /**
     * Query for all records that are already selected. This will occur if a prior process was interrupted before all
     * selected records processing was complete.
     *
     * @return matching records
     */
    public List<AsyncMsgRecord> queryForDeferredQueueSelected() {
        LOG.debug("Performing database record retrieve for deferred queue manager selected.");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            sess = getSession();
            if (sess != null) {
                Query query = sess.getNamedQuery("queryForDeferredQueueSelected");
                asyncMsgRecs = query.list();
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve for deferred queue manager selected. Results found: "
                    + (asyncMsgRecs == null ? "0" : Integer.toString(asyncMsgRecs.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }

        return asyncMsgRecs;
    }

    /**
     * Query by Message Id and Service Name. This should return only one record.
     *
     * @param queryCriteria
     * @return matching records
     */
    public List<AsyncMsgRecord> queryByCriteria(QueryDeferredQueueRequestType queryCriteria) {
        LOG.debug("Performing database record retrieve using AsyncMsgRecord instance to hold criteria.");

        List<AsyncMsgRecord> asyncMsgRecs = null;
        Session sess = null;

        try {
            sess = getSession();
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
                if (queryCriteria.getServiceName() != null && !queryCriteria.getServiceName().isEmpty()) {
                    criteria.add(Restrictions.in("ServiceName", queryCriteria.getServiceName()));
                    criteriaPopulated = true;
                }
                if (queryCriteria.getDirection() != null) {
                    criteria.add(Restrictions.eq("Direction", queryCriteria.getDirection()));
                    criteriaPopulated = true;
                }
                if (queryCriteria.getCommunityId() != null && !queryCriteria.getCommunityId().isEmpty()) {
                    criteria.add(Restrictions.in("CommunityId", queryCriteria.getCommunityId()));
                    criteriaPopulated = true;
                }
                if (queryCriteria.getStatus() != null && !queryCriteria.getStatus().isEmpty()) {
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
                    LOG.error("No query criteria defined.  At least one criteria value must be defined.");
                }
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed database record retrieve by criteria. Results found: "
                    + (asyncMsgRecs == null ? "0" : Integer.toString(asyncMsgRecs.size())));
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: " + he.getLocalizedMessage(), he);
                }
            }
        }

        return asyncMsgRecs;
    }

    /**
     * Insert list of records.
     *
     * @param asyncMsgRecs object to save.
     * @return true - success; false - failure
     */
    public boolean insertRecords(List<AsyncMsgRecord> asyncMsgRecs) {
        LOG.debug("AsyncMsgRecordDao.insertRecords() - Begin");

        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (NullChecker.isNotNullish(asyncMsgRecs)) {
            int size = asyncMsgRecs.size();
            AsyncMsgRecord dbRecord;

            try {
                session = getSession();
                tx = session.beginTransaction();

                LOG.info("Inserting Record...");

                for (int i = 0; i < size; i++) {
                    dbRecord = asyncMsgRecs.get(i);
                    session.persist(dbRecord);
                }

                tx.commit();
                LOG.info("AsyncMsgRecord List Inserted successfully...");
            } catch (HibernateException he) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error("Error during insertion caused by: {}", he.getLocalizedMessage(), he);
            } finally {
                // Actual insertion will happen at this step
                if (session != null) {
                    try {
                        session.close();
                    } catch (HibernateException he) {
                        LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                    }
                }
            }
        }

        LOG.debug("AsyncMsgRecordDao.insertRecords() - End");
        return result;
    }

    /**
     * Save a record to the database. Insert if pk is null. Update otherwise.
     *
     * @param asyncMsgRecord object to save.
     */
    public void save(AsyncMsgRecord asyncMsgRecord) {
        LOG.debug("AsyncMsgRecordDao.save() - Begin");

        Session sess = null;
        Transaction trans = null;
        try {
            sess = getSession();
            if (sess != null) {
                trans = sess.beginTransaction();
                sess.saveOrUpdate(asyncMsgRecord);
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (HibernateException he) {
                    LOG.error("Failed to commit transaction: {}", he.getLocalizedMessage(), he);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }

        LOG.debug("AsyncMsgRecordDao.save() - End");
    }

    /**
     * Save records to the database. Insert if pk is null. Update otherwise.
     *
     * @param asyncMsgRecs object to save.
     */
    public void save(List<AsyncMsgRecord> asyncMsgRecs) {
        LOG.debug("AsyncMsgRecordDao.save(list) - Begin");

        Session sess = null;
        Transaction trans = null;
        try {
            sess = getSession();
            if (sess != null) {
                trans = sess.beginTransaction();

                int size = asyncMsgRecs.size();
                AsyncMsgRecord dbRecord;

                LOG.info("Saving Records...");

                for (int i = 0; i < size; i++) {
                    dbRecord = asyncMsgRecs.get(i);
                    sess.saveOrUpdate(dbRecord);
                }

                LOG.info("AsyncMsgRecord List Saved successfully...");
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (HibernateException he) {
                    LOG.error("Failed to commit transaction: {}", he.getLocalizedMessage(), he);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }

        LOG.debug("AsyncMsgRecordDao.save(list) - End");
    }

    /**
     * Delete the specified record.
     *
     * @param asyncMsgRecord object to save.
     */
    public void delete(AsyncMsgRecord asyncMsgRecord) {
        LOG.debug("Performing a database record delete on asyncmsgrepo table");

        Session sess = null;
        Transaction trans = null;
        try {
            sess = getSession();
            if (sess != null) {
                trans = sess.beginTransaction();
                sess.delete(asyncMsgRecord);
            } else {
                LOG.error("Failed to obtain a session from the sessionFactory");
            }
        } finally {
            if (trans != null) {
                try {
                    trans.commit();
                } catch (HibernateException he) {
                    LOG.error("Failed to commit transaction: {}", he.getLocalizedMessage(), he);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException he) {
                    LOG.error("Failed to close session: {}", he.getLocalizedMessage(), he);
                }
            }
        }
        LOG.debug("Completed database record delete");
    }

    /**
     * Examine all asyncmsgrepo records to determine if their creation time has expired based on the gateway async
     * expiration settings. All expired records will be updated with an expired status.
     */
    public void checkExpiration() {
        LOG.debug("AsyncMsgRecordDao.checkExpiration() - Begin");

        // Read the delta properties from the gateway.properties file
        long value;
        String units;

        value = getValueFromPropFile();
        units = getUnitsFromPropFile();

        // Determine the time to query on
        Date expirationValue = calculateExpirationValue(value, units);

        // Query the database for all records older then the calculated time
        List<AsyncMsgRecord> asyncMsgRecs = queryForExpired(expirationValue);

        // Update all of the records that were returned from the query
        for (AsyncMsgRecord rec : asyncMsgRecs) {
            rec.setDuration(Long.valueOf(0));
            rec.setStatus(QUEUE_STATUS_EXPIRED);
            save(rec);
        }

        LOG.debug("AsyncMsgRecordDao.checkExpiration() - End");
    }

    /**
     * @return
     */
    private String getUnitsFromPropFile() {
        String units = null;
        try {
            units = accessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_PROP);
        } catch (PropertyAccessException ex) {
            LOG.error("Error: Failed to retrieve {} from property file {}: {}",
                NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_PROP, NhincConstants.GATEWAY_PROPERTY_FILE,
                ex.getLocalizedMessage(), ex);
        }
        return units;
    }

    /**
     * @return
     */
    private long getValueFromPropFile() {
        long value = 0;
        try {
            value = accessor.getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.ASYNC_DB_REC_EXP_VAL_PROP);
        } catch (PropertyAccessException ex) {
            LOG.error("Error: Failed to retrieve {} from property file {}: {}",
                NhincConstants.ASYNC_DB_REC_EXP_VAL_PROP, NhincConstants.GATEWAY_PROPERTY_FILE,
                ex.getLocalizedMessage(), ex);
        }
        return value;
    }

    private Date calculateExpirationValue(long value, String units) {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Convert the long to a Long Object and change the sign to negative so our query value ends up in the past.
        Long longObj = -value;

        if (NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_SEC.equalsIgnoreCase(units)) {
            currentTime.add(Calendar.SECOND, longObj.intValue());
        } else if (NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_MIN.equalsIgnoreCase(units)) {
            currentTime.add(Calendar.MINUTE, longObj.intValue());
        } else if (NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_HOUR.equalsIgnoreCase(units)) {
            currentTime.add(Calendar.HOUR_OF_DAY, longObj.intValue());
        } else {
            // Default to days
            currentTime.add(Calendar.DAY_OF_YEAR, longObj.intValue());
        }

        return currentTime.getTime();
    }

    /**
     * Returns a new session from AsyncMessages HibernateUtil
     *
     * @return
     */
    protected Session getSession() {

        Session session = null;
        HibernateUtil hibernateUtil = HibernateUtilFactory.getAsyncMsgsHibernateUtil();
        if (hibernateUtil != null) {
            session = hibernateUtil.getSessionFactory().openSession();
        }

        return session;
    }

}
