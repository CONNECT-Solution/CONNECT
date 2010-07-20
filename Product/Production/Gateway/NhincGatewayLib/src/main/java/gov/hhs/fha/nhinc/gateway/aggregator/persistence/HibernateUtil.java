package gov.hhs.fha.nhinc.gateway.aggregator.persistence;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.properties.HibernateAccessor;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * Utility to obtain hibernate connections.
 * 
 * @author Neil Webb, Les Westberg
 */
public class HibernateUtil
{
    private static Log log = LogFactory.getLog(HibernateUtil.class);
    private static final SessionFactory sessionFactory;
    private static final String HIBERNATE_CONFIG = "aggregator.cfg.xml";

    static
    {
        try
        {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure(HibernateAccessor.getHibernateFile(HIBERNATE_CONFIG)).buildSessionFactory();
        } catch (Throwable ex)
        {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Return the session factory singleton object.
     * 
     * @return The session factory.
     */
    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
    
    /**
     * This method saves the specified data into the tables associatd with 
     * the Hibernate objects 
     * 
     * @param oObject An instance of the Hibernate object that is to be saved.
     * @param sObjectId The ID of the hibernate object.  If this is a new record, then
     *                  leave this field blank.  This field is used for log entry
     *                  purposes.
     * @param sObjectIdFieldName The name of the field that represents the Object's ID.  This
     *                           is used for log entry purposes.
     */
    public static void save(Object oObject, String sObjectId, String sObjectIdFieldName)
    {
        String sLocalObjId = "";
        
        if ((sObjectId == null) ||
            (sObjectId.length() <= 0))
        {
            sLocalObjId = "New record";
        }
        else
        {
            sLocalObjId = sObjectId;
        }
            
        log.debug("Performing save for " + sObjectIdFieldName + ": " + sLocalObjId);
        Session oSession = null;
        Transaction oTransaction = null;
        try
        {
            SessionFactory oSessionFactory = HibernateUtil.getSessionFactory();
            if (oSessionFactory != null)
            {
                oSession = oSessionFactory.openSession();
                if (oSession != null)
                {
                    oTransaction = oSession.beginTransaction();
                    oSession.saveOrUpdate(oObject);
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory while saving " +
                              sObjectIdFieldName + ": " + sLocalObjId);
                }
            }
            else
            {
                log.error("Session factory was null while saving " + sObjectIdFieldName + 
                          ": " + sLocalObjId);
            }
        }
        finally
        {
            if (oTransaction != null)
            {
                try
                {
                    oTransaction.commit();
                }
                catch (Throwable t)
                {
                    log.error("Failed to commit transaction for " + sObjectIdFieldName + ": " + 
                              sLocalObjId + ".  Message: " + t.getMessage(), t);
                }
            }
            if (oSession != null)
            {
                try
                {
                    oSession.close();
                }
                catch (Throwable t)
                {
                    log.error("Failed to close session for " + sObjectIdFieldName + ": " + 
                              sLocalObjId + ".  Message: " + t.getMessage(), t);
                }
            }
        }

        log.debug("Completed document save for " + sObjectIdFieldName + ": " + sLocalObjId);
    }
    
    /**
     * Delete a row in table associated with the specified Hibernate class.
     * 
     * @param object An instance of the specific Hibernate object that is to be deleted.
     * @param sObjectId The ID of the hibernate object.  This field is used for log entry
     *                  purposes.
     * @param sObjectIdFieldName The name of the field that represents the Object's ID.  This
     *                           is used for log entry purposes.
     */
    public static void delete(Object oObject, String sObjectId, String sObjectIdFieldName)
    {
        log.debug("Performing delete for " + sObjectIdFieldName + ": " + sObjectId);

        Session oSession = null;
        Transaction oTransaction = null;
        
        try
        {
            SessionFactory oSessionFactory = HibernateUtil.getSessionFactory();
            if (oSessionFactory != null)
            {
                oSession = oSessionFactory.openSession();
                if (oSession != null)
                {
                    oTransaction = oSession.beginTransaction();
                    oSession.delete(oObject);
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory " +
                              "while deleting " + sObjectIdFieldName + ": " + sObjectId);
                }
            }
            else
            {
                log.error("Session factory was null while attempting to delete " +
                          sObjectIdFieldName + ": " + sObjectId);
            }
        }
        finally
        {
            if (oTransaction != null)
            {
                try
                {
                    oTransaction.commit();
                    
                }
                catch (Throwable t)
                {
                    log.error("Failed to commit transaction for " + sObjectIdFieldName + ": " + 
                              sObjectId + ".  Message: " + t.getMessage(), t);
                }
            }
            if (oSession != null)
            {
                try
                {
                    oSession.close();
                }
                catch (Throwable t)
                {
                    log.error("Failed to close session for " + sObjectIdFieldName + ": " + 
                              sObjectId + ".  Message: " + t.getMessage(), t);
                }
            }
        }

        log.debug("Completed delete for " + sObjectIdFieldName + 
                  ": " + sObjectId);
    }
    
    /**
     * Retrieve a record by identifier  (TransactionId)
     * 
     * @param oClass The class object for the Hibernate data object.
     * @param oObjectId The object ID to be used in the call to find the object.
     * @param sObjectId The ID of the hibernate object.  This field is used for log entry
     *                  purposes.
     * @param sObjectIdFieldName The name of the field that represents the Object's ID.  This
     *                           is used for log entry purposes.
     * @return The retrieved data.  The caller is responsible to cast this to the 
     *         correct type..
     */
    public static Object findById(Class oClass, Serializable oObjectId, String sObjectId, String sObjectIdFieldName)
    {
        log.debug("Performing findById(" + sObjectId + ").");
        Object oObject = null;
        Session oSession = null;
        try
        {
            SessionFactory oSessionFactory = HibernateUtil.getSessionFactory();
            if (oSessionFactory != null)
            {
                oSession = oSessionFactory.openSession();
                if (oSession != null)
                {
                    oObject = oSession.get(oClass, oObjectId);
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory " + 
                              "while retrieving " + sObjectIdFieldName + ":" + sObjectId);
                }
            }
            else
            {
                log.error("Session factory was null while retrieving " + sObjectIdFieldName + 
                          ": " + sObjectId);
            }
            
            if (log.isDebugEnabled())
            {
                log.debug("Completed findById(" + sObjectId + ")" +
                          ". Result was: " + ((oObject == null) ? "not " : "") + "found");
            }
        }
        finally
        {
            if (oSession != null)
            {
                try
                {
                    oSession.close();
                }
                catch (Throwable t)
                {
                    log.error("Failed to close session for " + sObjectIdFieldName + ":" + 
                              sObjectId + ".  Message: " + t.getMessage(), t);
                }
            }
        }
        return oObject;
    }
    
    
}
