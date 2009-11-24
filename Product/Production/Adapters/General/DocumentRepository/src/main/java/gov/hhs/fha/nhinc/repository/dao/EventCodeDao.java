package gov.hhs.fha.nhinc.repository.dao;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.repository.model.Document;
import gov.hhs.fha.nhinc.repository.model.EventCode;
import gov.hhs.fha.nhinc.repository.model.EventCodeParam;
import gov.hhs.fha.nhinc.repository.persistence.HibernateUtil;
import java.util.ArrayList;
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
        return HibernateUtil.getSessionFactory();
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
            fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.saveOrUpdate(eventCode);
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
            SessionFactory fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.delete(eventCode);
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
            SessionFactory fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    eventCode = (EventCode) sess.get(EventCode.class, eventCodeId);
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
            SessionFactory fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(EventCode.class);
                    criteria.add(Restrictions.eq("document", document));
                    eventCodes = criteria.list();
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
        return eventCodes;
    }

    /**
     * Retrieves all event codes for a given event code value
     *
     * @param eventCodeParam Event code query parameter
     * @return EventCode list
     */
    @SuppressWarnings("unchecked")
    public List<EventCode> eventCodeQuery(EventCodeParam eventCodeParam)
    {
        List<EventCode> eventCodes = null;

        Session sess = null;
        try
        {
            SessionFactory fact = getSessionFactory();
            if (fact != null)
            {
                sess = getSession(fact);
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(EventCode.class);
                    boolean performQuery = false;
                    if((eventCodeParam != null) && NullChecker.isNotNullish(eventCodeParam.getEventCode()))
                    {
                        criteria.add(Restrictions.eq("eventCode", eventCodeParam.getEventCode()));
                        performQuery = true;
                    }
                    if(NullChecker.isNotNullish(eventCodeParam.getEventCodeScheme()))
                    {
                        criteria.add(Restrictions.eq("eventCodeScheme", eventCodeParam.getEventCodeScheme()));
                        performQuery = true;
                    }

                    if(performQuery)
                    {
                        eventCodes = criteria.list();
                    }
                    else
                    {
                        eventCodes = new ArrayList<EventCode>();
                    }
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
        return eventCodes;
    }
}
