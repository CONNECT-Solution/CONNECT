/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.repository.dao;

import gov.hhs.fha.nhinc.repository.model.Document;
import gov.hhs.fha.nhinc.repository.model.ExtraSlot;
import gov.hhs.fha.nhinc.repository.persistence.HibernateUtil;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object class for ExtraSlot data
 * 
 * @author Chrisjan Matser
 */
public class ExtraSlotDao
{
    Log log = LogFactory.getLog(ExtraSlotDao.class);

    /**
     * Save an extra slot record to the database.
     * Insert if extra slot id is null. Update otherwise.
     * 
     * @param extraSlot ExtraSlot record to save.
     */
    public void save(ExtraSlot extraSlot)
    {
        SessionFactory fact = null;
        Session sess = null;
        Transaction trans = null;
        try
        {
            fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.saveOrUpdate(extraSlot);
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                log.error("Session factory was null");
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
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
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
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
    }

    /**
     * Delete an extra slot record.
     * 
     * @param extraSlot ExtraSlot record to delete.
     */
    public void delete(ExtraSlot extraSlot)
    {
        Session sess = null;
        Transaction trans = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.delete(extraSlot);

                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                log.error("Session factory was null");
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
                    log.error("Failed to commit transaction: " + t.getMessage(), t);
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
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
    }

    /**
     * Retrieve an extra slot record by identifier.
     * 
     * @param extraSlotId ExtraSlot record identifier.
     * @return ExtraSlot record retrieved from the database.
     */
    public ExtraSlot findById(Long extraSlotId)
    {
        ExtraSlot extraSlot = null;

        Session sess = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    extraSlot = (ExtraSlot) sess.get(ExtraSlot.class, extraSlotId);
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                log.error("Session factory was null");
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
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return extraSlot;
    }

    /**
     * Retrieves all extra slots for a given document
     * 
     * @param document Reference document object
     * @return ExtraSlot list
     */
    @SuppressWarnings("unchecked")
    public List<ExtraSlot> findForDocument(Document document)
    {
        List<ExtraSlot> extraSlots = null;

        Session sess = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(ExtraSlot.class);
                    criteria.add(Restrictions.eq("document", document));
                    extraSlots = criteria.list();
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                log.error("Session factory was null");
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
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return extraSlots;
    }
}
