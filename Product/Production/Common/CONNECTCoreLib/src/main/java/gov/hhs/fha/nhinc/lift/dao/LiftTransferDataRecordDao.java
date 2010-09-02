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

import gov.hhs.fha.nhinc.lift.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.lift.model.LiftTransferDataRecord;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author vvickers
 */
public class LiftTransferDataRecordDao {

        private Log log = null;

    protected Log getLogger()
    {
        if(log == null)
        {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    protected SessionFactory getSessionFactory()
    {
        HibernateUtil oHibernateUtil = new HibernateUtil();
        return oHibernateUtil.getSessionFactory();
    }

    protected Session getSession(SessionFactory sessionFactory)
    {
        Session session = null;
        if(sessionFactory != null)
        {
            session = sessionFactory.openSession();
        }
        return session;
    }

    /**
     * Save an transfer record to the database.
     * Insert if transfer record id is null. Update otherwise.
     *
     * @param transferRecord LiftTransferDataRecord record to save.
     */
    public void save(LiftTransferDataRecord transferRecord)
    {
        SessionFactory fact = null;
        Session sess = null;
        Transaction trans = null;
        try
        {
            fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.saveOrUpdate(transferRecord);
                }
                else
                {
                    getLogger().error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                getLogger().error("Session factory was null");
            }
        }
        finally
        {
            if (trans != null)
            {
                try
                {
                    trans.commit();
                }
                catch (Throwable t)
                {
                    getLogger().error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null)
            {
                try
                {
                    sess.close();
                }
                catch (Throwable t)
                {
                    getLogger().error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
    }

    /**
     * Delete a lift transfer data record.
     *
     * @param transferRecord LiftTransferData record to delete.
     */
    public void delete(LiftTransferDataRecord transferRecord)
    {
        Session sess = null;
        Transaction trans = null;
        try
        {
            SessionFactory fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.delete(transferRecord);
                }
                else
                {
                    getLogger().error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                getLogger().error("Session factory was null");
            }
        }
        finally
        {
            if (trans != null)
            {
                try
                {
                    trans.commit();
                }
                catch (Throwable t)
                {
                    getLogger().error("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null)
            {
                try
                {
                    sess.close();
                }
                catch (Throwable t)
                {
                    getLogger().error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
    }
        /**
     * Retrieves all event codes for a given document
     *
     * @param document Reference document object
     * @return EventCode list
     */
    @SuppressWarnings("unchecked")
    public List<LiftTransferDataRecord> findForGuid(String guid)
    {
        List<LiftTransferDataRecord> transferRecords = null;

        Session sess = null;
        try
        {
            SessionFactory fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(LiftTransferDataRecord.class);
                    criteria.add(Restrictions.eq("RequestKeyGuid", guid));
                    transferRecords = criteria.list();
                }
                else
                {
                    getLogger().error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                getLogger().error("Session factory was null");
            }
        }
        finally
        {
            if (sess != null)
            {
                try
                {
                    sess.close();
                }
                catch (Throwable t)
                {
                    getLogger().error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return transferRecords;
    }

    /**
     * This method updates the specified row in the gateway_lift_message table.
     *
     * @param oObject An instance of the Hibernate object that is to be saved.
     */
    public void updateRecord(LiftTransferDataRecord rec)
    {
        if (rec != null)
        {
            getLogger().debug("Performing save for GatewayLiftMsgRecord Id: " +
                       rec.getId() + "; RequestKeyGuid: " + rec.getRequestKeyGuid());
            Session sess = null;
            Transaction oTransaction = null;
            SessionFactory fact = getSessionFactory();
            try
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    oTransaction = sess.beginTransaction();
                    sess.update(rec);
                    oTransaction.commit();
                }
                else
                {
                    getLogger().error("Failed to get a session.");
                }
            }
            catch (Exception e)
            {
                if (oTransaction != null)
                {
                    oTransaction.rollback();
                }
                getLogger().error("Error during update caused by: " + e.getMessage());
            }
            finally
            {
                if (sess != null)
                {
                    sess.close();
                }
            }

            getLogger().debug("Completed document save for Id: " +
                       rec.getId() + "; RequestKeyGuid: " + rec.getRequestKeyGuid());
        }
    }

}
