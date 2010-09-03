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
package gov.hhs.fha.nhinc.lift.dao;

import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import gov.hhs.fha.nhinc.lift.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author westberg
 */
public class GatewayLiftMessageDao
{

    private Log log = null;

    /**
     * Default constructor.
     */
    public GatewayLiftMessageDao()
    {
        log = createLogger();
    }

    /**
     * Create a logger object.
     *
     * @return The logger object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * This method returns a session.
     *
     * @return The session.
     */
    protected Session getSession()
    {
        HibernateUtil oHibernateUtil = new HibernateUtil();
        SessionFactory sessionFactory = oHibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        return session;
    }

    /**
     * Start a transaction.
     *
     * @param oSession The session to start the transaction against.
     * @return The transaction that is being returned.
     */
    protected Transaction startTransaction(Session oSession)
    {
        return oSession.beginTransaction();
    }

    /**
     * Create the criteria object.   This is here so that it can be
     * overridden from the Junit tests.
     *
     * @param oSession The session to use to create the criteria.
     * @return The criteria object that is created.
     */
    protected Criteria createCriteria(Session oSession)
    {
        return oSession.createCriteria(GatewayLiftMsgRecord.class);
    }

    /**
     * Retrieve the list of records from the criteria object.
     * This is in a method so it can be overridden in the unit tests.
     * 
     * @param oCriteria The criteria object that contains the criteria.
     * @return The list of GatewayLiftMsgRecord objects that are returned.
     */
    protected List<GatewayLiftMsgRecord> getRecordList(Criteria oCriteria)
    {
        return oCriteria.list();
    }

    /**
     * Insert records into the gateway_lift_message table.
     *
     * @param liftMsgRecs
     * @return
     */
    public boolean insertRecords(List<GatewayLiftMsgRecord> liftMsgRecs)
    {
        log.debug("GatewayLiftMsgRecordDao.insertRecords() - Begin");

        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (NullChecker.isNotNullish(liftMsgRecs))
        {
            int size = liftMsgRecs.size();
            GatewayLiftMsgRecord dbRecord = null;

            try
            {
                session = getSession();
                tx = startTransaction(session);

                log.info("Inserting Record...");

                for (int i = 0; i < size; i++)
                {
                    dbRecord = (GatewayLiftMsgRecord) liftMsgRecs.get(i);
                    session.persist(dbRecord);
                }

                log.info("GatewayLiftMsgRecord List Inserted successfully...");
                tx.commit();
            }
            catch (Exception e)
            {
                result = false;
                if (tx != null)
                {
                    tx.rollback();
                }
                log.error("Error during insertion caused by :" + e.getMessage());
            }
            finally
            {
                // Actual insertion will happen at this step
                if (session != null)
                {
                    session.close();
                }
            }
        }

        log.debug("GatewayLiftMsgRecordDao.insertRecords() - End");
        return result;
    }

    /**
     * Retrieve by the message ID.
     *
     * @param lId The message ID of the message (primary key)
     * @return The result that is being returned.
     */
    public GatewayLiftMsgRecord queryById(Long lId)
    {
        log.debug("Performing database record retrieve using message id: " + lId.longValue());

        List<GatewayLiftMsgRecord> olLiftMsgRecs = null;
        Session oSession = null;

        try
        {
            oSession = getSession();
            if (oSession != null)
            {
                Criteria oCriteria = createCriteria(oSession);
                oCriteria.add(Restrictions.eq("Id", lId));
                olLiftMsgRecs = getRecordList(oCriteria);
            }
            else
            {
                log.error("Failed to obtain a session from the sessionFactory");
            }

            log.debug("Completed database record retrieve by id. Results found: " + ((olLiftMsgRecs == null) ? "0" : Integer.toString(olLiftMsgRecs.size())));
        }
        finally
        {
            if (oSession != null)
            {
                oSession.close();
            }
        }

        // There should be only one record - since this is a primary key.
        // We will only return the first.
        //---------------------------------------------------------------
        if ((olLiftMsgRecs != null) && (olLiftMsgRecs.size() >= 1))
        {
            return olLiftMsgRecs.get(0);
        }
        else
        {
            return null;
        }
    }

