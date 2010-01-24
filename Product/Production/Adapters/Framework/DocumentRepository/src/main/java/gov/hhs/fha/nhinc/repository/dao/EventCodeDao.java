package gov.hhs.fha.nhinc.repository.dao;

import gov.hhs.fha.nhinc.repository.model.Document;
import gov.hhs.fha.nhinc.repository.model.EventCode;
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
 * Data access object class for EventCode data
 * 
 * @author Neil Webb
 */
public class EventCodeDao
{
    Log log = LogFactory.getLog(EventCodeDao.class);

    /**
     * Save an event code record to the database.
     * Insert if event code id is null. Update otherwise.
     * 
     * @param eventCode EventCode record to save.
     */
    public void save(EventCode eventCode)
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
                    sess.saveOrUpdate(eventCode);
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
     * Delete an event code record.
     * 
     * @param eventCode EventCode record to delete.
     */
    public void delete(EventCode eventCode)
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
                    sess.delete(eventCode);

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
     * Retrieve an event code record by identifier.
     * 
     * @param eventCodeId EventCode record identifier.
     * @return EventCode record retrieved from the database.
     */
    public EventCode findById(Long eventCodeId)
    {
        EventCode eventCode = null;

        Session sess = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    eventCode = (EventCode) sess.get(EventCode.class, eventCodeId);
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
        return eventCode;
    }

    /**
     * Retrieves all event codes for a given document
     * 
     * @param document Reference document object
     * @return EventCode list
     */
    @SuppressWarnings("unchecked")
    public List<EventCode> findForDocument(Document document)
    {
        List<EventCode> eventCodes = null;

        Session sess = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(EventCode.class);
                    criteria.add(Restrictions.eq("document", document));
                    eventCodes = criteria.list();
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
        return eventCodes;
    }
}