    /**
     * Retrieve by the request key guid.
     *
     * @param sRequestKeyGUID The request Key GUID of the message.
     * @return The result that is being returned.
     */
    public GatewayLiftMsgRecord queryByRequestKeyGuid(String sRequestKeyGuid)
    {
        log.debug("Performing database record retrieve using requestKeyGuid: " + sRequestKeyGuid);

        List<GatewayLiftMsgRecord> olLiftMsgRecs = null;
        Session oSession = null;

        try
        {
            oSession = getSession();
            if (oSession != null)
            {
                Criteria oCriteria = createCriteria(oSession);
                oCriteria.add(Restrictions.eq("RequestKeyGuid", sRequestKeyGuid));
                olLiftMsgRecs = getRecordList(oCriteria);
            }
            else
            {
                log.error("Failed to obtain a session from the sessionFactory");
            }

            log.debug("Completed database record retrieve by requestKeyGuid. Results found: " + ((olLiftMsgRecs == null) ? "0" : Integer.toString(olLiftMsgRecs.size())));
        }
        finally
        {
            if (oSession != null)
            {
                oSession.close();
            }
        }

        // There should be only one record - since this should be unique.
        // We will only return the first.
        //---------------------------------------------------------------
        if ((olLiftMsgRecs != null) && (olLiftMsgRecs.size() >= 1))
        {
            return olLiftMsgRecs.get(0);
        }
        else
        {
            return null;
        }
    }

    /**
     * Retrieve by message state and ordered by reverse entry timestamp request key guid.
     *
     * @param sMessageState The message state to be keyed on.
     * @param dtProcessTime The date that records should be older than or equal to.
     * @return The results found matching that message state that is being returned.
     */
    public List<GatewayLiftMsgRecord> queryByMessageTypeOrderByProcessingTime(String sMessageState, Date dtProcessTime)
    {
        log.debug("Performing database record retrieve using messageState: " + sMessageState);

        List<GatewayLiftMsgRecord> olLiftMsgRecs = null;
        Session oSession = null;

        try
        {
            oSession = getSession();
            if (oSession != null)
            {
                Criteria oCriteria = createCriteria(oSession);
                oCriteria.add(Restrictions.eq("MessageState", sMessageState));
                oCriteria.add(Restrictions.le("ProcessingStartTimestamp", dtProcessTime));
                oCriteria.addOrder(Order.asc("ProcessingStartTimestamp"));
                olLiftMsgRecs = getRecordList(oCriteria);
            }
            else
            {
                log.error("Failed to obtain a session from the sessionFactory");
            }

            log.debug("Completed database record retrieve by message type order by processing time. Results found: " + ((olLiftMsgRecs == null) ? "0" : Integer.toString(olLiftMsgRecs.size())));
        }
        finally
        {
            if (oSession != null)
            {
                oSession.close();
            }
        }

        return olLiftMsgRecs;
    }

    /**
     * This method deletes a record from the gateway_lift_message table.
     * 
     * @param GatewayLiftMsgRecord The record to be deleted.  
     */
    public void deleteRecord(GatewayLiftMsgRecord oLiftMsgRecord)
    {
        log.debug("Performing a database record delete on gateway_lift_message table");

        Session oSession = null;
        Transaction oTransaction = null;

        if (oLiftMsgRecord != null)
        {
            try
            {
                oSession = getSession();
                if (oSession != null)
                {
                    oTransaction = startTransaction(oSession);
                    oSession.delete(oLiftMsgRecord);
                    oTransaction.commit();
                }
                else
                {
                    log.error("Failed to get a session.");
                }
            }
            catch (Exception e)
            {
                if (oTransaction != null)
                {
                    oTransaction.rollback();
                }
                log.error("Error during delete caused by :" + e.getMessage());
            }
            finally
            {
                if (oSession != null)
                {
                    oSession.close();
                }
            }
            log.debug("Completed database record delete");
        }
    }

    /**
     * This method updates the specified row in the gateway_lift_message table.
     *
     * @param oObject An instance of the Hibernate object that is to be saved.
     */
    public void updateRecord(GatewayLiftMsgRecord oRecord)
    {
        if (oRecord != null)
        {
            log.debug("Performing save for GatewayLiftMsgRecord Id: " +
                       oRecord.getId() + "; RequestKeyGuid: " + oRecord.getRequestKeyGuid());
            Session oSession = null;
            Transaction oTransaction = null;
            try
            {
                oSession = getSession();
                if (oSession != null)
                {
                    oTransaction = startTransaction(oSession);
                    oSession.update(oRecord);
                    oTransaction.commit();
                }
                else
                {
                    log.error("Failed to get a session.");
                }
            }
            catch (Exception e)
            {
                if (oTransaction != null)
                {
                    oTransaction.rollback();
                }
                log.error("Error during update caused by: " + e.getMessage());
            }
            finally
            {
                if (oSession != null)
                {
                    oSession.close();
                }
            }

            log.debug("Completed document save for Id: " +
                       oRecord.getId() + "; RequestKeyGuid: " + oRecord.getRequestKeyGuid());
        }
    }

}
